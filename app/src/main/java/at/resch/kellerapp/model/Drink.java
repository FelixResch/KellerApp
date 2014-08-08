package at.resch.kellerapp.model;

import at.resch.kellerapp.persistence.AutoIncrement;
import at.resch.kellerapp.persistence.Field;
import at.resch.kellerapp.persistence.ForeignKey;
import at.resch.kellerapp.persistence.PrimaryKey;

/**
 * Created by felix on 8/8/14.
 */
public class Drink {

    @PrimaryKey
    @Field("d_id")
    @Id(type = Integer.class)
    @AutoIncrement
    private int id;

    @Field("d_price_normal")
    private double priceNormal;

    @Field("d_price_party")
    private double priceParty;

    @Field("d_price_special")
    private double priceSpecial;

    @Field("d_price_empl")
    private double priceEmpl;

    @Field("d_name")
    @Id
    private String name;

    @Field("d_required_age")
    private int requiredAge;

    @Field("d_amount")
    private double amount;

    @ForeignKey(table = "ca_categories", field = "ca_id")
    @Field("d_ca_category")
    private int category;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getPriceNormal() {
        return priceNormal;
    }

    public void setPriceNormal(double priceNormal) {
        this.priceNormal = priceNormal;
    }

    public double getPriceParty() {
        return priceParty;
    }

    public void setPriceParty(double priceParty) {
        this.priceParty = priceParty;
    }

    public double getPriceSpecial() {
        return priceSpecial;
    }

    public void setPriceSpecial(double priceSpecial) {
        this.priceSpecial = priceSpecial;
    }

    public double getPriceEmpl() {
        return priceEmpl;
    }

    public void setPriceEmpl(double priceEmpl) {
        this.priceEmpl = priceEmpl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getRequiredAge() {
        return requiredAge;
    }

    public void setRequiredAge(int requiredAge) {
        this.requiredAge = requiredAge;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }
}
