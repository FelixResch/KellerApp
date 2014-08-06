package at.resch.kellerapp.model;

import at.resch.kellerapp.persistence.Field;
import at.resch.kellerapp.persistence.ForeignKey;
import at.resch.kellerapp.persistence.PrimaryKey;

/**
 * Created by felix on 8/4/14.
 */
public class Identity {

    @Field("i_c_card")
    @PrimaryKey
    @ForeignKey(table = "c_cards", field = "c_id")
    @Id
    private String card;

    @Field("i_u_user")
    @PrimaryKey
    @ForeignKey(table = "u_user", field = "u_id")
    @Id
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
