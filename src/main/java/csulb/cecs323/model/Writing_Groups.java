package csulb.cecs323.model;

import javax.persistence.*;

@Entity
public class Writing_Groups extends Authoring_Entities {
    @Column(length = 80)
    private String head_writer;

    @Column
    private int year_formed;

    public Writing_Groups() {}

    public Writing_Groups(String head_writer, int year_formed) {
        this.head_writer = head_writer;
        this.year_formed = year_formed;
    }

    public Writing_Groups(String email, String name, String head_writer, int year_formed) {
        super(email, name);
        this.head_writer = head_writer;
        this.year_formed = year_formed;
    }

    public String getHead_writer() {
        return head_writer;
    }

    public void setHead_writer(String head_writer) {
        this.head_writer = head_writer;
    }

    public int getYear_formed() {
        return year_formed;
    }

    public void setYear_formed(int year_formed) {
        this.year_formed = year_formed;
    }

}
