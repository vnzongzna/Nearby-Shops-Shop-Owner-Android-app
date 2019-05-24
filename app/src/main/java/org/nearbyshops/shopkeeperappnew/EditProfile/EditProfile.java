package org.nearbyshops.shopkeeperappnew.EditProfile;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import butterknife.ButterKnife;
import org.nearbyshops.shopkeeperappnew.EditProfile.ChangePassword.FragmentChangePassword;
import org.nearbyshops.shopkeeperappnew.EditProfile.Interfaces.NotifyChangeEmail;
import org.nearbyshops.shopkeeperappnew.EditProfile.Interfaces.NotifyChangePassword;
import org.nearbyshops.shopkeeperappnew.R;


public class EditProfile extends AppCompatActivity implements NotifyChangePassword, NotifyChangeEmail {

    public static final String TAG_FRAGMENT_EDIT = "fragment_edit";
    public static final String TAG_FRAGMENT_CHANGE_PASSWORD = "fragment_change_password";
    public static final String TAG_FRAGMENT_CHANGE_EMAIL = "fragment_change_email";

//    @BindView(R.id.appbar) AppBarLayout appBar;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        Fabric.with(this, new Crashlytics());


//        overridePendingTransition(R.anim.enter_from_right,R.anim.exit_to_left);
        setContentView(R.layout.activity_edit_profile);
        ButterKnife.bind(this);

//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//
        if(getSupportFragmentManager().findFragmentByTag(TAG_FRAGMENT_EDIT)==null)
        {
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.fragment_container,new FragmentEditProfile(),TAG_FRAGMENT_EDIT)
                    .commit();
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
//        overridePendingTransition(R.anim.enter_from_left,R.anim.exit_to_right);
    }




    @Override
    public void changePasswordClick() {

//        appBar.setExpanded(false,true);

        getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right)
                .addToBackStack("change_pass")
                .replace(R.id.fragment_container,new FragmentChangePassword(), EditProfile.TAG_FRAGMENT_CHANGE_PASSWORD)
                .commit();
    }







    @Override
    public void changeEmailClick() {
//
//        getSupportFragmentManager()
//                .beginTransaction()
//                .setCustomAnimations(R.anim.enter_from_right,R.anim.exit_to_left,R.anim.enter_from_left,R.anim.exit_to_right)
//                .addToBackStack("change_email")
//                .replace(R.id.fragment_container,new FragmentChangeEmail(), EditProfile.TAG_FRAGMENT_CHANGE_EMAIL)
//                .commit();
    }

}

