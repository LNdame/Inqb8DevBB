package cite.ansteph.beerly.view.beerlylover;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.Spinner;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import cite.ansteph.beerly.R;
import cite.ansteph.beerly.api.Routes;
import cite.ansteph.beerly.api.columns.BeerLoversColumns;
import cite.ansteph.beerly.api.columns.UserColumns;
import cite.ansteph.beerly.helper.SessionManager;
import cite.ansteph.beerly.model.BeerLovers;
import cite.ansteph.beerly.utils.DateTimeUtils;

public class EditLoverProfile extends AppCompatActivity {


    Spinner spnGender, spnCocktail, spnShot, spnCity;
    CheckBox chkTCs , chkCocktails, chkShots;

    TextView txtName, txtUserName, txtBirthdate;
    SessionManager sessionManager;

    String mFirst_name, mSurname, mEmail, mRefCode, mFirebaseUID;

    BeerLovers EditedLovers ;

    ArrayAdapter<CharSequence> cityAdapter;
    ArrayAdapter<CharSequence> shotAdapter;
    ArrayAdapter<CharSequence> cocktailAdapter;
    ArrayAdapter<CharSequence> originAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_lover_profile);
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
        if (bundle !=null){
            EditedLovers = (BeerLovers) bundle.getSerializable("lover");
        }else
        {
            Toast.makeText(getApplicationContext(), "Error finding the profile, Try again later", Toast.LENGTH_LONG).show();
            this.finish();
        }


        txtName = (TextView) findViewById(R.id.txtfirst_name) ;
        txtUserName = (TextView) findViewById(R.id.txtEdtUsername) ;
        txtBirthdate = (TextView) findViewById(R.id.txtEdtBirthDate) ;

       // mDateofBirth = (AutoCompleteTextView) findViewById(R.id.dateofbirth) ;
       // mUsername = (AutoCompleteTextView) findViewById(R.id.txtusername) ;
      //  mRefCode = (AutoCompleteTextView) findViewById(R.id.txtrefcode) ;

     //   chkTCs = (CheckBox) findViewById(R.id.chkterms) ;
        chkCocktails = (CheckBox) findViewById(R.id.chkCocktail) ;
        chkShots = (CheckBox) findViewById(R.id.chkShot) ;

        spnGender  =(Spinner)findViewById(R.id.spnedtGender);

        spnCocktail=(Spinner)findViewById(R.id.spnCocktails);
        spnShot=(Spinner)findViewById(R.id.spnShot);
        spnCity=(Spinner)findViewById(R.id.spnCity);


        originAdapter = ArrayAdapter.createFromResource(this,R.array.gender, android.R.layout.simple_spinner_item);
        originAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


        cocktailAdapter = ArrayAdapter.createFromResource(this,R.array.typeCocktail, android.R.layout.simple_spinner_dropdown_item);
        originAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


        shotAdapter = ArrayAdapter.createFromResource(this,R.array.typeCocktail, android.R.layout.simple_spinner_dropdown_item);
        originAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


        cityAdapter = ArrayAdapter.createFromResource(this,R.array.cities, android.R.layout.simple_spinner_dropdown_item);
        originAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


        spnGender.setAdapter(originAdapter);
        spnCocktail.setAdapter(cocktailAdapter);
        spnShot.setAdapter(shotAdapter);
        spnCity.setAdapter(cityAdapter);

        setupUI(EditedLovers);

    }




//format the date to what you want





    void setupUI (BeerLovers beerLovers)
    {

        String bod = DateTimeUtils.datetoStringShort(beerLovers.getDate_of_birth());


        txtBirthdate.setText(bod);
        txtName.setText(beerLovers.getFirst_name()+" "+beerLovers.getLast_name());
        txtUserName.setText(beerLovers.getUsername());

        mRefCode = beerLovers.getReferralCode();
        mFirst_name = beerLovers.getFirst_name();
        mSurname= beerLovers.getLast_name();
        mEmail = beerLovers.getEmail();
        mFirebaseUID =beerLovers.getFirebase_id();


       // spnCity.set

        if(!beerLovers.getHome_city().equals(null))
        {
            int pos = cityAdapter.getPosition(beerLovers.getHome_city());
            spnCity.setSelection(pos);
        }



        if(!beerLovers.getGender().equals(null))
        {
            int pos = originAdapter.getPosition(beerLovers.getGender());
            spnGender.setSelection(pos);
        }

        if(!beerLovers.getShotType().equals(null) && beerLovers.getShot() ==1)
        {
            chkShots.setChecked(true);
            int pos = shotAdapter.getPosition(beerLovers.getShotType());
            spnShot.setSelection(pos);
            spnShot.setVisibility(View.VISIBLE);
        }


        if(!beerLovers.getCocktailType().equals(null)&& beerLovers.getCocktail()==1)
        {
            chkCocktails.setChecked(true);

            int pos = cocktailAdapter.getPosition(beerLovers.getCocktailType());
            spnCocktail.setSelection(pos);
            spnCocktail.setVisibility(View.VISIBLE);
        }



    }


    public void toggleCocktailSpinner(View view)
    {
        if(chkCocktails.isChecked())
        {
            spnCocktail.setVisibility(View.VISIBLE);
        }else{
            spnCocktail.setVisibility(View.GONE);
        }
    }


    public void toggleShotSpinner(View view)
    {
        if(chkShots.isChecked())
        {
            spnShot.setVisibility(View.VISIBLE);
        }else{
            spnShot.setVisibility(View.GONE);
        }}


    public void onDoneClicked(View view)
    {
        try {
            UpdateBeerLover();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public void UpdateBeerLover() throws JSONException {
        // final ProgressDialog loading = ProgressDialog.show(getApplicationContext(), "Registering", "Please wait... you will soon be part of the class",false,false);
//Getting user data
        final String username = txtUserName.getText().toString().trim();
        final String gender = spnGender.getSelectedItem().toString();
        final String dob =txtBirthdate.getText().toString().trim();
        final  String  home_city = spnCity.getSelectedItem().toString();
        final  String  referral_code = (mRefCode!=null)? mRefCode:"" ;

        final String cocktail  = (chkCocktails.isChecked()) ? "1":"0";
        final String cocktailtype = (spnCocktail.getVisibility() == View.VISIBLE)?spnCocktail.getSelectedItem().toString():"none";
        final String shottype = (spnShot.getVisibility() == View.VISIBLE)?spnShot.getSelectedItem().toString():"none";
        final String shot  = (chkShots.isChecked()) ? "1":"0";
        // final  String passwd = edtPwd.getText().toString().trim();
        //final String origin = spnOrigin.getSelectedItem().toString();

        String url = String.format(Routes.URL_UPDATE_LOVER_PROFILE, mFirebaseUID);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //  loading.dismiss();

                try{
                    //creating the Json object from the response
                    JSONObject jsonResponse = new JSONObject(response);

                    startActivity(new Intent(getApplicationContext(), LoverProfile.class));
                  /*  boolean error = jsonResponse.getBoolean(Routes.ERROR_RESPONSE);
                    String serverMsg = jsonResponse.getString(Routes.MSG_RESPONSE);
                    //if it is success
                    if(!error)
                    {
                        //asking user to confirm OTP
                       // confirmOtp();

                        sessionManager.recordRegistration();

                    }else{ //check for message already existing user
                        Toast.makeText(getApplicationContext(), serverMsg, Toast.LENGTH_LONG).show();

                    }*/


                }catch (JSONException e)
                {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //   loading.dismiss();
                Toast.makeText(getApplicationContext(), error.getMessage()+"Oups! Could not talk to the server at this time, try again later",Toast.LENGTH_LONG).show();
            }
        }

        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();


                params.put(BeerLoversColumns.GENDER, gender);
                params.put(BeerLoversColumns.HOME_CITY, home_city);

                params.put(BeerLoversColumns.COCKTAIL, cocktail);
                params.put(BeerLoversColumns.COCKTAIL_TYPE, cocktailtype);
                params.put(BeerLoversColumns.SHOT, shot);
                params.put(BeerLoversColumns.SHOT_TYPE, shottype);


                return params;
            }
        };


        //  GlobalRetainer.getInstance().addToRequestQueue();
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);

    }

}
