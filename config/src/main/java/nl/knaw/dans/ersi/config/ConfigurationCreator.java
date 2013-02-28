package nl.knaw.dans.ersi.config;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;
import org.simpleframework.xml.stream.Format;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * ConfigurationCreator
 *
 */
public class ConfigurationCreator 
{
	private static Logger LOG = LoggerFactory.getLogger(ConfigurationCreator.class);
    public static void main( String[] args )
    {
    	 
    	 LOG.debug("Create configuration.xml file");
        Serializer serializer = new Persister(new Format("<?xml version=\"1.0\" encoding= \"UTF-8\" ?>"));
        
       
        OutputFileConfig outputNl = new OutputFileConfig();
        outputNl.setFileName("metadata in Dutch");
        outputNl.setTxtFilePath("./output/txt");
        outputNl.setHdfsFilePath("./output/hdfs");
        
        OaiPmhReposConfig oaiPmhRepos = new OaiPmhReposConfig();
        ExtractedOutput extractedOutput = new ExtractedOutput();
       
        OutputFileConfig outputEn = new OutputFileConfig();
        outputEn.setFileName("metadata in English");
        outputEn.setTxtFilePath("./output/txt");
        outputEn.setHdfsFilePath("./output/hdfs");
        
        Map<String, OutputFileConfig> map = new HashMap<String, OutputFileConfig>();
        map.put("nl",outputNl);
        map.put("en",outputNl);
        
        extractedOutput.setOutputFileConfig(map);
        
         Field selectedFieldTitle = new Field();
         selectedFieldTitle.setName("dc:title");
         Field selectedFieldDesc = new Field();
         selectedFieldDesc.setName("dc:description");
         Field selectedFieldSub = new Field();
         selectedFieldSub.setName("dc:subject");
         selectedFieldSub.setDetectLang(false);
         Field selectedFieldCov = new Field();
         selectedFieldCov.setName("dc:coverage");
         selectedFieldCov.setDetectLang(false);
         
        List<Field> selectedFields = new ArrayList<Field>();
        selectedFields.add(selectedFieldTitle);
        selectedFields.add(selectedFieldDesc);
        selectedFields.add(selectedFieldSub);
        selectedFields.add(selectedFieldCov);
         
        oaiPmhRepos.setSelectedFields(selectedFields);
        Configuration configuration = new Configuration("nl", "nl");
        
        DataExtractionConfig dep = new DataExtractionConfig();
        configuration.setDataExtractionConfig(dep);
        
        dep.setOaiPmhReposConfig(oaiPmhRepos);
        dep.setExtractedOutput(extractedOutput);
        Report report = new Report();
        report.setName("taikucing-report");
        report.setPath("");
		dep.setReport(report);
        
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
