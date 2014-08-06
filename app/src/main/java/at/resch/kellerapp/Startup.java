package at.resch.kellerapp;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Properties;

import at.resch.kellerapp.logging.Log;
import at.resch.kellerapp.logging.LogcatLogger;
import at.resch.kellerapp.logging.WidgetLogger;
import at.resch.kellerapp.model.SQLServer;
import at.resch.kellerapp.user.CardHandler;
import at.resch.kellerapp.view.ViewManager;


public class Startup extends Activity {

    private TextView log;
    private Log logging;
    private NfcAdapter nfcAdapter;

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
        logging.i("Preparing NFC Capabilities");
        logging.d("Creating CardHandler");
        new CardHandler().block();
        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        if (nfcAdapter == null) {
            logging.e("NFC is not working on this device! NFC Payment won't work!");
        } else {
            if (!nfcAdapter.isEnabled()) {
                logging.w("NFC not enabled please enable it!");
            }
            handleIntent(getIntent());
        }
    }

    private void handleIntent(Intent intent) {
        String action = intent.getAction();
        if (NfcAdapter.ACTION_TECH_DISCOVERED.equals(action)) {
            Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            byte[] id = tag.getId();
            StringBuffer result = new StringBuffer();
            for (byte b : id) {
                result.append(String.format("%02X", b));
            }
            android.util.Log.d("kellerapp-log", "Detected card: " + result.toString());
            CardHandler.get().handle(result.toString());
        }
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

    @Override
    protected void onResume() {
        super.onResume();

        setupForegroundDispatch(this, nfcAdapter);
    }

    private void setupForegroundDispatch(Activity activity, NfcAdapter nfcAdapter) {
        final Intent intent = new Intent(activity.getApplicationContext(), activity.getClass());
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

        final PendingIntent pendingIntent = PendingIntent.getActivity(activity.getApplicationContext(), 0, intent, 0);

        IntentFilter[] filters = new IntentFilter[1];
        String[][] techList = new String[][]{new String[]{"android.nfc.tech.NfcA", "android.nfc.tech.NdefFormatable", "android.nfc.tech.MifareClassic"}};

        filters[0] = new IntentFilter();
        filters[0].addAction(NfcAdapter.ACTION_TECH_DISCOVERED);
        filters[0].addCategory(Intent.CATEGORY_DEFAULT);

        nfcAdapter.enableForegroundDispatch(activity, pendingIntent, filters, techList);
    }

    @Override
    protected void onPause() {

        stopForegroundDispatch(this, nfcAdapter);

        super.onPause();
    }

    private void stopForegroundDispatch(Activity activity, NfcAdapter nfcAdapter) {
        nfcAdapter.disableForegroundDispatch(activity);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        handleIntent(intent);
    }
}
