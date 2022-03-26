package csulb.cecs323.model;

import javax.persistence.*;

@Entity
//@NamedNativeQuery(
//
//)
public class Books {
    @Id
    @Column(nullable = false, length = 17)
    private String ISBN;

    @Column(nullable = false, length = 80)
    private String title;

    @Column(nullable = false)
    private int year_published;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "authoring_entities", nullable = false)
    private Authoring_Entities authoring_entities;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "name", nullable = false)
    private Publishers Publisher;

    public Books() {}


}
