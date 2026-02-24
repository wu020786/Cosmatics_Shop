package service.product;

import java.util.List;

import dao.product.ProductDaoImpl;
import model.Product;
 
public class ProductServiceImpl implements ProductService 
{
    private ProductDaoImpl dao = new ProductDaoImpl();
    public List<Product> getAllProducts() 
    { return dao.selectAll(); }
    
    public void addProduct(Product p) 
    { dao.add(p); }
    
    public void updateProduct(Product p) 
    { dao.update(p); }
    
    public void deleteProduct(String id)
    { dao.delete(id); }
}