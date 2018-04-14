package cite.ansteph.beerly.view.beerlylover;

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
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;


import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
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
import cite.ansteph.beerly.adapter.EstAdapter;
import cite.ansteph.beerly.api.Routes;
import cite.ansteph.beerly.api.columns.BeerLoversColumns;
import cite.ansteph.beerly.api.columns.EstablishmentColumns;
import cite.ansteph.beerly.helper.SessionManager;
import cite.ansteph.beerly.model.BeerLovers;
import cite.ansteph.beerly.model.Establishment;
import cite.ansteph.beerly.slidingmenu.DrawerAdapter;
import cite.ansteph.beerly.slidingmenu.DrawerItem;
import cite.ansteph.beerly.slidingmenu.MenuPosition;
import cite.ansteph.beerly.slidingmenu.SimpleItem;
import cite.ansteph.beerly.slidingmenu.SpaceItem;
import cite.ansteph.beerly.utils.InternetConnectionStatus;
import cite.ansteph.beerly.view.beerlylover.affiliate.Affiliate;
import cite.ansteph.beerly.view.beerlylover.discount.Discount;
import cite.ansteph.beerly.view.beerlylover.event.EventPage;
import cite.ansteph.beerly.view.beerlylover.registration.Login;

public class Home extends AppCompatActivity implements DrawerAdapter.OnItemSelectedListener ,
        OnMapReadyCallback, GoogleMap.OnCameraChangeListener , GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener{

    private  static String TAG = Home.class.getSimpleName();

    protected GoogleApiClient mGoogleApiClient;
    private static final LatLngBounds SAbounds = new LatLngBounds(new LatLng(-31.029191, 18.070882), new LatLng(-28.293093, 32.446854));


    private String[] screenTitles;
    private Drawable[] screenIcons;

    private SlidingRootNav slidingRootNav;
    MapView mMapView;
    Marker [] mPubMarker ;

    protected RecyclerViewPager mRecyclerView;

    ArrayList<Establishment> mFullListEstablishment;

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

    LatLng mCurrentLatLng;

    SessionManager sessionManager;


    private GoogleMap mGoogleMap;
    ArrayList<Establishment> mEstablishments ;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        buildGoogleApiClient();
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mFullListEstablishment= new ArrayList<>();
        sessionManager = new SessionManager(getApplicationContext());

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



        mEstablishments = new ArrayList<>();



        DrawerAdapter adapter = new DrawerAdapter(Arrays.asList(
                createItemFor(MenuPosition.POS_HOME).setChecked(true),
                createItemFor(MenuPosition.POS_EVENT),
                createItemFor(MenuPosition.POS_MYPROFILE),
                createItemFor(MenuPosition.POS_DISCOUNT),
                createItemFor(MenuPosition.POS_PREFERENCE),
                createItemFor(MenuPosition.POS_AFFILIATE),
                new SpaceItem(48),
                createItemFor(MenuPosition.POS_LOGOUT)));
        adapter.setListener(this);


        RecyclerView list = (RecyclerView) findViewById(R.id.list);

       // RecyclerView list = findViewById(R.id.list);
        list.setNestedScrollingEnabled(false);
        list.setLayoutManager(new LinearLayoutManager(this));
        list.setAdapter(adapter);

        adapter.setSelected(MenuPosition.POS_HOME);

       // initViewPager(setupList());


        mAuth = FirebaseAuth.getInstance();

        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                // FirebaseUser user = firebaseAuth.getCurrentUser();
                mUser = firebaseAuth.getCurrentUser();

                if(mUser!=null)
                {
                    //User is signed in
                    // Log.d(TAG, "onAuthStateChanged:signed_in" + user.getUid());
                }else{
                    // Log.d(TAG, "onAuthStateChanged:signed_out");

                    startActivity(new Intent(getApplicationContext(), Login.class));
                   /* startActivityForResult(
                            AuthUI.getInstance()
                                    .createSignInIntentBuilder()
                                    .setIsSmartLockEnabled(true)
                                    .setProviders(AuthUI.EMAIL_PROVIDER,
                                            AuthUI.GOOGLE_PROVIDER  )
                                    .build(),
                            RC_SIGN_IN);*/
                }

            }
        };


        try {
            getEstablismentData();
        } catch (JSONException e) {
            e.printStackTrace();
        }







        // If a notification message is tapped, any data accompanying the notification
        // message is available in the intent extras. In this project the launcher
        // intent is fired when the notification is tapped, so any accompanying data would
        // be handled here. If you want a different intent fired, set the click_action
        // field of the notification message to the desired intent. The launcher intent
        // is used when no click_action is specified.
        //
        // Handle possible data accompanying notification message.
        if (getIntent().getExtras() != null) {

            for (String key : getIntent().getExtras().keySet()) {
                String value = getIntent().getExtras().getString(key);

                if (key.equals("AnotherActivity") && value.equals("True")) {
                    Intent intent = new Intent(this, Discount.class);
                    intent.putExtra("value", value);
                    startActivity(intent);
                    finish();
                }

            }
        }

        subscribeToPushService();




    }


    private void getEstablismentData() throws JSONException
    {

        checkConnection();

        String url = String.format(Routes.URL_RETRIEVE_EST);

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                loadEstablishment(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

        requestQueue.add(jsonArrayRequest);
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




    private void loadEstablishment(JSONArray estjsonArray)
    {
        ArrayList<Establishment> establishments = new ArrayList<>();

        for(int i = 0; i<estjsonArray.length(); i++)
        {
          try{
              JSONObject estjson = estjsonArray.getJSONObject(i);

              Establishment est  = new Establishment();
              est.setId(estjson.getInt(EstablishmentColumns.EST_ID));
              est.setName(estjson.getString(EstablishmentColumns.NAME));
              est.setAddress(estjson.getString(EstablishmentColumns.ADDRESS));
              est.setLiqour_license(estjson.getString(EstablishmentColumns.LIQUORLICENCE));
              est.setContact_person(estjson.getString(EstablishmentColumns.CONTACTPERSON));
              est.setContact_number(estjson.getString(EstablishmentColumns.CONTACTNUMBER));
              est.setEstablishment_url(estjson.getString(EstablishmentColumns.URL));
              est.setLatitude(estjson.getString(EstablishmentColumns.LATITUDE));
              est.setLongitude(estjson.getString(EstablishmentColumns.LONGITUDE));

              est.setMain_picture_url(estjson.getString(EstablishmentColumns.URLMAINPIC));
              est.setPicture_2_url(estjson.getString("picture_2"));
             // est.set(estjson.getString("hs_license"));
             // est.setName(estjson.getString(""));

            establishments.add(est);


          }
          catch (JSONException e)
          {
             e.printStackTrace();
          }
        }

       // initViewPager(establishments);
        //initMarker(establishments);
        mEstablishments = establishments;

        filteringEstablishment(mCurrentLatLng,establishments);


      /*  try {
           getPromoNumberData(establishments);
        } catch (JSONException e) {
            e.printStackTrace();
        }*/
    }



    Establishment createEmptyEst()
    {
        Establishment est  = new Establishment();
        est.setId(0);
        est.setName("No venue found near you");
        est.setAddress("Try moving the map around to discover more venues");
        est.setLiqour_license("0");
        est.setContact_person("0");
        est.setContact_number("0");
        est.setEstablishment_url("0");
        est.setLatitude("0");
       // est.setLongitude(estjson.getString(EstablishmentColumns.LONGITUDE));

       // est.setMain_picture_url(estjson.getString(EstablishmentColumns.URLMAINPIC));
       // est.setPicture_2_url(estjson.getString("picture_2"));



        return est;
    }




    //this the first attempt at geofiltering
    void filteringEstablishment(LatLng currentCenter, ArrayList<Establishment> establishments)
    {

        ArrayList<Establishment> filteredList = new ArrayList<>();

        for(Establishment est : establishments)
        {
            try{
                LatLng toDest = new LatLng(Double.valueOf(est.getLatitude()) ,Double.valueOf(est.getLongitude()) ) ;
                Double distance = SphericalUtil.computeDistanceBetween(currentCenter, toDest);

                if(distance <= 10000){
                    filteredList.add(est);
                }
            }catch (Exception e){
                e.printStackTrace();
            }




        }



        initViewPager(filteredList);
        initMarker(filteredList);

        if(filteredList.size()==0)
        {
            filteredList.add(createEmptyEst());
            initViewPager(filteredList);
        }

    }



    private void getPromoNumberData(final ArrayList<Establishment> establishments) throws JSONException
    {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

        for(int i =0; i<establishments.size(); i++){

            String url = String.format(Routes.URL_RETRIEVE_PROMO_EST, establishments.get(i).getId());
            final int finI = i;

            JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {
                    //loadEstablishment(response);

                    establishments.get(finI).setPromoNumber(response.length());

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            });


            requestQueue.add(jsonArrayRequest);
        }


        initViewPager(establishments);

    }


    private void subscribeToPushService() {
        FirebaseMessaging.getInstance().subscribeToTopic("BeerlyBeloved");

        Log.d("BeerlyBeloved", "Subscribed");
      //  Toast.makeText(Home.this, "Subscribed", Toast.LENGTH_SHORT).show();

        String token = FirebaseInstanceId.getInstance().getToken();

        // Log and toast
        Log.d("BeerlyBeloved", token);
       // Toast.makeText(Home.this, token, Toast.LENGTH_SHORT).show();
    }





    ArrayList<Establishment> setupList()
    {
        ArrayList<Establishment> list= new ArrayList<>();

        list.add(new Establishment("The brazen Head","some where"));
        list.add(new Establishment("The Pig Pull","somewhere"));
        list.add(new Establishment("Jagen Jug","somewhere"));
        list.add(new Establishment("The Chisa Nyama","somewhere"));
        list.add(new Establishment("Bonbon Sucree","somewhere"));

        return list;
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
            case MenuPosition.POS_HOME: break;
            case MenuPosition.POS_EVENT:intent = new Intent(getApplicationContext(), EventPage.class);break;
            case MenuPosition.POS_MYPROFILE:intent = new Intent(getApplicationContext(), LoverProfile.class);break;
            case MenuPosition.POS_DISCOUNT:intent = new Intent(getApplicationContext(), Discount.class);break;
            case MenuPosition.POS_PREFERENCE:intent = new Intent(getApplicationContext(), Preferences.class);break;
            case MenuPosition.POS_AFFILIATE:intent = new Intent(getApplicationContext(), Affiliate.class);break;

            default:
                intent = new Intent(getApplicationContext(), Home.class);
        }

        if(intent!=null)
        {
            startActivity(intent);
        }



    }


    protected  void initViewPager(ArrayList<Establishment> establishmentList){
    mRecyclerView =(RecyclerViewPager) findViewById(R.id.recyclerViewEst);

    LinearLayoutManager layoutManager = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL, false);

    mRecyclerView.setLayoutManager(layoutManager);
    mRecyclerView.setAdapter(new EstAdapter(this,mRecyclerView,establishmentList));
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


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private DrawerItem createItemFor(int position) {
        return new SimpleItem(screenIcons[position], screenTitles[position])
                .withIconTint(color(R.color.textColorSecondary))
                .withTextTint(color(R.color.textColorPrimary))
                .withSelectedIconTint(color(R.color.colorAccent))
                .withSelectedTextTint(color(R.color.colorAccent));
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

        if(mGoogleApiClient.isConnected()){
            // Log.v("Google API","Dis-Connecting");
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
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

        if (!mGoogleApiClient.isConnected() && !mGoogleApiClient.isConnecting()){
            // Log.v("Google API","Connecting");
            mGoogleApiClient.connect();
        }

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
        mGoogleMap.getUiSettings().setZoomControlsEnabled(false);
        mGoogleMap.getUiSettings().setZoomGesturesEnabled(true);
        mGoogleMap.setOnCameraChangeListener(this);
        try{
            boolean success = mGoogleMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(getApplicationContext(), R.raw.map_style)
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


       /* mPubMarker = new Marker[5];

        for(int i = 0; i<5;i++)
        {
            mPubMarker[i] = mGoogleMap.addMarker(new MarkerOptions().position(new LatLng(locations[i][0],locations[i][1])).title("Jagen Head")
                    .snippet("Go have fun").icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_map_pin_reg)));
        }*/



        //Marker mpe = mGoogleMap.addMarker(new MarkerOptions().position(pe).title("Jagen Head")
        //.snippet("Go have fun").icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_map_pin)));
    }


    private void initMarker(ArrayList<Establishment> establishments)
    {
        mGoogleMap.clear();
        mPubMarker = new Marker[establishments.size()];

        for(int i = 0; i<establishments.size();i++)
        {
            Establishment est = establishments.get(i);
            if(i==0){
                mPubMarker[i] = mGoogleMap.addMarker(new MarkerOptions().position(new LatLng(Double.parseDouble(est.getLatitude()),Double.parseDouble(est.getLongitude())))
                        .title(est.getName())
                        .snippet("Go have fun").icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_map_pin_select)));
            }else{


            mPubMarker[i] = mGoogleMap.addMarker(new MarkerOptions().position(new LatLng(Double.parseDouble(est.getLatitude()),Double.parseDouble(est.getLongitude())))
                    .title(est.getName())
                    .snippet("Go have fun").icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_map_pin_reg)));
            }
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



    public void checkInvitation ()
    {
        if(sessionManager.getInviteCode()== null || TextUtils.isEmpty(sessionManager.getInviteCode())){
            try {
                getLoverProfileData();
            } catch (JSONException e) {
                e.printStackTrace();
            }catch (Exception e)
            {
                e.printStackTrace();
            }

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


    private void getLoverProfileData() throws JSONException
    {
        String url = String.format(Routes.URL_RETRIEVE_LOVER_PROFILE,mUser.getUid());

        Log.e(TAG , mUser.getUid());
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                loadProfileUI(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

        requestQueue.add(jsonArrayRequest);
    }



    void loadProfileUI(JSONArray profilejsonArray){

        
        for(int i = 0; i<profilejsonArray.length(); i++)
        {
            try{
                JSONObject profjson = profilejsonArray.getJSONObject(i);

                BeerLovers lovers= new BeerLovers();
                lovers.setId(profjson.getInt(BeerLoversColumns.ID));
                lovers.setFirst_name(profjson.getString(BeerLoversColumns.FIRST_NAME));
                lovers.setLast_name(profjson.getString(BeerLoversColumns.LAST_NAME));
                lovers.setDate_of_birth(profjson.getString(BeerLoversColumns.DATE_OF_BIRTH));
                lovers.setCreated_at(profjson.getString(BeerLoversColumns.CREATED_AT));
                lovers.setHome_city (profjson.getString(BeerLoversColumns.HOME_CITY));
                lovers.setGender(profjson.getString(BeerLoversColumns.GENDER));
                lovers.setFirebase_id(profjson.getString(BeerLoversColumns.FIREBASE_ID));
                lovers.setUsername(profjson.getString(BeerLoversColumns.USERNAME));
                lovers.setReferralCode(profjson.getString(BeerLoversColumns.REFERRAL_CODE));
                lovers.setShot(profjson.getInt(BeerLoversColumns.SHOT));
                lovers.setShotType(profjson.getString(BeerLoversColumns.SHOT_TYPE));
                lovers.setCocktail(profjson.getInt(BeerLoversColumns.COCKTAIL));
                lovers.setCocktailType(profjson.getString(BeerLoversColumns.COCKTAIL_TYPE));

                lovers.setInvitation_code(profjson.getString(BeerLoversColumns.INVITATION_CODE));
                // lovers.setEmail(profjson.getString(BeerLoversColumns.EMAIL));
               
                
                //record the invitation code
                sessionManager.recordInvite(lovers.getInvitation_code());

            }
            catch (JSONException e)
            {
                e.printStackTrace();
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
    public void onCameraChange(CameraPosition cameraPosition) {

        Log.d(TAG, "Camera moved");

        mCurrentLatLng = new LatLng(cameraPosition.target.latitude,cameraPosition.target.longitude);

        Location loc  = new Location("");

        loc.setLatitude(cameraPosition.target.latitude);
        loc.setLongitude(cameraPosition.target.longitude);

        String latlong = cameraPosition.target.latitude +", "+cameraPosition.target.longitude;
        Log.d(TAG, latlong);

        filteringEstablishment(mCurrentLatLng,mEstablishments);
        //mLocation= loc;
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


    /*******************************************************these were added to satisfy the autocomplete**************/

    protected synchronized void buildGoogleApiClient()
    {
        mGoogleApiClient = new GoogleApiClient.Builder(Home.this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .addApi(Places.GEO_DATA_API)
                .build();
    }

}
