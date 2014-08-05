package at.resch.kellerapp.at.resch.kellerapp.logging;

import android.text.Html;
import android.widget.TextView;

/**
 * Created by felix on 8/4/14.
 */
public class WidgetLogger implements Logger {

    private TextView log;

    public WidgetLogger(TextView log) {
        this.log = log;
    }

    @Override
    public void append(Log.Level level, String message) {
        switch (level) {
            case DEBUG:
                log.append(Html.fromHtml("<font color=#aaaaaa>" + message + "</font>"));
                break;
            case INFO:
                log.append(Html.fromHtml("<font color=#ffffff>" + message + "</font>"));
                break;
            case WARNING:
                log.append(Html.fromHtml("<font color=#ffaa00>" + message + "</font>"));
                break;
            case ERROR:
                log.append(Html.fromHtml("<font color=#ff0000>" + message + "</font>"));
                break;
            case FATAL:
                log.append(Html.fromHtml("<font color=#aa0000>" + message + "</font>"));
                break;
        }
        log.append("\n");
    }

    public TextView getLog() {
        return log;
    }

    public void setLog(TextView log) {
        this.log = log;
    }
}
