package at.resch.kellerapp.model;

import at.resch.kellerapp.persistence.AutoIncrement;
import at.resch.kellerapp.persistence.Field;
import at.resch.kellerapp.persistence.PrimaryKey;

/**
 * Created by felix on 8/8/14.
 */
public class Ingredient {

    @PrimaryKey
    @AutoIncrement
    @Id(type = Integer.class)
    @Field("ig_id")
    private int id;

    @Field("ig_name")
    @Id
    private String name;

    @Field("ig_unit_size")
    private double unitSize;

    @Field("ig_stock_amount")
    private double stockAmount;

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

    public double getUnitSize() {
        return unitSize;
    }

    public void setUnitSize(double unitSize) {
        this.unitSize = unitSize;
    }

    public double getStockAmount() {
        return stockAmount;
    }

    public void setStockAmount(double stockAmount) {
        this.stockAmount = stockAmount;
    }
}
