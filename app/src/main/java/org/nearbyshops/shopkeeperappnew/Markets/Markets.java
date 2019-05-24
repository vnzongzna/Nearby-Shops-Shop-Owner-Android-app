package org.nearbyshops.shopkeeperappnew.Markets;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import butterknife.ButterKnife;
import org.nearbyshops.shopkeeperappnew.Interfaces.NotifyAboutLogin;
import org.nearbyshops.shopkeeperappnew.Interfaces.RefreshFragment;
import org.nearbyshops.shopkeeperappnew.Login.Login;
import org.nearbyshops.shopkeeperappnew.Markets.Interfaces.MarketSelected;
import org.nearbyshops.shopkeeperappnew.Markets.ViewHolders.ViewHolderEmptyScreen;
import org.nearbyshops.shopkeeperappnew.Markets.ViewHolders.ViewHolderSignIn;
import org.nearbyshops.shopkeeperappnew.R;


public class Markets extends AppCompatActivity implements MarketSelected, NotifyAboutLogin, ViewHolderSignIn.VHSignIn , ViewHolderEmptyScreen.VHCreateMarket {


//    public static final String SHOP_DETAIL_INTENT_KEY = "shop_detail";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_market_detail);
        ButterKnife.bind(this);



        if (getSupportFragmentManager().findFragmentByTag("market_fragment") == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, new MarketsFragmentNew(), "market_fragment")
                    .commit();
        }



//        String shopJson = getIntent().getStringExtra(TAG_JSON_STRING);
//        Shop market = UtilityFunctions.provideGson().fromJson(shopJson, Shop.class);


//        getSupportActionBar().setTitle(market.getShopName());




    }






//    @Override
    public void NotifyLogin() {

    }






    @Override
    public void marketSelected() {
        finish();
    }

    @Override
    public void loginSuccess() {

        refreshFragment();
    }



    @Override
    public void loggedOut() {

        refreshFragment();
    }





    void refreshFragment()
    {

        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);

        if(fragment instanceof RefreshFragment)
        {
            ((RefreshFragment) fragment).refreshFragment();
        }
    }







    @Override
    public void signInClick() {

        Intent intent = new Intent(this, Login.class);
        intent.putExtra(Login.TAG_LOGIN_GLOBAL,true);
        startActivityForResult(intent,123);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==123)
        {
            refreshFragment();
        }
    }






    @Override
    public void createMarketClick() {
//        showToastMessage("Create Market Clicked !");


        String url = "https://nearbyshops.org/entrepreneur.html";
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);
    }




    void showToastMessage(String message)
    {
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
    }
}
