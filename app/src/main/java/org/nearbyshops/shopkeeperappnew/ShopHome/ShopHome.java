package org.nearbyshops.shopkeeperappnew.ShopHome;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import butterknife.ButterKnife;
import butterknife.OnClick;
import org.nearbyshops.shopkeeperappnew.OrdersInventory.HomeDeliveryInventory.HomeDelivery;
import org.nearbyshops.shopkeeperappnew.ItemsByCategory.ItemsByCategory;
import org.nearbyshops.shopkeeperappnew.ItemsInShop.ItemsInShop;
import org.nearbyshops.shopkeeperappnew.ItemsInShopByCat.ItemsInShopByCat;
import org.nearbyshops.shopkeeperappnew.Model.Shop;
import org.nearbyshops.shopkeeperappnew.OrderHistory.OrderHistory;
import org.nearbyshops.shopkeeperappnew.OrdersInventory.PickFromShopInventory.PickFromShopInventory;
import org.nearbyshops.shopkeeperappnew.Prefrences.PrefShopHome;
import org.nearbyshops.shopkeeperappnew.QuickStockEditor.QuickStockEditor;
import org.nearbyshops.shopkeeperappnew.R;
import org.nearbyshops.shopkeeperappnew.StaffList.StaffList;
import org.nearbyshops.shopkeeperappnew.StaffListDelivery.DeliveryGuyList;

public class ShopHome extends AppCompatActivity {

    public static final String SHOP_ID_INTENT_KEY = "shop_id_key";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_home);
        ButterKnife.bind(this);



        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setupNotifications();
    }







    @OnClick(R.id.option_orders)
    void ordersClick()
    {

            startActivity(new Intent(this, HomeDelivery.class));
    }





    @OnClick(R.id.option_orders_pick_from_shop)
    void ordersPickFromShop()
    {
//        startActivity(new Intent(this, OrdersPickFromShop.class));
        startActivity(new Intent(this, PickFromShopInventory.class));
    }




    @OnClick(R.id.shop_home_order_history)
    void orderHistory()
    {
        startActivity(new Intent(this, OrderHistory.class));
    }








    @OnClick(R.id.shop_home_quick_stock_editor)
    void quickStockEditorClick(View view)
    {
        startActivity(new Intent(this, QuickStockEditor.class));

    }






    @OnClick(R.id.option_edit_stock)
    void editStockClick(View view)
    {
//        startActivity(new Intent(this, EditStock.class));
        startActivity(new Intent(this, ItemsInShopByCat.class));
    }

//    @OnClick(R.id.option_billing)
//    void billingClick(View view)
//    {
//        showToastMessage("Feature coming soon !");
//    }

//    @OnClick(R.id.option_shop_stats)
//    void optionShopStats()
//    {
//        showToastMessage("Feature coming soon !");
//    }






    @OnClick(R.id.option_delivery_guy_accounts)
    void DeliveryAccountsClick(View view)
    {
//        startActivity(new Intent(this, DeliveryGuyAccounts.class));
        startActivity(new Intent(this, DeliveryGuyList.class));
    }




    @OnClick(R.id.option_add_items)
    void optionItemsByCategory()
    {
        startActivity(new Intent(this, ItemsByCategory.class));
    }



    @OnClick(R.id.option_items_in_stock)
    void optionItemsInStock()
    {
        startActivity(new Intent(this, ItemsInShop.class));
    }



    @OnClick(R.id.option_staff_accounts)
    void optionStaffAccounts()
    {

//        startActivity(new Intent(this, ShopStaffAccounts.class));
        startActivity(new Intent(this, StaffList.class));
    }




    void setupNotifications()
    {
        Shop shop = PrefShopHome.getShop(this);

        if(shop!=null)
        {
            int currentapiVersion = Build.VERSION.SDK_INT;

            if (currentapiVersion >= Build.VERSION_CODES.KITKAT){
                // Do something for lollipop and above versions

//                Intent intent = new Intent(this, SSEIntentService.class);
//                intent.putExtra(SSEIntentService.SHOP_ID, shop.getShopID());
//                startService(intent);
            }
        }
    }




    void showToastMessage(String message)
    {
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
    }

}
