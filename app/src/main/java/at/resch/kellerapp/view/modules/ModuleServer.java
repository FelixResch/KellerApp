package at.resch.kellerapp.view.modules;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.ResultSet;
import java.util.Properties;

import at.resch.kellerapp.R;
import at.resch.kellerapp.model.Model;
import at.resch.kellerapp.persistence.Query;
import at.resch.kellerapp.persistence.QueryExecutedListener;
import at.resch.kellerapp.persistence.QueryExecutor;
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
        return new String[]{"PERMISSION_DEVELOPER"};
    }

    @Override
    public void open(final ViewManager viewManager) {
        viewManager.getActivity().setContentView(R.layout.server_layout);
        final Properties properties = new Properties();
        try {
            properties.load(viewManager.getActivity().openFileInput("server.properties"));
            ((EditText) viewManager.getActivity().findViewById(R.id.host)).setText(properties.getProperty("server.ip"));
            ((EditText) viewManager.getActivity().findViewById(R.id.port)).setText(properties.getProperty("server.port"));
            ((EditText) viewManager.getActivity().findViewById(R.id.user)).setText(properties.getProperty("connection.user"));
            ((EditText) viewManager.getActivity().findViewById(R.id.password)).setText(properties.getProperty("connection.pass"));
        } catch (IOException e) {
        }
        ((Button) viewManager.getActivity().findViewById(R.id.abbrechen)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ViewManager.get().closeView();
            }
        });
        ((Button) viewManager.getActivity().findViewById(R.id.speichern)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                properties.setProperty("server.ip", ((EditText) viewManager.getActivity().findViewById(R.id.host)).getText().toString());
                properties.setProperty("server.port", ((EditText) viewManager.getActivity().findViewById(R.id.port)).getText().toString());
                properties.setProperty("connection.user", ((EditText) viewManager.getActivity().findViewById(R.id.user)).getText().toString());
                properties.setProperty("connection.pass", ((EditText) viewManager.getActivity().findViewById(R.id.password)).getText().toString());
                try {
                    properties.store(viewManager.getActivity().openFileOutput("server.properties", Activity.MODE_PRIVATE), "Mysql Server Properties");
                } catch (IOException e) {
                }
                viewManager.closeView();
            }
        });
        ((Button) viewManager.getActivity().findViewById(R.id.test)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {

                properties.setProperty("server.ip", ((EditText) viewManager.getActivity().findViewById(R.id.host)).getText().toString());
                properties.setProperty("server.port", ((EditText) viewManager.getActivity().findViewById(R.id.port)).getText().toString());
                properties.setProperty("connection.user", ((EditText) viewManager.getActivity().findViewById(R.id.user)).getText().toString());
                properties.setProperty("connection.pass", ((EditText) viewManager.getActivity().findViewById(R.id.password)).getText().toString());
                QueryExecutor.openConnection(properties.getProperty("server.ip"), properties.getProperty("server.port"), "", properties.getProperty("connection.user"), properties.getProperty("connection.pass"));
                QueryExecutor executor = new QueryExecutor();
                executor.execute(new Query("select 1", new QueryExecutedListener() {
                    @Override
                    public void executionFinished(ResultSet result) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(viewManager.getActivity());
                        builder.setTitle("Verbindung hergestellt");
                        builder.setMessage("Sie können das Gerät jetzt mit dem Server verbinden und auf die Datenbank zugreifen.");
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        });
                        builder.show();
                    }
                }));

            }
        });
        ((Button) viewManager.getActivity().findViewById(R.id.script)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(viewManager.getActivity());
                builder.setTitle("Sind Sie sich sicher?");
                builder.setMessage("Das ausführen dieses Skripts löscht alle Daten auf dem Server und setzt damit das ganze System zurück!\nWollen Sie wirklich fortfahren?");
                builder.setPositiveButton("Ja", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        properties.setProperty("server.ip", ((EditText) viewManager.getActivity().findViewById(R.id.host)).getText().toString());
                        properties.setProperty("server.port", ((EditText) viewManager.getActivity().findViewById(R.id.port)).getText().toString());
                        properties.setProperty("connection.user", ((EditText) viewManager.getActivity().findViewById(R.id.user)).getText().toString());
                        properties.setProperty("connection.pass", ((EditText) viewManager.getActivity().findViewById(R.id.password)).getText().toString());
                        QueryExecutor.openConnection(properties.getProperty("server.ip"), properties.getProperty("server.port"), "", properties.getProperty("connection.user"), properties.getProperty("connection.pass"));
                        QueryExecutor executor = new QueryExecutor();
                        String query = ((TextView) viewManager.getActivity().findViewById(R.id.sql)).getText().toString().replace("\n\n", "\n");
                        String[] q = query.split("\n");
                        Query[] queries = new Query[q.length];
                        for (int j = 0; j < q.length; j++) {
                            queries[j] = new Query(q[j], null);
                        }
                        queries[q.length - 1].setListener(new QueryExecutedListener() {
                            @Override
                            public void executionFinished(ResultSet result) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(viewManager.getActivity());
                                builder.setTitle("Skript ausgeführt");
                                builder.setMessage("Die Datenbank wurde erfolgreich eingerichtet. Sie können jetzt Daten hinzufügen und entfernen.\nVersuche Daten zu mirgrieren!");
                                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.dismiss();
                                    }
                                });
                                builder.show();
                                Model.get().migrate();
                            }
                        });
                        executor.execute(queries);
                    }
                });
                builder.setNegativeButton("Nein", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                builder.show();
            }
        });
        LoadSQLFile load = new LoadSQLFile();
        load.execute("create_db.sql");
    }

    @Override
    public void reopen(ViewManager viewManager) {
        open(viewManager);
    }

    @Override
    public void close(ViewManager viewManager) {

    }

    private class LoadSQLFile extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            try {
                BufferedReader din = new BufferedReader(new InputStreamReader(ViewManager.get().getActivity().openFileInput(strings[0])));
                String ret = "", line;
                while ((line = din.readLine()) != null) {
                    ret += line + "\n";
                }
                din.close();
                return ret;
            } catch (IOException e) {
                Log.e("kellerapp-log", e.getMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            ((TextView) ViewManager.get().getActivity().findViewById(R.id.sql)).setText(s);
        }
    }
}
