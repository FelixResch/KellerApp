package at.resch.kellerapp.view.modules;

import android.widget.Toast;

import at.resch.kellerapp.R;
import at.resch.kellerapp.model.Model;
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
public class ModuleGuthaben implements Module {
    @Override
    public String getName() {
        return "Guthaben";
    }

    @Override
    public String[] getPermissions() {
        return new String[]{};
    }

    @Override
    public void open(final ViewManager viewManager) {
        viewManager.getActivity().setContentView(R.layout.nfc_wait);
        CardHandler.get().addCardListener(new CardListener() {
            @Override
            public void onCardDetected(String card) {
                User u = Model.get().getUser(card);
                if (u != null)
                    Toast.makeText(viewManager.getActivity(), u.getName() + " hat " + u.getBalance() + "€ Guthaben", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void reopen(ViewManager viewManager) {
        open(viewManager);
    }

    @Override
    public void close(ViewManager viewManager) {
        CardHandler.get().removeCardListener();
    }
}
