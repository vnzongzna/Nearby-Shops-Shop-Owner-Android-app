//package org.nearbyshops.shopkeeperappnew.DeliveryPersonInventory.Fragment;
//
//import android.content.Context;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ProgressBar;
//import android.widget.TextView;
//import androidx.recyclerview.widget.RecyclerView;
//import butterknife.BindView;
//import butterknife.ButterKnife;
//import butterknife.OnClick;
//import org.nearbyshops.shopkeeperappnew.Model.DeliveryAddress;
//import org.nearbyshops.shopkeeperappnew.Model.Order;
//import org.nearbyshops.shopkeeperappnew.R;
//
//import java.util.List;
//
///**
// * Created by sumeet on 13/6/16.
// */
//public class AdapterHandover extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
//
//
//    public static final int VIEW_TYPE_ORDER = 1;
//    public static final int VIEW_TYPE_SCROLL_PROGRESS_BAR = 2;
//
//    private int itemCount;
//
//
//    private List<Order> dataset = null;
//    private Context context;
//
//    NotificationReciever notifications;
//
//
//    public AdapterHandover(List<Order> dataset, Context context, NotificationReciever notifications) {
//        this.dataset = dataset;
//        this.context = context;
//        this.notifications = notifications;
//
//    }
//
//    @Override
//    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//
//
//        View view = null;
//
//        if(viewType==VIEW_TYPE_ORDER)
//        {
//
//            view = LayoutInflater.from(parent.getContext())
//                    .inflate(R.layout.list_item_order_pending_accept_driver_dashboard,parent,false);
//
//            return new ViewHolder(view);
//
//        }
//        else if(viewType==VIEW_TYPE_SCROLL_PROGRESS_BAR)
//        {
//
//            view = LayoutInflater.from(parent.getContext())
//                    .inflate(R.layout.list_item_progress_bar,parent,false);
//
//            return new LoadingViewHolder(view);
//        }
//
//        return null;
//    }
//
//    @Override
//    public void onBindViewHolder(RecyclerView.ViewHolder holderVH, int position) {
//
//
//
//        if(holderVH instanceof ViewHolder) {
//
//            ViewHolder holder = (ViewHolder) holderVH;
//
//
//            if (dataset != null) {
//
//                Order order = dataset.get(position);
//                DeliveryAddress deliveryAddress = order.getDeliveryAddress();
////                OrderStats orderStats = order.getOrderStats();
//
//                holder.orderID.setText("Order ID : " + order.getOrderID());
//                holder.dateTimePlaced.setText("Placed : " + order.getDateTimePlaced().toLocaleString());
//
//
//                holder.deliveryAddressName.setText(deliveryAddress.getName());
//
//                holder.deliveryAddress.setText(deliveryAddress.getDeliveryAddress() + ",\n"
//                        + deliveryAddress.getCity() + " - " + deliveryAddress.getPincode());
//
//                holder.deliveryAddressPhone.setText("Phone : " + deliveryAddress.getPhoneNumber());
//
//                holder.numberOfItems.setText(order.getItemCount() + " Items");
//                holder.orderTotal.setText("| Total : " + (order.getNetPayable()));
//                //holder.currentStatus.setText();
//
//            }
//
//        }
//        else if(holderVH instanceof LoadingViewHolder)
//        {
//
//            LoadingViewHolder viewHolder = (LoadingViewHolder) holderVH;
//
//
//            if(dataset.size() == itemCount)
//            {
//                viewHolder.progressBar.setVisibility(View.GONE);
//            }
//            else
//            {
//                viewHolder.progressBar.setVisibility(View.VISIBLE);
//                viewHolder.progressBar.setIndeterminate(true);
//
//            }
//        }
//    }
//
//
//
//
//    public void setItemCount(int itemCount) {
//        this.itemCount = itemCount;
//    }
//
//
//    @Override
//    public int getItemCount() {
//        return dataset.size()+1;
//    }
//
//
//    class ViewHolder extends RecyclerView.ViewHolder{
//
//
//        @BindView(R.id.order_id)
//        TextView orderID;
//
//        @BindView(R.id.dateTimePlaced)
//        TextView dateTimePlaced;
//
//        @BindView(R.id.deliveryAddressName)
//        TextView deliveryAddressName;
//
//        @BindView(R.id.deliveryAddress)
//        TextView deliveryAddress;
//
//        @BindView(R.id.deliveryAddressPhone)
//        TextView deliveryAddressPhone;
//
//
//        @BindView(R.id.numberOfItems)
//        TextView numberOfItems;
//
//        @BindView(R.id.orderTotal)
//        TextView orderTotal;
//
//        @BindView(R.id.currentStatus)
//        TextView currentStatus;
//
//        @BindView(R.id.accept_handover_button)
//        TextView acceptHandoverButton;
//
//        @BindView(R.id.progress_bar) ProgressBar progressBar;
//
//
//
//        public ViewHolder(View itemView) {
//            super(itemView);
//            ButterKnife.bind(this,itemView);
//        }
//
//
//
//        @OnClick(R.id.accept_handover_button)
//        void onClickConfirmButton(View view)
//        {
//            notifications.notifyAcceptHandover(dataset.get(getLayoutPosition()),acceptHandoverButton,progressBar);
//        }
//
//    }
//
//
//
//
//    public class LoadingViewHolder extends  RecyclerView.ViewHolder{
//
//        @BindView(R.id.progress_bar)
//        ProgressBar progressBar;
//
//        public LoadingViewHolder(View itemView) {
//            super(itemView);
//            ButterKnife.bind(this,itemView);
//        }
//    }
//
//
//
//
//    @Override
//    public int getItemViewType(int position) {
//
//        super.getItemViewType(position);
//
//        if(position == dataset.size())
//        {
//            return VIEW_TYPE_SCROLL_PROGRESS_BAR;
//        }
//        else
//        {
//            return VIEW_TYPE_ORDER;
//        }
//
////        return -1;
//    }
//
//
//
//
//    public interface NotificationReciever{
//
//        void notifyAcceptHandover(Order order, TextView button, ProgressBar progressBar);
//
//    }
//
//}
