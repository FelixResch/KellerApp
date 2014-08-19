package at.resch.kellerapp.model;

import android.util.Log;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;

import at.resch.kellerapp.persistence.ForeignKey;
import at.resch.kellerapp.persistence.PrimaryKey;

/**
 * Created by felix on 8/19/14.
 */
public class Selection<T> extends ArrayList<T> {

    private Class<T> type;

    public Selection(ArrayList<T> list, Class<T> type) {
        this.addAll(list);
        this.type = type;
    }

    public Selection<T> where(String field, Object value) {
        Selection<T> ret = new Selection<T>(this, type);
        Iterator<T> iterator = ret.iterator();
        Class<T> clazz = type;
        Field select = null;
        for (Field f : clazz.getDeclaredFields()) {
            f.setAccessible(true);
            if (f.isAnnotationPresent(at.resch.kellerapp.persistence.Field.class)) {
                at.resch.kellerapp.persistence.Field f_ = f.getAnnotation(at.resch.kellerapp.persistence.Field.class);
                if (f_.value().equals(field)) {
                    select = f;
                    break;
                }
            }
        }
        if (select == null) {
            return ret;
        }
        while (iterator.hasNext()) {
            T obj = iterator.next();
            try {
                if (!select.get(obj).equals(value))
                    iterator.remove();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return ret;
    }

    public <J> Selection<T> join(Selection<J> join) {
        Selection<T> ret = new Selection<T>(this, type);
        Class<T> own = type;
        Class<J> foreign = join.type;
        if (join.isEmpty()) {
            ret.clear();
            return ret;
        }
        String table = Model.get().getTableName(foreign);
        Field fk = null;
        String ff = null;
        for (Field f : own.getDeclaredFields()) {
            f.setAccessible(true);
            if (f.isAnnotationPresent(at.resch.kellerapp.persistence.Field.class) && f.isAnnotationPresent(ForeignKey.class)) {
                ForeignKey fk_ = f.getAnnotation(ForeignKey.class);
                if (fk_.table().equals(table)) {
                    fk = f;
                    ff = fk_.field();
                    break;
                }
            }
        }
        if (fk == null) {
            ret.clear();
            return ret;
        }
        Field id = null;
        for (Field f : foreign.getDeclaredFields()) {
            f.setAccessible(true);
            if (f.isAnnotationPresent(at.resch.kellerapp.persistence.Field.class) && (f.isAnnotationPresent(Id.class) || f.isAnnotationPresent(PrimaryKey.class))) {
                at.resch.kellerapp.persistence.Field f_ = f.getAnnotation(at.resch.kellerapp.persistence.Field.class);
                if (f_.value().equals(ff)) {
                    id = f;
                    break;
                }

            }
        }
        Iterator<T> iterator = ret.iterator();
        while (iterator.hasNext()) {
            T o = iterator.next();
            boolean found = false;
            for (J f : join) {
                try {
                    found = found | (fk.get(o).equals(id.get(f)));
                } catch (IllegalAccessException e) {
                    Log.e("kellerapp-log", "Error while joining selections", e);
                }
            }
            if (!found)
                iterator.remove();
        }
        return ret;
    }

    public T first() {
        if (this.size() > 0) {
            return this.get(0);
        }
        return null;
    }

    public T last() {
        if (this.size() > 0) {
            return this.get(this.size() - 1);
        }
        return null;
    }
}
