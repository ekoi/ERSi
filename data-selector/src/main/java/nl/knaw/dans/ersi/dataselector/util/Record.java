/**
 * 
 */
package nl.knaw.dans.ersi.dataselector.util;

import org.simpleframework.xml.Element;


/**
 * @author akmi
 *
 */

public class Record {
	
	@Element(name="urn", required=false)
	private String urn;

	@Element(name="data_orig", required=false)
	private String dataOrig;
	
	@Element(name="data_abr", required=false)
	private String dataAbr;
	
	@Element(name="data_nonabr", required=false)
	private String dataNonAbr;

	public String getUrn() {
		return urn;
	}

	public String getDataOrig() {
		return dataOrig;
	}

	public void setUrn(String urn) {
		this.urn = urn;
	}

	public void setDataOrig(String dataOrig) {
		this.dataOrig = dataOrig;
	}

	public void setDataAbr(String dataAbr) {
		this.dataAbr = dataAbr;
	}

	public void setDataNonAbr(String dataNonAbr) {
		this.dataNonAbr = dataNonAbr;
	}

	public String getDataAbr() {
		return dataAbr;
	}

	public String getDataNonAbr() {
		return dataNonAbr;
	}
	
	
}
