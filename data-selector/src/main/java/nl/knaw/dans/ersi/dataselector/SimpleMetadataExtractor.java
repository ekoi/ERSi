package nl.knaw.dans.ersi.dataselector;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;

import org.dom4j.Element;
import org.dom4j.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import se.kb.oai.OAIException;
import se.kb.oai.pmh.Header;
import se.kb.oai.pmh.OaiPmhServer;
import se.kb.oai.pmh.Record;
import se.kb.oai.pmh.RecordsList;
import se.kb.oai.pmh.ResumptionToken;

import com.cybozu.labs.langdetect.LangDetectException;


public class SimpleMetadataExtractor {
	
	private static Logger LOG = LoggerFactory.getLogger(SimpleMetadataExtractor.class);
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
	
	private boolean validateUserInput(List<String> input) {
		boolean valid = false;
		if (input == null || input.size() < 2)
			return false;
		for (String s : input) {
			if (s.indexOf("=") == -1 || !s.startsWith("-")){
				LOG.debug(s + " is illegal argument!");
				break;
			}
				
		}
		return valid;
	}
	
	public SimpleMetadataExtractor(String oaipmhServerURL, String outpuFileName, String oaipmhSetValue, String[] elementNames) {
		this.oaipmhServerURL = oaipmhServerURL;
		this.outputFileName = outpuFileName;
		this.oaipmhSetValue = oaipmhSetValue;
		this.elementNames = Arrays.asList(elementNames);
	}
	
	public static void main(String[] args) {
		// OaiPmhServer("https://easy.dans.knaw.nl/oai?verb=ListRecords&metadataPrefix=oai_dc&set=D30000:D34000");
		SimpleMetadataExtractor seme = new SimpleMetadataExtractor("https://easy.dans.knaw.nl/oai", "textdata", "D30000:D37000"
				, new String[]{"dc:title", "dc:description"});
		seme.extract();

	}
	
	public void extract(){
		OaiPmhServer server = new OaiPmhServer(getOaipmhServerURL());
		PrintWriter outNLFile = null;
		PrintWriter outENFile = null;
		PrintWriter outDEFile = null;
		PrintWriter outFRFile = null;
		PrintWriter outFile = null;
		PrintWriter outUNFile = null;
		try {
			File extractedOutputDirectory = new File("extracted-outputdata-only-description");
			if (extractedOutputDirectory.exists()) {
				boolean deleteDirectory = deleteDir(extractedOutputDirectory);
				if (!deleteDirectory)
					LOG.debug("Cannot delete the existing extracted-output directory.");
			}
			boolean success = extractedOutputDirectory.mkdir();
			if (!success)
				LOG.debug("Cannot create a directory.");
			outNLFile = new PrintWriter(new FileWriter(extractedOutputDirectory.getAbsolutePath() + "/" + getOutputFileName() + "-nl.txt"));
			outENFile = new PrintWriter(new FileWriter(extractedOutputDirectory.getAbsolutePath() + "/" + getOutputFileName() + "-en.txt"));
			outDEFile = new PrintWriter(new FileWriter(extractedOutputDirectory.getAbsolutePath() + "/" + getOutputFileName() + "-de.txt"));
			outFRFile = new PrintWriter(new FileWriter(extractedOutputDirectory.getAbsolutePath() + "/" + getOutputFileName() + "-fr.txt"));
			outUNFile = new PrintWriter(new FileWriter(extractedOutputDirectory.getAbsolutePath() + "/" + getOutputFileName() + "-un.txt"));
			outFile = new PrintWriter(new FileWriter(extractedOutputDirectory.getAbsolutePath() + "/" + getOutputFileName() + ".txt"));
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
					Node nodeTitle = element.selectSingleNode("./dc:title");
					Node nodeDescription = element.selectSingleNode("./dc:description");
					String text = /*"[" + identifier + "] " + nodeTitle.getStringValue() + " " + */nodeDescription.getStringValue();
					extractElements(outNLFile, outENFile, outDEFile, outFRFile, outUNFile, outFile, dl, identifier, text.toLowerCase());
				}

				if (records.getResumptionToken() != null) {
					ResumptionToken rt = records.getResumptionToken();
					LOG.debug("=========== " + numberOfResumption + "  " + rt.getId() + " ===========");
					try {
						Thread.sleep(5000);

					} catch (InterruptedException ie) {
						LOG.debug(ie.getMessage());
					}
					records = server.listRecords(rt);
				}
				else {
					more = false;
					LOG.debug("******** FINISH  ********");
					String report = "Number of resumption tokens: " + numberOfResumption + "\n";
					report+= "Number of processed records: " + numberOfRecords + "\n";
					report+= "Number of NL records: " + numberOfNlRecords + "\n";
					report+= "Number of EN records: " + numberOfEnRecords + "\n";
					report+= "Number of DE records: " + numberOfDeRecords + "\n";
					report+= "Number of FR records: " + numberOfFrRecords + "\n";
					report+= "Number of UN records: " + numberOfUNRecords + "\n";
					
					LOG.debug(report);
					
					outFile.println("************* REPORT **************");
					outFile.println(report);
				}
			}

		} catch (OAIException e) {
			LOG.error(e.getMessage());
		}catch (LangDetectException e) {
			LOG.error(e.getMessage());
		} catch (IOException e) {
			LOG.error(e.getMessage());
		} finally {
			if (outNLFile != null)
				outNLFile.close();
			if (outENFile != null)
				outENFile.close();
			if (outDEFile != null)
				outDEFile.close();
			if (outFRFile != null)
				outFRFile.close();
			if (outUNFile != null)
				outUNFile.close();
			if (outFile != null)
				outFile.close();

		}
	}

	public void extractElements(PrintWriter outNLFile, PrintWriter outENFile, PrintWriter outDEFile, PrintWriter outFRFile,
			PrintWriter outUNFile, PrintWriter outFile, LanguageRecognition dl, String identifier, String text) {
					// detect the language of the text
					String language = dl.detect(text);
					if (language.equals(LanguageRecognition.EN)) {
						outENFile.println(numberOfEnRecords + " [" + identifier +"] " + text);
						numberOfEnRecords++;
					} else if (language.equals(LanguageRecognition.NL)) {
						outNLFile.println(text);
						numberOfNlRecords++;
					} else if (language.equals(LanguageRecognition.FR)) {
						outFRFile.println(text);
						numberOfFrRecords++;
					} else if (language.equals(LanguageRecognition.DE)) {
						outDEFile.println(text);
						numberOfDeRecords++;
					}else {
						outUNFile.println(text);
						numberOfUNRecords++;
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

	/**
	 * @return the elementNames
	 */
	private List<String> getElementNames() {
		return elementNames;
	}
}
