package service.customer;

import model.Customer;

public interface CustomerService 
{
    Customer login(String username, String password);
    
    boolean isUsernameTaken(String username);
    
    void addCustomer(Customer customer);
    
    void updateCustomer(Customer customer);
}