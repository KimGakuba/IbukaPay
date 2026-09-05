package rw.ibukapay.dao;

import org.hibernate.Session;
import org.hibernate.Transaction;
import rw.ibukapay.model.Expense;

import java.util.List;

/**
 * The Class ExpenseDao - raw Hibernate CRUD operations for the Expense entity.
 *
 * @version 1.0
 */
public class ExpenseDao {

    // CREATE
    public Expense save(Expense expense) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        try {
            session.save(expense);
            transaction.commit();
        } catch (Exception ex) {
            transaction.rollback();
            throw ex;
        } finally {
            session.close();
        }
        return expense;
    }

    // READ - one record by primary key
    public Expense findById(int id) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            return session.get(Expense.class, id);
        } finally {
            session.close();
        }
    }

    // READ - one record by business key
    public Expense findByExpenseId(String expenseId) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            List<Expense> result = session
                    .createQuery("SELECT e FROM Expense e WHERE e.expenseId = :eid", Expense.class)
                    .setParameter("eid", expenseId)
                    .list();
            return result.isEmpty() ? null : result.get(0);
        } finally {
            session.close();
        }
    }

    // READ - all records
    public List<Expense> findAll() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            return session.createQuery("SELECT e FROM Expense e", Expense.class).list();
        } finally {
            session.close();
        }
    }

    // READ - number of expenses owned by a member (used by the delete guard)
    public long countByMemberId(int memberId) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            return (Long) session
                    .createQuery("SELECT count(e) FROM Expense e WHERE e.member.id = :mid")
                    .setParameter("mid", memberId)
                    .uniqueResult();
        } finally {
            session.close();
        }
    }

    // UPDATE
    public Expense update(Expense expense) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        try {
            session.update(expense);
            transaction.commit();
        } catch (Exception ex) {
            transaction.rollback();
            throw ex;
        } finally {
            session.close();
        }
        return expense;
    }

    // DELETE
    public void delete(Expense expense) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        try {
            session.delete(expense);
            transaction.commit();
        } catch (Exception ex) {
            transaction.rollback();
            throw ex;
        } finally {
            session.close();
        }
    }
}