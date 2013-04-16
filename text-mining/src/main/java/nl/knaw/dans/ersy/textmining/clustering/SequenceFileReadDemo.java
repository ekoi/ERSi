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

public class SequenceFileReadDemo {
	
	public static void main(String[] args) throws IOException {
		System.out.println("===BEGIN====");
		//String uri = "/Users/akmi/tmp/1resuptiontoken/seqdata-from-easy/nlEasy.seq";
				//wordcount/part-r-00000";
		//String uri = "/Users/akmi/zzz-test2/output-vectors/tokenized-documents/part-m-00000";
		//String uri = "/Users/akmi/zzz-test-20130126-2308/output-vectors/wordcount/part-r-00000";
		String uri = "/Volumes/Holdtank/Experiments/ERSi/data-cleansing-result/oai-pmh/output-vector/frequency.file-0";
        Configuration conf = new Configuration();
        FileSystem fs = FileSystem.get(URI.create(uri), conf);
        Path path = new Path(uri);
        SequenceFile.Reader reader = null;
        int i=0;
        int n=0;
        try {
                reader = new SequenceFile.Reader(fs, path, conf);
                Writable key = (Writable) ReflectionUtils.newInstance(
                                reader.getKeyClass(), conf);
                Writable value = (Writable) ReflectionUtils.newInstance(
                                reader.getValueClass(), conf);
                //long position = reader.getPosition();
                while (reader.next(key, value)) {
                        //String syncSeen = reader.syncSeen() ? "*" : "";
                        System.out.println("key: " + key + " value: " +
                                        value);
                        n+=Integer.parseInt(value.toString());;
                        i++;
                        //position = reader.getPosition(); // beginning of next record
                }
        } finally {
                IOUtils.closeStream(reader);
        }
        System.out.println("Number of word: " + i);
        System.out.println("Number of n: " + n);
        System.out.println("===END====");
}


}
