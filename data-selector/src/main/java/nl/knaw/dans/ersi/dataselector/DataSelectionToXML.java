package nl.knaw.dans.ersi.dataselector;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Element;
import org.dom4j.Node;

import se.kb.oai.OAIException;
import se.kb.oai.pmh.Header;
import se.kb.oai.pmh.OaiPmhServer;
import se.kb.oai.pmh.Record;
import se.kb.oai.pmh.RecordsList;
import se.kb.oai.pmh.ResumptionToken;

import com.cybozu.labs.langdetect.LangDetectException;

/**
 * Hello world!
 * 
 */
public class DataSelectionToXML {
	private String oaipmhServerURL;
	private String outputFileName;
	private String oaipmhSetValue;
	private List<String> elementNames;
	private static int numberOfRecords;
	private static int numberOfEnRecords;
	private static int numberOfNlRecords;
	private static int numberOfDeRecords;
	private static int numberOfFrRecords;
	private static int numberOfUNRecords;
	private static int numberOfResumption;
	
	public DataSelectionToXML(String oaipmhServerURL, String outpuFileName, String oaipmhSetValue, String[] elementNames) {
		this.oaipmhServerURL = oaipmhServerURL;
		this.outputFileName = outpuFileName;
		this.oaipmhSetValue = oaipmhSetValue;
		this.elementNames = Arrays.asList(elementNames);
	}
	
	public static void main(String[] args) {
		// OaiPmhServer("https://easy.dans.knaw.nl/oai?verb=ListRecords&metadataPrefix=oai_dc&set=D30000:D34000");
		DataSelectionToXML seme = new DataSelectionToXML("https://easy.dans.knaw.nl/oai", "datasetId-title-description",null
				, new String[]{"dc:title", "dc:description"});
		seme.extract();

	}
	
	public void extract(){
		OaiPmhServer server = new OaiPmhServer(getOaipmhServerURL());

		try {
			File extractedOutputDirectory = new File("xmldata-from-easy");
//			if (extractedOutputDirectory.exists()) {
//				boolean deleteDirectory = deleteDir(extractedOutputDirectory);
//				if (!deleteDirectory)
//					System.out
//							.println("Cannot delete the existing extracted-output directory.");
//			}
//			boolean success = extractedOutputDirectory.mkdir();
//			if (!success)
//				System.out.println("Cannot create a directory.");
			
			Map<String, List<Map<String, String>>> data = new HashMap<String, List<Map<String,String>>>();
			
			List<Map<String, String>> nlData = new ArrayList<Map<String,String>>();	
			data.put(LanguageRecognition.NL, nlData);
			List<Map<String, String>> enData = new ArrayList<Map<String,String>>();
			data.put(LanguageRecognition.EN, enData);
			List<Map<String, String>> frData = new ArrayList<Map<String,String>>();	
			data.put(LanguageRecognition.FR, frData);
			List<Map<String, String>> deData = new ArrayList<Map<String,String>>();	
			data.put(LanguageRecognition.DE, deData);
			List<Map<String, String>> unData = new ArrayList<Map<String,String>>();	
			data.put(LanguageRecognition.UN_RECOGNIZED, unData);
			
			RecordsList records = server.listRecords("oai_dc", null, null, getOaipmhSetValue());
			LanguageRecognition dl = new LanguageRecognition();
			boolean more = true;
			while (more) {
				numberOfResumption++;
				
				numberOfRecords += records.size();
				for (Record record : records.asList()) {
					Element element = record.getMetadata();
					Header header = record.getHeader();
					String identifier = header.getIdentifier();
					System.out.println("identifier: " + identifier);
					if (element != null ) {
						Node nodeTitle = element.selectSingleNode("./dc:title");
						Node nodeDescription = element.selectSingleNode("./dc:description");
						if (nodeTitle != null && nodeDescription != null) {
							String text = nodeDescription.getStringValue();
							// detect the language of the text
							String language = dl.detect(text);
							if (language.equals(LanguageRecognition.NL)) {
								Map<String, String> nlText = new HashMap<String, String>();
								nlText.put(identifier, text);
								nlData.add(nlText);
								numberOfEnRecords++;
							} else if (language.equals(LanguageRecognition.EN)) {
								Map<String, String> enText = new HashMap<String, String>();
								enText.put(identifier, text);
								enData.add(enText);
								numberOfNlRecords++;
							} else if (language.equals(LanguageRecognition.FR)) {
								Map<String, String> frText = new HashMap<String, String>();
								frText.put(identifier, text);
								frData.add(frText);
								numberOfFrRecords++;
							} else if (language.equals(LanguageRecognition.DE)) {
								Map<String, String> deText = new HashMap<String, String>();
								deText.put(identifier, text);
								deData.add(deText);
								numberOfDeRecords++;
							} else {
								Map<String, String> unText = new HashMap<String, String>();
								unText.put(identifier, text);
								unData.add(unText);
								numberOfUNRecords++;
							}
						}
					}
				}

				if (records.getResumptionToken() != null) {
					ResumptionToken rt = records.getResumptionToken();
					System.out.println("=========== " + numberOfResumption + "  " + rt.getId() + " ===========");
					try {
						Thread.sleep(5000);

					} catch (InterruptedException ie) {
						System.out.println(ie.getMessage());
					}
					records = server.listRecords(rt);
				}
				else {
					more=false;
					WriteXMLFile wxf = new WriteXMLFile();
					File xmlOutput = new File(extractedOutputDirectory.getAbsolutePath() + "/" + getOutputFileName()+ ".xml");
					if (xmlOutput.exists())
						xmlOutput.delete();
					
					wxf.createDataElements(data, xmlOutput);
					System.out.println("******** FINISH  ********");
					String report = "Number of resumption tokens: " + numberOfResumption + "\n";
					report+= "Number of processed records: " + numberOfRecords + "\n";
					report+= "Number of NL records: " + numberOfNlRecords + "\n";
					report+= "Number of EN records: " + numberOfEnRecords + "\n";
					report+= "Number of DE records: " + numberOfDeRecords + "\n";
					report+= "Number of FR records: " + numberOfFrRecords + "\n";
					report+= "Number of UN records: " + numberOfUNRecords + "\n";
					
					System.out.println(report);
					
				}
				
//				if (numberOfResumption>2) {
//					more=false;
//					WriteXMLFile wxf = new WriteXMLFile();
//					wxf.createDataElements(data, new File(extractedOutputDirectory.getAbsolutePath() + "/" + getOutputFileName()+ ".xml"));
//					System.out.println("******** FINISH  ********");
//				}
			}

		} catch (OAIException e) {
			e.printStackTrace();
		} catch (LangDetectException e) {
			e.printStackTrace();
		} finally {
			

		}
	}

	
	// Deletes all files and subdirectories under dir.
		// Returns true if all deletions were successful.
		// If a deletion fails, the method stops attempting to delete and returns
		// false.
		public boolean deleteDir(File dir) {
			if (dir.isDirectory()) {
				String[] children = dir.list();
				for (int i = 0; i < children.length; i++) {
					boolean success = deleteDir(new File(dir, children[i]));
					if (!success) {
						return false;
					}
				}
			}

			// The directory is now empty so delete it
			return dir.delete();
		}
	
	/**
	 * @return the oaipmhServerURL
	 */
	private String getOaipmhServerURL() {
		return oaipmhServerURL;
	}

	/**
	 * @return the outputFileName
	 */
	private String getOutputFileName() {
		return outputFileName;
	}

	/**
	 * @return the oaipmhSetValue
	 */
	private String getOaipmhSetValue() {
		return oaipmhSetValue;
	}
}
