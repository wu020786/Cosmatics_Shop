package service.sales;

import java.util.List;

import model.Sales;

public interface SalesService 
{
    // 員工登入驗證 (回傳 Sales 代表成功，null 代表失敗)
    Sales login(String username, String password);
    
    List<Sales> getAllSales();
}