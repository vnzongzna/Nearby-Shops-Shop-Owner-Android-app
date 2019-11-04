package org.nearbyshops.shopkeeperappnew.Services;

import android.app.IntentService;
import android.content.Intent;

import androidx.annotation.Nullable;

import com.google.gson.Gson;


import org.nearbyshops.shopkeeperappnew.API.ServiceConfigurationService;
import org.nearbyshops.shopkeeperappnew.DaggerComponentBuilder;
import org.nearbyshops.shopkeeperappnew.Markets.Model.ServiceConfigurationLocal;
import org.nearbyshops.shopkeeperappnew.MyApplication;
import org.nearbyshops.shopkeeperappnew.Prefrences.PrefGeneral;
import org.nearbyshops.shopkeeperappnew.Prefrences.PrefServiceConfig;

import java.util.Currency;
import java.util.Locale;

import javax.inject.Inject;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UpdateServiceConfiguration extends IntentService {




    @Inject
    Gson gson;

//    @Inject
//    ServiceConfigurationService service;



    public UpdateServiceConfiguration() {
        super("update_service_configuration");

        DaggerComponentBuilder.getInstance()
                .getNetComponent().Inject(this);
    }



    @Override
    protected void onHandleIntent(@Nullable Intent intent) {


        getLocalConfig();
    }




    void getLocalConfig()
    {


        if(PrefGeneral.getServiceURL(getApplicationContext())==null)
        {
            return;
        }


        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .baseUrl(PrefGeneral.getServiceURL(MyApplication.getAppContext()))
                .client(new OkHttpClient().newBuilder().build())
                .build();





        ServiceConfigurationService service = retrofit.create(ServiceConfigurationService.class);

        Call<ServiceConfigurationLocal> call = service.getServiceConfiguration(0.0,0.0);





        call.enqueue(new Callback<ServiceConfigurationLocal>() {
            @Override
            public void onResponse(Call<ServiceConfigurationLocal> call, Response<ServiceConfigurationLocal> response) {



                if(response.code()==200)
                {
                    PrefServiceConfig.saveServiceConfigLocal(response.body(),getApplicationContext());



                    ServiceConfigurationLocal config = response.body();

                    if(config!=null)
                    {
                        Currency currency = Currency.getInstance(new Locale("",config.getISOCountryCode()));
                        PrefGeneral.saveCurrencySymbol(currency.getSymbol(),getApplicationContext());
                    }
                }
                else
                {
                    PrefServiceConfig.saveServiceConfigLocal(null,getApplicationContext());
                }
            }



            @Override
            public void onFailure(Call<ServiceConfigurationLocal> call, Throwable t) {

                PrefServiceConfig.saveServiceConfigLocal(null,getApplicationContext());
            }
        });

    }






}
