package ma.projet.service;

import ma.projet.beans.Mariage;
import ma.projet.util.HibernateUtil;
import org.hibernate.Session;

import java.util.List;

public class MariageService {

    public Mariage save(Mariage m) {
        Session s = HibernateUtil.getSessionFactory().openSession();
        s.beginTransaction();
        s.save(m);
        s.getTransaction().commit();
        s.close();
        return m;
    }

    public List<Mariage> findAll() {
        Session s = HibernateUtil.getSessionFactory().openSession();
        List<Mariage> list = s.createQuery("from Mariage", Mariage.class).list();
        s.close();
        return list;
    }
}
