package org.nearbyshops.shopkeeperappnew.OrderHistory;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import org.nearbyshops.shopkeeperappnew.Model.Order;
import org.nearbyshops.shopkeeperappnew.OrderHistory.ViewHolders.ViewHolderOrder;
import org.nearbyshops.shopkeeperappnew.ViewHolderCommon.LoadingViewHolder;
import org.nearbyshops.shopkeeperappnew.ViewHolderCommon.Models.EmptyScreenData;
import org.nearbyshops.shopkeeperappnew.ViewHolderCommon.ViewHolderEmptyScreen;

import java.util.List;

/**
 * Created by sumeet on 13/6/16.
 */
class AdapterOrders extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private List<Object> dataset = null;


    public static final int VIEW_TYPE_ORDER = 1;

    public static final int VIEW_TYPE_EMPTY_SCREEN = 3;
    private final static int VIEW_TYPE_PROGRESS_BAR = 6;


    private boolean loadMore;

    private Context context;
    private Fragment fragment;




    AdapterOrders(List<Object> dataset, Fragment fragment, Context context) {
        this.dataset = dataset;
        this.fragment = fragment;
        this.context = context;
    }





    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = null;


        if(viewType==VIEW_TYPE_ORDER)
        {
            return ViewHolderOrder.create(parent,context,fragment);
        }
        else if (viewType == VIEW_TYPE_PROGRESS_BAR) {

            return LoadingViewHolder.create(parent,context);
        }
        else if(viewType==VIEW_TYPE_EMPTY_SCREEN)
        {
            return ViewHolderEmptyScreen.create(parent,context);
        }


        return null;
    }


    @Override
    public int getItemViewType(int position) {

        super.getItemViewType(position);

        if(position == dataset.size())
        {
            return VIEW_TYPE_PROGRESS_BAR;
        }
        else if(dataset.get(position) instanceof Order)
        {
            return VIEW_TYPE_ORDER;
        }
        else if(dataset.get(position) instanceof EmptyScreenData)
        {
            return VIEW_TYPE_EMPTY_SCREEN;
        }

        return -1;
    }



    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holderVH, int position) {


        if(holderVH instanceof ViewHolderOrder)
        {

            ((ViewHolderOrder) holderVH).setItem((Order) dataset.get(position));
        }
        else if (holderVH instanceof LoadingViewHolder) {

            ((LoadingViewHolder) holderVH).setLoading(loadMore);

        }
        else if(holderVH instanceof ViewHolderEmptyScreen)
        {
            ((ViewHolderEmptyScreen) holderVH).setItem((EmptyScreenData) dataset.get(position));
        }

    }



    void showLog(String message)
    {
        Log.d("order_status",message);
    }



    @Override
    public int getItemCount() {
        return (dataset.size()+1);
    }





    public void setLoadMore(boolean loadMore)
    {
        this.loadMore = loadMore;
    }


}
