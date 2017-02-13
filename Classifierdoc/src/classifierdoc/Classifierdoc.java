/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package classifierdoc;

/**
 *
 * @author Lucas
 */
import Jama.Matrix;
import clustering.Cluster;
import clustering.ClusterBag;
import clustering.Clustering;
import extractor.Document;
import extractor.Extractor;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import lsi.LSI;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

public class Classifierdoc {

    public static void main(String[] args) throws IOException {
        LSI lsiObj = null;

        lsiObj = new LSI("C:\\estudo2", "stopwords.txt");

        lsiObj.createTermDocumentMatrix();
        lsiObj.performSingularValueDecomposition();
        Matrix s = lsiObj.calculoDeSimilaridade();
        ArrayList<String> documents = lsiObj.getListaDeDocumentos();
        
        ArrayList<Integer> docs = new ArrayList<>();
        
        System.out.print("\t");
        for (int i = 0; i < documents.size(); i++) {
            System.out.print(documents.get(i) + "\t");
            docs.add(i + 1);            
            
        }
        
        System.out.println("");
        
        for (int i = 0; i < documents.size(); i++) {
            System.out.print(documents.get(i) + "\t");            
            for (int j = 0; j < documents.size(); j++) {
                System.out.print(s.get(i, j) + "\t");
            }
            System.out.println("");
        }
        
        System.out.println(docs.toString());
        
//        for (Map.Entry<String, Double> entry : s.entrySet()) {
//            String key = entry.getKey();
//            Double value = entry.getValue();            
//            System.out.println(key + " \t " + value);
//            
//        }

        System.out.println("T: " + docs.size());
        
        Clustering c = new Clustering(docs);        
        Cluster.setMatrixSimilarity(s);
        ClusterBag cb = c.generate();
        
        cb.show(documents);        
        
        System.out.println("\nThank you for trying out the system.");
    }

}
