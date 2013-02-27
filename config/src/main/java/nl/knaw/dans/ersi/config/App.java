package nl.knaw.dans.ersi.config;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

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
        
        List<Output> ouputList = new ArrayList<Output>();
        Output outputNl = new Output();
        outputNl.setLanguage("nl");
        outputNl.setTxtFileLocation("./output/test.txt");
        outputNl.setHdfsFileLocation("");
        ouputList.add(outputNl);
        OaiPmhRepos oaiPmhRepos = new OaiPmhRepos();
        ExtractedOutput extractedOutput = new ExtractedOutput();
        extractedOutput.setOutput(ouputList);
        
        List<String> metadataFields = new ArrayList<String>();
        metadataFields.add("eko");
        metadataFields.add("indarto");
        oaiPmhRepos.setSelectionFields(metadataFields);
        
        /*
         SelectionField selectionField = new SelectionField();
        List<SelectionField> selectionFields = new ArrayList<SelectionField>();
        selectionFields.add(selectionField);
        oaiPmhRepos.setSelectionFields(selectionFields);
         */
        
        Configuration configuration = new Configuration("nl", "nl");
        configuration.setOaiPmhRepos(oaiPmhRepos);
        configuration.setExtractedOutput(extractedOutput);
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
