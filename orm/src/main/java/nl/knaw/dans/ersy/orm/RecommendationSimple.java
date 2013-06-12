package nl.knaw.dans.ersy.orm;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import nl.knaw.dans.ersy.orm.dao.PidRelevancySimple;
import nl.knaw.dans.ersy.orm.util.HibernateUtil;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.Transaction;

 
 
public class RecommendationSimple {
	
	public static void main(String args[]) {
//		deleteTable();
//		storeTest();
//		listPidRelevancySimple();
//		likeIt("A","B2", true);
		List<String> l = findByPid("A");
		System.out.println(l);
	}
	
	private static void storeTest() {
		List<PidRelevancySimple> l = new ArrayList<PidRelevancySimple>();
    	for (int i=0; i<3; i++) {
    		PidRelevancySimple p = new PidRelevancySimple("A", "B" + i, 0.8-0.2*i);
    		l.add(p);
    		
    	}
    	for (int i=0; i<4; i++) {
    		PidRelevancySimple p = new PidRelevancySimple("C", "D" + i, 0.2*i);
    		l.add(p);
    		
    	}
    	store(l);
	}
    
	private static void store(List<PidRelevancySimple> pidRelevancies) {
		Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            for (PidRelevancySimple p : pidRelevancies) {
            	session.save(p);
            }
           
            transaction.commit();
        }catch (HibernateException e) {
            transaction.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
	}
	
    public static void listPidRelevancySimple() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;
        try {
             
            transaction = session.beginTransaction();
            List pidRelevancies = session.createQuery("from PidRelevancySimple").list();
            for (Iterator iterator = pidRelevancies.iterator(); iterator.hasNext();) {
                PidRelevancySimple pidRelevancy = (PidRelevancySimple) iterator.next();
                System.out.println("Name " + pidRelevancy.getPid());
            }
            transaction.commit();
        } catch (HibernateException e) {
            transaction.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
    }
	
    public static List<String> findByPid(String pid) {
    	List<String> reccoms = new ArrayList<String>();
    	
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;
        try {
             
            transaction = session.beginTransaction();
            Query query = session.createQuery("from PidRelevancySimple p where p.pid= :pid OR p.pidRel= :pidRel ORDER BY distance ASC, rating DESC");
            query.setParameter("pid", pid);
            query.setParameter("pidRel", pid);
            List<PidRelevancySimple> pidRelevancies = query.list();
            for (PidRelevancySimple pr : pidRelevancies) {
            	reccoms.add(pr.getPidRel());
            }
          
            
            transaction.commit();
        } catch (HibernateException e) {
            transaction.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
        return reccoms;
    }
    
    public static void deleteTable() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;
        try {
             
            transaction = session.beginTransaction();
            SQLQuery queryDeletePidRelevancySimpleTable = session.createSQLQuery("DELETE FROM pid_relevancy");
            queryDeletePidRelevancySimpleTable.executeUpdate();
            SQLQuery queryResetAutoincreamen = session.createSQLQuery("ALTER TABLE pid_relevancy AUTO_INCREMENT = 1");
            queryResetAutoincreamen.executeUpdate();
            transaction.commit();      
        } catch (HibernateException e) {
            transaction.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
    }
    
    public static void likeIt(String pid, String pidRel, boolean thumbUp) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;
        try {
             
            transaction = session.beginTransaction();
            Query query = session.createQuery("from PidRelevancySimple pr where pr.pid= :pid and pr.pidRel= :pidRel");
            query.setParameter("pid",pid);
            query.setParameter("pidRel",pidRel);
            List<PidRelevancySimple> pidRelevancies = query.list();
			for (PidRelevancySimple p : pidRelevancies) {
				if (thumbUp)
					p.setRating(p.getRating() + 1);
				else
					p.setRating(p.getRating() - 1);
			}
            transaction.commit();
        } catch (HibernateException e) {
            transaction.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
    }
    
	/*
	public static void main(String args[]) {
    	
    	List<List<String>> reccoms = findByPid("P-2");
    	System.out.println("==============================================" + reccoms.size());
	    for (List<String> list : reccoms) {	
	    	System.out.println("***************");
	    	for (String s: list) {
	    		System.out.println(s);
	    	}
	    }
    	//updateRating(25, "P-2",false);
    	//deleteAllTables();
    	//storeTest();
        //store();
         
        //listCluster();
        //updateCluster("Raghav", "1234567890", "0123456789");
        //deleteCluster("Raghav", "0123456789");
    }

	
	private static void storeTest() {
		List<Cluster> l = new ArrayList<Cluster>();
    	for (int i=0; i<3; i++) {
    	Cluster c = new Cluster("C_" + i);
    		Set<Dataset> s = new HashSet<Dataset>(); 
    		for (int j=0; j<5; j++) {
    			Dataset d = new Dataset();
    			d.setCluster(c);
    			d.setPid("P-"+j);
    			d.setDistance(0.3*j);
    			s.add(d);
    		}
    		c.setDataset(s);
    		l.add(c);
    		
    	}
    	store(l);
	}

	private static void store(List<Cluster> clusters) {
		Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            for (Cluster c : clusters) {
            	session.save(c);
            	Set<Dataset> set = c.getDataset();
            	for (Dataset d : set) {
            		session.save(d);
            	}
            }
           
            transaction.commit();
        }catch (HibernateException e) {
            transaction.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
	}
     
	
    public static void listCluster() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;
        try {
             
            transaction = session.beginTransaction();
            List clusters = session.createQuery("from Cluster").list();
            for (Iterator iterator = clusters.iterator(); iterator.hasNext();) {
                Cluster cluster = (Cluster) iterator.next();
                System.out.println("Name " + cluster.getName());
                for(Dataset dataset : cluster.getDataset()) {
                    System.out.println("Pid " + dataset.getPid());
                    System.out.println("Rating " + dataset.getRating());
                }
            }
            transaction.commit();
        } catch (HibernateException e) {
            transaction.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
    }
    
    public static List<List<String>> findByPid(String pid) {
    	List<List<String>> reccoms = new ArrayList<List<String>>();
    	
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;
        try {
             
            transaction = session.beginTransaction();
            Query query = session.createQuery("from Dataset where pid= :pid");
            query.setParameter("pid", pid);
            List<Dataset> datasets = query.list();
            if (datasets != null && !datasets.isEmpty()) {
            	for (Dataset dataset : datasets) {
            		int clusterId = dataset.getCluster().getClusterId();
            		Query q = session.createSQLQuery("select PID from dataset where cluster_id="+clusterId + " order by distance asc, rating desc");
            		List<String> list = q.list();
            		reccoms.add(list);
            	}
            }
          
            transaction.commit();
        } catch (HibernateException e) {
            transaction.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
        return reccoms;
    }
     
    public static void updateRating(int clusterId, String pid, boolean thumbUp) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;
        try {
             
            transaction = session.beginTransaction();
            Query query = session.createQuery("from Dataset dataset where dataset.pid= :pid");
            query.setParameter("pid",pid);
            List<Dataset> datasets = query.list();
            for(Dataset dataset : datasets){
                if (dataset.getCluster().getClusterId() == clusterId) {
                	if (thumbUp)
                		dataset.setRating(dataset.getRating() + 1);
                	else
                		dataset.setRating(dataset.getRating() - 1);
                }
             }
            transaction.commit();
        } catch (HibernateException e) {
            transaction.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
    }
    
    public static void deleteAllTables() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;
        try {
             
            transaction = session.beginTransaction();
            SQLQuery queryDeleteDatesetTable = session.createSQLQuery("DELETE FROM dataset");
            queryDeleteDatesetTable.executeUpdate();
            SQLQuery queryDeleteClusterTable = session.createSQLQuery("DELETE FROM cluster");
            queryDeleteClusterTable.executeUpdate();
             
            transaction.commit();
        } catch (HibernateException e) {
            transaction.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
    }
    */
}