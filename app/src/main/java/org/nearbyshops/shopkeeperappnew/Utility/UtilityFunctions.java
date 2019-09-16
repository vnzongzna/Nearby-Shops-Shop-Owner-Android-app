package org.nearbyshops.shopkeeperappnew.Utility;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.nearbyshops.shopkeeperappnew.Home;

import static com.mapbox.mapboxsdk.Mapbox.getApplicationContext;

/**
 * Created by sumeet on 10/7/17.
 */

public class UtilityFunctions {


    /* Utility Functions */

    public static Gson provideGson() {

        GsonBuilder gsonBuilder = new GsonBuilder();
        //gsonBuilder.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES);
        return gsonBuilder
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                .create();

//        .setDateFormat("yyyy-MM-dd hh:mm:ss.S")
    }






    public static void updateFirebaseSubscriptions(int shop_id)
    {
//        FirebaseApp.getInstance().delete();
//        FirebaseApp.initializeApp(getApplicationContext());

        String topic_name = "shop_" + shop_id;

        FirebaseMessaging.getInstance().subscribeToTopic(topic_name)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        System.out.println("Subscribed to : " + topic_name);

                    }
                });

    }





}
