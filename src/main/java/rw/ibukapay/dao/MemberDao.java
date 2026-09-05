package rw.ibukapay.dao;

import org.hibernate.Session;
import org.hibernate.Transaction;
import rw.ibukapay.model.Member;

import java.util.List;

/**
 * The Class MemberDao - raw Hibernate CRUD operations for the Member entity.
 *
 * @version 1.0
 */
public class MemberDao {

    // CREATE
    public Member save(Member member) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        try {
            session.save(member);
            transaction.commit();
        } catch (Exception ex) {
            transaction.rollback();
            throw ex;
        } finally {
            session.close();
        }
        return member;
    }

    // READ - one record by primary key
    public Member findById(int id) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            return session.get(Member.class, id);
        } finally {
            session.close();
        }
    }

    // READ - one record by business key
    public Member findByMemberId(String memberId) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            List<Member> result = session
                    .createQuery("SELECT m FROM Member m WHERE m.memberId = :mid", Member.class)
                    .setParameter("mid", memberId)
                    .list();
            return result.isEmpty() ? null : result.get(0);
        } finally {
            session.close();
        }
    }

    // READ - all records
    public List<Member> findAll() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            return session.createQuery("SELECT m FROM Member m", Member.class).list();
        } finally {
            session.close();
        }
    }

    // UPDATE
    public Member update(Member member) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        try {
            session.update(member);
            transaction.commit();
        } catch (Exception ex) {
            transaction.rollback();
            throw ex;
        } finally {
            session.close();
        }
        return member;
    }

    // DELETE
    public void delete(Member member) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        try {
            session.delete(member);
            transaction.commit();
        } catch (Exception ex) {
            transaction.rollback();
            throw ex;
        } finally {
            session.close();
        }
    }
}