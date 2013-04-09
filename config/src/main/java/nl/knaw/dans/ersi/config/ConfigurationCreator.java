package nl.knaw.dans.ersi.config;

import java.io.File;
import java.io.Serializable;
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
public class ConfigurationCreator implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1048255901585559931L;
	private static Logger LOG = LoggerFactory
			.getLogger(ConfigurationCreator.class);
	private String errorMessage;

	public static void main(String[] args) {

		LOG.debug("Create configuration.xml file");
		Serializer serializer = new Persister(new Format("<?xml version=\"1.0\" encoding= \"UTF-8\" ?>"));

		Configuration configuration = new Configuration();
		
		//Data extraction part
		DataExtractionConfig dep = new DataExtractionConfig();
		
		// OAI-PMH Setting
		OaiPmhReposConfig oaiPmhRepos = new OaiPmhReposConfig();
		oaiPmhRepos.setBaseUrl("http://easy.dans.knaw.nl/oai");
		oaiPmhRepos.setMetadataPrefix("oai_dc");
		oaiPmhRepos.setSet("D30000:D37000");

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
		
		// Local Setting
		LocalSourceDataConfig localSource = new LocalSourceDataConfig();
		localSource.setFileName("tsd-eko");
		localSource.setFilePath("/Users/akmi/Dropbox/THESIS/Sharing-thesis-project/TODO");
		localSource.setFileType("txt");
		
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
		
		
		
		
		//Extracted output part
		ExtractedOutput extractedOutput = new ExtractedOutput();
		
		OutputFileConfig outputNl = new OutputFileConfig();
		outputNl.setFileName("ArchaeologyMetadataInDutch");
		outputNl.setTxtFilePath("/tmp/ersy/data-extraction/archaeology/nl/txt");
		outputNl.setHdfsFilePath("/tmp/ersy/data-extraction/archaeology/nl/hdfs");
		
		Map<String, OutputFileConfig> map = new HashMap<String, OutputFileConfig>();
		map.put("nl", outputNl);
		extractedOutput.setOutputFileConfig(map);
	
		//Extracted output report part
		Report report = new Report();
		report.setName("DataExtractionReport");
		report.setPath("/tmp/ersy/data-extraction/report");
		
		dep.setOaiPmhReposConfig(oaiPmhRepos);
		dep.setLocalSource(localSource);
		dep.setExtractedOutput(extractedOutput);
		dep.setReport(report);
		
		configuration.setDataExtractionConfig(dep);


		// Data cleaning configuration
		DataCleansingConfig dcc = new DataCleansingConfig();
		SimpleDimensionReductionConfig sdr = new SimpleDimensionReductionConfig();
		sdr.setMinWordLength(3);
		List<String> skipWords = new ArrayList<String>();
		skipWords.add("onderzoeksrapport");
		sdr.setSkipWord(skipWords);
		dcc.setSimpleDimensionReduction(sdr);

		dcc.setInputDirectory("/tmp/ersy/data-extraction/archaeology/nl/hdfs");
		dcc.setOutputDirectory("/tmp/ersy/data-cleansing/oai-pmh/vectors");

		configuration.setDataCleansingConfig(dcc);
		
		File result = new File("/Users/akmi/Dropbox/THESIS/Sources/Eclipse/workspace/ERSi/config/src/resources/configuration.xml");

		try {
			serializer.write(configuration, result);
		} catch (Exception e) {
			LOG.error(e.getMessage());
		}
		LOG.debug("Configuration fiel is created. Location: "
				+ result.getAbsolutePath());
	}

	public boolean saveStringAsXml(String input, String xmlfile) {
		boolean success = false;
		Serializer serializer = new Persister(new Format());
		try {
			success = serializer.validate(Configuration.class, input);
			if (!success)
				return success;
			Configuration c = serializer.read(Configuration.class, input);
			c.generateModificationTimeNow();
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
	 * @param errorMessage
	 *            the errorMessage to set
	 */
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
}
