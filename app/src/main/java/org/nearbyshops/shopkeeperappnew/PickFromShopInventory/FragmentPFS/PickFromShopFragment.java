package org.nearbyshops.shopkeeperappnew.PickFromShopInventory.FragmentPFS;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import okhttp3.ResponseBody;
import org.nearbyshops.shopkeeperappnew.API.OrderServiceShopStaff;
import org.nearbyshops.shopkeeperappnew.DaggerComponentBuilder;
import org.nearbyshops.shopkeeperappnew.Interfaces.*;
import org.nearbyshops.shopkeeperappnew.Model.Order;
import org.nearbyshops.shopkeeperappnew.ModelEndpoints.OrderEndPoint;
import org.nearbyshops.shopkeeperappnew.OrderDetail.OrderDetail;
import org.nearbyshops.shopkeeperappnew.OrderDetail.PrefOrderDetail;
import org.nearbyshops.shopkeeperappnew.OrderHistoryNew.SlidingLayerSort.PrefSortOrders;
import org.nearbyshops.shopkeeperappnew.OrderHistoryNew.ViewHolders.ViewHolderOrder;
import org.nearbyshops.shopkeeperappnew.PickFromShopInventory.ViewHolders.ViewHolderOrderButtonSingle;
import org.nearbyshops.shopkeeperappnew.Prefrences.PrefLogin;
import org.nearbyshops.shopkeeperappnew.R;
import org.nearbyshops.shopkeeperappnew.ViewHolderCommon.Models.EmptyScreenData;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;


public class PickFromShopFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, ViewHolderOrderButtonSingle.ListItemClick, ViewHolderOrder.ListItemClick, NotifySort, NotifySearch, RefreshFragment {


//    @Inject
//    OrderService orderService;

    @Inject
    OrderServiceShopStaff orderServiceShopStaff;

    RecyclerView recyclerView;
    Adapter adapter;

    public List<Object> dataset = new ArrayList<>();

    GridLayoutManager layoutManager;
    SwipeRefreshLayout swipeContainer;


    final private int limit = 5;
    int offset = 0;
    int item_count = 0;

    boolean isDestroyed;



    public PickFromShopFragment() {

        DaggerComponentBuilder.getInstance()
                .getNetComponent()
                .Inject(this);

    }


    public static PickFromShopFragment newInstance(int orderStatus,boolean isPickFromShop) {
        PickFromShopFragment fragment = new PickFromShopFragment();
        Bundle args = new Bundle();
        args.putInt("order_status",orderStatus);
        args.putBoolean("is_pick_from_shop",isPickFromShop);
        fragment.setArguments(args);
        return fragment;
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        setRetainInstance(true);
        View rootView = inflater.inflate(R.layout.fragment_pfs_inventory, container, false);


        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
        swipeContainer = (SwipeRefreshLayout)rootView.findViewById(R.id.swipeContainer);


        if(savedInstanceState==null)
        {
            makeRefreshNetworkCall();
        }


        setupRecyclerView();
        setupSwipeContainer();


        return rootView;
    }


    void setupSwipeContainer()
    {
        if(swipeContainer!=null) {

            swipeContainer.setOnRefreshListener(this);
            swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                    android.R.color.holo_green_light,
                    android.R.color.holo_orange_light,
                    android.R.color.holo_red_light);
        }

    }


    void setupRecyclerView()
    {

        adapter = new Adapter(dataset,this);

        recyclerView.setAdapter(adapter);

        layoutManager = new GridLayoutManager(getActivity(),1);
        recyclerView.setLayoutManager(layoutManager);

        DisplayMetrics metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);

//        layoutManager.setSpanCount(metrics.widthPixels/400);



        int spanCount = (int) (metrics.widthPixels/(230 * metrics.density));

        if(spanCount==0){
            spanCount = 1;
        }

        layoutManager.setSpanCount(spanCount);


        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);


                if(offset + limit > layoutManager.findLastVisibleItemPosition() + 1 - 1)
                {
                    return;
                }


                if(layoutManager.findLastVisibleItemPosition()==dataset.size()-1 + 1)
                {
                    // trigger fetch next page

//                    if(layoutManager.findLastVisibleItemPosition() == previous_position)
//                    {
//                        return;
//                    }


                    if((offset+limit)<=item_count)
                    {
                        offset = offset + limit;
                        makeNetworkCall(false);
                    }

//                    previous_position = layoutManager.findLastVisibleItemPosition();

                }

            }
        });
    }



//    int previous_position = -1;



    @Override
    public void onRefresh() {

        offset = 0;
        makeNetworkCall(true);
    }


    void makeRefreshNetworkCall()
    {
        swipeContainer.post(new Runnable() {
            @Override
            public void run() {
                swipeContainer.setRefreshing(true);

                onRefresh();
            }
        });
    }




    void makeNetworkCall(final boolean clearDataset)
    {

//            Shop currentShop = UtilityShopHome.getShop(getContext());


        String current_sort = "";
        current_sort = PrefSortOrders.getSort(getContext()) + " " + PrefSortOrders.getAscending(getContext());


        int orderStatus = getArguments().getInt("order_status");
        boolean isPickFromShop = getArguments().getBoolean("is_pick_from_shop");


        Integer orderStatusHD = null;
        Integer orderStatusPFS = null;

        if(isPickFromShop)
        {
            orderStatusPFS = orderStatus;
        }
        else
        {
            orderStatusHD = orderStatus;
        }




        Call<OrderEndPoint> call = orderServiceShopStaff.getOrders(
                    PrefLogin.getAuthorizationHeaders(getActivity()),
                    null,null,
                    isPickFromShop,orderStatusHD,
                    orderStatusPFS,null,
                    null,null,
                    null,null,null,
                    searchQuery,
                    current_sort,limit,offset,null);





        call.enqueue(new Callback<OrderEndPoint>() {
            @Override
            public void onResponse(Call<OrderEndPoint> call, Response<OrderEndPoint> response) {

                if(isDestroyed)
                {
                    return;
                }

                if(response.body()!= null)
                {
                    item_count = response.body().getItemCount();

                    if(clearDataset)
                    {
                        dataset.clear();
                    }

                    if(response.body().getResults()!=null)
                    {
                        dataset.addAll(response.body().getResults());
                    }


//                    adapter.notifyDataSetChanged();
                    notifyTitleChanged();

                }

                swipeContainer.setRefreshing(false);




                if(item_count==0)
                {
                    dataset.add(EmptyScreenData.emptyScreenPFSINventory());
                }


                adapter.notifyDataSetChanged();

            }

            @Override
            public void onFailure(Call<OrderEndPoint> call, Throwable t) {

                if(isDestroyed)
                {
                    return;
                }

//                showToastMessage("Network Request failed !");
                dataset.clear();
                dataset.add(EmptyScreenData.getOffline());
                adapter.notifyDataSetChanged();

                swipeContainer.setRefreshing(false);
            }
        });
    }





    @Override
    public void onResume() {
        super.onResume();
        notifyTitleChanged();
        isDestroyed=false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        isDestroyed=true;
    }



    void showToastMessage(String message)
    {
        if(getActivity()!=null)
        {
            Toast.makeText(getActivity(),message,Toast.LENGTH_SHORT).show();
        }

    }








    void notifyTitleChanged()
    {

        if(getActivity() instanceof NotifyTitleChangedNew)
        {
            ((NotifyTitleChangedNew)getActivity())
                    .NotifyTitleChanged(
                            "(" + String.valueOf(dataset.size())
                                    + "/" + String.valueOf(item_count) + ")");

        }
    }





    // Refresh the Confirmed PlaceholderFragment

    private static String makeFragmentName(int viewId, int index) {
        return "android:switcher:" + viewId + ":" + index;
    }



    void refreshConfirmedFragment()
    {
        Fragment fragment = getActivity().getSupportFragmentManager()
                .findFragmentByTag(makeFragmentName(R.id.container,2));
//
//        if(fragment instanceof RefreshFragment)
//        {
//            ((RefreshFragment)fragment).refreshFragment();
//        }
    }



    @Override
    public void notifyOrderSelected(Order order) {

        PrefOrderDetail.saveOrder(order,getActivity());
        getActivity().startActivity(new Intent(getActivity(), OrderDetail.class));
    }







    @Override
    public void notifyCancelOrder(Order order) {

    }




    @Override
    public void notifyCancelOrder(final Order order, final int position) {


        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle("Confirm Cancel Order !")
                .setMessage("Are you sure you want to cancel this order !")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        cancelOrder(order, position);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        showToastMessage(" Not Cancelled !");
                    }
                })
                .show();
    }



    @Override
    public void confirmOrderPFS(Order order, int position, TextView button, ProgressBar progressBar) {



        button.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);



        Call<ResponseBody> call = orderServiceShopStaff.confirmOrderPFS(
                PrefLogin.getAuthorizationHeaders(getActivity()),
                order.getOrderID()
        );




        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {


                if(!isVisible())
                {
                    return;
                }


                button.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.INVISIBLE);



                if(response.code()==200)
                {
                    showToastMessage("Successful !");

                    refreshConfirmedFragment();
                    dataset.remove(order);
                    item_count = item_count - 1;
                    adapter.notifyItemRemoved(position);
                    notifyTitleChanged();

                }
                else if(response.code() == 401 || response.code() == 403)
                {
                    showToastMessage("Not permitted !");
                }
                else
                {
                    showToastMessage("Failed with Error Code : " + String.valueOf(response.code()));
                }


                if(item_count==0)
                {
                    dataset.add(EmptyScreenData.emptyScreenPFSINventory());
                    adapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {


                if(!isVisible())
                {
                    return;
                }

                button.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.INVISIBLE);


                dataset.clear();
                dataset.add(EmptyScreenData.getOffline());
                adapter.notifyDataSetChanged();

//                showToastMessage("Network request failed. Check your connection !");
            }
        });
    }




    @Override
    public void setOrderPackedPFS(Order order, int position, TextView button, ProgressBar progressBar) {



        button.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);


        Call<ResponseBody> call = orderServiceShopStaff.setOrderPackedPFS(
                PrefLogin.getAuthorizationHeaders(getActivity()),
                order.getOrderID()
        );


        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {


                if(!isVisible())
                {
                    return;
                }

                button.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.INVISIBLE);



                if(response.code()==200)
                {
                    showToastMessage("Successful !");

                    refreshConfirmedFragment();
                    dataset.remove(order);
                    item_count = item_count - 1;
                    adapter.notifyItemRemoved(position);
                    notifyTitleChanged();

                }
                else if(response.code() == 401 || response.code() == 403)
                {
                    showToastMessage("Not permitted !");
                }
                else
                {
                    showToastMessage("Failed with Error Code : " + String.valueOf(response.code()));
                }


                if(item_count==0)
                {
                    dataset.add(EmptyScreenData.emptyScreenPFSINventory());
                    adapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {


                if(!isVisible())
                {
                    return;
                }

                button.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.INVISIBLE);

//                showToastMessage("Network request failed. Check your connection !");



                dataset.clear();
                dataset.add(EmptyScreenData.getOffline());
                adapter.notifyDataSetChanged();
            }
        });
    }




    @Override
    public void readyForPickupPFS(Order order, int position, TextView button, ProgressBar progressBar) {


        button.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);



        Call<ResponseBody> call = orderServiceShopStaff.setOrderReadyForPickupPFS(
                PrefLogin.getAuthorizationHeaders(getActivity()),
                order.getOrderID()
        );



        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {


                if(!isVisible())
                {
                    return;
                }

                button.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.INVISIBLE);



                if(response.code()==200)
                {
                    showToastMessage("Successful !");

                    refreshConfirmedFragment();
                    dataset.remove(order);
                    item_count = item_count - 1;
                    adapter.notifyItemRemoved(position);
                    notifyTitleChanged();

                }
                else if(response.code() == 401 || response.code() == 403)
                {
                    showToastMessage("Not permitted !");
                }
                else
                {
                    showToastMessage("Failed with Error Code : " + String.valueOf(response.code()));
                }



                if(item_count==0)
                {
                    dataset.add(EmptyScreenData.emptyScreenPFSINventory());
                    adapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {


                if(!isVisible())
                {
                    return;
                }

                button.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.INVISIBLE);


//                showToastMessage("Network request failed. Check your connection !");


                dataset.clear();
                dataset.add(EmptyScreenData.getOffline());
                adapter.notifyDataSetChanged();
            }
        });
    }



    @Override
    public void paymentReceivedPFS(Order order, int position, TextView button, ProgressBar progressBar) {


        button.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);


        Call<ResponseBody> call = orderServiceShopStaff.paymentReceivedPFS(
                PrefLogin.getAuthorizationHeaders(getActivity()),
                order.getOrderID()
        );



        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {


                if(!isVisible())
                {
                    return;
                }

                button.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.INVISIBLE);



                if(response.code()==200)
                {
                    showToastMessage("Successful !");

                    refreshConfirmedFragment();
                    dataset.remove(order);
                    item_count = item_count - 1;
                    adapter.notifyItemRemoved(position);
                    notifyTitleChanged();

                }
                else if(response.code() == 401 || response.code() == 403)
                {
                    showToastMessage("Not permitted !");
                }
                else
                {
                    showToastMessage("Failed with Error Code : " + String.valueOf(response.code()));
                }



                if(item_count==0)
                {
                    dataset.add(EmptyScreenData.emptyScreenPFSINventory());
                    adapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {


                if(!isVisible())
                {
                    return;
                }

                button.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.INVISIBLE);

//                showToastMessage("Network request failed. Check your connection !");


                dataset.clear();
                dataset.add(EmptyScreenData.getOffline());
                adapter.notifyDataSetChanged();
            }
        });
    }








    @Override
    public void confirmOrderHD(Order order, int position, TextView button, ProgressBar progressBar) {

        button.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);

        Call<ResponseBody> call = orderServiceShopStaff.confirmOrder(
                PrefLogin.getAuthorizationHeaders(getActivity()),
                order.getOrderID());

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {


                if(!isVisible())
                {
                    return;
                }

                button.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.INVISIBLE);



                if(response.code()==200)
                {
                    showToastMessage("Confirmed !");

                    refreshConfirmedFragment();
                    dataset.remove(order);
                    item_count = item_count - 1;
                    adapter.notifyItemRemoved(position);
                    notifyTitleChanged();

                }
                else if(response.code() == 401 || response.code() == 403)
                {
                    showToastMessage("Not permitted !");
                }
                else
                {
                    showToastMessage("Failed with Error Code : " + String.valueOf(response.code()));
                }



                if(item_count==0)
                {
                    dataset.add(EmptyScreenData.emptyScreenPFSINventory());
                    adapter.notifyDataSetChanged();
                }


            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {



                if(!isVisible())
                {
                    return;
                }

                button.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.INVISIBLE);

//                showToastMessage("Network request failed. Check your connection !");


                dataset.clear();
                dataset.add(EmptyScreenData.getOffline());
                adapter.notifyDataSetChanged();

            }
        });


    }



    @Override
    public void setOrderPackedHD(Order order, int position, TextView button, ProgressBar progressBar) {


        button.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);

        Call<ResponseBody> call = orderServiceShopStaff.setOrderPacked(
                PrefLogin.getAuthorizationHeaders(getActivity()),
                order.getOrderID());

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {


                if(!isVisible())
                {
                    return;
                }

                button.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.INVISIBLE);



                if(response.code()==200)
                {
                    showToastMessage("Packed !");

                    refreshConfirmedFragment();
                    dataset.remove(order);
                    item_count = item_count - 1;
                    adapter.notifyItemRemoved(position);
                    notifyTitleChanged();

                }
                else if(response.code() == 401 || response.code() == 403)
                {
                    showToastMessage("Not permitted !");
                }
                else
                {
                    showToastMessage("Failed with Error Code : " + String.valueOf(response.code()));
                }



                if(item_count==0)
                {
                    dataset.add(EmptyScreenData.emptyScreenPFSINventory());
                    adapter.notifyDataSetChanged();
                }


            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {



                if(!isVisible())
                {
                    return;
                }

                button.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.INVISIBLE);

//                showToastMessage("Network request failed. Check your connection !");


                dataset.clear();
                dataset.add(EmptyScreenData.getOffline());
                adapter.notifyDataSetChanged();

            }
        });
    }


    private void cancelOrder(final Order order, final int position) {


//        Call<ResponseBody> call = orderService.cancelOrderByShop(order.getOrderID());

        Call<ResponseBody> call = orderServiceShopStaff.cancelledByShop(
                PrefLogin.getAuthorizationHeaders(getActivity()),
                order.getOrderID()
        );


        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {



                if(response.code() == 200 )
                {
                    showToastMessage("Successful");
//                    makeRefreshNetworkCall();

                    dataset.remove(order);
                    item_count = item_count - 1;
                    adapter.notifyItemRemoved(position);
                    notifyTitleChanged();
                }
                else if(response.code() == 304)
                {
                    showToastMessage("Not Cancelled !");
                }
                else
                {
                    showToastMessage("Server Error");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

                showToastMessage("Network Request Failed. Check your internet connection !");
            }
        });

    }


    @Override
    public void notifySortChanged() {
        makeRefreshNetworkCall();
    }





    String searchQuery = null;

    @Override
    public void search(final String searchString) {
        searchQuery = searchString;
        makeRefreshNetworkCall();
    }

    @Override
    public void endSearchMode() {
        searchQuery = null;
        makeRefreshNetworkCall();
    }


    @Override
    public void refreshFragment() {
        makeRefreshNetworkCall();
    }
}
