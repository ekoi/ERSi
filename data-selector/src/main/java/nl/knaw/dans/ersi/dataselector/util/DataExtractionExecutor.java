package nl.knaw.dans.ersi.dataselector.util;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import nl.knaw.dans.ersi.config.ConfigurationReader;
import nl.knaw.dans.ersi.dataselector.SimpleOaiPmhExtractor;

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
		SimpleOaiPmhExtractor seme = new SimpleOaiPmhExtractor(cr.getDataExtractionConfig());

	executor.execute(new Worker(seme));

	// reject new tasks
	executor.shutdown();

	// wait for termination
	executor.awaitTermination(1, TimeUnit.SECONDS);
	LOG.debug("=============EIND of DataExtractionExecutor =======");
    }
}


class Worker implements Runnable {
	
	private static Logger LOG = LoggerFactory.getLogger(Worker.class);
    private SimpleOaiPmhExtractor seme;

    public Worker(SimpleOaiPmhExtractor seme) {
	this.seme = seme;
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