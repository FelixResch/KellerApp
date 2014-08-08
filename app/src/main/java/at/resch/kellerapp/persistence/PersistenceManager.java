package at.resch.kellerapp.persistence;

import android.content.Context;
import android.util.Log;

import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import at.resch.kellerapp.model.Model;
import at.resch.kellerapp.model.SQLServer;
import at.resch.kellerapp.view.ViewManager;

/**
 * Created by felix on 8/4/14.
 */
public class PersistenceManager {

    private static QueryExecutor executor;

    public static Model load(String mysqlHost, String mysqlPort, String mysqlUser, String mysqlPass, SQLServer server) {
        String mysqlDatabase = Model.class.getAnnotation(Database.class).value();
        server.log("Connecting to " + mysqlHost + ":" + mysqlPort + " for Database " + mysqlDatabase + " as User " + mysqlUser);
        Connection db = null;
        try {
            db = DriverManager.getConnection("jdbc:mysql://" + mysqlHost + ":" + mysqlPort + "/" + mysqlDatabase, mysqlUser, mysqlPass);
        } catch (SQLException e) {
            server.log("Couldn't connect to MySQL Server. Please check properties and restart app!");
        }
        QueryExecutor.openConnection(mysqlHost, mysqlPort, mysqlDatabase, mysqlUser, mysqlPass);
        Model m = new Model();
        for (java.lang.reflect.Field f : Model.class.getDeclaredFields()) {
            f.setAccessible(true);
            if (!f.isAnnotationPresent(Table.class))
                continue;
            try {
                Collection instance = (Collection) f.getType().newInstance();
                String table = f.getAnnotation(Table.class).value();
                Statement statement = db.createStatement();
                ResultSet rs = statement.executeQuery("select * from " + table);
                if (rs.first()) {
                    do {
                        Type generic = f.getGenericType();
                        ParameterizedType parameterizedType = (ParameterizedType) generic;
                        Type[] actualTypes = parameterizedType.getActualTypeArguments();
                        Type type = actualTypes[0];
                        Class<?> clazz = (Class<?>) type;
                        Object i = clazz.newInstance();
                        //Object i = ((Class)((ParameterizedType)f.getGenericType()).getActualTypeArguments()[0]).newInstance();
                        for (Field f_ : i.getClass().getDeclaredFields()) {
                            f_.setAccessible(true);
                            if (f_.isAnnotationPresent(at.resch.kellerapp.persistence.Field.class)) {
                                String field = f_.getAnnotation(at.resch.kellerapp.persistence.Field.class).value();
                                if (f_.getType() == String.class) {
                                    f_.set(i, rs.getString(field));
                                } else if (f_.getType() == double.class) {
                                    f_.setDouble(i, rs.getDouble(field));
                                } else if (f_.getType() == int.class) {
                                    f_.setInt(i, rs.getInt(field));
                                }
                            }
                        }
                        instance.add(i);
                    } while (rs.next());
                }
                f.set(m, instance);
            } catch (InstantiationException e) {
                server.log(e.getMessage());
                Log.e("kellerapp-persistence", "Persistence Error", e);
            } catch (IllegalAccessException e) {
                server.log(e.getMessage());
                Log.e("kellerapp-persistence", "Persistence Error", e);
            } catch (SQLException e) {
                server.log(e.getMessage());
                Log.e("kellerapp-persistence", "Persistence Error", e);
            } catch (Exception e) {
                server.log(e.getMessage());
                Log.e("kellerapp-persistence", "Persistence Error", e);
            }
            try {
                if (f.get(m) == null)
                    f.set(m, f.getType().newInstance());
            } catch (IllegalAccessException e) {
                server.log("Fatal persistence Error!");
            } catch (InstantiationException e) {
                server.log("Fatal persistence Error!");
            }
        }
        createSQLScript(Model.class);
        return m;
    }

    public static void createSQLScript(Class<?> clazz) {
        try {
            PrintStream ps = new PrintStream(ViewManager.get().getActivity().openFileOutput("create_db.sql", Context.MODE_PRIVATE));
            if (clazz.isAnnotationPresent(Database.class)) {
                String db = clazz.getAnnotation(Database.class).value();
                ps.println("DROP DATABASE IF EXISTS " + db + ";");
                ps.println("CREATE DATABASE " + db + ";");
                ps.println("USE " + db + ";");
                ps.println();
                ArrayList<String> foreignKeys = new ArrayList<String>();
                for (Field f : clazz.getDeclaredFields()) {
                    f.setAccessible(true);
                    if (f.isAnnotationPresent(Table.class)) {
                        String t = f.getAnnotation(Table.class).value();
                        ArrayList<String> primaryKeys = new ArrayList<String>();
                        ps.print("CREATE TABLE " + t + "(");
                        for (Field f_ : ((Class<?>) ((ParameterizedType) f.getGenericType()).getActualTypeArguments()[0]).getDeclaredFields()) {
                            f_.setAccessible(true);
                            if (f_.isAnnotationPresent(at.resch.kellerapp.persistence.Field.class)) {
                                String n = f_.getAnnotation(at.resch.kellerapp.persistence.Field.class).value();
                                if (f_.getType() == String.class) {
                                    ps.print(n + " VARCHAR(255),");
                                } else if (f_.getType() == int.class) {
                                    ps.print(n + " INTEGER ");
                                    if (f_.isAnnotationPresent(AutoIncrement.class)) {
                                        ps.print(" AUTO_INCREMENT");
                                    }
                                    ps.print(", ");
                                } else if (f_.getType() == double.class) {
                                    ps.print(n + " REAL ");
                                    if (f_.isAnnotationPresent(AutoIncrement.class)) {
                                        ps.print(" AUTO_INCREMENT");
                                    }
                                    ps.print(", ");
                                } else if (f_.getType() == Date.class) {
                                    ps.print(n + " DATE, ");
                                }
                                if (f_.isAnnotationPresent(PrimaryKey.class)) {
                                    primaryKeys.add(n);
                                }
                                if (f_.isAnnotationPresent(ForeignKey.class)) {
                                    ForeignKey fk = f_.getAnnotation(ForeignKey.class);
                                    foreignKeys.add("ALTER TABLE " + t + " ADD FOREIGN KEY (" + n + ") REFERENCES " + fk.table() + "(" + fk.field() + ") ON DELETE CASCADE;");
                                }
                            }
                        }
                        ps.print("PRIMARY KEY (");
                        boolean first = true;
                        for (String n : primaryKeys) {
                            if (first)
                                first = false;
                            else
                                ps.print(", ");
                            ps.print(n);
                        }
                        ps.println("));");

                    }
                }
                ps.println();
                for (String fk : foreignKeys) {
                    ps.println(fk);
                }
                ps.println();
                ps.close();
            } else {
                ps.close();
            }
        } catch (FileNotFoundException e) {
            Log.e("kellerapp-log", e.getMessage());
        } catch (Exception e) {
            Log.e("kellerapp-log", e.getMessage(), e);
        }
    }

    public static void insert(String table, Object obj) {
        insert(table, obj, false);
    }


    public static void insert(String table, Object obj, boolean overwrite) {
        Class<?> clazz = obj.getClass();
        String query = "INSERT INTO " + table + " SET ";
        for (Field f : clazz.getDeclaredFields()) {
            f.setAccessible(true);
            if (f.isAnnotationPresent(at.resch.kellerapp.persistence.Field.class) && !f.isAnnotationPresent(AutoIncrement.class) || overwrite) {
                query += f.getAnnotation(at.resch.kellerapp.persistence.Field.class).value();
                if (f.getType() == String.class) {
                    try {
                        if (f.get(obj) == null) {
                            query += " = NULL,";
                        } else {
                            query += " = '" + f.get(obj) + "',";
                        }
                    } catch (IllegalAccessException e) {
                        Log.e("kellerapp-log", e.getMessage(), e);
                    }
                } else if (f.getType() == Date.class) {
                    try {
                        if (f.get(obj) == null) {
                            query += " = NULL,";
                        } else {
                            query += " = '" + new java.sql.Date(((Date) f.get(obj)).getTime()).toString() + "',";
                        }
                    } catch (IllegalAccessException e) {
                        Log.e("kellerapp-log", e.getMessage(), e);
                    }
                } else if (f.getType() == int.class || f.getType() == double.class) {
                    try {
                        query += " = " + f.get(obj) + ",";
                    } catch (IllegalAccessException e) {
                        Log.e("kellerapp-log", e.getMessage(), e);
                    }
                }
            }
        }
        query = query.substring(0, query.length() - 1) + ";";
        executeQuery(query);
    }

    public static void update(String table, Object obj) {
        Class<?> clazz = obj.getClass();
        String query = "UPDATE " + table + " SET ";
        for (Field f : clazz.getDeclaredFields()) {
            f.setAccessible(true);
            if (f.isAnnotationPresent(at.resch.kellerapp.persistence.Field.class) && !f.isAnnotationPresent(PrimaryKey.class)) {
                query += f.getAnnotation(at.resch.kellerapp.persistence.Field.class).value();
                if (f.getType() == String.class) {
                    try {
                        if (f.get(obj) == null) {
                            query += " = NULL,";
                        } else {
                            query += " = '" + f.get(obj) + "',";
                        }
                    } catch (IllegalAccessException e) {
                        Log.e("kellerapp-log", e.getMessage(), e);
                    }
                } else if (f.getType() == Date.class) {
                    try {
                        if (f.get(obj) == null) {
                            query += " = NULL,";
                        } else {
                            query += " = '" + new java.sql.Date(((Date) f.get(obj)).getTime()).toString() + "',";
                        }
                    } catch (IllegalAccessException e) {
                        Log.e("kellerapp-log", e.getMessage(), e);
                    }
                } else if (f.getType() == int.class || f.getType() == double.class) {
                    try {
                        query += " = " + f.get(obj) + ",";
                    } catch (IllegalAccessException e) {
                        Log.e("kellerapp-log", e.getMessage(), e);
                    }
                }
            }
        }
        query = query.substring(0, query.length() - 1);
        query += " WHERE ";
        for (Field f : clazz.getDeclaredFields()) {
            f.setAccessible(true);
            if (f.isAnnotationPresent(at.resch.kellerapp.persistence.Field.class) && f.isAnnotationPresent(PrimaryKey.class)) {
                query += f.getAnnotation(at.resch.kellerapp.persistence.Field.class).value();
                if (f.getType() == String.class) {
                    try {
                        if (f.get(obj) == null) {
                            query += " = NULL AND ";
                        } else {
                            query += " = '" + f.get(obj) + "' AND ";
                        }
                    } catch (IllegalAccessException e) {
                        Log.e("kellerapp-log", e.getMessage(), e);
                    }
                } else if (f.getType() == Date.class) {
                    try {
                        if (f.get(obj) == null) {
                            query += " = NULL AND";
                        } else {
                            query += " = '" + new java.sql.Date(((Date) f.get(obj)).getTime()).toString() + "' AND ";
                        }
                    } catch (IllegalAccessException e) {
                        Log.e("kellerapp-log", e.getMessage(), e);
                    }
                } else if (f.getType() == int.class || f.getType() == double.class) {
                    try {
                        query += " = " + f.get(obj) + " AND ";
                    } catch (IllegalAccessException e) {
                        Log.e("kellerapp-log", e.getMessage(), e);
                    }
                }
            }
        }
        query = query.substring(0, query.length() - 4) + ";";
        executeQuery(query);
    }

    public static void delete(String table, Object obj) {
        Class<?> clazz = obj.getClass();
        String query = "DELETE FROM " + table + " WHERE ";
        for (Field f : clazz.getDeclaredFields()) {
            f.setAccessible(true);
            if (f.isAnnotationPresent(at.resch.kellerapp.persistence.Field.class) && f.isAnnotationPresent(PrimaryKey.class)) {
                query += f.getAnnotation(at.resch.kellerapp.persistence.Field.class).value();
                if (f.getType() == String.class) {
                    try {
                        if (f.get(obj) == null) {
                            query += " = NULL AND ";
                        } else {
                            query += " = '" + f.get(obj) + "' AND ";
                        }
                    } catch (IllegalAccessException e) {
                        Log.e("kellerapp-log", e.getMessage(), e);
                    }
                } else if (f.getType() == Date.class) {
                    try {
                        if (f.get(obj) == null) {
                            query += " = NULL AND ";
                        } else {
                            query += " = '" + new java.sql.Date(((Date) f.get(obj)).getTime()).toString() + "' AND ";
                        }
                    } catch (IllegalAccessException e) {
                        Log.e("kellerapp-log", e.getMessage(), e);
                    }
                } else if (f.getType() == int.class || f.getType() == double.class) {
                    try {
                        query += " = " + f.get(obj) + " AND ";
                    } catch (IllegalAccessException e) {
                        Log.e("kellerapp-log", e.getMessage(), e);
                    }
                }
            }
        }
        query = query.substring(0, query.length() - 4) + ";";
        executeQuery(query);
    }

    private static void executeQuery(String query) {
        executor = new QueryExecutor();
        executor.execute(new Query(query, null));
    }
}
