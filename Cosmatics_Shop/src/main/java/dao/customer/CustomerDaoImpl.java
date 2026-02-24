package dao.customer;
import java.sql.*;
import model.Customer;
import util.Tool;

public class CustomerDaoImpl implements CustomerDao {
	
    @Override
    public void add(Customer c) {
        String sql = "INSERT INTO customer(customer_id, name, number, username, password, address, level) VALUES(?,?,?,?,?,?,?)";
        try (Connection conn = Tool.getConn(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, c.getCustomerId()); ps.setString(2, c.getName()); ps.setString(3, c.getNumber());
            ps.setString(4, c.getUsername()); ps.setString(5, c.getPassword()); ps.setString(6, c.getAddress());
            ps.setInt(7, c.getLevel());
            ps.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }
    
    @Override
    public Customer selectByUsername(String username) {
        String sql = "SELECT * FROM customer WHERE username = ?";
        Customer c = null;
        try (Connection conn = Tool.getConn(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    c = new Customer(); c.setId(rs.getInt("id")); c.setCustomerId(rs.getString("customer_id"));
                    c.setName(rs.getString("name")); c.setNumber(rs.getString("number"));
                    c.setUsername(rs.getString("username")); c.setPassword(rs.getString("password"));
                    c.setAddress(rs.getString("address")); c.setLevel(rs.getInt("level"));
                }
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return c;
    }
    
    
    @Override
    public boolean selectUsername(String username) {
        String sql = "SELECT * FROM customer WHERE username = ?";
        try (Connection conn = Tool.getConn(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) { if(rs.next()) return true; }
        } catch (SQLException e) { e.printStackTrace(); }
        return false;
    }
    
    
    @Override
    public void update(Customer c) {
        String sql = "UPDATE customer SET name=?, number=?, password=?, address=? WHERE customer_id=?";
        try (Connection conn = Tool.getConn(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, c.getName()); ps.setString(2, c.getNumber()); ps.setString(3, c.getPassword());
            ps.setString(4, c.getAddress()); ps.setString(5, c.getCustomerId());
            ps.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }
}