package at.resch.kellerapp.view.modules;

import at.resch.kellerapp.R;
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
    public void open(ViewManager viewManager) {
        viewManager.getActivity().setContentView(R.layout.nfc_wait);
    }

    @Override
    public void reopen(ViewManager viewManager) {

    }

    @Override
    public void close(ViewManager viewManager) {

    }
}
