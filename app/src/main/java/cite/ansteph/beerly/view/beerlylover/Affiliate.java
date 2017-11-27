package cite.ansteph.beerly.view.beerlylover;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
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
import android.widget.TextView;

import com.yarolegovich.slidingrootnav.SlidingRootNav;
import com.yarolegovich.slidingrootnav.SlidingRootNavBuilder;

import java.io.File;
import java.util.Arrays;

import cite.ansteph.beerly.R;
import cite.ansteph.beerly.slidingmenu.DrawerAdapter;
import cite.ansteph.beerly.slidingmenu.DrawerItem;
import cite.ansteph.beerly.slidingmenu.MenuPosition;
import cite.ansteph.beerly.slidingmenu.SimpleItem;
import cite.ansteph.beerly.slidingmenu.SpaceItem;
import cite.ansteph.beerly.utils.RandomStringUtils;
import cite.ansteph.beerly.view.beerlylover.discount.Discount;
import cite.ansteph.beerly.view.beerlylover.registration.Registration;

public class Affiliate extends AppCompatActivity implements DrawerAdapter.OnItemSelectedListener {
    private String[] screenTitles;
    private Drawable[] screenIcons;
    private SlidingRootNav slidingRootNav;

    TextView txtAffiliateCode;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_affiliate);
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

        DrawerAdapter adapter = new DrawerAdapter(Arrays.asList(
                createItemFor(MenuPosition.POS_HOME),
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

        createCode();



    }



    void createCode()
    {
        RandomStringUtils randomStringUtils = new RandomStringUtils(5);

        String code = "Loic-beerly-"+randomStringUtils.nextString();
        txtAffiliateCode.setText(code.toLowerCase());
    }



  public   void onCopyClicked(View view)
    {
        ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("code",txtAffiliateCode.getText().toString());
        clipboard.setPrimaryClip(clip);

        Snackbar.make(view, "Invitation code copied", Snackbar.LENGTH_LONG)
                .setAction(txtAffiliateCode.getText().toString(), null).show();
    }


    public void SendInvite (){


        try{
            String email =" ";
            String subject = "Beerly Beloved Invite";
            String msg ="You have been invited to Beerly Beloved when registering please use the referral code: "
                    +txtAffiliateCode.getText().toString();





            final Intent emailIntent = new Intent(Intent.ACTION_SEND);
            emailIntent.setType("plain/text");
            emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{email});
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);


            emailIntent.putExtra(Intent.EXTRA_TEXT, msg);
            startActivity(Intent.createChooser(emailIntent, "Sending Invite..."));
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

}
