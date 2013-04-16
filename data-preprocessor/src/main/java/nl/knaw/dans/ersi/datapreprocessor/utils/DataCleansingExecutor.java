package nl.knaw.dans.ersi.datapreprocessor.utils;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import nl.knaw.dans.ersi.preprocess.standard.SimpleDataCleansing;

public class DataCleansingExecutor {
    public static void main() throws Exception {
	ExecutorService executor = Executors.newFixedThreadPool(1);
	System.out.println("++++++++++++++++++++ START +++++++++++++");
	test(executor);

    }

    private static void test(ExecutorService executor) throws InterruptedException {
		SimpleDataCleansing sdc = new SimpleDataCleansing();

	executor.execute(new Worker(sdc));

	// reject new tasks
	executor.shutdown();

	// wait for termination
	executor.awaitTermination(1, TimeUnit.SECONDS);
	System.out.println("=============EIND END END=======");
    }
}


class Worker implements Runnable {
    private SimpleDataCleansing sdc;

    public Worker(SimpleDataCleansing sdc) {
	this.sdc = sdc;
    }

    public void run() {
    	System.out.println("*************BEGIN DATA CLEANSING*****************");
	try {
		sdc.run();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (InterruptedException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (ClassNotFoundException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} 
	System.out.println("#############DATA CLEANSING FINISH####################");
	
    }
}