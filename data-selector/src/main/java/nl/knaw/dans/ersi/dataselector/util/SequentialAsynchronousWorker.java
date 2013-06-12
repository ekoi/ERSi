package nl.knaw.dans.ersi.dataselector.util;

import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import nl.knaw.dans.ersi.dataselector.SimpleOaiPmhOnlyABRExtractor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SequentialAsynchronousWorker {
	private static Logger LOG = LoggerFactory.getLogger(SequentialAsynchronousWorker.class);
    public SequentialAsynchronousWorker() {
    }
    private static int numberOfJobs = 5;


    public static void main(String[] args) {
        Date startTime = new java.util.Date();
        LOG.debug("Start Work"  + startTime);
        ExecutorService es = Executors.newFixedThreadPool(3);
        ArrayList<Future> futures = new ArrayList<Future>();
        for(int i=0;i<numberOfJobs;i++) {
          LOG.debug("* Start worker "+i);
          futures.add(es.submit(new Callable() {
                        public Object call() throws Exception {
                            new SlowWorkerTryOut().doWork();
                            return null;
                        }
                    }));
        }

        LOG.debug("... try to do something while the work is being done.");
        LOG.debug("... and more ...");
        int ctr=0;
        for (Future future:futures)
        try {
            future.get();  // blocking call, explicitly waiting for the response from a specific task, not necessarily the first task that is completed
            LOG.debug("** response worker "+ ++ctr +" is in");
        } catch (InterruptedException e) {
        	LOG.error(e.getMessage());
        } catch (ExecutionException e) {
        	LOG.error(e.getMessage());
        }

        Date endTime = new java.util.Date();
        LOG.debug("End work at " + endTime);
        LOG.debug("Job took " + new Double(0.001*(endTime.getTime() - startTime.getTime()))+ " seconds");
        System.exit(0);
    }
}