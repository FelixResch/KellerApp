package at.resch.kellerapp.view.modules;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
public class ModuleAuflosen implements Module {
    @Override
    public String getName() {
        return "Auflösen";
    }

    @Override
    public String[] getPermissions() {
        return new String[]{"PERMISSION_TRANSACTION", "PERMISSION_CARD"};
    }

    @Override
    public void open(final ViewManager viewManager) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(viewManager.getActivity());
        builder.setTitle("Konto Auflösen");
        builder.setMessage("Dieser Vorgang entfernt den Benutzer dieser Karte, sowie alle dazugehörigen Karten aus der Datenbank und löst das assozierte Konto auf!\nFortfahren?");
        builder.setPositiveButton("Ja", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                viewManager.getActivity().setContentView(R.layout.nfc_wait);
                CardHandler.get().addCardListener(new CardListener() {
                    @Override
                    public void onCardDetected(String card) {
                        User u = Model.get().getUser(card);
                        if (u != null) {
                            //TODO remove with jdbc model
                            Model.get().remove(u);
                            Model.get().remove(Model.get().getCard(card));
                            Model.get().getUserManager().reload();
                            Toast.makeText(viewManager.getActivity(), "Entferne das Konto von " + u.getName(), Toast.LENGTH_SHORT).show();
                            AlertDialog.Builder builder1 = new AlertDialog.Builder(viewManager.getActivity());
                            builder1.setTitle("Geldrückgabe");
                            builder1.setMessage("Kunde bekommt " + String.format("%.2f", u.getBalance()) + "€ zurück!");
                            builder1.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }
                            });
                            builder1.show();
                            builder1.setOnCancelListener(new DialogInterface.OnCancelListener() {
                                @Override
                                public void onCancel(DialogInterface dialogInterface) {
                                    viewManager.closeView();
                                }
                            });
                        } else {
                            Toast.makeText(viewManager.getActivity(), "Unbekannte Karte " + card, Toast.LENGTH_SHORT).show();
                        }
                        CardHandler.get().removeCardListener();
                        viewManager.closeView();
                    }
                });
            }
        });
        builder.setNegativeButton("Nein", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                viewManager.closeView();
            }
        });
        builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                viewManager.closeView();
            }
        });
        builder.show();
    }

    @Override
    public void reopen(ViewManager viewManager) {
        open(viewManager);
    }

    @Override
    public void close(ViewManager viewManager) {

    }
}
