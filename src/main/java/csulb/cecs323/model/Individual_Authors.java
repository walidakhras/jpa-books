package csulb.cecs323.model;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@NamedNativeQuery(
        name = "ReturnIndividualAuthor",
        query = "SELECT *" +
                "FROM INDIVIDUAL_AUTHORS" +
                "WHERE email = ?",
        resultClass = Individual_Authors.class
)

@NamedNativeQuery(
        name = "ReturnAllIndividualAuthors",
        query = "SELECT *" +
                "FROM INDIVIDUAL_AUTHORS",
        resultClass = Individual_Authors.class
)
public class Individual_Authors extends Authoring_Entities {

    // Code citation:
    // We utilized this site https://www.baeldung.com/jpa-many-to-many
    // as a reference for the Many-to-Many code mapping for
    // Individual authors and ad_hoc_teams
    @ManyToMany(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    @JoinTable(
            name = "Ad_Hoc_Teams_Member",
            joinColumns = @JoinColumn(name = "author_email"),
            inverseJoinColumns = @JoinColumn(name = "ad_hoc_teams_email")
    )
    Set<Ad_Hoc_Team> teams = new HashSet<>();

    public Individual_Authors() {
    }

    public Individual_Authors(String email, String name) {
        super(email, name);
    }

    public Set<Ad_Hoc_Team> getTeams() {
        return teams;
    }

    @Override
    public String toString() {
        return "Author name: " + getName() + " Author email: " + getEmail();
    }
}
