package cite.ansteph.beerly.view.beerlylover;

import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.yarolegovich.slidingrootnav.SlidingRootNav;
import com.yarolegovich.slidingrootnav.SlidingRootNavBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

import cite.ansteph.beerly.R;
import cite.ansteph.beerly.adapter.BeerPrefRecyclerAdapter;
import cite.ansteph.beerly.api.Routes;
import cite.ansteph.beerly.api.columns.BeerLoversColumns;
import cite.ansteph.beerly.model.Beer;
import cite.ansteph.beerly.model.BeerLovers;
import cite.ansteph.beerly.model.Establishment;
import cite.ansteph.beerly.slidingmenu.DrawerAdapter;
import cite.ansteph.beerly.slidingmenu.DrawerItem;
import cite.ansteph.beerly.slidingmenu.MenuPosition;
import cite.ansteph.beerly.slidingmenu.SimpleItem;
import cite.ansteph.beerly.slidingmenu.SpaceItem;
import cite.ansteph.beerly.utils.DateTimeUtils;
import cite.ansteph.beerly.view.beerlylover.discount.Discount;
import cite.ansteph.beerly.view.beerlylover.registration.Login;
import cite.ansteph.beerly.view.beerlylover.registration.Registration;
import de.hdodenhof.circleimageview.CircleImageView;

public class LoverProfile extends AppCompatActivity implements DrawerAdapter.OnItemSelectedListener {
    private String[] screenTitles;
    private Drawable[] screenIcons;
    private SlidingRootNav slidingRootNav;

    private static  String TAG  = LoverProfile.class.getSimpleName();

    RecyclerView recyclerView , prefRecyclerView;

    ArrayList<Beer> mBeersList ;
    ArrayList<Beer> mBeersPrefList ;
    BeerPrefRecyclerAdapter mPrefBeerAdapter;

    TextView txtDisplayname, txtDateCreated, txtPrefUpdate ;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    FirebaseUser mUser;

    BeerLovers mBeerLovers;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lover_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mUser = FirebaseAuth.getInstance().getCurrentUser();
        mAuth = FirebaseAuth.getInstance();
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
                createItemFor(MenuPosition.POS_MYPROFILE).setChecked(true),
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

        adapter.setSelected(MenuPosition.POS_MYPROFILE);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());

        prefRecyclerView = (RecyclerView) findViewById(R.id.prefrecyclerview);
        mBeersPrefList = new ArrayList<>();
        mBeersPrefList= setupList();
        prefRecyclerView.setLayoutManager(mLayoutManager);

        mPrefBeerAdapter = new BeerPrefRecyclerAdapter(mBeersPrefList, this);

        prefRecyclerView.setAdapter(mPrefBeerAdapter);



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
            getLoverProfileData();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        populateProfilePic();
    }



    ArrayList<Beer> setupList()
    {
        ArrayList<Beer>  beers = new ArrayList<>();

        beers.add(new Beer (1,"Carling Black Label"));
        beers.add(new Beer (2,"Carling Blue Label Beer"));
        beers.add(new Beer (3,"Castle Lager"));



        // String duration, String task_date, String start, String end, String project, String description, String realduration, String task_break) {
        return  beers;
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

    private void signOut() {
        mAuth.signOut();
        //updateUI(null);
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
        if (position == MenuPosition.POS_LOGOUT) {
            signOut();
            //finish();
        }
        slidingRootNav.closeMenu();

        Intent intent = null;

        switch (position)
        {
            case MenuPosition.POS_HOME: intent = new Intent(getApplicationContext(), Home.class);break;
            case MenuPosition.POS_MYPROFILE:;break;
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

         txtDisplayname= (TextView) findViewById(R.id.txtDisplayname);
        txtDateCreated= (TextView) findViewById(R.id.txtDateCreated);
        txtPrefUpdate = (TextView) findViewById(R.id.txtPrefUpdate);


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
               // lovers.setEmail(profjson.getString(BeerLoversColumns.EMAIL));
                // est.set(estjson.getString("hs_license"));
                // est.setName(estjson.getString(""));


                txtDisplayname .setText(lovers.getFirst_name() +" "+lovers.getLast_name());

                String joined = DateTimeUtils.datetoStringShort(lovers.getCreated_at());

                txtDateCreated.setText(joined);

                mBeerLovers = lovers;

            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
        }




    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.lover_profile_menu, menu);

        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if(id==R.id.action_edit_profile){
            Intent i = new Intent( getApplicationContext(), EditLoverProfile.class);
            i.putExtra("lover", mBeerLovers );
            startActivity(i);
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


     void populateProfilePic()
     {
         CircleImageView img = (CircleImageView) findViewById(R.id.avatar);

         FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
         if (user.getPhotoUrl() != null) {
             //GlideApp.with(this).load(user.getPhotoUrl()).fitCenter().into(mUserProfilePicture);

             Glide.with(getApplicationContext()).load(user.getPhotoUrl()).fitCenter()
                     .into(img);
         }
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

        if(mAuthStateListener!=null)
            mAuth.removeAuthStateListener(mAuthStateListener);

    }







    @Override
    public void onResume() {
        super.onResume();

        mAuth.addAuthStateListener(mAuthStateListener);

    }


}
