package at.resch.kellerapp.at.resch.kellerapp.logging;

/**
 * Created by felix on 8/4/14.
 */
public class LogcatLogger implements Logger {

    @Override
    public void append(Log.Level level, String message) {
        switch (level) {
            case DEBUG:
                android.util.Log.d("kellerapp-log", message);
                break;
            case INFO:
                android.util.Log.i("kellerapp-log", message);
                break;
            case WARNING:
                android.util.Log.w("kellerapp-log", message);
                break;
            case ERROR:
                android.util.Log.e("kellerapp-log", message);
                break;
            case FATAL:
                android.util.Log.wtf("kellerapp-log", message);
                break;
        }
    }
}
