package rw.ibukapay.bean;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import rw.ibukapay.dao.UserDao;
import rw.ibukapay.model.User;
import rw.ibukapay.util.PasswordUtil;

/**
 * The Class UserManagementBean - sign in, sign up and session management.
 * The system only knows a single USER scope; the least privilege guarantee
 * comes from enforcing registration + sign in before anything is reachable.
 *
 * @version 1.0
 */
@ManagedBean
@SessionScoped
public class UserManagementBean {

    private final UserDao userDao = new UserDao();

    private String username;
    private String password;
    private String confirmPassword;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public User getCurrentUser() {
        return (User) FacesContext.getCurrentInstance().getExternalContext()
                .getSessionMap().get("currentUser");
    }

    public boolean isLoggedIn() {
        return getCurrentUser() != null;
    }

    public String login() {
        User user = userDao.findByUsername(username);
        if (user != null && PasswordUtil.verify(password, user.getPassword())) {
            FacesContext.getCurrentInstance().getExternalContext()
                    .getSessionMap().put("currentUser", user);
            return "index";
        }
        addMessage("Invalid username or password.");
        return null;
    }

    public String register(User newUser) {
        if (userDao.findByUsername(newUser.getUsername()) != null) {
            addMessage("Username '" + newUser.getUsername() + "' is already taken.");
            return null;
        }
        if (newUser.getPassword() == null || newUser.getPassword().length() < 6) {
            addMessage("Password must be between 6 and 50 characters.");
            return null;
        }
        if (newUser.getPassword().length() > 50) {
            addMessage("Password must be between 6 and 50 characters.");
            return null;
        }
        if (confirmPassword == null || !confirmPassword.equals(newUser.getPassword())) {
            addMessage("Passwords do not match.");
            return null;
        }
        newUser.setPassword(PasswordUtil.hash(newUser.getPassword()));
        userDao.save(newUser);
        FacesContext.getCurrentInstance().getExternalContext()
                .getSessionMap().put("currentUser", newUser);
        addMessage("Account created. Welcome, " + newUser.getUsername() + "!");
        return "index";
    }

    private void addMessage(String message) {
        FacesContext.getCurrentInstance()
                .addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, message, null));
    }
}