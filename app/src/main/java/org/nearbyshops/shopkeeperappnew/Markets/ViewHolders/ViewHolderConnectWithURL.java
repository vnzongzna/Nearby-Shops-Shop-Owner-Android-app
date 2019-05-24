package org.nearbyshops.shopkeeperappnew.Markets.ViewHolders;

import android.content.Context;
import android.text.Editable;
import android.text.Selection;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.google.gson.Gson;
import okhttp3.OkHttpClient;
import org.nearbyshops.shopkeeperappnew.API.ServiceConfigurationService;
import org.nearbyshops.shopkeeperappnew.DaggerComponentBuilder;
import org.nearbyshops.shopkeeperappnew.Interfaces.NotifyAboutLogin;
import org.nearbyshops.shopkeeperappnew.Markets.Interfaces.listItemMarketNotifications;
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


public class ViewHolderConnectWithURL extends RecyclerView.ViewHolder{



    private Context context;
    private NotifyAboutLogin fragment;




    @BindView(R.id.market_url) EditText marketURL;
    @BindView(R.id.progress_bar_connect) ProgressBar progressBarConnect;
    @BindView(R.id.button_connect) TextView buttonConnect;

    private listItemMarketNotifications subscriber;



    @Inject Gson gson;




    public static ViewHolderConnectWithURL create(ViewGroup parent, Context context, listItemMarketNotifications subscriber)
    {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_connect_using_url, parent, false);

        return new ViewHolderConnectWithURL(view,parent,context, subscriber);
    }




    public ViewHolderConnectWithURL(View itemView, ViewGroup parent, Context context, listItemMarketNotifications subscriber)
    {
        super(itemView);
        ButterKnife.bind(this,itemView);
        this.context = context;
//        this.fragment = fragment;


        this.subscriber = subscriber;


        DaggerComponentBuilder.getInstance()
                .getNetComponent()
                .Inject(this);


        marketURL.setText("http://");
        Selection.setSelection(marketURL.getText(), marketURL.getText().length());


        marketURL.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(!s.toString().startsWith("http://")){
                    marketURL.setText("http://");
                    Selection.setSelection(marketURL.getText(), marketURL.getText().length());

                }

            }
        });



        bindViews();
    }





    void bindViews()
    {
        String url = PrefGeneral.getServiceURL(context);

        if(url==null)
        {
            marketURL.setText("http://");
        }
        else
        {
            marketURL.setText(url);
        }

    }





    @OnClick(R.id.button_connect)
    void selectMarket()
    {
        String market_url = marketURL.getText().toString();

        if(market_url.length() == 0)
        {
            marketURL.setError("Enter Market URL");
            marketURL.requestFocus();
            return;
        }




        fetchConfiguration(marketURL.getText().toString());
    }





    @OnClick(R.id.clear)
    void clear()
    {
        marketURL.setText("");
    }




    void showToastMessage(String message)
    {
        Toast.makeText(context,message,Toast.LENGTH_SHORT).show();
    }





    void fetchConfiguration(final String serviceURL)
    {


        buttonConnect.setVisibility(View.INVISIBLE);
        progressBarConnect.setVisibility(View.VISIBLE);



        if(!Patterns.WEB_URL.matcher(serviceURL).matches())
        {
            showToastMessage("Invalid URL");



            buttonConnect.setVisibility(View.VISIBLE);
            progressBarConnect.setVisibility(View.INVISIBLE);
            return;
        }



        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .baseUrl(serviceURL)
                .client(new OkHttpClient().newBuilder().build())
                .build();




        ServiceConfigurationService service = retrofit.create(ServiceConfigurationService.class);

        Call<ServiceConfigurationLocal> call = service.getServiceConfiguration(0.0,0.0);





        call.enqueue(new Callback<ServiceConfigurationLocal>() {
            @Override
            public void onResponse(Call<ServiceConfigurationLocal> call, Response<ServiceConfigurationLocal> response) {


                buttonConnect.setVisibility(View.VISIBLE);
                progressBarConnect.setVisibility(View.INVISIBLE);




                if(response.code()==200)
                {

                    PrefGeneral.saveServiceURL(serviceURL,context);
                    PrefServiceConfig.saveServiceConfigLocal(response.body(),context);


                    ServiceConfigurationLocal config = response.body();

                    if(config!=null)
                    {
                        Currency currency = Currency.getInstance(new Locale("",config.getISOCountryCode()));
                        PrefGeneral.saveCurrencySymbol(currency.getSymbol(),context);
                    }



                    subscriber.selectMarketSuccessful(null,getLayoutPosition());
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


                buttonConnect.setVisibility(View.VISIBLE);
                progressBarConnect.setVisibility(View.INVISIBLE);

                subscriber.showMessage("Failed ... Please check your network ! ");
            }
        });
    }







}



