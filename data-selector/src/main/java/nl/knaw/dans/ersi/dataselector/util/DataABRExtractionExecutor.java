package nl.knaw.dans.ersi.dataselector.util;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import nl.knaw.dans.ersi.config.ConfigurationReader;
import nl.knaw.dans.ersi.dataselector.SimpleOaiPmhOnlyABRExtractor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import se.kb.oai.OAIException;

import com.cybozu.labs.langdetect.LangDetectException;

public class DataABRExtractionExecutor {
	private static Logger LOG = LoggerFactory.getLogger(DataABRExtractionExecutor.class);
    public static void main() throws Exception {
	ExecutorService executor = Executors.newFixedThreadPool(1);
	LOG.debug("++++++++++++++++++++ START DataABRExtractionExecutor +++++++++++++");
	test(executor);

    }

    private static void test(ExecutorService executor) throws InterruptedException {
    	ConfigurationReader cr = new ConfigurationReader();
		SimpleOaiPmhOnlyABRExtractor seme = new SimpleOaiPmhOnlyABRExtractor(cr);

	executor.execute(new WorkerABR(seme));

	// reject new tasks
	executor.shutdown();

	// wait for termination
	executor.awaitTermination(1, TimeUnit.SECONDS);
	LOG.debug("=============EIND of DataABRExtractionExecutor =======");
    }
}


class WorkerABR implements Runnable {
	private static Logger LOG = LoggerFactory.getLogger(WorkerABR.class);
    private SimpleOaiPmhOnlyABRExtractor seme;

    public WorkerABR(SimpleOaiPmhOnlyABRExtractor seme) {
	this.seme = seme;
    }

    public void run() {
    	LOG.debug("************START WorkerABR *****************");
	try {
		seme.extract();
	} catch (OAIException e) {
		LOG.error(e.getMessage());
	} catch (IOException e) {
		LOG.error(e.getMessage());
	} catch (LangDetectException e) {
		LOG.error(e.getMessage());
	}
	LOG.debug("############END WorkerABR#####################");
	
    }
}