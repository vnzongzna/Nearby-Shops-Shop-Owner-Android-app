package org.nearbyshops.shopkeeperappnew;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.multidex.MultiDex;
import androidx.multidex.MultiDexApplication;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.messaging.FirebaseMessaging;
import com.mapbox.mapboxsdk.Mapbox;

import org.json.JSONObject;
import org.nearbyshops.shopkeeperappnew.ApplicationState.ApplicationState;
import org.nearbyshops.shopkeeperappnew.OneSignal.PrefOneSignal;
import org.nearbyshops.shopkeeperappnew.OneSignal.UpdateOneSignalID;
import org.nearbyshops.shopkeeperappnew.Prefrences.PrefGeneral;

//import com.squareup.leakcanary.LeakCanary;
//import com.onesignal.OSNotification;
//import com.onesignal.OSNotificationOpenResult;
//import com.onesignal.OneSignal;

/**
 * Created by sumeet on 12/5/16.
 */
public class MyApplication extends MultiDexApplication {


    @Override
    protected void attachBaseContext(Context base)
    {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }







    private static Context context;

    public void onCreate() {

        super.onCreate();

        MyApplication.context = getApplicationContext();

//        LeakCanary.install(this);

        ApplicationState.getInstance().setMyApplication(this);



//
//
//        OneSignal.setLogLevel(OneSignal.LOG_LEVEL.DEBUG,OneSignal.LOG_LEVEL.DEBUG);
//
//        OneSignal.startInit(this)
//                .setNotificationReceivedHandler(new OneSignal.NotificationReceivedHandler() {
//                    @Override
//                    public void notificationReceived(OSNotification notification) {
//
//
////                        Log.d("one_signal",notification.toJSONObject().toString());
//
//                        Log.d("one_signal",notification.payload.additionalData.toString());
//
//                        JSONObject data = notification.payload.additionalData;
//
//
////                        int screenToOpen = -1;
//                        int notificationType = -1;
//
//
//                        if (data != null) {
//
////                            screenToOpen = data.optInt("screen_to_open",-1);
//                            notificationType = data.optInt("notification_type",-1);
//
//
//
//
//
//
//                            NotificationEvent event = new NotificationEvent(notificationType);
//                            EventBus.getDefault().post(event);
//
//                        }
//
//
//
//
//                    }
//                })
//                .setNotificationOpenedHandler(new OneSignal.NotificationOpenedHandler() {
//                    @Override
//                    public void notificationOpened(OSNotificationOpenResult result) {
//
//                        JSONObject data = result.notification.payload.additionalData;
//
////                        Gson gson = UtilityFunctions.provideGson();
////                        OneSignalData oneSignalData= gson.fromJson(data.toString(), OneSignalData.class);
//
//                        int screenToOpen = -1;
//                        int notificationType = -1;
//
//
//                        if (data != null) {
//                            screenToOpen = data.optInt("screen_to_open",-1);
//                            notificationType = data.optInt("notification_type",-1);
//
//                            if (screenToOpen != -1)
//                                Log.i("one_signal", "Screen To Open: " + String.valueOf(screenToOpen));
//                            Log.i("one_signal", "Notificaion Type : " + String.valueOf(notificationType));
//                        }
//
//
//
//
//                        Intent intent = new Intent(getApplicationContext(), Home.class);
//                        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK);
//
//                        intent.putExtra("screen_to_open",screenToOpen);
//
//                        startActivity(intent);
//
//                        Log.d("one_signal","Notification Opened !");
//
//
//
//                    }
//                })
//                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
//                .unsubscribeWhenNotificationsAreDisabled(true)
//                .init();
//
//
//
//
//        OneSignal.idsAvailable(new OneSignal.IdsAvailableHandler() {
//            @Override
//            public void idsAvailable(String userId, String registrationId) {
//
//
//                Log.d("one_signal", "User:" + userId);
//                Log.d("one_signal", "registrationId:" + registrationId);
//
//
//                PrefOneSignal.saveLastToken(getApplicationContext(),userId);
//                PrefOneSignal.saveToken(getApplicationContext(),userId);
//
//
//
//                if (PrefGeneral.getServiceURL(getApplicationContext())!=null) {
//                    startService(new Intent(getApplicationContext(), UpdateOneSignalID.class));
//                }
//
//
////                getApplicationContext().startService(new Intent(getApplicationContext(), UpdateOneSignalID.class));
//            }
//
//        });






        Mapbox.getInstance(this,getString(R.string.fake_key));


    }


    public static Context getAppContext() {

        return MyApplication.context;


    }


}
