package at.resch.kellerapp.persistence;

import android.util.Log;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by felix on 8/17/14.
 */
public class QueryResult {

    private HashMap<String, Integer> keys;

    private ArrayList<Object[]> rows;

    private boolean empty = true;

    int curr_row = -1;

    public QueryResult (ResultSet resultSet) {
        try {
            createMapping(resultSet);
            readData(resultSet);
        } catch (SQLException e) {
            Log.e("kellerapp-log", "Error while initializing ResultSet Wrapper", e);
        }
    }

    private void createMapping(ResultSet resultSet) throws SQLException {
        keys = new HashMap<String, Integer>();
        ResultSetMetaData metaData = resultSet.getMetaData();
        for(int i = 1; i <= metaData.getColumnCount(); i++) {
            if(metaData.getColumnName(i) != null) {
                keys.put(metaData.getColumnName(i), i);
            } else if (metaData.getColumnLabel(i) != null) {
                keys.put(metaData.getColumnLabel(i), i);
            }
        }
    }

    private void readData(ResultSet resultSet) throws SQLException {
        rows = new ArrayList<Object[]>();
        int cols = resultSet.getMetaData().getColumnCount();
        if(!resultSet.first())
            return;
        empty = false;
        do {
            Object[] vals = new Object[cols];
            for(int i = 1; i <= cols; i++) {
                vals[i - 1] = resultSet.getObject(i);
            }
            rows.add(vals);
        } while( resultSet.next());
    }

    public <T> T get(Class<T> type, int column, int row) {
        return type.cast(rows.get(row)[column - 1]);
    }

    public <T> T get(Class<T> type, String column, int row) {
        return get(type, keys.get(column), row);
    }

    public <T> T get(Class<T> type, int column) {
        if(curr_row != -1)
            return  get(type, column, curr_row);
        return null;
    }

    public <T> T get(Class<T> type, String column) {
        if(curr_row != -1)
            return get(type, column, curr_row);
        return null;
    }

    public void first() {
        if(!empty)
            curr_row = 0;
    }

    public boolean next() {
        if(!empty && curr_row > -1)
            return (curr_row ++) == rows.size();
        return false;
    }
}
