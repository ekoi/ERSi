/**
 * 
 */
package nl.knaw.dans.ersi.dataselector;

import nl.knaw.dans.ersi.config.DataExtractionConfig;

/**
 * @author akmi
 *
 */
public abstract class SimpleExtractor {

	/**
	 * 
	 */
	private DataExtractionConfig dataExtractionConfig;
	public SimpleExtractor(DataExtractionConfig dataExtractionConfig) {
		this.dataExtractionConfig = dataExtractionConfig;
	}
	/**
	 * @return the dataExtractionConfig
	 */
	protected DataExtractionConfig getDataExtractionConfig() {
		return dataExtractionConfig;
	}
	/**
	 * @param dataExtractionConfig the dataExtractionConfig to set
	 */
	protected void setDataExtractionConfig(DataExtractionConfig dataExtractionConfig) {
		this.dataExtractionConfig = dataExtractionConfig;
	}

}
