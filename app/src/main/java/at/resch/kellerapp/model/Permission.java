package at.resch.kellerapp.model;

import at.resch.kellerapp.persistence.Field;
import at.resch.kellerapp.persistence.ForeignKey;
import at.resch.kellerapp.persistence.PrimaryKey;

/**
 * Created by felix on 8/4/14.
 */
public class Permission {

    @Field("p_u_user")
    @PrimaryKey
    @ForeignKey(table = "u_user", field = "u_id")
    @Id
    private int user;

    @Field("p_permission")
    @PrimaryKey
    @Id
    private String permission;

    public int getUser() {
        return user;
    }

    public void setUser(int user) {
        this.user = user;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }
}
