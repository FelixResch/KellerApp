package at.resch.kellerapp.view.modules;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import at.resch.kellerapp.R;
import at.resch.kellerapp.model.Model;
import at.resch.kellerapp.model.MoneyResource;
import at.resch.kellerapp.user.RequiresPermission;
import at.resch.kellerapp.view.Module;
import at.resch.kellerapp.view.ViewManager;

/**
 * Created by felix on 8/4/14.
 */
@RequiresPermission("PERMISSION_VIEW")
public class ModuleFinanztools implements Module {
    @Override
    public String getName() {
        return "Finanztools";
    }

    @Override
    public String[] getPermissions() {
        return new String[]{"PERMISSION_ACCOUNTING"};
    }

    @Override
    public void open(ViewManager viewManager) {
        viewManager.getActivity().setContentView(R.layout.accounting_layout);
        ((TextView) viewManager.getActivity().findViewById(R.id.rote_kassa)).setText(String.format("%.2f €", Model.select(MoneyResource.class).where("mr_name_short", "kassa_rot").first().getAmount()));
        ((TextView) viewManager.getActivity().findViewById(R.id.weise_kassa)).setText(String.format("%.2f €", Model.select(MoneyResource.class).where("mr_name_short", "kassa_weis").first().getAmount()));
        ((TextView) viewManager.getActivity().findViewById(R.id.schwarze_kassa)).setText(String.format("%.2f €", Model.select(MoneyResource.class).where("mr_name_short", "kassa_schwarz").first().getAmount()));
        ((TextView) viewManager.getActivity().findViewById(R.id.keller_card)).setText(String.format("%.2f €", Model.select(MoneyResource.class).where("mr_name_short", "tmp_card").first().getAmount()));
        ((Button) viewManager.getActivity().findViewById(R.id.book)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ViewManager.get().openView(BuchungsView.class);
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
