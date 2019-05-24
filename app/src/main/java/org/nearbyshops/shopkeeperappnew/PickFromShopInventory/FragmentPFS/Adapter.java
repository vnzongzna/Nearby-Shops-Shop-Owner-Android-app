package org.nearbyshops.shopkeeperappnew.PickFromShopInventory.FragmentPFS;

import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import org.nearbyshops.shopkeeperappnew.Model.Order;
import org.nearbyshops.shopkeeperappnew.ModelStatusCodes.OrderStatusPickFromShop;
import org.nearbyshops.shopkeeperappnew.OrderHistoryNew.ViewHolders.ViewHolderOrder;
import org.nearbyshops.shopkeeperappnew.PickFromShopInventory.ViewHolders.ViewHolderOrderButtonSingle;
import org.nearbyshops.shopkeeperappnew.ViewHolderCommon.LoadingViewHolder;
import org.nearbyshops.shopkeeperappnew.ViewHolderCommon.Models.EmptyScreenData;
import org.nearbyshops.shopkeeperappnew.ViewHolderCommon.ViewHolderEmptyScreenNew;

import java.util.List;

/**
 * Created by sumeet on 13/6/16.
 */
class Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private List<Object> dataset = null;

    public static final int VIEW_TYPE_ORDER = 1;
    public static final int VIEW_TYPE_ORDER_WITH_BUTTON = 2;

    public static final int VIEW_TYPE_SCROLL_PROGRESS_BAR = 3;
    public static final int VIEW_TYPE_EMPTY_SCREEN = 4;


    private Fragment fragment;

    private boolean loadMore;



    Adapter(List<Object> dataset, Fragment fragment) {
        this.dataset = dataset;
        this.fragment = fragment;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = null;

        if(viewType==VIEW_TYPE_ORDER)
        {
            return ViewHolderOrder.create(parent,parent.getContext(),fragment);
        }
        else if(viewType == VIEW_TYPE_ORDER_WITH_BUTTON)
        {
            return ViewHolderOrderButtonSingle.create(parent,parent.getContext(),fragment);
        }
        else if (viewType == VIEW_TYPE_SCROLL_PROGRESS_BAR) {

            return LoadingViewHolder.create(parent,parent.getContext());
        }
        else if(viewType==VIEW_TYPE_EMPTY_SCREEN)
        {
            return ViewHolderEmptyScreenNew.create(parent,parent.getContext());
        }



        return null;
    }




    @Override
    public int getItemViewType(int position) {
        super.getItemViewType(position);

        if(position == dataset.size())
        {
            return VIEW_TYPE_SCROLL_PROGRESS_BAR;
        }
        else if(dataset.get(position) instanceof Order)
        {
            if(((Order) dataset.get(position)).getStatusPickFromShop()== OrderStatusPickFromShop.DELIVERED)
            {
                return VIEW_TYPE_ORDER;
            }
            else
            {
                return VIEW_TYPE_ORDER_WITH_BUTTON;
            }

        }
        else if(dataset.get(position) instanceof EmptyScreenData)
        {
            return VIEW_TYPE_EMPTY_SCREEN;
        }


        return -1;
    }





    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {




        if(holder instanceof ViewHolderOrderButtonSingle)
        {
            ((ViewHolderOrderButtonSingle) holder).setItem((Order) dataset.get(position));
        }
        else if(holder instanceof ViewHolderOrder)
        {
            ((ViewHolderOrder) holder).setItem((Order) dataset.get(position));
        }
        else if (holder instanceof LoadingViewHolder) {

            ((LoadingViewHolder) holder).setLoading(loadMore);
        }
        else if(holder instanceof ViewHolderEmptyScreenNew)
        {
            ((ViewHolderEmptyScreenNew) holder).setItem((EmptyScreenData) dataset.get(position));
        }

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
