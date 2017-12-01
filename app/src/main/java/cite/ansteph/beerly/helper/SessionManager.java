package cite.ansteph.beerly.helper;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import java.util.HashMap;

import cite.ansteph.beerly.model.Preference;
import cite.ansteph.beerly.view.beerlylover.Home;
import cite.ansteph.beerly.view.beerlylover.Preferences;
import cite.ansteph.beerly.view.beerlylover.registration.Registration;

/**
 * Created by loicstephan on 2017/11/12.
 */

public class SessionManager {


    SharedPreferences preferences;

    SharedPreferences beerPreferences;
    //Editor for shared preferences
    SharedPreferences.Editor editor;

    SharedPreferences.Editor beerPrefEditor;

    //Context
    Context _context;

    //shared pref mode
    int PRIVATE_MODE = 0;

    // Shared preferences file name
    private static final String PREF_NAME = "BeerlyBelovedPref";
    private static final String BEER_PREF_NAME = "BeerPref";


    // All Shared Preferences Keys
    private static final String KEY_IS_LOGGED_IN = "isLoggedIn";

    private static final String KEY_HAS_REGISTERED= "hasRegistered";



    public static final String KEY_FIREBASEUUID = "firebaseuuid";
    public static final String KEY_GENDER = "gender";
    public static final String KEY_DOB = "DOB";
    public static final String KEY_LASTNAME = "lastname";
    // public static final String KEY_EMPLOYEE_TYPE = "employee_type";
    public static final String KEY_EMAIL = "email";

    public static  final String KEY_INVITE ="invitation_code";

    public static  final String KEY_BASIC_ENROL ="basic_enrol";
    public static  final String KEY_ADVANCED_ENROL ="advanced_enrol";
    public static  final String KEY_ENROL ="enrol";


    public static  final String KEY_BEER_PREF1 ="beer_pref1";
    public static  final String KEY_BEER_PREF2 ="beer_pref2";
    public static  final String KEY_BEER_PREF3 ="beer_pref3";
    private static final String KEY_HAS_SAVED_PREF= "hasSavedPref";

    //contructor


    public SessionManager(Context context) {
        this._context = context;

        preferences=_context.getSharedPreferences(PREF_NAME,PRIVATE_MODE);
        beerPreferences =_context.getSharedPreferences(BEER_PREF_NAME, PRIVATE_MODE);
        editor = preferences.edit();
        beerPrefEditor = beerPreferences.edit();
    }

    /**
     * Create login session
     * */
    public void createLoginSession (String gender, String dob, String id)
    {
        //storing login value as true
        editor.putBoolean(KEY_IS_LOGGED_IN, true);

        editor.putString(KEY_FIREBASEUUID, id);
        editor.putString(KEY_GENDER, gender);
        editor.putString(KEY_DOB, dob);
        //editor.putString(KEY_LASTNAME, lastname);
        //editor.putString(KEY_EMAIL, email);
        //  editor.putString(KEY_EMPLOYEE_TYPE, employee_type);

        editor.commit();
    }

    public void recordRegistration(String id)
    {
        editor.putBoolean(KEY_HAS_REGISTERED, true);
        editor.putString(KEY_FIREBASEUUID, id);

        editor.commit();
    }


    public void recordInvite(String invite)
    {
        editor.putString(KEY_INVITE, invite);

        editor.commit();
    }


    public void recordPreference(int beerid1, int beerid2,int beerid3)
    {
        editor.putBoolean(KEY_HAS_SAVED_PREF, true);
        editor.putInt(KEY_BEER_PREF1, beerid1);
        editor.putInt(KEY_BEER_PREF2, beerid2);
        editor.putInt(KEY_BEER_PREF3, beerid3);

        editor.commit();
    }
    /**
     * Clear session details
     *
    public void logoutUser(){
        //Clearing all the data from Shared Preferences
        editor.clear();
        editor.commit();

        //After logout redirect user to login Activity

        Intent i = new Intent(_context, Login.class);
        // Closing all the Activities
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        //Add new Flag to start new Activity
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        //Starting Login Activity
        _context.startActivity(i);

        //
    }
     */

    public void createEnrol(String enrol)
    {
        beerPrefEditor.putString(KEY_ENROL, enrol);
        beerPrefEditor.commit();
    }


    public String retrieveEnrol()
    {
        return beerPreferences.getString(KEY_ENROL,null);
    }

  /*  public void checkRegPart2(){

        if(!this.isLoggedIn())
        {
            Intent i= new Intent(_context, Registration.class);

            // Closing all the Activities
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            //Add new Flag to start new Activity
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            //Starting Login Activity
            _context.startActivity(i);
        }else
        {
            Intent i = new Intent(_context, Home.class);
            // Closing all the Activities
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            //Add new Flag to start new Activity
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            //Starting Login Activity
            _context.startActivity(i);
        }

    }*/



    public void checkRegPart2(){

        if(!this.hasRegistered())
        {
           /* Intent i= new Intent(_context, Registration.class);

            // Closing all the Activities
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            //Add new Flag to start new Activity
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            //Starting Login Activity
            _context.startActivity(i);*/
        }else
        {
            if(!this.hasSavePref())
            {
                Intent i = new Intent(_context, Preferences.class);
                // Closing all the Activities
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                //Add new Flag to start new Activity
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                //Starting Login Activity
                _context.startActivity(i);

            }else {
                Intent i = new Intent(_context, Home.class);
                // Closing all the Activities
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                //Add new Flag to start new Activity
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                //Starting Login Activity
                _context.startActivity(i);
            }



        }

    }




    /**
     * Get stored session data
     * */
    public HashMap<String, String> getUserDetails(){

        HashMap<String, String> user = new HashMap<>();

        //user id
        user.put(KEY_FIREBASEUUID, preferences.getString(KEY_FIREBASEUUID,null));
        //user name
        user.put(KEY_GENDER, preferences.getString(KEY_GENDER,null));
        user.put(KEY_DOB, preferences.getString(KEY_DOB,null));
        user.put(KEY_LASTNAME, preferences.getString(KEY_LASTNAME,null));
        //user email
        user.put(KEY_EMAIL, preferences.getString(KEY_EMAIL,null));

        //user.put(KEY_EMPLOYEE_TYPE, preferences.getString(KEY_EMPLOYEE_TYPE,null));

        return user;
    }



    /**
     * Quick check for login
     * **/
    // Get Login State
    public boolean isLoggedIn() {
        return preferences.getBoolean(KEY_IS_LOGGED_IN,false);
    }

    public String getFirebaseID()
    {
        return preferences.getString(KEY_FIREBASEUUID,null);
    }
    public String getInviteCode()
    {
        return preferences.getString(KEY_INVITE,null);
    }

    // Get Login State
    public boolean hasRegistered() {
        return preferences.getBoolean(KEY_HAS_REGISTERED,false);
    }

    // Get Pref State
    public boolean hasSavePref() {
        return preferences.getBoolean(KEY_HAS_SAVED_PREF,false);
    }



}
