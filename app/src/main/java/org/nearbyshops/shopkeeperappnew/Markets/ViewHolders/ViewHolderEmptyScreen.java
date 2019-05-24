package org.nearbyshops.shopkeeperappnew.Markets.ViewHolders;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import org.nearbyshops.shopkeeperappnew.Interfaces.NotifyAboutLogin;
import org.nearbyshops.shopkeeperappnew.R;


public class ViewHolderEmptyScreen extends RecyclerView.ViewHolder{



    private Context context;
    private NotifyAboutLogin fragment;




    public static ViewHolderEmptyScreen create(ViewGroup parent, Context context)
    {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_markets_empty_screen, parent, false);

        return new ViewHolderEmptyScreen(view,parent,context);
    }





    public ViewHolderEmptyScreen(View itemView, ViewGroup parent, Context context)
    {
        super(itemView);
        ButterKnife.bind(this,itemView);
        this.context = context;
//        this.fragment = fragment;
    }






    @OnClick(R.id.create_market)
    void selectMarket()
    {
        if(context instanceof VHCreateMarket)
        {
            ((VHCreateMarket) context).createMarketClick();
        }
    }




    public interface VHCreateMarket
    {
        void createMarketClick();
    }

}



