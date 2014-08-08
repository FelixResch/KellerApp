package at.resch.kellerapp.model;

import at.resch.kellerapp.persistence.AutoIncrement;
import at.resch.kellerapp.persistence.Field;
import at.resch.kellerapp.persistence.ForeignKey;
import at.resch.kellerapp.persistence.PrimaryKey;

/**
 * Created by felix on 8/8/14.
 */
public class Transaction {

    @Id(type = Integer.class)
    @PrimaryKey
    @Field("t_id")
    @AutoIncrement
    private int number;

    @Field("t_mr_from")
    @ForeignKey(table = "mr_money_resources", field = "mr_id")
    private int from;

    @Field("t_mr_to")
    @ForeignKey(table = "mr_money_resources", field = "mr_id")
    private int to;

    @Field("t_amount")
    private double amount;

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getFrom() {
        return from;
    }

    public void setFrom(int from) {
        this.from = from;
    }

    public int getTo() {
        return to;
    }

    public void setTo(int to) {
        this.to = to;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}
