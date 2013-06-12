package nl.knaw.dans.ersy.textmining.clustering.utils;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import nl.knaw.dans.ersy.textmining.clustering.KMeansWithCanopyClustering;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DataClusteringExecutor {
	private static Logger LOG = LoggerFactory.getLogger(DataClusteringExecutor.class);
    public static void main() throws Exception {
	ExecutorService executor = Executors.newFixedThreadPool(1);
	LOG.info("++++++++++++++++++++ START DataClusteringExecutor+++++++++++++");
	test(executor);

    }

    private static void test(ExecutorService executor) throws InterruptedException {
		KMeansWithCanopyClustering kwcc = new KMeansWithCanopyClustering();

	executor.execute(new Worker(kwcc));

	// reject new tasks
	executor.shutdown();

	// wait for termination
	executor.awaitTermination(1, TimeUnit.SECONDS);
	LOG.info("=============EIND DataClusteringExecutor=======");
    }
}


class Worker implements Runnable {
	private static Logger LOG = LoggerFactory.getLogger(Worker.class);
    private KMeansWithCanopyClustering kwcc;

    public Worker(KMeansWithCanopyClustering kwcc) {
	this.kwcc = kwcc;
    }

    public void run() {
    	LOG.debug("*************BEGIN DATA CLUSTERING (Worker)*****************");
	
		try {
			kwcc.run();
		} catch (IOException e) {
			LOG.error(e.getMessage());
		} catch (ClassNotFoundException e) {
			LOG.error(e.getMessage());
		} catch (InterruptedException e) {
			LOG.error(e.getMessage());
		} catch (InstantiationException e) {	
			LOG.error(e.getMessage());
		} catch (IllegalAccessException e) {
			LOG.error(e.getMessage());
		}
	LOG.debug("#############DATA CLUSTERING FINISH (Worker) ####################");
	
    }
}