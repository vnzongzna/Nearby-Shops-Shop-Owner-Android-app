package org.nearbyshops.shopkeeperappnew.Prefrences;

import android.content.Context;
import android.content.SharedPreferences;
import org.nearbyshops.shopkeeperappnew.MyApplication;
import org.nearbyshops.shopkeeperappnew.R;

import static android.content.Context.MODE_PRIVATE;




/**
 * Created by sumeet on 5/5/16.
 */
public class PrefGeneral {

    private static final String TAG_PREF_CURRENCY = "currency_symbol";
    public static final String TAG_SERVICE_URL = "service_url_shop_owner";


    public static final String SERVICE_URL_LOCAL_HOTSPOT = "http://192.168.43.73:5121";
    public static final String SERVICE_URL_LOCAL = "http://192.168.0.5:5120";
    public static final String SERVICE_URL_NEARBYSHOPS = "http://api.nearbyshops.org";
    public static final String SERVICE_URL_NEARBYSHOPS_DEMO = "http://api-demo.nearbyshops.org";





    // put service_url = null to show users the option to select markets
    public static final String SERVICE_URL = SERVICE_URL_LOCAL_HOTSPOT;





    public static String getServiceURL(Context context) {

        context = MyApplication.getAppContext();

        SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.preference_file_name), Context.MODE_PRIVATE);
       return sharedPref.getString(TAG_SERVICE_URL, SERVICE_URL);
    }



    public static void saveServiceURL(String service_url,Context context)
    {

        context = MyApplication.getAppContext();
        // get a handle to shared Preference
        SharedPreferences sharedPref;

        sharedPref = context.getSharedPreferences(
                context.getString(R.string.preference_file_name),
                context.MODE_PRIVATE);

        // write to the shared preference
        SharedPreferences.Editor editor = sharedPref.edit();

        editor.putString(
                TAG_SERVICE_URL,
                service_url);

        editor.apply();
    }



    public static String getImageEndpointURL(Context context)
    {
        return PrefGeneral.getServiceURL(context) + "/api/Images";
    }




    public static void saveCurrencySymbol(String symbol, Context context)
    {
        //Creating a shared preference
        SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.preference_file_name), MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = sharedPref.edit();
        prefsEditor.putString(TAG_PREF_CURRENCY, symbol);
        prefsEditor.apply();
    }




    public static String getCurrencySymbol(Context context)
    {
        SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.preference_file_name), MODE_PRIVATE);
        return sharedPref.getString(TAG_PREF_CURRENCY, context.getString(R.string.rupee_symbol));
    }


}
