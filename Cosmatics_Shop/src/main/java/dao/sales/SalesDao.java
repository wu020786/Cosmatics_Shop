package dao.sales;

import java.util.List;

import model.Sales;

public interface SalesDao {
   
    Sales selectByUsername(String username);
    
    List<Sales> selectAll();
    
    
}