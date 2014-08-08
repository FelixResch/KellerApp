package at.resch.kellerapp.model;

import android.util.Log;
import android.widget.Toast;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.sql.ResultSet;
import java.util.ArrayList;

import at.resch.kellerapp.persistence.Database;
import at.resch.kellerapp.persistence.PersistenceManager;
import at.resch.kellerapp.persistence.Query;
import at.resch.kellerapp.persistence.QueryExecutedListener;
import at.resch.kellerapp.persistence.QueryExecutor;
import at.resch.kellerapp.persistence.Table;
import at.resch.kellerapp.user.UserManager;
import at.resch.kellerapp.view.ViewManager;

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

    @Table("d_drinks")
    private ArrayList<Drink> drinks;

    @Table("ig_ingredients")
    private ArrayList<Ingredient> ingredients;

    @Table("m_mixes")
    private ArrayList<Mix> mixes;

    @Table("mr_money_resources")
    private ArrayList<MoneyResource> moneyResources;

    @Table("pu_purchase_units")
    private ArrayList<PurchaseUnit> purchaseUnits;

    @Table("t_transactions")
    private ArrayList<Transaction> transactions;

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

    public ArrayList<Drink> getDrinks() {
        return drinks;
    }

    public void setDrinks(ArrayList<Drink> drinks) {
        this.drinks = drinks;
    }

    public ArrayList<Ingredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(ArrayList<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    public ArrayList<Mix> getMixes() {
        return mixes;
    }

    public void setMixes(ArrayList<Mix> mixes) {
        this.mixes = mixes;
    }

    public ArrayList<MoneyResource> getMoneyResources() {
        return moneyResources;
    }

    public void setMoneyResources(ArrayList<MoneyResource> moneyResources) {
        this.moneyResources = moneyResources;
    }

    public ArrayList<PurchaseUnit> getPurchaseUnits() {
        return purchaseUnits;
    }

    public void setPurchaseUnits(ArrayList<PurchaseUnit> purchaseUnits) {
        this.purchaseUnits = purchaseUnits;
    }

    public ArrayList<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(ArrayList<Transaction> transactions) {
        this.transactions = transactions;
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

    public void migrate() {
        QueryExecutor executor = new QueryExecutor();
        executor.execute(new Query("SET foreign_key_checks = 0;", new QueryExecutedListener() {
            @Override
            public void executionFinished(ResultSet result) {
                for (Field f : Model.class.getDeclaredFields()) {
                    f.setAccessible(true);
                    if (f.isAnnotationPresent(Table.class)) {
                        try {
                            ArrayList list = (ArrayList) f.get(Model.this);
                            for (Object o : list) {
                                PersistenceManager.insert(f.getAnnotation(Table.class).value(), o);
                            }
                        } catch (IllegalAccessException e) {
                            Log.e("kellerapp-log", "Persitence Error", e);
                        }
                    }
                }
                QueryExecutor executor = new QueryExecutor();
                executor.execute(new Query("SET foreign_key_checks = 1;", new QueryExecutedListener() {
                    @Override
                    public void executionFinished(ResultSet result) {
                        Toast.makeText(ViewManager.get().getActivity(), "Migration abgeschlossen", Toast.LENGTH_SHORT).show();
                    }
                }));
            }
        }));

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

