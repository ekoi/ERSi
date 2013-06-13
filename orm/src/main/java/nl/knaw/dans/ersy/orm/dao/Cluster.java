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
@Table(name = "CLUSTER")
public class Cluster {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(name = "CLUSTER_ID")
    private int clusterId;
     
    @Column(name = "Name")
    private String name;
     
    @OneToMany(mappedBy="cluster")
    private Set<Dataset> dataset;
    public Cluster() {
    }
    public Cluster(String name) {
        this.name = name;
    }
    public Cluster(String name, Set<Dataset> dataset) {
        this.name = name;
        this.dataset = dataset;
    }
 
     
    public int getClusterId() {
        return clusterId;
    }
    public void setClusterId(int clusterId) {
        this.clusterId = clusterId;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public Set<Dataset> getDataset() {
        return dataset;
    }
    public void setDataset(Set<Dataset> dataset) {
        this.dataset = dataset;
    }
}