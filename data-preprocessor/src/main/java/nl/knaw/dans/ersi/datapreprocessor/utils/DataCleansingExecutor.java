package nl.knaw.dans.ersi.datapreprocessor.utils;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import nl.knaw.dans.ersi.preprocess.standard.SimpleDataCleansing;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DataCleansingExecutor {
	private static Logger LOG = LoggerFactory.getLogger(DataCleansingExecutor.class);
    public static void main() throws Exception {
	ExecutorService executor = Executors.newFixedThreadPool(1);
	LOG.debug("++++++++++++++++++++ START DataCleansingExecutor +++++++++++++");
	test(executor);

    }

    private static void test(ExecutorService executor) throws InterruptedException {
		SimpleDataCleansing sdc = new SimpleDataCleansing();

	executor.execute(new Worker(sdc));

	// reject new tasks
	executor.shutdown();

	// wait for termination
	executor.awaitTermination(1, TimeUnit.SECONDS);
	LOG.debug("=============EIND DataCleansingExecutor =======");
    }
}


class Worker implements Runnable {
	private static Logger LOG = LoggerFactory.getLogger(Worker.class);
    private SimpleDataCleansing sdc;

    public Worker(SimpleDataCleansing sdc) {
	this.sdc = sdc;
    }

    public void run() {
    	LOG.debug("*************BEGIN DATA CLEANSING (Worker)*****************");
	try {
		sdc.run();
	} catch (IOException e) {
		LOG.error(e.getMessage());
	} catch (InterruptedException e) {
		LOG.error(e.getMessage());
	} catch (ClassNotFoundException e) {
		LOG.error(e.getMessage());
	} 
	LOG.debug("#############DATA CLEANSING FINISH (Worker) ####################");
	
    }
}