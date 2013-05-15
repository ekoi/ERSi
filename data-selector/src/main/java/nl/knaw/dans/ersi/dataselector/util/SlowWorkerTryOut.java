package nl.knaw.dans.ersi.dataselector.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SlowWorkerTryOut {
	private static Logger LOG = LoggerFactory.getLogger(SlowWorkerTryOut.class);

    public SlowWorkerTryOut() {
    }

    public void doWork() {
        try {
            LOG.debug("==== working, working, working ====== ");
            Thread.sleep(2000);
            LOG.debug("==== ready! ======");
        } catch (InterruptedException e) {
        }
    }

    public static void main(String[] args) {
        SlowWorkerTryOut worker = new SlowWorkerTryOut();
        LOG.debug("Start Work"  + new java.util.Date());
        worker.doWork();
        LOG.debug("... try to do something while the work is being done....");

        LOG.debug("End work" + new java.util.Date());
        System.exit(0);
    }

}