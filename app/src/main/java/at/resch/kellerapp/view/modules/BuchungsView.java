package at.resch.kellerapp.view.modules;

import at.resch.kellerapp.R;
import at.resch.kellerapp.user.RequiresPermission;
import at.resch.kellerapp.view.View;
import at.resch.kellerapp.view.ViewManager;

/**
 * Created by felix on 8/5/14.
 */
@RequiresPermission("PERMISSION_ACCOUNTING")
public class BuchungsView implements View {

    @Override
    public void open(ViewManager viewManager) {
        viewManager.getActivity().setContentView(R.layout.buchung_layout);
    }

    @Override
    public void reopen(ViewManager viewManager) {
        open(viewManager);
    }

    @Override
    public void close(ViewManager viewManager) {

    }
}
