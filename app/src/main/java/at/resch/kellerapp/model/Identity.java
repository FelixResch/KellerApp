package at.resch.kellerapp.model;

import at.resch.kellerapp.persistence.Field;

/**
 * Created by felix on 8/4/14.
 */
public class Identity {

    @Field("i_c_card")
    private String card;

    @Field("i_u_user")
    private int user;

    public int getUser() {
        return user;
    }

    public void setUser(int user) {
        this.user = user;
    }

    public String getCard() {
        return card;
    }

    public void setCard(String card) {
        this.card = card;
    }
}
