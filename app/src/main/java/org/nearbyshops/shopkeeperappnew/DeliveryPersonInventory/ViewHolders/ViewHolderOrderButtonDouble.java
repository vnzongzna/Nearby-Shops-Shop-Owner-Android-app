package org.nearbyshops.shopkeeperappnew.DeliveryPersonInventory.ViewHolders;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import org.nearbyshops.shopkeeperappnew.Model.Order;
import org.nearbyshops.shopkeeperappnew.ModelStatusCodes.OrderStatusHomeDelivery;
import org.nearbyshops.shopkeeperappnew.ModelStatusCodes.OrderStatusPickFromShop;
import org.nearbyshops.shopkeeperappnew.OrderHistoryNew.ViewHolders.ViewHolderOrder;
import org.nearbyshops.shopkeeperappnew.R;


public class ViewHolderOrderButtonDouble extends ViewHolderOrder {


    @BindView(R.id.button_left) TextView buttonLeft;
    @BindView(R.id.progress_left) ProgressBar progressBar;

    @BindView(R.id.button_right) TextView buttonRight;
    @BindView(R.id.progress_right) ProgressBar progressRight;




    private Context context;
    private Order order;
    private Fragment fragment;



    public static ViewHolderOrderButtonDouble create(ViewGroup parent, Context context, Fragment fragment)
    {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_order_button_double,parent,false);

        return new ViewHolderOrderButtonDouble(view,context,fragment);
    }







    public ViewHolderOrderButtonDouble(View itemView, Context context, Fragment fragment) {
        super(itemView,context,fragment);

        ButterKnife.bind(this, itemView);
        this.context = context;
        this.fragment = fragment;
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






    public void setItem (Order order)
    {
        super.setItem(order);
        this.order = order;
//        orderID.append(" - Append from Buttons");


        if(!order.isPickFromShop())
        {

            if(order.getStatusHomeDelivery()==OrderStatusHomeDelivery.OUT_FOR_DELIVERY)
            {
                buttonLeft.setText(" Delivered ");
                buttonRight.setText(" Return ");
            }

        }


    }








    @OnClick(R.id.button_left)
    void leftButtonClick()
    {
        if(fragment instanceof ListItemClick)
        {

            if(!order.isPickFromShop())
            {
                if(order.getStatusHomeDelivery()== OrderStatusHomeDelivery.OUT_FOR_DELIVERY)
                {
                    ((ListItemClick) fragment).deliveredHD(order,getAdapterPosition(), buttonLeft,progressBar);
                }
            }
        }
    }




    @OnClick(R.id.button_right)
    void rightButtonClick()
    {
        if(fragment instanceof ListItemClick)
        {

            if(!order.isPickFromShop())
            {
                if(order.getStatusHomeDelivery()== OrderStatusHomeDelivery.OUT_FOR_DELIVERY)
                {
                    ((ListItemClick) fragment).returnOrderHD(order,getAdapterPosition(), buttonRight,progressRight);
                }
            }
        }
    }












    public interface ListItemClick {

        void notifyOrderSelected(Order order);
        void notifyCancelOrder(Order order, int position);

        void deliveredHD(Order order, int position, TextView button, ProgressBar progressBar);
        void returnOrderHD(Order order, int position, TextView button, ProgressBar progressBar);

    }


}


