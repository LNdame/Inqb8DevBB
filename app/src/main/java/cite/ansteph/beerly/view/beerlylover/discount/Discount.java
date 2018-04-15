package cite.ansteph.beerly.view.beerlylover.discount;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.Result;

import java.util.ArrayList;

import cite.ansteph.beerly.R;
import cite.ansteph.beerly.adapter.DiscountRecyclerAdapter;
import cite.ansteph.beerly.model.DiscountM;
import cite.ansteph.beerly.model.Promotion;
import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class Discount extends AppCompatActivity implements ZXingScannerView.ResultHandler {

    private static String TAG = Discount.class.getSimpleName();

    TextView textView;

    private ZXingScannerView mScannerView;
    final private int REQUEST_CODE_ASK_PERMISSIONS = 123;


    RecyclerView recyclerView;

    ArrayList<DiscountM> mDiscountsList ;

    DiscountRecyclerAdapter mDiscountAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discount);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mScannerView = new ZXingScannerView(this);
        requestPermission();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

       // textView = (TextView) findViewById(R.id.txtDiscount);

        //textView.setText("Waiting for result");





        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
        mDiscountsList = new ArrayList<>();


        mDiscountAdapter = new DiscountRecyclerAdapter( mDiscountsList, this);

        recyclerView.setAdapter(mDiscountAdapter);

        setupList();
    }




    void setupList()
    {
        mDiscountsList.add(new DiscountM("BeerShack", "12:00:00"));
        mDiscountsList.add(new DiscountM("WhiteTiger", "12:00:00"));

        mDiscountAdapter.notifyDataSetChanged();
    }






    private void requestPermission()
    {
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE},REQUEST_CODE_ASK_PERMISSIONS);
        }
    }


    /**
     * Listener for response to user permission request
     *
     * @param requestCode  Check that permission request code matches
     * @param permissions  Permissions that requested
     * @param grantResults Whether permissions granted
     */

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode)
        {
            case REQUEST_CODE_ASK_PERMISSIONS:
                if(grantResults[0]== PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT)
                            .show();
                }else{
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT)
                            .show();
                }break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }



       /* if(grantResults[0]== PackageManager.PERMISSION_GRANTED){
            Log.i(TAG, "Permission " +permissions[0]+ " was " +grantResults[0]);
        }*/
    }


    @Override
    public void handleResult(Result result) {

    }
}
