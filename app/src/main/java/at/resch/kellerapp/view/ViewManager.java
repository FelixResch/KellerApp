package at.resch.kellerapp.view;

import android.app.Activity;
import android.widget.Toast;

import java.util.Stack;

import at.resch.kellerapp.at.resch.kellerapp.logging.Log;
import at.resch.kellerapp.model.Model;
import at.resch.kellerapp.user.RequiresPermission;

/**
 * Created by felix on 8/4/14.
 */
public class ViewManager {

    private Activity activity;
    private Log log;
    private static ViewManager instance;
    private Stack<View> viewStack;

    public ViewManager(Activity activity, Log log) {
        this.activity = activity;
        this.log = log;
        this.viewStack = new Stack<View>();
        instance = this;
    }

    public static ViewManager get() {
        return instance;
    }

    public Activity getActivity() {
        return activity;
    }

    public void openView(Class<?> view) {
        try {
            View v = (View) view.newInstance();
            if(view.isAnnotationPresent(RequiresPermission.class)) {
                if(!Model.get().getUserManager().authorize(view.getAnnotation(RequiresPermission.class).value())) {
                    Toast.makeText(activity, "Permission " + view.getAnnotation(RequiresPermission.class).value() + " required for this Operation!", Toast.LENGTH_LONG).show();
                    return;
                }
            }
            if(viewStack.size() >= 1)
                viewStack.peek().close(this);
            viewStack.push(v);
            v.open(this);
        } catch (InstantiationException e) {
            log.e(e.getMessage());
        } catch (IllegalAccessException e) {
            log.e(e.getMessage());
        }
    }

    public void closeView() {
        if(viewStack.size() == 1) {
            activity.finish();
        } else {
            viewStack.pop().close(this);
            viewStack.peek().reopen(this);
        }
    }
}
