package at.resch.kellerapp.persistence;

import java.sql.ResultSet;

/**
 * Created by felix on 8/8/14.
 */
public interface QueryExecutedListener {

    public abstract void executionFinished(QueryResult result);

    //public abstract void executionFailed(SQLException e, String query);
}
