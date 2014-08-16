package at.resch.kellerapp.view.modules;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Collection;
import java.util.Date;

import at.resch.kellerapp.R;
import at.resch.kellerapp.model.Card;
import at.resch.kellerapp.model.Identity;
import at.resch.kellerapp.model.Model;
import at.resch.kellerapp.model.User;
import at.resch.kellerapp.user.CardHandler;
import at.resch.kellerapp.user.CardListener;
import at.resch.kellerapp.view.Module;
import at.resch.kellerapp.view.ViewManager;

/**
 * Created by felix on 8/5/14.
 */
public class ModuleNeu implements Module {
    @Override
    public String getName() {
        return "Neue Karte";
    }

    @Override
    public String[] getPermissions() {
        return new String[]{"PERMISSION_TRANSACTION", "PERMISSION_CARD"};
    }

    @Override
    public void open(final ViewManager viewManager) {
        AlertDialog.Builder builder = new AlertDialog.Builder(viewManager.getActivity());
        builder.setTitle("Neue Karte");
        builder.setMessage("Ist der Benutzer bereits vorhanden?");
        builder.setPositiveButton("Ja", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                AlertDialog.Builder builder1 = new AlertDialog.Builder(viewManager.getActivity());
                builder1.setTitle("Benutzerwahl");
                builder1.setMessage("Bitte wähle aus, wie du den Nutzer auswählen willst");
                builder1.setPositiveButton("Alte Karte", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        viewManager.getActivity().setContentView(R.layout.nfc_wait);
                        CardHandler.get().addCardListener(new CardListener() {
                            @Override
                            public void onCardDetected(String card) {
                                User u = Model.get().getUser(card);
                                if (u != null) {
                                    CardHandler.get().removeCardListener();
                                    addUserCard(u);
                                } else {
                                    Toast.makeText(viewManager.getActivity(), "Unbekannte Karte " + card, Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                });
                builder1.setNegativeButton("Liste", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        Collection<User> users = Model.get().getUser();
                        final Dialog dialog = new Dialog(viewManager.getActivity());
                        dialog.setTitle("Benutzer Auswahl");
                        dialog.setContentView(R.layout.user_list);
                        UserAdapter ua = new UserAdapter(viewManager.getActivity());
                        for (User u : users) {
                            ua.add(u);
                        }
                        ListView lv = (ListView) dialog.findViewById(R.id.users);
                        lv.setAdapter(ua);
                        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                dialog.dismiss();
                                User u = (User) adapterView.getAdapter().getItem(i);
                                addUserCard(u);
                            }
                        });
                        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                            @Override
                            public void onCancel(DialogInterface dialogInterface) {
                                viewManager.closeView();
                            }
                        });
                        dialog.show();
                    }
                });
                builder1.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialogInterface) {
                        viewManager.closeView();
                    }
                });
                builder1.show();
            }
        });
        builder.setNegativeButton("Nein", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                final Dialog dialog = new Dialog(viewManager.getActivity());
                dialog.setTitle("Neuer Benutzer");
                dialog.setContentView(R.layout.new_user_dialog);
                final DatePicker birthday = (DatePicker) dialog.findViewById(R.id.birthday);
                birthday.init(1997, 1, 1, null);
                ((Button) dialog.findViewById(R.id.ok)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                        User u = new User();
                        u.setBirthday(new Date(birthday.getYear() - 1900, birthday.getMonth(), birthday.getDayOfMonth()));
                        u.setName(((EditText) dialog.findViewById(R.id.name)).getText().toString());
                        u.setEmail(((EditText) dialog.findViewById(R.id.email)).getText().toString());
                        u.setBalance(0.);
                        u.setTelephone(((EditText) dialog.findViewById(R.id.phone)).getText().toString());
                        Model.get().add(u);
                        addUserCard(u);
                    }
                });
                dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialogInterface) {
                        viewManager.closeView();
                    }
                });
                dialog.show();
            }
        });
        builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                viewManager.closeView();
            }
        });
        builder.show();
    }

    protected void addUserCard(final User u) {
        AlertDialog.Builder builder = new AlertDialog.Builder(ViewManager.get().getActivity());
        builder.setTitle("Neue Karte");
        builder.setMessage("Bitte die neue Karte an den Sensor halten");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                ViewManager.get().getActivity().setContentView(R.layout.nfc_wait);
                CardHandler.get().addCardListener(new CardListener() {
                    @Override
                    public void onCardDetected(String card) {
                        CardHandler.get().removeCardListener();
                        Card c = Model.get().getCard(card);
                        if (c != null) {
                            Toast.makeText(ViewManager.get().getActivity(), "Karte bereits in Verwendung", Toast.LENGTH_SHORT).show();
                        } else {
                            Card c_ = new Card();
                            c_.setId(card);
                            c_.setType(2);
                            Model.get().add(c_);
                            Identity i = new Identity();
                            i.setCard(card);
                            i.setUser(u.getId());
                            Model.get().add(i);
                            Toast.makeText(ViewManager.get().getActivity(), "Benutzer " + u.getName() + " mit Karte " + card + " verbunden", Toast.LENGTH_SHORT).show();
                            Model.get().getUserManager().reload();
                        }
                        ViewManager.get().closeView();
                    }
                });
            }
        });
        builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                ViewManager.get().closeView();
            }
        });
        builder.show();
    }

    public class UserAdapter extends ArrayAdapter<User> {

        public UserAdapter(Context context) {
            super(context, android.R.layout.simple_list_item_1);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View v = super.getView(position, convertView, parent);
            ((TextView) v).setText(getItem(position).getName());
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
