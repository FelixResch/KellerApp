package at.resch.kellerapp.logging;

/**
 * Created by felix on 8/4/14.
 */
public interface Logger {

    public abstract void append(Log.Level level, String message);
}
