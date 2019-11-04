package org.nearbyshops.shopkeeperappnew.DaggerComponents;

import dagger.Component;
import org.nearbyshops.shopkeeperappnew.DaggerModules.AppModule;
import org.nearbyshops.shopkeeperappnew.DaggerModules.NetModule;
import org.nearbyshops.shopkeeperappnew.DeliveryPersonInventory.Fragment.DeliveryInventoryFragment;
import org.nearbyshops.shopkeeperappnew.EditItem.EditItemFragmentNew;
import org.nearbyshops.shopkeeperappnew.EditItemImage.EditItemImageFragment;
import org.nearbyshops.shopkeeperappnew.EditProfile.ChangeEmail.FragmentChangeEmail;
import org.nearbyshops.shopkeeperappnew.EditProfile.ChangeEmail.FragmentVerifyEmailChange;
import org.nearbyshops.shopkeeperappnew.EditProfile.ChangePassword.FragmentChangePassword;
import org.nearbyshops.shopkeeperappnew.EditProfile.ChangePhone.FragmentChangePhone;
import org.nearbyshops.shopkeeperappnew.EditProfile.ChangePhone.FragmentVerifyPhone;
import org.nearbyshops.shopkeeperappnew.EditProfile.FragmentEditProfile;
import org.nearbyshops.shopkeeperappnew.EditShop.EditShopFragment;
import org.nearbyshops.shopkeeperappnew.EditShopImage.EditShopImageFragment;
import org.nearbyshops.shopkeeperappnew.ItemsByCategory.ItemsByCatFragment;
import org.nearbyshops.shopkeeperappnew.ItemsByCategory.ItemsByCatFragmentDeprecated;
import org.nearbyshops.shopkeeperappnew.ItemsInShop.ItemsInShopFragment;
import org.nearbyshops.shopkeeperappnew.ItemsInShopByCat.Adapter;
import org.nearbyshops.shopkeeperappnew.ItemsInShopByCat.ItemsInShopByCatFragment;
import org.nearbyshops.shopkeeperappnew.Login.LoginFragment;
import org.nearbyshops.shopkeeperappnew.Login.LoginGlobalFragment;
import org.nearbyshops.shopkeeperappnew.Login.ServiceIndicatorFragment;
import org.nearbyshops.shopkeeperappnew.MarketDetail.MarketDetailFragment;
import org.nearbyshops.shopkeeperappnew.MarketDetail.RateReviewDialogMarket;
import org.nearbyshops.shopkeeperappnew.Markets.MarketsFragment;
import org.nearbyshops.shopkeeperappnew.Markets.SubmitURLDialog;
import org.nearbyshops.shopkeeperappnew.Markets.AdapterMarkets;
import org.nearbyshops.shopkeeperappnew.Markets.ViewHolders.ViewHolderConnectWithURL;
import org.nearbyshops.shopkeeperappnew.Markets.ViewHolders.ViewHolderMarket;
import org.nearbyshops.shopkeeperappnew.Markets.ViewHolders.ViewHolderSavedMarket;
import org.nearbyshops.shopkeeperappnew.Markets.ViewModels.MarketViewModel;
import org.nearbyshops.shopkeeperappnew.OneSignal.UpdateOneSignalID;
import org.nearbyshops.shopkeeperappnew.OrderDetail.FragmentOrderDetail;
import org.nearbyshops.shopkeeperappnew.OrderHistory.OrderHistoryFragment;
import org.nearbyshops.shopkeeperappnew.OrdersInventory.Fragment.OrdersInventoryFragment;
import org.nearbyshops.shopkeeperappnew.QuickStockEditor.FragmentShopItem;
import org.nearbyshops.shopkeeperappnew.ViewHoldersGeneral.ViewHolderShopItem;
import org.nearbyshops.shopkeeperappnew.SelectDeliveryGuy.SelectDeliveryFragment;
import org.nearbyshops.shopkeeperappnew.Services.UpdateServiceConfiguration;
import org.nearbyshops.shopkeeperappnew.ShopAdminHome.ShopAdminHomeFragment;
import org.nearbyshops.shopkeeperappnew.ShopImageList.ShopImageListFragment;
import org.nearbyshops.shopkeeperappnew.ShopStaffHome.ShopStaffHomeFragment;
import org.nearbyshops.shopkeeperappnew.SignUp.ForgotPassword.FragmentCheckResetCode;
import org.nearbyshops.shopkeeperappnew.SignUp.ForgotPassword.FragmentEnterCredentials;
import org.nearbyshops.shopkeeperappnew.SignUp.ForgotPassword.FragmentResetPassword;
import org.nearbyshops.shopkeeperappnew.SignUp.FragmentEmailOrPhone;
import org.nearbyshops.shopkeeperappnew.SignUp.FragmentEnterPassword;
import org.nearbyshops.shopkeeperappnew.SignUp.FragmentVerify;
import org.nearbyshops.shopkeeperappnew.StaffList.EditProfileStaff.FragmentEditProfileStaff;
import org.nearbyshops.shopkeeperappnew.StaffList.StaffListFragment;
import org.nearbyshops.shopkeeperappnew.StaffListDelivery.DeliveryGuyListFragment;
import org.nearbyshops.shopkeeperappnew.StaffListDelivery.EditProfileDelivery.FragmentEditProfileDelivery;
import org.nearbyshops.shopkeeperappnew.Transactions.TransactionFragment;


import javax.inject.Singleton;

/**
 * Created by sumeet on 14/5/16.
 */




@Singleton
@Component(modules={AppModule.class, NetModule.class})
public interface NetComponent {

    void Inject(LoginFragment loginFragment);

    void Inject(UpdateOneSignalID updateOneSignalID);

    void Inject(ServiceIndicatorFragment serviceIndicatorFragment);

    void Inject(LoginGlobalFragment loginGlobalFragment);

    void Inject(ShopStaffHomeFragment shopStaffHomeFragment);

    void Inject(FragmentChangeEmail fragmentChangeEmail);

    void Inject(FragmentVerifyEmailChange fragmentVerifyEmailChange);

    void Inject(FragmentChangePassword fragmentChangePassword);

    void Inject(FragmentChangePhone fragmentChangePhone);

    void Inject(ShopAdminHomeFragment shopAdminHomeFragment);

    void Inject(TransactionFragment transactionFragment);

    void Inject(EditShopImageFragment editShopImageFragment);

    void Inject(ShopImageListFragment shopImageListFragment);

    void Inject(EditShopFragment editShopFragment);

    void Inject(ViewHolderShopItem viewHolderShopItem);

    void Inject(FragmentShopItem fragmentShopItem);

    void Inject(FragmentEditProfileStaff fragmentEditProfileStaff);

    void Inject(StaffListFragment staffListFragment);

    void Inject(FragmentEnterPassword fragmentEnterPassword);

    void Inject(FragmentCheckResetCode fragmentCheckResetCode);

    void Inject(FragmentEnterCredentials fragmentEnterCredentials);

    void Inject(FragmentResetPassword fragmentResetPassword);

    void Inject(FragmentEmailOrPhone fragmentEmailOrPhone);

    void Inject(FragmentVerify fragmentVerify);

    void Inject(FragmentOrderDetail fragmentOrderDetail);

    void Inject(OrderHistoryFragment orderHistoryFragment);

    void Inject(ItemsByCatFragmentDeprecated itemsByCatFragmentDeprecated);

    void Inject(Adapter adapter);

    void Inject(ItemsInShopByCatFragment itemsInShopByCatFragment);

    void Inject(org.nearbyshops.shopkeeperappnew.ItemsInShop.Adapter adapter);

    void Inject(ItemsInShopFragment itemsInShopFragment);

    void Inject(FragmentEditProfileDelivery fragmentEditProfileDelivery);

    void Inject(DeliveryGuyListFragment deliveryGuyListFragment);

    void Inject(FragmentEditProfile fragmentEditProfile);

    void Inject(FragmentVerifyPhone fragmentVerifyPhone);

    void Inject(ViewHolderSavedMarket viewHolderSavedMarket);

    void Inject(ViewHolderConnectWithURL viewHolderConnectWithURL);

    void Inject(ViewHolderMarket viewHolderMarket);

    void Inject(SubmitURLDialog submitURLDialog);

    void Inject(MarketViewModel marketViewModel);

    void Inject(MarketsFragment marketsFragment);

    void Inject(MarketDetailFragment marketDetailFragment);

    void Inject(RateReviewDialogMarket rateReviewDialogMarket);

    void Inject(AdapterMarkets adapterMarkets);

    void Inject(OrdersInventoryFragment ordersInventoryFragment);

    void Inject(SelectDeliveryFragment selectDeliveryFragment);

    void Inject(DeliveryInventoryFragment deliveryInventoryFragment);

    void Inject(ItemsByCatFragment itemsByCatFragment);

    void Inject(EditItemImageFragment editItemImageFragment);

    void Inject(EditItemFragmentNew editItemFragmentNew);

    void Inject(UpdateServiceConfiguration updateServiceConfiguration);
}
