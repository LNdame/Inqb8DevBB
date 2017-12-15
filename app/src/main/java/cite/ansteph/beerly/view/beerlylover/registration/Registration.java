package cite.ansteph.beerly.view.beerlylover.registration;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.EditText;
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
import com.google.android.gms.vision.text.Text;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import cite.ansteph.beerly.R;
import cite.ansteph.beerly.api.Routes;
import cite.ansteph.beerly.api.columns.BeerLoversColumns;
import cite.ansteph.beerly.api.columns.UserColumns;
import cite.ansteph.beerly.app.GlobalRetainer;
import cite.ansteph.beerly.helper.SessionManager;
import cite.ansteph.beerly.model.BeerLovers;
import cite.ansteph.beerly.model.Preference;
import cite.ansteph.beerly.utils.RandomStringUtils;
import cite.ansteph.beerly.view.beerlylover.Home;
import cite.ansteph.beerly.view.beerlylover.Preferences;

public class Registration extends AppCompatActivity {



    private AutoCompleteTextView mDateofBirth , mRefCode;
    EditText mUsername;
    Spinner spnOrigin, spnCocktail, spnShot, spnCity;
    CheckBox chkTCs , chkCocktails, chkShots;

    TextView txtFirstName, txtLastName, txtEmail;
    SessionManager sessionManager;

    String mFirst_name, mSurname, mEmail, mFirebaseUID;
    ArrayAdapter<CharSequence> cityAdapter;
    ArrayAdapter<CharSequence> shotAdapter;
    ArrayAdapter<CharSequence> cocktailAdapter;
    ArrayAdapter<CharSequence> originAdapter;


    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    FirebaseUser mUser;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

      /*    mAuth = FirebaseAuth.getInstance();

        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                // FirebaseUser user = firebaseAuth.getCurrentUser();
                mUser = firebaseAuth.getCurrentUser();

                if(mUser!=null)
                {
                    //User is signed in
                    // Log.d(TAG, "onAuthStateChanged:signed_in" + user.getUid());
                }else{
                    // Log.d(TAG, "onAuthStateChanged:signed_out");

                    startActivity(new Intent(getApplicationContext(), Login.class));

                }

            }
        };








        //

      String recordedFbId = sessionManager.getFirebaseID();

        if(recordedFbId!=null && !TextUtils.isEmpty(recordedFbId))
        {
            if(recordedFbId.equals(FirebaseAuth.getInstance().getCurrentUser().getUid())){
               sessionManager.checkRegPart2();
            }
        }*/

        sessionManager = new SessionManager(getApplicationContext());

       if(sessionManager.getFirebaseID()!=null && !TextUtils.isEmpty(sessionManager.getFirebaseID())){
           String recordedFbId = sessionManager.getFirebaseID();

           sessionManager.checkRegPart2();
       }



        setContentView(R.layout.activity_registration);

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


        txtEmail = (TextView) findViewById(R.id.txtemail) ;
        txtFirstName = (TextView) findViewById(R.id.txtfirst_name) ;
        txtLastName = (TextView) findViewById(R.id.txtlast_name) ;

        mDateofBirth = (AutoCompleteTextView) findViewById(R.id.dateofbirth) ;
        mUsername = (EditText) findViewById(R.id.txtusername) ;
        mRefCode = (AutoCompleteTextView) findViewById(R.id.txtrefcode) ;

        mDateofBirth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment nf = new DatePickerFragment();
                nf.show(getSupportFragmentManager(), "Date Picker");
            }
        });

        spnOrigin  =(Spinner)findViewById(R.id.spnSource);

        spnCocktail=(Spinner)findViewById(R.id.spnCocktails);
        spnShot=(Spinner)findViewById(R.id.spnShot);
        spnCity=(Spinner)findViewById(R.id.spnCity);

        chkTCs = (CheckBox) findViewById(R.id.chkterms) ;
        chkCocktails = (CheckBox) findViewById(R.id.chkCocktail) ;
        chkShots = (CheckBox) findViewById(R.id.chkShot) ;

        originAdapter = ArrayAdapter.createFromResource(this,R.array.gender, android.R.layout.simple_spinner_item);
        originAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


        cocktailAdapter = ArrayAdapter.createFromResource(this,R.array.typeCocktail, android.R.layout.simple_spinner_dropdown_item);
        originAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


        shotAdapter = ArrayAdapter.createFromResource(this,R.array.typeCocktail, android.R.layout.simple_spinner_dropdown_item);
        originAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


         cityAdapter = ArrayAdapter.createFromResource(this,R.array.cities, android.R.layout.simple_spinner_dropdown_item);
        originAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);



        spnOrigin.setAdapter(originAdapter);
        spnCocktail.setAdapter(cocktailAdapter);
        spnShot.setAdapter(shotAdapter);
        spnCity.setAdapter(cityAdapter);

        retrieveFirebaseDetails();

        try {
            getLoverProfileData();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    public void OnCompleteRegClicked(View view)
    {
        if(!isFormCancelled()) {
            try {
                registerCandidate();
            } catch (JSONException e) {
                e.printStackTrace();
            }
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


    public void registerCandidate() throws JSONException {
      // final ProgressDialog loading = ProgressDialog.show(getApplicationContext(), "Registering", "Please wait... you will soon be part of the class",false,false);
//Getting user data
        final String username = mUsername.getText().toString().trim();
        final String gender = spnOrigin.getSelectedItem().toString();
        final String dob = mDateofBirth.getText().toString().trim();
        final  String  home_city = spnCity.getSelectedItem().toString();
        final  String  referral_code = mRefCode.getText().toString().trim();

        final String cocktail  = (chkCocktails.isChecked()) ? "1":"0";
        final String cocktailtype = (spnCocktail.getVisibility() == View.VISIBLE)?spnCocktail.getSelectedItem().toString():"none";
        final String shottype = (spnShot.getVisibility() == View.VISIBLE)?spnShot.getSelectedItem().toString():"none";
        final String shot  = (chkShots.isChecked()) ? "1":"0";

        final String code = createInvitationCode(username);
       // final  String passwd = edtPwd.getText().toString().trim();
        //final String origin = spnOrigin.getSelectedItem().toString();

        String url = String.format(Routes.URL_REGISTER_BEERLOVERS);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
              //  loading.dismiss();

                try{
                    //creating the Json object from the response
                    JSONObject jsonResponse = new JSONObject(response);
                    sessionManager.recordRegistration(mFirebaseUID);

                    sessionManager.recordInvite(code);
                    startActivity(new Intent(getApplicationContext(), Preferences.class));

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
                Toast.makeText(getApplicationContext(), error.getMessage(),Toast.LENGTH_LONG).show();
            }
        }

        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                params.put(UserColumns.FIRST_NAME, mFirst_name);
                params.put(UserColumns.LAST_NAME, mSurname);
                params.put(UserColumns.EMAIL, mEmail);
               params.put(UserColumns.USERNAME, username);
                params.put(BeerLoversColumns.STATUS, "active");
                params.put(BeerLoversColumns.TERMS_CONDITIONS_ACCEPT, "1");
                params.put(BeerLoversColumns.GENDER, gender);
                params.put(BeerLoversColumns.HOME_CITY, home_city);
                params.put(BeerLoversColumns.REFERRAL_CODE, referral_code);
                params.put(BeerLoversColumns.FIREBASE_ID, mFirebaseUID);
                params.put(BeerLoversColumns.COCKTAIL, cocktail);
                params.put(BeerLoversColumns.COCKTAIL_TYPE, cocktailtype);
                params.put(BeerLoversColumns.SHOT, shot);
                params.put(BeerLoversColumns.SHOT_TYPE, shottype);
                params.put(BeerLoversColumns.DATE_OF_BIRTH, dob);
                params.put(BeerLoversColumns.INVITATION_CODE, code);

                return params;
            }
        };


      //  GlobalRetainer.getInstance().addToRequestQueue();
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);

    }


    private void getLoverProfileData() throws JSONException
    {
        String url = String.format(Routes.URL_RETRIEVE_LOVER_PROFILE,mFirebaseUID);

       // Log.e(TAG , mUser.getUid());
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                profileFound(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });


        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

        requestQueue.add(jsonArrayRequest);
    }

     void profileFound(JSONArray profilejsonArray)
     {
         if (profilejsonArray.length()>0)
         {
             sessionManager.recordRegistration(mFirebaseUID);
             sessionManager.checkRegPart2();
         }
     }


    public void retrieveFirebaseDetails()
    {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user!=null){
            String [] nameSplit = user.getDisplayName().split(" ");

            mFirst_name = nameSplit[0];
            mSurname = nameSplit[1];
            mEmail = user.getEmail();
            mFirebaseUID = user.getUid();

            //to be removed
            Toast.makeText(Registration.this, mFirebaseUID, Toast.LENGTH_SHORT).show();

            txtEmail.setText(TextUtils.isEmpty(user.getEmail()) ? "No email" : user.getEmail());
            txtFirstName.setText (getString(R.string.account_reg_welcome ,nameSplit[0]));
            txtLastName.setText(nameSplit[1]);
        }else{
            startActivity(new Intent(getApplicationContext(), Login.class));
        }



       // mUsername.setText( TextUtils.isEmpty(user.getUid()) ? "No email" : user.getUid());
    }




    public boolean isFormCancelled()
    {
        //Reset the errors


        mDateofBirth.setError(null);
        mUsername.setError(null);
        mRefCode.setError(null);

        //flash storing
        String dob = mDateofBirth.getText().toString();
        String username = mUsername.getText().toString();
        String refcode = mRefCode.getText().toString();



        boolean cancel = false;
        View focusView = null;

        //Check
        // Check for a valid password, if the user entered one.
        if(TextUtils.isEmpty(dob)|| dob.equals("Must be 18 years old or older")){
            mDateofBirth.setError(getString(R.string.error_field_required));
            focusView = mDateofBirth;
            cancel = true;
        }



        // Check for a valid email address.
        if (TextUtils.isEmpty(username)) {
            mUsername.setError(getString(R.string.error_field_required));
            focusView = mUsername;
            cancel = true;
        }

       if (!chkTCs.isChecked()){
           focusView = chkTCs;
           cancel = true;

           Snackbar.make(focusView, "You must accept the terms and conditions to continue", Snackbar.LENGTH_LONG)
                   .setAction("Action", null).show();
       }


        if(cancel)
        {
            focusView.requestFocus();
        }

        return cancel;
    }



    public String createInvitationCode(String username)
    {
        RandomStringUtils randomStringUtils = new RandomStringUtils(5);

        String code = username+ "-bb-"+randomStringUtils.nextString();
        return code;
    }




    @Override
    protected void onStart() {
        super.onStart();
      //  mAuth.addAuthStateListener(mAuthStateListener);

    }

    @Override
    protected void onStop() {
        super.onStop();
        /*if(mAuthStateListener!=null)
            mAuth.removeAuthStateListener(mAuthStateListener);*/

    }

    @Override
    public void onPause() {
        super.onPause();

       /* if(mAuthStateListener!=null)
            mAuth.removeAuthStateListener(mAuthStateListener);*/

    }

    @Override
    public void onResume() {
        super.onResume();

       /* mAuth.addAuthStateListener(mAuthStateListener);*/

    }
}
