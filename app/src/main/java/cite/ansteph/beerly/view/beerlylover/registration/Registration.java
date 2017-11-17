package cite.ansteph.beerly.view.beerlylover.registration;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.Spinner;

import cite.ansteph.beerly.R;
import cite.ansteph.beerly.view.beerlylover.Home;

public class Registration extends AppCompatActivity {



    private AutoCompleteTextView mDateofBirth , mUsername, mRefCode;
    Spinner spnOrigin, spnCocktail, spnShot, spnCity;
    CheckBox chkTCs , chkCocktails, chkShots;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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


        mDateofBirth = (AutoCompleteTextView) findViewById(R.id.dateofbirth) ;
        mUsername = (AutoCompleteTextView) findViewById(R.id.txtusername) ;
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

        ArrayAdapter<CharSequence> originAdapter = ArrayAdapter.createFromResource(getApplicationContext(),R.array.gender, android.R.layout.simple_spinner_item);
        originAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


        ArrayAdapter<CharSequence> cocktailAdapter = ArrayAdapter.createFromResource(getApplicationContext(),R.array.typeCocktail, android.R.layout.simple_spinner_item);
        originAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


        ArrayAdapter<CharSequence> shotAdapter = ArrayAdapter.createFromResource(getApplicationContext(),R.array.typeCocktail, android.R.layout.simple_spinner_item);
        originAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


        ArrayAdapter<CharSequence> cityAdapter = ArrayAdapter.createFromResource(getApplicationContext(),R.array.cities, android.R.layout.simple_spinner_item);
        originAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);



        spnOrigin.setAdapter(originAdapter);
        spnCocktail.setAdapter(cocktailAdapter);
        spnShot.setAdapter(shotAdapter);
        spnCity.setAdapter(cityAdapter);


    }


    public void OnCompleteRegClicked(View view)
    {
        if(!isFormCancelled())
        startActivity(new Intent(getApplicationContext(), Home.class));
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
        if(TextUtils.isEmpty(dob)){
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

}
