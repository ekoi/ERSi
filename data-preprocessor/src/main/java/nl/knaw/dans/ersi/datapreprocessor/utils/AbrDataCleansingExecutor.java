package nl.knaw.dans.ersi.datapreprocessor.utils;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import nl.knaw.dans.ersi.preprocess.standard.AbrDataCleansing;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AbrDataCleansingExecutor {
	private static Logger LOG = LoggerFactory.getLogger(AbrDataCleansingExecutor.class);
    public static void main() throws Exception {
	ExecutorService executor = Executors.newFixedThreadPool(1);
	LOG.info("++++++++++++++++++++ START +++++++++++++");
	test(executor);

    }

    private static void test(ExecutorService executor) throws InterruptedException {
		AbrDataCleansing sdc = new AbrDataCleansing();

	executor.execute(new WorkerAbr(sdc));

	// reject new tasks
	executor.shutdown();

	// wait for termination
	executor.awaitTermination(1, TimeUnit.SECONDS);
	LOG.info("=============EIND END END=======");
    }
}


class WorkerAbr implements Runnable {
	private static Logger LOG = LoggerFactory.getLogger(WorkerAbr.class);
    private AbrDataCleansing sdc;
    
    public WorkerAbr(AbrDataCleansing sdc) {
	this.sdc = sdc;
    }

    public void run() {
    	LOG.info("*************BEGIN DATA CLEANSING*****************");
	try {
		sdc.run();
	} catch (IOException e) {
		LOG.error(e.getMessage());
	} catch (InterruptedException e) {
		LOG.error(e.getMessage());
	} catch (ClassNotFoundException e) {
		LOG.error(e.getMessage());
	} 
	LOG.debug("#############DATA CLEANSING FINISH####################");
	
    }
}