package com.company;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "orders")
public class Order  {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;
    @Enumerated(EnumType.STRING)
    private Payment payment;
    @Enumerated(EnumType.STRING)
    private Delivery delivery;
    private String quantity;
    private Double amount;
    @ManyToMany(cascade = CascadeType.ALL)
    private List<Product> products;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "username")
    private User user;



    public Order() {
    }

    public Order( Payment payment, Delivery delivery, String quantity, Double amount, List<Product> products, User user) {

        this.payment = payment;
        this.delivery = delivery;
        this.quantity = quantity;
        this.amount = amount;
        this.products = products;
        this.user = user;
    }




    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Payment getPayment() {
        return payment;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }

    public Delivery getDelivery() {
        return delivery;
    }

    public void setDelivery(Delivery delivery) {
        this.delivery = delivery;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public List<Product> getProducts() {
        for (Product product:getProducts()){
            return products;
        }
    return null;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id='" + id + '\'' +
                ", payment=" + payment +
                ", delivery=" + delivery +
                ", quantity='" + quantity + '\'' +
                ", amount=" + amount +
                ", products=" + products +
                ", user=" + user +
                '}';
    }
}
