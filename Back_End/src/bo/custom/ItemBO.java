package bo.custom;

import bo.SuperBO;

import dto.ItemDTO;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

public interface ItemBO extends SuperBO {

    boolean saveItem(Connection connection,ItemDTO itemDTO) throws SQLException;

    boolean deleteItem(Connection connection,String code) throws SQLException;

    boolean editItem(Connection connection,ItemDTO i) throws SQLException;

    ArrayList<ItemDTO> loadAllItems(Connection connection) throws SQLException;

}
