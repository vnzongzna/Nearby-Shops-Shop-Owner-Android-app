package org.nearbyshops.shopkeeperappnew.ItemsByCategory;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import org.nearbyshops.shopkeeperappnew.API.ItemCategoryService;
import org.nearbyshops.shopkeeperappnew.API.ItemService;
import org.nearbyshops.shopkeeperappnew.API.ShopItemService;
import org.nearbyshops.shopkeeperappnew.DaggerComponentBuilder;
import org.nearbyshops.shopkeeperappnew.Interfaces.NotifySearch;
import org.nearbyshops.shopkeeperappnew.Interfaces.NotifySort;
import org.nearbyshops.shopkeeperappnew.Interfaces.ToggleFab;
import org.nearbyshops.shopkeeperappnew.ItemsByCategory.Interfaces.NotifyBackPressed;
import org.nearbyshops.shopkeeperappnew.ItemsByCategory.Interfaces.NotifyFABClick;
import org.nearbyshops.shopkeeperappnew.ItemsByCategory.Interfaces.NotifyIndicatorChanged;
import org.nearbyshops.shopkeeperappnew.ItemsByCategory.Model.ItemCategoriesList;
import org.nearbyshops.shopkeeperappnew.ItemsByCategory.Utility.PrefSortItems;
import org.nearbyshops.shopkeeperappnew.ItemsByCategory.ViewHolders.ViewHolderItemCategory;
import org.nearbyshops.shopkeeperappnew.ItemsByCategory.ViewHolders.ViewHolderItemSimple;
import org.nearbyshops.shopkeeperappnew.Model.Item;
import org.nearbyshops.shopkeeperappnew.Model.ItemCategory;
import org.nearbyshops.shopkeeperappnew.Model.ShopItem;
import org.nearbyshops.shopkeeperappnew.ModelEndpoints.ItemCategoryEndPoint;
import org.nearbyshops.shopkeeperappnew.ModelEndpoints.ItemEndPoint;
import org.nearbyshops.shopkeeperappnew.ModelEndpoints.ShopItemEndPoint;
import org.nearbyshops.shopkeeperappnew.Prefrences.PrefLogin;
import org.nearbyshops.shopkeeperappnew.Prefrences.PrefShopHome;
import org.nearbyshops.shopkeeperappnew.R;
import org.nearbyshops.shopkeeperappnew.ViewHolderCommon.Models.EmptyScreenData;
import org.nearbyshops.shopkeeperappnew.ViewHolderCommon.Models.HeaderTitle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by sumeet on 2/12/16.
 */

public class ItemsByCatFragmentNew extends Fragment implements SwipeRefreshLayout.OnRefreshListener,
        ViewHolderItemCategory.ListItemClick, ViewHolderItemSimple.ListItemClick,
        NotifyBackPressed, NotifySort, NotifyFABClick, NotifySearch {



    Map<Integer, ShopItem> shopItemMapTemp = new HashMap<>();

    private boolean isDestroyed = false;
    private boolean show = true;

    private int item_count_item_category = 0;

    private int limit_item = 10;
    private int offset_item = 0;
    private int item_count_item;
    private int fetched_items_count = 0;




    @BindView(R.id.swipe_container)
    SwipeRefreshLayout swipeContainer;
    @BindView(R.id.recycler_view)
    RecyclerView itemCategoriesList;

    private ArrayList<Object> dataset = new ArrayList<>();
//    ArrayList<ItemCategory> datasetCategory = new ArrayList<>();
//    ArrayList<Item> datasetItems = new ArrayList<>();


    private GridLayoutManager layoutManager;
    private AdapterSimple listAdapter;



    @Inject
    ItemCategoryService itemCategoryService;


    @Inject
    ShopItemService shopItemService;

    @Inject
    ItemService itemService;


    private ItemCategory currentCategory = null;


    public ItemsByCatFragmentNew() {

        super();

        DaggerComponentBuilder.getInstance()
                .getNetComponent().Inject(this);

        currentCategory = new ItemCategory();
        currentCategory.setItemCategoryID(1);
        currentCategory.setCategoryName("");
        currentCategory.setParentCategoryID(-1);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        setRetainInstance(true);
        View rootView = inflater.inflate(R.layout.fragment_item_categories_simple, container, false);

        ButterKnife.bind(this,rootView);


        setupRecyclerView();
        setupSwipeContainer();


        if(savedInstanceState ==null)
        {
            makeRefreshNetworkCall();
        }
        else
        {
            // add this at every rotation
            listAdapter.shopItemMap.putAll(shopItemMapTemp);
        }


        notifyItemHeaderChanged();

        return rootView;
    }



    private void setupSwipeContainer()
    {

        if(swipeContainer!=null) {

            swipeContainer.setOnRefreshListener(this);
            swipeContainer.setColorSchemeResources(
                    android.R.color.holo_blue_bright,
                    android.R.color.holo_green_light,
                    android.R.color.holo_orange_light,
                    android.R.color.holo_red_light);
        }

    }




    private void setupRecyclerView()
    {


        listAdapter = new AdapterSimple(dataset,getActivity(),this);
        itemCategoriesList.setAdapter(listAdapter);

        layoutManager = new GridLayoutManager(getActivity(),6, RecyclerView.VERTICAL,false);
        itemCategoriesList.setLayoutManager(layoutManager);



        // Code for Staggered Grid Layout
        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {


            @Override
            public int getSpanSize(int position) {


                if(position == dataset.size())
                {

                }
                else if(dataset.get(position) instanceof ItemCategory)
                {
                       final DisplayMetrics metrics = new DisplayMetrics();
                    getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);

                    int spanCount = (int) (metrics.widthPixels/(180 * metrics.density));

                    if(spanCount==0){
                        spanCount = 1;
                    }

//                    (6/spanCount)
                    return 3;

                }
                else if(dataset.get(position) instanceof Item)
                {

                    return 6;
                }
                else if(dataset.get(position) instanceof HeaderTitle)
                {
                    return 6;
                }

                return 6;
            }
        });


        final DisplayMetrics metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);

//        layoutManager.setSpanCount(metrics.widthPixels/350);


//        int spanCount = (int) (metrics.widthPixels/(150 * metrics.density));
//
//        if(spanCount==0){
//            spanCount = 1;
//        }

//        layoutManager.setSpanCount(spanCount);


        /*final DisplayMetrics metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);

        int spanCount = (int) (metrics.widthPixels/(180 * metrics.density));

        if(spanCount==0){
            spanCount = 1;
        }

        return (3/spanCount);*/


        itemCategoriesList.addOnScrollListener(new RecyclerView.OnScrollListener() {


            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);


                if(offset_item + limit_item > layoutManager.findLastVisibleItemPosition())
                {
                    return;
                }


                if(layoutManager.findLastVisibleItemPosition()==dataset.size())
                {

                    // trigger fetch next page

                    if((offset_item+limit_item)<=item_count_item)
                    {
                        offset_item = offset_item + limit_item;

                        makeRequestItem(false,false);
                    }

                }
            }


            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

            }


        });

    }


//    @State int previous_position = -1;



    @Override
    public void onRefresh() {


        makeRequestItem(true,true);
        makeNetworkCallShopItem();
    }






    private void makeRefreshNetworkCall()
    {
        swipeContainer.post(new Runnable() {
            @Override
            public void run() {

                swipeContainer.setRefreshing(true);
                onRefresh();
            }
        });

    }





    @Override
    public void onDestroyView() {
        super.onDestroyView();
        isDestroyed = true;
    }

    @Override
    public void onResume() {
        super.onResume();
        isDestroyed=false;
    }







    private void showToastMessage(String message)
    {
        if(getActivity()!=null)
        {
            Toast.makeText(getActivity(),message, Toast.LENGTH_SHORT).show();
        }
    }










    private String searchQuery = null;

    @Override
    public void search(final String searchString) {
        searchQuery = searchString;
        makeRefreshNetworkCall();
    }

    @Override
    public void endSearchMode() {
        searchQuery = null;
        makeRefreshNetworkCall();
    }









    private void makeRequestItem(final boolean clearDataset, boolean resetOffset)
    {

        if(resetOffset)
        {
            offset_item = 0;
        }


        String current_sort = "";

        current_sort = PrefSortItems.getSort(getContext()) + " " + PrefSortItems.getAscending(getContext());
/*
        Call<ItemEndPoint> endPointCall = itemService.getItemsEndpoint(currentCategory.getItemCategoryID(),
                null,
                null,
                null,
                null,null, null, null,
                current_sort, limit_item,offset_item,null);*/

        Call<ItemEndPoint> endPointCall = null;



        if(searchQuery==null)
        {
            endPointCall = itemService.getItemsOuterJoin(
                    currentCategory.getItemCategoryID(),
                    clearDataset,null,
                    current_sort,
                    limit_item,offset_item, null);
        }
        else
        {
            endPointCall = itemService.getItemsOuterJoin(
                    null,
                    clearDataset, searchQuery,
                    current_sort,
                    limit_item,offset_item, null);
        }







        endPointCall.enqueue(new Callback<ItemEndPoint>() {
            @Override
            public void onResponse(Call<ItemEndPoint> call, Response<ItemEndPoint> response) {


                if(isDestroyed)
                {
                    return;
                }



                if(response.code() ==200)
                {


                    if(clearDataset)
                    {
                        dataset.clear();
                        item_count_item = response.body().getItemCount();



                        if(response.body().getSubcategories()!=null && response.body().getSubcategories().size()>0)
                        {


                            if (searchQuery == null) {

                                HeaderTitle headerItemCategory = new HeaderTitle();

                                if (currentCategory.getParentCategoryID() == -1) {
                                    headerItemCategory.setHeading("Item Categories");
                                } else {
                                    headerItemCategory.setHeading(currentCategory.getCategoryName() + " Subcategories");
                                }

                                dataset.add(headerItemCategory);
                            }




                            if(currentCategory.getParentCategoryID()==-1 || response.body().getResults().size()==0)
                            {
                                dataset.addAll(response.body().getSubcategories());
                            }
                            else
                            {

                                ItemCategoriesList list = new ItemCategoriesList();
                                list.setItemCategories(response.body().getSubcategories());

                                dataset.add(list);

                            }


                        }






                        HeaderTitle headerItem = new HeaderTitle();



                        if(searchQuery==null)
                        {
                            if(response.body().getResults().size()>0)
                            {
                                headerItem.setHeading(currentCategory.getCategoryName() + " Items");
                            }
                            else
                            {
                                headerItem.setHeading("No Items in this category");
                            }


                        }
                        else
                        {
                            if(response.body().getResults().size()>0)
                            {
                                headerItem.setHeading("Search Results");
                            }
                            else
                            {
                                headerItem.setHeading("No items for the given search !");
                            }
                        }



                        dataset.add(headerItem);

                    }






                    dataset.addAll(response.body().getResults());





                    if(offset_item+limit_item >= item_count_item)
                    {
                        listAdapter.setLoadMore(false);
                    }
                    else
                    {
                        listAdapter.setLoadMore(true);
                    }



                    notifyItemHeaderChanged();

                }
                else
                {
                    showToastMessage("Failed Code : " + String.valueOf(response.code()));
                }







            }

            @Override
            public void onFailure(Call<ItemEndPoint> call, Throwable t) {

                if(isDestroyed)
                {
                    return;
                }




                if(dataset.size()<=1)
                {
                    dataset.clear();
                }

                dataset.add(EmptyScreenData.getOffline());
                listAdapter.notifyDataSetChanged();




//                showToastMessage("Items: Network request failed. Please check your connection !");

            }
        });

    }




    @Override
    public void notifyRequestSubCategory(ItemCategory itemCategory) {

        ItemCategory temp = currentCategory;
        currentCategory = itemCategory;
        currentCategory.setParentCategory(temp);

        makeRefreshNetworkCall();

        // End Search Mode
        searchQuery = null;

        // reset previous flag
//        resetPreviousPosition();

    }

    @Override
    public void notifyItemSelected() {
        if(getActivity() instanceof ToggleFab)
        {
            ((ToggleFab)getActivity()).showFab();
            show=true;
        }
    }





    @Override
    public boolean backPressed() {

        // reset previous flag
//        resetPreviousPosition();

        int currentCategoryID = 1; // the ID of root category is always supposed to be 1

        // clear selected items
        listAdapter.selectedItems.clear();

        if(currentCategory!=null) {


            if (currentCategory.getParentCategory() != null) {

                currentCategory = currentCategory.getParentCategory();
                currentCategoryID = currentCategory.getItemCategoryID();

            } else {
                currentCategoryID = currentCategory.getParentCategoryID();
            }


            if (currentCategoryID != -1) {
                makeRefreshNetworkCall();
            }
        }

        return currentCategoryID == -1;
    }





    private void notifyItemHeaderChanged()
    {
        if(getActivity() instanceof NotifyIndicatorChanged)
        {
            ((NotifyIndicatorChanged) getActivity()).notifyItemIndicatorChanged(String.valueOf(fetched_items_count) + " out of " + String.valueOf(item_count_item) + " " + currentCategory.getCategoryName() + " Items");
        }
    }


    @Override
    public void notifySortChanged() {

        System.out.println("Notify Sort Clicked !");
        makeRefreshNetworkCall();
    }






    // display shop Item Status




    private void makeNetworkCallShopItem()
    {

        int currentShopID = PrefShopHome.getShop(getContext()).getShopID();

//        Toast.makeText(getActivity(),"Shop ID : "  + String.valueOf(currentShopID),Toast.LENGTH_SHORT).show();

        if(currentCategory==null)
        {

            swipeContainer.setRefreshing(false);

            return;
        }

        Call<ShopItemEndPoint> call;


        if(searchQuery!=null)
        {
            /*call = shopItemService.getShopItemEndpoint(
                    null,
                    currentShopID,
                    null,null,null,null,null,null,null,null,null,null,null,null,
                    searchQuery,
                    null,null,null,
                    false,false
            );*/

            call = shopItemService.getShopItemsForShop(
                    null,
                    currentShopID,null,
                    searchQuery,
                    null,null,0
            );

        }
        else
        {

            /*call = shopItemService.getShopItemEndpoint(
                    currentCategory.getItemCategoryID(),
                    currentShopID,
                    null,null,null,null,null,null,null,null,null,null,null,null,
                    null,
                    null,null,null,
                    false,false
            );*/


            call = shopItemService.getShopItemsForShop(
                    currentCategory.getItemCategoryID(),
                    currentShopID,null,
                    null,
                    null,null,0
            );
        }



        call.enqueue(new Callback<ShopItemEndPoint>() {
            @Override
            public void onResponse(Call<ShopItemEndPoint> call, Response<ShopItemEndPoint> response) {

                if(isDestroyed)
                {
                    return;
                }


                if(response.code()==200 && response.body()!=null)
                {
                    listAdapter.shopItemMap.clear();

                    for(ShopItem shopItem: response.body().getResults())
                    {
                        listAdapter.shopItemMap.put(shopItem.getItemID(),shopItem);
                    }


                    // add this map into the temporary variable to save shopItems after rotation
                    shopItemMapTemp.putAll(listAdapter.shopItemMap);
                    listAdapter.notifyDataSetChanged();

                }
                else
                {
                    showToastMessage("Failed : " + String.valueOf(response.code()));
                }

            }




            @Override
            public void onFailure(Call<ShopItemEndPoint> call, Throwable t) {

                if(isDestroyed)
                {
                    return;
                }

//                showToastMessage("Network request failed. Please check your network !");
            }
        });
    }






    private void addSelectedToShopClick()
    {

        if(listAdapter.selectedItems.size()==0)
        {
            showToastMessage("No item selected. Please make a selection !");

            return;
        }

        List<ShopItem> tempShopItemList = new ArrayList<>();

        for(Map.Entry<Integer, Item> entry : listAdapter.selectedItems.entrySet())
        {
//            entry.getValue().setItemCategoryID(parentCategory.getItemCategoryID());


            ShopItem shopItem = new ShopItem();
            shopItem.setShopID(PrefShopHome.getShop(getContext()).getShopID());
            shopItem.setItemID(entry.getValue().getItemID());

            tempShopItemList.add(shopItem);
        }

        makeShopItemCreateBulkRequest(tempShopItemList);

    }





    private void makeShopItemCreateBulkRequest(List<ShopItem> tempShopItemList) {


        Call<ResponseBody> call = shopItemService.createShopItemBulk(
                PrefLogin.getAuthorizationHeaders(getActivity()),
                tempShopItemList
        );


        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                if(isDestroyed)
                {
                    return;
                }


                if(response.code() == 200)
                {
                    showToastMessage("Update Successful !");

                    clearSelectedItems();

                }else if (response.code() == 206)
                {
                    showToastMessage("Partially Updated. Check data changes !");

                    clearSelectedItems();

                }else if(response.code() == 304)
                {

                    showToastMessage("No item updated !");

                }else
                {
                    showToastMessage("Unknown server error or response !");
                }

                makeNetworkCallShopItem();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

                if(isDestroyed)
                {
                    return;
                }


//                showToastMessage("Network Request failed. Check your internet / network connection !");

            }
        });

    }




    private void removeSeletedShopItemClick(){



        if(listAdapter.selectedItems.size()==0)
        {
            showToastMessage("No item selected. Please make a selection !");

            return;
        }

        List<ShopItem> tempShopItemList = new ArrayList<>();

        for(Map.Entry<Integer, Item> entry : listAdapter.selectedItems.entrySet())
        {
//            entry.getValue().setItemCategoryID(parentCategory.getItemCategoryID());


            ShopItem shopItem = new ShopItem();
            shopItem.setShopID(PrefShopHome.getShop(getContext()).getShopID());
            shopItem.setItemID(entry.getValue().getItemID());

            tempShopItemList.add(shopItem);
        }

        makeShopItemDeleteBulkRequest(tempShopItemList);
    }




    private void makeShopItemDeleteBulkRequest(List<ShopItem> tempShopItemList) {

        Call<ResponseBody> call = shopItemService.deleteShopItemBulk(
                PrefLogin.getAuthorizationHeaders(getActivity()),
                tempShopItemList
        );


        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                if(isDestroyed)
                {
                    return;
                }

                if(response.code() == 200)
                {
                    showToastMessage("Update Successful !");
                    clearSelectedItems();

                }else if (response.code() == 206)
                {
                    showToastMessage("Partially Updated. Check data changes !");

                    clearSelectedItems();

                }else if(response.code() == 304)
                {

                    showToastMessage("No item updated !");

                }else
                {
                    showToastMessage("Unknown server error or response !");
                }

                makeNetworkCallShopItem();

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

                if(isDestroyed)
                {
                    return;
                }


                showToastMessage("Network Request failed. Check your internet / network connection !");

            }
        });

    }



    private void clearSelectedItems()
    {
        // clear the selected items
        listAdapter.selectedItems.clear();
    }


    @Override
    public void addItem() {

//        Intent intent = new Intent(getActivity(),EditItem.class);
//        intent.putExtra(EditItemFragmentNew.EDIT_MODE_INTENT_KEY, EditItemFragmentNew.MODE_ADD);
//        intent.putExtra(EditItemFragmentNew.ITEM_CATEGORY_INTENT_KEY,currentCategory);
//        startActivity(intent);
    }

    @Override
    public void editItem(Item item) {

//        Intent intentEdit = new Intent(getActivity(), EditItem.class);
//        intentEdit.putExtra(EditItemFragmentNew.EDIT_MODE_INTENT_KEY, EditItemFragmentNew.MODE_ADD);
//        intentEdit.putExtra(EditItemFragmentNew.IS_UPDATE_INTENT_KEY,true);
//
//
//        intentEdit.putExtra(EditItemFragmentNew.ITEM_CATEGORY_INTENT_KEY,currentCategory);
//
//        Gson gson = new Gson();
//        String json = gson.toJson(item);
//        intentEdit.putExtra(EditItemFragmentNew.ITEM_INTENT_KEY,json);
//
//        getActivity().startActivity(intentEdit);
    }


    @Override
    public void removeSelectedFromShop() {
//        showToastMessage("Remove Selected");

        removeSeletedShopItemClick();
    }

    @Override
    public void addSelectedToShop() {

//        showToastMessage("Add Selected !");
        addSelectedToShopClick();
    }







}
