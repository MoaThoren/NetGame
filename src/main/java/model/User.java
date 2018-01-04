package model;

import common.UserDTO;

import javax.persistence.*;
import java.io.Serializable;

@NamedQueries({
        @NamedQuery(
                name = "deleteUser",
                query = "DELETE FROM users user " +
                        "WHERE user.username LIKE :username " +
                        "AND user.password LIKE :password"
        )
        ,
        @NamedQuery(
                name = "checkUser",
                query = "SELECT user FROM users user " +
                        "WHERE user.username LIKE :username",
                lockMode = LockModeType.OPTIMISTIC
        )
        ,
        @NamedQuery(
                name = "loginUser",
                query = "SELECT user FROM users user " +
                        "WHERE user.username LIKE :username " +
                        "AND user.password LIKE :password",
                lockMode = LockModeType.OPTIMISTIC
        )
})

@Entity(name = "users")
public class User implements UserDTO, Serializable {
    @Id
    @Column(name = "username", nullable = false)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

    @Version
    @Column(name = "version")
    private int versionNum;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public User() {
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    /**
     * @return A string representation of all fields in this object.
     */
    @Override
    public String toString() {
        return "User: " + username;
    }
}
