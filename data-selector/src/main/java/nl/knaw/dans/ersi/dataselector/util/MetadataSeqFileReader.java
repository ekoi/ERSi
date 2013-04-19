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

public class MetadataSeqFileReader {
	
	public static void main(String[] args) throws IOException {
		String uri = "/Users/akmi/ersy_home/data-extraction/archaeology/nl/hdfs/ArchaeologyMetadataInDutch.seq";
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
//                        System.out.println("key: " + key + " value: " +
//                                        value);
                        System.out.println(key);
                        //position = reader.getPosition(); // beginning of next record
                        i++;
                }
        } finally {
                IOUtils.closeStream(reader);
        }
        System.out.println(i);
}


}
