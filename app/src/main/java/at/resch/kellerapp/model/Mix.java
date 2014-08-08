package at.resch.kellerapp.model;

import at.resch.kellerapp.persistence.Field;
import at.resch.kellerapp.persistence.ForeignKey;
import at.resch.kellerapp.persistence.PrimaryKey;

/**
 * Created by felix on 8/8/14.
 */
public class Mix {

    @PrimaryKey
    @Field("m_ig_ingredient")
    @ForeignKey(table = "ig_ingredients", field = "ig_id")
    private int ingredient;

    @PrimaryKey
    @Field("m_d_drink")
    @ForeignKey(table = "d_drinks", field = "d_id")
    @Id(type = Integer.class)
    private int drink;

    @Field("m_amount")
    private double amount;

    public int getIngredient() {
        return ingredient;
    }

    public void setIngredient(int ingredient) {
        this.ingredient = ingredient;
    }

    public int getDrink() {
        return drink;
    }

    public void setDrink(int drink) {
        this.drink = drink;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}
