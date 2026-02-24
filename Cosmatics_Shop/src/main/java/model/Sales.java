package model;

import java.io.Serializable;

public class Sales implements Serializable {
    private Integer id;
    private String salesId;
    private String name;
    private String username;
    private String password;
    private String address;

    // --- 1. 無參數建構子 (框架與預設使用) ---
    public Sales() {
    }

    // --- 2. 完整參數建構子 (方便快速建立物件，不包含自動遞增的 id) ---
    public Sales(String salesId, String name, String username, String password, String address) {
        this.salesId = salesId;
        this.name = name;
        this.username = username;
        this.password = password;
        this.address = address;
    }

    // --- 3. Getters and Setters ---
    
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSalesId() {
        return salesId;
    }

    public void setSalesId(String salesId) {
        this.salesId = salesId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
}