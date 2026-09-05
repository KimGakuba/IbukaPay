package rw.ibukapay.listener;

import rw.ibukapay.dao.UserDao;
import rw.ibukapay.model.User;
import rw.ibukapay.util.PasswordUtil;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

/**
 * The Class DataInitializer - seeds a default account on first startup so the
 * system is usable immediately: user/user123. All accounts share one USER scope.
 *
 * @version 1.0
 */
@WebListener
public class DataInitializer implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        try {
            UserDao userDao = new UserDao();
            if (userDao.findAll().isEmpty()) {
                userDao.save(new User("user", PasswordUtil.hash("user123")));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
    }
}