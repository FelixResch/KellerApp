package at.resch.kellerapp.view.modules;

import at.resch.kellerapp.view.Module;
import at.resch.kellerapp.view.ViewManager;

/**
 * Created by felix on 8/5/14.
 */
public class ModuleNeu implements Module {
    @Override
    public String getName() {
        return "Neue Karte";
    }

    @Override
    public String[] getPermissions() {
        return new String[]{"PERMISSION_TRANSACTION", "PERMISSION_CARD"};
    }

    @Override
    public void open(ViewManager viewManager) {
        viewManager.closeView();
    }

    @Override
    public void reopen(ViewManager viewManager) {
        open(viewManager);
    }

    @Override
    public void close(ViewManager viewManager) {

    }
}
