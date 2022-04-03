package csulb.cecs323.model;

import javax.persistence.*;

@Entity
@NamedNativeQuery(
        name = "ReturnBook",
        query = "SELECT *" +
                "FROM BOOKS " +
                "WHERE ISBN = ?",
        resultClass = Books.class
)

@NamedNativeQuery(
        name = "ReturnAllBooks",
        query = "SELECT *" +
                "FROM WRITING_GROUPS",
        resultClass = Books.class
)
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

    public Books(String ISBN, String title, int year_published, Authoring_Entities authoring_entities, Publishers publisher) {
        this.ISBN = ISBN;
        this.title = title;
        this.year_published = year_published;
        this.authoring_entities = authoring_entities;
        this.Publisher = publisher;
    }

    public String getISBN() {
        return ISBN;
    }

    public void setISBN(String ISBN) {
        this.ISBN = ISBN;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getYear_published() {
        return year_published;
    }

    public void setYear_published(int year_published) {
        this.year_published = year_published;
    }

    public Authoring_Entities getAuthoring_entities() {
        return authoring_entities;
    }

    public void setAuthoring_entities(Authoring_Entities authoring_entities) {
        this.authoring_entities = authoring_entities;
    }

    public Publishers getPublisher() {
        return Publisher;
    }

    public void setPublisher(Publishers publisher) {
        Publisher = publisher;
    }

    @Override
    public String toString() {
        return "Book Title: " + getTitle() + " ISBN: " + getISBN() + " Year Published: " + getYear_published()
                + " Publisher: " + getPublisher() + " Authoring Entity: " + getAuthoring_entities();
    }
}
