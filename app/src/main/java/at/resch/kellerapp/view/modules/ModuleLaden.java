package at.resch.kellerapp.view.modules;

import android.app.Dialog;
import android.content.DialogInterface;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import at.resch.kellerapp.R;
import at.resch.kellerapp.model.Model;
import at.resch.kellerapp.model.User;
import at.resch.kellerapp.user.CardHandler;
import at.resch.kellerapp.user.CardListener;
import at.resch.kellerapp.view.Module;
import at.resch.kellerapp.view.ViewManager;

/**
 * Created by felix on 8/5/14.
 */
public class ModuleLaden implements Module {
    @Override
    public String getName() {
        return "Aufladen";
    }

    @Override
    public String[] getPermissions() {
        return new String[]{"PERMISSION_TRANSACTION", "PERMISSION_CARD"};
    }

    @Override
    public void open(final ViewManager viewManager) {
        final Dialog dialog = new Dialog(viewManager.getActivity());
        dialog.setTitle("Zu Buchender Betrag");
        dialog.setContentView(R.layout.total_dialog_layout);
        ((Button) dialog.findViewById(R.id.ok)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                EditText amount = (EditText) dialog.findViewById(R.id.amount);
                try {
                    final double money = Double.parseDouble(amount.getText().toString());
                    viewManager.getActivity().setContentView(R.layout.nfc_wait);
                    CardHandler.get().addCardListener(new CardListener() {
                        @Override
                        public void onCardDetected(String card) {
                            User u = Model.get().getUser(card);
                            if (u != null) {
                                u.setBalance(u.getBalance() + money);
                                Model.get().update(u);
                                Toast.makeText(viewManager.getActivity(), "Buche " + String.format("%.2f", money) + "€ auf das Konto von " + u.getName(), Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(viewManager.getActivity(), "Unbekannte Karte " + card, Toast.LENGTH_SHORT).show();
                            }
                            CardHandler.get().removeCardListener();
                            viewManager.closeView();
                        }
                    });
                } catch (NumberFormatException e) {
                    Toast.makeText(viewManager.getActivity(), "Falsches Zahlenformat", Toast.LENGTH_SHORT).show();
                }
                dialog.dismiss();
            }
        });
        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                viewManager.closeView();
            }
        });
        dialog.show();
    }

    @Override
    public void reopen(ViewManager viewManager) {
        open(viewManager);
    }

    @Override
    public void close(ViewManager viewManager) {

    }
}
