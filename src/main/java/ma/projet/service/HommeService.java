package ma.projet.service;

import ma.projet.beans.Homme;
import ma.projet.beans.Mariage;
import ma.projet.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.Date;
import java.util.List;

public class HommeService {

    public Homme save(Homme h) {
        Session s = HibernateUtil.getSessionFactory().openSession();
        s.beginTransaction();
        s.save(h);
        s.getTransaction().commit();
        s.close();
        return h;
    }

    public List<Object[]> getEpausesBetween(Long hommeId, Date from, Date to) {
        Session s = HibernateUtil.getSessionFactory().openSession();
        String hql = "select m.femme.nom, m.femme.prenom, m.dateDebut from Mariage m where m.homme.id = :hid and m.dateDebut between :from and :to";
        Query<Object[]> q = s.createQuery(hql, Object[].class);
        q.setParameter("hid", hommeId);
        q.setParameter("from", from);
        q.setParameter("to", to);
        List<Object[]> res = q.list();
        s.close();
        return res;
    }

    public List<Mariage> getMariagesWithDetails(Long hommeId) {
        Session s = HibernateUtil.getSessionFactory().openSession();
        String hql = "from Mariage m join fetch m.femme where m.homme.id = :hid";
        Query<Mariage> q = s.createQuery(hql, Mariage.class);
        q.setParameter("hid", hommeId);
        List<Mariage> res = q.list();
        s.close();
        return res;
    }
}
