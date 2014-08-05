package at.resch.kellerapp.view;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import at.resch.kellerapp.R;
import at.resch.kellerapp.model.Model;
import at.resch.kellerapp.user.RequiresPermission;

/**
 * Created by felix on 8/4/14.
 */
@RequiresPermission("PERMISSION_VIEW")
public class MainMenu implements View {

    public static final String[] MODULES = new String[] {"at.resch.kellerapp.view.modules.ModuleGuthaben", "at.resch.kellerapp.view.modules.ModuleVerkauf",
            "at.resch.kellerapp.view.modules.ModuleEinkauf", "at.resch.kellerapp.view.modules.ModuleAufladen", "at.resch.kellerapp.view.modules.ModuleFinanztools",
            "at.resch.kellerapp.view.modules.ModuleEinstellungen", "at.resch.kellerapp.view.modules.ModuleServer", "at.resch.kellerapp.view.modules.ModuleLogout"};

    @Override
    public void open(final ViewManager viewManager) {
        viewManager.getActivity().setContentView(R.layout.menu_layout);
        GridView gw = (GridView) viewManager.getActivity().findViewById(R.id.menu);
        final SquareAdapter adapter = new SquareAdapter(viewManager.getActivity());
        for(String s : MODULES) {
            try {
                Class<?> type = Class.forName(s);
                Module m = (Module) type.newInstance();
                boolean ok = true;
                for(String p : m.getPermissions()) {
                    if(!Model.get().getUserManager().authorize(p)) {
                        ok = false;
                        break;
                    }
                }
                if(ok)
                    adapter.add(m);
            } catch (ClassNotFoundException e) {} catch (InstantiationException e) {} catch (IllegalAccessException e) {}
        }
        gw.setAdapter(adapter);
        gw.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, android.view.View view, int i, long l) {
                Module m = adapter.getItem(i);
                boolean ok = true;
                for(String p : m.getPermissions()) {
                    if(!Model.get().getUserManager().authorize(p)) {
                        ok = false;
                        Toast.makeText(viewManager.getActivity(), "Permission '" + p + "' required for opening Module", Toast.LENGTH_SHORT).show();
                        break;
                    }
                }
                if(ok)
                    ViewManager.get().openView(m.getClass());
            }
        });
    }

    public static class SquareAdapter extends ArrayAdapter<Module> {

        public SquareAdapter(Context context) {
            super(context, R.layout.tile_layout);
        }

        @Override
        public android.view.View getView(int position, android.view.View convertView, ViewGroup parent) {
            android.view.View v = super.getView(position, convertView, parent);
            v.setMinimumHeight((parent.getHeight() - 12) / 2);
            ((TextView)v).setText(super.getItem(position).getName());
            return v;
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
