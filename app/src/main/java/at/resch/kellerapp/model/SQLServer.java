package at.resch.kellerapp.model;

import android.os.AsyncTask;

import java.util.Properties;

import at.resch.kellerapp.at.resch.kellerapp.logging.Log;
import at.resch.kellerapp.persistence.PersistenceManager;
import at.resch.kellerapp.user.UserManager;
import at.resch.kellerapp.view.MainMenu;
import at.resch.kellerapp.view.ViewManager;

/**
 * Created by felix on 8/4/14.
 */
public class SQLServer extends AsyncTask<Properties, String, Model> {

    private Log log;

    public SQLServer(Log log) {
        this.log = log;
    }

    @Override
    protected Model doInBackground(Properties... properties) {
        publishProgress("This is going to take forever");
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
        }
        Model m =  PersistenceManager.load(properties[0].getProperty("server.ip"), properties[0].getProperty("server.port"),
                properties[0].getProperty("connection.user"), properties[0].getProperty("connection.pass"), this);
        log("Adding Dummy User @0");
        {
            User u = new User();
            u.setId(0);
            u.setName("Dummy User");
            m.getUser().add(u);
            Card c = new Card();
            c.setId("00000000");
            c.setType(0);
            m.getCards().add(c);
            Identity i = new Identity();
            i.setUser(0);
            i.setCard("00000000");
            m.getIdentities().add(i);
            Permission p = new Permission();
            p.setUser(0);
            //TODO Change back to PERMISSION_VIEW before release
            p.setPermission("DEVELOPER");
            m.getPermissions().add(p);
        }
        log("Adding Test Categories");
        {
            Category c1 = new Category();
            c1.setId(1);
            c1.setName("Alkoholfrei");
            m.getCategories().add(c1);
            Category c2 = new Category();
            c2.setId(2);
            c2.setName("Alkoholisch");
            m.getCategories().add(c2);
            Category c3 = new Category();
            c3.setId(3);
            c3.setName("Mischgetränke");
            m.getCategories().add(c3);
            Category c4 = new Category();
            c4.setId(4);
            c4.setName("Stamperl");
            m.getCategories().add(c4);
        }
        log("Creating User Manager");
        UserManager um = new UserManager(m);
        log("Logging in with Dummy user 0x00000000");
        um.authenticate("00000000");
        publishProgress("Finished");
        return m;
    }

    public void log(String msg) {
        publishProgress(msg);
    }

    @Override
    protected void onProgressUpdate(String... values) {
        for (String msg : values) {
            log.i(msg);
        }
    }

    @Override
    protected void onPostExecute(Model model) {
        model.setSettings(new Settings());
        ViewManager.get().openView(MainMenu.class);
    }
}