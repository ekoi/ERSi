package nl.knaw.dans.ersi.config;

import org.simpleframework.xml.Element;

public class ClusterAlgorithmConfig {
	@Element (name="canopy")
	CanopyConfig canopyConfig;
	
	@Element (name="kmeans")
	KMeansConfig kMeansConfig;

	public CanopyConfig getCanopyConfig() {
		return canopyConfig;
	}

	public void setCanopyConfig(CanopyConfig canopyConfig) {
		this.canopyConfig = canopyConfig;
	}
	
	public KMeansConfig getkMeansConfig() {
		return kMeansConfig;
	}

	public void setkMeansConfig(KMeansConfig kMeansConfig) {
		this.kMeansConfig = kMeansConfig;
	}
}
