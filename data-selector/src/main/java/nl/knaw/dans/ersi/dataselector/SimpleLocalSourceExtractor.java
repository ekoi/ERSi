package nl.knaw.dans.ersi.dataselector;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import nl.knaw.dans.ersi.config.ConfigurationReader;
import nl.knaw.dans.ersi.config.DataExtractionConfig;
import nl.knaw.dans.ersi.config.ExtractedOutputConfig;
import nl.knaw.dans.ersi.config.LocalSourceDataConfig;
import nl.knaw.dans.ersi.config.OutputFileConfig;
import nl.knaw.dans.ersi.dataselector.util.ListRecords;
import nl.knaw.dans.ersi.dataselector.util.Record;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;
import org.apache.hadoop.io.SequenceFile;
import org.apache.hadoop.io.SequenceFile.Writer;
import org.apache.hadoop.io.Text;
import org.apache.mahout.common.HadoopUtil;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import se.kb.oai.OAIException;

import com.cybozu.labs.langdetect.LangDetectException;

/**
 * Request the metadata from easy, extracted and convert to sequence file
 * 
 */
public class SimpleLocalSourceExtractor extends SimpleExtractor {
	
	private static Logger LOG = LoggerFactory.getLogger(SimpleLocalSourceExtractor.class);
	private static int numberOfRecords;
	private static int numberOfNl;
	private static int numberOfEn;
	private static int numberOfOther;

	public SimpleLocalSourceExtractor(ConfigurationReader confReader) {
		super(confReader);
	}


	public void extract() throws OAIException, IOException, LangDetectException {

		LocalSourceDataConfig localSourceDataConfig = getDataExtractionConfig()
				.getLocalSource();

		Serializer serializer = new Persister();
		String fileName = localSourceDataConfig.getFilePath()
				+ "/"
				+ localSourceDataConfig.getFileName() + "."
				+ localSourceDataConfig.getFileType();
		File source = new File(fileName);
		ListRecords listRecords;
		try {
			listRecords = serializer.read(ListRecords.class, source);
			List<Record> records = listRecords.getRecords();
			saveFile(records);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			LOG.error(e.getMessage());
		}
		
		LOG.debug("Records: " + numberOfRecords);
		LOG.debug("NL: " + numberOfNl);
		LOG.debug("EN: " + numberOfEn);
		LOG.debug("Other: " + numberOfOther);
	}

	private void saveFile(List<Record> records) throws LangDetectException{
		LanguageRecognition dl = new LanguageRecognition();
		ExtractedOutputConfig extractedOutput = getDataExtractionConfig()
				.getExtractedOutput();
		SequenceFile.Writer writer = null;
		Map<String, OutputFileConfig> outputFileConf = extractedOutput
				.getOutputFileConfig();

		Set<String> set = outputFileConf.keySet();

		Map<String, SequenceFile.Writer> writers = new HashMap<String, SequenceFile.Writer>();
		try {
		for (String lang : set) {
			OutputFileConfig ofc = outputFileConf.get(lang);
			if (ofc.getHdfsFilePath() != null) {
				numberOfRecords++;
				Path seqDir = new Path(ofc.getHdfsFilePath());
				String uri = ofc.getHdfsFilePath() + "/" + ofc.getFileName() + ".seq";
				Configuration conf = new Configuration();
				HadoopUtil.delete(conf, seqDir);
				FileSystem fs = FileSystem.get(URI.create(uri), conf);
				Path path = new Path(seqDir.getParent() + "/" +uri);
				writer = SequenceFile.createWriter(fs, conf, path, Text.class,
						Text.class);
				writers.put(lang, writer);
			}
		}
		for (Record record : records) {

			String urn = record.getUrn();
			String text = record.getDataAbr() + " " + record.getDataNonAbr();
			if (urn != null && text != null && !text.isEmpty()) {
				String language = dl.detect(text);
				if (language.equals(LanguageRecognition.NL)) {	
					Writer write = writers.get(LanguageRecognition.NL);
					Text key = new Text();
					Text value = new Text();
					key.set(urn);
					value.set(text);
					write.append(key, value);
					numberOfNl++;
				} else if (language.equals(LanguageRecognition.EN)) {
					numberOfEn++;
				} else {
					numberOfOther++;
				}
					
			}
		}
		}catch (IOException e) {
		} finally {
			Set<String> keys = writers.keySet();
			for (String key : keys) {
				SequenceFile.Writer w = writers.get(key);
				if (w != null)
					IOUtils.closeStream(w);
			}
		}
	}
}
