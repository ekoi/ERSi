package nl.knaw.dans.ersy.orm.dao;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
 
@Entity
@Table(name="DATASET")
public class Dataset {
    @Id
    @GeneratedValue
    @Column(name="ID")
    private long id;
     
    @Column(name="PID")
    private String pid;
     
    @Column(name="Distance")
    private double distance;
    
    @Column(name="Rating")
    private int rating;
     
    @ManyToOne
    @JoinColumn(name="CLUSTER_ID")
    private Cluster cluster;
    public Dataset() {
    }
    public Dataset(Cluster cluster, long id, String pid, String number) {
        this.cluster = cluster;
        this.id = id;
        this.pid = pid;
    }
     
    public Cluster getCluster() {
        return cluster;
    }
 
    public void setCluster(Cluster cluster) {
        this.cluster = cluster;
    }
 
    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }
    public String getPid() {
        return pid;
    }
    public void setPid(String pid) {
        this.pid = pid;
    }
	public double getDistance() {
		return distance;
	}
	public void setDistance(double distance) {
		this.distance = distance;
	}
	public int getRating() {
		return rating;
	}
	public void setRating(int rating) {
		this.rating = rating;
	}
}