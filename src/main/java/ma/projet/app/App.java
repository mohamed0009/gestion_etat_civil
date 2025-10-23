package ma.projet.app;

import ma.projet.beans.Femme;
import ma.projet.beans.Homme;
import ma.projet.beans.Mariage;
import ma.projet.service.FemmeService;
import ma.projet.service.HommeService;
import ma.projet.service.MariageService;
import ma.projet.util.HibernateUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class App {
    public static void main(String[] args) throws Exception {
        FemmeService femmeService = new FemmeService();
        HommeService hommeService = new HommeService();
        MariageService mariageService = new MariageService();

        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");

        // create 10 femmes
        List<Femme> femmes = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            Femme f = new Femme();
            f.setNom("NomF" + i);
            f.setPrenom("PrenomF" + i);
            f.setDateNaissance(df.parse("01/01/19" + (60 + i)));
            femmes.add(femmeService.save(f));
        }

        // create 5 hommes
        List<Homme> hommes = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            Homme h = new Homme();
            h.setNom("NomH" + i);
            h.setPrenom("PrenomH" + i);
            h.setDateNaissance(df.parse("01/01/19" + (50 + i)));
            hommes.add(hommeService.save(h));
        }

        // create some marriages
        Mariage m1 = new Mariage();
        m1.setHomme(hommes.get(0));
        m1.setFemme(femmes.get(0));
        m1.setDateDebut(df.parse("03/09/1990"));
        m1.setNbrEnfant(4);
        mariageService.save(m1);

        Mariage m2 = new Mariage();
        m2.setHomme(hommes.get(0));
        m2.setFemme(femmes.get(1));
        m2.setDateDebut(df.parse("03/09/1995"));
        m2.setNbrEnfant(2);
        mariageService.save(m2);

        // display femmes
        System.out.println("Liste des femmes :");
        for (Femme f : femmes)
            System.out.println(f.getNom() + " " + f.getPrenom());

        // example: display mariages of first homme
        System.out.println("Mariages de " + hommes.get(0).getNom() + " :");
        List<Mariage> mariages = hommeService.getMariagesWithDetails(hommes.get(0).getId());
        for (Mariage m : mariages) {
            System.out.println("Femme: " + m.getFemme().getNom() + " DateDebut: " + df.format(m.getDateDebut())
                    + " NbrEnfants: " + m.getNbrEnfant());
        }

        HibernateUtil.shutdown();
    }
}
