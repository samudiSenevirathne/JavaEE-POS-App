package bo.custom;


import bo.SuperBO;
import dto.CustomerDTO;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

public interface CustomerBO extends SuperBO {

    boolean saveCustomer(Connection connection,CustomerDTO customerDTO) throws SQLException;

    boolean deleteCustomer(Connection connection,String id) throws SQLException;

    boolean editCustomer(Connection connection,CustomerDTO u) throws SQLException;

    ArrayList<CustomerDTO> loadAllCustomers(Connection connection) throws SQLException;

}
