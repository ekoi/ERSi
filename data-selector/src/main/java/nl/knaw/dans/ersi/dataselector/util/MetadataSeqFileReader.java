package nl.knaw.dans.ersi.dataselector.util;

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

public class MetadataSeqFileReader {
	private static Logger LOG = LoggerFactory.getLogger(MetadataSeqFileReader.class);
	public static void main(String[] args) throws IOException {
		String uri = System.getenv("ERSY_HOME") + "/data-extraction/archaeology/nl/hdfs/ArchaeologyMetadataInDutch.seq";
        Configuration conf = new Configuration();
        FileSystem fs = FileSystem.get(URI.create(uri), conf);
        Path path = new Path(uri);
        int i=0;
        SequenceFile.Reader reader = null;
        try {
                reader = new SequenceFile.Reader(fs, path, conf);
                Writable key = (Writable) ReflectionUtils.newInstance(
                                reader.getKeyClass(), conf);
                Writable value = (Writable) ReflectionUtils.newInstance(
                                reader.getValueClass(), conf);
               // long position = reader.getPosition();
                while (reader.next(key, value)) {
                        //String syncSeen = reader.syncSeen() ? "*" : "";
//                        LOG.debug("key: " + key + " value: " +
//                                        value);
                        LOG.debug("key: " + key);
                        //position = reader.getPosition(); // beginning of next record
                        i++;
                }
        } finally {
                IOUtils.closeStream(reader);
        }
        LOG.debug("Line: " + i);
}


}
