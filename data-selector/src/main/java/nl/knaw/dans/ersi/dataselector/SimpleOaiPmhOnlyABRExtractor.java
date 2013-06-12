package nl.knaw.dans.ersi.dataselector;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import nl.knaw.dans.ersi.config.ConfigurationReader;
import nl.knaw.dans.ersi.config.Constants;
import nl.knaw.dans.ersi.config.DataExtractionConfig;
import nl.knaw.dans.ersi.config.ExtractedOutputConfig;
import nl.knaw.dans.ersi.config.FieldConfig;
import nl.knaw.dans.ersi.config.OaiPmhReposConfig;
import nl.knaw.dans.ersi.config.OutputFileConfig;
import nl.knaw.dans.ersi.dataselector.util.CreateABRList;
import nl.knaw.dans.ersy.process.controller.utils.ProcessStatus;
import nl.knaw.dans.ersy.process.controller.utils.ProcessStatus.ProcessName;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;
import org.apache.hadoop.io.SequenceFile;
import org.apache.hadoop.io.SequenceFile.Writer;
import org.apache.hadoop.io.Text;
import org.apache.mahout.common.HadoopUtil;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
public class SimpleOaiPmhOnlyABRExtractor extends SimpleExtractor {
	
	private static Logger LOG = LoggerFactory.getLogger(SimpleOaiPmhOnlyABRExtractor.class);
	private static int numberOfResumption;
	private static int numberOfRecords;
	private static int numberOfNl;
	private static int numberOfEn;
	private static int numberOfOther;
	private static int numbeerOfNlWords;

	private boolean oaiPmhXmlDebug;
	
	private List<String> theABRlist;

	public SimpleOaiPmhOnlyABRExtractor(ConfigurationReader getConfReaderg) {
		super(getConfReaderg);
	}

	public void extract() throws OAIException, IOException, LangDetectException {
		LOG.info("=== START ===");
		theABRlist = CreateABRList.getABRList();
		ProcessStatus processStatus = new ProcessStatus(ProcessName.DATA_EXTRACTION, Constants.ERSY_HOME);
		boolean b = processStatus.writeCurrentStatus();
		LOG.debug("Status start is : " + b);
		OaiPmhReposConfig oaiPmhReposconfig = getDataExtractionConfig()
				.getOaiPmhReposConfig();
		OaiPmhServer server = new OaiPmhServer(oaiPmhReposconfig.getBaseUrl());

		RecordsList records = server.listRecords(
				oaiPmhReposconfig.getMetadataPrefix(), null, null,
				oaiPmhReposconfig.getSet());

		if (oaiPmhXmlDebug) {
			OutputFormat format = OutputFormat.createPrettyPrint();
			XMLWriter ww = new XMLWriter(System.out, format);
			ww.write(records.getResponse());
			// XMLWriter w = new XMLWriter( new FileWriter(
			// "/Users/akmi/oai-dc.xml"
			// ) );
			// w.write( records.getResponse() );
		}

		saveFile(server, records);
		
		NumberFormat formatter = new DecimalFormat("#,###");
		formatter = NumberFormat.getInstance(new Locale("nl"));
		StringBuffer sb = new StringBuffer("<table>");
		sb.append("<tr><td>Last extration date:</td><td>" + processStatus.giveTimeLastProcess().replace("Last Data Extraction process is finished at ", "") + "</td></tr>");
		sb.append("<tr><td>Archaeology datasets:</td><td>" + formatter.format(numberOfRecords) + "</td></tr>");
		sb.append("<tr><td>Datasets in Dutch:</td><td>" + formatter.format(numberOfNl) + "</td></tr>");
		sb.append("<tr><td>Datasets in English:</td><td>" + formatter.format(numberOfEn) + "</td></tr>");
		sb.append("<tr><td>Datasets in Other:</td><td>" + formatter.format(numberOfOther) + "</td></tr>");
		sb.append("<tr><td>Total Words in Dutch Datasets:</td><td>" + formatter.format(numbeerOfNlWords) + "</td></tr>");
		sb.append("</table>");
		
		processStatus.writeReport(getConfReader().getErsyHome() + "/" + getReportConfig().getPath() + "/" + getReportConfig().getName(), sb.toString());
		LOG.debug("Number of Total records: " + numberOfRecords);
		LOG.debug("Number of Total NL records: " + numberOfNl);
		LOG.debug("Number of Total EN records: " + numberOfEn);
		LOG.debug("Number of Total other records: " + numberOfOther);
		LOG.debug("Number of Total words (Text in Dutch): " + numbeerOfNlWords);
		boolean b2 = processStatus.writeLastStatus();
		LOG.debug("Extraction status - write last status: " + b2);
		boolean b3 = processStatus.writeDoneStatus();
		LOG.debug("Extraction status - write DONE status: " + b3);
	}

	/**
	 * @param oaiPmhRepos
	 * @param server
	 * @param records
	 * @throws OAIException
	 * @throws LangDetectException
	 * @throws IOException
	 */
	private void saveFile(OaiPmhServer server, RecordsList records)
			throws LangDetectException, OAIException {
		  
		LanguageRecognition dl = new LanguageRecognition();
		OaiPmhReposConfig oaiPmhReposConfig = getDataExtractionConfig()
				.getOaiPmhReposConfig();

		ExtractedOutputConfig extractedOutput = getDataExtractionConfig()
				.getExtractedOutput();
		SequenceFile.Writer writer = null;
		Map<String, OutputFileConfig> outputFileConf = extractedOutput
				.getOutputFileConfig();

		Set<String> set = outputFileConf.keySet();

		Map<String, Writer> writers = new HashMap<String, Writer>();
		try {
			for (String lang : set) {
				OutputFileConfig ofc = outputFileConf.get(lang);
				if (ofc.getHdfsFilePath() != null) {
					Path seqDir = new Path(ofc.getHdfsFilePath());
					String uri = ofc.getHdfsFilePath() + "/" + ofc.getFileName() + ".seq";
					Configuration conf = new Configuration();
					HadoopUtil.delete(conf, seqDir);
					FileSystem fs = FileSystem.get(URI.create(uri), conf);
					Path path = new Path(uri);
					writer = SequenceFile.createWriter(fs, conf, path,
							Text.class, Text.class);
					writers.put(lang, writer);
				}
			}

			boolean more = true;
			while (more) {
				numberOfResumption++;

				numberOfRecords += records.size();
				for (Record record : records.asList()) {

					Element element = record.getMetadata();
					Header header = record.getHeader();
					String identifier = header.getIdentifier();
					if (element != null) {
						List<Node> identifiers = element.selectNodes("./dc:identifier");
						for (Node n : identifiers) {
							String s = n.getStringValue();
							if (s != null && !s.isEmpty() ) {
								if (s.toLowerCase().startsWith("urn:nbn:nl:ui")) {
									identifier = s;
									break;
								}
							} else {
								LOG.debug("No URN found");
							}
						}
						StringBuffer text = new StringBuffer();
						StringBuffer textToDetect = new StringBuffer();
						List<FieldConfig> selectedFields = oaiPmhReposConfig
								.getSelectedFields();
						for (FieldConfig field : selectedFields) {
							List<Node> nodes = element.selectNodes("./"
									+ field.getName());
							for (Node node : nodes) {
								String val = node.getStringValue() + " ";
								if (field.isDetectLang()) {
									textToDetect.append(val);
								}
								text.append(val);
							}
						}
						String language = dl.detect(textToDetect.toString());
						if (language.equals(LanguageRecognition.NL)) {
							Writer write = writers.get(LanguageRecognition.NL);
							Text key = new Text();
							Text value = new Text();
							key.set(identifier);
							String extractedText = text.toString().toLowerCase();
							numbeerOfNlWords += extractedText.split(" ").length;
							String[] ss = extractedText.split(" ");
							StringBuffer extractedTextABR = new StringBuffer();
							for (String sss : ss) {
								if (theABRlist.contains(sss)) {
									extractedTextABR.append(sss);
									extractedTextABR.append(" ");
								}
							}
							
							value.set(extractedTextABR.toString());
							write.append(key, value);
							numberOfNl++;
						} else if (language.equals(LanguageRecognition.EN)) {
							numberOfEn++;
							LOG.debug("pid EN identifier: " + identifier);
							LOG.debug("pid EN text: " + textToDetect.toString());
						} else {
							numberOfOther++;
							LOG.debug("pid OTHER: " + identifier);
							LOG.debug("pid OTHER text: " + textToDetect.toString());
						}
					}
				}
				if (records.getResumptionToken() != null) {
					ResumptionToken rt = records.getResumptionToken();
					LOG.debug(rt.getId());
					LOG.info("Number of resuption token: " + numberOfResumption);
					LOG.info("Number of records: " + numberOfRecords);
						Thread.sleep(5000);
					records = server.listRecords(rt);
				} else {
					more = false;
				}
			}
		} catch (IOException e) {

		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			Set<String> keys = writers.keySet();
			for (String key : keys) {
				SequenceFile.Writer w = writers.get(key);
				if (w != null)
					IOUtils.closeStream(w);
			}
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

}
