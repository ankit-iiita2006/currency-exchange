package models;

import play.data.validation.Constraints;
import play.db.jpa.JPA;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;


@Entity
public class Convertor {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    public Long id;

    @Constraints.Required
    public String to;

    @Constraints.Required
    public String from;

    @Constraints.Required
    public Double valuation;

    public static Convertor findById(Long id) {
        return JPA.em().find(Convertor.class, id);
    }

}

