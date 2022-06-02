package com.company;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name="product")

public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;
    private String title;
    private String producer;
    private String model;
    private double price;
    @ManyToMany(cascade = CascadeType.ALL, mappedBy = "products")
//    @JoinTable(name = "order_product", joinColumns = {@JoinColumn(referencedColumnName = "id")},inverseJoinColumns = {@JoinColumn(referencedColumnName = "id")})
    private List<Order> orders;

    public Product() {
    }

    public Product(String title, String producer, String model, double price) {
        this.title = title;
        this.producer = producer;
        this.model = model;
        this.price = price;

    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getProducer() {
        return producer;
    }

    public void setProducer(String producer) {
        this.producer = producer;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", producer='" + producer + '\'' +
                ", model='" + model + '\'' +
                ", price=" + price +
                ", orders=" + orders +
                '}';
    }
}
