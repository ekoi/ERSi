package nl.knaw.dans.ersy.textmining.utils;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.SequenceFile;
import org.apache.mahout.clustering.Cluster;
import org.apache.mahout.clustering.classify.WeightedVectorWritable;
import org.apache.mahout.math.NamedVector;

/**
 * Hello world!
 *
 */
public class HumanReadableClusterDumper 
{
    public static void main( String[] args ) throws IOException
    {
    	Configuration conf = new Configuration();
        FileSystem fs = FileSystem.get(conf);
        ///Users/akmi/zzz-test-20130126-2308/output-vectors/kmeans-output/clusteredPoints
        ///Volumes/Holdtank/clustering-results/canopy/20130205C-1206/clusters-output/canopy-centroids/clusters-0-final
        String outputDir = "/Volumes/Holdtank/clustering-results/canopy/20130210All-2137/clusters-output/clusters";
    	  Path vectorsFolder = new Path(outputDir, "tfidf-vectors");
    	 //Path clusterOutput = new Path(outputDir , "clusters");
        System.out.println( "Hello World!" );
        SequenceFile.Reader reader = new SequenceFile.Reader(fs,
                new Path(outputDir + "/" + Cluster.CLUSTERED_POINTS_DIR + "/part-m-00000"), conf);
            
            IntWritable key = new IntWritable();
            WeightedVectorWritable value = new WeightedVectorWritable();
            int x =0;
            int y=-1;
            int z =0;
            
            //for example the clusternumber 338 has a list of dataset easy-dataset:4,easy-dataset:7, easy-dataset:110
            Map<String, List<String>> mapOfClusterAndItsDatasets = new HashMap<String, List<String>>();
            List<String> listOfDatasets = null;
            
            //key is just a cluster number, the number is just an identity.
            //vector name is a document (or the key when it is assigned in hadoop
            //value is the ?
           
            while (reader.next(key, value)) {
            	
            	NamedVector vector = (NamedVector) value.getVector();
                String vectorName = vector.getName();
                //System.out.println("Dataset id: " + vectorName + ", key:" + key.toString());
                //System.out.println("-----------------------");
              // System.out.println(key.toString() + " belongs to cluster " + value.toString());
            	String clusterNumber = key.toString();
            	if (!mapOfClusterAndItsDatasets.containsKey(clusterNumber)) {
            		listOfDatasets = new ArrayList<String>();
            		mapOfClusterAndItsDatasets.put(clusterNumber, listOfDatasets);
            	} else {
            		listOfDatasets = mapOfClusterAndItsDatasets.get(clusterNumber);
            	}
            	listOfDatasets.add(vectorName);
            	
//            	if (y != Integer.parseInt(value.toString())){
//            		map.put("Cluster " + y, z); 
//            		y=Integer.parseInt(value.toString());
//            		z=0;
//            	} else
//            		z++;
            		
            	x++;
            }
            System.out.println("Number of processed data: " + x);
            System.out.println("Total number of the cluster: " + mapOfClusterAndItsDatasets.size());
            reader.close();
            
            //for example : cluster-343 has 40 datasets
            Map<Integer, Integer> clusterAndItsNumberOfDatasets = new HashMap<Integer, Integer>();
            Set<String> set = mapOfClusterAndItsDatasets.keySet();
            for (String str : set) {
            	clusterAndItsNumberOfDatasets.put(Integer.parseInt(str), mapOfClusterAndItsDatasets.get(str).size());
//            	if ( mapOfClusterAndItsDatasets.get(str).size() >= 14 && mapOfClusterAndItsDatasets.get(str).size() <=100)
//            		System.out.println(mapOfClusterAndItsDatasets.get(str));
            	
            	if ( mapOfClusterAndItsDatasets.get(str).size() == 1)
            		System.out.println(mapOfClusterAndItsDatasets.get(str));
            }
            
            Set<Entry<Integer, Integer>> setk = clusterAndItsNumberOfDatasets.entrySet();
            //sorting cluster based on the key.
            SortedSet<Integer> keys = new TreeSet<Integer>(clusterAndItsNumberOfDatasets.keySet());
            
            //Try to group the cluster with the same number of dataset together
            //For example, cluster123 and cluster221 both have the same number of datasets.
            //The map will be something like: [the number of cluster that has 1 document, 5 clusters]
            Map<Integer,List<Integer>> groupClusterWithTheSameNumberOfDatasets = new HashMap<Integer, List<Integer>>();
            List<Integer> xxx = null;
            int numberOfDatasets = 0;
            for (Integer k : keys) { 
            	int numberOfClusters = 1;
            	int numberOfDatasetsInACluster = clusterAndItsNumberOfDatasets.get(k);
            	numberOfDatasets += numberOfDatasetsInACluster;
            	  // System.out.println("Cluster " + k + " has " + numberOfDatasetsInACluster + " documents");
            	   if (!groupClusterWithTheSameNumberOfDatasets.containsKey(numberOfDatasetsInACluster)) {
            		   xxx = new ArrayList<Integer>();
            		   groupClusterWithTheSameNumberOfDatasets.put(numberOfDatasetsInACluster, xxx);
            	   } else {
            		   xxx = groupClusterWithTheSameNumberOfDatasets.get(numberOfDatasetsInACluster);
            	   }
            	   xxx.add(numberOfDatasetsInACluster);
           	}
            System.out.println("Number of datasets: " + numberOfDatasets);
            
            //sorting cluster based on the key.
            SortedSet<Integer> setOfNumberOfCluster = new TreeSet<Integer>(groupClusterWithTheSameNumberOfDatasets.keySet());
            
            
            FileWriter fw = new FileWriter("/Users/akmi/20130210All-2137.csv");
            PrintWriter out = new PrintWriter(fw);
            
            for (int j : setOfNumberOfCluster) {
            	//System.out.println("The number of cluster that has " + j + " is " + groupClusterWithTheSameNumberOfDatasets.get(j).size());
            	//System.out.println(j);//groupClusterWithTheSameNumberOfDatasets.get(j).size());
            	out.print(j);
            	out.print(",");
            	out.println(groupClusterWithTheSameNumberOfDatasets.get(j).size());
            }
            
          //Flush the output to the file
            out.flush();
                
            //Close the Print Writer
            out.close();
                
            //Close the File Writer
            fw.close();       
            
            
 //          Set<Integer> clusterscounter = clustersAndItsCountedNumber.keySet();
//           for (int i : clusterscounter) {
//        	   System.out.println("Cluster with number of docs is " + i + " has total: " + clustersAndItsCountedNumber.get(i));
//           }
          
           
 //          SortedSet<Integer> kucing = new TreeSet<Integer>(clustersAndItsCountedNumber.keySet());
           
           
//            Map<String, Integer> nnn = MapUtil.sortByValue(clusterAndItsNumbers);
//            Set<String> sss = nnn.keySet();
//            for (String ssss : sss) {
//            System.out.println("Cluster[ " + ssss + "] has " + nnn.get(ssss) + " documents.");
//            }
            
            
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    //=================================
    public static <K, V extends Comparable<V>> Map<K, V> sortByValues(final Map<K, V> map) {
        Comparator<K> valueComparator =  new Comparator<K>() {
            public int compare(K k1, K k2) {
                int compare = map.get(k2).compareTo(map.get(k1));
                if (compare == 0) return 1;
                else return compare;
            }
        };
        Map<K, V> sortedByValues = new TreeMap<K, V>(valueComparator);
        sortedByValues.putAll(map);
        return sortedByValues;
    }
    
    private static LinkedHashMap<String, Integer> sortMapByValuesWithDuplicates(Map<String, Integer> passedMap) {
        List<String> mapKeys = new ArrayList<String>(passedMap.keySet());
        List<Integer> mapValues = new ArrayList<Integer>(passedMap.values());
        Collections.sort(mapValues);
        Collections.sort(mapKeys);

        LinkedHashMap<String, Integer> sortedMap = new LinkedHashMap<String, Integer>();

        Iterator<Integer> valueIt = mapValues.iterator();
        while (valueIt.hasNext()) {
            Integer val = valueIt.next();
            Iterator<String> keyIt = mapKeys.iterator();

            while (keyIt.hasNext()) {
                Object key = keyIt.next();
                String comp1 = passedMap.get(key).toString();
                String comp2 = val.toString();

                if (comp1.equals(comp2)) {
                    passedMap.remove(key);
                    mapKeys.remove(key);
                    sortedMap.put((String) key, val);
                    break;
                }
            }
        }
        return sortedMap;
    }
}
