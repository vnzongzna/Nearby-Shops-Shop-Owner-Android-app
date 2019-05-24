package org.nearbyshops.shopkeeperappnew.HomeDeliveryInventory.Fragment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import org.nearbyshops.shopkeeperappnew.API.OrderServiceShopStaff;
import org.nearbyshops.shopkeeperappnew.DaggerComponentBuilder;
import org.nearbyshops.shopkeeperappnew.Model.DeliveryAddress;
import org.nearbyshops.shopkeeperappnew.Model.Order;
import org.nearbyshops.shopkeeperappnew.Prefrences.PrefLogin;
import org.nearbyshops.shopkeeperappnew.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import javax.inject.Inject;
import java.util.List;

/**
 * Created by sumeet on 13/6/16.
 */
public class Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private List<Order> dataset = null;
    private NotifyConfirmOrder notifyConfirmOrder;
    private Context context;

    public static final int VIEW_TYPE_ORDERS = 2;
    public static final int VIEW_TYPE_SCROLL_PROGRESS_BAR = 4;


    private int itemCount;


    @Inject
    OrderServiceShopStaff orderServiceShopStaff;





    Adapter(List<Order> dataset, NotifyConfirmOrder notifyConfirmOrder, Context context) {
        this.dataset = dataset;
        this.notifyConfirmOrder = notifyConfirmOrder;
        this.context = context;
    }




    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {



        View view = null;

        if(viewType == VIEW_TYPE_ORDERS)
        {

            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_item_order,parent,false);

            return new ViewHolderOrder(view);
        }
        else if(viewType == VIEW_TYPE_SCROLL_PROGRESS_BAR)
        {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_item_progress_bar,parent,false);

            return new LoadingViewHolder(view);
        }

        return null;
    }



    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holderVH, int position) {


        if(holderVH instanceof ViewHolderOrder)
        {
            ViewHolderOrder holder = (ViewHolderOrder) holderVH;

            Order order = dataset.get(position);
            DeliveryAddress deliveryAddress = order.getDeliveryAddress();
//            OrderStats orderStats = order.getOrderStats();

            holder.orderID.setText("Order ID : " + order.getOrderID());
            holder.dateTimePlaced.setText("" + order.getDateTimePlaced().toLocaleString());


            holder.deliveryAddressName.setText(deliveryAddress.getName());

            holder.deliveryAddress.setText(deliveryAddress.getDeliveryAddress() + ",\n"
                    + deliveryAddress.getCity() + " - " + deliveryAddress.getPincode());

            holder.deliveryAddressPhone.setText("Phone : " + deliveryAddress.getPhoneNumber());

//            holder.numberOfItems.setText(orderStats.getItemCount() + " Items");
//            holder.orderTotal.setText("| Total : " + (orderStats.getItemTotal() + order.getDeliveryCharges()));
//            //holder.currentStatus.setText();



            holder.numberOfItems.setText(order.getItemCount() + " Items");
            holder.orderTotal.setText("| Total : " + (order.getNetPayable()));

        }
        else if(holderVH instanceof LoadingViewHolder)
        {

            LoadingViewHolder viewHolder = (LoadingViewHolder) holderVH;

            if(dataset.size() == itemCount)
            {
                viewHolder.progressBar.setVisibility(View.GONE);
            }
            else
            {
                viewHolder.progressBar.setVisibility(View.VISIBLE);
                viewHolder.progressBar.setIndeterminate(true);

            }
        }

    }






    public void setItemCount(int itemCount) {
        this.itemCount = itemCount;
    }

    @Override
    public int getItemCount() {
        return (dataset.size()+1);
    }



    @Override
    public int getItemViewType(int position) {

        super.getItemViewType(position);

        if(position == dataset.size())
        {
            return VIEW_TYPE_SCROLL_PROGRESS_BAR;
        }
        else
        {
            return VIEW_TYPE_ORDERS;
        }

//        return -1;
    }




    class ViewHolderOrder extends RecyclerView.ViewHolder implements View.OnClickListener {


        @BindView(R.id.order_id)
        TextView orderID;

        @BindView(R.id.dateTimePlaced)
        TextView dateTimePlaced;

        @BindView(R.id.deliveryAddressName)
        TextView deliveryAddressName;

        @BindView(R.id.deliveryAddress)
        TextView deliveryAddress;

        @BindView(R.id.deliveryAddressPhone)
        TextView deliveryAddressPhone;


        @BindView(R.id.numberOfItems)
        TextView numberOfItems;

        @BindView(R.id.orderTotal)
        TextView orderTotal;

        @BindView(R.id.currentStatus)
        TextView currentStatus;

//        @BindView(R.id.confirmOrderButton)
        TextView confirmOrderButton;

        @BindView(R.id.progress_bar) ProgressBar progressBar;


        public ViewHolderOrder(View itemView) {
            super(itemView);

            ButterKnife.bind(this,itemView);
            itemView.setOnClickListener(this);
        }


//        @OnClick(R.id.confirmOrderButton)
        void onClickConfirmButton(View view)
        {
            Order order = dataset.get(getLayoutPosition());

            confirmOrderButton.setVisibility(View.INVISIBLE);
            progressBar.setVisibility(View.VISIBLE);

            Call<ResponseBody> call = orderServiceShopStaff.confirmOrder(
                    PrefLogin.getAuthorizationHeaders(context),
                    order.getOrderID());

            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                    if(response.code()==200)
                    {
                        showToastMessage("Order Confirmed !");

                        notifyConfirmOrder.notifyOrderCOnfirmed(dataset.get(getLayoutPosition()),getLayoutPosition());

//                    makeRefreshNetworkCall();
                        dataset.remove(getLayoutPosition());
                        notifyItemRemoved(getLayoutPosition());

                    }
                    else
                    {
                        showToastMessage("Failed Status : " + String.valueOf(response.code()));
                    }



                    confirmOrderButton.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.INVISIBLE);

                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {

                    showToastMessage("Network Request Failed. Try again !");


                    confirmOrderButton.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.INVISIBLE);

                }
            });


        }


        @OnClick(R.id.close_button)
        void closeButton(View view)
        {
            notifyConfirmOrder.notifyCancelOrder(dataset.get(getLayoutPosition()),getLayoutPosition());
        }

        @Override
        public void onClick(View v) {
            notifyConfirmOrder.notityOrderSelected(dataset.get(getLayoutPosition()));
        }


    }




    public class LoadingViewHolder extends  RecyclerView.ViewHolder{

        @BindView(R.id.progress_bar)
        ProgressBar progressBar;

        public LoadingViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }



    void showToastMessage(String message)
    {
        Toast.makeText(context,message,Toast.LENGTH_SHORT).show();
    }




    interface NotifyConfirmOrder{
        void notifyOrderCOnfirmed(Order order, int position);
        void notifyCancelOrder(Order order, int position);
        void notityOrderSelected(Order order);
    }

}
