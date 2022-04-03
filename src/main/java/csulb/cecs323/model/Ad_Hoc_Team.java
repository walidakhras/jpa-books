package csulb.cecs323.model;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@NamedNativeQuery(
        name = "ReturnAdHocTeam",
        query = "SELECT *" +
                "FROM AUTHORING_ENTITIES " +
                "WHERE email = ? AND authoring_entity_type = 'Ad_Hoc_Team'",
        resultClass = Ad_Hoc_Team.class
)

@NamedNativeQuery(
        name = "ReturnAllAdHocTeams",
        query = "SELECT *" +
                "FROM AUTHORING_ENTITIES " +
                "WHERE authoring_entity_type = ?",
        resultClass = Ad_Hoc_Team.class
)
public class Ad_Hoc_Team extends Authoring_Entities {

    @ManyToMany(mappedBy = "teams", cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    Set<Individual_Authors> authors = new HashSet<>();

    public Ad_Hoc_Team() {}

    public Ad_Hoc_Team(String email, String name) {
        super(email, name);
    }

    // Code Citation:
    // We got the strategy for adding rows to the junction table between
    // Individual authors and ad hoc teams from this article
    // https://www.infoworld.com/article/3373652/java-persistence-with-jpa-and-hibernate-part-1-entities-and-relationships.html

    public void addAuthorToAdTeam(Individual_Authors author) {
        authors.add(author);
        author.getTeams().add(this);
    }

    @Override
    public String toString() {
        return "Team Name: " + getName() + " Team Email: " + getEmail();
    }
}
