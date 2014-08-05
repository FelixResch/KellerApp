package at.resch.kellerapp.view.modules;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import at.resch.kellerapp.R;
import at.resch.kellerapp.user.RequiresPermission;
import at.resch.kellerapp.view.Module;
import at.resch.kellerapp.view.ViewManager;

/**
 * Created by felix on 8/4/14.
 */
@RequiresPermission("PERMISSION_VIEW")
public class ModuleEinkauf implements Module {

    @Override
    public String getName() {
        return "Einkauf";
    }

    @Override
    public String[] getPermissions() {
        return new String[] {"PERMISSION_TRANSACTION", "PERMISSION_STOCK"};
    }

    @Override
    public void open(final ViewManager viewManager) {
        viewManager.getActivity().setContentView(R.layout.einkauf_layout);
        ((Button) viewManager.getActivity().findViewById(R.id.back)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewManager.closeView();
            }
        });
        ((Button) viewManager.getActivity().findViewById(R.id.pay)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(viewManager.getActivity());
                builder.setTitle("Zahlungsmethode");
                builder.setMessage("Bitte wähle die Zahlungsmethode aus");
                builder.setNegativeButton("Bar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                builder.setPositiveButton("Karte", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                builder.show();
            }
        });
        ((Button) viewManager.getActivity().findViewById(R.id.total)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(viewManager.getActivity());
                dialog.setTitle("Gesamtpreis");
                dialog.setContentView(R.layout.total_dialog_layout);
                ((Button) dialog.findViewById(R.id.ok)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        EditText amount = (EditText) dialog.findViewById(R.id.amount);
                        TextView money = (TextView) viewManager.getActivity().findViewById(R.id.money);
                        try {
                            money.setText(String.format("%.2f", Double.parseDouble(amount.getText().toString())) + " €");
                        } catch (NumberFormatException e) {
                            Toast.makeText(viewManager.getActivity(), "Falsches Zahlenformat", Toast.LENGTH_SHORT).show();
                        }
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });
        {
            TabHost tabs = (TabHost) viewManager.getActivity().findViewById(R.id.tabHost);

            tabs.setup();

            TabHost.TabSpec spec = tabs.newTabSpec("Test 1");
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
    }

    @Override
    public void reopen(ViewManager viewManager) {

    }

    @Override
    public void close(ViewManager viewManager) {

    }
}
