package dao.order;

import java.util.List;
import model.Order;
import model.OrderDetail;

public interface OrderDao 
{
    void addOrder(Order o);
    
    void addOrderDetail(OrderDetail od);
    
    List<Order> selectOrdersByCustomer(String customerId);
    
  
    void deleteOrder(String orderId);
    
  
    List<OrderDetail> selectOrderDetails(String orderId);
    
    
    void updateOrder(Order order, List<OrderDetail> details);
    
  
    List<Object[]> getAllOrderDetailsWithInfo();
    
 
    List<Order> selectOrdersBySales(String salesId);
    
    List<Object[]> getSalesLeaderboard();
    
 // 進階店長報表：支援時間篩選與特定項目過濾
    List<Object[]> getAdvancedManagerReport(String startDate, String endDate, int reportType, String specificItem);
}