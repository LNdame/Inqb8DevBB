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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.flaviofaria.kenburnsview.KenBurnsView;
import com.yarolegovich.lovelydialog.LovelyDialogCompat;
import com.yarolegovich.lovelydialog.LovelySaveStateHandler;
import com.yarolegovich.lovelydialog.LovelyStandardDialog;
import com.yarolegovich.slidingrootnav.SlidingRootNav;
import com.yarolegovich.slidingrootnav.SlidingRootNavBuilder;

import java.util.Arrays;

import cite.ansteph.beerly.R;
import cite.ansteph.beerly.api.Routes;
import cite.ansteph.beerly.model.Establishment;
import cite.ansteph.beerly.slidingmenu.DrawerAdapter;
import cite.ansteph.beerly.slidingmenu.DrawerItem;
import cite.ansteph.beerly.slidingmenu.MenuPosition;
import cite.ansteph.beerly.slidingmenu.SimpleItem;
import cite.ansteph.beerly.slidingmenu.SpaceItem;
import cite.ansteph.beerly.view.beerlylover.discount.Discount;
import cite.ansteph.beerly.view.beerlylover.event.EventPage;
import cite.ansteph.beerly.view.beerlylover.registration.Registration;

public class Profile extends AppCompatActivity implements DrawerAdapter.OnItemSelectedListener  {

    private String[] screenTitles;
    private Drawable[] screenIcons;
    private SlidingRootNav slidingRootNav;

    Establishment mCurrentEstabliment;

    Menu mMenu;

    KenBurnsView mProfilePic;

    private static final int ID_STANDARD_DIALOG = R.id.fab;
    private LovelySaveStateHandler saveStateHandler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        Bundle bundle = getIntent().getExtras();
        if(bundle!=null){
            mCurrentEstabliment = (Establishment)bundle.getSerializable("profile");
            setTitle(mCurrentEstabliment.getName());
            setupUI(mCurrentEstabliment);
        }else{
            Toast.makeText(getApplicationContext(), "Oups! No profile found for this establisment!", Toast.LENGTH_LONG).show();
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

       // adapter.setSelected(MenuPosition.POS_DISCOUNT);

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
    }



    void setupUI(Establishment est)
    {
        TextView txtname = (TextView) findViewById(R.id.txtestName);
        TextView txtAddress = (TextView) findViewById(R.id.txtaddress);
        TextView txtSpeciality = (TextView) findViewById(R.id.txtspeciality);

        mProfilePic = (KenBurnsView) findViewById(R.id.mainPicture);

        String imgUrl= Routes.DOMAIN+est.getMain_picture_url();

        Glide.with(getApplicationContext()).load(imgUrl).
                thumbnail(0.5f)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(mProfilePic);

        txtname.setText(est.getName());
        txtAddress.setText(est.getAddress());
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
            case MenuPosition.POS_HOME:intent = new Intent(getApplicationContext(), Home.class);break;
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


    void getDirections(String latitude, String longitude  )
    {
        String address  = "http://maps.google.com/maps?daddr=" +latitude+" , "+longitude;

        Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                Uri.parse(address));
        startActivity(intent);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.profile_menu, menu);

        this.mMenu = menu;
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
       //
        MenuItem promoItem = menu.findItem(R.id.action_promotion);
        promoItem.setTitle("Special (" +mCurrentEstabliment.getPromoNumber()+")");
        return true;// super.onPrepareOptionsMenu(menu);
    }

    private void updatePromoMenuItem()
    {
        MenuItem promoItem = mMenu.findItem(R.id.action_promotion);
        promoItem.setTitle("Special (" +mCurrentEstabliment.getPromoNumber()+")");

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if(id==R.id.action_promotion){
            Intent i = new Intent( getApplicationContext(), EstMenu.class);
            i.putExtra("Establishment", mCurrentEstabliment );
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
