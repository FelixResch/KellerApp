package at.resch.kellerapp.persistence;

import android.os.AsyncTask;
import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by felix on 8/8/14.
 */
public class QueryExecutor extends AsyncTask<Query, String, Query[]> {

    private static String host;
    private static String port;
    private static String database;
    private static String user;
    private static String pass;

    private static Connection connection;

    public static void openConnection(String host, String port, String database, String user, String pass) {
        QueryExecutor.host = host;
        QueryExecutor.database = database;
        QueryExecutor.pass = pass;
        QueryExecutor.port = port;
        QueryExecutor.user = user;
        establishConnection();
    }

    protected static void openConnection() {
        if (connection == null) {
            establishConnection();
        } else {
            try {
                Statement statement = connection.createStatement();
                statement.executeQuery("SELECT 1");
                statement.close();
            } catch (SQLException e) {
                establishConnection();
            }

        }
    }

    private static void establishConnection() {
        try {
            connection = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database, user, pass);
        } catch (SQLException e) {
            Log.e("kellerapp-log", "Can't connect to MySQL Database", e);
        }
    }

    @Override
    protected Query[] doInBackground(Query... queries) {
        openConnection();
        for (int i = 0; i < queries.length; i++) {
            publishProgress("Attempting to execute query " + queries[i].getQuery());
            try {
                Statement statement = connection.createStatement();
                String query_text = queries[i].getQuery().toLowerCase();
                if (query_text.startsWith("select")) {
                    queries[i].setResultSet(statement.executeQuery(queries[i].getQuery()));
                } else {
                    statement.execute(queries[i].getQuery());
                }
                statement.close();
                publishProgress("Succesful");
            } catch (SQLException e) {
                Log.e("kellerapp-log", "Error while executing query against Database (" + queries[i].getQuery() + ")", e);
                publishProgress("Failed");
            } catch (Exception e) {
                Log.e("kellerapp-log", "Error while executing query against Database", e);
                publishProgress("Failed");
            }
        }
        return queries;
    }

    @Override
    protected void onPostExecute(Query[] queries) {
        for (Query q : queries) {
            if (q.getListener() != null)
                q.getListener().executionFinished(q.getResultSet());
        }
    }

    @Override
    protected void onProgressUpdate(String... values) {
        for (String s : values) {
            at.resch.kellerapp.logging.Log.get().d(s);
        }
    }
}
