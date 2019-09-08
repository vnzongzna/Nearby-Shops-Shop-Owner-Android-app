package org.nearbyshops.shopkeeperappnew.ViewHolderCommon;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import org.nearbyshops.shopkeeperappnew.R;
import org.nearbyshops.shopkeeperappnew.ViewHolderCommon.Models.EmptyScreenData;


public class ViewHolderEmptyScreen extends RecyclerView.ViewHolder{



    private Context context;
    private Fragment fragment;

    @BindView(R.id.message) TextView message;
    @BindView(R.id.button) TextView button;


    private EmptyScreenData data;

//    Create your own Market and help local Economy ... Its free !



    public static ViewHolderEmptyScreen create(ViewGroup parent, Context context, Fragment fragment)
    {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_empty_screen, parent, false);

        return new ViewHolderEmptyScreen(view,parent,context, fragment);
    }





    public ViewHolderEmptyScreen(View itemView, ViewGroup parent, Context context, Fragment fragment)
    {
        super(itemView);
        ButterKnife.bind(this,itemView);
        this.context = context;
        this.fragment = fragment;
    }






    @OnClick(R.id.button)
    void selectMarket()
    {
        if(fragment instanceof VHEmptyScreen)
        {
            ((VHEmptyScreen) fragment).buttonClick(data.getUrlForButtonClick());
        }
    }






    public void setItem(EmptyScreenData data)
    {
        this.data = data;

        message.setText(data.getMessage());
        button.setText(data.getButtonText());
    }




    public interface VHEmptyScreen
    {
        void buttonClick(String url);
    }

}



