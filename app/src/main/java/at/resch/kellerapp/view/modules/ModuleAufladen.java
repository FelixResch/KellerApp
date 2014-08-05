package at.resch.kellerapp.view.modules;

import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import at.resch.kellerapp.R;
import at.resch.kellerapp.model.Model;
import at.resch.kellerapp.view.MainMenu;
import at.resch.kellerapp.view.Module;
import at.resch.kellerapp.view.ViewManager;

/**
 * Created by felix on 8/4/14.
 */
public class ModuleAufladen implements Module {
    @Override
    public String getName() {
        return "Kellercard";
    }

    @Override
    public String[] getPermissions() {
        return new String[]{"PERMISSION_TRANSACTION", "PERMISSION_CARD"};
    }

    public static final String[] MODULES = new String[]{"at.resch.kellerapp.view.modules.ModuleNeu", "at.resch.kellerapp.view.modules.ModuleLaden", "at.resch.kellerapp.view.modules.ModuleEntfernen", "at.resch.kellerapp.view.modules.ModuleAuflosen"};

    @Override
    public void open(final ViewManager viewManager) {
        viewManager.getActivity().setContentView(R.layout.menu_layout);
        GridView gw = (GridView) viewManager.getActivity().findViewById(R.id.menu);
        final MainMenu.SquareAdapter adapter = new MainMenu.SquareAdapter(viewManager.getActivity());
        for (String s : MODULES) {
            try {
                Class<?> type = Class.forName(s);
                Module m = (Module) type.newInstance();
                boolean ok = true;
                for (String p : m.getPermissions()) {
                    if (!Model.get().getUserManager().authorize(p)) {
                        ok = false;
                        break;
                    }
                }
                if (ok)
                    adapter.add(m);
            } catch (ClassNotFoundException e) {
            } catch (InstantiationException e) {
            } catch (IllegalAccessException e) {
            }
        }
        gw.setAdapter(adapter);
        gw.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, android.view.View view, int i, long l) {
                Module m = adapter.getItem(i);
                boolean ok = true;
                for (String p : m.getPermissions()) {
                    if (!Model.get().getUserManager().authorize(p)) {
                        ok = false;
                        Toast.makeText(viewManager.getActivity(), "Permission '" + p + "' required for opening Module", Toast.LENGTH_SHORT).show();
                        break;
                    }
                }
                if (ok)
                    ViewManager.get().openView(m.getClass());
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
