package nl.knaw.dans.ersy.orm;

import java.util.ArrayList;
import java.util.List;

import nl.knaw.dans.ersy.orm.dao.PidRelevancy;
import nl.knaw.dans.ersy.orm.util.HibernateUtil;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

 
 
public class RecommendationFromDual {
	
	public enum DRM {
		ABR {public String toString(){return "ABR";}} 
		, STANDARD {public String toString(){return "STANDARD";}} 
		, ABR_PLUS{public String toString(){return "ABR Plus";}}
	}
	
	public static void main(String args[]) {
    	
		System.out.println(getAllMiningProcessMethods());
    }
    
  public static List<String> getAllMiningProcessMethods() {
	  List<String> methodNames = new ArrayList<String>();
	  methodNames.add(DRM.STANDARD.toString());
	  methodNames.add(DRM.ABR.toString());
	  methodNames.add(DRM.ABR_PLUS.toString());
	  return methodNames;
  }
  
  public static List<RecommendationPid> findRelevancePids(String methodName, String pid) {
	List<RecommendationPid> recs = new ArrayList<RecommendationPid>();
		if (methodName.equals(DRM.STANDARD.toString())) {
			RecommendationPid rp1 = new RecommendationPid();
			rp1.setId(1);
			rp1.setDistance(0.791111);
			rp1.setRating(0);
			rp1.setPid("urn:nbn:nl:ui:13-alf-xz8");
			recs.add(rp1);
			
			RecommendationPid rp2 = new RecommendationPid();
			rp2.setId(2);
			rp2.setDistance(0.792111);
			rp2.setRating(1);
			rp2.setPid("urn:nbn:nl:ui:13-65l-2sq");
			recs.add(rp2);
			
			RecommendationPid rp3 = new RecommendationPid();
			rp3.setId(3);
			rp3.setDistance(0.793111);
			rp3.setRating(3);
			rp3.setPid("urn:nbn:nl:ui:13-nat-tq5");
			recs.add(rp3);
			
			RecommendationPid rp4 = new RecommendationPid();
			rp4.setId(4);
			rp4.setDistance(0.794111);
			rp4.setRating(4);
			rp4.setPid("urn:nbn:nl:ui:13-xxg-hf3");
			recs.add(rp4);
			
			RecommendationPid rp5 = new RecommendationPid();
			rp5.setId(5);
			rp5.setDistance(0.795111);
			rp5.setRating(5);
			rp5.setPid("urn:nbn:nl:ui:13-180-88g");
			recs.add(rp5);
			
			RecommendationPid rp6 = new RecommendationPid();
			rp6.setId(6);
			rp6.setDistance(0.796111);
			rp6.setRating(6);
			rp6.setPid("urn:nbn:nl:ui:13-nat-tq5");
			recs.add(rp6);
			
			RecommendationPid rp7 = new RecommendationPid();
			rp7.setId(7);
			rp7.setDistance(0.797111);
			rp7.setRating(7);
			rp7.setPid("urn:nbn:nl:ui:13-6xt-o8l");
			recs.add(rp7);
			
			RecommendationPid rp8 = new RecommendationPid();
			rp8.setId(8);
			rp8.setDistance(0.798111);
			rp8.setRating(8);
			rp8.setPid("urn:nbn:nl:ui:13-00c-35j");
			recs.add(rp8);
		} else if (methodName.equals(DRM.ABR.toString())) {
			RecommendationPid rp1 = new RecommendationPid();
			rp1.setId(1);
			rp1.setDistance(0.791111);
			rp1.setRating(0);
			rp1.setPid("urn:nbn:nl:ui:13-4oa-ooe ");
			recs.add(rp1);
			
			RecommendationPid rp2 = new RecommendationPid();
			rp2.setId(2);
			rp2.setDistance(0.792111);
			rp2.setRating(1);
			rp2.setPid("urn:nbn:nl:ui:13-dj2-trt");
			recs.add(rp2);
			
			RecommendationPid rp3 = new RecommendationPid();
			rp3.setId(3);
			rp3.setDistance(0.793111);
			rp3.setRating(3);
			rp3.setPid("urn:nbn:nl:ui:13-vke-mju");
			recs.add(rp3);
			
			RecommendationPid rp4 = new RecommendationPid();
			rp4.setId(4);
			rp4.setDistance(0.794111);
			rp4.setRating(4);
			rp4.setPid("urn:nbn:nl:ui:13-nat-tq5");
			recs.add(rp4);
			
			RecommendationPid rp5 = new RecommendationPid();
			rp5.setId(5);
			rp5.setDistance(0.795111);
			rp5.setRating(5);
			rp5.setPid("urn:nbn:nl:ui:13-9tq-5tt");
			recs.add(rp5);
			
			RecommendationPid rp6 = new RecommendationPid();
			rp6.setId(6);
			rp6.setDistance(0.796111);
			rp6.setRating(6);
			rp6.setPid("urn:nbn:nl:ui:13-j65-d2u");
			recs.add(rp6);
		} else if (methodName.equals(DRM.ABR_PLUS.toString())){
			RecommendationPid rp1 = new RecommendationPid();
			rp1.setId(1);
			rp1.setDistance(0.791111);
			rp1.setRating(0);
			rp1.setPid("urn:nbn:nl:ui:13-e0m-bk3");
			recs.add(rp1);
			
			RecommendationPid rp2 = new RecommendationPid();
			rp2.setId(2);
			rp2.setDistance(0.792111);
			rp2.setRating(1);
			rp2.setPid("urn:nbn:nl:ui:13-fq8-ut0");
			recs.add(rp2);
			
			RecommendationPid rp3 = new RecommendationPid();
			rp3.setId(3);
			rp3.setDistance(0.793111);
			rp3.setRating(3);
			rp3.setPid("urn:nbn:nl:ui:13-4oa-ooe");
			recs.add(rp3);
			
			RecommendationPid rp4 = new RecommendationPid();
			rp4.setId(4);
			rp4.setDistance(0.794111);
			rp4.setRating(4);
			rp4.setPid("urn:nbn:nl:ui:13-dxm-rle");
			recs.add(rp4);
			
			RecommendationPid rp5 = new RecommendationPid();
			rp5.setId(5);
			rp5.setDistance(0.795111);
			rp5.setRating(5);
			rp5.setPid("urn:nbn:nl:ui:13-4oa-ooe");
			recs.add(rp5);
			
			RecommendationPid rp6 = new RecommendationPid();
			rp6.setId(6);
			rp6.setDistance(0.796111);
			rp6.setRating(6);
			rp6.setPid("urn:nbn:nl:ui:13-9tq-5tt");
			recs.add(rp6);
			
			RecommendationPid rp7 = new RecommendationPid();
			rp7.setId(7);
			rp7.setDistance(0.797111);
			rp7.setRating(7);
			rp7.setPid("urn:nbn:nl:ui:13-4oa-ooe");
			recs.add(rp7);
			
			RecommendationPid rp8 = new RecommendationPid();
			rp8.setId(8);
			rp8.setDistance(0.798111);
			rp8.setRating(8);
			rp8.setPid("urn:nbn:nl:ui:13-l8k-x5h");
			recs.add(rp8);
			
			RecommendationPid rp9 = new RecommendationPid();
			rp9.setId(9);
			rp9.setDistance(0.799911);
			rp9.setRating(9);
			rp9.setPid("urn:nbn:nl:ui:13-6xt-o8l");
			recs.add(rp9);
			
			RecommendationPid rp10 = new RecommendationPid();
			rp10.setId(10);
			rp10.setDistance(0.999911);
			rp10.setRating(10);
			rp10.setPid("urn:nbn:nl:ui:13-180-88g");
			recs.add(rp10);
		} else {
			
		}
    return recs;
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
  
   
}