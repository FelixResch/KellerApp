package at.resch.kellerapp.model;

import android.util.Log;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;

import at.resch.kellerapp.persistence.Database;
import at.resch.kellerapp.persistence.PersistenceManager;
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

    @Table("ct_cardtypes")
    private ArrayList<CardType> cardTypes;

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
        return get(Card.class, id);
    }

    public User getUser(int id) {
        return get(User.class, id);
    }

    public User getUser(String id) {
        return get(User.class, get(Identity.class, id).getUser());
    }

    public ArrayList<CardType> getCardTypes() {
        return cardTypes;
    }

    public void setCardTypes(ArrayList<CardType> cardTypes) {
        this.cardTypes = cardTypes;
    }

    public void add(Object o) {
        for (Field f : getClass().getDeclaredFields()) {
            if (f.isAnnotationPresent(Table.class) && ((Class<?>) ((ParameterizedType) f.getGenericType()).getActualTypeArguments()[0]) == o.getClass()) {
                try {
                    ArrayList list = (ArrayList) f.get(this);
                    list.add(o);
                    PersistenceManager.insert(f.getAnnotation(Table.class).value(), o);
                    return;
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        Log.d("kellerapp-log", "No List for Type: " + o.getClass().getCanonicalName());
    }

    public void addOverwrite(Object o) {
        for (Field f : getClass().getDeclaredFields()) {
            if (f.isAnnotationPresent(Table.class) && ((Class<?>) ((ParameterizedType) f.getGenericType()).getActualTypeArguments()[0]) == o.getClass()) {
                try {
                    ArrayList list = (ArrayList) f.get(this);
                    list.add(o);
                    PersistenceManager.insert(f.getAnnotation(Table.class).value(), o, true);
                    return;
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        Log.d("kellerapp-log", "No List for Type: " + o.getClass().getCanonicalName());
    }

    public void remove(Object o) {
        for (Field f : getClass().getDeclaredFields()) {
            if (f.isAnnotationPresent(Table.class) && ((Class<?>) ((ParameterizedType) f.getGenericType()).getActualTypeArguments()[0]) == o.getClass()) {
                try {
                    ArrayList list = (ArrayList) f.get(this);
                    list.remove(o);
                    PersistenceManager.delete(f.getAnnotation(Table.class).value(), o);
                    return;
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        Log.d("kellerapp-log", "No List for Type: " + o.getClass().getCanonicalName());
    }

    public void update(Object o) {
        for (Field f : getClass().getDeclaredFields()) {
            if (f.isAnnotationPresent(Table.class) && ((Class<?>) ((ParameterizedType) f.getGenericType()).getActualTypeArguments()[0]) == o.getClass()) {
                PersistenceManager.update(f.getAnnotation(Table.class).value(), o);
                return;
            }
        }
        Log.d("kellerapp-log", "No List for Type: " + o.getClass().getCanonicalName());
    }

    public <T> T get(Class<T> type, Object id) {
        for (Field f : getClass().getDeclaredFields()) {
            if (f.isAnnotationPresent(Table.class) && ((Class<?>) ((ParameterizedType) f.getGenericType()).getActualTypeArguments()[0]) == type) {
                try {
                    ArrayList list = (ArrayList) f.get(this);
                    for (Object o : list) {
                        for (Field ids : type.getDeclaredFields()) {
                            ids.setAccessible(true);
                            if (ids.isAnnotationPresent(Id.class)) {
                                Id i = ids.getAnnotation(Id.class);
                                if (i.type() == Void.class && ids.getType().isAssignableFrom(id.getClass()) || ids.getType() == id.getClass()) {
                                    Object val = ids.get(o);
                                    boolean eval = val.equals(id);
                                    if (val.equals(id)) {
                                        return (T) o;
                                    }
                                } else if (i.type() != Void.class && i.type().isAssignableFrom(id.getClass()) || i.type() == id.getClass()) {
                                    if (ids.get(o).equals(id)) {
                                        return (T) o;
                                    }
                                }
                            }
                        }
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    Log.e("kellerapp-log", e.getMessage(), e);
                }
            }
        }
        return null;
    }
}

