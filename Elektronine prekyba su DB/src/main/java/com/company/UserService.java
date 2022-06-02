package com.company;

import com.company.exeption.UserException;
import org.hibernate.*;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;

import javax.persistence.PersistenceException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.io.*;

import java.util.List;

public class UserService {

    private SessionFactory sessionFactory;


    public UserService() {
        try {
            sessionFactory = new Configuration().configure().buildSessionFactory();

        } catch (HibernateException e) {
            throw e;
        }
    }


    public User getUser(String username, String password) throws UserException, FileNotFoundException {
        Session session = sessionFactory.openSession();
        Query<User> query = session.createQuery("Select u from User u where u.username=:username and u.password=:password", User.class);
        query.setParameter("username", username);
        query.setParameter("password", password);
        if (query.getSingleResult() != null) {
            return query.getSingleResult();
        }

        throw new UserException("Neteisingi prisijungimo duomenys");
    }

    public List<User> getAllUsers() {
        Session session = sessionFactory.openSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<User> crq = cb.createQuery(User.class);
        Root<User> root = crq.from(User.class);
        crq.select(root);

        Query<User> query = session.createQuery(crq);
        List<User> results = query.getResultList();
        session.close();
        return results;
    }

    public boolean isUserExists(String username) {

        List<User> users = getAllUsers();

        for (User user : users) {
            if (user.getUsername().equalsIgnoreCase(username)) {
                return true;
            }
        }
        return false;
    }



    public User addUser(User user) {
        Session session = sessionFactory.openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.save(user);
            tx.commit();
        } catch (PersistenceException e) {
            if (tx != null) {
                tx.rollback();
            }
            return null;

        } finally {
            session.close();
        }
        return user;
    }


}
