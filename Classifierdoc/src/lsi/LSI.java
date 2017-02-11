package lsi;

import invert.InvertedFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.TreeMap;

import Jama.Matrix;
import Jama.SingularValueDecomposition;
import java.util.HashMap;
import java.util.HashSet;

public class LSI {

    InvertedFile invObj;
    SingularValueDecomposition svd;
    Matrix TD, leftSingularMatrix, rightSingularMatrix, singularValueMatrix;
    Matrix similarityValues;
    boolean eligibleQuery = false;
    int relevantDocs = 5;
    int k;

    public LSI(String dirPath, String stoplistPath) throws IOException {
        invObj = new InvertedFile(dirPath, stoplistPath);
        invObj.createStopList();
        invObj.iterateOverDirectory();
        k = -1;
    }

    public ArrayList<String> getListaDeDocumentos() {
        return invObj.getDocumentList();
    }

    public void createTermDocumentMatrix() {
        TD = new Matrix(invObj.getTermMatrixValues());
    }

    private void prepareMatrices() {
        for (int i = 0; i < leftSingularMatrix.getRowDimension(); i++) {
            for (int j = k; j < leftSingularMatrix.getColumnDimension(); j++) {
                leftSingularMatrix.set(i, j, 0);
            }
        }

        for (int i = k; i < singularValueMatrix.getRowDimension(); i++) {
            for (int j = k; j < singularValueMatrix.getColumnDimension(); j++) {
                singularValueMatrix.set(i, j, 0);
            }
        }

        for (int i = 0; i < rightSingularMatrix.getRowDimension(); i++) {
            for (int j = k; j < rightSingularMatrix.getColumnDimension(); j++) {
                rightSingularMatrix.set(i, j, 0);
            }
        }
    }

    public void performSingularValueDecomposition() {
        svd = new SingularValueDecomposition(TD);
        leftSingularMatrix = svd.getU();
        singularValueMatrix = svd.getS();
        rightSingularMatrix = svd.getV();

        if (k == -1) {
            k = leftSingularMatrix.getColumnDimension();
        }

        if (k >= 0 && k < leftSingularMatrix.getColumnDimension()) {
            prepareMatrices();
        }
    }

    public double getVectorModulus(Matrix matrix) {
        double product = 0;

        for (int i = 0; i < matrix.getRowDimension(); i++) {
            product += matrix.get(i, 0) * matrix.get(i, 0);
        }

        product = Math.sqrt(product);
        return product;
    }

    private double getModulus(Matrix matrix) {
        return (getVectorModulus(leftSingularMatrix.transpose().times(matrix)));
    }

    private Matrix getDocumentColumnMatrix(int columnNumber) {
        Matrix columnMatrix = new Matrix(rightSingularMatrix.getColumnDimension(), 1);
        Matrix resultMatrix;
        for (int i = 0; i < columnMatrix.getRowDimension(); i++) {
            columnMatrix.set(i, 0, rightSingularMatrix.get(columnNumber, i));
        }

        resultMatrix = leftSingularMatrix.times(singularValueMatrix);
        resultMatrix = resultMatrix.times(columnMatrix);

        return resultMatrix;
    }

    private double getSimilarity(Matrix doc1, Matrix doc2) {
        Matrix productMatrix;
        double denominator, similarity;

        productMatrix = (doc1.transpose()).times(doc2);
        denominator = getModulus(doc1) * getModulus(doc2);

        similarity = productMatrix.det() / denominator;
        return similarity;
    }

    public HashMap<String, Double> calculoDeSimilaridade() {
        ArrayList<String> documents = invObj.getDocumentList();
        HashMap<String, Double> hashDocumentsCombine = new HashMap<>();                
        Matrix documentMatrix1;
        Matrix documentMatrix2;

        for (int lineNumber = 0; lineNumber < documents.size(); lineNumber++) {
            documentMatrix1 = getDocumentColumnMatrix(lineNumber);
            for (int columnNumber = 0 + lineNumber + 1; columnNumber < documents.size(); columnNumber++) {
                documentMatrix2 = getDocumentColumnMatrix(columnNumber);
                hashDocumentsCombine.put(documents.get(lineNumber) + " x " + documents.get(columnNumber), getSimilarity(documentMatrix1, documentMatrix2));                              
            }

        }

        return hashDocumentsCombine;
    }

}
