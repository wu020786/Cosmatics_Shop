package service.customer;

import dao.customer.CustomerDaoImpl;
import model.Customer;

public class CustomerServiceImpl implements CustomerService 
{
    private CustomerDaoImpl dao = new CustomerDaoImpl();
    
    public Customer login(String u, String p) {
        Customer c = dao.selectByUsername(u);
        if(c != null && c.getPassword().equals(p)) return c;
        return null;
    }
    
    public boolean isUsernameTaken(String u) { return dao.selectUsername(u); }
    public void addCustomer(Customer c) { dao.add(c); }
    public void updateCustomer(Customer c) { dao.update(c); }
}