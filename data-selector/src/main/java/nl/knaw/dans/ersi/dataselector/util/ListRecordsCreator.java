package nl.knaw.dans.ersi.dataselector.util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;
import org.simpleframework.xml.stream.Format;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * ConfigurationCreator
 *
 */
public class ListRecordsCreator 
{
	private static Logger LOG = LoggerFactory.getLogger(ListRecordsCreator.class);
    public static void main( String[] args )
    {
    	 
    	 LOG.debug("Create configuration.xml file");
        Serializer serializer = new Persister(new Format("<?xml version=\"1.0\" encoding= \"UTF-8\" ?>"));
        
       ListRecords lr = new ListRecords();
       List<Record> records = new ArrayList<Record>();
       Record r = new Record();
       r.setUrn("the urn");
       r.setDataOrig("origin");
       r.setDataNonAbr("nonabr");
       r.setDataAbr("abr");
       records.add(r);
	lr.setRecords(records );
        
        File result = new File("/Users/akmi/Dropbox/THESIS/Sharing-thesis-project/TODO/test2.xml");

        try {
			serializer.write(lr, result);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			LOG.error(e.getMessage());
		}
        
//        File source = new File("./src/resources/example.xml");
//
//        try {
//			Example example2 = serializer.read(Example.class, source);
//			LOG.debug(example2.getMessage());
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			LOG.error(e.getMessage());
//		}
    }
}
