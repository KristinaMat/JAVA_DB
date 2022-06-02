package com.company;

import com.company.exeption.ProductExeption;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;

import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ProductService {
    private SessionFactory sessionFactory;
    private List<Product> cart = new ArrayList<>();

    public ProductService() {
        try {
            sessionFactory = new Configuration().configure().buildSessionFactory();
        } catch (HibernateException e) {
            throw e;
        }
    }


    public Product getProduct(String title, String producer, String model) throws FileNotFoundException {
        Session session = sessionFactory.openSession();
        Query<Product> query = session.createQuery("Select p from Product p where p.title=:title and p.producer=:producer and p.model=:model", Product.class);
        query.setParameter("title", title);
        query.setParameter("producer", producer);
        query.setParameter("model", model);
        if (query.getSingleResult() != null) {
            return query.getSingleResult();
        }
        session.close();
        return null;
    }


    public List<Product> getAllProducts() throws FileNotFoundException {
        Session session = sessionFactory.openSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<Product> crq = cb.createQuery(Product.class);
        Root<Product> root = crq.from(Product.class);
        crq.select(root);

        Query<Product> query = session.createQuery(crq);
        List<Product> results = query.getResultList();
        session.close();
        return results;
    }


    public Product addProduct(Product product) throws IOException {
        Session session = sessionFactory.openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.save(product);
            tx.commit();
        } catch (PersistenceException e) {
            if (tx != null) {
                tx.rollback();
            }
            return null;

        } finally {
            session.close();
        }
        return product;
    }


    public Product deleteProductByModel(String model) throws IOException, ProductExeption {
        Session session = sessionFactory.openSession();
        Query<Product> q = session.createQuery("Select p from Product p where p.model =:model", Product.class);
        q.setParameter("model", model);

        Product toDelete = null;
        try {
            toDelete = q.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }

        Transaction tx = session.beginTransaction();
        try {
            session.delete(toDelete);
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
        } finally {
            session.close();
        }

        return toDelete;

    }


    public void addToCart(Product product) {

        cart.add(product);

    }

    public Product getCartProduct(String title, String producer, String model) throws FileNotFoundException {
        List<Product> products = getAllProducts();
        for (Product product : products) {
            if (product.getTitle().equalsIgnoreCase(title) && product.getProducer().equalsIgnoreCase(producer) && product.getModel().equalsIgnoreCase(model)) {
                return product;
            }

        }
        return null;
    }

    public List<Product> getAllCartProduct() throws FileNotFoundException {
        List<Product> cart = new ArrayList<>();
        for (Product product : cart) {
            cart.add(product);

        }
        return cart;
    }

    public void deleteCartProductByModel(String model) throws ProductExeption, FileNotFoundException {
        List<Product> cart = new ArrayList<>();

        Product productToDelete = getProductByModel(model, cart);

        cart.remove(productToDelete);

    }

    private Product getProductByModel(String model, List<Product> cart) {
        Iterator<Product> iter = cart.iterator();
        while (iter.hasNext()) {
            Product product = iter.next();
            if (product.getModel().equalsIgnoreCase(model)) {
                return product;
            }
        }
        return null;
    }
}
