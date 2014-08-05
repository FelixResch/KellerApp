package at.resch.kellerapp.view.modules;

import at.resch.kellerapp.model.Model;
import at.resch.kellerapp.user.RequiresPermission;
import at.resch.kellerapp.view.Module;
import at.resch.kellerapp.view.ViewManager;

/**
 * Created by felix on 8/5/14.
 */
@RequiresPermission("PERMISSION_VIEW")
public class ModuleLogout implements Module {
    @Override
    public String getName() {
        return "Logout";
    }

    @Override
    public String[] getPermissions() {
        return new String[]{"PERMISSION_VIEW"};
    }

    @Override
    public void open(ViewManager viewManager) {
        Model.get().getUserManager().onCardDetected("00000000");
        viewManager.closeView();
    }

    @Override
    public void reopen(ViewManager viewManager) {

    }

    @Override
    public void close(ViewManager viewManager) {

    }
}
