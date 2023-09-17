package bo.custom.impl;

import bo.custom.OrderDetailBO;
import dao.DAOFactory;
import dao.custom.OrderDetailDAO;
import dto.OrderDetailDTO;
import entity.OrderDetail;

import java.sql.Connection;
import java.sql.SQLException;

public class OrderDetailBOImpl implements OrderDetailBO {

    private final OrderDetailDAO orderDetailDAO= (OrderDetailDAO) DAOFactory.getDaoFactory().getDao(DAOFactory.DAOType.ORDER_DETAIL);

    @Override
    public boolean saveOrderDetail(Connection connection, OrderDetailDTO od) throws SQLException {
        return orderDetailDAO.save(connection,new OrderDetail(od.getOid(),od.getItemCode(),od.getQty(),od.getUnitPrice()));
    }
}
