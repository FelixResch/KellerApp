package at.resch.kellerapp.view.modules;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import at.resch.kellerapp.R;
import at.resch.kellerapp.model.Model;
import at.resch.kellerapp.model.MoneyResource;
import at.resch.kellerapp.model.TransactionManager;
import at.resch.kellerapp.user.RequiresPermission;
import at.resch.kellerapp.view.View;
import at.resch.kellerapp.view.ViewManager;

/**
 * Created by felix on 8/5/14.
 */
@RequiresPermission("PERMISSION_ACCOUNTING")
public class BuchungsView implements View {

    @Override
    public void open(final ViewManager viewManager) {
        viewManager.getActivity().setContentView(R.layout.buchung_layout);
        final Spinner from = (Spinner) viewManager.getActivity().findViewById(R.id.from);
        final Spinner to = (Spinner) viewManager.getActivity().findViewById(R.id.to);
        MoneyResourceAdapter from_ = new MoneyResourceAdapter(viewManager.getActivity());
        from_.addAll(Model.select(MoneyResource.class));
        from_.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        from.setAdapter(from_);
        MoneyResourceAdapter to_ = new MoneyResourceAdapter(viewManager.getActivity());
        to_.addAll(Model.select(MoneyResource.class));
        to_.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        to.setAdapter(to_);
        ((Button) viewManager.getActivity().findViewById(R.id.book)).setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(android.view.View view) {
                MoneyResource f = ((MoneyResourceAdapter) from.getAdapter()).getItem(from.getSelectedItemPosition());
                MoneyResource t = ((MoneyResourceAdapter) to.getAdapter()).getItem(to.getSelectedItemPosition());
                double amount = Double.parseDouble(((TextView) viewManager.getActivity().findViewById(R.id.amount)).getText().toString());
                TransactionManager.transact(f, t, amount);
            }
        });
    }

    @Override
    public void reopen(ViewManager viewManager) {
        open(viewManager);
    }

    @Override
    public void close(ViewManager viewManager) {

    }

    public class MoneyResourceAdapter extends ArrayAdapter<MoneyResource> {

        public MoneyResourceAdapter(Context context) {
            super(context, android.R.layout.simple_list_item_1);
        }

        @Override
        public android.view.View getView(int position, android.view.View convertView, ViewGroup parent) {
            android.view.View v = super.getView(position, convertView, parent);
            ((TextView) v).setText(getItem(position).getName());
            return v;
        }

        @Override
        public android.view.View getDropDownView(int position, android.view.View convertView, ViewGroup parent) {
            android.view.View v = super.getDropDownView(position, convertView, parent);
            ((TextView) v).setText(getItem(position).getName());
            return v;
        }
    }
}
