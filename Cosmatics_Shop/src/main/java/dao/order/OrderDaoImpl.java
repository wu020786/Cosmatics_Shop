package dao.order;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.Order;
import model.OrderDetail;
import util.Tool;

public class OrderDaoImpl implements OrderDao 
{
    @Override
    public void addOrder(Order o) {
        String sql = "INSERT INTO orders(order_id, customer_id, sales_id, total_amount) VALUES(?,?,?,?)";
        try (Connection conn = Tool.getConn(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, o.getOrderId()); ps.setString(2, o.getCustomerId());
            ps.setString(3, o.getSalesId()); ps.setDouble(4, o.getTotalAmount());
            ps.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }
    
    
    @Override
    public void addOrderDetail(OrderDetail od) {
        String sql = "INSERT INTO order_detail(order_id, product_id, product_name, quantity, unit_price) VALUES(?,?,?,?,?)";
        try (Connection conn = Tool.getConn(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, od.getOrderId()); ps.setString(2, od.getProductId());
            ps.setString(3, od.getProductName()); ps.setInt(4, od.getQuantity());
            ps.setDouble(5, od.getUnitPrice());
            ps.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }
    
    
    @Override
    public List<Order> selectOrdersByCustomer(String customerId) {
        String sql = "SELECT * FROM orders WHERE customer_id = ? ORDER BY order_time DESC";
        List<Order> l = new ArrayList<>();
        try (Connection conn = Tool.getConn(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, customerId);
            try (ResultSet rs = ps.executeQuery()) {
                while(rs.next()) {
                    Order o = new Order(); o.setId(rs.getInt("id")); o.setOrderId(rs.getString("order_id"));
                    o.setCustomerId(rs.getString("customer_id")); o.setSalesId(rs.getString("sales_id"));
                    o.setTotalAmount(rs.getDouble("total_amount")); o.setOrderTime(rs.getTimestamp("order_time"));
                    l.add(o);
                }
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return l;
    }
    
    @Override
    public void deleteOrder(String orderId) {
        // SQL 1: 先刪除明細 (子表)
        String sqlDetail = "DELETE FROM order_detail WHERE order_id=?";
        // SQL 2: 再刪除主檔 (父表)
        String sqlOrder = "DELETE FROM orders WHERE order_id=?";
        
        try (Connection conn = Tool.getConn()) {
            // 開啟手動交易 (確保明細和主檔要嘛一起刪除，要嘛都不刪)
            conn.setAutoCommit(false); 
            
            try (PreparedStatement ps1 = conn.prepareStatement(sqlDetail);
                 PreparedStatement ps2 = conn.prepareStatement(sqlOrder)) {
                
                // 執行刪除明細
                ps1.setString(1, orderId);
                ps1.executeUpdate();
                
                // 執行刪除主檔
                ps2.setString(1, orderId);
                ps2.executeUpdate();
                
                // 兩者都成功，提交交易
                conn.commit(); 
            } catch (SQLException ex) {
                // 如果發生錯誤，就復原 (Rollback)
                conn.rollback(); 
                ex.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public List<OrderDetail> selectOrderDetails(String orderId) {
        String sql = "SELECT * FROM order_detail WHERE order_id = ?";
        List<OrderDetail> list = new ArrayList<>();
        try (Connection conn = Tool.getConn(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, orderId);
            try (ResultSet rs = ps.executeQuery()) {
                while(rs.next()) {
                    OrderDetail od = new OrderDetail();
                    od.setId(rs.getInt("id"));
                    od.setOrderId(rs.getString("order_id"));
                    od.setProductId(rs.getString("product_id"));
                    od.setProductName(rs.getString("product_name"));
                    od.setQuantity(rs.getInt("quantity"));
                    od.setUnitPrice(rs.getDouble("unit_price"));
                    list.add(od);
                }
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    @Override
    public void updateOrder(Order order, List<OrderDetail> details) {
        // 更新邏輯：1. 更新主檔總額 -> 2. 刪除舊明細 -> 3. 寫入新明細
        String updateOrderSql = "UPDATE orders SET total_amount=? WHERE order_id=?";
        String deleteDetailsSql = "DELETE FROM order_detail WHERE order_id=?";
        String insertDetailSql = "INSERT INTO order_detail(order_id, product_id, product_name, quantity, unit_price) VALUES(?,?,?,?,?)";
        
        try (Connection conn = Tool.getConn()) {
            conn.setAutoCommit(false); // 開啟交易
            
            try (PreparedStatement psOrder = conn.prepareStatement(updateOrderSql);
                 PreparedStatement psDelete = conn.prepareStatement(deleteDetailsSql);
                 PreparedStatement psInsert = conn.prepareStatement(insertDetailSql)) {
                
                // 1. 更新主訂單金額
                psOrder.setDouble(1, order.getTotalAmount());
                psOrder.setString(2, order.getOrderId());
                psOrder.executeUpdate();
                
                // 2. 刪除舊的明細
                psDelete.setString(1, order.getOrderId());
                psDelete.executeUpdate();
                
                // 3. 寫入新的明細
                for(OrderDetail od : details) {
                    psInsert.setString(1, order.getOrderId());
                    psInsert.setString(2, od.getProductId());
                    psInsert.setString(3, od.getProductName());
                    psInsert.setInt(4, od.getQuantity());
                    psInsert.setDouble(5, od.getUnitPrice());
                    psInsert.executeUpdate();
                }
                
                conn.commit(); // 提交交易
            } catch (SQLException ex) {
                conn.rollback();
                ex.printStackTrace();
            }
        } catch (SQLException e) { e.printStackTrace(); }
    }
    
    @Override
    public List<Object[]> getAllOrderDetailsWithInfo() {
        // 使用 JOIN 串接訂單(orders)、明細(order_detail)與產品(product)
        String sql = "SELECT o.order_id, o.order_time, o.customer_id, o.sales_id, " +
                     "p.category_name, od.product_name, od.quantity, (od.quantity * od.unit_price) as subtotal " +
                     "FROM orders o " +
                     "JOIN order_detail od ON o.order_id = od.order_id " +
                     "LEFT JOIN product p ON od.product_id = p.product_id " +
                     "ORDER BY o.order_time DESC";
        List<Object[]> list = new ArrayList<>();
        try (Connection conn = Tool.getConn(); 
             PreparedStatement ps = conn.prepareStatement(sql); 
             ResultSet rs = ps.executeQuery()) {
            while(rs.next()) {
                String catName = rs.getString("category_name");
                if(catName == null) catName = "Unknown"; // 避免產品被刪除後找不到分類
                
                list.add(new Object[]{
                    rs.getString("order_id"), 
                    rs.getTimestamp("order_time"), 
                    rs.getString("customer_id"), 
                    rs.getString("sales_id"),
                    catName, 
                    rs.getString("product_name"), 
                    rs.getInt("quantity"), 
                    rs.getDouble("subtotal")
                });
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }
    
    @Override
    public List<Order> selectOrdersBySales(String salesId) {
        String sql = "SELECT * FROM orders WHERE sales_id = ? ORDER BY order_time DESC";
        List<Order> l = new ArrayList<>();
        try (Connection conn = Tool.getConn(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, salesId);
            try (ResultSet rs = ps.executeQuery()) {
                while(rs.next()) {
                    Order o = new Order(); 
                    o.setId(rs.getInt("id")); 
                    o.setOrderId(rs.getString("order_id"));
                    o.setCustomerId(rs.getString("customer_id")); 
                    o.setSalesId(rs.getString("sales_id"));
                    o.setTotalAmount(rs.getDouble("total_amount")); 
                    o.setOrderTime(rs.getTimestamp("order_time")); 
                    l.add(o);
                }
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return l;
    }
    
    @Override
    public List<Object[]> getSalesLeaderboard() {
        // 去資料庫把 sales 跟 orders 結合起來算總額，並依照業績排序
        String sql = "SELECT s.name, COUNT(o.order_id) as order_count, IFNULL(SUM(o.total_amount), 0) as total_rev " +
                     "FROM sales s LEFT JOIN orders o ON s.sales_id = o.sales_id " +
                     "GROUP BY s.sales_id, s.name ORDER BY total_rev DESC";
        List<Object[]> list = new ArrayList<>();
        try (Connection conn = Tool.getConn(); 
             PreparedStatement ps = conn.prepareStatement(sql); 
             ResultSet rs = ps.executeQuery()) {
            
            int rank = 1;
            while(rs.next()) {
                double rev = rs.getDouble("total_rev");
                int count = rs.getInt("order_count");
                double aov = (count > 0) ? (rev / count) : 0.0;
                // 把算好的資料打包成陣列: [排名, 業務姓名, 訂單數, 總業績, 客單價]
                list.add(new Object[]{ rank++, rs.getString("name"), count, rev, aov });
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }
    
    @Override
    public List<Object[]> getAdvancedManagerReport(String startDate, String endDate, int reportType, String specificItem) {
        List<Object[]> list = new ArrayList<>();
        StringBuilder sql = new StringBuilder();
        
        sql.append("SELECT s.name AS sales_name ");
        if (reportType == 0) { 
            sql.append(", COUNT(DISTINCT o.order_id) AS orders, SUM(od.quantity * od.unit_price) AS revenue ");
        } else if (reportType == 1) { 
            sql.append(", p.category_name AS item_name, SUM(od.quantity) AS qty, SUM(od.quantity * od.unit_price) AS revenue ");
        } else if (reportType == 2) { 
            sql.append(", p.product_name AS item_name, SUM(od.quantity) AS qty, SUM(od.quantity * od.unit_price) AS revenue ");
        }
        
        sql.append("FROM sales s ");
        sql.append("JOIN orders o ON s.sales_id = o.sales_id ");
        sql.append("JOIN order_detail od ON o.order_id = od.order_id ");
        sql.append("JOIN product p ON od.product_id = p.product_id ");
        
        sql.append("WHERE 1=1 ");
        if (startDate != null && !startDate.isEmpty()) sql.append("AND o.order_time >= ? ");
        if (endDate != null && !endDate.isEmpty()) sql.append("AND o.order_time <= ? ");
        
        // 【新增過濾條件】如果有選特定的分類或產品，就加入 SQL 條件
        if (specificItem != null && !specificItem.startsWith("---")) {
            if (reportType == 1) sql.append("AND p.category_name = ? ");
            if (reportType == 2) sql.append("AND p.product_name = ? ");
        }
        
        if (reportType == 0) {
            sql.append("GROUP BY s.sales_id, s.name ORDER BY revenue DESC");
        } else if (reportType == 1) {
            sql.append("GROUP BY s.sales_id, s.name, p.category_name ORDER BY revenue DESC");
        } else if (reportType == 2) {
            sql.append("GROUP BY s.sales_id, s.name, p.product_name ORDER BY revenue DESC");
        }

        try (Connection conn = Tool.getConn(); PreparedStatement ps = conn.prepareStatement(sql.toString())) {
            int paramIndex = 1;
            if (startDate != null && !startDate.isEmpty()) ps.setString(paramIndex++, startDate + " 00:00:00");
            if (endDate != null && !endDate.isEmpty()) ps.setString(paramIndex++, endDate + " 23:59:59");
            
            // 【綁定過濾條件參數】
            if (specificItem != null && !specificItem.startsWith("---")) {
                if (reportType == 1 || reportType == 2) ps.setString(paramIndex++, specificItem);
            }
            
            try (ResultSet rs = ps.executeQuery()) {
                while(rs.next()) {
                    if (reportType == 0) {
                        double rev = rs.getDouble("revenue");
                        int count = rs.getInt("orders");
                        double aov = (count > 0) ? (rev / count) : 0.0;
                        list.add(new Object[]{ rs.getString("sales_name"), count, rev, aov });
                    } else {
                        list.add(new Object[]{ rs.getString("sales_name"), rs.getString("item_name"), rs.getInt("qty"), rs.getDouble("revenue") });
                    }
                }
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }
    
}