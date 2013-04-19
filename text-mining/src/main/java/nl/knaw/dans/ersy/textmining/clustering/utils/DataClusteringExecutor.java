package nl.knaw.dans.ersy.textmining.clustering.utils;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import nl.knaw.dans.ersy.textmining.clustering.KMeansWithCanopyClustering;

public class DataClusteringExecutor {
    public static void main() throws Exception {
	ExecutorService executor = Executors.newFixedThreadPool(1);
	System.out.println("++++++++++++++++++++ START +++++++++++++");
	test(executor);

    }

    private static void test(ExecutorService executor) throws InterruptedException {
		KMeansWithCanopyClustering kwcc = new KMeansWithCanopyClustering();

	executor.execute(new Worker(kwcc));

	// reject new tasks
	executor.shutdown();

	// wait for termination
	executor.awaitTermination(1, TimeUnit.SECONDS);
	System.out.println("=============EIND END END=======");
    }
}


class Worker implements Runnable {
    private KMeansWithCanopyClustering kwcc;

    public Worker(KMeansWithCanopyClustering kwcc) {
	this.kwcc = kwcc;
    }

    public void run() {
    	System.out.println("*************BEGIN DATA CLUSTERING*****************");
	
		try {
			kwcc.run();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {	
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	System.out.println("#############DATA CLUSTERING FINISH####################");
	
    }
}