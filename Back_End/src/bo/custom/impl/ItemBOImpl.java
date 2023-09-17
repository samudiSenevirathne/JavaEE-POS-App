package bo.custom.impl;

import bo.custom.ItemBO;
import dao.DAOFactory;
import dao.custom.ItemDAO;
import dto.ItemDTO;
import entity.Item;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

public class ItemBOImpl implements ItemBO {

    private final ItemDAO itemDAO= (ItemDAO) DAOFactory.getDaoFactory().getDao(DAOFactory.DAOType.ITEM);

    @Override
    public boolean saveItem(Connection connection, ItemDTO i) throws SQLException {
        return itemDAO.save(connection,new Item(i.getCode(),i.getDescription(),i.getQtyOnHand(),i.getUnitPrice()));
    }

    @Override
    public boolean deleteItem(Connection connection, String code) throws SQLException {
        return itemDAO.delete(connection,code);
    }

    @Override
    public boolean editItem(Connection connection, ItemDTO i) throws SQLException {
        return itemDAO.update(connection,new Item(i.getCode(),i.getDescription(),i.getQtyOnHand(),i.getUnitPrice()));
    }

    @Override
    public ArrayList<ItemDTO> loadAllItems(Connection connection) throws SQLException {
        ArrayList<ItemDTO>allItems=new ArrayList<>();
        ArrayList<Item> items = itemDAO.loadAll(connection);
        for(Item i:items){
            allItems.add(new ItemDTO(i.getCode(),i.getDescription(),i.getQtyOnHand(),i.getUnitPrice()));
        }
        return allItems;
    }

    @Override
    public boolean editItemQty(Connection connection, ItemDTO i) throws SQLException {
        return itemDAO.update(connection,new Item(i.getCode(),i.getQtyOnHand()));
    }
}
