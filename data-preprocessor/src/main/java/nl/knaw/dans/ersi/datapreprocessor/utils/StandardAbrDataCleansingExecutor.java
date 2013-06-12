package nl.knaw.dans.ersi.datapreprocessor.utils;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import nl.knaw.dans.ersi.preprocess.standard.AbrDataCleansing;
import nl.knaw.dans.ersi.preprocess.standard.StandardAndAbrDataCleansing;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StandardAbrDataCleansingExecutor {
	private static Logger LOG = LoggerFactory.getLogger(StandardAbrDataCleansingExecutor.class);
    public static void main() throws Exception {
	ExecutorService executor = Executors.newFixedThreadPool(1);
	LOG.info("++++++++++++++++++++ START +++++++++++++");
	test(executor);

    }

    private static void test(ExecutorService executor) throws InterruptedException {
    	StandardAndAbrDataCleansing sdc = new StandardAndAbrDataCleansing();

	executor.execute(new WorkerStandardAbr(sdc));

	// reject new tasks
	executor.shutdown();

	// wait for termination
	executor.awaitTermination(1, TimeUnit.SECONDS);
	LOG.info("=============EIND END END=======");
    }
}


class WorkerStandardAbr implements Runnable {
	private static Logger LOG = LoggerFactory.getLogger(WorkerAbr.class);
    private StandardAndAbrDataCleansing sdc;
    
    public WorkerStandardAbr(StandardAndAbrDataCleansing sdc) {
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