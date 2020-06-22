// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.
/**
 * MSALModule.java functions as a wrapper around MSAL for Android's signing in, signing out, and acquiring tokens functionality.
 * It is a proof of concept, focusing on single account mode and AAD.
 */

package com.reactlibrary;

import android.util.Log;

import androidx.annotation.NonNull;

import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;

import com.facebook.react.bridge.WritableNativeMap;
import com.microsoft.identity.client.*;
import com.microsoft.identity.client.exception.MsalException;

import static com.facebook.react.views.textinput.ReactTextInputManager.TAG;

public class MSALModule extends ReactContextBaseJavaModule {

    private final ReactApplicationContext reactContext;
    private ISingleAccountPublicClientApplication publicClientApplication;

    public MSALModule(ReactApplicationContext reactContext) {
        super(reactContext);
        this.reactContext = reactContext;

        try{
            //initialize publicClientApplication with config file, located in main/java/res/raw/auth_config_single_account.json
            publicClientApplication = PublicClientApplication.createSingleAccountPublicClientApplication(
                reactContext,
                reactContext.getResources().getIdentifier("auth_config_single_account", "raw", reactContext.getPackageName()));
        } catch (Exception e) {
            //will handle this
            Log.d(TAG, "Something wrong with initialization of publicClientApplication: " + e.toString());
        }
    }

     /**
   * getName(): MSAL is the name used when importing the module from native modules
   */
    @Override
    public String getName() {
        return "MSAL";
    }

    /**
     * SignIn: calls the signIn method of ISingleAccountPublicClientApplication.
     * Parameters: string scopesValue (string containing scopes separated by a " "), Promise promise (returned to an async function)
     */
    @ReactMethod
    public void signIn(String scopesValue, Promise promise) {
        if (!scopesValue.isEmpty()) {
            publicClientApplication.signIn(getCurrentActivity(), null, scopesValue.toLowerCase().split(" "), getLoginCallback(promise));
        } else {
            //the default is User.Read
            String[] array = {"User.Read"};
            publicClientApplication.signIn(getCurrentActivity(), null, array, getLoginCallback(promise));
        }
    }

    /**
     * getLoginCallback should log messages of a successful authentication, update the current account, and then resolve/reject a promise
     * Based on getAuthCallback()
     */

    private AuthenticationCallback getLoginCallback(final Promise promise) {
        return new AuthenticationCallback() {

            @Override
            public void onSuccess(IAuthenticationResult authenticationResult) {
                Log.d(TAG, "Successfully authenticated");

            //update account
                Log.d(TAG, "Account: " + authenticationResult.getAccount().getUsername());
                promise.resolve(mapAccount(authenticationResult.getAccount()));
            }

            @Override
            public void onError(MsalException exception) {
                /* Failed to acquireToken */
                Log.d(TAG, "Authentication failed: " + exception.toString());
                promise.reject("Authentication failed: " + exception.toString(), exception);
            }

            @Override
            public void onCancel() {
                /* User canceled the authentication */
                Log.d(TAG, "User cancelled login.");
                promise.reject("userCancel", "User cancelled login.");
            }
        };
    }

    /**
     * getAccount(): retrieves account currently signed in
     * Parameters: Promise promise (resolve will return the account as a map if it exists; reject will return null for nonexistent map or error)
     */
    @ReactMethod
    public void getAccount(Promise promise) {
        //if account is null, either no account is signed in or an error occured
        IAccount account = loadAccount();
        if (account == null) {
            promise.reject("loadaccountnull", "No signed in account, or exception. Check Android log for details.");
        } else {
            promise.resolve(mapAccount(account));
        }
    }

    /**
     * loadAccount(): will load a currently signed in account. 
     * Returns an IAccount if an account exists; returns null if no account is signed in or an error occurs
     */

    private IAccount loadAccount() {
        if (publicClientApplication == null) {
            return null;
        }
        try {
            ICurrentAccountResult result = publicClientApplication.getCurrentAccount();
            IAccount currAccount = result.getCurrentAccount();
            if (currAccount == null) {
                Log.d(TAG, "No account currently signed in.");
            } else {
                Log.d(TAG, "Retrieved account.");
            }
            return currAccount;
        } catch (Exception e) {
            Log.d(TAG, "Error loading account: " + e.toString());
            return null;
        }
        
    }

    /**
     * signOut(): signs out the user currently signed in
     * Parameters: Promise promise (resolve will return a boolean true if account was successfully signed out and reject will return the exception)
     */
    @ReactMethod
    public void signOut(Promise promise) {
        publicClientApplication.signOut (new ISingleAccountPublicClientApplication.SignOutCallback() {
            @Override
            public void onSignOut() {
                Log.d(TAG, "User successfully signed out.");
                promise.resolve(true);
            }

            @Override
            public void onError(@NonNull MsalException exception) {
                Log.d(TAG, "Error while signing out: " + exception.toString());
                promise.reject(exception);
            }
        });
    }

    /*
    * Helper functions:
    * mapMSALResult(IAuthenticationResult result): used to convert AuthenticationResult into a map
    * so that it can be passed through a promise on the JS side
    * */

    private WritableNativeMap mapMSALResult (IAuthenticationResult result) {
        WritableNativeMap map = new WritableNativeMap();
        //add attributes from account
        map.putMap("account", mapAccount(result.getAccount()));
        //add attributes from result
        return map;
    }

    /**
     * Helper functions:
     * mapAccount(): used to convert IAccount into a map
     * so that it can be passed through a promise on the JS side
     * */

    private WritableNativeMap mapAccount (IAccount account) {
        WritableNativeMap map = new WritableNativeMap();
        //add attributes from account
        map.putString("authority", account.getAuthority());
        map.putString("id", account.getId());
        map.putString("tenantId", account.getTenantId());
        map.putString("username", account.getUsername());
        map.putString("idToken", (String) account.getClaims().get("id_token"));

        return map;
    }
}