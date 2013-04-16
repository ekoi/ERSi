package nl.knaw.dans.ersi.dataselector.util;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import nl.knaw.dans.ersi.config.ConfigurationReader;
import nl.knaw.dans.ersi.dataselector.SimpleOaiPmhExtractor;
import se.kb.oai.OAIException;

import com.cybozu.labs.langdetect.LangDetectException;

public class DataExtractionExecutor {
    public static void main() throws Exception {
	ExecutorService executor = Executors.newFixedThreadPool(1);
	System.out.println("++++++++++++++++++++ START +++++++++++++");
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
	System.out.println("=============EIND END END=======");
    }
}


class Worker implements Runnable {
    private SimpleOaiPmhExtractor seme;

    public Worker(SimpleOaiPmhExtractor seme) {
	this.seme = seme;
    }

    public void run() {
    	System.out.println("******************************");
	try {
		seme.extract();
	} catch (OAIException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (LangDetectException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	System.out.println("#################################");
	
    }
}