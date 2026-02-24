package dao.product;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.Product;
import util.Tool;

public class ProductDaoImpl implements ProductDao 
{
    @Override
    public void add(Product p) {
        String sql = "INSERT INTO product(product_id, product_name, category_id, category_name, price) VALUES(?,?,?,?,?)";
        try (Connection conn = Tool.getConn(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, p.getProductId()); ps.setString(2, p.getProductName());
            ps.setString(3, p.getCategoryId()); ps.setString(4, p.getCategoryName()); ps.setDouble(5, p.getPrice());
            ps.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }
    
    @Override
    public List<Product> selectAll() {
        String sql = "SELECT * FROM product";
        List<Product> l = new ArrayList<>();
        try (Connection conn = Tool.getConn(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while(rs.next()) {
                Product p = new Product(); p.setId(rs.getInt("id")); p.setProductId(rs.getString("product_id"));
                p.setProductName(rs.getString("product_name")); p.setCategoryId(rs.getString("category_id"));
                p.setCategoryName(rs.getString("category_name")); p.setPrice(rs.getDouble("price"));
                l.add(p);
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return l;
    }
    
    @Override
    public void update(Product p) {
        String sql = "UPDATE product SET product_name=?, price=? WHERE product_id=?";
        try (Connection conn = Tool.getConn(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, p.getProductName()); ps.setDouble(2, p.getPrice()); ps.setString(3, p.getProductId());
            ps.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }
    
    @Override
    public void delete(String productId) {
        String sql = "DELETE FROM product WHERE product_id=?";
        try (Connection conn = Tool.getConn(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, productId); ps.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }
}
