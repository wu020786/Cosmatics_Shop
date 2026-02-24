package model;

import java.io.Serializable;

public class Customer implements Serializable {
    private Integer id;
    private String customerId;
    private String name;
    private String number; 
    private String username;
    private String password;
    private String address;
    private Integer level;

    
    public Customer() {}

    
    public Customer(String customerId, String name, String number, String username, String password, String address, Integer level) {
        this.customerId = customerId;
        this.name = name;
        this.number = number;
        this.username = username;
        this.password = password;
        this.address = address;
        this.level = level;
    }

   
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }
}