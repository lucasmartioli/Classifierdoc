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
public class Clustering {

    private final ArrayList<Integer> docs;
    public static final int minClusters = 1;
    private ArrayList<ClusterBag> population;
    private final int populationSize = 900;
    private final int maxInteractions = 10000;
    private final int sizeGeneration = 1000;

    public Clustering(ArrayList<Integer> docs) {
        this.docs = docs;
    }

    public double objectiveFunction(ClusterBag cb) {
        return cb.bagValue;

    }

    private ArrayList<Integer> cloneDocs() {
        ArrayList<Integer> d = new ArrayList<>();
        for (Integer integer : docs) {
            d.add(integer);
        }
        return d;
    }

    private void intializePopulation() {
        population = new ArrayList<>();
        for (int i = 0; i < populationSize; i++) {
            ClusterBag cluster = new ClusterBag();
            cluster.generateRandom(cloneDocs());
            //cluster.show();
            if (cluster.isSolution()) {
                population.add(cluster);
            } else {
                i--;
            }
                
        }

    }

    public ClusterBag generate() {
        intializePopulation();
        ClusterBag bestClusterBag = population.get(1);

        for (int i = 0; i < maxInteractions; i++) {
            ClusterBag bestSolutionInPopulation = findBestSolutionInPopulation();
            if (objectiveFunction(bestClusterBag) < objectiveFunction(bestSolutionInPopulation)) {
                bestClusterBag = bestSolutionInPopulation;
            }

            generateNewPopulation();

        }

        return bestClusterBag;
    }

    private ClusterBag findBestSolutionInPopulation() {
        ClusterBag bestSolutionInPopulation;

        bestSolutionInPopulation = population.get(1);
        for (int i = 0; i < population.size(); i++) {
            if (objectiveFunction(population.get(i)) > objectiveFunction(bestSolutionInPopulation)) {
                bestSolutionInPopulation = population.get(i);
            }
        }

        return bestSolutionInPopulation;
    }

    private void generateNewPopulation() {

        ClusterBag best = findBestSolutionInPopulation();

        Random gerador = new Random(Calendar.getInstance().getTimeInMillis());

        ArrayList<ClusterBag> generation = new ArrayList<>();

        for (int i = 0; i < sizeGeneration; i++) {
            generation.add(crossOver(best, population.get(gerador.nextInt((population.size()/2) - 1)), population.get(gerador.nextInt(population.size() - (population.size()/2) - 1))));
        }

        for (ClusterBag son : generation) {
            if (son.isSolution())             
                this.addInPopulationElitism(son);
        }

    }

    private ClusterBag crossOver(ClusterBag cb1, ClusterBag cb2, ClusterBag cb3) {

        ClusterBag cb4 = new ClusterBag();

        Random gerador = new Random(Calendar.getInstance().getTimeInMillis());

        ArrayList<Integer> d = cloneDocs();

        Cluster bestCluster = returnBestCluster(cb1);
        cb4.add(bestCluster);

        for (Integer integer : bestCluster.docs) {
            d.remove(d.indexOf(integer));
        }

        Cluster bestCluster2 = returnBestCluster(cb2);
        Cluster nCluster = new Cluster();

        for (Integer integer : bestCluster2.docs) {
            if (d.indexOf(integer) != -1) {
                nCluster.add(integer);
                d.remove(d.indexOf(integer));
            }
        }

        if (nCluster.size() != 0)
            cb4.add(nCluster);
        
        bestCluster2 = returnBestCluster(cb3);
        nCluster = new Cluster();

        for (Integer integer : bestCluster2.docs) {
            if (d.indexOf(integer) != -1) {
                nCluster.add(integer);
                d.remove(d.indexOf(integer));
            }
        }

        if (nCluster.size() != 0)
            cb4.add(nCluster);

        if (d.size() > 0) {
            int kclusters = gerador.nextInt(d.size());
            if (kclusters == 0)
                kclusters = 1;
            
            int max = d.size() / kclusters;

            for (int i = 0; i < kclusters; i++) {
                Cluster cluster = new Cluster();
                cb4.add(cluster);

                while (d.size() > 0) {
                    int n = 0;
                    if (d.size() > 1) {
                        n = gerador.nextInt(d.size() - 1);
                    }
                    cluster.add(d.get(n));
                    d.remove(n);

                    if (i != kclusters - 1 && cluster.size() >= max) {
                        break;
                    }
                }

            }
        }

        cb4.calculate();

        return cb4;
    }

    Cluster returnBestCluster(ClusterBag cb) {
        Cluster bestCluster = new Cluster();

        for (Cluster c : cb.clusterBag) {
            if (bestCluster.value < c.value) {
                bestCluster.clone(c);
            }
        }

        return bestCluster;
    }

    private void addInPopulationElitism(ClusterBag son) {
        for (int i = 0; i < population.size(); i++) {
            if (objectiveFunction(son) > objectiveFunction(population.get(i))) {
                population.set(i, son);
                break;
            }
        }
    }

}
