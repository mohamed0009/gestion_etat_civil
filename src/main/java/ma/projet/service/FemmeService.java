package ma.projet.service;

import ma.projet.beans.Femme;
import ma.projet.beans.Mariage;
import ma.projet.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.query.NativeQuery;
import org.hibernate.query.Query;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.Date;
import java.util.List;

public class FemmeService {

    public Femme save(Femme f) {
        Session s = HibernateUtil.getSessionFactory().openSession();
        s.beginTransaction();
        s.save(f);
        s.getTransaction().commit();
        s.close();
        return f;
    }

    // native named-like query executed via createNativeQuery
    public int countEnfantsBetween(Long femmeId, Date from, Date to) {
        Session s = HibernateUtil.getSessionFactory().openSession();
        String sql = "select sum(m.nbrEnfant) from mariage m where m.femme_id = :fid and m.dateDebut between :from and :to";
        javax.persistence.Query q = s.createNativeQuery(sql);
        q.setParameter("fid", femmeId);
        q.setParameter("from", from);
        q.setParameter("to", to);
        Object res = q.getSingleResult();
        s.close();
        if (res == null)
            return 0;
        return ((Number) res).intValue();
    }

    // named query style: women married at least twice
    public List<Femme> femmesMariéesDeuxFoisOuPlus() {
        Session s = HibernateUtil.getSessionFactory().openSession();
        String hql = "select f from Femme f where size(f.mariages) >= 2";
        Query<Femme> q = s.createQuery(hql, Femme.class);
        List<Femme> res = q.list();
        s.close();
        return res;
    }

    // Criteria API: number of men married to 4 women between two dates
    public long countHommesMarriésAQuatreFemmesBetween(Date from, Date to) {
        Session s = HibernateUtil.getSessionFactory().openSession();
        CriteriaBuilder cb = s.getCriteriaBuilder();
        CriteriaQuery<Long> cq = cb.createQuery(Long.class);
        Root<Mariage> root = cq.from(Mariage.class);
        cq.select(cb.countDistinct(root.get("homme")));
        cq.where(cb.between(root.get("dateDebut"), from, to));
        cq.groupBy(root.get("homme"));
        // We can't express "having count(femme)=4" directly via this simple API without
        // Subquery; implement in HQL fallback
        String hql = "select m.homme.id from Mariage m where m.dateDebut between :from and :to group by m.homme.id having count(distinct m.femme)=4";
        Query<Long> q = s.createQuery(hql, Long.class);
        q.setParameter("from", from);
        q.setParameter("to", to);
        List<Long> res = q.list();
        s.close();
        return res.size();
    }

}
