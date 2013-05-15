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

import nl.knaw.dans.ersi.config.ConfigurationReader;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.SequenceFile;
import org.apache.mahout.clustering.Cluster;
import org.apache.mahout.clustering.classify.WeightedVectorWritable;
import org.apache.mahout.math.NamedVector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Hello world!
 *
 */
public class HumanReadableClusterDumper 
{
	private static Logger LOG = LoggerFactory.getLogger(HumanReadableClusterDumper.class);
    public static void main( String[] args ) throws IOException
    {
    	ConfigurationReader cr = new ConfigurationReader();
    	Configuration conf = new Configuration();
        FileSystem fs = FileSystem.get(conf);
        String clusteringResultLocation = cr.getClusteringConfig().getOutputPath() + "/clusters-output/clusters";
    	Path vectorsFolder = new Path(clusteringResultLocation, "tfidf-vectors");
    	 //Path clusterOutput = new Path(outputDir , "clusters");
    	
        SequenceFile.Reader reader = new SequenceFile.Reader(fs,
                new Path(clusteringResultLocation + "/" + Cluster.CLUSTERED_POINTS_DIR + "/part-m-00000"), conf);
            
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
               LOG.debug("Dataset id: " + vectorName + ", key:" + key.toString());
               LOG.debug("key: " + key.toString() + " belongs to cluster " + value.toString());
               String clusterNumber = key.toString();
           		LOG.debug("clusterNumber: " + clusterNumber);
               LOG.debug("-----------------------");
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
//            FileWriter f = new FileWriter(System.getenv("ERSY_HOME") + "/" + "dimension-reduction-abr3.csv");
//            PrintWriter o = new PrintWriter(f);
//            o.println("Cluster Code,Number of Documents, Document Id");
//            Set<String> sss = mapOfClusterAndItsDatasets.keySet();
//            for (String s1 : sss) {
//            	List<String> ll = mapOfClusterAndItsDatasets.get(s1);
//            //LOG.debug("Cluseter code " + s1 + " has " + ll.size() + " members. The members are: " + ll );
//            	StringBuffer sb = new StringBuffer();
//            	for (String l : ll) 
//            		sb.append(l + "#");
//            	o.println(s1 + "," + ll.size() + ",\"" +sb + "\"");
//            }
//            o.close();
//            f.close();
            Set<String> clusternumber = mapOfClusterAndItsDatasets.keySet();
            List<Integer> listofdatasetsize = new ArrayList<Integer>();
            LOG.debug("Total cluster: " + clusternumber.size());
            for (String s1 : clusternumber) {
            	List<String> datasets = mapOfClusterAndItsDatasets.get(s1);
            	//LOG.debug(s1 + "," + ll.size());
            	listofdatasetsize.add(datasets.size());
            }
            LOG.debug("List of datasets size: " + listofdatasetsize.size());
            LOG.debug("listofdatasetsize: " + listofdatasetsize);
            Collections.sort(listofdatasetsize);
            LOG.debug("listofdatasetsize: " + listofdatasetsize);
            LOG.debug("Number of processed data: " + x);
            LOG.debug("Total number of the cluster: " + mapOfClusterAndItsDatasets.size());
            reader.close();
            
            //for example : cluster-343 has 40 datasets
            Map<Integer, Integer> clusterAndItsNumberOfDatasets = new HashMap<Integer, Integer>();
            Set<String> set = mapOfClusterAndItsDatasets.keySet();
            for (String str : set) {
            //	LOG.debug("key: " + str + "\thas Size: " + mapOfClusterAndItsDatasets.get(str).size());
            	clusterAndItsNumberOfDatasets.put(Integer.parseInt(str), mapOfClusterAndItsDatasets.get(str).size());
//            	if ( mapOfClusterAndItsDatasets.get(str).size() >= 14 && mapOfClusterAndItsDatasets.get(str).size() <=100)
//            		LOG.debug(mapOfClusterAndItsDatasets.get(str));
            	
//            	if ( mapOfClusterAndItsDatasets.get(str).size() == 2)
//            		LOG.debug(mapOfClusterAndItsDatasets.get(str));
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
            	//int numberOfClusters = 1;
            	int numberOfDatasetsInACluster = clusterAndItsNumberOfDatasets.get(k);
            	numberOfDatasets += numberOfDatasetsInACluster;
            	  // LOG.debug("Cluster " + k + " has " + numberOfDatasetsInACluster + " documents");
            	   if (!groupClusterWithTheSameNumberOfDatasets.containsKey(numberOfDatasetsInACluster)) {
            		   xxx = new ArrayList<Integer>();
            		   groupClusterWithTheSameNumberOfDatasets.put(numberOfDatasetsInACluster, xxx);
            	   } else {
            		   xxx = groupClusterWithTheSameNumberOfDatasets.get(numberOfDatasetsInACluster);
            	   }
            	   xxx.add(numberOfDatasetsInACluster);
           	}
            LOG.debug("Number of datasets: " + numberOfDatasets);
            
            //sorting cluster based on the key.
            SortedSet<Integer> setOfNumberOfCluster = new TreeSet<Integer>(groupClusterWithTheSameNumberOfDatasets.keySet());
            
            if (args != null && args.length==2) {
            	String csv1 = args[0];
            	String csv2 = args[1];
            FileWriter fw = new FileWriter(csv1);
            PrintWriter out = new PrintWriter(fw);
            out.println("Cluster number,Cluster Size");
            for (int j : setOfNumberOfCluster) {
            //	LOG.debug("The number of cluster that has " + j + " is " + groupClusterWithTheSameNumberOfDatasets.get(j).size());
            	//LOG.debug(j);//groupClusterWithTheSameNumberOfDatasets.get(j).size());
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
//        	   LOG.debug("Cluster with number of docs is " + i + " has total: " + clustersAndItsCountedNumber.get(i));
//           }
          
           
 //          SortedSet<Integer> kucing = new TreeSet<Integer>(clustersAndItsCountedNumber.keySet());
           
           
//            Map<String, Integer> nnn = MapUtil.sortByValue(clusterAndItsNumbers);
//            Set<String> sss = nnn.keySet();
//            for (String ssss : sss) {
//            LOG.debug("Cluster[ " + ssss + "] has " + nnn.get(ssss) + " documents.");
//            }
            
            
            
            FileWriter fw1 = new FileWriter(csv2);
            PrintWriter out1 = new PrintWriter(fw1);
            Map<Integer, List<Integer>> sizeingroup = new HashMap<Integer, List<Integer>>();
            List<Integer> yyyy  = new ArrayList<Integer>();
            for (int j : listofdatasetsize) {
            //	LOG.debug("The number of cluster that has " + j + " is " + groupClusterWithTheSameNumberOfDatasets.get(j).size());
            	//LOG.debug(j);//groupClusterWithTheSameNumberOfDatasets.get(j).size());
            	if (!sizeingroup.containsKey(j)) {
            		yyyy = new ArrayList<Integer>();
            		sizeingroup.put(j, yyyy);
            	} else {
            		yyyy = sizeingroup.get(j);
            	}
            	yyyy.add(j);	
            	out1.println(j);
            }
            
          //Flush the output to the file
            out1.flush();
                
            //Close the Print Writer
            out1.close();
                
            //Close the File Writer
            fw1.close();     
          
            LOG.debug("sizeingroup: " + sizeingroup);
            Set<Integer> ks = sizeingroup.keySet();
            ArrayList<Integer> list = new ArrayList<Integer>(ks);     
            Collections.sort(list);
            
            FileWriter fw3 = new FileWriter(System.getenv("ERSY_HOME") + "/" + "s3.csv");
            PrintWriter out3 = new PrintWriter(fw3);
            out3.println("Documents Size, Number of clusters");
            for (int k : list) {
            	List<Integer> l = sizeingroup.get(k);
            	LOG.debug(k + "\t" + l.size() + "\t" + l);
            	out3.println(k + "," + l.size());
            }
            out3.close();
            fw3.close();
            }
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
