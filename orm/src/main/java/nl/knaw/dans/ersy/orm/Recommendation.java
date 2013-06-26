package nl.knaw.dans.ersy.orm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import nl.knaw.dans.ersy.orm.dao.MiningProcess;
import nl.knaw.dans.ersy.orm.dao.PidRelevancy;
import nl.knaw.dans.ersy.orm.util.HibernateUtil;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Recommendation {
	public enum DRM {
		ABR {public String toString(){return "ABR";}} 
		, STANDARD {public String toString(){return "STANDARD";}} 
		, ABR_PLUS{public String toString(){return "ABR Plus";}}
	}
	
	private static Logger LOG = LoggerFactory.getLogger(Recommendation.class);

	public static void store(List<MiningProcess> miningProcesses) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction transaction = null;
		int i = 0;
		try {
			transaction = session.beginTransaction();
			for (MiningProcess mp : miningProcesses) {
				session.save(mp);
				Set<PidRelevancy> set = mp.getPidRelevancies();
				for (PidRelevancy pr : set) {
					if (pr != null) {
						i++;
						session.save(pr);
						if (i % 30 == 0) {
							LOG.info("number of saved records: " + i);
							// 30, same as the JDBC batch size
							// flush a batch and release memory
							session.flush(); // Line 1
							session.clear();
						}
					}
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

	public static void listMiningProcess() {
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction transaction = null;
		try {

			transaction = session.beginTransaction();
			List miningProcesses = session.createQuery("from MiningProcess")
					.list();
			for (Iterator iterator = miningProcesses.iterator(); iterator
					.hasNext();) {
				MiningProcess mp = (MiningProcess) iterator.next();
				System.out.println("Name " + mp.getMethodName());
				for (PidRelevancy pr : mp.getPidRelevancies()) {
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

	public static List<String> getAllMiningProcessMethods() {
		List<String> methodNames = new ArrayList<String>();

		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction transaction = null;
		try {

			transaction = session.beginTransaction();
			List miningProcesses = session.createQuery("from MiningProcess")
					.list();
			for (Iterator iterator = miningProcesses.iterator(); iterator
					.hasNext();) {
				MiningProcess mp = (MiningProcess) iterator.next();
				methodNames.add(mp.getMethodName());
			}
			transaction.commit();
		} catch (HibernateException e) {
			transaction.rollback();
			e.printStackTrace();
		} finally {
			session.close();
		}

		return methodNames;
	}

	public static List<RecommendationPid> findRelevancePids(DRM standard,
			String pid) {
		LOG.info("Search recommendation for pid: " + pid);
		List<RecommendationPid> recs = new ArrayList<RecommendationPid>();
		long begin = System.currentTimeMillis();
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction transaction = null;
		try {

			transaction = session.beginTransaction();
			Query query = session
					.createQuery("from MiningProcess where methodName= :methodName");
			query.setParameter("methodName", standard.toString());

			List<MiningProcess> mps = query.list();
			if (mps != null && !mps.isEmpty() && mps.size() == 1) {

				Query queryPidRel = session
						.createQuery("from PidRelevancy where miningProcess= :miningProcess and (pid= :pid or pidRel= :pid) order by distance desc");
				queryPidRel.setParameter("pid", pid);
				queryPidRel.setParameter("miningProcess", mps.get(0));
				queryPidRel.setMaxResults(10);
				recs.addAll(getPidsFromPidRelevance(queryPidRel, pid));
			}

			transaction.commit();
		} catch (HibernateException e) {
			if (transaction != null)
				transaction.rollback();
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
		
		
		Collections.sort(recs, new Comparator<RecommendationPid>() {
	        public int compare(RecommendationPid o1, RecommendationPid o2) {
	        	
	        	return o2.getRating() > o1.getRating() ? 1 : (o1.getRating() < o2.getRating() ? -1 : 0);
	        }

	    });
		
		
		LOG.info("Relevancies pid size: " + recs.size());
		LOG.info("Duration: " + convertToHumanReadableDuration((begin)));
		return recs;
	}

	private static String convertToHumanReadableDuration(long begin) {
		long diff = System.currentTimeMillis() - begin;
		String s = String.format(
				"%d hours. %d min, %d sec",
				TimeUnit.MILLISECONDS.toHours(diff),
				TimeUnit.MILLISECONDS.toMinutes(diff),
				TimeUnit.MILLISECONDS.toSeconds(diff)
						- TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS
								.toMinutes(diff)));
		return s;
	}

	public static List<RecommendationPid> findRelevancePids(DRM standard,
			String pid, double distance) {
		List<RecommendationPid> recs = new ArrayList<RecommendationPid>();

		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction transaction = null;
		try {

			transaction = session.beginTransaction();
			Query query = session
					.createQuery("from MiningProcess where methodName= :methodName");
			query.setParameter("methodName", standard.toString());
			List<MiningProcess> mps = query.list();
			if (mps != null && !mps.isEmpty() && mps.size() == 1) {
				int mpid = mps.get(0).getMpid();

				Query queryPidRel = session
						.createQuery("from PidRelevancy p where p.miningProcess= :miningProcess and p.distance> :distance and (p.pid= :pid or p.pidRel= :pid) order by distance desc, rating desc");
				queryPidRel.setParameter("pid", pid);
				queryPidRel.setParameter("miningProcess", mps.get(0));
				queryPidRel.setParameter("distance", distance);
				recs.addAll(getPidsFromPidRelevance(queryPidRel, pid));
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
	private static List<RecommendationPid> getPidsFromPidRelevance(Query q,
			String pid) {
		List<RecommendationPid> rps = new ArrayList<RecommendationPid>();
		List<PidRelevancy> prs = q.list();
		for (PidRelevancy pr : prs) {
			RecommendationPid rp = new RecommendationPid();
			rp.setId(pr.getId());
			if (pr.getPid().equals(pid))
				rp.setPid(pr.getPidRel());
			else
				rp.setPid(pr.getPid());
			rp.setDistance(pr.getDistance());
			rp.setRating(pr.getRating());
			rps.add(rp);
		}
		return rps;
	}

	
	public static void updateRating(int id, boolean thumbUp) {
		LOG.info("Updating rating: " + thumbUp + " for id: " + id);
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction transaction = null;
		try {

			transaction = session.beginTransaction();
			Query query = session
					.createQuery("from PidRelevancy p where p.id= :id");
			query.setParameter("id", id);
			List<PidRelevancy> prs = query.list();
			if (prs != null && prs.size() == 1) {
				PidRelevancy pr = prs.get(0);
				if (thumbUp)
					pr.setRating(pr.getRating() + 1);
				else
					pr.setRating(pr.getRating() - 1);
				session.save(pr);
			}
			transaction.commit();
		} catch (HibernateException e) {
			transaction.rollback();
			e.printStackTrace();
		} finally {
			session.close();
		}
	}

	public static int getRating(int id) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		try {
			Query query = session
					.createQuery("from PidRelevancy p where p.id= :id");
			query.setParameter("id", id);
			List<PidRelevancy> prs = query.list();
			if (prs != null && prs.size() == 1)
				return prs.get(0).getRating();
		} finally {
			session.close();
		}
		return 0;
	}

	public static void deleteAllTables() {
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction transaction = null;
		try {

			transaction = session.beginTransaction();

			SQLQuery queryPidRelevancyTable = session
					.createSQLQuery("DELETE FROM pid_relevancy");
			queryPidRelevancyTable.executeUpdate();

			SQLQuery queryResetAutoincreament = session
					.createSQLQuery("ALTER TABLE pid_relevancy AUTO_INCREMENT = 1");
			queryResetAutoincreament.executeUpdate();

			SQLQuery queryDeleteMiningProcessTable = session
					.createSQLQuery("DELETE FROM mining_process");
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
