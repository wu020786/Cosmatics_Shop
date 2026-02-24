package service.sales;

import dao.sales.SalesDaoImpl;
import model.Sales;
 

public class SalesServiceImpl implements SalesService {

    private SalesDaoImpl salesDao = new SalesDaoImpl();

    @Override
    public Sales login(String username, String password) 
    {
        // 1. 去資料庫把這個帳號的員工撈出來
        Sales sales = salesDao.selectByUsername(username);
        
        // 2. 如果有這個人，而且密碼比對正確，就回傳員工物件
        if (sales != null && sales.getPassword().equals(password)) {
            return sales;
        }
        
        // 3. 帳號或密碼錯誤
        return null;
        
     
    }
    
 // 【新增】實作取得所有業務員名單
    @Override
    public java.util.List<Sales> getAllSales() 
    {
        return salesDao.selectAll();
    }
}