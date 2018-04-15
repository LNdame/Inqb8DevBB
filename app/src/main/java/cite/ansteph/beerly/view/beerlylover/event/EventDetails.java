package cite.ansteph.beerly.view.beerlylover.event;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
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

import cite.ansteph.beerly.R;
import cite.ansteph.beerly.api.Routes;
import cite.ansteph.beerly.model.BeerlyEvent;
import cite.ansteph.beerly.model.Establishment;
import cite.ansteph.beerly.view.beerlylover.EstMenu;

public class EventDetails extends AppCompatActivity {

    BeerlyEvent mCurrentEvent;
    Menu mMenu;

    KenBurnsView mProfilePic;

    private static final int ID_STANDARD_DIALOG = R.id.fab;
    private LovelySaveStateHandler saveStateHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showLovelyDialog(view.getId(), null);

               /* Snackbar.make(view, "This will take you out of the app and unto the Maps", Snackbar.LENGTH_LONG)
                        .setAction("Continue ?", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                getDirections(mCurrentEvent.getLatitude(), mCurrentEvent.getLongitude());
                            }
                        }).show();*/
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        saveStateHandler = new LovelySaveStateHandler();

        Bundle bundle = getIntent().getExtras();
        if(bundle!=null){
            mCurrentEvent = (BeerlyEvent) bundle.getSerializable("profile");
            setTitle(mCurrentEvent.getName());
            setupUI(mCurrentEvent);
        }else{
            Toast.makeText(getApplicationContext(), "Oups! No profile found for this establisment!", Toast.LENGTH_LONG).show();
            this.finish();
        }



    }

    void setupUI(BeerlyEvent event)
    {
        TextView txtname = (TextView) findViewById(R.id.txtestName);
        TextView txtAddress = (TextView) findViewById(R.id.txtaddress);
        TextView txtDesc = (TextView) findViewById(R.id.txtdescription);

        TextView txtStartDate = (TextView) findViewById(R.id.txtstardate);
        TextView txtEndDate = (TextView) findViewById(R.id.txtenddate);

        mProfilePic = (KenBurnsView) findViewById(R.id.mainPicture);

        String imgUrl= Routes.DOMAIN+event.getMain_picture_url();

        Glide.with(getApplicationContext()).load(imgUrl).
                thumbnail(0.5f)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(mProfilePic);

        txtname.setText(event.getName());
        txtAddress.setText(event.getAddress());
        txtDesc.setText(event.getDescription());

        txtStartDate.setText(event.getStartDate());
        txtEndDate.setText(event.getEndDate());

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
        getMenuInflater().inflate(R.menu.event_profile_menu, menu);

        this.mMenu = menu;
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        //
        MenuItem promoItem = menu.findItem(R.id.action_promotion);
        promoItem.setTitle("PROMO (" +mCurrentEvent.getPromoNumber()+")");
        return true;// super.onPrepareOptionsMenu(menu);
    }

    private void updatePromoMenuItem()
    {
        MenuItem promoItem = mMenu.findItem(R.id.action_promotion);
        promoItem.setTitle("PROMO (" +mCurrentEvent.getPromoNumber()+")");

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if(id==R.id.action_promotion){
            Intent i = new Intent( getApplicationContext(), EstMenu.class);
            i.putExtra("Establishment", mCurrentEvent );
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
                        getDirections(mCurrentEvent.getLatitude(), mCurrentEvent.getLongitude());
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
