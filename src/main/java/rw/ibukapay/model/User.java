package rw.ibukapay.model;

import javax.faces.bean.ManagedBean;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * The Class User - application account holder.
 * Attributes: userId, userName, passwordHash
 * All accounts share the same USER scope (no privileged accounts).
 *
 * @version 1.0
 */
@ManagedBean
@Entity
@Table(name = "ibukapay_user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", nullable = false)
    private int userId;

    @Column(name = "user_name", nullable = false, unique = true, length = 20)
    @NotBlank(message = "User name is required")
    @Size(min = 3, max = 20, message = "User name must be between 3 and 20 characters")
    @Pattern(regexp = "[a-zA-Z0-9_]+", message = "User name may only contain letters, digits and underscore")
    private String userName;

    // stores a salted PBKDF2 hash (~100 chars); validated on the raw value at registration
    @Column(name = "password_hash", nullable = false, length = 255)
    @NotBlank(message = "Password is required")
    private String passwordHash;

    public User() {
    }

    public User(String userName, String passwordHash) {
        this.userName = userName;
        this.passwordHash = passwordHash;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }
}