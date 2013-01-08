package nl.knaw.dans.ersi.dataselector;

import java.io.File;
import java.io.IOException;
import java.net.URI;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;
import org.apache.hadoop.io.SequenceFile;
import org.apache.hadoop.io.Text;
import org.apache.mahout.common.HadoopUtil;
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
 * Request the metadata from easy, extracted and convert to sequence file
 * 
 */
public class MetadataToSeqFile {
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
	
	public MetadataToSeqFile(String oaipmhServerURL, String outpuFileName, String oaipmhSetValue, String[] elementNames) {
		this.oaipmhServerURL = oaipmhServerURL;
		this.outputFileName = outpuFileName;
		this.oaipmhSetValue = oaipmhSetValue;
	}
	
	public static void main(String[] args) {
		// OaiPmhServer("https://easy.dans.knaw.nl/oai?verb=ListRecords&metadataPrefix=oai_dc&set=D30000:D34000");
		MetadataToSeqFile seme = new MetadataToSeqFile("https://easy.dans.knaw.nl/oai", "datasetId-title-description",null
				, new String[]{"dc:title", "dc:description"});
		seme.extract();

	}
	
	public void extract(){
		OaiPmhServer server = new OaiPmhServer(getOaipmhServerURL());
		SequenceFile.Writer writer = null;
		try {
			//File extractedOutputDirectory = new File("seqfiles-from-easy");
//			if (extractedOutputDirectory.exists()) {
//				boolean deleteDirectory = deleteDir(extractedOutputDirectory);
//				if (!deleteDirectory)
//					System.out
//							.println("Cannot delete the existing extracted-output directory.");
//			}
//			boolean success = extractedOutputDirectory.mkdir();
//			if (!success)
//				System.out.println("Cannot create a directory.");
			
			
			Path seqDir = new Path("seqfiles-from-easy");
			String uri = seqDir.getName()+"/nlEasy.seq";
	        Configuration conf = new Configuration();
	        HadoopUtil.delete(conf, seqDir);
	        FileSystem fs = FileSystem.get(URI.create(uri), conf);
	        Path path = new Path( uri);
	        Text key = new Text();
	        Text value = new Text();
	       
	        writer = SequenceFile.createWriter( fs, conf, path, key.getClass(), value.getClass());
	        
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
								key.set(identifier);
								value.set(text);
								writer.append( key, value);
								numberOfEnRecords++;
							} else if (language.equals(LanguageRecognition.EN)) {
								
								numberOfNlRecords++;
							} else if (language.equals(LanguageRecognition.FR)) {
								
								numberOfFrRecords++;
							} else if (language.equals(LanguageRecognition.DE)) {
								
								numberOfDeRecords++;
							} else {
								
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
				
				if (numberOfResumption>0) {
					more=false;
					
					System.out.println("******** FINISH  ********");
				}
			}

		} catch (OAIException e) {
			e.printStackTrace();
		} catch (LangDetectException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			IOUtils.closeStream( writer); 
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
