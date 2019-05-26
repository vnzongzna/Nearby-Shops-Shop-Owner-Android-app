package org.nearbyshops.shopkeeperappnew.ShopImageList;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnLongClick;
import com.squareup.picasso.Picasso;
import org.nearbyshops.shopkeeperappnew.Model.ShopImage;
import org.nearbyshops.shopkeeperappnew.Prefrences.PrefGeneral;
import org.nearbyshops.shopkeeperappnew.R;
import org.nearbyshops.shopkeeperappnew.ViewHolderCommon.Models.HeaderTitle;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by sumeet on 19/12/15.
 */


public class Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {



    private int itemCount;


    // keeping track of selections
    Map<Integer, ShopImage> selectedItems = new HashMap<>();
    ShopImage selectedItemSingle;


    private List<Object> dataset;
    private Context context;
    private NotificationsFromAdapter notificationReceiver;
    private Fragment fragment;

    public static final int VIEW_TYPE_TRIP_REQUEST = 1;
    public static final int VIEW_TYPE_HEADER = 5;
    private final static int VIEW_TYPE_PROGRESS_BAR = 6;
//    private final static int VIEW_TYPE_FILTER = 7;
//    private final static int VIEW_TYPE_FILTER_SUBMISSIONS = 8;


    public Adapter(List<Object> dataset, Context context, NotificationsFromAdapter notificationReceiver, Fragment fragment) {

//        DaggerComponentBuilder.getInstance()
//                .getNetComponent().Inject(this);

        this.notificationReceiver = notificationReceiver;
        this.dataset = dataset;
        this.context = context;
        this.fragment = fragment;
    }




    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = null;

        if (viewType == VIEW_TYPE_TRIP_REQUEST) {

            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_item_item_image_new, parent, false);

            return new ViewHolderItemImage(view);
        }
        else if (viewType == VIEW_TYPE_HEADER) {

            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_item_header_type_simple, parent, false);

            return new ViewHolderHeader(view);

        } else if (viewType == VIEW_TYPE_PROGRESS_BAR) {

            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_item_progress_bar, parent, false);

            return new LoadingViewHolder(view);

        }

        return null;
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        if (holder instanceof ViewHolderItemImage) {

            bindTripRequest((ViewHolderItemImage) holder, position);
        }
        else if (holder instanceof ViewHolderHeader) {

            if (dataset.get(position) instanceof HeaderTitle) {
                HeaderTitle header = (HeaderTitle) dataset.get(position);

                ((ViewHolderHeader) holder).header.setText(header.getHeading());
            }

        } else if (holder instanceof LoadingViewHolder) {

            LoadingViewHolder viewHolder = (LoadingViewHolder) holder;

//            int itemCount = 0;



//            if (fragment instanceof ShopImageListFragment) {
//                itemCount = (((ShopImageListFragment) fragment).item_count + 1 );
//            }
////
//            itemCount = dataset.size();




            if(dataset.size() >= itemCount) {



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
    public int getItemViewType(int position) {

        super.getItemViewType(position);

        if (position == dataset.size()) {
            return VIEW_TYPE_PROGRESS_BAR;
        } else if (dataset.get(position) instanceof HeaderTitle) {
            return VIEW_TYPE_HEADER;
        } else if (dataset.get(position) instanceof ShopImage) {
            return VIEW_TYPE_TRIP_REQUEST;
        }

        return -1;
    }


    @Override
    public int getItemCount() {

        return (dataset.size() + 1);
    }




    void bindTripRequest(ViewHolderItemImage holder, int position)
    {

        if(dataset.get(position) instanceof ShopImage)
        {
            ShopImage shopImage = (ShopImage) dataset.get(position);

            holder.imageTitle.setText(shopImage.getCaptionTitle());
            holder.imageDescription.setText(shopImage.getCaption());
            holder.copyrights.setText(shopImage.getCopyrights());


            Drawable drawable = ContextCompat.getDrawable(context, R.drawable.ic_nature_people_white_48px);

//            String imagePath = PrefGeneral.getServiceURL(context) + "/api/v1/TaxiImages/Image/" + "nine_hundred_"+ taxiImage.getImageFilename() + ".jpg";
//            String image_url = PrefGeneral.getServiceURL(context) + "/api/v1/TaxiImages/Image/" + taxiImage.getImageFilename();


            String imagePath = PrefGeneral.getServiceURL(context) + "/api/v1/ShopImage/Image/five_hundred_" + shopImage.getImageFilename() + ".jpg";


            Picasso.get()
                    .load(imagePath)
                    .placeholder(drawable)
                    .into(holder.taxiImage);



            if(selectedItems.containsKey(shopImage.getShopImageID()))
            {
//                holder.listItem.setBackgroundColor(ContextCompat.getColor(context,R.color.gplus_color_2));
//                holder.listItem.animate().scaleXBy(-3);
//                holder.listItem.animate().scaleYBy(-3);
//                holder.listItem.animate().scaleY(-2);

                holder.checkIcon.setVisibility(View.VISIBLE);

            }
            else
            {
//                holder.listItem.setBackgroundColor(ContextCompat.getColor(context,R.color.light_grey));

                holder.checkIcon.setVisibility(View.INVISIBLE);
            }

        }
    }








    class ViewHolderItemImage extends RecyclerView.ViewHolder{


        @BindView(R.id.title)
        TextView imageTitle;
        @BindView(R.id.description)
        TextView imageDescription;
        @BindView(R.id.copyright_info)
        TextView copyrights;
        @BindView(R.id.taxi_image)
        ImageView taxiImage;
        @BindView(R.id.list_item)
        ConstraintLayout listItem;
        @BindView(R.id.check_icon)
        ImageView checkIcon;
//        @BindView(R.id.is_enabled)
//        CheckBox isEnabled;





        public ViewHolderItemImage(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }




        @OnClick(R.id.popup_menu)
        void popupClick(View v)
        {
            PopupMenu menu = new PopupMenu(context,v);

            menu.getMenuInflater().inflate(R.menu.menu_item_image_item,menu.getMenu());

            menu.show();
            menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {

                    switch (item.getItemId())
                    {
                        case R.id.delete :

                            notificationReceiver.deleteClick(
                                    (ShopImage) dataset.get(getLayoutPosition()),
                                    getLayoutPosition()
                            );



                            break;
                    }


                    return false;
                }
            });

        }




        @OnLongClick(R.id.list_item)
        boolean listItemLongClick(View view)
        {

            ShopImage taxiImage = (ShopImage) dataset.get(getLayoutPosition());



            if(selectedItems.containsKey(
                    taxiImage.getShopImageID()
            ))
            {
                selectedItems.remove(taxiImage.getShopImageID());
                checkIcon.setVisibility(View.INVISIBLE);

            }else
            {
                selectedItems.put(taxiImage.getShopImageID(),taxiImage);
                checkIcon.setVisibility(View.VISIBLE);
                selectedItemSingle = taxiImage;
            }



            notificationReceiver.notifyListItemSelected();
//                    notifyItemChanged(getLayoutPosition());



//                    if(selectedItems.containsKey(taxiImage.getImageID()))
//                    {
//
//
//                    }
//                    else
//                    {
//
//                        checkIcon.setVisibility(View.INVISIBLE);
//                    }


            return notificationReceiver.listItemLongClick(view,
                    (ShopImage) dataset.get(getLayoutPosition()),
                    getLayoutPosition()
            );
        }



        @OnClick(R.id.list_item)
        void listItemClick()
        {

            notificationReceiver.listItemClick(
                    (ShopImage) dataset.get(getLayoutPosition()),
                    getLayoutPosition()
            );

        }



//            notifyItemChanged(getLayoutPosition());
        }



    // ViewHolder Class declaration ends






    class ViewHolderHeader extends RecyclerView.ViewHolder{

        @BindView(R.id.header)
        TextView header;

        public ViewHolderHeader(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }

    }// ViewHolder Class declaration ends








    interface NotificationsFromAdapter
    {
        void deleteClick(ShopImage shopImage, int position);
        void notifyListItemSelected();
        void listItemClick(ShopImage shopImage, int position);
        boolean listItemLongClick(View view, ShopImage shopImage, int position);
    }





    class LoadingViewHolder extends  RecyclerView.ViewHolder{

        @BindView(R.id.progress_bar)
        ProgressBar progressBar;

        public LoadingViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }



}
