package at.resch.kellerapp.model;

import at.resch.kellerapp.persistence.Field;

/**
 * Created by felix on 8/4/14.
 */
public class Card {

    @Field("c_id")
    private String id;

    @Field("c_ct_type")
    private int type;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
