/**
 * 
 */
package nl.knaw.dans.ersy.orm;

import java.util.Date;

/**
 * @author akmi
 *
 */
public class MiningMethod {
	private int id;
	private String methodName;
	private Date executionDate;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getMethodName() {
		return methodName;
	}
	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}
	public Date getExecutionDate() {
		return executionDate;
	}
	public void setExecutionDate(Date executionDate) {
		this.executionDate = executionDate;
	}

}
