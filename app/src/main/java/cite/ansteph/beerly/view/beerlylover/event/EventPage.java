package cite.ansteph.beerly.view.beerlylover.event;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.maps.android.SphericalUtil;
import com.lsjwzh.widget.recyclerviewpager.RecyclerViewPager;
import com.yarolegovich.slidingrootnav.SlidingRootNav;
import com.yarolegovich.slidingrootnav.SlidingRootNavBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

import cite.ansteph.beerly.R;
import cite.ansteph.beerly.adapter.EventAdapter;
import cite.ansteph.beerly.api.Routes;
import cite.ansteph.beerly.api.columns.EventColumns;
import cite.ansteph.beerly.helper.SessionManager;
import cite.ansteph.beerly.model.BeerlyEvent;
import cite.ansteph.beerly.model.Establishment;
import cite.ansteph.beerly.slidingmenu.DrawerAdapter;
import cite.ansteph.beerly.slidingmenu.DrawerItem;
import cite.ansteph.beerly.slidingmenu.MenuPosition;
import cite.ansteph.beerly.slidingmenu.SimpleItem;
import cite.ansteph.beerly.slidingmenu.SpaceItem;
import cite.ansteph.beerly.utils.InternetConnectionStatus;
import cite.ansteph.beerly.view.beerlylover.affiliate.Affiliate;
import cite.ansteph.beerly.view.beerlylover.Home;
import cite.ansteph.beerly.view.beerlylover.LoverProfile;
import cite.ansteph.beerly.view.beerlylover.Preferences;
import cite.ansteph.beerly.view.beerlylover.discount.Discount;
import cite.ansteph.beerly.view.beerlylover.registration.Login;

public class EventPage extends AppCompatActivity  implements DrawerAdapter.OnItemSelectedListener , OnMapReadyCallback, GoogleMap.OnCameraChangeListener  {

    private  static String TAG = Home.class.getSimpleName();

    private String[] screenTitles;
    private Drawable[] screenIcons;

    private SlidingRootNav slidingRootNav;
    MapView mMapView;
    Marker[] mPubMarker ;

    protected RecyclerViewPager mRecyclerView;


    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;

    FirebaseUser mUser;

    public static final  int RC_SIGN_IN =1;
    final private int LOCATION_PERMISSION_REQUEST_CODE = 1;

    double locations[][] = new double[][]{{-33.955239, 25.611931},
            { -33.9736, 25.5983},
            {-33.914472, 25.602237},
            {-33.955239, 25.611931},
            { -33.939309, 25.559880},


    };

    SessionManager sessionManager;
    LatLng mCurrentLatLng;

    private GoogleMap mGoogleMap;
    ArrayList<BeerlyEvent> mBeerlyEvents ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_page);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mBeerlyEvents = new ArrayList<>();
        mAuth = FirebaseAuth.getInstance();

        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                mUser = firebaseAuth.getCurrentUser();
                if(mUser!=null)
                {

                }else{
                    // Log.d(TAG, "onAuthStateChanged:signed_out");
                    startActivity(new Intent(getApplicationContext(), Login.class));

                }

            }
        };





        slidingRootNav = new SlidingRootNavBuilder(this)
                .withToolbarMenuToggle(toolbar)
                .withMenuOpened(false)
                .withContentClickableWhenMenuOpened(false)
                .withSavedState(savedInstanceState)
                .withMenuLayout(R.layout.menu_left_drawer)
                .inject();

        screenIcons = loadScreenIcons();
        screenTitles = loadScreenTitles();

        try{
            MapsInitializer.initialize(getApplicationContext());
            mMapView = (MapView) findViewById(R.id.map);
            mMapView.onCreate(savedInstanceState);
            mMapView.getMapAsync(this);
        }catch (Exception e)
        {
            e.printStackTrace();
        }



        DrawerAdapter adapter = new DrawerAdapter(Arrays.asList(
                createItemFor(MenuPosition.POS_HOME),
                createItemFor(MenuPosition.POS_EVENT).setChecked(true),
                createItemFor(MenuPosition.POS_MYPROFILE),
                createItemFor(MenuPosition.POS_DISCOUNT),
                createItemFor(MenuPosition.POS_PREFERENCE),
               // createItemFor(MenuPosition.POS_AFFILIATE),
                new SpaceItem(48),
                createItemFor(MenuPosition.POS_LOGOUT)));
        adapter.setListener(this);

        RecyclerView list = (RecyclerView) findViewById(R.id.list);

        list.setNestedScrollingEnabled(false);
        list.setLayoutManager(new LinearLayoutManager(this));
        list.setAdapter(adapter);

        adapter.setSelected(MenuPosition.POS_EVENT);



        try {
            getEventData();
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }


    private String[] loadScreenTitles() {
        return getResources().getStringArray(R.array.ld_activityScreenTitles);
    }

    private Drawable[] loadScreenIcons() {
        TypedArray ta = getResources().obtainTypedArray(R.array.ld_activityScreenIcons);
        Drawable[] icons = new Drawable[ta.length()];
        for (int i = 0; i < ta.length(); i++) {
            int id = ta.getResourceId(i, 0);
            if (id != 0) {
                icons[i] = ContextCompat.getDrawable(this, id);
            }
        }
        ta.recycle();
        return icons;
    }

    @ColorInt
    private int color(@ColorRes int res) {
        return ContextCompat.getColor(this, res);
    }

    private DrawerItem createItemFor(int position) {
        return new SimpleItem(screenIcons[position], screenTitles[position])
                .withIconTint(color(R.color.textColorSecondary))
                .withTextTint(color(R.color.textColorPrimary))
                .withSelectedIconTint(color(R.color.colorAccent))
                .withSelectedTextTint(color(R.color.colorAccent));
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void signOut() {
        mAuth.signOut();
        //updateUI(null);
    }


    @Override
    public void onItemSelected(int position) {
        if (position == MenuPosition.POS_LOGOUT) {
            signOut();
            //  finish();
        }
        slidingRootNav.closeMenu();

        Intent intent = null;

        switch (position)
        {
            case MenuPosition.POS_HOME:intent = new Intent(getApplicationContext(), Home.class);break;
            case MenuPosition.POS_EVENT: break;
            case MenuPosition.POS_MYPROFILE:intent = new Intent(getApplicationContext(), LoverProfile.class);break;
            case MenuPosition.POS_DISCOUNT:intent = new Intent(getApplicationContext(), Affiliate.class);break;
            case MenuPosition.POS_PREFERENCE:intent = new Intent(getApplicationContext(), Preferences.class);break;
            case MenuPosition.POS_AFFILIATE:intent = new Intent(getApplicationContext(), Discount.class);break;

            default:
                intent = new Intent(getApplicationContext(), Home.class);
        }

        if(intent!=null)
        {
            startActivity(intent);
        }


    }

    void checkConnection()
    {
        LinearLayout lyt = (LinearLayout) findViewById(R.id.lytConnection);

        if( InternetConnectionStatus.isFullConnectionOn(this))
        {
            lyt.setVisibility(View.GONE);
        }else{
            lyt.setVisibility(View.VISIBLE);
        }


    }


    private void getEventData() throws JSONException
    {
        checkConnection();

        String url = String.format(Routes.URL_RETRIEVE_EVENTS);

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                loadBeerlyEvent(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

        requestQueue.add(jsonArrayRequest);
    }



    private void loadBeerlyEvent(JSONArray eventjsonArray)
    {
        ArrayList<BeerlyEvent> eventArrayList = new ArrayList<>();

        for(int i = 0; i<eventjsonArray.length(); i++)
        {
            try{
                JSONObject eventjson = eventjsonArray.getJSONObject(i);

                BeerlyEvent est  = new BeerlyEvent();
                est.setId(eventjson.getInt(EventColumns.EST_ID));
                est.setName(eventjson.getString(EventColumns.TITLE));
                est.setAddress(eventjson.getString(EventColumns.ADDRESS));
                //est.setLiqour_license(eventjson.getString(EventColumns.LIQUORLICENCE));
                est.setDescription(eventjson.getString(EventColumns.DESCRIPTION));
                est.setContact_person(eventjson.getString(EventColumns.CONTACTPERSON));
                est.setContact_number(eventjson.getString(EventColumns.CONTACTNUMBER));
                est.setEstablishment_url(eventjson.getString(EventColumns.URL));
                est.setLatitude(eventjson.getString(EventColumns.LATITUDE));
                est.setLongitude(eventjson.getString(EventColumns.LONGITUDE));

                est.setMain_picture_url(eventjson.getString(EventColumns.URLMAINPIC));
                est.setPicture_2_url(eventjson.getString(EventColumns.PIC2));

                est.setEndDate(eventjson.getString(EventColumns.ENDDATE));
                est.setStartDate(eventjson.getString(EventColumns.STARTDATE));
                // est.set(estjson.getString("hs_license"));
                // est.setName(estjson.getString(""));

                eventArrayList.add(est);


            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
        }


        mBeerlyEvents = eventArrayList;

        filteringEvent(mCurrentLatLng,mBeerlyEvents);


       // initViewPager(eventArrayList);
        //initMarker(eventArrayList);

        /*
        try {
            getPromoNumberData(eventArrayList);
        } catch (JSONException e) {
            e.printStackTrace();
        }*/
    }


    void filteringEvent(LatLng currentCenter, ArrayList<BeerlyEvent> events)
    {

        ArrayList<BeerlyEvent> filteredList = new ArrayList<>();

        for(BeerlyEvent evt : events)
        {
            try{
                LatLng toDest = new LatLng(Double.valueOf(evt.getLatitude()) ,Double.valueOf(evt.getLongitude()) ) ;
                Double distance = SphericalUtil.computeDistanceBetween(currentCenter, toDest);

                if(distance <= 10000){
                    filteredList.add(evt);
                }
            }catch (Exception e){
                e.printStackTrace();
            }




        }



        initViewPager(filteredList);
        initMarker(filteredList);

        if(filteredList.size()==0)
        {
            filteredList.add(createEmptyEvt());
            initViewPager(filteredList);
        }

    }


    BeerlyEvent createEmptyEvt()
    {
        BeerlyEvent evt  = new BeerlyEvent();
        evt.setId(0);
        evt.setName("No event found near you");
        evt.setAddress("Try moving the map around to discover more events");
        evt.setLiqour_license("0");
        evt.setContact_person("0");
        evt.setContact_number("0");
        evt.setEstablishment_url("0");
        evt.setLatitude("0");
        // est.setLongitude(estjson.getString(EstablishmentColumns.LONGITUDE));

        // est.setMain_picture_url(estjson.getString(EstablishmentColumns.URLMAINPIC));
        // est.setPicture_2_url(estjson.getString("picture_2"));



        return evt;
    }

    protected  void initViewPager(ArrayList<BeerlyEvent> eventList){
        mRecyclerView =(RecyclerViewPager) findViewById(R.id.recyclerViewEvt);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL, false);

        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(new EventAdapter(this,mRecyclerView,eventList));
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLongClickable(true);



        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                int childCount = mRecyclerView.getChildCount();
                int width =mRecyclerView.getChildAt(0).getWidth();
                int padding = (mRecyclerView.getWidth()-width)/2;

                for(int j =0; j<childCount; j++)
                {
                    View v = recyclerView.getChildAt(j);

                    float rate =0;
                    if (v.getLeft() <= padding) {
                        if (v.getLeft() >= padding - v.getWidth()) {
                            rate = (padding - v.getLeft()) * 1f / v.getWidth();
                        } else {
                            rate = 1;
                        }
                        v.setScaleY(1 - rate * 0.1f);
                        v.setScaleX(1 - rate * 0.1f);

                    } else {

                        if (v.getLeft() <= recyclerView.getWidth() - padding) {
                            rate = (recyclerView.getWidth() - padding - v.getLeft()) * 1f / v.getWidth();
                        }
                        v.setScaleY(0.9f + rate * 0.1f);
                        v.setScaleX(0.9f + rate * 0.1f);
                    }
                }
            }
        });
        mRecyclerView.addOnPageChangedListener(new RecyclerViewPager.OnPageChangedListener() {
            @Override
            public void OnPageChanged(int oldPosition, int newPosition) {
                // Log.d("test", "oldPosition:" + oldPosition + " newPosition:" + newPosition);
                //  Toast.makeText(getApplicationContext(), " Position:" + mRecyclerView.getCurrentPosition(), Toast.LENGTH_LONG).show();

                setMarkerIcon(mRecyclerView.getCurrentPosition());
            }
        });

        mRecyclerView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                if (mRecyclerView.getChildCount() < 3) {
                    if (mRecyclerView.getChildAt(1) != null) {
                        if (mRecyclerView.getCurrentPosition() == 0) {
                            View v1 = mRecyclerView.getChildAt(1);
                            v1.setScaleY(0.9f);
                            v1.setScaleX(0.9f);
                        } else {
                            View v1 = mRecyclerView.getChildAt(0);
                            v1.setScaleY(0.9f);
                            v1.setScaleX(0.9f);
                        }
                    }
                } else {
                    if (mRecyclerView.getChildAt(0) != null) {
                        View v0 = mRecyclerView.getChildAt(0);
                        v0.setScaleY(0.9f);
                        v0.setScaleX(0.9f);
                    }
                    if (mRecyclerView.getChildAt(2) != null) {
                        View v2 = mRecyclerView.getChildAt(2);
                        v2.setScaleY(0.9f);
                        v2.setScaleX(0.9f);
                    }
                }

            }
        });
    }


    private void getPromoNumberData(final ArrayList<BeerlyEvent> events) throws JSONException
    {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

        for(int i =0; i<events.size(); i++){

            String url = String.format(Routes.URL_RETRIEVE_PROMO_EST, events.get(i).getId());
            final int finI = i;

            JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {
                    //loadBeerlyEvent(response);

                    events.get(finI).setPromoNumber(response.length());

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            });


            requestQueue.add(jsonArrayRequest);
        }


        //initViewPager(establishments);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
        mGoogleMap.getUiSettings().setZoomControlsEnabled(false);
        mGoogleMap.getUiSettings().setZoomGesturesEnabled(true);
        mGoogleMap.setOnCameraChangeListener(this);
        try{
            boolean success = mGoogleMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(getApplicationContext(), R.raw.map_event_style)
            );
            if(!success){
                Log.e(TAG,"Style parsing failed");
            }
        }catch (Resources.NotFoundException e){
            Log.e(TAG,  "Can't find style. Error: ", e);
        }

        LatLng pe = new LatLng(-33.9736, 25.5983);
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(pe,10.9f));
        enableMyLocation();

    }


    private void initMarker(ArrayList<BeerlyEvent> events)
    {
        mGoogleMap.clear();
        mPubMarker = new Marker[events.size()];

        for(int i = 0; i<events.size();i++)
        {
            BeerlyEvent est = events.get(i);

            if(i==0){
                mPubMarker[i] = mGoogleMap.addMarker(new MarkerOptions().position(new LatLng(Double.parseDouble(est.getLatitude()),Double.parseDouble(est.getLongitude())))
                        .title(est.getName())
                        .snippet("Go have fun").icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_map_pin_select)));
            }else{

            mPubMarker[i] = mGoogleMap.addMarker(new MarkerOptions().position(new LatLng(Double.parseDouble(est.getLatitude()),Double.parseDouble(est.getLongitude())))
                    .title(est.getName())
                    .snippet("Go have fun").icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_map_pin_reg)));}
        }
    }


    private void setMarkerIcon(int pos)
    {
        for(int i =0; i< mPubMarker.length; i++)
        {
            if(i==pos)
                mPubMarker[i].setIcon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_map_pin_select));
            else
                mPubMarker[i].setIcon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_map_pin_reg));

        }

    }

    /**
     * Enables the My Location layer if the fine location permission has been granted.
     */
    private void enableMyLocation()
    {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            requestPermission();
        } else if (mGoogleMap != null) {
            // Access to the location has been granted to the app.
            mGoogleMap.setMyLocationEnabled(true);
            LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            Criteria criteria = new Criteria();

            String bestProvider = String.valueOf(manager.getBestProvider(criteria,true));

            Location myLocation = manager.getLastKnownLocation(bestProvider);

            if(myLocation !=null)
            {
                Log.e("EstHomeMapTag","GPS is ON");
                final double curLatitude = myLocation.getLatitude();
                final double curLongitude = myLocation.getLongitude();

                LatLng latLng = new LatLng(curLatitude, curLongitude);

                mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,15.9f));
                mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(10.9f),2000,null);
            }


        }
    }

    private void requestPermission()
    {
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},LOCATION_PERMISSION_REQUEST_CODE);
        }
    }


    /**
     * Listener for response to user permission request
     *
     * @param requestCode  Check that permission request code matches
     * @param permissions  Permissions that requested
     * @param grantResults Whether permissions granted
     */

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode)
        {
            case LOCATION_PERMISSION_REQUEST_CODE:
                if(grantResults[0]== PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT)
                            .show();
                }else{
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT)
                            .show();
                }break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }



       /* if(grantResults[0]== PackageManager.PERMISSION_GRANTED){
            Log.i(TAG, "Permission " +permissions[0]+ " was " +grantResults[0]);
        }*/
    }


    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthStateListener);

    }

    @Override
    protected void onStop() {
        super.onStop();
        if(mAuthStateListener!=null)
            mAuth.removeAuthStateListener(mAuthStateListener);

    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
        if(mAuthStateListener!=null)
            mAuth.removeAuthStateListener(mAuthStateListener);

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

        super.onSaveInstanceState(outState);
        mMapView.onSaveInstanceState(outState);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
        mAuth.addAuthStateListener(mAuthStateListener);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onCameraChange(CameraPosition cameraPosition) {
        Log.d(TAG, "Camera moved");

        mCurrentLatLng = new LatLng(cameraPosition.target.latitude,cameraPosition.target.longitude);

        Location loc  = new Location("");

        loc.setLatitude(cameraPosition.target.latitude);
        loc.setLongitude(cameraPosition.target.longitude);

        String latlong = cameraPosition.target.latitude +", "+cameraPosition.target.longitude;
        Log.d(TAG, latlong);

        filteringEvent(mCurrentLatLng,mBeerlyEvents);
        //
    }
}
