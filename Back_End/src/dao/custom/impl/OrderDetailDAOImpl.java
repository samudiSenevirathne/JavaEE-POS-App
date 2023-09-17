package dao.custom.impl;

import dao.SQLUtil;
import dao.custom.OrderDetailDAO;
import entity.OrderDetail;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

public class OrderDetailDAOImpl implements OrderDetailDAO {

    @Override
    public boolean save(Connection connection, OrderDetail od) throws SQLException{
        String sql="insert into orderdetails values(?,?,?,?)";
        return SQLUtil.execute(connection,sql,od.getOid(),od.getItemCode(),od.getQty(),od.getUnitPrice());
    }

    @Override
    public boolean delete(Connection connection, String id) throws SQLException {
        return false;
    }

    @Override
    public boolean update(Connection connection, OrderDetail dto) throws SQLException {
        return false;
    }

    @Override
    public ArrayList<OrderDetail> loadAll(Connection connection) throws SQLException {
        return null;
    }
}
