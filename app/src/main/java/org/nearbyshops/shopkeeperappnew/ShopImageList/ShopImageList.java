package org.nearbyshops.shopkeeperappnew.ShopImageList;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import butterknife.ButterKnife;
import butterknife.OnClick;
import org.nearbyshops.shopkeeperappnew.EditShopImage.EditShopImage;
import org.nearbyshops.shopkeeperappnew.EditShopImage.EditShopImageFragment;
import org.nearbyshops.shopkeeperappnew.R;


public class ShopImageList extends AppCompatActivity {


    public static final String TAG_STEP_ONE = "tag_step_one";
    public static final String TAG_STEP_TWO = "tag_step_two";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


//        overridePendingTransition(R.anim.enter_from_right,R.anim.exit_to_left);
        setContentView(R.layout.activity_shop_image_list);
        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.white));
//        toolbar.setTitle("Forgot Password");
        setSupportActionBar(toolbar);


        if(savedInstanceState==null)
        {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container,new ShopImageListFragment(),TAG_STEP_ONE)
                    .commitNow();
        }


    }




    @Override
    public void onBackPressed() {
        super.onBackPressed();
//        overridePendingTransition(R.anim.enter_from_left,R.anim.exit_to_right);
    }


    void showToastMessage(String message)
    {
        Toast.makeText(this,message, Toast.LENGTH_SHORT).show();
    }









    @OnClick(R.id.fab)
    void fabClick()
    {
        int shopId = getIntent().getIntExtra("shop_id",0);

        Intent intent = new Intent(this, EditShopImage.class);
        intent.putExtra(EditShopImageFragment.EDIT_MODE_INTENT_KEY, EditShopImageFragment.MODE_ADD);
        intent.putExtra(EditShopImageFragment.SHOP_ID_INTENT_KEY,shopId);
        startActivity(intent);

    }






//    @OnClick({R.id.filters})
    void showFilters()
    {
        showToastMessage("Filters Click !");
//        FilterTaxi filterTaxi = new FilterTaxi();
//        filterTaxi.show(getActivity().getSupportFragmentManager(),"filter_taxi");
    }

}
