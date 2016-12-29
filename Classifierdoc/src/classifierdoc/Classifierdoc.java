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
import extractor.Document;
import extractor.Extractor;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

public class Classifierdoc {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        String[] files = new String[350];
        for (int i = 0; i < files.length; i++) {
            files[i] = i + ".pdf";            
        }

        //Passar o caminho base por parametro e os arquivos!!
        ArrayList<Document> documents = Extractor.returnDocuments("c:/estudo/", files);

        for (Document document : documents) {
            System.out.print(document.getContent());
        }

    }

}
