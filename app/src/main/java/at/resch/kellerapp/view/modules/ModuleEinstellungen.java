package at.resch.kellerapp.view.modules;

import android.view.View;
import android.widget.Button;
import android.widget.Switch;

import at.resch.kellerapp.R;
import at.resch.kellerapp.model.Model;
import at.resch.kellerapp.user.RequiresPermission;
import at.resch.kellerapp.view.Module;
import at.resch.kellerapp.view.ViewManager;

/**
 * Created by felix on 8/4/14.
 */
@RequiresPermission("PERMISSION_VIEW")
public class ModuleEinstellungen implements Module {
    @Override
    public String getName() {
        return "Einstellungen";
    }

    @Override
    public String[] getPermissions() {
        return new String[] {"PERMISSION_SETTINGS"};
    }

    @Override
    public void open(final ViewManager viewManager) {
        viewManager.getActivity().setContentView(R.layout.settings_layout);
        ((Switch)viewManager.getActivity().findViewById(R.id.party_modus)).setChecked(Model.get().getSettings().isPartyMode());
        ((Button)viewManager.getActivity().findViewById(R.id.abbrechen)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewManager.closeView();
            }
        });
        ((Button)viewManager.getActivity().findViewById(R.id.speichern)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Model.get().getSettings().setPartyMode(((Switch)viewManager.getActivity().findViewById(R.id.party_modus)).isChecked());
                Model.get().getSettings().store();
                viewManager.closeView();
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
}
