package cite.ansteph.beerly.view.beerlylover;

import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.auth.FirebaseAuth;
import com.yarolegovich.slidingrootnav.SlidingRootNav;
import com.yarolegovich.slidingrootnav.SlidingRootNavBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import cite.ansteph.beerly.R;
import cite.ansteph.beerly.adapter.BeerPrefRecyclerAdapter;
import cite.ansteph.beerly.adapter.BeerRecyclerViewAdapter;
import cite.ansteph.beerly.api.Routes;
import cite.ansteph.beerly.api.columns.BeerColumns;
import cite.ansteph.beerly.api.columns.BeerLoversColumns;
import cite.ansteph.beerly.api.columns.PreferenceColumns;
import cite.ansteph.beerly.api.columns.UserColumns;
import cite.ansteph.beerly.helper.RecyclerItemTouchHelper;
import cite.ansteph.beerly.helper.SessionManager;
import cite.ansteph.beerly.listener.RecyclerViewClickListener;
import cite.ansteph.beerly.model.Beer;
import cite.ansteph.beerly.model.Establishment;
import cite.ansteph.beerly.model.Preference;
import cite.ansteph.beerly.slidingmenu.DrawerAdapter;
import cite.ansteph.beerly.slidingmenu.DrawerItem;
import cite.ansteph.beerly.slidingmenu.MenuPosition;
import cite.ansteph.beerly.slidingmenu.SimpleItem;
import cite.ansteph.beerly.slidingmenu.SpaceItem;
import cite.ansteph.beerly.view.beerlylover.affiliate.Affiliate;
import cite.ansteph.beerly.view.beerlylover.discount.Discount;
import cite.ansteph.beerly.view.beerlylover.event.EventPage;
import cite.ansteph.beerly.view.beerlylover.registration.Registration;

public class Preferences extends AppCompatActivity implements RecyclerViewClickListener ,DrawerAdapter.OnItemSelectedListener,
        RecyclerItemTouchHelper.RecyclerItemTouchHelperListener {
    RecyclerView recyclerView , prefRecyclerView;

    ArrayList<Beer> mBeersList ;
    ArrayList<Beer> mBeersPrefList ;
    RecyclerView.Adapter mBeerAdapter;
    BeerPrefRecyclerAdapter mPrefBeerAdapter;

    ArrayList<Preference> mTempPreferenceList ;

    private String[] screenTitles;
    private Drawable[] screenIcons;
    private SlidingRootNav slidingRootNav;

    int mPrefCount=0;

    HashMap<Integer, Beer> LoversPreferences;

    TextView txtPrefCount ;
    SessionManager sessionManager;
    RelativeLayout container;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sessionManager = new SessionManager(getApplicationContext());


        setContentView(R.layout.activity_preferences);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        LoversPreferences = new HashMap<>();
        slidingRootNav = new SlidingRootNavBuilder(this)
                .withToolbarMenuToggle(toolbar)
                .withMenuOpened(false)
                .withContentClickableWhenMenuOpened(false)
                .withSavedState(savedInstanceState)
                .withMenuLayout(R.layout.menu_left_drawer)
                .inject();

        screenIcons = loadScreenIcons();
        screenTitles = loadScreenTitles();

        DrawerAdapter adapter = new DrawerAdapter(Arrays.asList(
                createItemFor(MenuPosition.POS_HOME),
                createItemFor(MenuPosition.POS_EVENT),
                createItemFor(MenuPosition.POS_MYPROFILE),
                createItemFor(MenuPosition.POS_DISCOUNT),
                createItemFor(MenuPosition.POS_PREFERENCE).setChecked(true),
                //createItemFor(MenuPosition.POS_AFFILIATE),
                new SpaceItem(48),
                createItemFor(MenuPosition.POS_LOGOUT)));
        adapter.setListener(this);

        container  = (RelativeLayout) findViewById(R.id.containerlyt) ;
        RecyclerView list = (RecyclerView) findViewById(R.id.list);

        // RecyclerView list = findViewById(R.id.list);
        list.setNestedScrollingEnabled(false);
        list.setLayoutManager(new LinearLayoutManager(this));
        list.setAdapter(adapter);

        adapter.setSelected(MenuPosition.POS_PREFERENCE);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());


        txtPrefCount = (TextView) findViewById(R.id.txtpreferences);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);



        mBeersPrefList = new ArrayList<>();
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
        mBeersList = setupList();

        mBeerAdapter = new BeerRecyclerViewAdapter(this, mBeersList, Preferences.this);

        recyclerView.setAdapter(mBeerAdapter);



        prefRecyclerView = (RecyclerView) findViewById(R.id.prefrecyclerview);
        prefRecyclerView.setLayoutManager(mLayoutManager);
        prefRecyclerView.setItemAnimator(new DefaultItemAnimator());
        prefRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        mPrefBeerAdapter = new BeerPrefRecyclerAdapter(mBeersPrefList, this);

        prefRecyclerView.setAdapter(mPrefBeerAdapter);

        try {
            getBeerData();
        } catch (JSONException e) {
            e.printStackTrace();
        }


        try {
            getBeerPreferenceData();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // adding item touch helper
        // only ItemTouchHelper.LEFT added to detect Right to Left swipe
        // if you want both Right -> Left and Left -> Right
        // add pass ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT as param
        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT, this);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(prefRecyclerView);


        ItemTouchHelper.SimpleCallback itemTouchHelperCallback1 = new  ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT | ItemTouchHelper.UP){

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {

            }

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        };
        // attaching the touch helper to recycler view
        new ItemTouchHelper(itemTouchHelperCallback1).attachToRecyclerView(prefRecyclerView);


    }



    //Dummy data to be removed or retrofitted once the database is live
    ArrayList<Beer> setupList()
    {
        ArrayList<Beer>  beers = new ArrayList<>();

        beers.add(new Beer (1,"Carling Black Label"));
        beers.add(new Beer (2,"Carling Blue Label Beer"));
        beers.add(new Beer (3,"Castle Lager"));
        beers.add(new Beer (4,"Castle Lite"));
        beers.add(new Beer (5,"Castle Milk Stout"));
        beers.add(new Beer (6,"Hansa Pilsner"));
        beers.add(new Beer (7,"Pilsner Urquell"));
        beers.add(new Beer (8,"Peroni Nastro Azzurro"));
        beers.add(new Beer (9,"Grolsch"));
        beers.add(new Beer (10,"Redd's"));
        beers.add(new Beer (11,"Brutal Fruit"));
        beers.add(new Beer (12,"Flying Fish"));


        // String duration, String task_date, String start, String end, String project, String description, String realduration, String task_break) {
        return  beers;
    }


    private void getBeerData() throws JSONException
    {
        String url = String.format(Routes.URL_RETRIEVE_BEERS);

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                loadBeer(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });


        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

        requestQueue.add(jsonArrayRequest);
    }



    private void loadBeer(JSONArray beerjsonArray)
    {
        ArrayList<Beer> beers = new ArrayList<>();
      mBeersList.clear();
        for(int i = 0; i<beerjsonArray.length(); i++)
        {
            try{
                JSONObject itemjson = beerjsonArray.getJSONObject(i);

                Beer br = new Beer();
                br.setId(itemjson.getInt(BeerColumns.BEERID));
                br.setName(itemjson.getString(BeerColumns.NAME));
                br.setVendor(itemjson.getString(BeerColumns.VENDOR));
                br.setDescription(itemjson.getString(BeerColumns.DESCRIPTION));
                br.setPercentage(itemjson.getDouble(BeerColumns.PERCENTAGE));

                // est.set(estjson.getString("hs_license"));
                // est.setName(estjson.getString(""));

                mBeersList.add(br);


            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
        }


       // mBeersList =beers;
        mBeerAdapter.notifyDataSetChanged();

        //initViewPager(establishments);
        //initMarker(establishments);
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
    public void onItemSelected(int position) {
        if (position == MenuPosition.POS_LOGOUT || position == MenuPosition.EF_POS_LOGOUT) {
            finish();
        }
        slidingRootNav.closeMenu();

        Intent intent = null;

        switch (position)
        {
            case MenuPosition.POS_HOME: intent = new Intent(getApplicationContext(), Home.class);break;
            case MenuPosition.POS_EVENT:intent = new Intent(getApplicationContext(), EventPage.class);break;
            case MenuPosition.POS_MYPROFILE:intent = new Intent(getApplicationContext(), LoverProfile.class);break;
            case MenuPosition.POS_DISCOUNT:intent = new Intent(getApplicationContext(), Affiliate.class);break;
            case MenuPosition.POS_PREFERENCE:;break;
            case MenuPosition.POS_AFFILIATE:intent = new Intent(getApplicationContext(), Discount.class);break;

            default:
                intent = new Intent(getApplicationContext(), Home.class);
        }

        if(intent!=null)
        {
            startActivity(intent);
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.preferences_menu, menu);

       /* MenuItem loggedUser = menu.findItem(R.id.action_loggedUser);
        if(mGlobalRetainer.get_grClient().getName()!=null)
        {
            loggedUser.setTitle(mGlobalRetainer.get_grClient().getName());
        }*/
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if(id==R.id.action_savepref)
        {

            if(mPrefCount>=3){

                try {

                    if(mTempPreferenceList!=null && mTempPreferenceList.size()>=3){

                        //make sure that the temp array has been actualized
                        modifyPreferenceTemp();

                        updatePreferences();
                    }else{
                        storePreferences();
                    }



                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }else{
                String msg = "You still have less than 3 preferences. Please select "+(3- mPrefCount)+" more";

                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
            }


        }

       /* //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            //  return true;
            Intent i = new Intent(getApplicationContext(), ActionList.class);
            startActivity(i);
        }


        if (id == R.id.action_logout) {
            sessionManager.logoutUser();
        }
        if (id == R.id.action_profile) {
            Intent i = new Intent(getApplicationContext(), EditProfile.class);
            startActivity(i);
        }*/

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onRecyclerViewItemClicked(View v, int position) {
        //Toast.makeText(getApplicationContext(),"Clicked",Toast.LENGTH_LONG).show();


        if(mPrefCount<3)
        {
            boolean shouldAdd  = true;

            for(Beer beer: mBeersPrefList) {
                if (beer.getName().equals(mBeersList.get(position).getName()))
                    shouldAdd =false;
            }
            if(shouldAdd) {

                mPrefCount++;
                LoversPreferences.put(position, mBeersList.get(position));


                mBeersPrefList.add(mBeersList.get(position));
                // refreshing recycler view
                mPrefBeerAdapter.notifyDataSetChanged();
                String ct = "(" + (3 - mPrefCount) + " more)";
                txtPrefCount.setText(ct);
            }

        }
    }







    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        if(viewHolder instanceof  BeerPrefRecyclerAdapter.PrefViewHolder)
        {
            // get the removed item name to display it in snack bar
            String name = mBeersList.get(viewHolder.getAdapterPosition()).getName();

            // backup of removed item for undo purpose
            final Beer deletedItem = mBeersList.get(viewHolder.getAdapterPosition());
            final int deletedIndex = viewHolder.getAdapterPosition();


            // remove the item from recycler view
            mPrefBeerAdapter.removeItem(viewHolder.getAdapterPosition());

            mPrefCount--;
            String ct = "(" +(3-mPrefCount)+" more)";
            txtPrefCount.setText(ct);

            // showing snack bar with Undo option
            Snackbar snackbar = Snackbar
                    .make(container, name + " removed from list!", Snackbar.LENGTH_LONG);
            snackbar.setAction("UNDO", new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    // undo is selected, restore the deleted item
                    mPrefBeerAdapter.restoreItem(deletedItem, deletedIndex);

                    mPrefCount++;
                    String ct = "(" +(3-mPrefCount)+" more)";
                    txtPrefCount.setText(ct);
                }
            });
            snackbar.setActionTextColor(Color.YELLOW);
            snackbar.show();

        }
    }


    public void storePreferences() throws JSONException {

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

        if(mBeersPrefList!=null && mBeersPrefList.size()>0)
        {
            sessionManager.recordPreference(mBeersPrefList.get(0).getId(),mBeersPrefList.get(1).getId(),mBeersPrefList.get(2).getId());

            for(int i =0; i<mBeersPrefList.size();i++)
            {

                final String fireID = FirebaseAuth.getInstance().getCurrentUser().getUid();
                final String beerID = String.valueOf(mBeersPrefList.get(i).getId()) ;
                final String preference_number  = String.valueOf(i+1);


               // if(mTempPreferenceList!=null && )

                String url = String.format(Routes.URL_STORE_PREFERENCES);

                StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //  loading.dismiss();

                        try{
                            //creating the Json object from the response
                            JSONObject jsonResponse = new JSONObject(response);
                          //  sessionManager.recordRegistration(mFirebaseUID);


                            startActivity(new Intent(getApplicationContext(), Home.class));


                        }catch (JSONException e)
                        {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //   loading.dismiss();
                        Toast.makeText(getApplicationContext(), error.getMessage()+"Oups! Could not talk to the server at this time, try again later",Toast.LENGTH_LONG).show();
                    }
                }

                ){
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<>();

                        params.put(PreferenceColumns.PREFERENCE_NUMBER, preference_number);
                        params.put(PreferenceColumns.BEER_ID, beerID);
                        params.put(PreferenceColumns.FIREBASE_ID, fireID);


                        return params;
                    }
                };


                //  GlobalRetainer.getInstance().addToRequestQueue();

                requestQueue.add(stringRequest);


            }

        }





    }




    public void updatePreferences() throws JSONException {

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());


                // if(mTempPreferenceList!=null && )
        sessionManager.recordPreference(mTempPreferenceList.get(0).getId(),mTempPreferenceList.get(1).getId(),mTempPreferenceList.get(2).getId());


        String url = String.format(Routes.URL_UPDATE_PREFERENCES, FirebaseAuth.getInstance().getCurrentUser().getUid());

                StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //  loading.dismiss();

                        try{
                            //creating the Json object from the response
                            JSONArray jsonResponse = new JSONArray(response);
                            //  sessionManager.recordRegistration(mFirebaseUID);


                            startActivity(new Intent(getApplicationContext(), Home.class));


                        }catch (JSONException e)
                        {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //   loading.dismiss();
                        Toast.makeText(getApplicationContext(), error.getMessage()+"Oups! Could not talk to the server at this time, try again later",Toast.LENGTH_LONG).show();
                    }
                }

                ){
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<>();

                        params.put(PreferenceColumns.PREFERENCE1, String.valueOf(mTempPreferenceList.get(0).getBeer_id()) );
                        params.put(PreferenceColumns.PREFERENCE2, String.valueOf(mTempPreferenceList.get(1).getBeer_id()));
                        params.put(PreferenceColumns.PREFERENCE3, String.valueOf(mTempPreferenceList.get(2).getBeer_id()));


                        return params;
                    }
                };


                //  GlobalRetainer.getInstance().addToRequestQueue();

                requestQueue.add(stringRequest);


    }





    private void getBeerPreferenceData() throws JSONException
    {

        final String fireID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        String url = String.format(Routes.URL_RETRIEVE_BEER_PREFERENCES,fireID);

       // Log.e(TAG , mUser.getUid());
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                loadPreferences(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });


        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

        requestQueue.add(jsonArrayRequest);
    }


    private void loadPreferences(JSONArray prefjsonArray)
    {
        mTempPreferenceList= new ArrayList<>();
        for(int i = 0; i<prefjsonArray.length(); i++)
        {
            try{

                JSONObject prefjson = prefjsonArray.getJSONObject(i);

                //Preference object to be hold in a temp in case the user doesn't change anything
                Preference pref = new Preference();

                pref.setId(prefjson.getInt(PreferenceColumns.ID));
                pref.setBeer_id(prefjson.getInt(PreferenceColumns.BEER_ID));
                pref.setBeer_lover_id(prefjson.getInt(PreferenceColumns.BEERLOVERS_UUID));
                pref.setPreference_number(prefjson.getInt(PreferenceColumns.PREFERENCE_NUMBER));

                pref.setName(prefjson.getString(PreferenceColumns.BEER_NAME));
                pref.setVendor(prefjson.getString(PreferenceColumns.BEER_VENDOR));


                //the object actually loaded in ui
                Beer beer = new Beer();
                beer.setId(prefjson.getInt(PreferenceColumns.BEER_ID));
                beer.setName(prefjson.getString(PreferenceColumns.BEER_NAME));
                beer.setVendor(prefjson.getString(PreferenceColumns.BEER_VENDOR));



                mTempPreferenceList.add(pref);
                mBeersPrefList.add(beer);
                mPrefCount++;
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }

        }


        String ct = "(" +(3-mPrefCount)+" more)";
        txtPrefCount.setText(ct);

        mPrefBeerAdapter.notifyDataSetChanged();

    }



    void modifyPreferenceTemp()
    {

        if(mTempPreferenceList!=null && mTempPreferenceList.size()>0)
        {
            for(int i =0 ; i<mTempPreferenceList.size(); i++)
            {
                mTempPreferenceList.get(i).setBeer_id(mBeersPrefList.get(i).getId());
                mTempPreferenceList.get(i).setName(mBeersPrefList.get(i).getName());
                mTempPreferenceList.get(i).setVendor(mBeersPrefList.get(i).getVendor());

            }
        }




    }



}


