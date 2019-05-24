package org.nearbyshops.shopkeeperappnew.ShopStaffHome;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import org.nearbyshops.shopkeeperappnew.ItemsByCategoryTypeSimple.ItemsByCatSimple;
import org.nearbyshops.shopkeeperappnew.ItemsInShop.ItemsInShop;
import org.nearbyshops.shopkeeperappnew.ItemsInShopByCat.ItemsInShopByCat;
import org.nearbyshops.shopkeeperappnew.Model.Shop;
import org.nearbyshops.shopkeeperappnew.ModelRoles.ShopStaffPermissions;
import org.nearbyshops.shopkeeperappnew.ModelRoles.User;
import org.nearbyshops.shopkeeperappnew.Prefrences.PrefLogin;
import org.nearbyshops.shopkeeperappnew.Prefrences.PrefShopHome;
import org.nearbyshops.shopkeeperappnew.QuickStockEditor.QuickStockEditor;
import org.nearbyshops.shopkeeperappnew.R;




public class ShopStaffDashboard extends AppCompatActivity {

    @BindView(R.id.border_top) TextView borderTop;
    @BindView(R.id.header_items) TextView headerItems;

    @BindView(R.id.items_by_category) ImageView itemsByCat;
    @BindView(R.id.text_items_by_category) TextView textItemsByCat;

    @BindView(R.id.items) ImageView items;
    @BindView(R.id.text_items)TextView textItems;


    @BindView(R.id.border_items_in_shop) TextView borderItemsInShop;
    @BindView(R.id.header_items_in_shop) TextView headerItemsInShop;

    @BindView(R.id.items_in_shop_by_category) ImageView itemsInShopByCat;
    @BindView(R.id.text_items_in_shop_by_cat) TextView textItemsInShopByCat;

    @BindView(R.id.items_in_shop) ImageView items_in_shop;
    @BindView(R.id.text_items_in_shop) TextView text_items_in_shop;

    @BindView(R.id.quick_stock_editor) ImageView quickStockEditor;
    @BindView(R.id.text_quick_stock_editor_text) TextView textQuickStockEditor;


    @BindView(R.id.border_orders) TextView borderOrders;
    @BindView(R.id.header_orders_and_delivery) TextView headerOrders;
    @BindView(R.id.orders_home_delivery) ImageView home_delivery;
    @BindView(R.id.text_home_delivery_orders) TextView text_home_delivery;

    @BindView(R.id.orders_pick_from_shop) ImageView pickFromShopOrders;
    @BindView(R.id.text_pick_from_shop) TextView textPickFromShop;


    @BindView(R.id.border_bottom) TextView borderBottom;

//    ShopStaff staff;

    User user;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_dashboard);
        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        staff = PrefLogin.getShopStaff(this);
//        setupDashboard();


        user = PrefLogin.getUser(this);
        ShopStaffPermissions permissions = user.getRt_shop_staff_permissions();


        Shop shop = new Shop();
        shop.setShopID(permissions.getShopID());
        PrefShopHome.saveShop(shop,this);

        setupDashboardNew();
    }



//    void setupDashboard()
//    {
//        if(!staff.getEnabled())
//        {
//            return;
//        }
//
//        if(staff.isAddRemoveItemsFromShop() || staff.isAddRemoveItemsFromShop())
//        {
//            borderTop.setVisibility(View.VISIBLE);
//            headerItems.setVisibility(View.VISIBLE);
//
//            itemsByCat.setVisibility(View.VISIBLE);
//            textItemsByCat.setVisibility(View.VISIBLE);
//
//            items.setVisibility(View.VISIBLE);
//            textItems.setVisibility(View.VISIBLE);
//        }
//
//
//        if(staff.isUpdateStock())
//        {
//            borderItemsInShop.setVisibility(View.VISIBLE);
//            headerItemsInShop.setVisibility(View.VISIBLE);
//
//            itemsInShopByCat.setVisibility(View.VISIBLE);
//            textItemsInShopByCat.setVisibility(View.VISIBLE);
//
//            items_in_shop.setVisibility(View.VISIBLE);
//            text_items_in_shop.setVisibility(View.VISIBLE);
//
//            quickStockEditor.setVisibility(View.VISIBLE);
//            textQuickStockEditor.setVisibility(View.VISIBLE);
//            borderBottom.setVisibility(View.VISIBLE);
//        }
//
//
//        if(staff.isCancelOrders()||
//                staff.isConfirmOrders()||
//                staff.isSetOrdersPacked()||
//                staff.isHandoverToDelivery()||
//                staff.isMarkOrdersDelivered()||
//                staff.isAcceptPaymentsFromDelivery()||
//                staff.isAcceptReturns())
//        {
//            borderOrders.setVisibility(View.VISIBLE);
//            headerOrders.setVisibility(View.VISIBLE);
//            home_delivery.setVisibility(View.VISIBLE);
//            text_home_delivery.setVisibility(View.VISIBLE);
//            borderBottom.setVisibility(View.VISIBLE);
//        }
//
//
//
//        if(staff.isPermitCancelOrdersPFS() ||
//                staff.isPermitConfirmOrdersPFS()||
//                staff.isPermitSetOrdersPackedPFS()||
//                staff.isPermitSetReadyForPickupPFS()||
//                staff.isPermitSetPaymentReceivedPFS()||
//                staff.isPermitMarkDeliveredPFS())
//        {
//            borderOrders.setVisibility(View.VISIBLE);
//            headerOrders.setVisibility(View.VISIBLE);
//            pickFromShopOrders.setVisibility(View.VISIBLE);
//            textPickFromShop.setVisibility(View.VISIBLE);
//            borderBottom.setVisibility(View.VISIBLE);
//        }
//
//
//    }



    void setupDashboardNew()
    {
            borderTop.setVisibility(View.VISIBLE);
            headerItems.setVisibility(View.VISIBLE);

            itemsByCat.setVisibility(View.VISIBLE);
            textItemsByCat.setVisibility(View.VISIBLE);

            items.setVisibility(View.VISIBLE);
            textItems.setVisibility(View.VISIBLE);


              borderItemsInShop.setVisibility(View.VISIBLE);
            headerItemsInShop.setVisibility(View.VISIBLE);

            itemsInShopByCat.setVisibility(View.VISIBLE);
            textItemsInShopByCat.setVisibility(View.VISIBLE);

            items_in_shop.setVisibility(View.VISIBLE);
            text_items_in_shop.setVisibility(View.VISIBLE);

            quickStockEditor.setVisibility(View.VISIBLE);
            textQuickStockEditor.setVisibility(View.VISIBLE);
            borderBottom.setVisibility(View.VISIBLE);



            borderOrders.setVisibility(View.VISIBLE);
            headerOrders.setVisibility(View.VISIBLE);
            home_delivery.setVisibility(View.VISIBLE);
            text_home_delivery.setVisibility(View.VISIBLE);
            borderBottom.setVisibility(View.VISIBLE);


            borderOrders.setVisibility(View.VISIBLE);
            headerOrders.setVisibility(View.VISIBLE);
            pickFromShopOrders.setVisibility(View.VISIBLE);
            textPickFromShop.setVisibility(View.VISIBLE);
            borderBottom.setVisibility(View.VISIBLE);
    }







//    @OnClick(R.id.items)
//    void optionItemsClick()
//    {
//        startActivity(new Intent(this, ItemsTypeSimple.class));
//    }


    @OnClick(R.id.items_by_category)
    void optionItemCatApprovals()
    {
        startActivity(new Intent(this, ItemsByCatSimple.class));
    }



    @OnClick(R.id.quick_stock_editor)
    void itemCategoriesClick(View view)
    {
        startActivity(new Intent(this, QuickStockEditor.class));
    }




    @OnClick(R.id.items_in_shop)
    void optionAdminClick(View view)
    {
        startActivity(new Intent(this, ItemsInShop.class));
    }



    @OnClick(R.id.items_in_shop_by_category)
    void distributorAccountClick(View view)
    {
        startActivity(new Intent(this, ItemsInShopByCat.class));
    }







//    @OnClick(R.id.orders_home_delivery)
//    void homeDeliveryClick()
//    {
//        startActivity(new Intent(this, OrdersHomeDelivery.class));
//    }
//
//
//    @OnClick(R.id.orders_pick_from_shop)
//    void pickFromShopClick()
//    {
//        startActivity(new Intent(this, OrdersPickFromShop.class));
//    }
//



}
