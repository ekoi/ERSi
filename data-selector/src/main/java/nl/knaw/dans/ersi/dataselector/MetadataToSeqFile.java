package nl.knaw.dans.ersi.dataselector;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
	
	private static List<String> listOfWords = new ArrayList<String>();
	private static Map<String, Integer> mapOfWords = new HashMap<String, Integer>();
	
	
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
			
			
			Path seqDir = new Path("seqfiles-from-easy-20130205C-1206");
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
			
			FileWriter fw = new FileWriter("title-description-report-20130205C.txt");
            PrintWriter out = new PrintWriter(fw);
			
			FileWriter fwNl = new FileWriter("title-description-NL-20130205C.txt");
            PrintWriter outNl = new PrintWriter(fwNl);
            FileWriter fwEn = new FileWriter("title-description-EN-20130205C.txt");
            PrintWriter outEn = new PrintWriter(fwEn);
            FileWriter fwOther = new FileWriter("title-description-Other-20130205C.txt");
            PrintWriter outOther = new PrintWriter(fwOther);
            
            FileWriter fwNl3 = new FileWriter("title-description-NL3-20130205C.txt");
            PrintWriter outNl3 = new PrintWriter(fwNl3);
            FileWriter fwNl5 = new FileWriter("title-description-NL5-20130205C.txt");
            PrintWriter outNl5 = new PrintWriter(fwNl5);
            FileWriter fwNl10 = new FileWriter("title-description-NL10-20130205C.txt");
            PrintWriter outNl10 = new PrintWriter(fwNl10);
            
            FileWriter fwEn3 = new FileWriter("title-description-NL5-20130205C.txt");
            PrintWriter outEn3 = new PrintWriter(fwEn3);
            FileWriter fwEn5 = new FileWriter("title-description-NL5-20130205C.txt");
            PrintWriter outEn5 = new PrintWriter(fwEn5);
            FileWriter fwEn10 = new FileWriter("title-description-NL10-20130205C.txt");
            PrintWriter outEn10 = new PrintWriter(fwEn10);
            
            
            FileWriter fwNl3id = new FileWriter("title-description-NL3id-20130205C.txt");
            PrintWriter outNl3id = new PrintWriter(fwNl3id);
            FileWriter fwNl5id = new FileWriter("title-description-NL5id-20130205C.txt");
            PrintWriter outNl5id = new PrintWriter(fwNl5id);
            FileWriter fwNl10id = new FileWriter("title-description-NL10id-20130205C.txt");
            PrintWriter outNl10id = new PrintWriter(fwNl10id);
            
            FileWriter fwEn3id = new FileWriter("title-description-NL5id-20130205C.txt");
            PrintWriter outEn3id = new PrintWriter(fwEn3id);
            FileWriter fwEn5id = new FileWriter("title-description-NL5id-20130205C.txt");
            PrintWriter outEn5id = new PrintWriter(fwEn5id);
            FileWriter fwEn10id = new FileWriter("title-description-NL10id-20130205C.txt");
            PrintWriter outEn10id = new PrintWriter(fwEn10id);
            
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
						List<Node> nodeTitles = element.selectNodes("./dc:title");
						Node nodeDescription = element.selectSingleNode("./dc:description");
						List<Node> nodeSubjects = element.selectNodes("./dc:subject");
						List<Node> nodeCoverages = element.selectNodes("./dc:coverage");
						if (nodeTitles != null && nodeDescription != null) {
							numberOfIds ++;
							if (identifier.equals("oai:easy.dans.knaw.nl:easy-dataset:44229")) {
								for (Node n:nodeTitles) {
								System.out.println(n.getStringValue());
								}
							}
							StringBuffer title = new StringBuffer();
							for (Node n:nodeTitles) {
								title.append(n.getStringValue());
								title.append(" ");
							}
							
							StringBuffer subject = new StringBuffer();
							for (Node n:nodeSubjects) {
								subject.append(n.getStringValue());
								subject.append(" ");
							}
							
							StringBuffer coverages = new StringBuffer();
							for (Node n:nodeCoverages) {
								coverages.append(n.getStringValue());
								coverages.append(" ");
							}
							System.out.println("coverages: " + coverages);
							String text = title + "" + subject + coverages + nodeDescription.getStringValue();
							text = text.replace(",", " ");
							//System.out.println(text);
							String textchunks[] = text.split(" ");
							int numberOfWords = textchunks.length;
							for(String s:textchunks) {
								if (!mapOfWords.containsKey(s))
									mapOfWords.put(s, 1);
								else {
									int i = mapOfWords.get(s);
									i = i+1;
								}
									
							}
							numberOfAllWords += numberOfWords;
							String id = identifier.split("easy-dataset:")[1];
							
							// detect the language of the text
							String language = dl.detect(text);
							if (language.equals(LanguageRecognition.NL)) {
								outNl.println(text);

								key.set(identifier);
								value.set(text);
								writer.append( key, value);
								numberOfNlRecords++;
								
								if (numberOfWords < 3 ) {
									outNl3.println(text);
									outNl3.println(id + "#&#" + text);
									listOfWords3Nl.add(id);
									numberOfWords3Nl++;
								} else if (numberOfWords >= 3 && numberOfWords < 5){
									outNl5.println(text);
									outNl5.println(id + "#&#" + text);
									listOfWords5Nl.add(id);
									numberOfWords5Nl++;
								} else if (numberOfWords >= 5 && numberOfWords < 10) {
									outNl10.println(text);
									outNl10.println(id + "#&#" + text);
									listOfWords10Nl.add(id);
									numberOfWords10Nl++;
								}
							} else if (language.equals(LanguageRecognition.EN)) {
								outEn.println(text);
								numberOfEnRecords++;
								if (numberOfWords < 3 ) {
									outEn3.println(text);
									outEn3.println(id + "#&#" + text);
									listOfWords3En.add(id);
									numberOfWords3En++;
								}else if (numberOfWords >= 3 && numberOfWords < 5){
									outEn5.println(text);
									outEn5.println(id + "#&#" + text);
									listOfWords5En.add(id);
									numberOfWords5En++;
								}else if (numberOfWords >= 5 && numberOfWords < 10){
									outEn10.println(text);
									outEn10.println(id + "#&#" + text);
									numberOfWords10En++;
									listOfWords10En.add(id);
								}
								
							} else if (language.equals(LanguageRecognition.FR)) {
								outOther.println(id + "#&#" + text);
								listOfWordsFr.add(id);
								numberOfFrRecords++;
							} else if (language.equals(LanguageRecognition.DE)) {
								outOther.println(id + "#&#" + text);
								listOfWordsDe.add(id);
								numberOfDeRecords++;
							} else {
								outOther.println(id + "#&#" + text);
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
					out.print(report);
					System.out.println(report);
					 outNl.close();
			         fwNl.close(); 
			         outEn.close();
			         fwEn.close(); 
			         outOther.close();
			         fwOther.close(); 
			         
			         out.close();
			         fw.close(); 
			         
			         outNl3.close();
			         fwNl3.close(); 
			         
			         outNl5.close();
			         fwNl5.close(); 
			         outNl10.close();
			         fwNl10.close(); 
			         
			         outEn3.close();
			         fwEn5.close(); 
			         outEn5.close();
			         fwEn5.close();
			         outEn10.close();
			         fwEn10.close(); 
			         
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
