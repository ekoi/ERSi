package nl.knaw.dans.ersi.config;

import org.simpleframework.xml.Element;

public class CanopyConfig {
	
	@Element (name="distance-measure-class-name")
	private String distanceMeasureClassName;
	
	@Element(name="distance-metric-T1")
	private double distanceMetricT1;
	
	@Element(name="distance-metric-T2")
	private double distanceMetricT2;
	
	@Element(name="cluster-classification-threshold")
	private double clusterClassificationThreshold;
	
	public String getDistanceMeasureClassName() {
		return distanceMeasureClassName;
	}

	public void setDistanceMeasureClassName(String distanceMeasureClassName) {
		this.distanceMeasureClassName = distanceMeasureClassName;
	}

	public double getDistanceMetricT1() {
		return distanceMetricT1;
	}

	public void setDistanceMetricT1(double distanceMetricT1) {
		this.distanceMetricT1 = distanceMetricT1;
	}

	public double getDistanceMetricT2() {
		return distanceMetricT2;
	}

	public void setDistanceMetricT2(double distanceMetricT2) {
		this.distanceMetricT2 = distanceMetricT2;
	}

	public double getClusterClassificationThreshold() {
		return clusterClassificationThreshold;
	}

	public void setClusterClassificationThreshold(
			double clusterClassificationThreshold) {
		this.clusterClassificationThreshold = clusterClassificationThreshold;
	}

	
	
}
