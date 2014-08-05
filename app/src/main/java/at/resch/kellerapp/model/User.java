package at.resch.kellerapp.model;

import java.util.Date;

import at.resch.kellerapp.persistence.Field;

/**
 * Created by felix on 8/4/14.
 */
public class User {

    @Field("u_id")
    private int id;

    @Field("u_name")
    private String name;

    @Field("u_birthday")
    private Date birthday;

    @Field("u_email")
    private String email;

    @Field("u_telephone")
    private String telephone;

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
