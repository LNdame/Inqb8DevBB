package cite.ansteph.beerly;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import cite.ansteph.beerly.view.beerlylover.Home;
import com.crashlytics.android.Crashlytics;


import cite.ansteph.beerly.view.beerlylover.registration.Login;
import cite.ansteph.beerly.view.beerlylover.registration.Registration;
import io.fabric.sdk.android.Fabric;

public class Splash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_splash);




        Thread timerThread = new Thread(){
            public void run(){
                try{
                    sleep(4000);
                }catch(InterruptedException e){
                    e.printStackTrace();
                }finally{
                    /**
                     * Call this function whenever you want to check user login
                     * This will redirect user to Login is he is not
                     * logged in
                     * */
                    //sessionManager.checkLogin();
                    Intent intent = new Intent(getApplicationContext(), Registration.class);
                    startActivity(intent);



                }
            }
        };
        timerThread.start();

    }

    public void goHome(View v)
    {

       // startActivity(new Intent(getApplicationContext(), Home.class));
        startActivity(new Intent(getApplicationContext(), Home.class));
    }
}
