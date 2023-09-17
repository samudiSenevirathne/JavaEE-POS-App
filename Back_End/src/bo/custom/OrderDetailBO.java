package bo.custom;

import bo.SuperBO;
import dto.OrderDetailDTO;

import java.sql.Connection;
import java.sql.SQLException;

public interface OrderDetailBO extends SuperBO {

    boolean saveOrderDetail(Connection connection, OrderDetailDTO orderDetailDTO) throws SQLException;

}
