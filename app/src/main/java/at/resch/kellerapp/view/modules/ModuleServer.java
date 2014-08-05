package at.resch.kellerapp.view.modules;

import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.Properties;

import at.resch.kellerapp.R;
import at.resch.kellerapp.user.RequiresPermission;
import at.resch.kellerapp.view.Module;
import at.resch.kellerapp.view.ViewManager;

/**
 * Created by felix on 8/4/14.
 */
@RequiresPermission("PERMISSION_VIEW")
public class ModuleServer implements Module {

    @Override
    public String getName() {
        return "Server";
    }

    @Override
    public String[] getPermissions() {
        return new String[] {"PERMISSION_DEVELOPER"};
    }

    @Override
    public void open(final ViewManager viewManager) {
        viewManager.getActivity().setContentView(R.layout.server_layout);
        final Properties properties = new Properties();
        try {
            properties.load(viewManager.getActivity().openFileInput("server.properties"));
            ((EditText)viewManager.getActivity().findViewById(R.id.host)).setText(properties.getProperty("server.ip"));
            ((EditText)viewManager.getActivity().findViewById(R.id.port)).setText(properties.getProperty("server.port"));
            ((EditText)viewManager.getActivity().findViewById(R.id.user)).setText(properties.getProperty("connection.user"));
            ((EditText)viewManager.getActivity().findViewById(R.id.password)).setText(properties.getProperty("connection.pass"));
        } catch (IOException e) {
        }
        ((Button)viewManager.getActivity().findViewById(R.id.abbrechen)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ViewManager.get().closeView();
            }
        });
        ((Button)viewManager.getActivity().findViewById(R.id.speichern)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                properties.setProperty("server.ip", ((EditText)viewManager.getActivity().findViewById(R.id.host)).getText().toString());
                properties.setProperty("server.port", ((EditText)viewManager.getActivity().findViewById(R.id.port)).getText().toString());
                properties.setProperty("connection.user", ((EditText)viewManager.getActivity().findViewById(R.id.user)).getText().toString());
                properties.setProperty("connection.pass", ((EditText)viewManager.getActivity().findViewById(R.id.password)).getText().toString());
                try {
                    properties.store(viewManager.getActivity().openFileOutput("server.properties", Activity.MODE_PRIVATE), "Mysql Server Properties");
                } catch (IOException e) {}
                viewManager.closeView();
            }
        });
    }

    @Override
    public void reopen(ViewManager viewManager) {

    }

    @Override
    public void close(ViewManager viewManager) {

    }
}
