/*
 * Copyright 2016 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the
 * License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cite.ansteph.beerly.view.beerlylover.registration.firebaseauth.ui.idp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RestrictTo;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


import com.google.firebase.auth.AuthCredential;

import java.util.ArrayList;
import java.util.List;

import cite.ansteph.beerly.R;
import cite.ansteph.beerly.view.beerlylover.registration.firebaseauth.AuthUI;
import cite.ansteph.beerly.view.beerlylover.registration.firebaseauth.IdpResponse;
import cite.ansteph.beerly.view.beerlylover.registration.firebaseauth.provider.EmailProvider;
import cite.ansteph.beerly.view.beerlylover.registration.firebaseauth.provider.GoogleProvider;
import cite.ansteph.beerly.view.beerlylover.registration.firebaseauth.provider.IdpProvider;
import cite.ansteph.beerly.view.beerlylover.registration.firebaseauth.provider.PhoneProvider;
import cite.ansteph.beerly.view.beerlylover.registration.firebaseauth.provider.Provider;
import cite.ansteph.beerly.view.beerlylover.registration.firebaseauth.provider.ProviderUtils;
import cite.ansteph.beerly.view.beerlylover.registration.firebaseauth.ui.AppCompatBase;
import cite.ansteph.beerly.view.beerlylover.registration.firebaseauth.ui.FlowParameters;
import cite.ansteph.beerly.view.beerlylover.registration.firebaseauth.ui.HelperActivityBase;
import cite.ansteph.beerly.view.beerlylover.registration.firebaseauth.ui.TaskFailureLogger;
import cite.ansteph.beerly.view.beerlylover.registration.firebaseauth.util.signincontainer.SaveSmartLock;

/**
 * Presents the list of authentication options for this app to the user. If an identity provider
 * option is selected, a {@link CredentialSignInHandler} is launched to manage the IDP-specific
 * sign-in flow. If email authentication is chosen, the {@link RegisterEmailActivity} is started. if
 * phone authentication is chosen, the {@link com.firebase.ui.auth.ui.phone.PhoneVerificationActivity}
 * is started.
 */
@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
public class AuthMethodPickerActivity extends AppCompatBase implements IdpProvider.IdpCallback {
    private static final String TAG = "AuthMethodPicker";

    private static final int RC_ACCOUNT_LINK = 3;

    private List<Provider> mProviders;
    @Nullable
    private SaveSmartLock mSaveSmartLock;

    public static Intent createIntent(Context context, FlowParameters flowParams) {
        return HelperActivityBase.createBaseIntent(
                context, AuthMethodPickerActivity.class, flowParams);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fui_auth_method_picker_layout);
        mSaveSmartLock = getAuthHelper().getSaveSmartLockInstance(this);

        populateIdpList(getFlowParams().providerInfo);

        int logoId = getFlowParams().logoId;
        if (logoId == AuthUI.NO_LOGO) {
            findViewById(R.id.logo).setVisibility(View.GONE);

            ConstraintLayout layout = findViewById(R.id.root);
            ConstraintSet constraints = new ConstraintSet();
            constraints.clone(layout);
            constraints.setHorizontalBias(R.id.container, 0.5f);
            constraints.setVerticalBias(R.id.container, 0.5f);
            constraints.applyTo(layout);
        } else {
            ImageView logo = findViewById(R.id.logo);
            logo.setImageResource(logoId);
        }
    }

    private void populateIdpList(List<AuthUI.IdpConfig> providers) {
        mProviders = new ArrayList<>();
        for (AuthUI.IdpConfig idpConfig : providers) {
            switch (idpConfig.getProviderId()) {
                case AuthUI.GOOGLE_PROVIDER:
                    mProviders.add(new GoogleProvider(this, idpConfig));
                    break;
                case AuthUI.FACEBOOK_PROVIDER:
                   // mProviders.add(new FacebookProvider(idpConfig, getFlowParams().themeId));
                    break;
                case AuthUI.TWITTER_PROVIDER:
                   // mProviders.add(new TwitterProvider(this));
                    break;
                case AuthUI.EMAIL_PROVIDER:
                    mProviders.add(new EmailProvider(this, getFlowParams()));
                    break;
                case AuthUI.PHONE_VERIFICATION_PROVIDER:
                    mProviders.add(new PhoneProvider(this, getFlowParams()));
                    break;
                default:
                    Log.e(TAG, "Encountered unknown provider parcel with type: "
                            + idpConfig.getProviderId());
            }
        }

        ViewGroup btnHolder = findViewById(R.id.btn_holder);
        for (final Provider provider : mProviders) {
            View loginButton = getLayoutInflater()
                    .inflate(provider.getButtonLayout(), btnHolder, false);

            loginButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (provider instanceof IdpProvider) {
                        getDialogHolder().showLoadingDialog(R.string.fui_progress_dialog_loading);
                    }
                    provider.startLogin(AuthMethodPickerActivity.this);
                }
            });
            if (provider instanceof IdpProvider) {
                ((IdpProvider) provider).setAuthenticationCallback(this);
            }
            btnHolder.addView(loginButton);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_ACCOUNT_LINK) {
            finish(resultCode, data);
        } else {
            for (Provider provider : mProviders) {
                provider.onActivityResult(requestCode, resultCode, data);
            }
        }
    }

    @Override
    public void onSuccess(IdpResponse response) {
        AuthCredential credential = ProviderUtils.getAuthCredential(response);
        getAuthHelper().getFirebaseAuth()
                .signInWithCredential(credential)
                .addOnCompleteListener(new CredentialSignInHandler(
                        this,
                        mSaveSmartLock,
                        RC_ACCOUNT_LINK,
                        response))
                .addOnFailureListener(
                        new TaskFailureLogger(TAG, "Firebase sign in with credential " +
                                credential.getProvider() + " unsuccessful. " +
                                "Visit https://console.firebase.google.com to enable it."));
    }

    @Override
    public void onFailure() {
        // stay on this screen
        getDialogHolder().dismissDialog();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mProviders != null) {
            for (Provider provider : mProviders) {
                if (provider instanceof GoogleProvider) {
                    ((GoogleProvider) provider).disconnect();
                }
            }
        }
    }
}
