package csulb.cecs323.model;

import javax.persistence.*;
import java.util.Set;

@Entity
public class Ad_Hoc_Team extends Authoring_Entities {

    @ManyToMany(mappedBy = "teams")
    Set<Individual_Authors> authors;

    public Ad_Hoc_Team() {}

    public Ad_Hoc_Team(String email, String name) {
        super(email, name);
    }
}
