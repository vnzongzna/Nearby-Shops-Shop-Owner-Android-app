package org.nearbyshops.shopkeeperappnew.Login;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import okhttp3.OkHttpClient;
import org.nearbyshops.shopkeeperappnew.API_SDS.UserServiceGlobal;
import org.nearbyshops.shopkeeperappnew.DaggerComponentBuilder;
import org.nearbyshops.shopkeeperappnew.Interfaces.NotifyAboutLogin;
import org.nearbyshops.shopkeeperappnew.ModelRoles.User;
import org.nearbyshops.shopkeeperappnew.MyApplication;
import org.nearbyshops.shopkeeperappnew.Prefrences.PrefLogin;
import org.nearbyshops.shopkeeperappnew.Prefrences.PrefLoginGlobal;
import org.nearbyshops.shopkeeperappnew.Prefrences.PrefServiceConfig;
import org.nearbyshops.shopkeeperappnew.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import javax.inject.Inject;


/**
 * Created by sumeet on 19/4/17.
 */




public class LoginGlobalFragment extends Fragment {

    public static final String TAG_SERVICE_INDICATOR = "service_indicator";

    boolean isDestroyed = false;


    @Inject Gson gson;
//    @BindView(R.id.ccp) CountryCodePicker ccp;
    @BindView(R.id.username)
TextInputEditText username;
    @BindView(R.id.password)
    TextInputEditText password;
    @BindView(R.id.progress_bar_login) ProgressBar progressBar;

//    @BindView(R.id.clear)TextView clear;
//    @BindView(R.id.select_service) TextView selectAutomatic;


    public LoginGlobalFragment() {

        DaggerComponentBuilder.getInstance()
                .getNetComponent()
                .Inject(this);
    }





    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);


        setRetainInstance(true);
        View rootView = inflater.inflate(R.layout.fragment_login_global, container, false);
        ButterKnife.bind(this,rootView);



        return rootView;
    }




    void showToastMessage(String message)
    {
        Toast.makeText(getActivity(),message, Toast.LENGTH_SHORT).show();
    }





    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==1&& resultCode==1)
        {
//            Fragment fragment = getChildFragmentManager()
//                    .findFragmentByTag(TAG_SERVICE_INDICATOR);

//            if(fragment instanceof ServiceIndicatorFragment)
//            {
//                ((ServiceIndicatorFragment)fragment).refresh();
//            }
        }
        else if(requestCode==5)
        {
//            Fragment fragment = getChildFragmentManager()
//                    .findFragmentByTag(TAG_SERVICE_INDICATOR);
//
//            if(fragment instanceof ServiceIndicatorFragment)
//            {
////            showToastMessage("Clear Click : Inside If Block");
//                ((ServiceIndicatorFragment)fragment).refresh();
//            }
        }
    }


//    @OnClick(R.id.clear)
//    void clear()
//    {
//        PrefGeneral.saveServiceURL(null);
//        PrefServiceConfig.saveServiceConfig(null,getActivity());
//
//
//        Fragment fragment = getChildFragmentManager()
//                .findFragmentByTag(TAG_SERVICE_INDICATOR);
//
//        if(fragment instanceof ServiceIndicatorFragment)
//        {
//            ((ServiceIndicatorFragment)fragment).refresh();
//        }
//    }







//    @OnClick(R.id.sign_up)
//    void signUp()
//    {

//        PrefrenceSignUp.saveUser(null,getActivity());
//        Intent intent = new Intent(getActivity(), SignUp.class);
//        startActivity(intent);
//    }





//    @OnClick(R.id.forgot_password)
//    void forgotPasswordClick()
//    {

//        PrefrenceForgotPassword.saveUser(null,getActivity());
//        Intent intent = new Intent(getActivity(), ForgotPassword.class);
//        startActivity(intent);
//    }





//    @OnTextChanged(R.id.username)
//    void usernameChanged()
//    {
////        UtilityLogin.saveUsername(getActivity(),username.getText().toString());
//    }
//
//
//
//    @OnTextChanged(R.id.password)
//    void passwordChanged()
//    {
////        UtilityLogin.savePassword(getActivity(),password.getText().toString());
//    }



    boolean validateData()
    {
        boolean isValid = true;
//        boolean phoneValidity = false;
//        boolean emailValidity = false;
//
//
//        emailValidity = EmailValidator.getInstance().isValid(username.getText().toString());
//        phoneValidity = android.util.Patterns.PHONE.matcher(username.getText().toString()).matches();



        if(password.getText().toString().isEmpty())
        {
            password.requestFocus();
            password.setError("Password cannot be empty !");
            isValid = false;
        }


//        if(!emailValidity && !phoneValidity)
//        {
//            username.setError("Not a valid email or phone !");
//            username.requestFocus();
//
//            isValid = false;
//        }

        if(username.getText().toString().isEmpty())
        {
            password.requestFocus();
            username.setError("username cannot be empty !");
            username.requestFocus();

            isValid = false;
        }



        return isValid;
    }



    @Override
    public void onStart() {
        super.onStart();
        isDestroyed= false;
    }




    @Override
    public void onDestroyView() {
        super.onDestroyView();

        isDestroyed= true;
    }




    @BindView(R.id.login)
    Button loginButton;





    @OnClick(R.id.login)
    void makeRequestLogin()
    {
        loginToGlobalEndpoint();
    }







    void loginToGlobalEndpoint()
    {
        if(!validateData())
        {
            // validation failed return
            return;
        }





        final String phoneWithCode = username.getText().toString();
//        final String phoneWithCode = ccp.getSelectedCountryCode()+ username.getText().toString();

        progressBar.setVisibility(View.VISIBLE);
        loginButton.setVisibility(View.INVISIBLE);


        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .baseUrl(PrefServiceConfig.getServiceURL_SDS(MyApplication.getAppContext()))
                .client(new OkHttpClient().newBuilder().build())
                .build();


        Call<User> call = retrofit.create(UserServiceGlobal.class).getProfile(
                PrefLogin.baseEncoding(phoneWithCode,password.getText().toString())
        );





        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {

                if(isDestroyed)
                {
                    return;
                }

                progressBar.setVisibility(View.GONE);
                loginButton.setVisibility(View.VISIBLE);

                if(response.code()==200)
                {
                    // save username and password



//                    if(response.body().getRole()!=User.ROLE_END_USER_CODE)
//                    {
//                        showToastMessage("Only an End-User is allowed to login");
//                        return;
//                    }



                    PrefLoginGlobal.saveCredentials(
                            getActivity(),
                            phoneWithCode,
                            password.getText().toString()

                    );





                    // save token and token expiry timestamp
//                    PrefLogin.saveToken(
//                            getActivity(),
//                            response.body().getToken(),
//                            response.body().getTimestampTokenExpires()
//                    );


                    // save user profile information
                    PrefLoginGlobal.saveUserProfile(
                            response.body(),
                            getActivity()
                    );









//                    PrefOneSignal.saveToken(getActivity(),PrefOneSignal.getLastToken(getActivity()));
//
//                    if(PrefOneSignal.getToken(getActivity())!=null)
//                    {
//                        // update one signal id if its not updated
//                        getActivity().startService(new Intent(getActivity(), UpdateOneSignalID.class));
//                    }










                    if(getActivity() instanceof NotifyAboutLogin)
                    {
//                        showToastMessage("Notify about login !");
                        ((NotifyAboutLogin) getActivity()).loginSuccess();
                    }



//                        getActivity().finish();


//                    showToastMessage("LoginUsingOTP success : code : " + String.valueOf(response.code()));



                }
                else
                {
                    showToastMessage("Login Failed : Username or password is incorrect !");
                    System.out.println("Login Failed : Code " + String.valueOf(response.code()));
                }

            }




            @Override
            public void onFailure(Call<User> call, Throwable t) {

                if(isDestroyed)
                {
                    return;
                }
                showToastMessage("Network connection problem !");
                progressBar.setVisibility(View.GONE);
                loginButton.setVisibility(View.VISIBLE);
            }
        });



    }







}
