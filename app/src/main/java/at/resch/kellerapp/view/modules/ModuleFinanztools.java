package at.resch.kellerapp.view.modules;

import at.resch.kellerapp.user.RequiresPermission;
import at.resch.kellerapp.view.Module;
import at.resch.kellerapp.view.ViewManager;

/**
 * Created by felix on 8/4/14.
 */
@RequiresPermission("PERMISSION_VIEW")
public class ModuleFinanztools implements Module {
    @Override
    public String getName() {
        return "Finanztools";
    }

    @Override
    public String[] getPermissions() {
        return new String[] {"PERMISSION_ACCOUNTING"};
    }

    @Override
    public void open(ViewManager viewManager) {
        viewManager.closeView();
    }

    @Override
    public void reopen(ViewManager viewManager) {

    }

    @Override
    public void close(ViewManager viewManager) {

    }
}
