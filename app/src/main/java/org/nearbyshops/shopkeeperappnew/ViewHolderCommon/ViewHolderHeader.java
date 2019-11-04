package org.nearbyshops.shopkeeperappnew.ViewHolderCommon;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import org.nearbyshops.shopkeeperappnew.R;
import org.nearbyshops.shopkeeperappnew.ViewHolderCommon.Models.HeaderData;


public class ViewHolderHeader extends RecyclerView.ViewHolder{



    @BindView(R.id.header) TextView header;


    private Context context;
    private HeaderData item;




    public static ViewHolderHeader create(ViewGroup parent, Context context)
    {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_header_type_simple, parent, false);


        return new ViewHolderHeader(view,context);
    }





    public ViewHolderHeader(View itemView, Context context) {
        super(itemView);

        ButterKnife.bind(this,itemView);
        this.context = context;
    }


    public void setItem(HeaderData data)
    {
        header.setText(data.getHeading());
    }


}

