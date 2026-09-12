package rw.ibukapay.dao;

import org.hibernate.Session;
import rw.ibukapay.model.Category;

import java.util.List;

/**
 * The Class CategoryDao - read operations for the Category lookup list.
 *
 * @version 1.0
 */
public class CategoryDao {

    HibernateUtil hibernateUtil = new HibernateUtil();

    // READ - all categories (used to populate the record form dropdown)
    public List<Category> findAllCategories() {
        // step 1: create session
        Session ss = hibernateUtil.getSessionFactory().openSession();
        // step 2: perform action
        List<Category> categories = ss
                .createQuery("SELECT c FROM Category c ORDER BY c.name", Category.class).list();
        ss.close();
        return categories;
    }

    // READ - one category by primary key
    public Category findById(int id) {
        // step 1: create session
        Session ss = hibernateUtil.getSessionFactory().openSession();
        // step 2: perform action
        Category category = ss.get(Category.class, id);
        ss.close();
        return category;
    }
}