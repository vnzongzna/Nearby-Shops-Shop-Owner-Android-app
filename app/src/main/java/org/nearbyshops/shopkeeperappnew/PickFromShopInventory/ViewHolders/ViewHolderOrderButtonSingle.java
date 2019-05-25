package org.nearbyshops.shopkeeperappnew.PickFromShopInventory.ViewHolders;

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




public class ViewHolderOrderButtonSingle extends ViewHolderOrder {


    @BindView(R.id.button_single) TextView buttonSingle;
    @BindView(R.id.progress_bar) ProgressBar progressBar;


    private Context context;
    private Order order;
    private Fragment fragment;



    public static ViewHolderOrderButtonSingle create(ViewGroup parent, Context context, Fragment fragment)
    {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_order_button_single,parent,false);

        return new ViewHolderOrderButtonSingle(view,context,fragment);
    }







    public ViewHolderOrderButtonSingle(View itemView, Context context, Fragment fragment) {
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


        if(order.isPickFromShop())
        {
            if(order.getStatusPickFromShop()== OrderStatusPickFromShop.ORDER_PLACED)
            {
                buttonSingle.setText(" Confirm ");
            }
            else if(order.getStatusPickFromShop()==OrderStatusPickFromShop.ORDER_CONFIRMED)
            {
                buttonSingle.setText(" Packed ");
            }
            else if(order.getStatusPickFromShop()==OrderStatusPickFromShop.ORDER_PACKED)
            {
                buttonSingle.setText(" Ready for Pickup ");
            }
            else if(order.getStatusPickFromShop()==OrderStatusPickFromShop.ORDER_READY_FOR_PICKUP)
            {
                buttonSingle.setText(" Payment Received ");
            }
        }
        else
        {

            if(order.getStatusHomeDelivery()== OrderStatusHomeDelivery.ORDER_PLACED)
            {
                buttonSingle.setText(" Confirm ");
            }
            else if(order.getStatusHomeDelivery()==OrderStatusHomeDelivery.ORDER_CONFIRMED)
            {
                buttonSingle.setText(" Packed ");
            }



        }


    }








    @OnClick(R.id.button_single)
    void leftButtonClick()
    {
        if(fragment instanceof ListItemClick)
        {

            if(order.isPickFromShop())
            {
                if(order.getStatusPickFromShop()==OrderStatusPickFromShop.ORDER_PLACED)
                {
                    ((ListItemClick) fragment).confirmOrderPFS(order,getAdapterPosition(),buttonSingle,progressBar);

                }
                else if(order.getStatusPickFromShop()==OrderStatusPickFromShop.ORDER_CONFIRMED)
                {
                    ((ListItemClick) fragment).setOrderPackedPFS(order,getAdapterPosition(),buttonSingle,progressBar);
                }
                else if(order.getStatusPickFromShop()==OrderStatusPickFromShop.ORDER_PACKED)
                {

                    ((ListItemClick) fragment).readyForPickupPFS(order,getAdapterPosition(),buttonSingle,progressBar);

                }
                else if(order.getStatusPickFromShop()==OrderStatusPickFromShop.ORDER_READY_FOR_PICKUP)
                {
                    ((ListItemClick) fragment).paymentReceivedPFS(order,getAdapterPosition(),buttonSingle,progressBar);

                }
            }
            else
            {

                if(order.getStatusHomeDelivery()== OrderStatusHomeDelivery.ORDER_PLACED)
                {
                    ((ListItemClick) fragment).confirmOrderHD(order,getAdapterPosition(),buttonSingle,progressBar);
                }
                else if(order.getStatusHomeDelivery()==OrderStatusHomeDelivery.ORDER_CONFIRMED)
                {
                    ((ListItemClick) fragment).setOrderPackedHD(order,getAdapterPosition(),buttonSingle,progressBar);
                }

            }
        }
    }










    public interface ListItemClick {

        void notifyOrderSelected(Order order);
        void notifyCancelOrder(Order order, int position);

        void confirmOrderPFS(Order order, int position, TextView button, ProgressBar progressBar);
        void setOrderPackedPFS(Order order, int position, TextView button, ProgressBar progressBar);
        void readyForPickupPFS(Order order, int position, TextView button, ProgressBar progressBar);
        void paymentReceivedPFS(Order order, int position, TextView button, ProgressBar progressBar);


        void confirmOrderHD(Order order, int position, TextView button, ProgressBar progressBar);
        void setOrderPackedHD(Order order, int position, TextView button, ProgressBar progressBar);

    }


}


