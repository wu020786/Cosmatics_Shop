package dao.customer;

import model.Customer;

public interface CustomerDao 
{
	
    void add(Customer c);
    
    Customer selectByUsername(String username);
    
    boolean selectUsername(String username);
    
    void update(Customer c);
}