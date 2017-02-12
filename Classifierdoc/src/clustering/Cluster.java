/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clustering;

import Jama.Matrix;
import java.util.ArrayList;

/**
 *
 * @author Lucas
 */
public class Cluster {

    private static Matrix similarity;
    public ArrayList<Integer> docs;
    double value = 0;

    public Cluster() {
        docs = new ArrayList<>();
    }

    public static void setMatrixSimilarity(Matrix sm) {
        similarity = sm;
    }

    void calculate() {
        value = 0;
        for (int i = 0; i < docs.size(); i++) {
            for (int j = i + 1; j < docs.size(); j++) {
                value += similarity.get(i, j);
            }
        }
    }

    void add(Integer doc) {
        docs.add(doc);
        calculate();
    }

    int size() {
        return docs.size();
    }

    void clone(Cluster c1) {
        c1.docs.stream().forEach((doc) -> {
            add(doc);
        });        
    }

    void show() {
        System.out.print("Taxa: " + value + " documentos: ");
        for (Integer doc : docs) {
            System.out.print(doc + " ");
        }
        
    }
    
    void show(ArrayList<String> documents) {
        System.out.print("Taxa: " + value + " documentos: ");
        for (Integer doc : docs) {
            System.out.print(documents.get(doc - 1) + " ");
        }
        
    }

}
