package at.resch.kellerapp.view.modules;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.widget.Toast;

import at.resch.kellerapp.R;
import at.resch.kellerapp.model.Card;
import at.resch.kellerapp.model.Identity;
import at.resch.kellerapp.model.Model;
import at.resch.kellerapp.user.CardHandler;
import at.resch.kellerapp.user.CardListener;
import at.resch.kellerapp.view.Module;
import at.resch.kellerapp.view.ViewManager;

/**
 * Created by felix on 8/5/14.
 */
public class ModuleEntfernen implements Module {
    @Override
    public String getName() {
        return "Entfernen";
    }

    @Override
    public String[] getPermissions() {
        return new String[]{"PERMISSION_TRANSACTION", "PERMISSION_CARD"};
    }

    @Override
    public void open(final ViewManager viewManager) {
        AlertDialog.Builder builder = new AlertDialog.Builder(viewManager.getActivity());
        builder.setTitle("Karte löschen");
        builder.setMessage("Dieser Vorgang entfernt die Karte aus der Datenbank, belässt jedoch den Benutzer sowie den Kontostand. \nFortfahren?");
        builder.setPositiveButton("Ja", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                viewManager.getActivity().setContentView(R.layout.nfc_wait);
                CardHandler.get().addCardListener(new CardListener() {
                    @Override
                    public void onCardDetected(String card) {
                        Toast.makeText(viewManager.getActivity(), "Deleting Card " + card, Toast.LENGTH_SHORT).show();
                        //TODO remove card with jdbc model
                        Model.get().remove(Model.get().get(Card.class, card));
                        Model.get().remove(Model.get().get(Identity.class, card));
                        Model.get().getUserManager().reload();
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
