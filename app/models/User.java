package models;


import play.data.validation.Constraints;
import play.db.jpa.JPA;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.List;

@Entity
public class User {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    public Long id;

    @Constraints.Required
    public String email;

    @Constraints.Required
    public String name;

    @Constraints.Required
    public String password;


    public static User findById(Long id) {

        return JPA.em().find(User.class, id);
    }

    public static User authenticate(String email, String password) {
        List<User> users = (List<User>) JPA
                .em()
                .createQuery(
                        "select u from User u where u.email = :email "
                                + "and u.password = :password ")
                .setParameter("email", email)
                .setParameter("password", password)
                .getResultList();
        if(users != null && users.size() > 0){
            return users.get(0);
        }else{
            return null;
        }
    }

    public void update(Long id) {
        this.id = id;
        JPA.em().merge(this);
    }

    public void save() {
        JPA.em().persist(this);
    }

    public void delete() {
        JPA.em().remove(this);
    }


}




