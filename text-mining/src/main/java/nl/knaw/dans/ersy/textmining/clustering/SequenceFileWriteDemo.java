package nl.knaw.dans.ersy.textmining.clustering;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;
import org.apache.hadoop.io.SequenceFile;
import org.apache.hadoop.io.Text;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SequenceFileWriteDemo {
	private static Logger LOG = LoggerFactory.getLogger(SequenceFileWriteDemo.class);
	//private static final String[] DATA = { "One, two, buckle my shoe", "Three, four, shut the door", "Five, six, pick up sticks", "Seven, eight, lay them straight", "Nine, ten, a big fat hen" };

    public static void main( String[] args) throws IOException { 
    	LOG.debug("===BEGIN SequenceFileWriteDemo====");
        String uri = System.getenv("ERSY_HOME") + "/T555.seq";
        Configuration conf = new Configuration();
        FileSystem fs = FileSystem.get(URI.create( uri), conf);
        Path path = new Path( uri);
        //IntWritable key = new IntWritable();
        Text key = new Text();
        
        Text value = new Text();
        SequenceFile.Writer writer = null;
        try { 
            writer = SequenceFile.createWriter( fs, conf, path, key.getClass(), value.getClass());
            BufferedReader input =  new BufferedReader(new FileReader(System.getenv("ERSY_HOME") + "/T555.txt"));
            
            String line = null;
            while (( line = input.readLine()) != null && !line.isEmpty()){
            	LOG.debug(line);
            	String words[] = line.split("#");
            	key.set(words[0]);
            	value.set(words[1]);
            	LOG.debug("word 0: " + words[0]);
            	LOG.debug("word 1: " + words[1]);
            	 writer.append( key, value);
              }
        } finally 
        { IOUtils.closeStream( writer); 
        } 
        LOG.debug("===END SequenceFileWriteDemo====");
    } 
}
