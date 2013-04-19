package nl.knaw.dans.ersi.dataselector.util;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import nl.knaw.dans.ersi.config.ConfigurationReader;
import nl.knaw.dans.ersi.dataselector.SimpleOaiPmhWithABRExtractor;
import se.kb.oai.OAIException;

import com.cybozu.labs.langdetect.LangDetectException;

public class DataABRExtractionExecutor {
    public static void main() throws Exception {
	ExecutorService executor = Executors.newFixedThreadPool(1);
	System.out.println("++++++++++++++++++++ START +++++++++++++");
	test(executor);

    }

    private static void test(ExecutorService executor) throws InterruptedException {
    	ConfigurationReader cr = new ConfigurationReader();
		SimpleOaiPmhWithABRExtractor seme = new SimpleOaiPmhWithABRExtractor(cr.getDataExtractionConfig());

	executor.execute(new WorkerABR(seme));

	// reject new tasks
	executor.shutdown();

	// wait for termination
	executor.awaitTermination(1, TimeUnit.SECONDS);
	System.out.println("=============EIND of DataABRExtractionExecutor =======");
    }
}


class WorkerABR implements Runnable {
    private SimpleOaiPmhWithABRExtractor seme;

    public WorkerABR(SimpleOaiPmhWithABRExtractor seme) {
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