package at.resch.kellerapp.model;

/**
 * Created by felix on 8/19/14.
 */
public class TransactionManager {

    public static void transact(MoneyResource from, MoneyResource to, double amount) {
        Transaction transaction = new Transaction();
        transaction.setAmount(amount);
        transaction.setFrom(from.getId());
        transaction.setTo(to.getId());
        Model.get().add(transaction);
        from.setAmount(from.getAmount() - amount);
        to.setAmount(to.getAmount() + amount);
        Model.get().updateAll(from, to);
    }

    public static void transact(MoneyResource from, double amount) {
        transact(from, Model.select(MoneyResource.class).first(), amount);
    }

    public static void transact(String from, String to, double amount) {
        transact(Model.select(MoneyResource.class).where("mr_name_short", from).first(), Model.select(MoneyResource.class).where("mr_name_short", to).first(), amount);
    }

    public static void transact(String from, double amount) {
        transact(Model.select(MoneyResource.class).where("mr_name_short", from).first(), amount);
    }
}
