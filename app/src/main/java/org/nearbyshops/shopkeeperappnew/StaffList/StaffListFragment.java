package org.nearbyshops.shopkeeperappnew.StaffList;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.google.gson.Gson;
import org.nearbyshops.shopkeeperappnew.API.ShopStaffLoginService;
import org.nearbyshops.shopkeeperappnew.API.UserService;
import org.nearbyshops.shopkeeperappnew.DaggerComponentBuilder;
import org.nearbyshops.shopkeeperappnew.ModelRoles.User;
import org.nearbyshops.shopkeeperappnew.ModelRoles.UserEndpoint;
import org.nearbyshops.shopkeeperappnew.Prefrences.PrefLocation;
import org.nearbyshops.shopkeeperappnew.Prefrences.PrefLogin;
import org.nearbyshops.shopkeeperappnew.R;
import org.nearbyshops.shopkeeperappnew.StaffList.EditProfileStaff.EditProfileStaff;
import org.nearbyshops.shopkeeperappnew.StaffList.EditProfileStaff.FragmentEditProfileStaff;
import org.nearbyshops.shopkeeperappnew.ViewHolderCommon.Models.EmptyScreenDataFullScreen;
import org.nearbyshops.shopkeeperappnew.ViewHolderCommon.Models.HeaderData;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import javax.inject.Inject;
import java.util.ArrayList;

/**
 * Created by sumeet on 14/6/17.
 */

public class StaffListFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, ViewHolderShopStaff.ListItemClick{




    private boolean isDestroyed = false;

    @BindView(R.id.swipe_container) SwipeRefreshLayout swipeContainer;
    @BindView(R.id.recycler_view) RecyclerView recyclerView;


    @Inject
    UserService userService;

    @Inject
    ShopStaffLoginService service;

    private GridLayoutManager layoutManager;
    private Adapter listAdapter;
    private ArrayList<Object> dataset = new ArrayList<>();


    // flags
    private boolean clearDataset = false;

//    boolean getRowCountVehicle = false;
//    boolean resetOffsetVehicle = false;


    private int limit = 10;
    private int offset = 0;
    private int item_count = 0;




//    @BindView(R.id.drivers_count) TextView driversCount;
//    int i = 1;

    public StaffListFragment() {

        DaggerComponentBuilder.getInstance()
                .getNetComponent().Inject(this);
    }



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        setRetainInstance(true);
        View rootView = inflater.inflate(R.layout.fragment_delivery_guy_list, container, false);
        ButterKnife.bind(this,rootView);


//        Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
//        toolbar.setTitleTextColor(ContextCompat.getColor(getActivity(),R.color.white));
//        toolbar.setTitle("Trip History");
//        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);



        setupSwipeContainer();
        setupRecyclerView();

        if(savedInstanceState == null)
        {
            makeRefreshNetworkCall();
        }


//        driversCount.setText("Drivers COunt : " + String.valueOf(++i));

        return rootView;
    }






    private void setupSwipeContainer()
    {

        if(swipeContainer!=null) {

            swipeContainer.setOnRefreshListener(this);
            swipeContainer.setColorSchemeResources(
                    android.R.color.holo_blue_bright,
                    android.R.color.holo_green_light,
                    android.R.color.holo_orange_light,
                    android.R.color.holo_red_light);
        }

    }





    private void setupRecyclerView()
    {

        listAdapter = new Adapter(dataset,getActivity(),this);
        recyclerView.setAdapter(listAdapter);

        layoutManager = new GridLayoutManager(getActivity(),1, LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));


        final DisplayMetrics metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);


        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {


            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);


                if(layoutManager.findLastVisibleItemPosition()==dataset.size())
                {

                    if(offset + limit > layoutManager.findLastVisibleItemPosition())
                    {
                        return;
                    }


                    // trigger fetch next page

                    if((offset + limit)<= item_count)
                    {
                        offset = offset + limit;

                        getStaffProfiles();
                    }


                }
            }
        });

    }






    @Override
    public void onResume() {
        super.onResume();
        isDestroyed = false;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        isDestroyed = true;
    }





    private void makeRefreshNetworkCall()
    {
        swipeContainer.post(new Runnable() {
            @Override
            public void run() {

                swipeContainer.setRefreshing(true);
                onRefresh();
            }
        });
    }




    @Override
    public void onRefresh() {

        clearDataset = true;
//        getRowCountVehicle = true;
//        resetOffsetVehicle = true;

        getStaffProfiles();
    }








    private void getStaffProfiles()
    {

        if(clearDataset)
        {
            offset = 0;
        }


        User user = PrefLogin.getUser(getActivity());

        if(user ==null)
        {
            swipeContainer.setRefreshing(false);
            return;
        }



        Call<UserEndpoint> call = service.getStaffForAdmin(
                PrefLogin.getAuthorizationHeaders(getActivity()),
                (double) PrefLocation.getLatitude(getActivity()),(double)PrefLocation.getLongitude(getActivity()),
                null,null,
                null,null,
                limit, offset,
                clearDataset,false
        );




        call.enqueue(new Callback<UserEndpoint>() {
            @Override
            public void onResponse(Call<UserEndpoint> call, Response<UserEndpoint> response) {


                if(isDestroyed)
                {
                    return;
                }

                if(response.code() == 200 && response.body()!=null) {

                    if (clearDataset) {
                        dataset.clear();
                        clearDataset = false;

                        item_count = response.body().getItemCount();

                        if(item_count>0)
                        {
                            dataset.add(new HeaderData("Staff Members"));
                        }
                    }





                    if(response.body().getResults()!=null)
                    {
                        dataset.addAll(response.body().getResults());
                    }





                    if(item_count==0)
                    {
                        dataset.add(EmptyScreenDataFullScreen.emptyScreenStaffList());

                    }


                    if(offset + limit >= item_count)
                    {
                        listAdapter.setLoadMore(false);
                    }
                    else
                    {
                        listAdapter.setLoadMore(true);
                    }
                }


                listAdapter.notifyDataSetChanged();


                swipeContainer.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<UserEndpoint> call, Throwable t) {


                if(isDestroyed)
                {
                    return;
                }

//                showToastMessage("Network Connection Failed !");

                swipeContainer.setRefreshing(false);


                dataset.clear();
                dataset.add(EmptyScreenDataFullScreen.getOffline());
                listAdapter.notifyDataSetChanged();

            }
        });


    }




    void showToastMessage(String message)
    {
        Toast.makeText(getActivity(),message, Toast.LENGTH_SHORT).show();
    }



//    @Override
//    public void taxiFiltersChanged() {
//        makeRefreshNetworkCall();
//    }



    @Override
    public void notifyTripRequestSelected() {

    }





    @Override
    public void listItemClick(User user, int position) {

        Gson gson = new Gson();
        String jsonString = gson.toJson(user);
//
//        Intent intent = new Intent(getActivity(), TripHistoryDetail.class);
//        intent.putExtra(TripHistoryDetail.TRIP_HISTORY_DETAIL_INTENT_KEY,jsonString);
//        startActivity(intent);


        Intent intent = new Intent(getActivity(), EditProfileStaff.class);
        intent.putExtra("staff_profile",jsonString);
        intent.putExtra(FragmentEditProfileStaff.EDIT_MODE_INTENT_KEY, FragmentEditProfileStaff.MODE_UPDATE);
        startActivity(intent);
    }




    @Override
    public boolean listItemLongClick(View view, User tripRequest, int position) {
        return false;
    }






}
