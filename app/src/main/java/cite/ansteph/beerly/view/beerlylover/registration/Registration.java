package cite.ansteph.beerly.view.beerlylover.registration;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.Spinner;

import cite.ansteph.beerly.R;

public class Registration extends AppCompatActivity {


    private AutoCompleteTextView mDateofBirth;
    Spinner spnOrigin; CheckBox chkTCs;
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

        mDateofBirth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment nf = new DatePickerFragment();
                nf.show(getSupportFragmentManager(), "Date Picker");
            }
        });

        spnOrigin  =(Spinner)findViewById(R.id.spnSource);

        chkTCs = (CheckBox) findViewById(R.id.chkterms) ;

        ArrayAdapter<CharSequence> originAdapter = ArrayAdapter.createFromResource(getApplicationContext(),R.array.gender, android.R.layout.simple_spinner_item);
        originAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spnOrigin.setAdapter(originAdapter);

    }

}
