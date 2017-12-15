package cite.ansteph.beerly.view.beerlylover.discount;

import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import cite.ansteph.beerly.R;
import cite.ansteph.beerly.api.Routes;
import cite.ansteph.beerly.api.columns.DiscountColumns;
import cite.ansteph.beerly.api.columns.PreferenceColumns;
import cite.ansteph.beerly.api.columns.PromotionColumns;
import cite.ansteph.beerly.model.Beer;
import cite.ansteph.beerly.model.Promotion;
import cite.ansteph.beerly.view.beerlylover.Home;

public class DiscountResult extends AppCompatActivity {

    TextView txtCountDown;
    TextView txtDiscountMsg;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discount_result);


        String id = getIntent().getStringExtra("code");

       // TextView tt = (TextView) findViewById(R.id.textView3);

        txtDiscountMsg = (TextView) findViewById(R.id.txtdiscountmsg);

      //  tt.setText(id);

        txtCountDown = (TextView) findViewById(R.id.txtCountdown);

        // close the activity in case of empty barcode
        if (TextUtils.isEmpty(id)) {
            Toast.makeText(getApplicationContext(), "Barcode is empty!", Toast.LENGTH_LONG).show();
            finish();
        }else{

            int estid = Integer.parseInt(id);

            try {
                getPromoData(estid);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        startCountDown();






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



    void loadPromo(JSONArray profilejsonArray){

        // txtDisplayname= (TextView) findViewById(R.id.txtDisplayname);
        //   txtDateCreated= (TextView) findViewById(R.id.txtDateCreated);
        //  txtPrefUpdate = (TextView) findViewById(R.id.txtPrefUpdate);

       // mPromotionsList.clear();

        String estid ="";

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

                // JSONObject beerJson = profjson.getJSONObject("beer");establishment_name
                txtDiscountMsg.setText(getString(R.string.discount_on ,profjson.getString("establishment_name")) );
                Beer be = new Beer();
                be.setId(promo.getBeer_id());
                be.setName( profjson.getString("establishment_name"));
                promo.setBeer(be);

              //  mPromotionsList.add(promo);

                //    txtDisplayname .setText(lovers.getFirst_name() +" "+lovers.getLast_name());
                //    txtDateCreated.setText(lovers.getCreated_at());

                estid = String.valueOf(promo.getEstablishment_id()) ;

            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
        }


        try {
            storeDiscount(estid);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        // mPromoAdapter.notifyDataSetChanged();

       /* try {
            getBeers(mPromotionsList);
        } catch (JSONException e) {
            e.printStackTrace();
        }*/
    }



    public void startCountDown()
    {
        new CountDownTimer(43200000, 1000) { // adjust the milli seconds here

            public void onTick(long millisUntilFinished) {
                txtCountDown.setText(""+String.format("%d h, %d min, %d sec",
                        TimeUnit.MILLISECONDS.toHours(millisUntilFinished),
                        TimeUnit.MILLISECONDS.toMinutes( millisUntilFinished) -TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millisUntilFinished)),
                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))));
            }

            public void onFinish() {
                txtCountDown.setText("done!");
            }
        }.start();
    }





    public void storeDiscount(final String estId) throws JSONException {

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

        // if(mTempPreferenceList!=null && )

        final String fireID  = FirebaseAuth.getInstance().getCurrentUser().getUid();

        String url = String.format(Routes.URL_STORE_DISCOUNT);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //  loading.dismiss();

                try{
                    //creating the Json object from the response
                    JSONArray jsonResponse = new JSONArray(response);
                    //  sessionManager.recordRegistration(mFirebaseUID);


                   // startActivity(new Intent(getApplicationContext(), Home.class));


                }catch (JSONException e)
                {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //   loading.dismiss();
                Toast.makeText(getApplicationContext(), error.getMessage(),Toast.LENGTH_LONG).show();
            }
        }

        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                params.put(DiscountColumns.FIREBASE_ID, fireID );
                params.put(DiscountColumns.ESTABLISMENT_EST_ID,estId);
              //  params.put(PreferenceColumns.PREFERENCE3, String.valueOf(mTempPreferenceList.get(2).getBeer_id()));


                return params;
            }
        };


        //  GlobalRetainer.getInstance().addToRequestQueue();

        requestQueue.add(stringRequest);


    }









}
/*int seconds = (int) (milliseconds / 1000) % 60 ;
int minutes = (int) ((milliseconds / (1000*60)) % 60);
int hours   = (int) ((milliseconds / (1000*60*60)) % 24);*/