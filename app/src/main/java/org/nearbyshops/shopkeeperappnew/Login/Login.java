package org.nearbyshops.shopkeeperappnew.Login;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import org.nearbyshops.shopkeeperappnew.Interfaces.NotifyAboutLogin;
import org.nearbyshops.shopkeeperappnew.R;


public class Login extends AppCompatActivity implements NotifyAboutLogin {


    public static final String TAG_LOGIN_FRAGMENT = "tag_login_fragment";

    public static final String TAG_LOGIN_GLOBAL  = "tag_login_global";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);

//        overridePendingTransition(R.anim.enter_from_right,R.anim.exit_to_left);
        setContentView(R.layout.activity_login_new);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.white));
        toolbar.setTitle("Login");
        setSupportActionBar(toolbar);




        if(savedInstanceState==null)
        {
            if(getIntent().getBooleanExtra(TAG_LOGIN_GLOBAL,false))
            {

                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container,new LoginGlobalFragment(),TAG_LOGIN_FRAGMENT)
                        .commitNow();

            }
            else
            {
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container,new LoginFragment(),TAG_LOGIN_FRAGMENT)
                        .commitNow();

            }

        }

    }






    @Override
    public void onBackPressed() {
        super.onBackPressed();
//        overridePendingTransition(R.anim.enter_from_left,R.anim.exit_to_right);
    }

    public static final int RESULT_CODE_LOGIN_SUCCESS  = 1;






    @Override
    public void loginSuccess() {

        setResult(RESULT_OK);
        finish();
    }

    @Override
    public void loggedOut() {

    }
}
