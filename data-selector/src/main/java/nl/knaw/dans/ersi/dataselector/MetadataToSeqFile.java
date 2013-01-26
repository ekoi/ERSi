package nl.knaw.dans.ersi.dataselector;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;
import org.apache.hadoop.io.SequenceFile;
import org.apache.hadoop.io.Text;
import org.apache.mahout.common.HadoopUtil;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

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
	private static int numberOfIds;
	private static int numberOfEnRecords;
	private static int numberOfNlRecords;
	private static int numberOfDeRecords;
	private static int numberOfFrRecords;
	private static int numberOfUNRecords;
	private static int numberOfResumption;
	private static int numberOfAllWords;
	private static int numberOfWords3;
	private static int numberOfWords5;
	private static int numberOfWords10;
	private static int numberOfWords3Nl;
	private static int numberOfWords5Nl;
	private static int numberOfWords10Nl;
	private static int numberOfWords3En;
	private static int numberOfWords5En;
	private static int numberOfWords10En;
	private static List<String> listOfWords3Nl = new ArrayList<String>();
	private static List<String> listOfWords5Nl = new ArrayList<String>();
	private static List<String> listOfWords10Nl = new ArrayList<String>();
	private static List<String> listOfWords3En = new ArrayList<String>();
	private static List<String> listOfWords5En = new ArrayList<String>();
	private static List<String> listOfWords10En = new ArrayList<String>();
	private static List<String> listOfWordsDe = new ArrayList<String>();
	private static List<String> listOfWordsFr = new ArrayList<String>();
	
	
	public MetadataToSeqFile(String oaipmhServerURL, String outpuFileName, String oaipmhSetValue, String[] elementNames) {
		this.oaipmhServerURL = oaipmhServerURL;
		this.outputFileName = outpuFileName;
		this.oaipmhSetValue = oaipmhSetValue;
	}
	
	public static void main(String[] args) {
		// OaiPmhServer("https://easy.dans.knaw.nl/oai?verb=ListRecords&metadataPrefix=oai_dc&set=D30000:D34000");
		MetadataToSeqFile seme = new MetadataToSeqFile("https://easy.dans.knaw.nl/oai", "datasetId-title-description.seq",null
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
			
			
			Path seqDir = new Path("seqfiles-from-easy-20130126-2308");
			String uri = seqDir.getName()+"/" + outputFileName;
	        Configuration conf = new Configuration();
	        HadoopUtil.delete(conf, seqDir);
	        FileSystem fs = FileSystem.get(URI.create(uri), conf);
	        Path path = new Path( uri);
	        Text key = new Text();
	        Text value = new Text();
	       
	        writer = SequenceFile.createWriter( fs, conf, path, key.getClass(), value.getClass());
	        
			RecordsList records = server.listRecords("oai_dc", null, null, getOaipmhSetValue());
			/*
			 temp: write response to a file or system.out for debugging.
			// Pretty print the document to System.out
	        OutputFormat format = OutputFormat.createPrettyPrint();
	        XMLWriter w = new XMLWriter( new FileWriter( "/Users/akmi/oai-dc.xml" ) );
	        //XMLWriter w = new XMLWriter( System.out, format );
	        w.write( records.getResponse() );
	        */
			
			
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
							numberOfIds ++;
							String text = nodeTitle.getStringValue() + " " + nodeDescription.getStringValue();
							text = text.replace(",", "");
							int numberOfWords = text.split(" ").length;
							numberOfAllWords += numberOfWords;
							String id = identifier.split("easy-dataset:")[1];
							
							// detect the language of the text
							String language = dl.detect(text);
							if (language.equals(LanguageRecognition.NL)) {
								key.set(identifier);
								value.set(text);
								writer.append( key, value);
								numberOfEnRecords++;
								
								if (numberOfWords < 3 ) {
									listOfWords3Nl.add(id);
									numberOfWords3Nl++;
								} else if (numberOfWords >= 3 && numberOfWords < 5){
									listOfWords5Nl.add(id);
									numberOfWords5Nl++;
								} else if (numberOfWords >= 5 && numberOfWords < 10) {
									listOfWords10Nl.add(id);
									numberOfWords10Nl++;
								}
							} else if (language.equals(LanguageRecognition.EN)) {
								numberOfNlRecords++;
								if (numberOfWords < 3 ) {
									listOfWords3En.add(id);
									numberOfWords3En++;
								}else if (numberOfWords >= 3 && numberOfWords < 5){
									listOfWords5En.add(id);
									numberOfWords5En++;
								}else if (numberOfWords >= 5 && numberOfWords < 10){
									numberOfWords10En++;
									listOfWords10En.add(id);
								}
								
							} else if (language.equals(LanguageRecognition.FR)) {
								listOfWordsFr.add(id);
								numberOfFrRecords++;
							} else if (language.equals(LanguageRecognition.DE)) {
								listOfWordsDe.add(id);
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
						Thread.sleep(3000);

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
					report+= "Number of processed ids: " + numberOfIds + "\n";
					report+= "Number of NL records: " + numberOfNlRecords + "\n";
					report+= "Number of EN records: " + numberOfEnRecords + "\n";
					report+= "Number of DE records: " + numberOfDeRecords + "\n";
					report+= "Number of FR records: " + numberOfFrRecords + "\n";
					report+= "Number of UN records: " + numberOfUNRecords + "\n";
					report+= "Number of NL records less than 3: " + numberOfWords3Nl + "\n";
					report+= "NL3: " + listOfWords3Nl.size() + "\n";
					report+= "Number of NL records at least 3 and less than 5: " + numberOfWords5Nl + "\n";
					report+= "NL5: " + listOfWords5Nl.size() + "\n";
					report+= "Number of NL records at least 5 and less than 10: " + numberOfWords10Nl + "\n";
					report+= "NL10: " + listOfWords10Nl.size() + "\n";
					report+= "Number of EN records less than 3: " + numberOfWords3En + "\n";
					report+= "EN3: " + listOfWords3En.size() + "\n";
					report+= "Number of EN records at least 3 and less than 5: " + numberOfWords5En + "\n";
					report+= "EN5: " + listOfWords5En.size() + "\n";
					report+= "Number of EN records at least 5 and less than 10: " + numberOfWords10En + "\n";
					report+= "EN10: " + listOfWords10En.size() + "\n";
					report+= "========================================\n";
					report+= "List NL3: " + listOfWords3Nl + "\n";
					report+= "List NL5: " + listOfWords5Nl + "\n";
					report+= "List NL10: " + listOfWords10Nl + "\n";
					report+= "List EN3: " + listOfWords3En + "\n";
					report+= "List EN5: " + listOfWords5En + "\n";
					report+= "List EN10: " + listOfWords10En + "\n";
					report+= "========================================\n";
					report+= "List DE: " + listOfWordsDe + "\n";
					report+= "List FR: " + listOfWordsFr + "\n";
					report+= "========================================\n";
					report+= "TOTAL WORDS: " + numberOfAllWords;
					report+= "******** FINISH  ********\n";
					System.out.println(report);
					
				}
				
//				if (numberOfResumption>0) {
//					more=false;
//					
//					System.out.println("******** FINISH  ********");
//				}
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
	 * @return the oaipmhSetValue
	 */
	private String getOaipmhSetValue() {
		return oaipmhSetValue;
	}
}
