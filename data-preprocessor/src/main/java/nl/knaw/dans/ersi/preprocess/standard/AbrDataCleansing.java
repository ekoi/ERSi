package nl.knaw.dans.ersi.preprocess.standard;

import java.io.IOException;
import java.util.List;

import nl.knaw.dans.ersi.config.ConfigurationReader;
import nl.knaw.dans.ersi.config.Constants;
import nl.knaw.dans.ersi.config.DataCleansingConfig;
import nl.knaw.dans.ersy.process.controller.utils.ProcessStatus;
import nl.knaw.dans.ersy.process.controller.utils.ProcessStatus.ProcessName;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.SequenceFile;
import org.apache.hadoop.io.Text;
import org.apache.lucene.analysis.Analyzer;
import org.apache.mahout.common.HadoopUtil;
import org.apache.mahout.common.Pair;
import org.apache.mahout.math.VectorWritable;
import org.apache.mahout.vectorizer.DictionaryVectorizer;
import org.apache.mahout.vectorizer.DocumentProcessor;
import org.apache.mahout.vectorizer.tfidf.TFIDFConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AbrDataCleansing {
	private static Logger LOG = LoggerFactory.getLogger(AbrDataCleansing.class);
	public AbrDataCleansing() {
	}
  public void run() throws IOException, InterruptedException, ClassNotFoundException {
	  ProcessStatus processStatus = new ProcessStatus(ProcessName.DATA_CLEANING, Constants.ERSY_HOME);
		boolean b = processStatus.writeCurrentStatus();
		LOG.debug("Status start is : " + b);
	ConfigurationReader c = new ConfigurationReader(Constants.ERSY_HOME);
	DataCleansingConfig dcc = c.getDataCleansingConfig();
	int minSupport = dcc.getMinSupport(); //minSupport of the feature to be included
    int minDf = dcc.getMinDf(); //The minimum document frequency.
    int maxDFPercent = dcc.getMaxDFPercent(); //The max percentage of vectors for the DF. Can be used to remove really high frequency features.
    int maxNGramSize = dcc.getMaxNGramSize();
    float minLLRValue = dcc.getMinLLRValue();//minimum threshold to prune ngrams
    int reduceTasks = dcc.getReduceTasks();
    int chunkSize = dcc.getChunkSize();
    int norm = dcc.getNorm();
    boolean sequentialAccessOutput = dcc.isSequentialAccessOutput();
    
    String inputDir = dcc.getInputDirectory();

    Configuration conf = new Configuration();
    FileSystem fs = FileSystem.get(conf);
 
    String outputDir = dcc.getOutputDirectory();
    HadoopUtil.delete(conf, new Path(outputDir));
    Path tokenizedPath = new Path(outputDir,
        DocumentProcessor.TOKENIZED_DOCUMENT_OUTPUT_FOLDER);
    //ErsiAnalyzer analyzer = new ErsiAnalyzer();
    //Tokenize text
    ErsiAbrWhitespaceAnalyzer.minTernLength = dcc.getSimpleDimensionReduction().getMinWordLength();
    ErsiAbrWhitespaceAnalyzer.ignoreWords = dcc.getSimpleDimensionReduction().getSkipWord();
    DocumentProcessor.tokenizeDocuments(new Path(inputDir), ErsiAbrWhitespaceAnalyzer.class
        .asSubclass(Analyzer.class), tokenizedPath, conf);
    
    //Create TF. This value is VERY IMPORTANT: -1.0f
    DictionaryVectorizer.createTermFrequencyVectors(tokenizedPath,
      new Path(outputDir), DictionaryVectorizer.DOCUMENT_VECTOR_OUTPUT_FOLDER, 
      conf, minSupport, maxNGramSize, minLLRValue, -1.0f, true, reduceTasks,
      chunkSize, sequentialAccessOutput, true);
    Pair<Long[], List<Path>> dfData = TFIDFConverter.calculateDF(
    		new Path(outputDir, DictionaryVectorizer.DOCUMENT_VECTOR_OUTPUT_FOLDER),
    	    new Path(outputDir), conf, chunkSize);
    TFIDFConverter.processTfIdf(
      new Path(outputDir , DictionaryVectorizer.DOCUMENT_VECTOR_OUTPUT_FOLDER),
      new Path(outputDir), conf, dfData, minDf, maxDFPercent, 
      norm, true, sequentialAccessOutput, true, reduceTasks);
    
    String vectorsFolder = outputDir + "/tfidf-vectors";
    
    SequenceFile.Reader reader = new SequenceFile.Reader(fs,
        new Path(vectorsFolder, "part-r-00000"), conf);
 
    Text key = new Text();
    VectorWritable value = new VectorWritable();
    int count=0;
    while (reader.next(key, value)) {
    	count++;
      LOG.debug(key.toString() + " = > "
                         + value.get().asFormatString());
    }
    reader.close();
    LOG.debug("Number of processing data: " + count);
    boolean b2 = processStatus.writeLastStatus();
	LOG.debug("Status last is : " + b2);
	boolean b3 = processStatus.writeDoneStatus();
	LOG.debug("Status done is : " + b3);
  }
  
  public static void main(String args[]) throws Exception {
		ConfigurationReader c = new ConfigurationReader();
		DataCleansingConfig dcc = c.getDataCleansingConfig();
		int minSupport = dcc.getMinSupport(); //minSupport of the feature to be included
	    int minDf = dcc.getMinDf(); //The minimum document frequency.
	    int maxDFPercent = dcc.getMaxDFPercent(); //The max percentage of vectors for the DF. Can be used to remove really high frequency features.
	    int maxNGramSize = dcc.getMaxNGramSize();
	    float minLLRValue = dcc.getMinLLRValue();//minimum threshold to prune ngrams
	    int reduceTasks = dcc.getReduceTasks();
	    int chunkSize = dcc.getChunkSize();
	    int norm = dcc.getNorm();
	    boolean sequentialAccessOutput = dcc.isSequentialAccessOutput();
	    
	    String inputDir = dcc.getInputDirectory();

	    Configuration conf = new Configuration();
	    FileSystem fs = FileSystem.get(conf);
	 
	    String outputDir = dcc.getOutputDirectory();
	    HadoopUtil.delete(conf, new Path(outputDir));
	    Path tokenizedPath = new Path(outputDir,
	        DocumentProcessor.TOKENIZED_DOCUMENT_OUTPUT_FOLDER);
	    //ErsiAnalyzer analyzer = new ErsiAnalyzer();
	    //Tokenize text
	    ErsiDutchAnalyzer.minTernLength = dcc.getSimpleDimensionReduction().getMinWordLength();
	    ErsiDutchAnalyzer.ignoreWords = dcc.getSimpleDimensionReduction().getSkipWord();
	    DocumentProcessor.tokenizeDocuments(new Path(inputDir), ErsiDutchAnalyzer.class
	        .asSubclass(Analyzer.class), tokenizedPath, conf);
	    
	    //Create TF. This value is VERY IMPORTANT: -1.0f
	    DictionaryVectorizer.createTermFrequencyVectors(tokenizedPath,
	      new Path(outputDir), DictionaryVectorizer.DOCUMENT_VECTOR_OUTPUT_FOLDER, 
	      conf, minSupport, maxNGramSize, minLLRValue, -1.0f, true, reduceTasks,
	      chunkSize, sequentialAccessOutput, true);
	    Pair<Long[], List<Path>> dfData = TFIDFConverter.calculateDF(
	    		new Path(outputDir, DictionaryVectorizer.DOCUMENT_VECTOR_OUTPUT_FOLDER),
	    	    new Path(outputDir), conf, chunkSize);
	    TFIDFConverter.processTfIdf(
	      new Path(outputDir , DictionaryVectorizer.DOCUMENT_VECTOR_OUTPUT_FOLDER),
	      new Path(outputDir), conf, dfData, minDf, maxDFPercent, 
	      norm, true, sequentialAccessOutput, true, reduceTasks);
	    
	    String vectorsFolder = outputDir + "/tfidf-vectors";
	    
	    SequenceFile.Reader reader = new SequenceFile.Reader(fs,
	        new Path(vectorsFolder, "part-r-00000"), conf);
	 
	    Text key = new Text();
	    VectorWritable value = new VectorWritable();
	    int count=0;
	    while (reader.next(key, value)) {
	    	count++;
	      LOG.debug(key.toString() + " = > "
	                         + value.get().asFormatString());
	    }
	    reader.close();
	    LOG.info("==================");
	    LOG.info("Number of processing data: " + count);
	    LOG.info("=========END=========");
	  }
}
