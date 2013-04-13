package nl.knaw.dans.ersi.config;

import org.simpleframework.xml.Element;

public class KMeansConfig {
	@Element (name="distance-measure-class-name")
	private String distanceMeasureClassName;
	
	@Element (name="convergence-delta")
	private double convergenceDelta;
	
	@Element (name="max-iterations")
	private int maxIterations;
	
	@Element(name="cluster-classification-threshold")
	private double clusterClassificationThreshold;

	public String getDistanceMeasureClassName() {
		return distanceMeasureClassName;
	}

	public void setDistanceMeasureClassName(String distanceMeasureClassName) {
		this.distanceMeasureClassName = distanceMeasureClassName;
	}

	public double getConvergenceDelta() {
		return convergenceDelta;
	}

	public void setConvergenceDelta(double convergenceDelta) {
		this.convergenceDelta = convergenceDelta;
	}

	public int getMaxIterations() {
		return maxIterations;
	}

	public void setMaxIterations(int maxIterations) {
		this.maxIterations = maxIterations;
	}

	public double getClusterClassificationThreshold() {
		return clusterClassificationThreshold;
	}

	public void setClusterClassificationThreshold(
			double clusterClassificationThreshold) {
		this.clusterClassificationThreshold = clusterClassificationThreshold;
	}

}
