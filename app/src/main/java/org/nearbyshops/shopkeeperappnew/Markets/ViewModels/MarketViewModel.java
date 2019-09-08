package org.nearbyshops.shopkeeperappnew.Markets.ViewModels;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.google.gson.Gson;
import okhttp3.OkHttpClient;
import org.nearbyshops.shopkeeperappnew.DaggerComponentBuilder;
import org.nearbyshops.shopkeeperappnew.Markets.Model.Endpoints.ServiceConfigurationEndPoint;
import org.nearbyshops.shopkeeperappnew.Markets.Model.SignInMarker;
import org.nearbyshops.shopkeeperappnew.Markets.api.ServiceConfigService;
import org.nearbyshops.shopkeeperappnew.ModelRoles.User;
import org.nearbyshops.shopkeeperappnew.MyApplication;
import org.nearbyshops.shopkeeperappnew.Prefrences.PrefLocation;
import org.nearbyshops.shopkeeperappnew.Prefrences.PrefLoginGlobal;
import org.nearbyshops.shopkeeperappnew.Prefrences.PrefServiceConfig;
import org.nearbyshops.shopkeeperappnew.ViewHolderCommon.Models.EmptyScreenData;
import org.nearbyshops.shopkeeperappnew.ViewHolderCommon.Models.HeaderTitle;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

public class MarketViewModel extends AndroidViewModel {

    private MutableLiveData<List<Object>> datasetLive;
    private List<Object> dataset;
    private MutableLiveData<Integer> status;
    private MutableLiveData<String> message;



    final private int limit = 25;
    int offset = 0;
    int item_count = 0;


    @Inject
    Gson gson;




    public MarketViewModel(@NonNull Application application) {
        super(application);

        status = new MutableLiveData<>();
        message = new MutableLiveData<>();
        datasetLive = new MutableLiveData<>();
        dataset = new ArrayList<>();


        DaggerComponentBuilder.getInstance()
                .getNetComponent().Inject(this);
    }





    public MutableLiveData<List<Object>> getData()
    {
        return datasetLive;
    }




    void loadMoreData()
    {
    }





    public LiveData<Integer> getStatus()
    {

        return status;
    }





    public LiveData<String> getMessage()
    {

        return message;
    }



    void search(String searchString)
    {

    }



    void endSearchMode()
    {

    }








    public void loadData(final boolean clearDataset)
    {


            Retrofit retrofit = new Retrofit.Builder()
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .baseUrl(PrefServiceConfig.getServiceURL_SDS(MyApplication.getAppContext()))
                    .client(new OkHttpClient().newBuilder().build())
                    .build();



            Call<ServiceConfigurationEndPoint> call;




            if(PrefLoginGlobal.getUser(getApplication())==null)
            {

                call = retrofit.create(ServiceConfigService.class).getShopListSimple(
                        (double) PrefLocation.getLatitude(getApplication()), PrefLocation.getLongitude(getApplication()),
                        null,
                        null,
                        null,limit,offset);


            }
            else
            {

                call = retrofit.create(ServiceConfigService.class).getShopListSimple(
                        PrefLoginGlobal.getAuthorizationHeaders(getApplication()),
                        PrefLocation.getLatitude(getApplication()), PrefLocation.getLongitude(getApplication()),
                        null,
                        null,
                        null,limit,offset);
            }






//        PrefLocation.getLatitude(getActivity()),
//                PrefLocation.getLongitude(getActivity()),

//        filterOfficial,filterVerified,
//                serviceType,

            call.enqueue(new Callback<ServiceConfigurationEndPoint>() {
                @Override
                public void onResponse(Call<ServiceConfigurationEndPoint> call, Response<ServiceConfigurationEndPoint> response) {


                    if(response.code()==200)
                    {
                        item_count = response.body().getItemCount();
//                        adapter.setTotalItemsCount(item_count);


                        if(clearDataset)
                        {

                            dataset.clear();
//                            savedMarkets.clear();




//                            ServiceConfigurationLocal configurationLocal = PrefServiceConfig.getServiceConfigLocal(getApplication());
//
//                            if(configurationLocal!=null)
//                            {
//                                dataset.add(configurationLocal);
//                            }



                            if(response.body().getSavedMarkets()!=null)
                            {
//                                savedMarkets.addAll(response.body().getSavedMarkets());
                                dataset.add(response.body().getSavedMarkets());
                            }


//                        Log.d(UtilityFunctions.TAG_LOG,UtilityFunctions.provideGson().toJson(response.body().getSavedMarkets()));
//                        Log.d(UtilityFunctions.TAG_LOG,"Saved Markets List Size : " + String.valueOf(savedMarkets.size()));







                            User user = PrefLoginGlobal.getUser(getApplication());

                            if(user!=null)
                            {
                                dataset.add(user);
                            }
                            else
                            {
                                dataset.add(new SignInMarker());
                            }



//                            if(item_count>0)
//                            {
//                                dataset.add(new HeaderItemsList());
//                            }



                            dataset.add(new HeaderTitle());

                        }



                        if(response.body().getResults()!=null)
                        {
                            dataset.addAll(response.body().getResults());

                            dataset.add(EmptyScreenData.getCreateMarketData());


//                            if(response.body().getResults().size()==0)
//                            {
//                                dataset.add(new CreateMarketMarker());
//                                dataset.add(new ConnectWithURLMarker());
//
//                            }
//                            else if(dataset.size()>3)
//                            {
//                                dataset.add(3,new ConnectWithURLMarker());
//                            }
//                            else
//                            {
//                                dataset.add(new ConnectWithURLMarker());
//                            }


                        }






                        datasetLive.postValue(dataset);


//                        message.setValue("Response OK");

                    }
                    else
                    {
                        message.setValue("Failed : code : " + String.valueOf(response.code()));

                    }




                }

                @Override
                public void onFailure(Call<ServiceConfigurationEndPoint> call, Throwable t) {

                    message.setValue("Failed ... please check your network connection !");

                }
            });

        }





}


