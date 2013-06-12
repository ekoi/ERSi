package nl.knaw.dans.ersi.config;

import org.simpleframework.xml.Element;

public class ClusteringConfig {
	
	@Element(name="cluster-algorithm")
	private ClusterAlgorithmConfig clusterAlgorithmConfig;
	
	@Element(name="input-vectors-path")
	String inputVectorsPath;
	
	@Element(name="output-path")
	String outputPath;

	public ClusterAlgorithmConfig getClusterAlgorithmConfig() {
		return clusterAlgorithmConfig;
	}

	public void setClusterAlgorithmConfig(
			ClusterAlgorithmConfig clusterAlgorithmConfig) {
		this.clusterAlgorithmConfig = clusterAlgorithmConfig;
	}

	public String getInputVectorsPath() {
		return Constants.ERSY_HOME + "/" + inputVectorsPath;
	}

	public void setInputVectorsPath(String inputVectorsPath) {
		this.inputVectorsPath = inputVectorsPath;
	}

	public String getOutputPath() {
		return Constants.ERSY_HOME + "/" + outputPath;
	}

	public void setOutputPath(String outputPath) {
		this.outputPath = outputPath;
	}

}