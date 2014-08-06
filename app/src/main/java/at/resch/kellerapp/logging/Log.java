package at.resch.kellerapp.logging;

import java.util.ArrayList;

/**
 * Created by felix on 8/4/14.
 */
public class Log {

    private static Log instance;

    public static Log get() {
        return instance;
    }

    public enum Level {
        DEBUG, INFO, WARNING, ERROR, FATAL
    }

    private ArrayList<Logger> loggers;

    public Log() {
        loggers = new ArrayList<Logger>();
        instance = this;
    }

    public void addLogger(Logger logger) {
        loggers.add(logger);
    }

    public void appendMessage(Level level, String message) {
        if (message == null)
            message = "null";
        for (Logger l : loggers) {
            l.append(level, message);
        }
    }

    public void debug(String message) {
        appendMessage(Level.DEBUG, message);
    }

    public void d(String message) {
        debug(message);
    }

    public void info(String message) {
        appendMessage(Level.INFO, message);
    }

    public void log(String message) {
        info(message);
    }

    public void i(String message) {
        info(message);
    }

    public void l(String message) {
        log(message);
    }

    public void warn(String message) {
        appendMessage(Level.WARNING, message);
    }

    public void w(String message) {
        warn(message);
    }

    public void error(String message) {
        appendMessage(Level.ERROR, message);
    }

    public void e(String message) {
        error(message);
    }

    public void fatal(String message) {
        appendMessage(Level.FATAL, message);
    }

    public void f(String message) {
        fatal(message);
    }
}
