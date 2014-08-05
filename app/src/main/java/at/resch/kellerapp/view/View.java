package at.resch.kellerapp.view;

/**
 * Created by felix on 8/4/14.
 */
public interface View {

    public abstract void open(ViewManager viewManager);
    public abstract void reopen(ViewManager viewManager);
    public abstract void close(ViewManager viewManager);
}
