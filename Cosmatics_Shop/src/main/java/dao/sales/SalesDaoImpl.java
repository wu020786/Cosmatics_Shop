package dao.sales;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import dao.sales.SalesDao;
import model.Sales;
import util.Tool;

public class SalesDaoImpl implements SalesDao {

    @Override
    public Sales selectByUsername(String username) {
        String sql = "SELECT * FROM sales WHERE username = ?";
        Sales sales = null;
        
        try (Connection conn = Tool.getConn();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, username);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    sales = new Sales();
                    sales.setId(rs.getInt("id"));
                    sales.setSalesId(rs.getString("sales_id"));
                    sales.setName(rs.getString("name"));
                    sales.setUsername(rs.getString("username"));
                    sales.setPassword(rs.getString("password"));
                    sales.setAddress(rs.getString("address"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return sales;
    }
    
 // 【新增】實作撈取所有業務員
    @Override
    public java.util.List<Sales> selectAll() {
        String sql = "SELECT * FROM sales";
        java.util.List<Sales> list = new java.util.ArrayList<>();
        try (Connection conn = Tool.getConn(); 
             PreparedStatement ps = conn.prepareStatement(sql); 
             ResultSet rs = ps.executeQuery()) {
            while(rs.next()) {
                Sales s = new Sales();
                s.setId(rs.getInt("id"));
                s.setSalesId(rs.getString("sales_id"));
                s.setName(rs.getString("name"));
                s.setUsername(rs.getString("username"));
                s.setPassword(rs.getString("password"));
                s.setAddress(rs.getString("address"));
                list.add(s);
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }
}