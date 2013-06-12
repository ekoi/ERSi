package nl.knaw.dans.ersi.dataselector.util;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import nl.knaw.dans.ersi.config.ConfigurationReader;
import nl.knaw.dans.ersi.dataselector.SimpleExtractor;
import nl.knaw.dans.ersi.dataselector.SimpleOaiPmhExtractor;

import org.apache.mahout.common.distance.DistanceMeasure;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import se.kb.oai.OAIException;

import com.cybozu.labs.langdetect.LangDetectException;

public class DataExtractionExecutor {
	private static Logger LOG = LoggerFactory.getLogger(DataExtractionExecutor.class);
    public static void main() throws Exception {
		ExecutorService executor = Executors.newFixedThreadPool(1);
		LOG.debug("++++++++++++++++++++ START DataExtractionExecutor +++++++++++++");
		test(executor);

    }
    

    private static void test(ExecutorService executor) throws InterruptedException {
    	ConfigurationReader cr = new ConfigurationReader();
		SimpleOaiPmhExtractor seme = new SimpleOaiPmhExtractor(cr);

	executor.execute(new Worker(seme));

	// reject new tasks
	executor.shutdown();

	// wait for termination
	executor.awaitTermination(1, TimeUnit.SECONDS);
	LOG.debug("=============EIND of DataExtractionExecutor =======");
    }
    
    public static void go(String ersyHome, String extractionClassName) throws Exception {
    	ExecutorService executor = Executors.newFixedThreadPool(1);
    	LOG.info("++++++++++++++++++++ START DataExtractionExecutor +++++++++++++");
    	doWork(executor, ersyHome, extractionClassName);

    }
    
    private static void doWork(ExecutorService executor, String ersyHome, String extractionClassName) throws InterruptedException {
    	LOG.info(ersyHome);
    	
    	ConfigurationReader cr = new ConfigurationReader(ersyHome );
    	 try {
    		 Constructor<SimpleExtractor> c = (Constructor<SimpleExtractor>) Class.forName(extractionClassName).getConstructor(ConfigurationReader.class);
			SimpleExtractor seme = (SimpleExtractor) c.newInstance(cr);

			executor.execute(new Worker(seme));
		
			// reject new tasks
			executor.shutdown();
		
			// wait for termination
			executor.awaitTermination(1, TimeUnit.SECONDS);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		LOG.debug("=============EIND of DataExtractionExecutor =======");
    }
}


class Worker implements Runnable {
	
	private static Logger LOG = LoggerFactory.getLogger(Worker.class);
    private SimpleExtractor seme;

    public Worker(SimpleExtractor seme2) {
	this.seme = seme2;
    }

    public void run() {
    	LOG.debug("*************START DataExtractionExecutor Worker*****************");
	try {
		seme.extract();
	} catch (OAIException e) {
		// TODO Auto-generated catch block
		LOG.error(e.getMessage());
	} catch (IOException e) {
		// TODO Auto-generated catch block
		LOG.error(e.getMessage());
	} catch (LangDetectException e) {
		// TODO Auto-generated catch block
		LOG.error(e.getMessage());
	}
	LOG.debug("##############END DataExtractionExecutor Worker###################");
	
    }
}