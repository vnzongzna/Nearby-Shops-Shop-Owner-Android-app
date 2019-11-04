package org.nearbyshops.shopkeeperappnew;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.RemoteMessage;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.nearbyshops.shopkeeperappnew.DeliveryGuyHome.DeliveryGuyHomeFragment;
import org.nearbyshops.shopkeeperappnew.LoginPlaceholders.FragmentSignInMessage;
import org.nearbyshops.shopkeeperappnew.Markets.Interfaces.MarketSelected;
import org.nearbyshops.shopkeeperappnew.Markets.MarketsFragment;
import org.nearbyshops.shopkeeperappnew.ModelEventBus.NotificationEvent;
import org.nearbyshops.shopkeeperappnew.Interfaces.LocationUpdated;
import org.nearbyshops.shopkeeperappnew.Interfaces.NotifyAboutLogin;
import org.nearbyshops.shopkeeperappnew.ModelRoles.User;
import org.nearbyshops.shopkeeperappnew.OneSignal.PrefOneSignal;
import org.nearbyshops.shopkeeperappnew.OneSignal.UpdateOneSignalID;
import org.nearbyshops.shopkeeperappnew.Prefrences.PrefGeneral;
import org.nearbyshops.shopkeeperappnew.Prefrences.PrefLocation;
import org.nearbyshops.shopkeeperappnew.Prefrences.PrefLogin;
import org.nearbyshops.shopkeeperappnew.Services.UpdateServiceConfiguration;
import org.nearbyshops.shopkeeperappnew.ShopAdminHome.ShopAdminHomeFragment;
import org.nearbyshops.shopkeeperappnew.ShopStaffHome.ShopStaffHomeFragment;
import org.nearbyshops.shopkeeperappnew.Utility.UtilityFunctions;


public class Home extends AppCompatActivity implements NotifyAboutLogin, MarketSelected {



    public static final String TAG_LOGIN = "tag_login";
    public static final String TAG_MARKETS = "tag_markets";

    public static final String TAG_PROFILE = "tag_profile_fragment";


    // fragments
//    VehiclesFragment vehiclesFragment;
//    ProfileFragmentOld profileFragment;



    LocationManager locationManager;
    LocationListener locationListener;



    boolean isFirstLaunch = true;



    public Home() {
//
//        DaggerComponentBuilder.getInstance()
//                .getNetComponent()
//                .Inject(this);
    }





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_new);


        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);

        showDashboard();


        startService(new Intent(this, UpdateServiceConfiguration.class));


        checkPermissions();
        fetchLocation();



        if(PrefGeneral.getServiceURL(this)!=null && PrefOneSignal.getToken(this)!=null)
        {
            startService(new Intent(getApplicationContext(), UpdateOneSignalID.class));
        }



        FirebaseApp.initializeApp(getApplicationContext());

        UtilityFunctions.updateFirebaseSubscriptions();




//        FirebaseApp.getInstance().delete();


//        _192.168.43.73_5121_




//        String topic =   "_192.168.43.73_5121_weather" ;
//
//
//        FirebaseMessaging.getInstance().subscribeToTopic(topic)
//                .addOnCompleteListener(new OnCompleteListener<Void>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Void> task) {
//                        String msg = "Subscribed";
//                        if (!task.isSuccessful()) {
//                            msg = "Failed";
//                        }
////                        Log.d(TAG, msg);
//                        Toast.makeText(Home.this, msg, Toast.LENGTH_SHORT).show();
//                    }
//                });

    }







    void checkPermissions() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.


            //
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    2);
            return;
        }


//        fetchLocation();
    }






    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        //
//        if(requestCode==2)
//        {
//            // If request is cancelled, the result arrays are empty.
//
//
        if (grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            // permission was granted, yay! Do the
            // contacts-related task you need to do.
            //                startService(new Intent(this,LocationUpdateServiceLocal.class));

            showToastMessage("Permission Granted !");

//            Fragment fragment = getSupportFragmentManager().findFragmentByTag(TAG_ITEMS_FRAGMENT);


//            Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
//
//
//            if (fragment instanceof LocationUpdated) {
//                ((LocationUpdated) fragment).permissionGranted();
//            }


            fetchLocation();

        } else {
            // permission denied, boo! Disable the
            // functionality that depends on this permission.
            showToastMessage("Permission Rejected");
        }


    }











    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }


    @Override
    protected void onStop() {
        super.onStop();

//        stopService(new Intent(this,LocationUpdateServiceLocal.class));
        EventBus.getDefault().unregister(this);
    }






    @Override
    public void loginSuccess() {


        showDashboard();
    }

    @Override
    public void loggedOut() {

    }






    public void showLoginFragment()
    {


        if(PrefGeneral.getServiceURL(this)==null)
        {
            if(getSupportFragmentManager().findFragmentByTag(TAG_MARKETS)==null)
            {
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container,new MarketsFragment(),TAG_MARKETS)
                        .commit();
            }
        }
        else if(getSupportFragmentManager().findFragmentByTag(TAG_LOGIN)==null)
        {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container,new FragmentSignInMessage(),TAG_LOGIN)
                    .commit();
        }
    }






    public void showDashboard() {

        User user = PrefLogin.getUser(getBaseContext());


        if (user == null) {
            showLoginFragment();

            return;
        }





        if (user.getRole() == User.ROLE_SHOP_STAFF_CODE) {

            if (getSupportFragmentManager().findFragmentByTag(TAG_PROFILE) == null) {
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, new ShopStaffHomeFragment(), TAG_PROFILE)
                        .commit();
            }

        } else if (user.getRole() == User.ROLE_SHOP_ADMIN_CODE) {

            if (getSupportFragmentManager().findFragmentByTag(TAG_PROFILE) == null) {
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, new ShopAdminHomeFragment(), TAG_PROFILE)
                        .commit();
            }
        }
        else if (user.getRole() == User.ROLE_DELIVERY_GUY_CODE || user.getRole() == User.ROLE_DELIVERY_GUY_SELF_CODE)
        {
            if (getSupportFragmentManager().findFragmentByTag(TAG_PROFILE) == null) {
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, new DeliveryGuyHomeFragment(), TAG_PROFILE)
                        .commit();
            }

        }





    }






    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopLocationUpdates();
    }






    void fetchLocation() {

        // Acquire a reference to the system Location Manager
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        // Define a listener that responds to location updates
        locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                // Called when a new location is found by the network location provider.

                saveLocation(location);
                stopLocationUpdates();
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            public void onProviderEnabled(String provider) {
            }

            public void onProviderDisabled(String provider) {
            }
        };


        // Register the listener with the Location Manager to receive location updates
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }


        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);



        if(location==null)
        {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 100, locationListener);
        }
        else
        {
            saveLocation(location);
        }

    }










    void saveLocation(Location location)
    {


        Location currentLocation = PrefLocation.getLocation(this);


//        showToast("Distance Change : " + currentLocation.distanceTo(location));

        if(currentLocation.distanceTo(location)>100)
        {
            // save location only if there is a significant change in location

            PrefLocation.saveLatitude((float) location.getLatitude(), Home.this);
            PrefLocation.saveLongitude((float) location.getLongitude(), Home.this);


//        showToast("Home : Location Updated");


            Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);

            if (fragment instanceof LocationUpdated) {
                ((LocationUpdated) fragment).locationUpdated();
            }

        }

    }





    protected void stopLocationUpdates() {



//        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        if(locationManager!=null && locationListener!=null)
        {
            locationManager.removeUpdates(locationListener);
        }

//        stopSelf();
    }









    void showToastMessage(String message)
    {
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
    }


    boolean isDestroyed = false;



//    int badgeCountTripRequests = 0;
//    int badgeCountCurrentTrips = 0;


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(NotificationEvent event) {/* Do something */

    }


    @Override
    public void marketSelected() {
        showDashboard();
    }


}



