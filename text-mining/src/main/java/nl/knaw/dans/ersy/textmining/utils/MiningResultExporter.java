package nl.knaw.dans.ersy.textmining.utils;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import nl.knaw.dans.ersi.config.ConfigurationReader;
import nl.knaw.dans.ersi.config.Constants;
import nl.knaw.dans.ersy.orm.Recommendation;
import nl.knaw.dans.ersy.orm.dao.MiningProcess;
import nl.knaw.dans.ersy.orm.dao.PidRelevancy;

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

public class MiningResultExporter {
	private static Logger LOG = LoggerFactory.getLogger(MiningResultExporter.class);
	public static void main( String[] args ) throws IOException
    {
		LOG.info("====START Export mining result to database=====");
		if (args.length != 2) {
			LOG.info("Please specify the arguments. It should be 2 argumemts. args[0] is the process nme. args[1] is minimal distance.");
			System.exit(0);
		}
		double minDistance = 0;
		try {
			minDistance = Double.parseDouble(args[1]);
		} catch (NumberFormatException e) {
			LOG.error(e.getMessage());
			System.exit(100);
		}	
		long begin = System.currentTimeMillis();
    	ConfigurationReader cr = new ConfigurationReader(Constants.ERSY_HOME);
    	Configuration conf = new Configuration();
        FileSystem fs = FileSystem.get(conf);
        String clusteringResultLocation = cr.getClusteringConfig().getOutputPath() + "/clusters-output/clusters";
    	Path vectorsFolder = new Path(clusteringResultLocation, "tfidf-vectors");
    	 //Path clusterOutput = new Path(outputDir , "clusters");
    	
        SequenceFile.Reader reader = new SequenceFile.Reader(fs,
                new Path(clusteringResultLocation + "/" + Cluster.CLUSTERED_POINTS_DIR + "/part-m-00000"), conf);
            
            IntWritable key = new IntWritable();
            WeightedVectorWritable value = new WeightedVectorWritable();
            
            //for example the clusternumber 338 has a list of (vectors) dataset easy-dataset:4,easy-dataset:7, easy-dataset:110
            Map<String, List<NamedVector>> mapOfClusterAndItsDatasets = new HashMap<String, List<NamedVector>>();
            List<NamedVector> listOfDatasets = null;
            
            //key is just a cluster number, the number is just an identity.
            //vector name is a document (or the key when it is assigned in hadoop
            //value is the ?
           
            LOG.info("Extracting vectors....");
            while (reader.next(key, value)) {
            	NamedVector vector = (NamedVector) value.getVector();
                String vectorName = vector.getName();
               String clusterNumber = key.toString();
           		//LOG.debug("clusterNumber: " + clusterNumber);
            	if (!mapOfClusterAndItsDatasets.containsKey(clusterNumber)) {
            		listOfDatasets = new ArrayList<NamedVector>();
            		mapOfClusterAndItsDatasets.put(clusterNumber, listOfDatasets);
            	} else {
            		listOfDatasets = mapOfClusterAndItsDatasets.get(clusterNumber);
            	}
            	listOfDatasets.add(vector);
            }
         
            
            System.out.println(mapOfClusterAndItsDatasets.size());
            
            int count = 0;
            
            Set<String> cn = mapOfClusterAndItsDatasets.keySet();
        	
        	List<MiningProcess> mps = new ArrayList<MiningProcess>();
            MiningProcess mp = new MiningProcess(args[0]);
            Set<PidRelevancy> prs = new HashSet<PidRelevancy>();
            LOG.debug("INSERTING data to DB....");
            for (String x : cn) {
            	List<NamedVector> vectors = mapOfClusterAndItsDatasets.get(x);
            	int vl = vectors.size();
            	LOG.debug("vecotr size: " + vl);
            	//System.out.println(vl);
            	for (int i=0; i< vl; i++) {
            		NamedVector nvi = vectors.get(i);
            		for (int j=i+1; j<vl; j++) {
            			NamedVector nvj = vectors.get(j);
            			double distance = nvi.getDistanceSquared(nvj);
            			if (distance > minDistance) {
	            			//System.out.println(distance);
	            			PidRelevancy pidr = new PidRelevancy();
	            			pidr.setDistance(distance);
	            			pidr.setMiningProcess(mp);
	            			pidr.setPid(nvi.getName());
	            			pidr.setPidRel(nvj.getName());
	            			prs.add(pidr);
	            			LOG.debug("Insert record: " + nvi.getName());
	            			LOG.debug("count: " + count++);
            			}
            		}
            	}
            }
            mp.setPidRelevancies(prs);
            mps.add(mp);
            LOG.info("MPS SIZE: " + mps.size());
            LOG.info("Number of rows: " + count);
            Recommendation.store(mps);
            
            long diff = System.currentTimeMillis() - begin;
    	    String s= String.format("%d hours. %d min, %d sec", 
    	    		TimeUnit.MILLISECONDS.toHours(diff),
            	    TimeUnit.MILLISECONDS.toMinutes(diff),
            	    TimeUnit.MILLISECONDS.toSeconds(diff) - 
            	    TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(diff))
            	);
    	    
    	    LOG.info("Duration: " + s);
    	    LOG.info("====END=====");
    }
}
