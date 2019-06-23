package org.nearbyshops.shopkeeperappnew.OrdersInventory.Fragment;

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
import org.nearbyshops.shopkeeperappnew.ApplicationState.ApplicationState;
import org.nearbyshops.shopkeeperappnew.DaggerComponentBuilder;
import org.nearbyshops.shopkeeperappnew.OrdersInventory.HomeDeliveryInventory.HomeDelivery;
import org.nearbyshops.shopkeeperappnew.Interfaces.*;
import org.nearbyshops.shopkeeperappnew.Model.Order;
import org.nearbyshops.shopkeeperappnew.ModelEndpoints.OrderEndPoint;
import org.nearbyshops.shopkeeperappnew.ModelStatusCodes.OrderStatusHomeDelivery;
import org.nearbyshops.shopkeeperappnew.OrderDetail.OrderDetail;
import org.nearbyshops.shopkeeperappnew.OrderDetail.PrefOrderDetail;
import org.nearbyshops.shopkeeperappnew.OrderHistory.SlidingLayerSort.PrefSortOrders;
import org.nearbyshops.shopkeeperappnew.OrderHistory.ViewHolders.ViewHolderOrder;
import org.nearbyshops.shopkeeperappnew.OrdersInventory.ViewHolders.ViewHolderOrderButtonSingle;
import org.nearbyshops.shopkeeperappnew.OrdersInventory.ViewHolders.ViewHolderOrderSelectable;
import org.nearbyshops.shopkeeperappnew.OrdersInventory.ViewHolders.ViewHolderOrderWithDeliveryProfile;
import org.nearbyshops.shopkeeperappnew.Prefrences.PrefLogin;
import org.nearbyshops.shopkeeperappnew.R;
import org.nearbyshops.shopkeeperappnew.SelectDeliveryGuy.SelectDeliveryGuy;
import org.nearbyshops.shopkeeperappnew.StaffListDelivery.DeliveryGuyList;
import org.nearbyshops.shopkeeperappnew.ViewHolderCommon.Models.EmptyScreenData;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;




public class OrdersInventoryFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener,
        ViewHolderOrderButtonSingle.ListItemClick, ViewHolderOrder.ListItemClick,
        ViewHolderOrderWithDeliveryProfile.ListItemClick,
        NotifySort, NotifySearch, RefreshFragment,
        ViewHolderOrderSelectable.ListItemClick,
        HomeDelivery.HandoverClicked {


//    @Inject
//    OrderService orderService;

    @Inject
    OrderServiceShopStaff orderServiceShopStaff;

    private RecyclerView recyclerView;
    private Adapter adapter;

    public List<Object> dataset = new ArrayList<>();

    private GridLayoutManager layoutManager;
    private SwipeRefreshLayout swipeContainer;


    final private int limit = 5;
    private int offset = 0;
    private int item_count = 0;


    private boolean isDestroyed;



    public OrdersInventoryFragment() {

        DaggerComponentBuilder.getInstance()
                .getNetComponent()
                .Inject(this);

    }


    public static OrdersInventoryFragment newInstance(int orderStatus, boolean isPickFromShop) {
        OrdersInventoryFragment fragment = new OrdersInventoryFragment();
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





    private void makeNetworkCall(final boolean clearDataset)
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
                    isPickFromShop,
                    orderStatusHD, orderStatusPFS, deliveryGuyID,
                    null,null,
                    null,null,null,
                    searchQuery, current_sort,limit,offset,null);







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



                    if(offset+limit >= item_count)
                    {
                        adapter.setLoadMore(false);
                    }
                    else
                    {
                        adapter.setLoadMore(true);
                    }


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



    private void refreshConfirmedFragment()
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
    public void selectionStarted() {

    }

    @Override
    public void selectedStopped() {

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

    @Override
    public void acceptHandover(Order order, int position, TextView button, ProgressBar progressBar) {

    }


    @Override
    public void cancelHandoverHD(Order order, int position, TextView button, ProgressBar progressBar) {

        button.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);


        Call<ResponseBody> call = orderServiceShopStaff.undoHandover(
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
                    showToastMessage("Handover reversed !");

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
    public void acceptReturnHD(Order order, int position, TextView button, ProgressBar progressBar) {

        button.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);


        Call<ResponseBody> call = orderServiceShopStaff.acceptReturn(
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
                    showToastMessage("Return Accepted !");

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
    public void unpackOrderHD(Order order, int position, TextView button, ProgressBar progressBar) {


        button.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);



        Call<ResponseBody> call = orderServiceShopStaff.unpackOrder(
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
                    showToastMessage("Order Deleted !");

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
    public void paymentReceivedHD(Order order, int position, TextView button, ProgressBar progressBar) {

        button.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);


        Call<ResponseBody> call = orderServiceShopStaff.paymentReceived(
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
                    showToastMessage("Done !");

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






    private String searchQuery = null;

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







    private int selectedDeliveryGuy = 0;
    private ArrayList<Order> selected = new ArrayList<>();



    private void handoverToDeliveryStart()
    {


        if(adapter.getSelectedOrders().size()==0)
        {
            showToastMessage("No Orders Selected !");

            return;
        }


        selected = new ArrayList<>();

        for (Map.Entry<Integer, Order> entry : adapter.getSelectedOrders().entrySet())
        {
//            System.out.println(entry.getKey() + "/" + entry.getValue());

            selected.add(entry.getValue());

        }

        //intent.putExtra("selected",selected);

        ApplicationState.getInstance().getSelectedOrdersForDelivery().clear();
        ApplicationState.getInstance().getSelectedOrdersForDelivery().addAll(selected);


//        Intent intent = new Intent(getActivity(), ConfirmItemsForDelivery.class);
//        getActivity().startActivity(intent);




        Intent intent = new Intent(getActivity(), DeliveryGuyList.class);
        intent.putExtra("select_delivery_guy",true);

        startActivityForResult(intent,123);

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==123 && resultCode==456)
        {
            selectedDeliveryGuy = data.getIntExtra("delivery_guy_id",0);
            handoverTODelivery();
        }
        else if(requestCode==562 && resultCode==458)
        {

            deliveryGuyID=data.getIntExtra("delivery_guy_id",-1);

            if(deliveryGuyID==-1)
            {
                deliveryGuyID=null;
            }


            makeRefreshNetworkCall();
        }
    }





    private void handoverTODelivery()
    {

        Call<ResponseBody> call
                = orderServiceShopStaff.handoverToDelivery(
                PrefLogin.getAuthorizationHeaders(getActivity()),
                selectedDeliveryGuy,
                selected);


        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {


                if(response.code() == 200)
                {
                    showToastMessage("Handover Successful !");
                    adapter.getSelectedOrders().clear();

                    makeRefreshNetworkCall();
                }
                else
                {
                    showToastMessage("Error Code : " + String.valueOf(response.code()));
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

                showToastMessage("Network Request failed. Try again !");
            }
        });

    }





    @Override
    public void handoverClicked() {

        int orderStatus = getArguments().getInt("order_status");
        boolean isPickFromShop = getArguments().getBoolean("is_pick_from_shop");


        if(!isPickFromShop && orderStatus == OrderStatusHomeDelivery.ORDER_PACKED)
        {
            handoverToDeliveryStart();
        }
        else if(!isPickFromShop && orderStatus==OrderStatusHomeDelivery.HANDOVER_REQUESTED)
        {
            selectDeliveryGuy();
        }
        else if(!isPickFromShop && orderStatus==OrderStatusHomeDelivery.OUT_FOR_DELIVERY)
        {
            selectDeliveryGuy();
        }
        else if(!isPickFromShop && orderStatus==OrderStatusHomeDelivery.RETURN_REQUESTED)
        {
            selectDeliveryGuy();
        }
        else if(!isPickFromShop && orderStatus==OrderStatusHomeDelivery.RETURNED_ORDERS)
        {
            handoverToDeliveryStart();
        }


    }







    private Integer deliveryGuyID = null;



    private void selectDeliveryGuy()
    {

        int orderStatus = getArguments().getInt("order_status");


        Intent intent = new Intent(getActivity(), SelectDeliveryGuy.class);
        intent.putExtra("order_status",orderStatus);
        startActivityForResult(intent,562);
    }

}
