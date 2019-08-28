package org.nearbyshops.shopkeeperappnew.DaggerModules;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import org.nearbyshops.shopkeeperappnew.API.*;
import org.nearbyshops.shopkeeperappnew.MyApplication;
import org.nearbyshops.shopkeeperappnew.Prefrences.PrefGeneral;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import javax.inject.Singleton;

/**
 * Created by sumeet on 14/5/16.
 */

        /*
        retrofit = new Retrofit.Builder()
                .baseUrl(UtilityGeneral.getServiceURL(context))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        */

@Module
public class NetModule {

    String serviceURL;

    // Constructor needs one parameter to instantiate.
    public NetModule() {

    }

    // Dagger will only look for methods annotated with @Provides
    @Provides
    @Singleton
    // Application reference must come from AppModule.class
    SharedPreferences providesSharedPreferences(Application application) {
        return PreferenceManager.getDefaultSharedPreferences(application);
    }

    /*
    @Provides
    @Singleton
    Cache provideOkHttpCache(Application application) {
        int cacheSize = 10 * 1024 * 1024; // 10 MiB
        Cache cache = new Cache(application.getCacheDir(), cacheSize);
        return cache;
    }

    */

    @Provides
    @Singleton
    Gson provideGson() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        //gsonBuilder.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES);
        return gsonBuilder
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                .create();


//        .setDateFormat("yyyy-MM-dd hh:mm:ss.S")
    }

    @Provides
    @Singleton
    OkHttpClient provideOkHttpClient() {

        OkHttpClient client = new OkHttpClient()
                .newBuilder()
                .build();

        // Cache cache

        // cache is commented out ... you can add cache by putting it back in the builder options
        //.cache(cache)

        return client;
    }


    //    @Singleton

    @Provides
    Retrofit provideRetrofit(Gson gson, OkHttpClient okHttpClient) {

        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(okHttpClient)
                .baseUrl(PrefGeneral.getServiceURL(MyApplication.getAppContext()))
                .build();

        //        .client(okHttpClient)

//        Log.d("applog","Retrofit : " + UtilityGeneral.getServiceURL(MyApplication.getAppContext()));


        return retrofit;
    }






//    @Provides
//    OrderService provideOrderService(Retrofit retrofit)
//    {
//
//        return retrofit.create(OrderService.class);
//    }



    @Provides
    ShopItemService provideShopItemService(Retrofit retrofit)
    {

        return retrofit.create(ShopItemService.class);
    }





    @Provides
    UserService provideUser(Retrofit retrofit)
    {
        return retrofit.create(UserService.class);
    }




    @Provides
    ItemService provideItemService(Retrofit retrofit)
    {
        ItemService service = retrofit.create(ItemService.class);

        return service;
    }

    @Provides
    ItemCategoryService provideItemCategory(Retrofit retrofit)
    {

        return retrofit.create(ItemCategoryService.class);
    }


    @Provides
    ShopService provideShopService(Retrofit retrofit)
    {

        return retrofit.create(ShopService.class);
    }





    @Provides
    OrderItemService provideOrderItemService(Retrofit retrofit)
    {
        return retrofit.create(OrderItemService.class);
    }


    @Provides
    OrderServiceShopStaff provideOrderShopStaff(Retrofit retrofit)
    {
        return retrofit.create(OrderServiceShopStaff.class);
    }




    @Provides
    ShopStaffLoginService provideShopStaffLoginService(Retrofit retrofit)
    {
        return retrofit.create(ShopStaffLoginService.class);
    }





    @Provides
    DeliveryGuyLoginService provideDeliveryGuyLoginService(Retrofit retrofit)
    {
        return retrofit.create(DeliveryGuyLoginService.class);
    }


    @Provides
    ShopImageService provideShopImage(Retrofit retrofit)
    {
        return retrofit.create(ShopImageService.class);
    }


    @Provides
    TransactionService provideTransactionService(Retrofit retrofit)
    {
        return retrofit.create(TransactionService.class);
    }




    @Provides
    ServiceConfigurationService provideServiceConfigurationService(Retrofit retrofit)
    {
        return retrofit.create(ServiceConfigurationService.class);
    }




    @Provides
    OrderServiceDeliveryPersonSelf provideOrderServiceDeliveryPerson(Retrofit retrofit)
    {
        return retrofit.create(OrderServiceDeliveryPersonSelf.class);
    }






    @Provides
    ItemImageService provideItemImageService(Retrofit retrofit)
    {
        return retrofit.create(ItemImageService.class);
    }




    @Provides
    ItemSpecNameService provideItemSpecNameService(Retrofit retrofit)
    {
        return retrofit.create(ItemSpecNameService.class);
    }

}
