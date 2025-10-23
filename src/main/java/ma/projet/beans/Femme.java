package ma.projet.beans;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "femme")
public class Femme extends Personne {

    @OneToMany(mappedBy = "femme", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Mariage> mariages = new ArrayList<>();

    public List<Mariage> getMariages() {
        return mariages;
    }

    public void setMariages(List<Mariage> mariages) {
        this.mariages = mariages;
    }

    public void addMariage(Mariage m) {
        mariages.add(m);
        m.setFemme(this);
    }

    public void removeMariage(Mariage m) {
        mariages.remove(m);
        m.setFemme(null);
    }
}