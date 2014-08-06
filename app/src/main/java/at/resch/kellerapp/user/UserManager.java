package at.resch.kellerapp.user;

import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

import at.resch.kellerapp.model.Card;
import at.resch.kellerapp.model.Identity;
import at.resch.kellerapp.model.Model;
import at.resch.kellerapp.model.Permission;
import at.resch.kellerapp.model.User;
import at.resch.kellerapp.view.ViewManager;

/**
 * Created by felix on 8/4/14.
 */
public class UserManager implements CardListener {

    private HashMap<String, User> identities;
    private ArrayList<String> userPermissions;

    private User currentUser;
    private Model model;

    public UserManager(Model model) {
        model.setUserManager(this);
        this.model = model;
        reload();
    }

    public boolean authenticate(String id) {
        if (!identities.containsKey(id))
            return false;
        ArrayList<String> userPermissions = new ArrayList<String>();
        for (Permission p : model.getPermissions()) {
            if (p.getUser() == identities.get(id).getId()) {
                userPermissions.add(p.getPermission());
            }
        }
        if (userPermissions.contains("PERMISSION_APP") || userPermissions.contains("PERMISSION_ALL") || userPermissions.contains("DEVELOPER")) {
            this.userPermissions = userPermissions;
            currentUser = identities.get(id);
            return true;
        }
        return false;
    }

    public boolean authorize(String permission) {
        if (currentUser == null)
            return false;
        if (userPermissions.contains("PERMISSION_ALL") || userPermissions.contains("DEVELOPER"))
            return true;
        if (userPermissions.contains(permission))
            return true;
        return false;
    }


    public User getCurrentUser() {
        return currentUser;
    }

    @Override
    public void onCardDetected(String card) {
        if (authenticate(card)) {
            ViewManager.get().reopenView();
            Toast.makeText(ViewManager.get().getActivity(), "Willkommen " + currentUser.getName(), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(ViewManager.get().getActivity(), "Unbekannte ID " + card, Toast.LENGTH_SHORT).show();
        }
    }

    public void reload() {
        identities = new HashMap<String, User>();
        for (Identity i : model.getIdentities()) {
            User u = null;
            for (User u_ : model.getUser()) {
                if (u_.getId() == i.getUser()) {
                    u = u_;
                }
            }
            String card = null;
            for (Card c : model.getCards()) {
                if (c.getId().equals(i.getCard()))
                    card = c.getId();
            }
            identities.put(card, u);
        }
    }
}
