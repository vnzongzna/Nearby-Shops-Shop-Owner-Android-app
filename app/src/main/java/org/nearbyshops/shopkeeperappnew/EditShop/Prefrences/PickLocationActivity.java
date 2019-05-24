package org.nearbyshops.shopkeeperappnew.EditShop.Prefrences;//package org.nearbyshops.shopkeeperappnew.ShopAdminHome.EditShop.Prefrences;
//
//import android.Manifest;
//import android.content.Intent;
//import android.content.pm.PackageManager;
//import android.os.Bundle;
//import android.support.v4.app.ActivityCompat;
//import android.support.v4.app.Fragment;
//import android.support.v4.app.FragmentActivity;
//import android.widget.SeekBar;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.google.android.gms.maps.CameraUpdateFactory;
//import com.google.android.gms.maps.GoogleMap;
//import com.google.android.gms.maps.OnMapReadyCallback;
//import com.google.android.gms.maps.SupportMapFragment;
//import com.google.android.gms.maps.model.Circle;
//import com.google.android.gms.maps.model.CircleOptions;
//import com.google.android.gms.maps.model.LatLng;
//import com.google.android.gms.maps.model.Marker;
//import com.google.android.gms.maps.model.MarkerOptions;
//
//
//import org.nearbyshops.shopkeeperappnew.R;
//
//import butterknife.BindView;
//import butterknife.ButterKnife;
//import butterknife.OnClick;
//
//
//public class PickLocationActivity extends FragmentActivity implements OnMapReadyCallback, SeekBar.OnSeekBarChangeListener {
//
//    private GoogleMap mMap;
//
//    Marker currentMarker;
//
//    @BindView(R.id.seekbar)
//    SeekBar seekbar;
//
//    @BindView(R.id.delivery_range_header)
//    TextView deliveryRangeHeader;
//    private double deliveryRange;
//
//
//    public static final String INTENT_KEY_CURRENT_LAT = "current_lat";
//    public static final String INTENT_KEY_CURRENT_LON = "current_lon";
//    public static final String INTENT_KEY_DELIVERY_RANGE = "delivery_range";
//
//
////    float current_latitude;
////    float current_longitude;
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_pick_location);
//
//        ButterKnife.bind(this);
//
//        Fragment mapFragment = getSupportFragmentManager().findFragmentByTag("tag_map");
//
//        if(mapFragment == null)
//        {
//
//            mapFragment = new SupportMapFragment();
//
//            getSupportFragmentManager()
//                    .beginTransaction()
//                    .add(R.id.map,mapFragment,"tag_map")
//                    .commit();
//        }
//
//
//        if(mapFragment instanceof SupportMapFragment)
//        {
//            ((SupportMapFragment) mapFragment).getMapAsync(this);
//        }
//
//
//        seekbar.setOnSeekBarChangeListener(this);
//
//    }
//
//
//    @OnClick(R.id.confirm_selected_location_button)
//    void confirmButtonClick()
//    {
//
//        if(currentMarker==null)
//        {
//            Toast.makeText(this, R.string.toast_location_not_selected, Toast.LENGTH_SHORT).show();
//
//            return;
//        }
//
//        Intent data = new Intent();
//        data.putExtra("latitude",currentMarker.getPosition().latitude);
//        data.putExtra("longitude",currentMarker.getPosition().longitude);
//        data.putExtra("delivery_range_kms",(deliveryRange/ 1000));
//        setResult(RESULT_OK,data);
//        finish();
//    }
//
//    /**
//     * Manipulates the map once available.
//     * This callback is triggered when the map is ready to be used.
//     * This is where we can add markers or lines, add listeners or move the camera. In this case,
//     * we just add a marker near Sydney, Australia.
//     * If Google Play services is not installed on the device, the user will be prompted to install
//     * it inside the SupportMapFragment. This method will only be triggered once the user has
//     * installed Google Play services and returned to the app.
//     */
//    @Override
//    public void onMapReady(GoogleMap googleMap) {
//        mMap = googleMap;
//
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            // TODO: Consider calling
//            //    ActivityCompat#requestPermissions
//            // here to request the missing permissions, and then overriding
//            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//            //                                          int[] grantResults)
//            // to handle the case where the user grants the permission. See the documentation
//            // for ActivityCompat#requestPermissions for more details.
//
//
//            ActivityCompat.requestPermissions(this,
//                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},2);
//
//            return;
//        }
//
//
//
//        mMap.setMyLocationEnabled(true);
//        mMap.getUiSettings().setMapToolbarEnabled(true);
//
//        double current_lat = getIntent().getDoubleExtra(INTENT_KEY_CURRENT_LAT,-1000);
//        double current_lon = getIntent().getDoubleExtra(INTENT_KEY_CURRENT_LON,-1000);
//        double delivery_range = getIntent().getDoubleExtra(INTENT_KEY_DELIVERY_RANGE,-1);
//
//
//        if(current_lat != -1000 && current_lon != -1000)
//        {
//            LatLng latLng = new LatLng(current_lat,current_lon);
//            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,14));
//
//
//            currentMarker = mMap.addMarker(new MarkerOptions().position(latLng).snippet(latLng.toString()).title("Selected Location"));
////            mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
//            currentMarker.showInfoWindow();
//
//            // 0 and false are dummy unused variables
//
//            if(delivery_range!=-1)
//            {
//                seekbar.setProgress((int)(delivery_range*1000)/10);
//            }
//
//            onProgressChanged(seekbar,0,false);
//
//        }
//
//
//
//
//
////        Location currentLocation = UtilityLocationShops.getCurrentLocation(this);
//
////        if(currentLocation!=null)
////        {
////            LatLng latLng = new LatLng(currentLocation.getLatitude(),currentLocation.getLongitude());
////            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,14));
////        }
//
//
//
//        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
//            @Override
//            public void onMapLongClick(LatLng latLng) {
//
////                Toast.makeText(PickLocationActivity.this,"Long Click : " + latLng.toString(), Toast.LENGTH_SHORT).show();
//
//
//                if(currentMarker!=null)
//                {
//                    currentMarker.remove();
//                }
//
//
//                currentMarker = mMap.addMarker(new MarkerOptions().position(latLng).snippet(latLng.toString()).title("Selected Location"));
////                mMap.moveCamera();
//                mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
//                currentMarker.showInfoWindow();
//
//                // 0 and false are dummy unused variables
//                onProgressChanged(seekbar,0,false);
//
//
////                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(,17));
//
//            }
//        });
//
//
//        // Add a marker in Sydney and move the camera
////        LatLng sydney = new LatLng(-34, 151);
////        mMap.addMarker(new MarkerOptions().zIndex(13).position(sydney).title("Marker in Sydney"));
////        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
//    }
//
//
//    Circle circle = null;
//
//    @Override
//    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
//
//        if(currentMarker==null)
//        {
//            return;
//        }
//
//
//        if(circle!=null)
//        {
//            circle.remove();
//        }
//
//
//        /*mMap.addCircle(new CircleOptions()
//                .center(new LatLng(
//                        currentMarker.getPosition().latitude,
//                        currentMarker.getPosition().longitude
//                ))
//        );*/
//
//        LatLng latLng = new LatLng(
//                currentMarker.getPosition().latitude,
//                currentMarker.getPosition().longitude
//        );
//
//
//
//
//        circle = mMap.addCircle(
//                new CircleOptions()
//                        .center(latLng)
//                        .radius(seekBar.getProgress()*10)
//                        .fillColor(0x33000000)
//                        .strokeWidth(1)
//                        .strokeColor(R.color.gplus_color_2)
//
////               R.color.cyan900
//        );
//
//
//        deliveryRange = ((float)seekBar.getProgress() * 10 );
//        deliveryRangeHeader.setText("Delivery Range : " + String.format( "%.2f",(deliveryRange/ 1000)) + " Km : " + String.valueOf((seekBar.getProgress()*10)) + " Meters");
//        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentMarker.getPosition(),getZoomLevel(circle)));
//    }
//
//
//
//    @Override
//    public void onStartTrackingTouch(SeekBar seekBar) {
//    }
//
//    @Override
//    public void onStopTrackingTouch(SeekBar seekBar) {
//
//
//    }
//
//    public int getZoomLevel(Circle circle)
//    {
//        int zoomLevel = 11;
//        if (circle != null)
//        {
//            double radius = circle.getRadius();
//            double scale = radius / 500;
//            zoomLevel = (int) Math.floor((16 - Math.log(scale) / Math.log(2)));
//        }
//        return zoomLevel ;
//    }
//
//
//}
