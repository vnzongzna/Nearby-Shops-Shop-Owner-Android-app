package org.nearbyshops.shopkeeperappnew.ShopStaffHome;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import org.nearbyshops.shopkeeperappnew.DaggerComponentBuilder;
import org.nearbyshops.shopkeeperappnew.EditProfile.EditProfile;
import org.nearbyshops.shopkeeperappnew.EditProfile.FragmentEditProfile;
import org.nearbyshops.shopkeeperappnew.Interfaces.NotifyAboutLogin;
import org.nearbyshops.shopkeeperappnew.Prefrences.PrefLogin;
import org.nearbyshops.shopkeeperappnew.Prefrences.PrefShopHome;
import org.nearbyshops.shopkeeperappnew.R;

public class ShopStaffHomeFragment extends Fragment {

    @BindView(R.id.notice) TextView notice;




    public ShopStaffHomeFragment() {
        DaggerComponentBuilder.getInstance()
                .getNetComponent().Inject(this);
    }



    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        setRetainInstance(true);
        View rootView = inflater.inflate(R.layout.activity_shop_staff_home, container, false);
        ButterKnife.bind(this,rootView);


//        if(getChildFragmentManager().findFragmentByTag(TAG_SERVICE_INDICATOR)==null)
//        {
//            getChildFragmentManager()
//                    .beginTransaction()
//                    .replace(R.id.service_indicator,new ServiceIndicatorFragment(),TAG_SERVICE_INDICATOR)
//                    .commit();
//        }
//


        return rootView;
    }






    @Override
    public void onResume() {
        super.onResume();
//        checkAccountActivation();
    }



//    void checkAccountActivation()
//    {
//        // if account is deactivated notify User
//
//        ShopStaff staff = PrefLogin.getShopStaff(this);
//
//        if(staff!=null && !staff.getEnabled())
//        {
//            notice.setVisibility(View.VISIBLE);
//        }
//        else
//        {
//            notice.setVisibility(View.GONE);
//        }
//
//    }





    @OnClick(R.id.edit_profile)
    void editProfileClick()
    {
        // fetch latest ShopStaff from login endpoint
        // if status is 200 ok open an edit form page

//        Call<ShopStaff> call = staffService.getLogin(
//                PrefLogin.getAuthorizationHeaders(getActivity())
//        );
//
//        call.enqueue(new Callback<ShopStaff>() {
//            @Override
//            public void onResponse(Call<ShopStaff> call, Response<ShopStaff> response) {
//
//                if(response.code()==200)
//                {
//                    PrefLogin.saveShopStaff(response.body(),getActivity());
//
//                    Intent intent = new Intent(getActivity(), EditStaffSelf.class);
//                    intent.putExtra(EditStaffSelfFragment.EDIT_MODE_INTENT_KEY, EditStaffSelfFragment.MODE_UPDATE);
//                    startActivity(intent);
//                }
//                else
//                {
//                    showToastMessage("Failed ! Status code : " + String.valueOf(response.code()));
//                }
//            }
//
//            @Override
//            public void onFailure(Call<ShopStaff> call, Throwable t) {
//
//                showToastMessage("Failed ! Check your internet connection .");
//            }
//        });








        Intent intent = new Intent(getActivity(), EditProfile.class);
        intent.putExtra(FragmentEditProfile.EDIT_MODE_INTENT_KEY, FragmentEditProfile.MODE_UPDATE);
//        intent.putExtra("user_role", User.ROLE_SHOP_STAFF_CODE);
        startActivity(intent);
    }








    @OnClick(R.id.dashboard)
    void dashboardClick()
    {
        startActivity(new Intent(getActivity(),ShopStaffDashboard.class));
    }




    void showToastMessage(String message)
    {
        Toast.makeText(getActivity(),message,Toast.LENGTH_SHORT).show();
    }










    @OnClick(R.id.logout)
    void logoutClick()
    {

        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());

        dialog.setTitle("Confirm Logout !")
                .setMessage("Do you want to log out !")
                .setPositiveButton("Yes",new DialogInterface.OnClickListener(){

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        logout();

                    }
                })
                .setNegativeButton("No",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        showToastMessage("Cancelled !");
                    }
                })
                .show();
    }






    void logout()
    {
        // log out
        PrefLogin.saveUserProfile(null,getActivity());
        PrefLogin.saveCredentials(getActivity(),null,null);
        PrefShopHome.saveShop(null,getActivity());

        // stop location update service

        if(getActivity() instanceof NotifyAboutLogin)
        {
            ((NotifyAboutLogin) getActivity()).loginSuccess();
        }
    }



}
