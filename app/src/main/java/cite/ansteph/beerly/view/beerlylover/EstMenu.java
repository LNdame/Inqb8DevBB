package cite.ansteph.beerly.view.beerlylover;

import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.yarolegovich.slidingrootnav.SlidingRootNav;
import com.yarolegovich.slidingrootnav.SlidingRootNavBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

import cite.ansteph.beerly.R;
import cite.ansteph.beerly.adapter.BeerMenuRecycleAdapter;
import cite.ansteph.beerly.api.Routes;
import cite.ansteph.beerly.model.Beer;
import cite.ansteph.beerly.model.Establishment;
import cite.ansteph.beerly.model.Promotion;
import cite.ansteph.beerly.slidingmenu.DrawerAdapter;
import cite.ansteph.beerly.slidingmenu.DrawerItem;
import cite.ansteph.beerly.slidingmenu.MenuPosition;
import cite.ansteph.beerly.slidingmenu.SimpleItem;
import cite.ansteph.beerly.slidingmenu.SpaceItem;
import cite.ansteph.beerly.view.beerlylover.discount.Discount;
import cite.ansteph.beerly.view.beerlylover.registration.Registration;

public class EstMenu extends AppCompatActivity implements DrawerAdapter.OnItemSelectedListener {

    private String[] screenTitles;
    private Drawable[] screenIcons;
    private SlidingRootNav slidingRootNav;

    RecyclerView recyclerView;

    ArrayList<Promotion> mPromotionsList ;

    RecyclerView.Adapter mPromoAdapter;

    Establishment mCurrentEstabliment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ets_menu);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Bundle bundle = getIntent().getExtras();

            if(bundle!=null){
                mCurrentEstabliment = (Establishment)bundle.getSerializable("profile");
                setTitle(mCurrentEstabliment.getName() +"'s Promotions");
                //setupUI(mCurrentEstabliment);
            }else{
                Toast.makeText(getApplicationContext(), "Oups! No promo found for this establisment!", Toast.LENGTH_LONG).show();
                this.finish();
            }


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

      //  adapter.setSelected(MenuPosition.POS_MYPROFILE);




        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
        mPromotionsList = setupList();


        mPromoAdapter = new BeerMenuRecycleAdapter( mPromotionsList, this);

        recyclerView.setAdapter(mPromoAdapter);


    }


    ArrayList<Promotion> setupList()
    {
        ArrayList<Promotion>  promotions = new ArrayList<>();

        promotions.add(new Promotion ("Happy hours", "2017-11-13 04:42:22","2017-11-13 04:42:22", 20.00,new Beer(1, "Castle light")));
        promotions.add(new Promotion("Bafana's Dissapointment (again)", "2017-11-13 04:42:22","2017-11-13 04:42:22", 20.00,new Beer(1, "Carling Black Label")));
        promotions.add(new Promotion ("Christmas Special ", "2017-11-13 04:42:22","2017-11-13 04:42:22", 20.00,new Beer(1, "Castle light")));
        promotions.add(new Promotion ("Boks Victory Tours", "2017-11-13 04:42:22","2017-11-13 04:42:22", 20.00,new Beer(1, "Castle Draught")));


        // String duration, String task_date, String start, String end, String project, String description, String realduration, String task_break) {
        return  promotions;
    }



    private void getLoverProfileData(int id) throws JSONException
    {
        String url = String.format(Routes.URL_RETRIEVE_LOVER_PROFILE,String.valueOf(id));

       // Log.e(TAG , mUser.getUid());
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                loadPromo(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });


        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

        requestQueue.add(jsonArrayRequest);
    }


    void loadPromo(JSONArray profilejsonArray){

       // txtDisplayname= (TextView) findViewById(R.id.txtDisplayname);
     //   txtDateCreated= (TextView) findViewById(R.id.txtDateCreated);
      //  txtPrefUpdate = (TextView) findViewById(R.id.txtPrefUpdate);


        for(int i = 0; i<profilejsonArray.length(); i++)
        {
            try{
                JSONObject profjson = profilejsonArray.getJSONObject(i);

                Promotion promo= new Promotion();
                promo.setId(profjson.getInt("id"));
                promo.setEstablishment_id(profjson.getInt("establishment_id"));
                promo.setBeer_id(profjson.getInt("beer_id"));
                promo.setTitle(profjson.getString("title"));
                promo.setStart_date(profjson.getString("start_date"));
                promo.setEnd_date(profjson.getString("end_date"));
                promo.setPrice (profjson.getDouble("price"));
                promo.setStatus(profjson.getString("status"));

                JSONObject beerJson = profjson.getJSONObject("beer");

                Beer be = new Beer();

            //    txtDisplayname .setText(lovers.getFirst_name() +" "+lovers.getLast_name());
            //    txtDateCreated.setText(lovers.getCreated_at());

            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
        }

        mPromoAdapter.notifyDataSetChanged();


    }






    private Beer getBeer(int id) throws JSONException
    {
        String url = String.format(Routes.URL_RETRIEVE_LOVER_PROFILE,String.valueOf(id));

        final Beer[] beer = {null};
        // Log.e(TAG , mUser.getUid());
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                try{
                    JSONObject beerjson = response.getJSONObject(0);

                     beer[0] = new Beer(beerjson.getInt("id"),
                            beerjson.getString("name"),
                            beerjson.getString("description"),
                            beerjson.getString("vendor"),
                            beerjson.getDouble("percentage")


                    );


                }catch (JSONException e)
                {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });


        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

        requestQueue.add(jsonArrayRequest);

        return beer[0];
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
        if (position == MenuPosition.POS_LOGOUT) {
            finish();
        }
        slidingRootNav.closeMenu();

        Intent intent = null;

        switch (position)
        {
            case MenuPosition.POS_HOME: intent = new Intent(getApplicationContext(), Home.class);break;
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
}
