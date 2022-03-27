package csulb.cecs323.model;

import javax.persistence.*;
import java.util.Set;

@Entity
public class Individual_Authors extends Authoring_Entities {

    @ManyToMany
    @JoinTable(
            name = "Ad_Hoc_Teams_Member",
            joinColumns = @JoinColumn(name = "author_email"),
            inverseJoinColumns = @JoinColumn(name = "ad_hoc_teams_email")
    )
    Set<Ad_Hoc_Team> teams;

    public Individual_Authors() {
    }

    public Individual_Authors(String email, String name) {
        super(email, name);
    }
}
