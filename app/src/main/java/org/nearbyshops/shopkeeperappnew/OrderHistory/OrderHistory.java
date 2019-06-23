package org.nearbyshops.shopkeeperappnew.OrderHistory;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import butterknife.ButterKnife;
import org.nearbyshops.shopkeeperappnew.Interfaces.NotifySearch;
import org.nearbyshops.shopkeeperappnew.Interfaces.NotifySort;
import org.nearbyshops.shopkeeperappnew.R;


public class OrderHistory extends AppCompatActivity implements NotifySort{



//    @BindView(R.id.slidingLayer) SlidingLayer slidingLayer;

    public static final String TAG_SLIDING_LAYER = "sliding_layer";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history);
        ButterKnife.bind(this);

//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });




        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container,new OrderHistoryFragment(),"order_history")
                .commit();



//        setupSlidingLayer();
    }


//    void setupSlidingLayer()
//    {
//
//        ////slidingLayer.setShadowDrawable(R.drawable.sidebar_shadow);
//        //slidingLayer.setShadowSizeRes(R.dimen.shadow_size);
//
//        if(slidingLayer!=null) {
//            slidingLayer.setChangeStateOnTap(true);
//            slidingLayer.setSlidingEnabled(true);
//            slidingLayer.setPreviewOffsetDistance(15);
//            slidingLayer.setOffsetDistance(10);
//            slidingLayer.setStickTo(SlidingLayer.STICK_TO_RIGHT);
//
////            DisplayMetrics metrics = new DisplayMetrics();
////            getWindowManager().getDefaultDisplay().getMetrics(metrics);
//
//            //RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(250, ViewGroup.LayoutParams.MATCH_PARENT);
//
//            //slidingContents.setLayoutParams(layoutParams);
//
//            //slidingContents.setMinimumWidth(metrics.widthPixels-50);
//
//
//            if (getSupportFragmentManager().findFragmentByTag(TAG_SLIDING_LAYER)==null)
//            {
//                getSupportFragmentManager()
//                        .beginTransaction()
//                        .add(R.id.slidinglayerfragment,new SlidingLayerSortOrders(),TAG_SLIDING_LAYER)
//                        .commit();
//            }
//        }
//    }



//    @OnClick({R.id.icon_sort,R.id.text_sort})
//    void sortClick()
//    {
//        slidingLayer.openLayer(true);
//    }



    @Override
    public void notifySortChanged() {

        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);


        if(fragment instanceof NotifySort)
        {
            ((NotifySort)fragment).notifySortChanged();
        }
    }


    private static String makeFragmentName(int viewId, int index) {
        return "android:switcher:" + viewId + ":" + index;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }




    // Add Search Feature to the activity


//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_order_history_hd, menu);
//        return true;
//    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_order_history_hd, menu);


        // Associate searchable configuration with the SearchView
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        SearchManager searchManager = (SearchManager) getSystemService(SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

        MenuItem item = menu.findItem(R.id.action_search);

        item.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {


                Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);


                if(fragment instanceof NotifySearch)
                {
                    ((NotifySearch) fragment).endSearchMode();
                }

                return true;
            }
        });



//
//        MenuItemCompat.setOnActionExpandListener(menu.findItem(R.id.action_search), new MenuItemCompat.OnActionExpandListener() {
//            @Override
//            public boolean onMenuItemActionExpand(MenuItem item) {
//                return true;
//            }
//
//            @Override
//            public boolean onMenuItemActionCollapse(MenuItem item) {
//
////                Fragment fragment = getSupportFragmentManager().findFragmentByTag(TAG_FRAGMENT);
//
//
////                Toast.makeText(PickFromShopInventory.this, "onCollapsed Called ", Toast.LENGTH_SHORT).show();
//
//                return true;
//            }
//        });

        return true;
    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        handleIntent(intent);
    }


    private void handleIntent(Intent intent) {

        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            //use the query to search your data somehow

            Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);


            if(fragment instanceof NotifySearch)
            {
                ((NotifySearch) fragment).search(query);
            }
        }
    }


}
