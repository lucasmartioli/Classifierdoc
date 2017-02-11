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
        HashMap<String, Double> s = lsiObj.calculoDeSimilaridade();
        ArrayList<String> documents = lsiObj.getListaDeDocumentos();
        
        for (Map.Entry<String, Double> entry : s.entrySet()) {
            String key = entry.getKey();
            Double value = entry.getValue();            
            System.out.println(key + " \t " + value);
            
        }
      

        System.out.println("\nThank you for trying out the system.");
    }

}
