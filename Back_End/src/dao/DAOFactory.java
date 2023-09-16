package dao;

import dao.custom.impl.CustomerDAOImpl;

public class DAOFactory {

    public static DAOFactory daoFactory;

    private DAOFactory() {

    }

    public static DAOFactory getDaoFactory() {
        if (daoFactory == null) {
            daoFactory = new DAOFactory();
        }
        return daoFactory;
    }

    public enum DAOType {
        CUSTOMER
    }

    public SuperDAO getDao(DAOType type) {
        switch (type) {
            case CUSTOMER:
                return new CustomerDAOImpl();
            default:
                return null;
        }
    }

}
