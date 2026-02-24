package dao.product;

import java.util.List;
import model.Product;

public interface ProductDao 
{
    void add(Product p);
    
    List<Product> selectAll();
    
    void update(Product p);
    
    void delete(String productId);
}