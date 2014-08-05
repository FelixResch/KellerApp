package at.resch.kellerapp.model;

import at.resch.kellerapp.persistence.Field;

/**
 * Created by felix on 8/4/14.
 */
public class Category {

    @Field("ca_id")
    private int id;

    @Field("ca_name")
    private String name;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
