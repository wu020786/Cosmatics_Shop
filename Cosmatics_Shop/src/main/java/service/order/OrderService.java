package service.order;

import java.util.List;
import model.Order;
import model.OrderDetail;

public interface OrderService 
{
    void placeOrder(Order order, List<OrderDetail> details);
    
    List<Order> getOrdersByCustomer(String customerId);
    
    void cancelOrder(String orderId);
    
    List<OrderDetail> getOrderDetails(String orderId);
    
    void modifyOrder(Order order, List<OrderDetail> details);
    
 
    List<Object[]> getAllOrderDetailsWithInfo();
    
    List<Order> getOrdersBySales(String salesId);
    
 // 店長專用
    List<Object[]> getSalesLeaderboard();
    
    List<Object[]> getAdvancedManagerReport(String startDate, String endDate, int reportType, String specificItem);
}