//package org.nearbyshops.shopkeeperappnew.DeliveryPersonInventory.Fragment;
//
//import android.os.Bundle;
//import android.util.DisplayMetrics;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ProgressBar;
//import android.widget.TextView;
//import android.widget.Toast;
//import androidx.fragment.app.Fragment;
//import androidx.recyclerview.widget.GridLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
//import okhttp3.ResponseBody;
//import org.nearbyshops.shopkeeperappnew.API.OrderServiceDeliveryPersonSelf;
//import org.nearbyshops.shopkeeperappnew.DaggerComponentBuilder;
//import org.nearbyshops.shopkeeperappnew.Interfaces.NotifySearch;
//import org.nearbyshops.shopkeeperappnew.Interfaces.NotifySort;
//import org.nearbyshops.shopkeeperappnew.Interfaces.NotifyTitleChanged;
//import org.nearbyshops.shopkeeperappnew.Model.Order;
//import org.nearbyshops.shopkeeperappnew.ModelEndpoints.OrderEndPoint;
//import org.nearbyshops.shopkeeperappnew.ModelStatusCodes.OrderStatusHomeDelivery;
//import org.nearbyshops.shopkeeperappnew.Prefrences.PrefLogin;
//import org.nearbyshops.shopkeeperappnew.R;
//import org.nearbyshops.shopkeeperappnew.SlidingLayerSort.PrefSortOrdersHD;
//import retrofit2.Call;
//import retrofit2.Callback;
//import retrofit2.Response;
//
//import javax.inject.Inject;
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * Created by sumeet on 13/6/16.
// */
//
//
//public class HandoverRequestedFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener,
//        AdapterHandover.NotificationReciever , NotifySearch, NotifySort {
//
//
////    @Inject
////    OrderService orderService;
//
//    @Inject
//    OrderServiceDeliveryPersonSelf orderServiceDelivery;
//
//
//    RecyclerView recyclerView;
//
//    AdapterHandover adapter;
//    public List<Order> dataset = new ArrayList<>();
//    GridLayoutManager layoutManager;
//
//    SwipeRefreshLayout swipeContainer;
//
//
//    final private int limit = 5;
//    int offset = 0;
//    int item_count = 0;
//    boolean isDestroyed;
//
//
//
//    public HandoverRequestedFragment() {
//
//        DaggerComponentBuilder.getInstance()
//                .getNetComponent()
//                .Inject(this);
//
//    }
//
//    /**
//     * Returns a new instance of this fragment for the given section
//     * number.
//     */
//    public static HandoverRequestedFragment newInstance() {
//        HandoverRequestedFragment fragment = new HandoverRequestedFragment();
//        Bundle args = new Bundle();
//        fragment.setArguments(args);
//        return fragment;
//    }
//
//
//
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        View rootView = inflater.inflate(R.layout.fragment_delivery_person_inventory, container, false);
//        setRetainInstance(true);
//
//        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
//        swipeContainer = (SwipeRefreshLayout)rootView.findViewById(R.id.swipeContainer);
//
//
//
////        if(savedInstanceState!=null)
////        {
////            // restore instance state
////            deliveryGuySelf = savedInstanceState.getParcelable("savedVehicle");
////        }
//        if(savedInstanceState==null)
//        {
//            makeRefreshNetworkCall();
//        }
//
//
//        setupRecyclerView();
//        setupSwipeContainer();
//
//
//        return rootView;
//    }
//
//
//    void setupSwipeContainer()
//    {
//        if(swipeContainer!=null) {
//
//            swipeContainer.setOnRefreshListener(this);
//            swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
//                    android.R.color.holo_green_light,
//                    android.R.color.holo_orange_light,
//                    android.R.color.holo_red_light);
//        }
//
//    }
//
//
//    void setupRecyclerView()
//    {
//
//        adapter = new AdapterHandover(dataset,getActivity(),this);
//
//        recyclerView.setAdapter(adapter);
//
//        layoutManager = new GridLayoutManager(getActivity(),1);
//        recyclerView.setLayoutManager(layoutManager);
//
//        DisplayMetrics metrics = new DisplayMetrics();
//        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
////        layoutManager.setSpanCount(metrics.widthPixels/400);
//
//
//
//        int spanCount = (int) (metrics.widthPixels/(230 * metrics.density));
//
//        if(spanCount==0){
//            spanCount = 1;
//        }
//
//        layoutManager.setSpanCount(spanCount);
//
//
//
//
//        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
//                super.onScrollStateChanged(recyclerView, newState);
//
//
//                if(offset + limit > layoutManager.findLastVisibleItemPosition()+1)
//                {
//                    return;
//                }
//
//                if(layoutManager.findLastVisibleItemPosition()==dataset.size()-1)
//                {
//                    // trigger fetch next page
//
////                    if(layoutManager.findLastVisibleItemPosition() == previous_position)
////                    {
////                        return;
////                    }
//
//
//
//                    if((offset+limit)<=item_count)
//                    {
//                        offset = offset + limit;
//
//
//                        swipeContainer.post(new Runnable() {
//                            @Override
//                            public void run() {
//
//                                swipeContainer.setRefreshing(true);
//
//                                makeNetworkCall(false,false);
//                            }
//                        });
//
//                    }
//
////                    previous_position = layoutManager.findLastVisibleItemPosition();
//                }
//            }
//        });
//    }
//
////    int previous_position = -1;
//
//
//
//
//
//    @Override
//    public void onRefresh() {
//
////        offset = 0;
//        makeNetworkCall(true,true);
//    }
//
//
//    @Override
//    public void onResume() {
//        super.onResume();
//        notifyTitleChanged();
//        isDestroyed=false;
//    }
//
//
//
//
//    void makeRefreshNetworkCall()
//    {
//
//        swipeContainer.post(new Runnable() {
//            @Override
//            public void run() {
//
//                swipeContainer.setRefreshing(true);
//                onRefresh();
//            }
//        });
//    }
//
//
//
//
//    void makeNetworkCall(final boolean clearDataset, final boolean resetOffset)
//    {
//        if(resetOffset)
//        {
//            offset =0;
//        }
//
//
//
//        int deliveryGuyID = 0;
//
//
//        deliveryGuyID = getActivity().getIntent().getIntExtra("delivery_guy_id",0);
//
//
//
//        String current_sort = "";
//        current_sort = PrefSortOrdersHD.getSort(getContext()) + " " + PrefSortOrdersHD.getAscending(getContext());
//
//
//        Call<OrderEndPoint> call = orderServiceDelivery
//                                        .getOrders(PrefLogin.getAuthorizationHeaders(getActivity()),
//                                                deliveryGuyID,
//                                                null,null,
//                                                false, OrderStatusHomeDelivery.HANDOVER_REQUESTED,
//                                                null,
//                                                null,null,
//                                                null,null,
//                                                null,
//                                                searchQuery,current_sort,limit,offset,null);
//
//
//        call.enqueue(new Callback<OrderEndPoint>() {
//            @Override
//            public void onResponse(Call<OrderEndPoint> call, Response<OrderEndPoint> response) {
//
//                if(isDestroyed)
//                {
//                    return;
//                }
//
//                if(response.body()!= null)
//                {
//                    item_count = response.body().getItemCount();
//                    adapter.setItemCount(item_count);
//
//                    if(clearDataset)
//                    {
//                        dataset.clear();
//                    }
//
//                    dataset.addAll(response.body().getResults());
//                    adapter.notifyDataSetChanged();
//                    notifyTitleChanged();
//
//                }
//                else
//                {
//                    showToastMessage("Response code : " + String.valueOf(response.code()));
//                }
//
//
//                swipeContainer.setRefreshing(false);
//
//            }
//
//            @Override
//            public void onFailure(Call<OrderEndPoint> call, Throwable t) {
//                if(isDestroyed)
//                {
//                    return;
//                }
//
//                showToastMessage("Network Request failed !");
//                swipeContainer.setRefreshing(false);
//            }
//        });
//
//    }
//
//
//
//
//    void showToastMessage(String message)
//    {
//        if(getActivity()!=null)
//        {
//            Toast.makeText(getActivity(),message,Toast.LENGTH_SHORT).show();
//        }
//
//    }
//
//
//
//
//
//
//    @Override
//    public void notifyAcceptHandover(Order order, final TextView button, final ProgressBar progressBar) {
//
////        order.setStatusHomeDelivery(OrderStatusHomeDelivery.HANDOVER_ACCEPTED);
//
//        button.setVisibility(View.INVISIBLE);
//        progressBar.setVisibility(View.VISIBLE);
//
//
//        Call<ResponseBody> call = orderServiceDelivery.acceptOrder(
//                PrefLogin.getAuthorizationHeaders(getActivity()),
//                order.getOrderID());
//
//        call.enqueue(new Callback<ResponseBody>() {
//            @Override
//            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//
//                if(response.code()==200)
//                {
//                    showToastMessage("Order Accepted !");
//                    makeRefreshNetworkCall();
//                }
//                else if(response.code()==401 || response.code()==404)
//                {
//                    showToastMessage("Not Permitted !");
//                }
//                else
//                {
//                    showToastMessage("Response Code : " + String.valueOf(response.code()));
//                }
//
//
//                button.setVisibility(View.VISIBLE);
//                progressBar.setVisibility(View.INVISIBLE);
//
//
//            }
//
//            @Override
//            public void onFailure(Call<ResponseBody> call, Throwable t) {
//
//                showToastMessage("Network Request Failed. Try again !");
//
//
//                button.setVisibility(View.VISIBLE);
//                progressBar.setVisibility(View.INVISIBLE);
//
//
//            }
//        });
//    }
//
//
//
//
////    public DeliveryGuySelf getDeliveryGuySelf() {
////        return deliveryGuySelf;
////    }
////
////    public void setDeliveryGuySelf(DeliveryGuySelf deliveryGuySelf) {
////        this.deliveryGuySelf = deliveryGuySelf;
////    }
////
////
////    @Override
////    public void onSaveInstanceState(Bundle outState) {
////        super.onSaveInstanceState(outState);
////        outState.putParcelable("savedVehicle", deliveryGuySelf);
////    }
//
//
//
//    void notifyTitleChanged()
//    {
//
//        if(getActivity() instanceof NotifyTitleChanged)
//        {
//            ((NotifyTitleChanged)getActivity())
//                    .NotifyTitleChanged(
//                            "Pending Handover (" + String.valueOf(dataset.size())
//                                    + "/" + String.valueOf(item_count) + ")",0);
//
//
//        }
//    }
//
//
//
//
//
//    @Override
//    public void notifySortChanged() {
//        makeRefreshNetworkCall();
//    }
//
//    String searchQuery = null;
//
//    @Override
//    public void search(final String searchString) {
//        searchQuery = searchString;
//        makeRefreshNetworkCall();
//    }
//
//    @Override
//    public void endSearchMode() {
//        searchQuery = null;
//        makeRefreshNetworkCall();
//    }
//
//
//
//
//}
