package bo.custom.impl;

import bo.custom.OrderBO;
import dao.DAOFactory;
import dao.custom.OrderDAO;
import dto.OrderDTO;
import entity.Order;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

public class OrderBOImpl implements OrderBO {

    private final OrderDAO orderDAO= (OrderDAO) DAOFactory.getDaoFactory().getDao(DAOFactory.DAOType.ORDER);

    @Override
    public boolean saveOrder(Connection connection, OrderDTO o) throws SQLException {
        return orderDAO.save(connection,new Order(o.getOid(),o.getDate(),o.getCustomerID()));
    }

    @Override
    public ArrayList<OrderDTO> loadAllIOrders(Connection connection) throws SQLException {
        ArrayList<OrderDTO>allOrders=new ArrayList<>();
        ArrayList<Order> orders = orderDAO.loadAll(connection);
        for(Order o:orders){
            allOrders.add(new OrderDTO(o.getOid(),o.getDate(),o.getCustomerID()));
        }
        return allOrders;
    }
}
