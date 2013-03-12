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
        outputNl.setFileName("Metadata-in-dutch");
        outputNl.setTxtFilePath("/Volumes/Holdtank/Experiments/ERSi/extracted-files/oai-pmh/txt");
        outputNl.setHdfsFilePath("/Volumes/Holdtank/Experiments/ERSi/extracted-files/oai-pmh/hdfs/tmp");
        
        OaiPmhReposConfig oaiPmhRepos = new OaiPmhReposConfig();
        LocalSourceDataConfig localSource = new LocalSourceDataConfig();
        ExtractedOutput extractedOutput = new ExtractedOutput();
       
        OutputFileConfig outputEn = new OutputFileConfig();
        outputEn.setFileName("Metadata-in-english");
        outputEn.setTxtFilePath("/Volumes/Holdtank/Experiments/ERSi/extracted-files/oai-pmh/txt");
        
        Map<String, OutputFileConfig> map = new HashMap<String, OutputFileConfig>();
        map.put("nl",outputNl);
        map.put("en",outputEn);
        
        extractedOutput.setOutputFileConfig(map);
        
        //OAI-PMH Setting
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
        
        //Local Setting 
        Field selectedFieldUrn = new Field();
        selectedFieldUrn.setDetectLang(false);
        selectedFieldUrn.setName("urn");
        Field selectedFieldData = new Field();
        selectedFieldData.setDetectLang(false);
        selectedFieldData.setName("data_abr");
        
        List<Field> selectedLocalFields = new ArrayList<Field>();
        selectedLocalFields.add(selectedFieldUrn);
        selectedLocalFields.add(selectedFieldData);
        localSource.setSelectedFields(selectedLocalFields);
        localSource.setFileName("tsd-eko");
        localSource.setFilePath("/Users/akmi/Dropbox/THESIS/Sharing-thesis-project/TODO");
        localSource.setFileType("txt");
        
        Configuration configuration = new Configuration("nl", "nl");
        
        DataExtractionConfig dep = new DataExtractionConfig();
        configuration.setDataExtractionConfig(dep);
        
        dep.setOaiPmhReposConfig(oaiPmhRepos);
		dep.setLocalSource(localSource);
        dep.setExtractedOutput(extractedOutput);
        Report report = new Report();
        report.setName("data-extraction-report");
        report.setPath("/Volumes/Holdtank/Experiments/ERSi/report");
		dep.setReport(report);
        
        File result = new File("/Volumes/Holdtank/Experiments/ERSi/conf/configuration.xml");

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
