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
	private String errorMessage;
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
        
		
		//Data cleaning configuration
		DataCleansingConfig dcc = new DataCleansingConfig();
		SimpleDimensionReductionConfig sdr = new SimpleDimensionReductionConfig();
		sdr.setMinWordLength(3);
		List<String> skipWords = new ArrayList<String>();
		skipWords.add("onderzoeksrapport");
		sdr.setSkipWord(skipWords);
		dcc.setSimpleDimensionReduction(sdr);
		
		dcc.setInputDirectory("/Volumes/Holdtank/Experiments/ERSi/extracted-files/oai-pmh/hdfs/tmp");
		dcc.setOutputDirectory("/Volumes/Holdtank/Experiments/ERSi/data-cleansing-result/local/output-vector");
		
		configuration.setDataCleansingConfig(dcc);
        File result = new File("/Users/akmi/Dropbox/THESIS/Sources/Eclipse/workspace/ERSi/config/src/resources/configuration.xml");

        try {
			serializer.write(configuration, result);
		} catch (Exception e) {
			LOG.error(e.getMessage());
		}
        LOG.debug("Configuration fiel is created. Location: " + result.getAbsolutePath());
    }
    
    public boolean saveStringAsXml(String input, String xmlfile) {
    	boolean success = false;
    	Serializer serializer = new Persister(new Format("<?xml version=\"1.0\" encoding= \"UTF-8\" ?>")); 
    	try {
    		success = serializer.validate(Configuration.class, input); 
 			if (!success)
 				return success;
 			Configuration c = serializer.read(Configuration.class, input);
 			File file = new File(xmlfile);
    		serializer.write(c, file);
    		return true;
 		} catch (Exception e) {
 			setErrorMessage(e.getMessage());
 			LOG.error(e.getMessage());
 		}
    	return false;
    }

	/**
	 * @return the errorMessage
	 */
	public String getErrorMessage() {
		return errorMessage;
	}

	/**
	 * @param errorMessage the errorMessage to set
	 */
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
}
