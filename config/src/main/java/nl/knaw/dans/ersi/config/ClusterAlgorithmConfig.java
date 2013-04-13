package nl.knaw.dans.ersi.config;

import java.util.List;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;

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
