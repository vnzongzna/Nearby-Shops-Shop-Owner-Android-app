package org.nearbyshops.shopkeeperappnew.OrderDetail;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import org.nearbyshops.shopkeeperappnew.API.OrderItemService;
import org.nearbyshops.shopkeeperappnew.DaggerComponentBuilder;
import org.nearbyshops.shopkeeperappnew.Model.Item;
import org.nearbyshops.shopkeeperappnew.Model.Order;
import org.nearbyshops.shopkeeperappnew.Model.Shop;
import org.nearbyshops.shopkeeperappnew.ModelEndpoints.OrderItemEndPoint;
import org.nearbyshops.shopkeeperappnew.Prefrences.PrefLogin;
import org.nearbyshops.shopkeeperappnew.Prefrences.PrefShopHome;
import org.nearbyshops.shopkeeperappnew.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by sumeet on 15/11/16.
 */

public class FragmentOrderDetail extends Fragment implements SwipeRefreshLayout.OnRefreshListener, AdapterOrderDetailNew.NotifyItemClick {


    Order order;

    @Inject
    OrderItemService orderItemService;

    RecyclerView recyclerView;
    AdapterOrderDetailNew adapter;

    public List<Object> dataset = new ArrayList<>();

    GridLayoutManager layoutManager;
    SwipeRefreshLayout swipeContainer;



    final private int limit = 5;
    int offset = 0;
    int item_count = 0;

    boolean isDestroyed;



    public FragmentOrderDetail() {
        DaggerComponentBuilder.getInstance()
                .getNetComponent()
                .Inject(this);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        setRetainInstance(true);
        View rootView = inflater.inflate(R.layout.fragment_order_detail_screen, container, false);



        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
        swipeContainer = (SwipeRefreshLayout)rootView.findViewById(R.id.swipeContainer);

        order = PrefOrderDetail.getOrder(getActivity());


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

        adapter = new AdapterOrderDetailNew(dataset,getActivity(),this);

        layoutManager = new GridLayoutManager(getActivity(),1);


/*
        // add order to the dataset
        if(!dataset.contains(order))
        {
            dataset.add(0,order);
            adapter.notifyItemChanged(0);
        }*/


//        layoutManager.setSpanCount(metrics.widthPixels/400);





//        if(spanCount==0){
//            spanCount = 1;
//        }

//        layoutManager.setSpanCount(spanCount);

//        final int finalSpanCount = spanCount;

        /*layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {

                System.out.println("Position : " + position);


//                if(adapter.getItemViewType(position)==AdapterOrderDetail.TAG_VIEW_HOLDER_ORDER_ITEM)
//                {
//                    return 1;
//                }
//                else if(adapter.getItemViewType(position)==AdapterOrderDetail.TAG_VIEW_HOLDER_ORDER)
//                {
//


//                    DisplayMetrics metrics = new DisplayMetrics();
//                    getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
//                    int spanCount = (int) (metrics.widthPixels / (230 * metrics.density));
//
//                    if (spanCount == 0) {
//                        return 1;
//                    } else {
//                        return spanCount;
//                    }

//                    return 2;
//                }



                if (dataset.get(position) instanceof OrderItem) {

                    return 2;

                }
                else if (dataset.get(position) instanceof Order)
                {

//                    DisplayMetrics metrics = new DisplayMetrics();
//                    getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
//                    int spanCount = (int) (metrics.widthPixels / (230 * metrics.density));
//
//                    if (spanCount == 0) {
//                        return 1;
//                    } else {
//                        return spanCount;
//                    }

                    return 4;
                }

                return 4;
            }
        });
*/

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.HORIZONTAL));



        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if(layoutManager.findLastVisibleItemPosition()==dataset.size())
                {
                    // trigger fetch next page

                    if(layoutManager.findLastVisibleItemPosition() == previous_position)
                    {
                        return;
                    }


                    if((offset+limit)<=item_count)
                    {
                        offset = offset + limit;
                        makeNetworkCall(false);
                    }

                    previous_position = layoutManager.findLastVisibleItemPosition();

                }

            }
        });
    }



    int previous_position = -1;



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

        Shop currentShop = PrefShopHome.getShop(getContext());

        Call<OrderItemEndPoint> call = orderItemService.getOrderItem(
                PrefLogin.getAuthorizationHeaders(getActivity()),
                order.getOrderID(),null,null,null,null,null,null);


        call.enqueue(new Callback<OrderItemEndPoint>() {
            @Override
            public void onResponse(Call<OrderItemEndPoint> call, Response<OrderItemEndPoint> response) {

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
                        dataset.add(0,order);
                    }

                    dataset.addAll(response.body().getResults());
                    adapter.notifyDataSetChanged();
//                    notifyTitleChanged();

                }

                swipeContainer.setRefreshing(false);

//                showToastMessage("Response Code : " + String.valueOf(response.code()));

            }

            @Override
            public void onFailure(Call<OrderItemEndPoint> call, Throwable t) {
                if(isDestroyed)
                {
                    return;
                }

                showToastMessage("Network Request failed !");
                swipeContainer.setRefreshing(false);

            }
        });

    }





    private void showToastMessage(String message)
    {
        if(getActivity()!=null)
        {
            Toast.makeText(getActivity(),message,Toast.LENGTH_SHORT).show();
        }

    }



    @Override
    public void onDestroy() {
        super.onDestroy();
        isDestroyed=true;
    }





    @Override
    public void notifyItemClicked(Item item) {
//        showToastMessage("Item Clicked !");
    }
}
