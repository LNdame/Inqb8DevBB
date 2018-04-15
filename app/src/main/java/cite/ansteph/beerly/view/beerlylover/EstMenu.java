package cite.ansteph.beerly.view.beerlylover;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.net.Uri;
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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.yarolegovich.lovelydialog.LovelyDialogCompat;
import com.yarolegovich.lovelydialog.LovelySaveStateHandler;
import com.yarolegovich.lovelydialog.LovelyStandardDialog;
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
import cite.ansteph.beerly.api.columns.PromotionColumns;
import cite.ansteph.beerly.model.Beer;
import cite.ansteph.beerly.model.Establishment;
import cite.ansteph.beerly.model.Promotion;
import cite.ansteph.beerly.slidingmenu.DrawerAdapter;
import cite.ansteph.beerly.slidingmenu.DrawerItem;
import cite.ansteph.beerly.slidingmenu.MenuPosition;
import cite.ansteph.beerly.slidingmenu.SimpleItem;
import cite.ansteph.beerly.slidingmenu.SpaceItem;
import cite.ansteph.beerly.view.beerlylover.discount.Discount;
import cite.ansteph.beerly.view.beerlylover.event.EventPage;
import cite.ansteph.beerly.view.beerlylover.registration.Registration;

public class EstMenu extends AppCompatActivity implements DrawerAdapter.OnItemSelectedListener {

    private String[] screenTitles;
    private Drawable[] screenIcons;
    private SlidingRootNav slidingRootNav;

    RecyclerView recyclerView;

    ArrayList<Promotion> mPromotionsList ;

    RecyclerView.Adapter mPromoAdapter;

    Establishment mCurrentEstabliment;

    private static final int ID_STANDARD_DIALOG = R.id.fab;
    private LovelySaveStateHandler saveStateHandler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ets_menu);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Bundle bundle = getIntent().getExtras();

            if(bundle!=null){
                mCurrentEstabliment = (Establishment)bundle.getSerializable("Establishment");
                setTitle(mCurrentEstabliment.getName() +"'s Specials");
                //setupUI(mCurrentEstabliment);
            }else{
                Toast.makeText(getApplicationContext(), "Oups! No promo found for this establisment!", Toast.LENGTH_LONG).show();
                this.finish();
            }

        saveStateHandler = new LovelySaveStateHandler();

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

      //  adapter.setSelected(MenuPosition.POS_MYPROFILE);




        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showLovelyDialog(view.getId(), null);

               /* Snackbar.make(view, "This will take you out of the app and unto the Maps", Snackbar.LENGTH_LONG)
                        .setAction("Continue ?", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                getDirections(mCurrentEstabliment.getLatitude(), mCurrentEstabliment.getLongitude());
                            }
                        }).show();*/
            }
        });


        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
        mPromotionsList = new ArrayList<>();


        mPromoAdapter = new BeerMenuRecycleAdapter( mPromotionsList, this);

        recyclerView.setAdapter(mPromoAdapter);

        try {
            getPromoData(mCurrentEstabliment.getId());
        } catch (JSONException e) {
            e.printStackTrace();
        }

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



    private void getPromoData(int est_id) throws JSONException
    {
        String url = String.format(Routes.URL_RETRIEVE_PROMO_EST,String.valueOf(est_id));

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


    void getDirections(String latitude, String longitude  )
    {
        String address  = "http://maps.google.com/maps?daddr=" +latitude+" , "+longitude;

        Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                Uri.parse(address));
        startActivity(intent);
    }


    void loadPromo(JSONArray profilejsonArray){

       // txtDisplayname= (TextView) findViewById(R.id.txtDisplayname);
     //   txtDateCreated= (TextView) findViewById(R.id.txtDateCreated);
      //  txtPrefUpdate = (TextView) findViewById(R.id.txtPrefUpdate);

        mPromotionsList.clear();

        for(int i = 0; i<profilejsonArray.length(); i++)
        {
            try{
                JSONObject profjson = profilejsonArray.getJSONObject(i);

                Promotion promo= new Promotion();
                promo.setId(profjson.getInt(PromotionColumns.ID));
                promo.setEstablishment_id(profjson.getInt(PromotionColumns.ESTABLISMENT_EST_ID));
                promo.setBeer_id(profjson.getInt(PromotionColumns.BEER_BEERID));
                promo.setTitle(profjson.getString(PromotionColumns.TITLE));
                promo.setStart_date(profjson.getString(PromotionColumns.START_DATE));
                promo.setEnd_date(profjson.getString(PromotionColumns.END_DATE));
                promo.setPrice (profjson.getDouble(PromotionColumns.PRICE));
                promo.setStatus(profjson.getString(PromotionColumns.STATUS));

               // JSONObject beerJson = profjson.getJSONObject("beer");

                Beer be = new Beer();
                be.setId(promo.getBeer_id());
                be.setName(profjson.getString(PromotionColumns.BEER_NAME));
                promo.setBeer(be);

                mPromotionsList.add(promo);

            //    txtDisplayname .setText(lovers.getFirst_name() +" "+lovers.getLast_name());
            //    txtDateCreated.setText(lovers.getCreated_at());

            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
        }

        mPromoAdapter.notifyDataSetChanged();

       /* try {
            getBeers(mPromotionsList);
        } catch (JSONException e) {
            e.printStackTrace();
        }*/
    }





  private  void getbeer(int id)throws JSONException
  {
      String url = ""+String.format(Routes.URL_RETRIEVE_A_BEER,String.valueOf(id));

      StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
              new Response.Listener<String>() {
                  @Override
                  public void onResponse(String response) {
                      try{
                          JSONObject beerjson = new JSONObject(response);

                          Beer beer = new Beer(beerjson.getInt("id"),
                                  beerjson.getString("name"),
                                  beerjson.getString("description"),
                                  beerjson.getString("vendor"),
                                  beerjson.getDouble("percentage")
                          );

                          mPromotionsList.get(0).setBeer(beer);
                          mPromoAdapter.notifyDataSetChanged();

                      }catch (JSONException e)
                      {
                          e.printStackTrace();
                      }
                  }
              }, new Response.ErrorListener() {
          @Override
          public void onErrorResponse(VolleyError error) {

          }
      }){};
      RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
      requestQueue.add(stringRequest);

  }





    private void getBeers(final ArrayList<Promotion> promotions) throws JSONException
    {

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

        if(promotions!= null && promotions.size()>0)
        {

            for(int i  =0; i<promotions.size(); i++)
            {

                String url = String.format(Routes.URL_RETRIEVE_A_BEER,String.valueOf(promotions.get(i).getBeer_id()));
                final int finalI = i;
                JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        try{
                            JSONObject beerjson = response.getJSONObject(0);

                           Beer beer = new Beer(beerjson.getInt("id"),
                                    beerjson.getString("name"),
                                    beerjson.getString("description"),
                                    beerjson.getString("vendor"),
                                    beerjson.getDouble("percentage")
                            );

                            mPromotionsList.get(finalI).setBeer(beer);

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
                requestQueue.add(jsonArrayRequest);
            }


           // mPromotionsList.clear();
           // mPromotionsList = promotions;
            mPromoAdapter.notifyDataSetChanged();


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
    public void onItemSelected(int position) {
        if (position == MenuPosition.POS_LOGOUT) {
            finish();
        }
        slidingRootNav.closeMenu();

        Intent intent = null;

        switch (position)
        {
            case MenuPosition.POS_HOME: intent = new Intent(getApplicationContext(), Home.class);break;
            case MenuPosition.POS_EVENT:intent = new Intent(getApplicationContext(), EventPage.class);break;
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



    @Override
    public void onSaveInstanceState(Bundle outState) {
        saveStateHandler.saveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedState) {
        super.onRestoreInstanceState(savedState);
        if (LovelySaveStateHandler.wasDialogOnScreen(savedState)) {
            //Dialog won't be restarted automatically, so we need to call this method.
            //Each dialog knows how to restore its state
            showLovelyDialog(LovelySaveStateHandler.getSavedDialogId(savedState), savedState);
        }
    }




    private void showStandardDialog(Bundle savedInstanceState) {
        new LovelyStandardDialog(this, LovelyStandardDialog.ButtonLayout.VERTICAL)
                .setTopColorRes( R.color.colorAccent)
                .setButtonsColorRes(R.color.colorPrimaryDark)
                .setIcon(R.drawable.ic_car)
                .setTitle(R.string.arrive_alive)
                .setInstanceStateHandler(ID_STANDARD_DIALOG, saveStateHandler)
                .setSavedInstanceState(savedInstanceState)
                .setMessage(R.string.arrive_alive_msg)
                .setPositiveButton(R.string.ar_alive_ok, LovelyDialogCompat.wrap(new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(Intent.ACTION_VIEW);
                                intent.setData(Uri.parse("market://details?id=ee.mtakso.client&hl=en"));
                                startActivity(intent);

                            }
                        })
                        //LovelyDialogCompat.wrap(
                        // (dialog, which) -> Toast.makeText(Profile.this,
                        //       R.string.repo_waiting,
                        //     Toast.LENGTH_SHORT)
                        //   .show())
                )
                .setNegativeButton(R.string.ar_alive_neutral,LovelyDialogCompat.wrap(new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        getDirections(mCurrentEstabliment.getLatitude(), mCurrentEstabliment.getLongitude());
                    }
                }))
                .show();
    }


    private void showLovelyDialog(int dialogId, Bundle savedInstanceState) {
        switch (dialogId) {
            case ID_STANDARD_DIALOG:
                showStandardDialog(savedInstanceState);
                break;

        }
    }

}
