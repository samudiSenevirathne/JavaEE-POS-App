package dao.custom.impl;

import dao.SQLUtil;
import dao.custom.CustomerDAO;
import entity.Customer;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class CustomerDAOImpl implements CustomerDAO {

    @Override
    public boolean save(Connection connection, Customer c) throws SQLException {
        String sql="insert into Customer values(?,?,?,?)";
        return SQLUtil.execute(connection,sql,c.getId(),c.getName(),c.getAddress(),c.getSalary());
    }

    @Override
    public boolean delete(Connection connection,String id) throws SQLException {
        String sql="delete from Item where code=?";
        return SQLUtil.execute(connection,sql,id);
    }

    @Override
    public boolean update(Connection connection,Customer c) throws SQLException {
        String sql="update Customer set name=?,address=?,salary=? where id=?";
        return SQLUtil.execute(connection,sql,c.getName(),c.getAddress(),c.getSalary(),c.getId());
    }

    @Override
    public ArrayList<Customer> loadAll(Connection connection) throws SQLException {
        ArrayList<Customer>list=new ArrayList<>();
        String sql="select * from Customer";
        ResultSet resultSet = SQLUtil.execute(connection,sql);
        while(resultSet.next()){
            Customer customer=new Customer(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getString(3),
                    resultSet.getString(4)
            );
            list.add(customer);
        }
        return list;
    }
}
