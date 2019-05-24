package org.nearbyshops.shopkeeperappnew.Markets.Interfaces;


import org.nearbyshops.shopkeeperappnew.Markets.Model.ServiceConfigurationGlobal;

public interface listItemMarketNotifications
{
    void listItemClick(ServiceConfigurationGlobal configurationGlobal, int position);
    void selectMarketSuccessful(ServiceConfigurationGlobal configurationGlobal, int position);
    void showMessage(String message);
}