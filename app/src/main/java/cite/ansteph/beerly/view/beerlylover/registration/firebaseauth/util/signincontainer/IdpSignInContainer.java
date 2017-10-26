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

package cite.ansteph.beerly.view.beerlylover.registration.firebaseauth.util.signincontainer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;


import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.TwitterAuthProvider;

import cite.ansteph.beerly.view.beerlylover.registration.firebaseauth.AuthUI;
import cite.ansteph.beerly.view.beerlylover.registration.firebaseauth.ErrorCodes;
import cite.ansteph.beerly.view.beerlylover.registration.firebaseauth.IdpResponse;
import cite.ansteph.beerly.view.beerlylover.registration.firebaseauth.User;
import cite.ansteph.beerly.view.beerlylover.registration.firebaseauth.provider.GoogleProvider;
import cite.ansteph.beerly.view.beerlylover.registration.firebaseauth.provider.IdpProvider;
import cite.ansteph.beerly.view.beerlylover.registration.firebaseauth.provider.ProviderUtils;

import cite.ansteph.beerly.view.beerlylover.registration.firebaseauth.ui.ExtraConstants;
import cite.ansteph.beerly.view.beerlylover.registration.firebaseauth.ui.FlowParameters;
import cite.ansteph.beerly.view.beerlylover.registration.firebaseauth.ui.FragmentBase;
import cite.ansteph.beerly.view.beerlylover.registration.firebaseauth.ui.HelperActivityBase;
import cite.ansteph.beerly.view.beerlylover.registration.firebaseauth.ui.TaskFailureLogger;
import cite.ansteph.beerly.view.beerlylover.registration.firebaseauth.ui.idp.CredentialSignInHandler;


public class IdpSignInContainer extends FragmentBase implements IdpProvider.IdpCallback {
    private static final String TAG = "IDPSignInContainer";
    private static final int RC_WELCOME_BACK_IDP = 4;

    private HelperActivityBase mActivity;
    private IdpProvider mIdpProvider;
    @Nullable
    private SaveSmartLock mSaveSmartLock;

    public static void signIn(FragmentActivity activity, FlowParameters parameters, User user) {
        FragmentManager fm = activity.getSupportFragmentManager();
        Fragment fragment = fm.findFragmentByTag(TAG);
        if (!(fragment instanceof IdpSignInContainer)) {
            IdpSignInContainer result = new IdpSignInContainer();

            Bundle bundle = parameters.toBundle();
            bundle.putParcelable(ExtraConstants.EXTRA_USER, user);
            result.setArguments(bundle);

            try {
                fm.beginTransaction().add(result, TAG).disallowAddToBackStack().commit();
            } catch (IllegalStateException e) {
                Log.e(TAG, "Cannot add fragment", e);
            }
        }
    }

    public static IdpSignInContainer getInstance(FragmentActivity activity) {
        Fragment fragment = activity.getSupportFragmentManager().findFragmentByTag(TAG);
        if (fragment instanceof IdpSignInContainer) {
            return (IdpSignInContainer) fragment;
        } else {
            return null;
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (!(getActivity() instanceof HelperActivityBase)) {
            throw new RuntimeException("Can only attach IdpSignInContainer to HelperActivityBase.");
        }

        mActivity = (HelperActivityBase) getActivity();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSaveSmartLock = getAuthHelper().getSaveSmartLockInstance(mActivity);

        User user = User.getUser(getArguments());
        String provider = user.getProviderId();

        AuthUI.IdpConfig providerConfig = null;
        for (AuthUI.IdpConfig config : getFlowParams().providerInfo) {
            if (config.getProviderId().equalsIgnoreCase(provider)) {
                providerConfig = config;
                break;
            }
        }

        if (providerConfig == null) {
            // we don't have a provider to handle this
            finish(Activity.RESULT_CANCELED, IdpResponse.getErrorCodeIntent(ErrorCodes.UNKNOWN_ERROR));
            return;
        }

        if (provider.equalsIgnoreCase(GoogleAuthProvider.PROVIDER_ID)) {
            mIdpProvider = new GoogleProvider(
                    getActivity(),
                    providerConfig,
                    user.getEmail());
        } else if (provider.equalsIgnoreCase(FacebookAuthProvider.PROVIDER_ID)) {
           // mIdpProvider = new FacebookProvider(providerConfig, getFlowParams().themeId);
        } else if (provider.equalsIgnoreCase(TwitterAuthProvider.PROVIDER_ID)) {
          //  mIdpProvider = new TwitterProvider(getContext());
        }

        mIdpProvider.setAuthenticationCallback(this);

        if (savedInstanceState == null) {
            mIdpProvider.startLogin(getActivity());
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putBoolean(ExtraConstants.HAS_EXISTING_INSTANCE, true);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onSuccess(IdpResponse response) {
        AuthCredential credential = ProviderUtils.getAuthCredential(response);
        getAuthHelper().getFirebaseAuth()
                .signInWithCredential(credential)
                .addOnCompleteListener(new CredentialSignInHandler(
                        mActivity,
                        mSaveSmartLock,
                        RC_WELCOME_BACK_IDP,
                        response))
                .addOnFailureListener(
                        new TaskFailureLogger(TAG, "Failure authenticating with credential " +
                                credential.getProvider()));
    }

    @Override
    public void onFailure() {
        finish(Activity.RESULT_CANCELED, IdpResponse.getErrorCodeIntent(ErrorCodes.UNKNOWN_ERROR));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_WELCOME_BACK_IDP) {
            finish(resultCode, data);
        } else {
            mIdpProvider.onActivityResult(requestCode, resultCode, data);
        }
    }
}