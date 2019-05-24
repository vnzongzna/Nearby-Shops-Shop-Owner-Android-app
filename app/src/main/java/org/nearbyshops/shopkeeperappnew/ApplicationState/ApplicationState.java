package org.nearbyshops.shopkeeperappnew.ApplicationState;

import org.nearbyshops.shopkeeperappnew.Model.Order;
import org.nearbyshops.shopkeeperappnew.Model.Shop;
import org.nearbyshops.shopkeeperappnew.MyApplication;

import java.util.ArrayList;

/**
 * Created by sumeet on 15/3/16.
 */
public class ApplicationState {

    static ApplicationState instance = null;

    Shop currentShop = null;

    MyApplication myApplication;

    ArrayList<Order> selectedOrdersForDelivery = new ArrayList<>();


    private ApplicationState() {
    }


    public static ApplicationState getInstance()
    {

        if(instance == null)
        {
            instance = new ApplicationState();

            return instance;
        }

        return instance;
    }


    public ArrayList<Order> getSelectedOrdersForDelivery() {
        return selectedOrdersForDelivery;
    }

    public void setSelectedOrdersForDelivery(ArrayList<Order> selectedOrdersForDelivery) {
        this.selectedOrdersForDelivery = selectedOrdersForDelivery;
    }

    /*public Shop getCurrentShop() {

        Log.i("applog",String.valueOf(currentShop.getShopID()));

        return currentShop;
    }

    public void setCurrentShop(Shop currentShop) {

        Log.i("applog",String.valueOf(currentShop.getShopID()));

        this.currentShop = currentShop;
    }*/


    public MyApplication getMyApplication() {
        return myApplication;
    }

    public void setMyApplication(MyApplication myApplication) {
        this.myApplication = myApplication;
    }
}
