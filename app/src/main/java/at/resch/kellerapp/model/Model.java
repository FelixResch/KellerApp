package at.resch.kellerapp.model;

import java.util.ArrayList;

import at.resch.kellerapp.persistence.Database;
import at.resch.kellerapp.persistence.Table;
import at.resch.kellerapp.user.UserManager;

/**
 * Created by felix on 8/4/14.
 */
@Database("kellerapp")
public class Model {

    private static Model instance;

    public static Model get() {
        return instance;
    }

    public Model() {
        instance = this;
    }

    @Table("c_cards")
    private ArrayList<Card> cards;

    @Table("u_user")
    private ArrayList<User> user;

    @Table("i_identities")
    private ArrayList<Identity> identities;

    @Table("p_permissions")
    private ArrayList<Permission> permissions;

    @Table("ca_categories")
    private ArrayList<Category> categories;


    private UserManager userManager;
    private Settings settings;

    public UserManager getUserManager() {
        return userManager;
    }

    public void setUserManager(UserManager userManager) {
        this.userManager = userManager;
    }

    public ArrayList<Card> getCards() {
        return cards;
    }

    public void setCards(ArrayList<Card> cards) {
        this.cards = cards;
    }

    public ArrayList<User> getUser() {
        return user;
    }

    public void setUser(ArrayList<User> user) {
        this.user = user;
    }

    public ArrayList<Identity> getIdentities() {
        return identities;
    }

    public void setIdentities(ArrayList<Identity> identities) {
        this.identities = identities;
    }

    public ArrayList<Permission> getPermissions() {
        return permissions;
    }

    public void setPermissions(ArrayList<Permission> permissions) {
        this.permissions = permissions;
    }

    public Settings getSettings() {
        return settings;
    }

    public void setSettings(Settings settings) {
        this.settings = settings;
    }

    public ArrayList<Category> getCategories() {
        return categories;
    }

    public void setCategories(ArrayList<Category> categories) {
        this.categories = categories;
    }

    public Card getCard(String id) {
        for (Card c : cards) {
            if (c.getId().equals(id))
                return c;
        }
        return null;
    }

    public User getUser(int id) {
        for (User u : user) {
            if (u.getId() == id)
                return u;
        }
        return null;
    }

    public User getUser(String id) {
        for (Identity i : identities) {
            if (i.getCard().equals(id))
                return getUser(i.getUser());
        }
        return null;
    }
}

