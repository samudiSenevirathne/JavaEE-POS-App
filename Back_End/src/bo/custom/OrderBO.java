package bo.custom;

import bo.SuperBO;
import dto.OrderDTO;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

public interface OrderBO extends SuperBO {

    boolean saveOrder(Connection connection, OrderDTO orderDTO) throws SQLException;

    ArrayList<OrderDTO> loadAllIOrders(Connection connection) throws SQLException;

}
