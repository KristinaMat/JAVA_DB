package com.company;

import com.company.exeption.UserException;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;

import javax.persistence.PersistenceException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.io.*;
import java.util.List;

public class OrderService {

    private SessionFactory sessionFactory;


    public OrderService() {
        try {
            sessionFactory = new Configuration().configure().buildSessionFactory();
        } catch (HibernateException e) {
            throw e;
        }
    }

    public List<Order> getOrder(User user) throws UserException, FileNotFoundException {
        Session session = sessionFactory.openSession();
        Query<Order> query = session.createQuery("Select o from Orders o where o.username=:username", Order.class);
        query.setParameter("username", user.getUsername());

        if (query.getResultList() != null) {
            return query.getResultList();
        }

        throw new UserException("Jus neturite uzsakymu");
    }

    public List<Order> getAllOrders() {
        Session session = sessionFactory.openSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<Order> crq = cb.createQuery(Order.class);
        Root<Order> root = crq.from(Order.class);
        crq.select(root);

        Query<Order> query = session.createQuery(crq);
        List<Order> results = query.getResultList();
        session.close();
        return results;
    }

    public User getALLOrderForUser(String username) throws FileNotFoundException {
        Session session = sessionFactory.openSession();
        Query<User> query = session.createQuery("Select u from User u where u.username=:username", User.class);
        query.setParameter("username", username);
        return query.getSingleResult();
        
    }


    public Order addOrder(Order order) throws IOException {
        Session session = sessionFactory.openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.save(order);
            tx.commit();
        } catch (PersistenceException e) {
            if (tx != null) {
                tx.rollback();
            }
            return null;

        } finally {
            session.close();
        }
        return order;

    }


}
