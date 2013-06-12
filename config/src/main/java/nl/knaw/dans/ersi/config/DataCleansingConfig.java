package nl.knaw.dans.ersi.config;

import org.simpleframework.xml.Element;

public class DataCleansingConfig {
	
	@Element(name="simple-dimension-reduction")
	private SimpleDimensionReductionConfig simpleDimensionReduction;
	
	@Element
	int minSupport = 2; //minSupport of the feature to be included
	
	@Element
    int minDf = 2; //The minimum document frequency.
	
	@Element
    int maxDFPercent = 90; //The max percentage of vectors for the DF. Can be used to remove really high frequency features.
	
	@Element
    int maxNGramSize = 1;
	
	@Element
    float minLLRValue = 3;//minimum threshold to prune ngrams
	
	@Element
    int reduceTasks = 1;
	
	@Element
    int chunkSize = 200;
	
	@Element
    int norm = 2;
	
	@Element
    boolean sequentialAccessOutput = true;
	
	@Element(name="input-directory")
	String inputDirectory;
	
	@Element(name="output-directory")
	String outputDirectory;

	public SimpleDimensionReductionConfig getSimpleDimensionReduction() {
		return simpleDimensionReduction;
	}

	public void setSimpleDimensionReduction(
			SimpleDimensionReductionConfig simpleDimensionReduction) {
		this.simpleDimensionReduction = simpleDimensionReduction;
	}

	public int getMinSupport() {
		return minSupport;
	}

	public void setMinSupport(int minSupport) {
		this.minSupport = minSupport;
	}

	public int getMinDf() {
		return minDf;
	}

	public void setMinDf(int minDf) {
		this.minDf = minDf;
	}

	public int getMaxDFPercent() {
		return maxDFPercent;
	}

	public void setMaxDFPercent(int maxDFPercent) {
		this.maxDFPercent = maxDFPercent;
	}

	public int getMaxNGramSize() {
		return maxNGramSize;
	}

	public void setMaxNGramSize(int maxNGramSize) {
		this.maxNGramSize = maxNGramSize;
	}

	public float getMinLLRValue() {
		return minLLRValue;
	}

	public void setMinLLRValue(float minLLRValue) {
		this.minLLRValue = minLLRValue;
	}

	public int getReduceTasks() {
		return reduceTasks;
	}

	public void setReduceTasks(int reduceTasks) {
		this.reduceTasks = reduceTasks;
	}

	public int getChunkSize() {
		return chunkSize;
	}

	public void setChunkSize(int chunkSize) {
		this.chunkSize = chunkSize;
	}

	public int getNorm() {
		return norm;
	}

	public void setNorm(int norm) {
		this.norm = norm;
	}

	public boolean isSequentialAccessOutput() {
		return sequentialAccessOutput;
	}

	public void setSequentialAccessOutput(boolean sequentialAccessOutput) {
		this.sequentialAccessOutput = sequentialAccessOutput;
	}

	public String getInputDirectory() {
		return Constants.ERSY_HOME + "/" + inputDirectory;
	}

	public void setInputDirectory(String inputDirectory) {
		this.inputDirectory = inputDirectory;
	}

	public String getOutputDirectory() {
		return Constants.ERSY_HOME + "/" + outputDirectory;
	}

	public void setOutputDirectory(String outputDirectory) {
		this.outputDirectory = outputDirectory;
	}
}