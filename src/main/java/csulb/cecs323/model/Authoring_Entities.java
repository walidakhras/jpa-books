package csulb.cecs323.model;

import javax.persistence.*;

@Entity
@NamedNativeQuery(
        name = "ReturnAuthoringEntity",
        query = "SELECT *" +
                "FROM AUTHORING_ENTITIES " +
                "WHERE email = ?",
        resultClass = Authoring_Entities.class
)

@NamedNativeQuery(
        name = "ReturnAllAuthoringEntities",
        query = "SELECT *" +
                "FROM AUTHORING_ENTITIES",
        resultClass = Authoring_Entities.class
)
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "Authoring_Entity_Type")
public abstract class Authoring_Entities {
    @Id
    @Column(nullable = false, length = 30)
    private String email;

    @Column(nullable = false, length = 31)
    private String name;

    @Column(name = "Authoring_Entity_Type")
    private String type;

    public Authoring_Entities() {}

    public Authoring_Entities(String email, String name) {
        this.email = email;
        this.name = name;
    }
        
    public String getAuthoringEntityType() {
            return type;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
