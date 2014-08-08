package at.resch.kellerapp.model;

import at.resch.kellerapp.persistence.AutoIncrement;
import at.resch.kellerapp.persistence.Field;
import at.resch.kellerapp.persistence.PrimaryKey;

/**
 * Created by felix on 8/6/14.
 */
public class CardType {

    @Field("ct_id")
    @PrimaryKey
    @AutoIncrement
    @Id(type = Integer.class)
    private int id;

    @Field("ct_name")
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
