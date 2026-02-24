package service.product;

import java.util.List;
import model.Product;

public interface ProductService 
{
    List<Product> getAllProducts();
    
    void addProduct(Product product);
    
    void updateProduct(Product product);
    
    void deleteProduct(String productId);
}