package dao.custom.impl;

import dao.SQLUtil;
import dao.custom.ItemDAO;
import entity.Item;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ItemDAOImpl implements ItemDAO {

    @Override
    public boolean save(Connection connection, Item i) throws SQLException {
        String sql="insert into Item values(?,?,?,?)";
        return SQLUtil.execute(connection,sql,i.getCode(),i.getDescription(),i.getQtyOnHand(),i.getUnitPrice());
    }

    @Override
    public boolean delete(Connection connection, String id) throws SQLException {
        String sql="delete from Item where code=?";
        return SQLUtil.execute(connection,sql,id);
    }

    @Override
    public boolean update(Connection connection, Item i) throws SQLException {
        String sql="update Item set description=?,qtyOnHand=?,unitPrice=? where code=?";
        return SQLUtil.execute(connection,sql,i.getDescription(),i.getQtyOnHand(),i.getUnitPrice(),i.getCode());
    }

    @Override
    public ArrayList<Item> loadAll(Connection connection) throws SQLException {
        ArrayList<Item>list=new ArrayList<>();
        String sql="select * from Item";
        ResultSet resultSet = SQLUtil.execute(connection,sql);
        while(resultSet.next()){
            Item item=new Item(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getInt(3),
                    resultSet.getDouble(4)
            );
            list.add(item);
        }
        return list;
    }

    @Override
    public boolean updateQty(Connection connection, Item i) throws SQLException {
        String sql="update item set qtyOnHand=qtyOnHand-? where code=?";
        return SQLUtil.execute(connection,sql,i.getQtyOnHand(),i.getCode());
    }
}
