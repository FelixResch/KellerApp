package at.resch.kellerapp.view.modules;

import android.view.View;
import android.widget.Button;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

import at.resch.kellerapp.R;
import at.resch.kellerapp.user.RequiresPermission;
import at.resch.kellerapp.view.Module;
import at.resch.kellerapp.view.ViewManager;

/**
 * Created by felix on 8/4/14.
 */
@RequiresPermission("PERMISSION_VIEW")
public class ModuleVerkauf implements Module {

    @Override
    public String getName() {
        return "Verkauf";
    }

    @Override
    public String[] getPermissions() {
        return new String[] {"PERMISSION_TRANSACTION", "PERMISSION_STOCK"};
    }

    @Override
    public void open(final ViewManager viewManager) {
        viewManager.getActivity().setContentView(R.layout.sell_layout);
        ((Button)viewManager.getActivity().findViewById(R.id.back)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewManager.closeView();
            }
        });
        {
            TabHost tabs = (TabHost) viewManager.getActivity().findViewById(R.id.tabHost);

            tabs.setup();

            TabSpec spec = tabs.newTabSpec("Test 1");
            spec.setContent(R.id.tab1);
            spec.setIndicator("Alkoholfrei");
            tabs.addTab(spec);
            spec = tabs.newTabSpec("Test 2");
            spec.setContent(R.id.tab2);
            spec.setIndicator("Alkoholisch");
            tabs.addTab(spec);
            spec = tabs.newTabSpec("Test 3");
            spec.setContent(R.id.tab3);
            spec.setIndicator("Mischgetränke");
            tabs.addTab(spec);
            spec = tabs.newTabSpec("Test 4");
            spec.setContent(R.id.tab4);
            spec.setIndicator("Stamperl");
            tabs.addTab(spec);
        }
    }

    @Override
    public void reopen(ViewManager viewManager) {
        open(viewManager);
    }

    @Override
    public void close(ViewManager viewManager) {

    }
}
