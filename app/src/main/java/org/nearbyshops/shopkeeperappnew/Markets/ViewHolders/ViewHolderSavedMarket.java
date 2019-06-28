package org.nearbyshops.shopkeeperappnew.Markets.ViewHolders;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import okhttp3.OkHttpClient;
import org.nearbyshops.shopkeeperappnew.API.ServiceConfigurationService;
import org.nearbyshops.shopkeeperappnew.DaggerComponentBuilder;
import org.nearbyshops.shopkeeperappnew.Markets.Interfaces.listItemMarketNotifications;
import org.nearbyshops.shopkeeperappnew.Markets.Model.ServiceConfigurationGlobal;
import org.nearbyshops.shopkeeperappnew.Markets.Model.ServiceConfigurationLocal;
import org.nearbyshops.shopkeeperappnew.Prefrences.PrefGeneral;
import org.nearbyshops.shopkeeperappnew.Prefrences.PrefServiceConfig;
import org.nearbyshops.shopkeeperappnew.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import javax.inject.Inject;
import java.util.Currency;
import java.util.Locale;

public class ViewHolderSavedMarket extends RecyclerView.ViewHolder {


    @BindView(R.id.market_photo) ImageView marketPhoto;
    @BindView(R.id.market_name) TextView marketName;
    @BindView(R.id.market_city) TextView marketCity;

    @BindView(R.id.progress_bar_select) ProgressBar progressBarSelect;
    @BindView(R.id.select_market) TextView selectMarket;



    private ServiceConfigurationGlobal configurationGlobal;
    private Context context;


    private listItemMarketNotifications subscriber;


    @Inject Gson gson;






    public static ViewHolderSavedMarket create(ViewGroup parent, Context context, listItemMarketNotifications subscriber)
    {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_market_saved_type_two,parent,false);

        return new ViewHolderSavedMarket(view,context,subscriber);
    }




    public ViewHolderSavedMarket(@NonNull View itemView, Context context, listItemMarketNotifications subscriber) {
        super(itemView);
        ButterKnife.bind(this,itemView);

        this.context = context;
        this.subscriber = subscriber;

        DaggerComponentBuilder.getInstance()
                .getNetComponent()
                .Inject(this);
    }




    public void setItem(ServiceConfigurationGlobal item)
    {

        this.configurationGlobal = item;


        marketName.setText(configurationGlobal.getServiceName());
        marketCity.setText(configurationGlobal.getCity());


        String imagePath = PrefServiceConfig.getServiceURL_SDS(context)
                + "/api/v1/ServiceConfiguration/Image/three_hundred_" + configurationGlobal.getLogoImagePath() + ".jpg";


//                System.out.println("Service LOGO : " + imagePath);

        Drawable placeholder = VectorDrawableCompat
                .create(context.getResources(),
                        R.drawable.ic_nature_people_white_48px, context.getTheme());


        Picasso.get()
                .load(imagePath)
                .placeholder(placeholder)
                .into(marketPhoto);

    }









    @OnClick(R.id.list_item)
    void listItemClick()
    {
        subscriber.listItemClick(configurationGlobal,getLayoutPosition());
    }





    @OnClick(R.id.select_market)
    void selectMarket()
    {


        ServiceConfigurationGlobal configurationGlobal = this.configurationGlobal;

        fetchConfiguration(configurationGlobal);
    }





    void fetchConfiguration(final ServiceConfigurationGlobal configurationGlobal)
    {

//            PrefGeneral.saveServiceURL(configurationGlobal.getServiceURL(),getApplicationContext());
//            PrefServiceConfig.saveServiceConfigLocal(null,getApplicationContext());



//            PrefGeneral.getServiceURL(MyApplication.getAppContext())


        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .baseUrl(configurationGlobal.getServiceURL())
                .client(new OkHttpClient().newBuilder().build())
                .build();




        ServiceConfigurationService service = retrofit.create(ServiceConfigurationService.class);

        Call<ServiceConfigurationLocal> call = service.getServiceConfiguration(0.0,0.0);




        selectMarket.setVisibility(View.INVISIBLE);
        progressBarSelect.setVisibility(View.VISIBLE);


        call.enqueue(new Callback<ServiceConfigurationLocal>() {
            @Override
            public void onResponse(Call<ServiceConfigurationLocal> call, Response<ServiceConfigurationLocal> response) {


                selectMarket.setVisibility(View.VISIBLE);
                progressBarSelect.setVisibility(View.INVISIBLE);




                if(response.code()==200)
                {

                    PrefGeneral.saveServiceURL(configurationGlobal.getServiceURL(),context);
                    PrefServiceConfig.saveServiceConfigLocal(response.body(),context);


                    ServiceConfigurationLocal config = response.body();

                    if(config!=null)
                    {
                        Currency currency = Currency.getInstance(new Locale("",config.getISOCountryCode()));
                        PrefGeneral.saveCurrencySymbol(currency.getSymbol(),context);
                    }



                    subscriber.selectMarketSuccessful(configurationGlobal,getLayoutPosition());
                }
                else
                {
//                        PrefServiceConfig.saveServiceConfigLocal(null,getApplicationContext());
//                        PrefGeneral.saveServiceURL(null,getApplicationContext());


                    showToastMessage("Failed Code : " + String.valueOf(response.code()));
                }


            }



            @Override
            public void onFailure(Call<ServiceConfigurationLocal> call, Throwable t) {


                selectMarket.setVisibility(View.VISIBLE);
                progressBarSelect.setVisibility(View.INVISIBLE);
                showToastMessage("Failed ... Please check your network ! ");


            }
        });
    }







    void showToastMessage(String message)
    {
        Toast.makeText(context,message,Toast.LENGTH_SHORT).show();
    }


}
