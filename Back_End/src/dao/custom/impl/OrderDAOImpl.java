package dao.custom.impl;

import dao.SQLUtil;
import dao.custom.OrderDAO;
import entity.Order;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class OrderDAOImpl implements OrderDAO {

    @Override
    public boolean save(Connection connection, Order o) throws SQLException {
        String sql="insert into orders values(?,?,?)";
        return SQLUtil.execute(connection,sql,o.getOid(),o.getDate(),o.getCustomerID());
    }

    @Override
    public boolean delete(Connection connection, String id) throws SQLException {
        return false;
    }

    @Override
    public boolean update(Connection connection, Order dto) throws SQLException {
        return false;
    }

    @Override
    public ArrayList<Order> loadAll(Connection connection) throws SQLException {
        ArrayList<Order>list=new ArrayList<>();
        String sql="select * from orders";
        ResultSet resultSet = SQLUtil.execute(connection,sql);
        while(resultSet.next()){
            Order order=new Order(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getString(3)
            );
            list.add(order);
        }
        return list;
    }
}
