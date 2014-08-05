package at.resch.kellerapp;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Properties;

import at.resch.kellerapp.at.resch.kellerapp.logging.Log;
import at.resch.kellerapp.at.resch.kellerapp.logging.LogcatLogger;
import at.resch.kellerapp.at.resch.kellerapp.logging.WidgetLogger;
import at.resch.kellerapp.model.SQLServer;
import at.resch.kellerapp.view.ViewManager;

import static android.content.Context.*;


public class Startup extends Activity {

    private TextView log;
    private Log logging;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);


        setContentView(R.layout.activity_startup);
        logging = new Log();
        logging.addLogger(new LogcatLogger());
        logging.d("Succesfully added Logcat logger");
        logging.d("Creating ViewManager");
        new ViewManager(this, logging);
        logging.d("Fetching log TextView");
        log = (TextView) findViewById(R.id.log);
        logging.i("Adding TextView Logger");
        logging.addLogger(new WidgetLogger(log));
        logging.d("Reading Properties");
        Properties properties = new Properties();
        try {
            properties.load(this.openFileInput("server.properties"));
            throw new IOException("bla bla");
        } catch (IOException e) {
            logging.e(e.getMessage());
            properties.put("server.name", "Overlord");
            properties.put("server.ip", "192.168.0.10");
            properties.put("server.port", "3306");
            properties.put("connection.driver", "com.mysql.jdbc.Driver");
            properties.put("connection.user", "root");
            properties.put("connection.pass", "");
            try {
                properties.store(new OutputStreamWriter(this.openFileOutput("server.properties", MODE_PRIVATE)), "Standard Settings");
            } catch (IOException e1) {
                logging.f(e.getMessage());
                finish();
                return;
            }
        }
        logging.i("Connecting to " + properties.getProperty("server.name") + " Logging Services");
        SQLServer server = new SQLServer(logging);
        server.execute(properties);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.startup, menu);
        return true;
    }

    @Override
    public void onBackPressed() {
        ViewManager.get().closeView();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
