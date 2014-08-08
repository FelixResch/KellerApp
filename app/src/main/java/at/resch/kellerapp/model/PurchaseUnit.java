package at.resch.kellerapp.model;

import at.resch.kellerapp.persistence.AutoIncrement;
import at.resch.kellerapp.persistence.Field;
import at.resch.kellerapp.persistence.ForeignKey;
import at.resch.kellerapp.persistence.PrimaryKey;

/**
 * Created by felix on 8/8/14.
 */
public class PurchaseUnit {

    @PrimaryKey
    @AutoIncrement
    @Field("pu_id")
    private int id;

    @Field("pu_ig_ingredient")
    @ForeignKey(table = "ig_ingredients", field = "ig_id")
    private int ingredient;

    @Field("pu_amount")
    private double amount;

    @Field("pu_barcode")
    private String barcode;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIngredient() {
        return ingredient;
    }

    public void setIngredient(int ingredient) {
        this.ingredient = ingredient;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }
}
