package nl.knaw.dans.ersy.orm;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import nl.knaw.dans.ersy.orm.dao.MiningProcess;
import nl.knaw.dans.ersy.orm.dao.PidRelevancy;
import nl.knaw.dans.ersy.orm.dao.PidRelevancySimple;
import nl.knaw.dans.ersy.orm.util.HibernateUtil;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.Transaction;

 
 
public class Recommendation {
	
	
	public static void main(String args[]) {
    	
		//updateRating(7,true);
		List<RecommendationPid> reccoms = findRelevancePids("STANDARD-ABR","urn:nbn:nl:ui:13-yla-ywp");
    	
	    	System.out.println("***************");
	    	for (RecommendationPid s: reccoms) {
	    		System.out.println(s.getPid() + "\t" + s.getDistance() + "\t" + s.getId());
	    		
	    	}
	    	System.out.println("==============================================\n" + reccoms.size());
    	//updateRating(25, "P-2",false);
//    	deleteAllTables();
//		storeTest();
//		listMiningProcess();
        //store();
         
        //listMiningProcess();
        //updateMiningProcess("Raghav", "1234567890", "0123456789");
        //deleteMiningProcess("Raghav", "0123456789");
    }

	
	private static void storeTest() {
		List<MiningProcess> mps = new ArrayList<MiningProcess>();
    	MiningProcess mp = new MiningProcess("ABR");
    		Set<PidRelevancy> s = new HashSet<PidRelevancy>(); 
    		for (int j=0; j<3; j++) {
    			PidRelevancy d = new PidRelevancy();
    			d.setMiningProcess(mp);
    			d.setPid("A");
    			d.setPidRel("R_" + j);
    			d.setDistance(0.3*j);
    			s.add(d);
    		}
    		for (int j=0; j<2; j++) {
    			PidRelevancy d = new PidRelevancy();
    			d.setMiningProcess(mp);
    			d.setPid("B-" + j);
    			d.setPidRel("A");
    			d.setDistance(0.3*j);
    			s.add(d);
    		}
    		
    		for (int j=0; j<5; j++) {
    			PidRelevancy d = new PidRelevancy();
    			d.setMiningProcess(mp);
    			d.setPid("C");
    			d.setPidRel("X_" + j);
    			d.setDistance(0.3*j);
    			s.add(d);
    		}
    		mp.setPidRelevancies(s);
    		mps.add(mp);
    		
    	
    	store(mps);
	}

	public static void store(List<MiningProcess> miningProcesses) {
		Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            for (MiningProcess mp : miningProcesses) {
            	session.save(mp);
            	Set<PidRelevancy> set = mp.getPidRelevancies();
            	for (PidRelevancy pr : set) {
            		if (pr != null)
            			session.save(pr);
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
     
	
    public static void listMiningProcess() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;
        try {
             
            transaction = session.beginTransaction();
            List miningProcesses = session.createQuery("from MiningProcess").list();
            for (Iterator iterator = miningProcesses.iterator(); iterator.hasNext();) {
                MiningProcess mp = (MiningProcess) iterator.next();
                System.out.println("Name " + mp.getMethodName());
                for(PidRelevancy pr : mp.getPidRelevancies()) {
                    System.out.println("Pid " + pr.getPid());
                    System.out.println("PidRel " + pr.getPidRel());
                    System.out.println("Rating " + pr.getRating());
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
  
  public static List<RecommendationPid> findRelevancePids(String methodName, String pid) {
	List<RecommendationPid> recs = new ArrayList<RecommendationPid>();
	
    Session session = HibernateUtil.getSessionFactory().openSession();
    Transaction transaction = null;
    try {
         
        transaction = session.beginTransaction();
        Query query = session.createQuery("from MiningProcess where methodName= :methodName");
        query.setParameter("methodName", methodName);
        List<MiningProcess> mps = query.list();
			if (mps != null && !mps.isEmpty() && mps.size() == 1) {
				int mpid = mps.get(0).getMpid();

				Query queryPidRel = session.createQuery("from PidRelevancy p where p.miningProcess= :miningProcess and p.pid= :pid order by distance desc, rating desc");
				queryPidRel.setParameter("pid", pid);
				queryPidRel.setParameter("miningProcess", mps.get(0));
				recs.addAll(getPidsFromPidRelevance(queryPidRel, true));
				
				Query queryPid = session.createQuery("from PidRelevancy p where p.miningProcess= :miningProcess and p.pidRel= :pid order by distance desc, rating desc");
				queryPid.setParameter("pid", pid);
				queryPid.setParameter("miningProcess", mps.get(0));
				recs.addAll(getPidsFromPidRelevance(queryPid, false));
				
			} 
      
        transaction.commit();
    } catch (HibernateException e) {
        transaction.rollback();
        e.printStackTrace();
    } finally {
        session.close();
    }
    return recs;
}


/**
 * @param recs
 * @param q
 */
private static List<RecommendationPid> getPidsFromPidRelevance(Query q, boolean pidRel) {
	List<RecommendationPid> rps = new ArrayList<RecommendationPid>();
	List<PidRelevancy> prs = q.list();
	for (PidRelevancy pr : prs) {
		RecommendationPid rp = new RecommendationPid();
		rp.setId(pr.getId());
		if (!pidRel)
			rp.setPid(pr.getPid());
		else
			rp.setPid(pr.getPidRel());
		rp.setDistance(pr.getDistance());
		rp.setRating(pr.getRating());
		rps.add(rp);
	}
	return rps;
}
    
     
    public static void updateRating(int id, boolean thumbUp) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;
        try {
             
            transaction = session.beginTransaction();
            Query query = session.createQuery("from PidRelevancy p where p.id= :id");
            query.setParameter("id",id);
            List<PidRelevancy> prs = query.list();
            if (prs != null && prs.size() == 1) {
            	PidRelevancy pr = prs.get(0);
            	if (thumbUp)
            		prs.get(0).setRating(pr.getRating() + 1);
            	else
            		prs.get(0).setRating(pr.getRating() - 1);
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
            
            SQLQuery queryPidRelevancyTable = session.createSQLQuery("DELETE FROM pid_relevancy");
            queryPidRelevancyTable.executeUpdate();
            
            SQLQuery queryResetAutoincreament = session.createSQLQuery("ALTER TABLE pid_relevancy AUTO_INCREMENT = 1");
            queryResetAutoincreament.executeUpdate();
            
            SQLQuery queryDeleteMiningProcessTable = session.createSQLQuery("DELETE FROM mining_process");
            queryDeleteMiningProcessTable.executeUpdate();
            
            
            transaction.commit();
        } catch (HibernateException e) {
            transaction.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
    }
    
}