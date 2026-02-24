package service.order;

import java.util.List;

import dao.order.OrderDaoImpl;
import model.Order;
import model.OrderDetail;
 
public class OrderServiceImpl implements OrderService 
{
    private OrderDaoImpl dao = new OrderDaoImpl();
    
    public void placeOrder(Order o, List<OrderDetail> details) 
    {
        dao.addOrder(o);
        for(OrderDetail d : details) {
            d.setOrderId(o.getOrderId());
            dao.addOrderDetail(d);
        }
    }
    
    public List<Order> getOrdersByCustomer(String cid) 
    { return dao.selectOrdersByCustomer(cid); }
    
 
    @Override
    public void cancelOrder(String orderId) {
        dao.deleteOrder(orderId);
    }
    
    @Override
    public List<OrderDetail> getOrderDetails(String orderId) {
        return dao.selectOrderDetails(orderId);
    }

    @Override
    public void modifyOrder(Order order, List<OrderDetail> details) {
        dao.updateOrder(order, details);
    }
    
    @Override
    public List<Object[]> getAllOrderDetailsWithInfo() {
        return dao.getAllOrderDetailsWithInfo();
    }
    
    @Override
    public List<Order> getOrdersBySales(String salesId) {
        return dao.selectOrdersBySales(salesId);
    }
    
    @Override
    public List<Object[]> getSalesLeaderboard() {
        return dao.getSalesLeaderboard();
    }
    
    @Override
    public List<Object[]> getAdvancedManagerReport(String startDate, String endDate, int reportType, String specificItem) {
        return dao.getAdvancedManagerReport(startDate, endDate, reportType, specificItem);
    }
    
}