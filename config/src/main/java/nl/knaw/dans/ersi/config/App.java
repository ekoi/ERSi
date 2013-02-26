package nl.knaw.dans.ersi.config;

import java.io.File;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;
import org.simpleframework.xml.stream.Format;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        System.out.println( "Hello World!" );
        Serializer serializer = new Persister(new Format("<?xml version=\"1.0\" encoding= \"UTF-8\" ?>"));
        OaiPmh oaiPmhServer = new OaiPmh();
        Configuration configuration = new Configuration("nl", "nl");
        configuration.setOaiPmh(oaiPmhServer);
        File result = new File("./src/resources/configuration.xml");

        try {
			serializer.write(configuration, result);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
//        File source = new File("./src/resources/example.xml");
//
//        try {
//			Example example2 = serializer.read(Example.class, source);
//			System.out.println(example2.getMessage());
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
    }
}
