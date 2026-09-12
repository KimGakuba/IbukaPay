package rw.ibukapay.dao;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

/**
 * The Class HibernateUtil - follows the reference project (PatientManagementSystem V3):
 * the DAO layer creates a HibernateUtil instance and asks it for a SessionFactory.
 * The SessionFactory is built once and cached so it is not re-created on every call.
 *
 * @version 1.0
 */
public class HibernateUtil {

    // built once and reused for the whole application lifetime
    private static final SessionFactory SESSION_FACTORY = buildSessionFactory();

    private static SessionFactory buildSessionFactory() {
        Configuration configuration = new Configuration();
        configuration.configure();
        return configuration.buildSessionFactory();
    }

    public SessionFactory getSessionFactory() {
        return SESSION_FACTORY;
    }
}