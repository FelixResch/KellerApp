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
}
