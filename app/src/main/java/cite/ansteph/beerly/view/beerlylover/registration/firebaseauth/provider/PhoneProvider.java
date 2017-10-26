package cite.ansteph.beerly.view.beerlylover.registration.firebaseauth.provider;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.LayoutRes;

import cite.ansteph.beerly.R;
import cite.ansteph.beerly.view.beerlylover.registration.firebaseauth.AuthUI;
import cite.ansteph.beerly.view.beerlylover.registration.firebaseauth.ui.FlowParameters;
import cite.ansteph.beerly.view.beerlylover.registration.firebaseauth.ui.phone.PhoneVerificationActivity;


public class PhoneProvider implements Provider {

    private static final int RC_PHONE_FLOW = 4;

    private Activity mActivity;
    private FlowParameters mFlowParameters;

    public PhoneProvider(Activity activity, FlowParameters parameters) {
        mActivity = activity;
        mFlowParameters = parameters;
    }

    @Override
    public String getName(Context context) {
        return context.getString(R.string.fui_provider_name_phone);
    }

    @Override
    @LayoutRes
    public int getButtonLayout() {
        return R.layout.fui_provider_button_phone;
    }

    @Override
    public void startLogin(Activity activity) {

        Bundle params = null;
        for (AuthUI.IdpConfig idpConfig : mFlowParameters.providerInfo) {
            if (idpConfig.getProviderId().equals(AuthUI.PHONE_VERIFICATION_PROVIDER)) {
                params = idpConfig.getParams();
            }
        }

        activity.startActivityForResult(
                PhoneVerificationActivity.createIntent(activity, mFlowParameters, params),
                RC_PHONE_FLOW);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RC_PHONE_FLOW && resultCode == Activity.RESULT_OK) {
            mActivity.setResult(Activity.RESULT_OK, data);
            mActivity.finish();
        }
    }
}
