/**
 * 
 */
package nl.knaw.dans.ersy.orm;

import cat.quickdb.annotation.Column;
import cat.quickdb.annotation.ColumnDefinition;
import cat.quickdb.annotation.Definition;
import cat.quickdb.annotation.Properties;
import cat.quickdb.annotation.Table;

/**
 * @author akmi
 * 
 */
@Table
public class ProcessReportTable {
	@Column(type = Properties.TYPES.PRIMARYKEY)
	@ColumnDefinition(autoIncrement = true, primary = true, type = Definition.DATATYPE.INTEGER, length = 11)
	private int id;
	
	@Column
	private Long identification;

	@Column
	private String processName;

	@Column
	private boolean finish;

	@Column
	private String executedBy;

	public ProcessReportTable() {
	}

	public ProcessReportTable(long identification, String processName, String executedBy) {
		this.identification = identification;
		this.processName = processName;
		this.executedBy = executedBy;
	}
	
	public void setId(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public Long getIdentification() {
		return identification;
	}

	public void setIdentification(Long identification) {
		this.identification = identification;
	}

	public String getProcessName() {
		return processName;
	}

	public void setProcessName(String processName) {
		this.processName = processName;
	}

	public boolean isFinish() {
		return finish;
	}

	public void setFinish(boolean finish) {
		this.finish = finish;
	}

	public String getExecutedBy() {
		return executedBy;
	}

	public void setExecutedBy(String executedBy) {
		this.executedBy = executedBy;
	}
	
	

}
