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
    public static final int minClusters = 2;
    private ArrayList<ClusterBag> population;
    private final int populationSize = 300;
    private final int maxInteractions = 5000;
    private final int sizeGeneration = 1000;

    public Clustering(ArrayList<Integer> docs) {
        this.docs = docs;
    }

    public double objectiveFunction(ClusterBag cb) {
        return cb.bagValue * cb.size();

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
            population.add(cluster);
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
            generation.add(crossOver(best, population.get(gerador.nextInt(population.size() - 1))));
        }

        for (ClusterBag son : generation) {
            if (son.isSolution())             
                this.addInPopulationElitism(son);
        }

    }

    private ClusterBag crossOver(ClusterBag cb1, ClusterBag cb2) {

        ClusterBag cb3 = new ClusterBag();

        Random gerador = new Random(Calendar.getInstance().getTimeInMillis());

        ArrayList<Integer> d = cloneDocs();

        Cluster bestCluster = returnBestCluster(cb1);
        cb3.add(bestCluster);

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

        cb3.add(nCluster);
//
//        boolean sucess = false;
//        while (!sucess) {
//            if (bestCluster.docs.size() == 0) {
//                break;
//            }
//
//            for (Integer integer : bestCluster.docs) {
//                if (d.indexOf(integer) < 0 && bestCluster.docs.size() > 0) {
//                    bestCluster.docs.remove(integer);
//                    sucess = false;
//                    break;
//                } else if (bestCluster.docs.size() > 0) {
//                    sucess = true;
//                    d.remove(d.indexOf(integer));
//                } else {
//                    sucess = true;
//                }
//            }
//        }

//        bestCluster.show();
        if (d.size() > 0) {
            int kclusters = gerador.nextInt(d.size() - Clustering.minClusters) + Clustering.minClusters;
            int max = d.size() / kclusters;

            for (int i = 0; i < kclusters; i++) {
                Cluster cluster = new Cluster();
                cb3.add(cluster);

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

        cb3.calculate();

        return cb3;
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
