package org.nearbyshops.shopkeeperappnew.ItemsInShop;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.squareup.picasso.Picasso;
import okhttp3.ResponseBody;
import org.nearbyshops.shopkeeperappnew.API.ShopItemService;
import org.nearbyshops.shopkeeperappnew.DaggerComponentBuilder;
import org.nearbyshops.shopkeeperappnew.Model.Item;
import org.nearbyshops.shopkeeperappnew.Model.ShopItem;
import org.nearbyshops.shopkeeperappnew.Prefrences.PrefGeneral;
import org.nearbyshops.shopkeeperappnew.Prefrences.PrefLogin;
import org.nearbyshops.shopkeeperappnew.QuickStockEditor.ViewHolders.ViewHolderShopItem;
import org.nearbyshops.shopkeeperappnew.R;
import org.nearbyshops.shopkeeperappnew.ViewHolderCommon.LoadingViewHolder;
import org.nearbyshops.shopkeeperappnew.ViewHolderCommon.Models.EmptyScreenData;
import org.nearbyshops.shopkeeperappnew.ViewHolderCommon.Models.HeaderTitle;
import org.nearbyshops.shopkeeperappnew.ViewHolderCommon.ViewHolderEmptyScreenNew;
import org.nearbyshops.shopkeeperappnew.ViewHolderCommon.ViewHolderHeaderSimple;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import javax.inject.Inject;
import java.util.List;

/**
 * Created by sumeet on 19/12/15.
 */


public class AdapterItemsInShop extends RecyclerView.Adapter<RecyclerView.ViewHolder>{


    private List<Object> dataset;
    private Context context;


    public static final int VIEW_TYPE_SHOP_ITEM = 2;


    public static final int VIEW_TYPE_EMPTY_SCREEN = 5;
    public static final int VIEW_TYPE_HEADER = 3;
    public static final int VIEW_TYPE_SCROLL_PROGRESS_BAR = 4;



    private Fragment fragment;
    private boolean loadMore;




    public AdapterItemsInShop(List<Object> dataset, Context context, Fragment fragment) {

        this.dataset = dataset;
        this.context = context;
        this.fragment = fragment;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = null;

        if(viewType == VIEW_TYPE_SHOP_ITEM)
        {
            return ViewHolderShopItem.create(parent,context,fragment,this,dataset);
        }
        else if(viewType == VIEW_TYPE_HEADER)
        {
            return ViewHolderHeaderSimple.create(parent,context);
        }
        else if(viewType == VIEW_TYPE_SCROLL_PROGRESS_BAR)
        {
            return LoadingViewHolder.create(parent,context);
        }
        else if(viewType==VIEW_TYPE_EMPTY_SCREEN)
        {
            return ViewHolderEmptyScreenNew.create(parent,context);
        }


        return null;
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        if(holder instanceof ViewHolderShopItem)
        {
            ((ViewHolderShopItem) holder).bindShopItem((ShopItem) dataset.get(position));

        }
        else if (holder instanceof ViewHolderHeaderSimple) {

            if (dataset.get(position) instanceof HeaderTitle) {

                ((ViewHolderHeaderSimple) holder).setItem((HeaderTitle) dataset.get(position));
            }

        }
        else if(holder instanceof ViewHolderEmptyScreenNew)
        {
            ((ViewHolderEmptyScreenNew) holder).setItem((EmptyScreenData) dataset.get(position));
        }
        else if (holder instanceof LoadingViewHolder) {

            ((LoadingViewHolder) holder).setLoading(loadMore);
        }

    }


    @Override
    public int getItemViewType(int position) {

        super.getItemViewType(position);

        if(position == dataset.size())
        {
            return VIEW_TYPE_SCROLL_PROGRESS_BAR;
        }
        if (dataset.get(position) instanceof ShopItem)
        {
            return VIEW_TYPE_SHOP_ITEM;
        }
        else if(dataset.get(position) instanceof HeaderTitle)
        {
            return VIEW_TYPE_HEADER;
        }
        else if(dataset.get(position) instanceof EmptyScreenData)
        {
            return VIEW_TYPE_EMPTY_SCREEN;
        }

        return -1;
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