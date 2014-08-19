package at.resch.kellerapp.view.modules;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;

import at.resch.kellerapp.R;
import at.resch.kellerapp.model.Category;
import at.resch.kellerapp.model.Drink;
import at.resch.kellerapp.model.Model;
import at.resch.kellerapp.model.MoneyResource;
import at.resch.kellerapp.model.Selection;
import at.resch.kellerapp.model.TransactionManager;
import at.resch.kellerapp.model.User;
import at.resch.kellerapp.user.CardHandler;
import at.resch.kellerapp.user.CardListener;
import at.resch.kellerapp.user.RequiresPermission;
import at.resch.kellerapp.view.Module;
import at.resch.kellerapp.view.ViewManager;

/**
 * Created by felix on 8/4/14.
 */
@RequiresPermission("PERMISSION_VIEW")
public class ModuleVerkauf implements Module {

    @Override
    public String getName() {
        return "Verkauf";
    }

    @Override
    public String[] getPermissions() {
        return new String[] {"PERMISSION_TRANSACTION", "PERMISSION_STOCK"};
    }

    @Override
    public void open(final ViewManager viewManager) {
        viewManager.getActivity().setContentView(R.layout.sell_layout);
        ((Button)viewManager.getActivity().findViewById(R.id.back)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewManager.closeView();
            }
        });
        ((Button) viewManager.getActivity().findViewById(R.id.clear)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        ((ListView) viewManager.getActivity().findViewById(R.id.order)).setAdapter(new OrderAdapter(viewManager.getActivity()));
        ((ListView) viewManager.getActivity().findViewById(R.id.order)).setOnItemClickListener(new OrderHandler());
        ((Button) viewManager.getActivity().findViewById(R.id.pay)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(viewManager.getActivity());
                builder.setTitle("Zahlungsmethode");
                builder.setMessage("Bitte wähle die Zahlungsmethode aus");
                builder.setNegativeButton("Bar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        MoneyResource kassa = Model.select(MoneyResource.class).where("mr_name_short", "kassa_rot").first();
                        MoneyResource gegen = Model.select(MoneyResource.class).where("mr_name_short", "gegen_bar").first();
                        ListView order = (ListView) viewManager.getActivity().findViewById(R.id.order);
                        OrderAdapter adapter = (OrderAdapter) order.getAdapter();
                        TransactionManager.transact(gegen, kassa, adapter.getTotal());
                        adapter.clear();
                        ((TextView) viewManager.getActivity().findViewById(R.id.total)).setText("0,00 € ");
                    }
                });
                builder.setPositiveButton("NFC", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        viewManager.getActivity().setContentView(R.layout.nfc_wait);
                        CardHandler.get().addCardListener(new CardListener() {
                            @Override
                            public void onCardDetected(String card) {
                                CardHandler.get().removeCardListener();
                                User user = Model.get().getUser(card);
                                ListView order = (ListView) viewManager.getActivity().findViewById(R.id.order);
                                final OrderAdapter adapter = (OrderAdapter) order.getAdapter();
                                if (user.getBalance() < adapter.getTotal()) {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(viewManager.getActivity());
                                    builder.setMessage("Benutzer hat nicht genügend Guthaben!\nZu bezahlen: " + adapter.getTotal() + "\nGuthaben: " + user.getBalance());
                                    builder.setTitle("Zu wenig Guthaben");
                                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            dialogInterface.dismiss();
                                            adapter.clear();
                                            ((TextView) viewManager.getActivity().findViewById(R.id.total)).setText("0,00 € ");
                                        }
                                    });
                                    builder.show();
                                } else {
                                    MoneyResource kassa = Model.select(MoneyResource.class).where("mr_name_short", "kassa_rot").first();
                                    MoneyResource cards = Model.select(MoneyResource.class).where("mr_name_short", "tmp_card").first();
                                    TransactionManager.transact(cards, kassa, adapter.getTotal());
                                    user.setBalance(user.getBalance() - adapter.getTotal());
                                    Model.get().updateAll(user);
                                    adapter.clear();
                                    ((TextView) viewManager.getActivity().findViewById(R.id.total)).setText("0,00 € ");
                                }
                            }
                        });
                    }
                });
                builder.show();
            }
        });
        {
            TabHost tabs = (TabHost) viewManager.getActivity().findViewById(R.id.tabHost);

            tabs.setup();

            TabSpec spec = tabs.newTabSpec("Test 1");
            spec.setContent(R.id.tab1);
            spec.setIndicator("Alkoholfrei");
            tabs.addTab(spec);
            spec = tabs.newTabSpec("Test 2");
            spec.setContent(R.id.tab2);
            spec.setIndicator("Alkoholisch");
            tabs.addTab(spec);
            spec = tabs.newTabSpec("Test 3");
            spec.setContent(R.id.tab3);
            spec.setIndicator("Mischgetränke");
            tabs.addTab(spec);
            spec = tabs.newTabSpec("Test 4");
            spec.setContent(R.id.tab4);
            spec.setIndicator("Stamperl");
            tabs.addTab(spec);
        }
        //Lade alkoholfreie getränke
        {
            Selection<Drink> drinks = Model.select(Drink.class).join(Model.select(Category.class).where("ca_name", "Alkoholfrei"));
            ListView listView = (ListView) viewManager.getActivity().findViewById(R.id.alkoholfrei);
            DrinkAdapter adapter = new DrinkAdapter(viewManager.getActivity());
            adapter.addAll(drinks);
            listView.setAdapter(adapter);
            listView.setOnItemClickListener(new DrinkHandler());
        }
        //Lade alkoholische getränke
        {
            Selection<Drink> drinks = Model.select(Drink.class).join(Model.select(Category.class).where("ca_name", "Alkoholisch"));
            ListView listView = (ListView) viewManager.getActivity().findViewById(R.id.alkoholisch);
            DrinkAdapter adapter = new DrinkAdapter(viewManager.getActivity());
            adapter.addAll(drinks);
            listView.setAdapter(adapter);
            listView.setOnItemClickListener(new DrinkHandler());
        }
        //Lade mischgetränke
        {
            Selection<Drink> drinks = Model.select(Drink.class).join(Model.select(Category.class).where("ca_name", "Mischgetränke"));
            ListView listView = (ListView) viewManager.getActivity().findViewById(R.id.mixdrinks);
            DrinkAdapter adapter = new DrinkAdapter(viewManager.getActivity());
            adapter.addAll(drinks);
            listView.setAdapter(adapter);
            listView.setOnItemClickListener(new DrinkHandler());
        }
        //Lade stamperl
        {
            Selection<Drink> drinks = Model.select(Drink.class).join(Model.select(Category.class).where("ca_name", "Stamperl"));
            ListView listView = (ListView) viewManager.getActivity().findViewById(R.id.shots);
            DrinkAdapter adapter = new DrinkAdapter(viewManager.getActivity());
            adapter.addAll(drinks);
            listView.setAdapter(adapter);
            listView.setOnItemClickListener(new DrinkHandler());
        }
    }

    public class DrinkAdapter extends ArrayAdapter<Drink> {

        public DrinkAdapter(Context context) {
            super(context, R.layout.drink_layout, R.id.name);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View v = super.getView(position, convertView, parent);
            Drink item = getItem(position);
            TextView name = (TextView) v.findViewById(R.id.name);
            name.setText(item.getName() + " (" + item.getAmount() + "l)");
            TextView price = (TextView) v.findViewById(R.id.price);
            price.setText(String.format("%.2f €", Model.get().getSettings().isPartyMode() ? item.getPriceParty() : item.getPriceNormal()));
            return v;
        }
    }

    public class DrinkHandler implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            DrinkAdapter adapter = (DrinkAdapter) adapterView.getAdapter();
            Drink item = adapter.getItem(i);
            Activity activity = ViewManager.get().getActivity();
            TextView total = (TextView) activity.findViewById(R.id.total);
            ListView order = (ListView) activity.findViewById(R.id.order);
            OrderAdapter orderAdapter = (OrderAdapter) order.getAdapter();

            orderAdapter.add(item);
            orderAdapter.notifyDataSetChanged();
            total.setText(String.format("%.2f € ", orderAdapter.getTotal()));
        }
    }

    public class OrderItem {
        private Drink drink;
        private int amount;

        public OrderItem(Drink drink) {
            this.drink = drink;
            this.amount = 1;
        }

        public double getPrice() {
            return (Model.get().getSettings().isPartyMode() ? drink.getPriceParty() : drink.getPriceNormal()) * amount;
        }
    }

    public class OrderAdapter extends ArrayAdapter<OrderItem> {

        public OrderAdapter(Context context) {
            super(context, R.layout.drink_layout, R.id.name);
        }

        public void add(Drink drink) {
            for (int i = 0; i < getCount(); i++) {
                if (getItem(i).drink.getId() == drink.getId()) {
                    getItem(i).amount++;
                    return;
                }
            }
            add(new OrderItem(drink));
        }

        public double getTotal() {
            double ret = 0.;
            for (int i = 0; i < getCount(); i++) {
                ret += getItem(i).getPrice();
            }
            return ret;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View v = super.getView(position, convertView, parent);
            OrderItem item = getItem(position);
            TextView name = (TextView) v.findViewById(R.id.name);
            name.setText(String.format("%2dx %s", item.amount, item.drink.getName()));
            TextView price = (TextView) v.findViewById(R.id.price);
            price.setText(String.format("%.2f €", item.getPrice()));
            return v;
        }
    }

    public class OrderHandler implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            OrderAdapter orderAdapter = (OrderAdapter) adapterView.getAdapter();
            OrderItem item = orderAdapter.getItem(i);
            if (item.amount == 1) {
                orderAdapter.remove(item);
            } else {
                item.amount--;
            }

            orderAdapter.notifyDataSetChanged();

            Activity activity = ViewManager.get().getActivity();
            TextView total = (TextView) activity.findViewById(R.id.total);
            total.setText(String.format("%.2f € ", orderAdapter.getTotal()));

        }
    }

    @Override
    public void reopen(ViewManager viewManager) {
        open(viewManager);
    }

    @Override
    public void close(ViewManager viewManager) {

    }
}
