package at.resch.kellerapp.model;

import android.app.Activity;

import java.io.IOException;
import java.util.Properties;

import at.resch.kellerapp.view.ViewManager;

/**
 * Created by felix on 8/4/14.
 */
public class Settings {

    private boolean partyMode;

    public boolean isPartyMode() {
        return partyMode;
    }

    public void setPartyMode(boolean partyMode) {
        this.partyMode = partyMode;
    }

    public Settings() {
        reload();
    }

    public void reload() {
        Properties properties = new Properties();
        try {
            properties.load(ViewManager.get().getActivity().openFileInput("application.properties"));
            partyMode = properties.getProperty("party.mode").equals("true");
        } catch (IOException e) {}
    }

    public void store() {
        Properties properties = new Properties();
        properties.setProperty("party.mode", "" + partyMode);
        try {
            properties.store(ViewManager.get().getActivity().openFileOutput("application.properties", Activity.MODE_PRIVATE), "Application Settings");
        } catch (IOException e) {}
    }
}
