package org.nearbyshops.shopkeeperappnew.Prefrences;

import android.content.Context;
import android.content.SharedPreferences;
import com.google.gson.Gson;
import org.nearbyshops.shopkeeperappnew.Model.Shop;
import org.nearbyshops.shopkeeperappnew.R;
import org.nearbyshops.shopkeeperappnew.Utility.UtilityFunctions;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by sumeet on 19/10/16.
 */



public class PrefShopHome {


    public static void saveShop(Shop shop, Context context)
    {

        //Creating a shared preference

        SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.preference_file_name), MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = sharedPref.edit();

//        if(shop == null)
//        {
//            prefsEditor.putString("shop", "null");
//
//        }
//        else
//        {
//        }

        Gson gson = UtilityFunctions.provideGson();
        String json = gson.toJson(shop);
        prefsEditor.putString("shop_profile", json);

        prefsEditor.apply();
    }


    public static Shop getShop(Context context)
    {
        SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.preference_file_name), MODE_PRIVATE);

        Gson gson = UtilityFunctions.provideGson();
        String json = sharedPref.getString("shop_profile", null);

        return gson.fromJson(json, Shop.class);
    }


}
