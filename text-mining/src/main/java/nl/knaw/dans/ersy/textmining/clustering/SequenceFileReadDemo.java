package nl.knaw.dans.ersy.textmining.clustering;

import java.io.IOException;
import java.net.URI;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;
import org.apache.hadoop.io.SequenceFile;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.util.ReflectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SequenceFileReadDemo {
	private static Logger LOG = LoggerFactory.getLogger(SequenceFileReadDemo.class);
	public static void main(String[] args) throws IOException {
		LOG.debug("===BEGIN====");
		//String uri = "/Users/akmi/tmp/1resuptiontoken/seqdata-from-easy/nlEasy.seq";
				//wordcount/part-r-00000";
		//String uri = "/Users/akmi/zzz-test2/output-vectors/tokenized-documents/part-m-00000";
		//String uri = "/Users/akmi/zzz-test-20130126-2308/output-vectors/wordcount/part-r-00000";
		String uri = System.getenv("ERSY_HOME") + "/data-cleansing/oai-pmh/vectors/tokenized-documents/part-m-00000";
        Configuration conf = new Configuration();
        FileSystem fs = FileSystem.get(URI.create(uri), conf);
        Path path = new Path(uri);
        SequenceFile.Reader reader = null;
        int i=0;
        int n=0;
        int j=0;
        try {
                reader = new SequenceFile.Reader(fs, path, conf);
                Writable key = (Writable) ReflectionUtils.newInstance(
                                reader.getKeyClass(), conf);
                Writable value = (Writable) ReflectionUtils.newInstance(
                                reader.getValueClass(), conf);
                //long position = reader.getPosition();
                while (reader.next(key, value)) {
                        //String syncSeen = reader.syncSeen() ? "*" : "";
                        LOG.debug("key: " + key + " value: " +
                                        value);
                        if (value == null || value.toString().trim().equals("") ||  value.toString().trim().length() <3) {
                        	LOG.debug("========="); 
                        	n++;
                        } else {
                        	j++;
                        }
                        
                       // n+=Integer.parseInt(value.toString());;
                        i++;
                        //position = reader.getPosition(); // beginning of next record
                }
        } finally {
                IOUtils.closeStream(reader);
        }
        LOG.debug("Number of pid: " + i);
        LOG.debug("Number of pid - non abr: " + n);
        LOG.debug("Number of pid - wel abr: " + j);
        LOG.debug("Total: " + (n + j));
        LOG.debug("===END SequenceFileReadDemo====");
}


}
