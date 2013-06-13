package nl.knaw.dans.ersy.orm.dao;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
@Entity
@Table(name = "mining_process")
public class MiningProcess {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(name = "mpid")
    private int mpid;
     
    @Column(name = "method_name")
    private String methodName;
     
    @OneToMany(mappedBy="miningProcess")
    private Set<PidRelevancy> pidRelevancies;
    public MiningProcess() {
    }
    public MiningProcess(String methodName) {
        this.methodName = methodName;
    }
    public MiningProcess(String methodName, Set<PidRelevancy> pidRelevancies) {
        this.methodName = methodName;
        this.pidRelevancies = pidRelevancies;
    }
	public int getMpid() {
		return mpid;
	}
	public void setMpid(int mpid) {
		this.mpid = mpid;
	}
	public String getMethodName() {
		return methodName;
	}
	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}
	public Set<PidRelevancy> getPidRelevancies() {
		return pidRelevancies;
	}
	public void setPidRelevancies(Set<PidRelevancy> pidRelevancies) {
		this.pidRelevancies = pidRelevancies;
	}
 
}