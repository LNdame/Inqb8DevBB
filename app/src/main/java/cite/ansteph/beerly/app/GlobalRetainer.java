package cite.ansteph.beerly.app;

import android.app.Application;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.batch.android.Batch;
import com.batch.android.BatchActivityLifecycleHelper;
import com.batch.android.Config;
import com.onesignal.OneSignal;

/**
 * Created by loicstephan on 2017/11/14.
 */

public class GlobalRetainer extends Application {

    public static final String TAG = GlobalRetainer.class
            .getSimpleName();

    private static GlobalRetainer mInstance;
    private static Context mAppContext;

    private RequestQueue mRequestQueue;

    @Override
    public void onCreate() {
        super.onCreate();
        GlobalRetainer.mAppContext = getApplicationContext();
        mInstance = this;

        Batch.setConfig(new Config("DEV5B03FE3B4EF1D60B81B9F2901A1"));
        registerActivityLifecycleCallbacks(new BatchActivityLifecycleHelper());

        //oneSignal Initilization
        OneSignal.startInit(this)
                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                .unsubscribeWhenNotificationsAreDisabled(true)
                .init();

        Log.d(TAG,"Triggered");
    }

    public static synchronized GlobalRetainer getInstance(){
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return mRequestQueue;
    }



    public <T> void addToRequestQueue(Request<T> req, String tag) {
        // set the default tag if tag is empty
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }

    public static Context getAppContext() {
        return mAppContext;
    }

    public static void setAppContext(Context mAppContext) {
        GlobalRetainer.mAppContext = mAppContext;
    }

}
