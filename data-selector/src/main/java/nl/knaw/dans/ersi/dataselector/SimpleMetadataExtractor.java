package nl.knaw.dans.ersi.dataselector;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;

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
public class SimpleMetadataExtractor {
	private String oaipmhServerURL;
	private String outputFileName;
	private String oaipmhSetValue;
	private static int numberOfRecords;
	private static int numberOfEnRecords;
	private static int numberOfNlRecords;
	private static int numberOfDeRecords;
	private static int numberOfFrRecords;
	private static int numberOfUNRecords;
	private static int numberOfResumption;
	
	public SimpleMetadataExtractor(String oaipmhServerURL, String outpuFileName, String oaipmhSetValue, String[] elementNames) {
		this.oaipmhServerURL = oaipmhServerURL;
		this.outputFileName = outpuFileName;
		this.oaipmhSetValue = oaipmhSetValue;
		Arrays.asList(elementNames);
	}
	
	public static void main(String[] args) {
		// OaiPmhServer("https://easy.dans.knaw.nl/oai?verb=ListRecords&metadataPrefix=oai_dc&set=D30000:D34000");
		SimpleMetadataExtractor seme = new SimpleMetadataExtractor("https://easy.dans.knaw.nl/oai", "datasetId-title-description",null
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
			File extractedOutputDirectory = new File("extracted-data-from-easy");
			if (extractedOutputDirectory.exists()) {
				boolean deleteDirectory = deleteDir(extractedOutputDirectory);
				if (!deleteDirectory)
					System.out
							.println("Cannot delete the existing extracted-output directory.");
			}
			boolean success = extractedOutputDirectory.mkdir();
			if (!success)
				System.out.println("Cannot create a directory.");
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
					System.out.println("identifier: " + identifier);
					String easyIdentifier[] = identifier.split("easy-dataset:");
					String datasetId = "<###" + easyIdentifier[1] + "###>";
					System.out.println("identifier: " + datasetId);
					if (element != null ) {
						Node nodeTitle = element.selectSingleNode("./dc:title");
						Node nodeDescription = element.selectSingleNode("./dc:description");
						if (nodeTitle != null && nodeDescription != null) {
							String text = nodeTitle.getStringValue() + " " + nodeDescription.getStringValue();
							// detect the language of the text
							String language = dl.detect(text);
							if (language.equals(LanguageRecognition.EN)) {
								outENFile.println(datasetId + text);
								numberOfEnRecords++;
							} else if (language.equals(LanguageRecognition.NL)) {
								outNLFile.println(datasetId + text);
								numberOfNlRecords++;
							} else if (language.equals(LanguageRecognition.FR)) {
								outFRFile.println(datasetId + text);
								numberOfFrRecords++;
							} else if (language.equals(LanguageRecognition.DE)) {
								outDEFile.println(datasetId + text);
								numberOfDeRecords++;
							}else {
								outUNFile.println(datasetId + text);
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
					more = false;
					System.out.println("******** FINISH  ********");
					String report = "Number of resumption tokens: " + numberOfResumption + "\n";
					report+= "Number of processed records: " + numberOfRecords + "\n";
					report+= "Number of NL records: " + numberOfNlRecords + "\n";
					report+= "Number of EN records: " + numberOfEnRecords + "\n";
					report+= "Number of DE records: " + numberOfDeRecords + "\n";
					report+= "Number of FR records: " + numberOfFrRecords + "\n";
					report+= "Number of UN records: " + numberOfUNRecords + "\n";
					
					System.out.println(report);
					
					outFile.println("************* REPORT **************");
					outFile.println(report);
				}
			}

		} catch (OAIException e) {
			e.printStackTrace();
		}catch (LangDetectException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
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
