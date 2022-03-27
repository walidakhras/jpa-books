package csulb.cecs323.model;

import javax.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "Authoring_Entity_Type")
public abstract class Authoring_Entities {
    @Id
    @Column(nullable = false, length = 30)
    private String email;

    @Column(nullable = false, length = 31)
    private String name;

    public Authoring_Entities() {}

    public Authoring_Entities(String email, String name) {
        this.email = email;
        this.name = name;
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
