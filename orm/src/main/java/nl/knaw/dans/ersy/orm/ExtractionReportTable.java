/**
 * 
 */
package nl.knaw.dans.ersy.orm;

import java.sql.Timestamp;

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
public class ExtractionReportTable {
	@Column(type = Properties.TYPES.PRIMARYKEY)
	@ColumnDefinition(autoIncrement = true, primary = true, type = Definition.DATATYPE.INTEGER, length = 11)
	private int id;
	
	@Column
	private Long identification;

	@Column
	private Integer fromSource;

	@Column
	private Integer dutch;

	@Column
	private Integer english;

	@Column
	private Integer other;

	@Column
	private Integer dutchWords;

	@Column
	private String executedBy;

	public ExtractionReportTable() {
	}

	public ExtractionReportTable(long identification, int fromSource, int dutch, int dutchWords,
			int english, int other, String executedBy) {
		this.identification = identification;
		this.fromSource = fromSource;
		this.dutch = dutch;
		this.dutchWords = dutchWords;
		this.english = english;
		this.other = other;
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

	public Integer getFromSource() {
		return fromSource;
	}

	public void setFromSource(Integer fromSource) {
		this.fromSource = fromSource;
	}

	public Integer getDutch() {
		return dutch;
	}

	public void setDutch(Integer dutch) {
		this.dutch = dutch;
	}

	public Integer getEnglish() {
		return english;
	}

	public void setEnglish(Integer english) {
		this.english = english;
	}

	public Integer getOther() {
		return other;
	}

	public void setOther(Integer other) {
		this.other = other;
	}

	public Integer getDutchWords() {
		return dutchWords;
	}

	public void setDutchWords(Integer dutchWords) {
		this.dutchWords = dutchWords;
	}

	public String getExecutedBy() {
		return executedBy;
	}

	public void setExecutedBy(String executedBy) {
		this.executedBy = executedBy;
	}

}
