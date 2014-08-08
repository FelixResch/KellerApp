package at.resch.kellerapp.persistence;

import java.sql.ResultSet;

/**
 * Created by felix on 8/8/14.
 */
public class Query {

    private String query;
    private QueryExecutedListener listener;
    private ResultSet resultSet;

    public Query(String query, QueryExecutedListener listener) {
        this.query = query;
        this.listener = listener;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public QueryExecutedListener getListener() {
        return listener;
    }

    public void setListener(QueryExecutedListener listener) {
        this.listener = listener;
    }

    public ResultSet getResultSet() {
        return resultSet;
    }

    public void setResultSet(ResultSet resultSet) {
        this.resultSet = resultSet;
    }
}
