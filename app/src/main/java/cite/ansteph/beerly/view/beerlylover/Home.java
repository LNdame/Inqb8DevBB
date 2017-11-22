package cite.ansteph.beerly.view.beerlylover;

import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
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
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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
import cite.ansteph.beerly.app.GlobalRetainer;
import cite.ansteph.beerly.model.Establishment;
import cite.ansteph.beerly.slidingmenu.DrawerAdapter;
import cite.ansteph.beerly.slidingmenu.DrawerItem;
import cite.ansteph.beerly.slidingmenu.MenuPosition;
import cite.ansteph.beerly.slidingmenu.SimpleItem;
import cite.ansteph.beerly.slidingmenu.SpaceItem;
import cite.ansteph.beerly.view.beerlylover.discount.Discount;
import cite.ansteph.beerly.view.beerlylover.registration.Login;
import cite.ansteph.beerly.view.beerlylover.registration.Registration;

public class Home extends AppCompatActivity implements DrawerAdapter.OnItemSelectedListener , OnMapReadyCallback {


    private String[] screenTitles;
    private Drawable[] screenIcons;

    private SlidingRootNav slidingRootNav;
    MapView mMapView;
    Marker [] mPubMarker ;

    protected RecyclerViewPager mRecyclerView;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;

    FirebaseUser mUser;

    public static final  int RC_SIGN_IN =1;


    double locations[][] = new double[][]{{-33.955239, 25.611931},
        { -33.9736, 25.5983},
            {-33.914472, 25.602237},
                {-33.955239, 25.611931},
                    { -33.939309, 25.559880},


    };


    private GoogleMap mGoogleMap;
    ArrayList<Establishment> mEstablishments ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


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


    }


    private void getEstablismentData() throws JSONException
    {
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



    private void loadEstablishment(JSONArray estjsonArray)
    {
        ArrayList<Establishment> establishments = new ArrayList<>();

        for(int i = 0; i<estjsonArray.length(); i++)
        {
          try{
              JSONObject estjson = estjsonArray.getJSONObject(i);

              Establishment est  = new Establishment();
              est.setId(estjson.getInt("id"));
              est.setName(estjson.getString("name"));
              est.setAddress(estjson.getString("address"));
              est.setLiqour_license(estjson.getString("liqour_license"));
              est.setContact_person(estjson.getString("contact_person"));
              est.setContact_number(estjson.getString("contact_number"));
              est.setEstablishment_url(estjson.getString("establishment_url"));
              est.setLatitude(estjson.getString("latitude"));
              est.setLongitude(estjson.getString("longitude"));

              est.setMain_picture_url(estjson.getString("main_picture_url"));
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

        initViewPager(establishments);
        initMarker(establishments);
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
            case MenuPosition.POS_MYPROFILE:intent = new Intent(getApplicationContext(), LoverProfile.class);break;
            case MenuPosition.POS_DISCOUNT:intent = new Intent(getApplicationContext(), Discount.class);break;
            case MenuPosition.POS_PREFERENCE:intent = new Intent(getApplicationContext(), Preferences.class);break;
            case MenuPosition.POS_AFFILIATE:intent = new Intent(getApplicationContext(), Registration.class);break;

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
            Log.d("test", "oldPosition:" + oldPosition + " newPosition:" + newPosition);
            Toast.makeText(getApplicationContext(), " Position:" + mRecyclerView.getCurrentPosition(), Toast.LENGTH_LONG).show();

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

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
        mGoogleMap.getUiSettings().setZoomControlsEnabled(false);
        mGoogleMap.getUiSettings().setZoomGesturesEnabled(true);

        LatLng pe = new LatLng(-33.9736, 25.5983);
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(pe,12.5f));

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
        mPubMarker = new Marker[establishments.size()];

        for(int i = 0; i<establishments.size();i++)
        {
            Establishment est = establishments.get(i);
            mPubMarker[i] = mGoogleMap.addMarker(new MarkerOptions().position(new LatLng(Double.parseDouble(est.getLatitude()),Double.parseDouble(est.getLongitude())))
                    .title(est.getName())
                    .snippet("Go have fun").icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_map_pin_reg)));
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
}
