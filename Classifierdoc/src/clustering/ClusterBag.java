/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clustering;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;

/**
 *
 * @author Lucas
 */
public class ClusterBag {

    public ArrayList<Cluster> clusterBag;
    double bagValue = 0;

    public ClusterBag() {
        clusterBag = new ArrayList<>();
    }

    public void show() {

        System.out.println("Taxa cluster bag: " + bagValue);
        int i = 1;
        for (Cluster cluster : clusterBag) {

            System.out.print("Cluster" + i + ": ");
            cluster.show();
            System.out.println("");
            i++;
        }
    }

    public void show(ArrayList<String> documents) {

        System.out.println("Taxa cluster bag: " + bagValue);
        int i = 1;

        for (Cluster cluster : clusterBag) {

            System.out.print("Cluster" + i + ": ");
            cluster.show(documents);
            System.out.println("");
            i++;
        }
    }

    double calculate() {

        bagValue = 0;
        for (Cluster cluster : clusterBag) {
            bagValue += cluster.value;
        }

        return bagValue;
    }

    void generateRandom(ArrayList<Integer> docs) {

        Random gerador = new Random(Calendar.getInstance().getTimeInMillis());
//        gerador.

        int kclusters = gerador.nextInt(docs.size() - Clustering.minClusters) + Clustering.minClusters;
        int max = Math.round(docs.size() / kclusters);

        for (int i = 0; i < kclusters; i++) {
            Cluster cluster = new Cluster();
            clusterBag.add(cluster);

            while (docs.size() > 0) {
                int n = 0;
                if (docs.size() > 1) {
                    n = gerador.nextInt(docs.size() - 1);
                }

                cluster.add(docs.get(n));
                docs.remove(n);

                if (i != kclusters - 1 && cluster.size() >= max) {
                    break;
                }
            }

        }

        calculate();

    }

    void add(Cluster c) {
        clusterBag.add(c);
        calculate();
    }

    int size() {
        return clusterBag.size();
    }

    boolean isSolution() {
        for (Cluster cluster : clusterBag) {
            if (cluster.docs.size() == 0)
                return false;
        }
        
        return bagValue > 0;        
    }

}
