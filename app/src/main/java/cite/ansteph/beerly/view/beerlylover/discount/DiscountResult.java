package cite.ansteph.beerly.view.beerlylover.discount;

import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.TextView;
import android.widget.Toast;

import java.util.concurrent.TimeUnit;

import cite.ansteph.beerly.R;

public class DiscountResult extends AppCompatActivity {

    TextView txtCountDown;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discount_result);


        String barcode = getIntent().getStringExtra("code");

        TextView tt = (TextView) findViewById(R.id.textView3);

        tt.setText(barcode);

        txtCountDown = (TextView) findViewById(R.id.txtCountdown);

        // close the activity in case of empty barcode
        if (TextUtils.isEmpty(barcode)) {
            Toast.makeText(getApplicationContext(), "Barcode is empty!", Toast.LENGTH_LONG).show();
            finish();
        }

        startCountDown();
    }




    public void startCountDown()
    {
        new CountDownTimer(300000, 1000) { // adjust the milli seconds here

            public void onTick(long millisUntilFinished) {
                txtCountDown.setText(""+String.format("%d h,%d min, %d sec",
                        TimeUnit.MILLISECONDS.toHours(millisUntilFinished),
                        TimeUnit.MILLISECONDS.toMinutes( millisUntilFinished),
                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))));
            }

            public void onFinish() {
                txtCountDown.setText("done!");
            }
        }.start();
    }

}
/*int seconds = (int) (milliseconds / 1000) % 60 ;
int minutes = (int) ((milliseconds / (1000*60)) % 60);
int hours   = (int) ((milliseconds / (1000*60*60)) % 24);*/