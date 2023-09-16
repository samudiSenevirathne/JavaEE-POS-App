package bo;

import bo.custom.impl.CustomerBOImpl;

public class BOFactory {
    public static BOFactory boFactory;

    private BOFactory(){

    }

    public static BOFactory getBoFactory(){
        if(boFactory==null){
            boFactory=new BOFactory();
        }
        return boFactory;
    }

    public enum BOType{
        CUSTOMER
    }

    public SuperBO getBo(BOType type){
        switch(type){
            case CUSTOMER:
                return new CustomerBOImpl();
            default:
                return null;
        }
    }

}
