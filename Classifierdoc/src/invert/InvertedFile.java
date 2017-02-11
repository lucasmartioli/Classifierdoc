/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Lucas
 */
package invert;

import extractor.Document;
import extractor.Extractor;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.TreeMap;

public class InvertedFile {

    String dirPath, stopListPath;
    ArrayList<String> stopWordList = new ArrayList<String>();
    TreeMap<String, Integer> wordList = new TreeMap<String, Integer>();
    TreeMap<String, TreeMap<String, Integer>> bagOfWords = new TreeMap<String, TreeMap<String, Integer>>();
    TreeMap<String, Integer> documentLength = new TreeMap<String, Integer>();
    int contextWords = 15;
    int totalDocuments = 0;
    public static final String whiteSpacePattern = "\\s";

    public InvertedFile(String directory, String stoplist) {
        dirPath = directory;
        stopListPath = stoplist;
    }

    public void createStopList() throws IOException {
        FileInputStream fstream = null;
        String line;
        try {
            fstream = new FileInputStream(stopListPath);
        } catch (FileNotFoundException e) {
            System.out.println("O arquivo de palavras de parada não existe!");
        }

        DataInputStream in = new DataInputStream(fstream);
        BufferedReader bffr = new BufferedReader(new InputStreamReader(in));

        while ((line = bffr.readLine()) != null) {
            stopWordList.add(line.trim().toLowerCase());
        }
    }

   
    public boolean isStopListWord(String word) {
        if (stopWordList.contains(word.toLowerCase())) {
            return true;
        }

        return false;
    }

    public boolean isTermOnWordList(String term) {
        if (wordList.containsKey(term.toLowerCase())) {
            return true;
        }

        return false;
    }

    public boolean isTermInDocument(String term, String document) {
        TreeMap<String, Integer> documentList = bagOfWords.get(term.toLowerCase());

        if (documentList.containsKey(document)) {
            return true;
        }

        return false;
    }

   
    public int getNumberOfTerms() {
        return wordList.size();
    }

    public int getNumberOfDocuments() {
        return totalDocuments;
    }

    public TreeMap<String, Integer> getWordList() {
        return wordList;
    }

    public double[][] getTermMatrixValues() {
        double[][] matrixTerms = new double[getNumberOfTerms()][getNumberOfDocuments()];
        int i, j;

        i = 0;
        for (String term : wordList.keySet()) {
            j = 0;
            for (String document : documentLength.keySet()) {
                matrixTerms[i][j] = getTFInDocument(term, document) * getIDF(term);
                j++;
            }
            i++;
        }

        return matrixTerms;
    }
    
    public void iterateOverDirectory() throws IOException {
        File dir = new File(dirPath);
        for (File file : dir.listFiles()) {
            if (file.getName().equals(".")
                    || file.getName().equals("..")
                    || file.isHidden()
                    || file.isDirectory()) {
                continue;
            }
            System.out.println(file.getName());

            parseFile(file.getName());
            totalDocuments++;
        }
    }

    private void parseFile(String fileName) throws IOException {        
        String word;
        String[] wordsInLine;
        TreeMap<String, Integer> wordFrequence = null;
        int wordCounter = 0, i;

        String[] files = {fileName};
        ArrayList<Document> d = Extractor.returnDocuments("C:\\estudo2\\", files);
        
        if (d.size() <= 0)
            return;

        wordsInLine = d.get(0).getContent().split(whiteSpacePattern);
        for (i = 0; i < wordsInLine.length; i++) {
            word = wordsInLine[i].toLowerCase();
            word = word.replaceAll("[^a-z]", "").trim();
            
            if (word.isEmpty())
                continue;

            if (isStopListWord(word)) {
                wordCounter += 1;
                continue;
            }
            if (wordList.containsKey(word)) {
                wordFrequence = bagOfWords.get(word);
                if (wordFrequence.containsKey(fileName)) {
                    wordFrequence.put(fileName, wordFrequence.get(fileName) + 1);
                } else {
                    wordFrequence.put(fileName, 1);
                    wordList.put(word, wordList.get(word) + 1);
                }
            } else {
                wordList.put(word, 1);
                wordFrequence = new TreeMap<>();
                wordFrequence.put(fileName, 1);
                bagOfWords.put(word, wordFrequence);
            }
            wordCounter += 1;
        }

        documentLength.put(fileName, wordCounter);
    }

    /**
     *
     * @param term
     * @return Number of documents a particular term appears in
     */
    private int getDocumentFrequency(String term) {
        if (wordList.get(term.toLowerCase()) != null) {
            return wordList.get(term.toLowerCase());
        }

        return 0;
    }

    /**
     *
     * @param term
     * @return Inverse document frequency for the term
     */
    public double getIDF(String term) {
        int termFreq = getDocumentFrequency(term.toLowerCase());
        if (termFreq != 0) {
            return (double) (1 + Math.log10(totalDocuments) - Math.log10(termFreq));
        }

        return (Double) null;
    }

    public double getTFInDocument(String term, String document) {
        if (isTermInDocument(term.toLowerCase(), document)) {
            int termAppearance = bagOfWords.get(term.toLowerCase()).get(document);
            return (1 + Math.log10(termAppearance));
        }

        return 0;
    }

    public ArrayList<String> getDocumentList() {
        ArrayList<String> documentList = new ArrayList<String>();

        for (String document : documentLength.keySet()) {
            documentList.add(document);
        }

        return documentList;
    }
}
