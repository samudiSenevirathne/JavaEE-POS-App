package bo.custom.impl;

import bo.custom.CustomerBO;
import dao.DAOFactory;
import dao.custom.CustomerDAO;
import dto.CustomerDTO;
import entity.Customer;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

public class CustomerBOImpl implements CustomerBO {

    private final CustomerDAO customerDAO= (CustomerDAO) DAOFactory.getDaoFactory().getDao(DAOFactory.DAOType.CUSTOMER);

    @Override
    public boolean saveCustomer(Connection connection, CustomerDTO c) throws SQLException {
        return customerDAO.save(connection,new Customer(c.getId(),c.getName(),c.getAddress(),c.getSalary()));
    }

    @Override
    public boolean deleteCustomer(Connection connection, String id) throws SQLException {
        return customerDAO.delete(connection,id);
    }

    @Override
    public boolean editCustomer(Connection connection, CustomerDTO c) throws SQLException {
        return customerDAO.update(connection,new Customer(c.getId(),c.getName(),c.getAddress(),c.getSalary()));
    }

    @Override
    public ArrayList<CustomerDTO> loadAllCustomers(Connection connection) throws SQLException {
        ArrayList<CustomerDTO>allCustomers=new ArrayList<>();
        ArrayList<Customer> customers = customerDAO.loadAll(connection);
        for(Customer c:customers){
            allCustomers.add(new CustomerDTO(c.getId(),c.getName(),c.getAddress(),c.getSalary()));
        }
        return allCustomers;
    }
}
