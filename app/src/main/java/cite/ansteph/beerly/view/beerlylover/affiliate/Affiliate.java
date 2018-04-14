package cite.ansteph.beerly.view.beerlylover.affiliate;

import android.content.ClipData;
import android.content.ClipboardManager;
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
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.auth.FirebaseAuth;
import com.yarolegovich.slidingrootnav.SlidingRootNav;
import com.yarolegovich.slidingrootnav.SlidingRootNavBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

import cite.ansteph.beerly.R;
import cite.ansteph.beerly.api.Routes;
import cite.ansteph.beerly.api.columns.BeerLoversColumns;
import cite.ansteph.beerly.helper.SessionManager;
import cite.ansteph.beerly.model.BeerLovers;
import cite.ansteph.beerly.slidingmenu.DrawerAdapter;
import cite.ansteph.beerly.slidingmenu.DrawerItem;
import cite.ansteph.beerly.slidingmenu.MenuPosition;
import cite.ansteph.beerly.slidingmenu.SimpleItem;
import cite.ansteph.beerly.slidingmenu.SpaceItem;
import cite.ansteph.beerly.view.beerlylover.Home;
import cite.ansteph.beerly.view.beerlylover.LoverProfile;
import cite.ansteph.beerly.view.beerlylover.Preferences;
import cite.ansteph.beerly.view.beerlylover.discount.Discount;
import cite.ansteph.beerly.view.beerlylover.event.EventPage;

public class Affiliate extends AppCompatActivity implements DrawerAdapter.OnItemSelectedListener {

    private  static String TAG = Affiliate.class.getSimpleName();

    private String[] screenTitles;
    private Drawable[] screenIcons;
    private SlidingRootNav slidingRootNav;

    TextView txtAffiliateCode;

    TextView txtAffiliate;

    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_affiliate);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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

        DrawerAdapter adapter = new DrawerAdapter(Arrays.asList(
                createItemFor(MenuPosition.POS_HOME),
                createItemFor(MenuPosition.POS_EVENT),
                createItemFor(MenuPosition.POS_MYPROFILE),
                createItemFor(MenuPosition.POS_DISCOUNT),
                createItemFor(MenuPosition.POS_PREFERENCE),
                createItemFor(MenuPosition.POS_AFFILIATE).setChecked(true),
                new SpaceItem(48),
                createItemFor(MenuPosition.POS_LOGOUT)));
        adapter.setListener(this);



        RecyclerView list = (RecyclerView) findViewById(R.id.list);

        // RecyclerView list = findViewById(R.id.list);
        list.setNestedScrollingEnabled(false);
        list.setLayoutManager(new LinearLayoutManager(this));
        list.setAdapter(adapter);

        adapter.setSelected(MenuPosition.POS_AFFILIATE);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        txtAffiliateCode = (TextView) findViewById(R.id.txtinvitecode);



        txtAffiliate = (TextView) findViewById(R.id.txtHowdowork);

        if(txtAffiliate!=null)
        {
            txtAffiliate.setMovementMethod(LinkMovementMethod.getInstance());
        }


        //Checkif there is invitation <code>
        checkInvitation ();

    }






  public   void onCopyClicked(View view)
    {


        ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("code",txtAffiliateCode.getText().toString());
        clipboard.setPrimaryClip(clip);

        Snackbar.make(view, "Invitation code copied", Snackbar.LENGTH_LONG)
                .setAction(txtAffiliateCode.getText().toString(), null).show();
    }


    /**
     *
     * General sharing algorithm*/

    public void SendInvite (View view){


        try{
            String email =" ";
            String subject = "Beerly Beloved Invitation";
            String msg ="You have been invited to Beerly Beloved when registering please use the referral code: "
                    +txtAffiliateCode.getText().toString() +"\n Find it at https://play.google.com/store/apps/details?id=cite.ansteph.beerly"  ;

            final Intent sharingIntent = new Intent(Intent.ACTION_SEND);


            sharingIntent.setType("text/plain");
            //emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{email});
            sharingIntent.putExtra(Intent.EXTRA_SUBJECT, subject);


            sharingIntent.putExtra(Intent.EXTRA_TEXT, msg);
            startActivity(Intent.createChooser(sharingIntent, "Sending Invite..."));

          //  startActivity(Intent.createChooser(sharingIntent, getResources().getString(R.string.share_using)));

        }catch (Exception e){
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
            case MenuPosition.POS_AFFILIATE:;break;

            default:
                intent = new Intent(getApplicationContext(), Home.class);
        }

        if(intent!=null)
        {
            startActivity(intent);
        }

    }

    public void checkInvitation ()
    {
        if(sessionManager.getInviteCode()== null || TextUtils.isEmpty(sessionManager.getInviteCode())){
            try {
                getLoverProfileData(FirebaseAuth.getInstance().getCurrentUser().getUid());
            } catch (JSONException e) {
                e.printStackTrace();
            }catch (Exception e)
            {
                e.printStackTrace();
            }

        }else{
            String code = sessionManager.getInviteCode() ;

            txtAffiliateCode.setText(code.toLowerCase());
        }
    }


    public void onHowWorkClicked(View view)
    {
       startActivity(new Intent(getApplicationContext(), JoinAffiliate.class));

    }

    private void getLoverProfileData(String uid) throws JSONException
    {
        String url = String.format(Routes.URL_RETRIEVE_LOVER_PROFILE,uid);

        Log.e(TAG , uid);
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
                sessionManager.recordInvite(lovers.getInvitation_code().toLowerCase());
                txtAffiliateCode.setText(lovers.getInvitation_code().toLowerCase());

            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
        }




    }

}
