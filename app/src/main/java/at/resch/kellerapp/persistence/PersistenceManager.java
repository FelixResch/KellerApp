package at.resch.kellerapp.persistence;

import java.lang.reflect.*;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;

import at.resch.kellerapp.model.Model;
import at.resch.kellerapp.model.SQLServer;

/**
 * Created by felix on 8/4/14.
 */
public class PersistenceManager {

    public static Model load(String mysqlHost, String mysqlPort, String mysqlUser, String mysqlPass, SQLServer server) {
        String mysqlDatabase = Model.class.getAnnotation(Database.class).value();
        server.log("Connecting to " + mysqlHost + ":" + mysqlPort + " for Database " + mysqlDatabase + " as User " + mysqlUser);
        //TODO JDBC thingy
        Connection db = null;
        Model m = new Model();
        for(java.lang.reflect.Field f : Model.class.getDeclaredFields()) {
            f.setAccessible(true);
            if(!f.isAnnotationPresent(Table.class))
                continue;
            try {
                Collection instance = (Collection) f.getType().newInstance();
                String table = f.getAnnotation(Table.class).value();
                Statement statement = db.createStatement();
                ResultSet rs = statement.executeQuery("select * from " + table);
                rs.first();
                do {
                    Object i = f.getType().getComponentType().newInstance();
                    for(Field f_ : i.getClass().getDeclaredFields()) {
                        f_.setAccessible(true);
                        if(f_.isAnnotationPresent(at.resch.kellerapp.persistence.Field.class)) {
                            String field = f_.getAnnotation(at.resch.kellerapp.persistence.Field.class).value();
                            if(f_.getType() == String.class) {
                                f_.set(i, rs.getString(field));
                            } else if (f_.getType() == double.class) {
                                f_.setDouble(i, rs.getDouble(field));
                            } else if (f_.getType() == int.class) {
                                f_.setInt(i, rs.getInt(field));
                            }
                        }
                    }
                    instance.add(i);
                } while(rs.next());
                f.set(m, instance);
            } catch (InstantiationException e) {
                server.log(e.getMessage());
            } catch (IllegalAccessException e) {
                server.log(e.getMessage());
            } catch (SQLException e) {
                server.log(e.getMessage());
            } catch (Exception e) {
                server.log(e.getMessage());
            }
            try {
                f.set(m, f.getType().newInstance());
            } catch (IllegalAccessException e) {
                server.log("Fatal persistence Error!");
            } catch (InstantiationException e) {
                server.log("Fatal persistence Error!");
            }
        }
        return m;
    }
}
