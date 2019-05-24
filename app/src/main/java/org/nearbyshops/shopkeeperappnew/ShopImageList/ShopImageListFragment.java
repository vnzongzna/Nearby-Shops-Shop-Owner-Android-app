package org.nearbyshops.shopkeeperappnew.ShopImageList;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.*;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ActionMode;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import org.nearbyshops.shopkeeperappnew.API.ShopImageService;
import org.nearbyshops.shopkeeperappnew.DaggerComponentBuilder;
import org.nearbyshops.shopkeeperappnew.EditShopImage.EditShopImage;
import org.nearbyshops.shopkeeperappnew.EditShopImage.EditShopImageFragment;
import org.nearbyshops.shopkeeperappnew.EditShopImage.UtilityShopImage;
import org.nearbyshops.shopkeeperappnew.Model.ShopImage;
import org.nearbyshops.shopkeeperappnew.ModelEndpoints.ShopImageEndPoint;
import org.nearbyshops.shopkeeperappnew.ModelRoles.User;
import org.nearbyshops.shopkeeperappnew.Prefrences.PrefLogin;
import org.nearbyshops.shopkeeperappnew.R;
import org.nearbyshops.shopkeeperappnew.ViewHolderCommon.Models.HeaderTitle;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import javax.inject.Inject;
import java.util.ArrayList;

/**
 * Created by sumeet on 14/6/17.
 */

public class ShopImageListFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, Adapter.NotificationsFromAdapter{

    boolean isDestroyed = false;

    @BindView(R.id.swipe_container)
    SwipeRefreshLayout swipeContainer;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;



    @Inject
    ShopImageService service;

    GridLayoutManager layoutManager;
    Adapter listAdapter;

    ArrayList<Object> dataset = new ArrayList<>();


    // flags
    boolean clearDataset = false;

    boolean getRowCountVehicle = false;
    boolean resetOffsetVehicle = false;


    private int limit_vehicle = 10;
    int offset_vehicle = 0;
    public int item_count_vehicle = 0;


//    @BindView(R.id.drivers_count) TextView driversCount;
//    int i = 1;

    public ShopImageListFragment() {

        DaggerComponentBuilder.getInstance()
                .getNetComponent().Inject(this);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        setRetainInstance(true);
        View rootView = inflater.inflate(R.layout.fragment_shop_image_list, container, false);
        ButterKnife.bind(this, rootView);


//        Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
//        toolbar.setTitleTextColor(ContextCompat.getColor(getActivity(),R.color.white));
//        toolbar.setTitle("Trip History");
//        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);


        setupSwipeContainer();
        setupRecyclerView();

        if (savedInstanceState == null) {
            makeRefreshNetworkCall();
        }


//        driversCount.setText("Drivers COunt : " + String.valueOf(++i));

        return rootView;
    }


    void setupSwipeContainer() {

        if (swipeContainer != null) {

            swipeContainer.setOnRefreshListener(this);
            swipeContainer.setColorSchemeResources(
                    android.R.color.holo_blue_bright,
                    android.R.color.holo_green_light,
                    android.R.color.holo_orange_light,
                    android.R.color.holo_red_light);
        }

    }


    void setupRecyclerView() {

        listAdapter = new Adapter(dataset, getActivity(), this, this);
        recyclerView.setAdapter(listAdapter);

        layoutManager = new GridLayoutManager(getActivity(), 1, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

//        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));


        final DisplayMetrics metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);


        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {


            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);


                if (layoutManager.findLastVisibleItemPosition() == dataset.size()+1) {

                    if (offset_vehicle + limit_vehicle > layoutManager.findLastVisibleItemPosition()) {
                        return;
                    }


                    // trigger fetch next page

                    if ((offset_vehicle + limit_vehicle) <= item_count_vehicle+1) {

                        offset_vehicle = offset_vehicle + limit_vehicle;

                        getTripHistory();
                    }


                }
            }
        });

    }


    @Override
    public void onResume() {
        super.onResume();
        isDestroyed = false;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        isDestroyed = true;
    }


    void makeRefreshNetworkCall() {
        swipeContainer.post(new Runnable() {
            @Override
            public void run() {

                swipeContainer.setRefreshing(true);
                onRefresh();
            }
        });
    }


    @Override
    public void onRefresh() {

        clearDataset = true;
        getRowCountVehicle = true;
        resetOffsetVehicle = true;

        getTripHistory();
    }









    void getTripHistory() {

        if (resetOffsetVehicle) {
            offset_vehicle = 0;
            resetOffsetVehicle = false;
        }


        User user = PrefLogin.getUser(getActivity());

        if (user == null) {
            swipeContainer.setRefreshing(false);
            return;
        }


//        int itemID = getActivity().getIntent().getIntExtra("item_id", 0);

        int shopId = getActivity().getIntent().getIntExtra("shop_id",0);



        Call<ShopImageEndPoint> call = service.getShopImages(
                 shopId, ShopImage.IMAGE_ORDER,
                null, null,
                clearDataset,false
        );




        call.enqueue(new Callback<ShopImageEndPoint>() {
            @Override
            public void onResponse(Call<ShopImageEndPoint> call, Response<ShopImageEndPoint> response) {

                if (isDestroyed) {
                    return;
                }


                if (response.code() == 200 && response.body() != null) {


                    if (clearDataset) {

                        dataset.clear();
                        clearDataset = false;

//                        dataset.add(new FilterSubmissions());


                        item_count_vehicle = response.body().getItemCount();
                        getRowCountVehicle = false;

                        listAdapter.setItemCount(item_count_vehicle);

//                            dataset.add(new HeaderTitle("Type of Data"));

                        dataset.add(new HeaderTitle("Shop Images"));
                    }



//                    if (getRowCountVehicle) {
//
//                    }


                    if (response.body().getResults() != null) {
                        dataset.addAll(response.body().getResults());
                    }

                    listAdapter.notifyDataSetChanged();
                }


                swipeContainer.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<ShopImageEndPoint> call, Throwable t) {



                if (isDestroyed) {
                    return;
                }

                showToastMessage("Network Connection Failed !");

                swipeContainer.setRefreshing(false);
            }
        });

    }


    void showToastMessage(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }





    ActionMode mActionMode;


    private ActionMode.Callback mActionModeCallback = new ActionMode.Callback() {

        // Called when the action mode is created; startActionMode() was called
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            // Inflate a menu resource providing context menu items
            MenuInflater inflater = mode.getMenuInflater();
            inflater.inflate(R.menu.action_menu_images_single, menu);
            return true;
        }

        // Called each time the action mode is shown. Always called after onCreateActionMode, but
        // may be called multiple times if the mode is invalidated.
        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false; // Return false if nothing is done
        }

        // Called when the user selects a contextual menu item
        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            switch (item.getItemId()) {
                case R.id.action_edit:


//                    showToastMessage("Edit !");
                    listItemClick(listAdapter.selectedItemSingle, -1);

                    mode.finish(); // Action picked, so close the CAB

                    return true;

                case R.id.action_delete:

                    showToastMessage("Delete !");
                    mode.finish();

                    return true;


                default:
                    return false;
            }
        }

        // Called when the user exits the action mode
        @Override
        public void onDestroyActionMode(ActionMode mode) {
            mActionMode = null;
        }
    };


    ShopImage taxiImageDelete;
    int positionDelete;


    void deleteClickMain(final ShopImage shopImage, final int position)
    {

        taxiImageDelete = shopImage;
        positionDelete = position;


        String filename = " ";
        if(taxiImageDelete.getImageFilename()!=null)
        {
            filename = taxiImageDelete.getImageFilename();
        }



        Call<ResponseBody> call = service.deleteShopImage(
                PrefLogin.getAuthorizationHeaders(getActivity()),
                shopImage.getShopImageID()
        );




        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                if(isDestroyed)
                {
                    return;
                }


                if (response.code()==200)
                {
                    showToastMessage("Deleted !");
                    listAdapter.notifyItemRemoved(position);
                    dataset.remove(shopImage);

                }
                else
                {
                    showToastMessage("Failed code : " + String.valueOf(response.code()));
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {


                if(isDestroyed)
                {
                    return;
                }


                showToastMessage("Delete Failed !");

            }
        });


    }


    @Override
    public void deleteClick(final ShopImage shopImage, final int position) {


        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());

        dialog.setTitle("Confirm Delete Image !")
                .setMessage("Are you sure you want to delete this Image !")
                .setPositiveButton("Yes",new DialogInterface.OnClickListener(){

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        deleteClickMain(shopImage,position);

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


    @Override
    public void notifyListItemSelected() {


        if(mActionMode!=null) {
            int size = listAdapter.selectedItems.size();
            mActionMode.setTitle(String.valueOf(size));


            if (size == 0) {
                mActionMode.finish();
            }
            else if(size==1)
            {
                mActionMode.getMenu().findItem(R.id.action_edit).setVisible(true);

//                mActionMode.getMenuInflater()
//                        .inflate(R.menu.action_menu_images_single,mActionMode.getMenu());
            }
            else if(size>1)
            {
                mActionMode.getMenu().findItem(R.id.action_edit).setVisible(false);


//                mActionMode.getMenuInflater()
//                        .inflate(R.menu.action_menu_images,mActionMode.getMenu());
            }

        }
        else
        {
            mActionMode = ((AppCompatActivity)getActivity()).startSupportActionMode(mActionModeCallback);
            mActionMode.setTitle(String.valueOf(listAdapter.selectedItems.size()));
        }
    }









    @Override
    public void listItemClick(ShopImage itemImage, int position) {


//        int itemID = getActivity().getIntent().getIntExtra("item_id",0);
        int shopId = getActivity().getIntent().getIntExtra("shop_id",0);

        Intent intent = new Intent(getActivity(), EditShopImage.class);
        intent.putExtra(EditShopImageFragment.EDIT_MODE_INTENT_KEY, EditShopImageFragment.MODE_UPDATE);
        intent.putExtra(EditShopImageFragment.SHOP_ID_INTENT_KEY,shopId);


        UtilityShopImage.saveItemImage(itemImage,getActivity());
        startActivity(intent);
    }






    @Override
    public boolean listItemLongClick(View view, ShopImage taxiImage, int position) {
        return true;
    }


}
