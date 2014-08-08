package at.resch.kellerapp.model;

import at.resch.kellerapp.persistence.AutoIncrement;
import at.resch.kellerapp.persistence.Field;
import at.resch.kellerapp.persistence.PrimaryKey;

/**
 * Created by felix on 8/8/14.
 */
public class MoneyResource {

    @PrimaryKey
    @AutoIncrement
    @Id(type = Integer.class)
    @Field("mr_id")
    private int id;

    @Field("mr_name")
    private String name;

    @Field("mr_name_short")
    @Id
    private String shortName;

    @Field("mr_amount")
    private double amount;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}
