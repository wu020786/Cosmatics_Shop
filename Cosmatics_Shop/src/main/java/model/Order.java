package model;

import java.io.Serializable;
import java.sql.Timestamp;

public class Order implements Serializable {
    private Integer id;
    private String orderId;
    private String customerId;
    private String salesId;
    private Double totalAmount;
    private Timestamp orderTime;

     
    public Order() {}

   
    public Order(String orderId, String customerId, String salesId, Double totalAmount) {
        this.orderId = orderId;
        this.customerId = customerId;
        this.salesId = salesId;
        this.totalAmount = totalAmount;
    }

     
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getSalesId() {
        return salesId;
    }

    public void setSalesId(String salesId) {
        this.salesId = salesId;
    }

    public Double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Timestamp getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(Timestamp orderTime) {
        this.orderTime = orderTime;
    }
}