package org.nearbyshops.shopkeeperappnew.PickFromShopInventory.ViewHolders;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnLongClick;
import org.nearbyshops.shopkeeperappnew.Model.Order;
import org.nearbyshops.shopkeeperappnew.ModelStatusCodes.OrderStatusHomeDelivery;
import org.nearbyshops.shopkeeperappnew.ModelStatusCodes.OrderStatusPickFromShop;
import org.nearbyshops.shopkeeperappnew.OrderHistoryNew.ViewHolders.ViewHolderOrder;
import org.nearbyshops.shopkeeperappnew.R;

import java.util.HashMap;
import java.util.Map;


public class ViewHolderOrderSelectable extends ViewHolderOrder {



    private Context context;
    private Order order;
    private Fragment fragment;

    @BindView(R.id.list_item) ConstraintLayout listItem;

    private RecyclerView.Adapter adapter;
    private Map<Integer,Order> selectedOrders;






    public static ViewHolderOrderSelectable create(
            ViewGroup parent, Context context,
            Fragment fragment, Map<Integer,Order> selectedOrders, RecyclerView.Adapter adapter)
    {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_order,parent,false);

        return new ViewHolderOrderSelectable(view,context,fragment,selectedOrders, adapter);
    }




    public ViewHolderOrderSelectable(View itemView, Context context, Fragment fragment,
                                     Map<Integer,Order> selectedOrders, RecyclerView.Adapter adapter) {

        super(itemView,context,fragment);

        ButterKnife.bind(this, itemView);
        this.context = context;
        this.fragment = fragment;
        this.selectedOrders = selectedOrders;
        this.adapter = adapter;
    }






    @OnClick(R.id.close_button)
    void closeButton(View view) {

        if (fragment instanceof ListItemClick) {
            ((ListItemClick) fragment).notifyCancelOrder(order,getAdapterPosition());
        }
    }






    @OnClick(R.id.list_item)
    void listItemClick ()
    {
        if (fragment instanceof ListItemClick) {

            ((ListItemClick) fragment).notifyOrderSelected(order);
        }
    }




    @OnLongClick(R.id.list_item)
    boolean listItemLongClick()
    {


        if (selectedOrders.containsKey(order.getOrderID()))
        {
            selectedOrders.remove(order.getOrderID());
        }
        else
        {
            selectedOrders.put(order.getOrderID(),order);
        }


        //notifyDataSetChanged();

//        adapter.notifyItemChanged(getLayoutPosition());
        bindOrder();


        if(selectedOrders.size()==0)
        {
            if(fragment instanceof ListItemClick)
            {
                ((ListItemClick) fragment).selectedStopped();
            }
        }
        else if(selectedOrders.size()==1)
        {

            if(fragment instanceof ListItemClick)
            {
                ((ListItemClick) fragment).selectionStarted();
            }
        }


        return true;
    }



    public void setItem (Order order)
    {
        super.setItem(order);
        this.order = order;

        bindOrder();
    }



    void bindOrder()
    {
        if (selectedOrders.containsKey(order.getOrderID())) {

            listItem.setBackgroundResource(R.color.gplus_color_2);
        }
        else
        {
            listItem.setBackgroundResource(R.color.listItemGrey);
        }
    }





    public interface ListItemClick {

        void notifyOrderSelected(Order order);
        void notifyCancelOrder(Order order, int position);


        void selectionStarted();
        void selectedStopped();
    }


}


