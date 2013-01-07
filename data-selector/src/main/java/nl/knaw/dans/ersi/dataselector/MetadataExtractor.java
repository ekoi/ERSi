package nl.knaw.dans.ersi.dataselector;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import org.dom4j.Element;
import org.dom4j.Node;

import se.kb.oai.OAIException;
import se.kb.oai.pmh.Header;
import se.kb.oai.pmh.OaiPmhServer;
import se.kb.oai.pmh.Record;
import se.kb.oai.pmh.RecordsList;
import se.kb.oai.pmh.ResumptionToken;

/**
 * Hello world!
 * 
 */
public class MetadataExtractor {
	private String oaipmhServerURL;
	private String outputFileName;
	private String oaipmhSetValue;
	private static int numberOfRecords;
	private static int numberOfResumption;
	
	public MetadataExtractor(String oaipmhServerURL, String outpuFileName, String oaipmhSetValue) {
		this.oaipmhServerURL = oaipmhServerURL;
		this.outputFileName = outpuFileName;
		this.oaipmhSetValue = oaipmhSetValue;
	}
	
	public static void main(String[] args) {
		// OaiPmhServer("https://easy.dans.knaw.nl/oai?verb=ListRecords&metadataPrefix=oai_dc&set=D30000:D34000");
		MetadataExtractor me = new MetadataExtractor("https://easy.dans.knaw.nl/oai", "datasetId-title-description", null);
		me.extract();

	}
	
	public void extract(){
		OaiPmhServer server = new OaiPmhServer(getOaipmhServerURL());
		PrintWriter outFile = null;
		try {
			File extractedOutputDirectory = new File("data-from-easy");
			if (extractedOutputDirectory.exists()) {
				boolean deleteDirectory = deleteDir(extractedOutputDirectory);
				if (!deleteDirectory)
					System.out
							.println("Cannot delete the existing extracted-output directory.");
			}
			boolean success = extractedOutputDirectory.mkdir();
			if (!success)
				System.out.println("Cannot create a directory.");
			outFile = new PrintWriter(new FileWriter(extractedOutputDirectory.getAbsolutePath() + "/" + getOutputFileName() + ".txt"));
			RecordsList records = server.listRecords("oai_dc", null, null, getOaipmhSetValue());
			boolean more = true;
			while (more) {
				numberOfResumption++;
				for (Record record : records.asList()) {
					Element element = record.getMetadata();
					Header header = record.getHeader();
					String identifier = header.getIdentifier();
					System.out.println("identifier: " + identifier);
					String easyIdentifier[] = identifier.split("easy-dataset:");
					System.out.println("identifier: " + easyIdentifier[1]);
					if (element != null ) {
						Node nodeTitle = element.selectSingleNode("./dc:title");
						Node nodeDescription = element.selectSingleNode("./dc:description");
						if (nodeTitle != null && nodeDescription != null) {
							String text = easyIdentifier[1] + "|###|" + nodeTitle.getStringValue() + " " + nodeDescription.getStringValue();
								outFile.println(text);
							numberOfRecords++;
						}
					}
				}

				if (records.getResumptionToken() != null) {
					ResumptionToken rt = records.getResumptionToken();
					System.out.println("=========== " + numberOfResumption + "  " + rt.getId() + " ===========");
					try {
						Thread.sleep(2000);

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
					System.out.println(report);
				}
			}

		} catch (OAIException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
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
