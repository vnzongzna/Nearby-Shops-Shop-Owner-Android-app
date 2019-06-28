package org.nearbyshops.shopkeeperappnew.Markets.ViewHolders;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.core.content.ContextCompat;
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


public class ViewHolderMarket extends RecyclerView.ViewHolder implements View.OnClickListener {


    @BindView(R.id.service_name) TextView serviceName;
    @BindView(R.id.address) TextView serviceAddress;
    @BindView(R.id.indicator_category) TextView indicatorCategory;
    @BindView(R.id.indicator_verified) TextView indicatorVerified;
    @BindView(R.id.distance) TextView distance;
    @BindView(R.id.rating) TextView rating;
    @BindView(R.id.rating_count) TextView ratingCount;
    @BindView(R.id.description) TextView description;
    @BindView(R.id.logo) ImageView serviceLogo;

    @BindView(R.id.progress_bar_select) ProgressBar progressBarSelect;
    @BindView(R.id.select_market) TextView selectMarket;


    private ServiceConfigurationGlobal configurationGlobal;
    private listItemMarketNotifications subscriber;
    private Context context;


    @Inject Gson gson;





    public static ViewHolderMarket create(ViewGroup parent, Context context, listItemMarketNotifications subscriber)
    {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_market, parent, false);

        return new ViewHolderMarket(view,parent,context,subscriber);
    }







    public ViewHolderMarket(View itemView, ViewGroup parent, Context context, listItemMarketNotifications subscriber)
    {
        super(itemView);
        ButterKnife.bind(this,itemView);



        this.context = context;
        this.subscriber = subscriber;

//        itemView = LayoutInflater.from(parent.getContext())
//                .inflate(R.layout.list_item_market,parent,false);


        itemView.setOnClickListener(this);



        DaggerComponentBuilder.getInstance()
                .getNetComponent()
                .Inject(this);
    }



    public void setItem(ServiceConfigurationGlobal configurationGlobal)
    {
        this.configurationGlobal = configurationGlobal;


        serviceName.setText(configurationGlobal.getServiceName());
        serviceAddress.setText(configurationGlobal.getCity());

//                service.getAddress() + ", " +



//                if(service.getVerified())
//                {
//                    holder.indicatorVerified.setVisibility(View.VISIBLE);
//                }
//                else
//                {
//                    holder.indicatorVerified.setVisibility(View.GONE);
//                }



//                holder.indicatorVerified.setVisibility(View.VISIBLE);



        distance.setText("Distance : " + String.format("%.2f",configurationGlobal.getRt_distance()));
//                holder.rating.setText(String.format("%.2f",));

        description.setText(configurationGlobal.getDescriptionShort());




        if(configurationGlobal.getRt_rating_count()==0)
        {
            rating.setText(" New ");
            rating.setBackgroundColor(ContextCompat.getColor(context, R.color.phonographyBlue));
            ratingCount.setVisibility(View.GONE);
        }
        else
        {
            rating.setText(String.format("%.2f",configurationGlobal.getRt_rating_avg()));
            ratingCount.setText("( " + String.valueOf((int)configurationGlobal.getRt_rating_count()) + " Ratings )");

            rating.setBackgroundColor(ContextCompat.getColor(context, R.color.gplus_color_2));
            ratingCount.setVisibility(View.VISIBLE);

        }





        String imagePath = PrefServiceConfig.getServiceURL_SDS(context)
                + "/api/v1/ServiceConfiguration/Image/three_hundred_" + configurationGlobal.getLogoImagePath() + ".jpg";

//                System.out.println("Service LOGO : " + imagePath);

        Drawable placeholder = VectorDrawableCompat
                .create(context.getResources(),
                        R.drawable.ic_nature_people_white_48px, context.getTheme());


        Picasso.get()
                .load(imagePath)
                .placeholder(placeholder)
                .into(serviceLogo);

    }






//        @OnClick(R.id.description)
//        void copyURLClick()
//        {
//            ClipboardManager clipboard = (ClipboardManager) fragment.getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
//            ClipData clip = ClipData.newPlainText("URL", serviceURL.getText().toString());
//            clipboard.setPrimaryClip(clip);
//
//            showToastMessage("Copied !");
//        }




    @OnClick(R.id.select_market)
    void selectMarket()
    {


        ServiceConfigurationGlobal configurationGlobal = this.configurationGlobal;

        fetchConfiguration(configurationGlobal);
    }



    @Override
    public void onClick(View v) {

        subscriber.listItemClick(configurationGlobal,getLayoutPosition());
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


                    subscriber.showMessage("Failed Code : " + String.valueOf(response.code()));
                }


            }



            @Override
            public void onFailure(Call<ServiceConfigurationLocal> call, Throwable t) {


                selectMarket.setVisibility(View.VISIBLE);
                progressBarSelect.setVisibility(View.INVISIBLE);

                subscriber.showMessage("Failed ... Please check your network ! ");
            }
        });
    }

}



