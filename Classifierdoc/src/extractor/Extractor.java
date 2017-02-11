/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package extractor;

import classifierdoc.Classifierdoc;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;


/**
 *
 * @author Lucas
 */
public class Extractor {

    public static ArrayList<Document> returnDocuments(String pathBase, String[] files) {

        ArrayList<Document> documents = new ArrayList<>();

        for (String file : files) {
            PDDocument pdDocument = null;
            String paperString = null;
            try {
                pdDocument = PDDocument.load(new File(pathBase + file));
                paperString = new PDFTextStripper().getText(pdDocument);
                pdDocument.close();
                Document document = new Document(paperString);
                documents.add(document);

            } catch (FileNotFoundException ex) {
                System.out.println("Arquivo não encontrado! Detalhes: " + ex.getLocalizedMessage());
                continue;
            } catch (IOException ex) {
                Logger.getLogger(Classifierdoc.class.getName()).log(Level.SEVERE, null, ex);

            }
        }

        return documents;
    }

}
