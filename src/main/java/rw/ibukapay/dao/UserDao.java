package rw.ibukapay.dao;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import rw.ibukapay.model.User;

import java.util.List;

/**
 * The Class UserDao - CRUD operations for application accounts.
 *
 * @version 1.0
 */
public class UserDao {

    public void save(User user) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.save(user);
            transaction.commit();
        } catch (Exception ex) {
            ex.printStackTrace();
            if (transaction != null) {
                try {
                    transaction.rollback();
                } catch (Exception rb) {
                    rb.printStackTrace();
                }
            }
        }
    }

    public User findByUsername(String username) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<User> query = session.createQuery(
                    "from User where username = :username", User.class);
            query.setParameter("username", username);
            return query.uniqueResult();
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public List<User> findAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<User> query = session.createQuery("from User order by username", User.class);
            return query.list();
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public void delete(User user) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.delete(user);
            transaction.commit();
        } catch (Exception ex) {
            if (transaction != null) {
                transaction.rollback();
            }
            ex.printStackTrace();
        }
    }
}