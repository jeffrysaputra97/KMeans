/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kmeans;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Jeffry
 */
public class KMeans {

    /**
     * @param args the command line arguments
     */
    public static Double euclideanDistance(Double[] data, Double[] centroid) {
        Double d = new Double(0);
        for (int j = 0; j < data.length; j++) {
            d += Math.pow(data[j] - centroid[j], 2);
        }
        return Math.sqrt(d);
    }

    public static int min(Double[] data, ArrayList<Double[]> centroid) {
        int minIndex = 0;
        Double[] distance = new Double[centroid.size()];
        
        for (int i = 0; i < distance.length; i++) {
            distance[i] = euclideanDistance(data, centroid.get(i));
        }

        for (int i = 0; i < distance.length; i++) {
            if (distance[i] < distance[minIndex]) {
                minIndex = i;
            }
        }

        return minIndex;
    }
    
    public static void addArrayToArrayList (Double[] a, ArrayList<Double[]> b) {
        Double[] copy = new Double[a.length];
        
        for (int i = 0; i < a.length; i++) {
            copy[i] = a[i];
        }
        
        b.add(copy);
    }
    
    public static void addArrayListToArrayListofArrayList(ArrayList<Double[]> a, ArrayList<ArrayList<Double[]>> b) {
        ArrayList<Double[]> copyArrList = new ArrayList<>();
                
        for (Double[] doubles : a) {
            Double[] copy = new Double[doubles.length];

            for (int i = 0; i < doubles.length; i++) {
                copy[i] = doubles[i];
            }

            copyArrList.add(copy);
        }

        b.add(copyArrList);
    }

    public static void main(String[] args) {
        // TODO code application logic here
        int clusterNum, data, attribute;
        boolean change = true, firstIteration = true;

        Scanner scan = new Scanner(System.in);
        ArrayList<Double[]> temp = new ArrayList();
        ArrayList<Double[]> centroids = new ArrayList();
        ArrayList<ArrayList<Double[]>> cluster = new ArrayList();
        ArrayList<ArrayList<Double[]>> before = null;
        Random rand = new Random();

        System.out.print("Masukkan jumlah cluster yang diinginkan: ");
        clusterNum = scan.nextInt();

        System.out.print("Masukkan jumlah data yang diinginkan: ");
        data = scan.nextInt();

        System.out.print("Masukkan jumlah atribut untuk setiap data: ");
        attribute = scan.nextInt();

        Double[][] dataset = new Double[data][attribute];

        /*
            Looping input
         */
//        for (int i = 0; i < data; i++) {
//            for (int j = 0; j < attribute; j++) {
//                System.out.print("Masukkan dataset ke-" + i + " atribut ke-" + j + ": ");
//                dataset[i][j] = scan.nextDouble();
//            }
//            temp.add(dataset[i]);
//        }

        try {
                BufferedReader buffReader = new BufferedReader(new FileReader("D:\\[DariBackup]\\Dokumen\\ITHB\\Kapsel\\KMeans\\src\\kmeans\\input.txt"));
                String buffer = buffReader.readLine();
                int i = 0;
                
                while (buffer != null) {
                    String[] input = buffer.split(" ");
                    int j = 0;
                    
                    for (String string : input) {
                        dataset[i][j] = Double.parseDouble(string);
                        j++;
                    }
                    
                    addArrayToArrayList(dataset[i], temp);
                    
                    buffer = buffReader.readLine();
                    i++;
                }
                
            } catch (IOException ex) {
                Logger.getLogger(KMeans.class.getName()).log(Level.SEVERE, null, ex);
            }

        System.out.println("\nData: ");

        for (int i = 0; i < dataset.length; i++) {
            for (int j = 0; j < dataset[0].length; j++) {
                System.out.print(dataset[i][j] + "  ");
            }
            System.out.println("");
        }
        
        /*
            Looping untuk cari centroid random di awal
         */
        for (int i = 0; i < clusterNum; i++) {
            int randIndex = rand.nextInt(temp.size());
            ArrayList<Double[]> pointer = new ArrayList<>();
            addArrayToArrayList(temp.get(randIndex), centroids);
            addArrayToArrayList(temp.get(randIndex), pointer);
            cluster.add(pointer);
            temp.remove(randIndex);
        }

        System.out.println("\nCentroids: ");

        for (int i = 0; i < cluster.size(); i++) {
            System.out.println("Cluster " + i);
            for (int j = 0; j < cluster.get(i).size(); j++) {
                for (int k = 0; k < cluster.get(i).get(j).length; k++) {
                    System.out.print(cluster.get(i).get(j)[k] + "  ");
                }
                System.out.println();
            }
            System.out.println();
        }
//        System.out.println(temp.get(0)[0] + " a " + temp.get(0)[1]);
//        System.out.println(cluster.get(0).get(0)[0] + " b " + cluster.get(0).get(0)[1]);

        do {
            if (firstIteration) {
                for (int i = 0; i < temp.size(); i++) {
                    int clusterIndex = min(temp.get(i), centroids);
                    addArrayToArrayList(temp.get(i), cluster.get(clusterIndex));
                }
            } else {
                cluster.removeAll(cluster);
                
                for (int i = 0; i < clusterNum; i++) {
                    cluster.add(new ArrayList<>());
                }
                
                for (int i = 0; i < dataset.length; i++) {
                    int clusterIndex = min(dataset[i], centroids);
                    addArrayToArrayList(dataset[i], cluster.get(clusterIndex));
                }
            }

            System.out.println("\nCluster Results: ");

            for (int i = 0; i < cluster.size(); i++) {
                System.out.println("Cluster " + i);
                for (int j = 0; j < cluster.get(i).size(); j++) {
                    for (int k = 0; k < cluster.get(i).get(j).length; k++) {
                        System.out.print(cluster.get(i).get(j)[k] + "  ");
                    }
                    System.out.println();
                }
                System.out.println();
            }

            if (before == null) {
                before = new ArrayList();
                for (int i = 0; i < cluster.size(); i++) {
                    addArrayListToArrayListofArrayList(cluster.get(i), before);
                }

                System.out.println("\nBefore Cluster Results: ");

                for (int i = 0; i < before.size(); i++) {
                    System.out.println("Cluster " + i);
                    for (int j = 0; j < before.get(i).size(); j++) {
                        for (int k = 0; k < before.get(i).get(j).length; k++) {
                            System.out.print(before.get(i).get(j)[k] + "  ");
                        }
                        System.out.println();
                    }
                    System.out.println();
                }
            }
            for (int i = 0; i < cluster.size(); i++) {
                if (cluster.get(i).size() == before.get(i).size() && !firstIteration) {
                    change = false;
                    int j = 0;
                    int k = 0;
                    while (!change && j < cluster.get(i).size()) {
                        while (k < cluster.get(i).get(j).length) {
                            if (cluster.get(i).get(j)[k] != before.get(i).get(j)[k]) {
                                change = true;
                            }
                            k++;
                        }
                        j++;
                    }
                }
            }
            
            if (change) {
                for (int j = 0; j < cluster.size(); j++) {
                    for (int k = 0; k < attribute; k++) {
                        Double mean = new Double(0);
                        for (int l = 0; l < cluster.get(j).size(); l++) {
                            mean += cluster.get(j).get(l)[k];
                        }
                        centroids.get(j)[k] = mean / cluster.get(j).size();
                    }
                }

                System.out.println("New Centroid: ");

                for (int j = 0; j < centroids.size(); j++) {
                    for (int k = 0; k < centroids.get(j).length; k++) {
                        System.out.printf("%.3f ", centroids.get(j)[k]);
                    }
                    System.out.println();
                }
            }
            
            for (ArrayList<Double[]> arrayList : cluster) {
                addArrayListToArrayListofArrayList(arrayList, before);
            }
            
            firstIteration = false;
        } while (change);

    }
}
