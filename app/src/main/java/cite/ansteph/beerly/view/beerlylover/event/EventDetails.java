package cite.ansteph.beerly.view.beerlylover.event;

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

import cite.ansteph.beerly.R;
import cite.ansteph.beerly.api.Routes;
import cite.ansteph.beerly.model.BeerlyEvent;
import cite.ansteph.beerly.model.Establishment;
import cite.ansteph.beerly.view.beerlylover.EstMenu;

public class EventDetails extends AppCompatActivity {

    BeerlyEvent mCurrentEvent;
    Menu mMenu;

    KenBurnsView mProfilePic;
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
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


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
        TextView txtSpeciality = (TextView) findViewById(R.id.txtspeciality);

        mProfilePic = (KenBurnsView) findViewById(R.id.mainPicture);

        String imgUrl= Routes.DOMAIN+event.getMain_picture_url();

        Glide.with(getApplicationContext()).load(imgUrl).
                thumbnail(0.5f)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(mProfilePic);

        txtname.setText(event.getName());
        txtAddress.setText(event.getAddress());
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

}
