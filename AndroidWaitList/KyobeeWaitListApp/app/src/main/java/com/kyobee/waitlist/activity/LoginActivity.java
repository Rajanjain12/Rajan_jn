package com.kyobee.waitlist.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.hanks.library.AnimateCheckBox;
import com.kyobee.waitlist.Kyobee;
import com.kyobee.waitlist.R;
import com.kyobee.waitlist.customcontrol.CustomButtonRegular;
import com.kyobee.waitlist.customcontrol.CustomDialog;
import com.kyobee.waitlist.customcontrol.CustomEditTextRegular;
import com.kyobee.waitlist.customcontrol.CustomTextViewRegular;
import com.kyobee.waitlist.net.Connection;
import com.kyobee.waitlist.pojo.APIService;
import com.kyobee.waitlist.pojo.Login;
import com.kyobee.waitlist.utils.AppInfo;
import com.kyobee.waitlist.utils.GSONGetSet;
import com.kyobee.waitlist.utils.General;
import com.kyobee.waitlist.utils.Utils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity{

    public static final int GUEST_MODE = 0;
    public static final int DISPLAY_MODE = 1;

    public static String LOGTAG = LoginActivity.class.getSimpleName ();
    CustomTextViewRegular txtCopyRight;
    CustomEditTextRegular edtUsername, edtPassword;
    LinearLayout linearRemember;
    AnimateCheckBox chkRemember;
    CustomButtonRegular btnLogin;
    String username = "", password = "";
    boolean remember = false;
    AlertDialog alertDialog;
    AppCompatActivity activity;
    int operation = -1;
    private APIService mAPIService;

    @Override
    public void onCreate (Bundle savedInstanceState){
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_login);
        activity = this;


        if (getIntent ().hasExtra (General.OP)){
            operation = getIntent ().getIntExtra (General.OP, -1);
        }

        if (operation > -1){
            popUpCheckinDisplay ();
        } else if (Kyobee.getInstance ().isLoggedIn () && Kyobee.getInstance ().isRemember ()){
            // if user logged in but selected any mode or not
            if (Kyobee.getInstance ().isModeSelected ()){
                if (Kyobee.getInstance ().getLoginMode () == GUEST_MODE){
                    startActivity (new Intent (activity, GuestActivity.class));
                    finish ();
                } else if (Kyobee.getInstance ().getLoginMode () == DISPLAY_MODE){
                    startActivity (new Intent (activity, DisplayMultiActivity.class));
                    finish ();
                }
            } else{
                popUpCheckinDisplay ();
            }
        } else{
            Kyobee.getInstance ().logout ();
        }

        txtCopyRight = (CustomTextViewRegular) findViewById (R.id.txtCopyRight);
        String version = getString (R.string.copyright) + " " + AppInfo.getAppVersionName (this);
        txtCopyRight.setText (version);

        edtUsername = (CustomEditTextRegular) findViewById (R.id.edtUsername);
        edtPassword = (CustomEditTextRegular) findViewById (R.id.edtPassword);
        linearRemember = (LinearLayout) findViewById (R.id.linearRemember);
        chkRemember = (AnimateCheckBox) findViewById (R.id.chkRemember);
        btnLogin = (CustomButtonRegular) findViewById (R.id.btnLogin);
        linearRemember.setOnClickListener (new View.OnClickListener (){
            @Override
            public void onClick (View v){
                if (chkRemember.isChecked ()){
                    chkRemember.setChecked (false);
                } else{
                    chkRemember.setChecked (true);
                }
            }
        });
/*
        if (Kyobee.getInstance ().isRemember ()){
            edtUsername.setText (Kyobee.getInstance ().getUsername ());
            edtPassword.setText (Kyobee.getInstance ().getPassword ());
        }*/

        btnLogin.setOnClickListener (new View.OnClickListener (){
            @Override
            public void onClick (View v){
                if (Connection.checkConnectionStatus (activity) > 0){
                    validation ();
                } else{
                    Utils.noInternet (activity);
                }

            }
        });

    }

    public void callLogin (){

        CustomDialog.showProgressDialog (activity, getString (R.string.login), getString (R.string.please_wait));
        mAPIService = General.getClient ().create (APIService.class);
        Call<Login> loginCredAuth = mAPIService.loginCredAuth (username, password);

        Log.d (LOGTAG, "Log In Url - " + mAPIService.loginCredAuth (username, password).request ().url ());
        loginCredAuth.enqueue (new Callback<Login> (){
            @Override
            public void onResponse (Call<Login> call, Response<Login> response){
                CustomDialog.dismissProgressDialog ();
                Log.d (LOGTAG, "Success Log In - " + response.toString ());
                Login login = response.body ();
                if (login.getSuccess ().equalsIgnoreCase (General.OK)){
                    GSONGetSet.setLogin (login);
                    Kyobee.getInstance ().setOrgId (login.getOrgId ());
                    if (remember){
                        Kyobee.getInstance ().setUsername (username);
                        Kyobee.getInstance ().setPassword (password);
                    } else{
                        Kyobee.getInstance ().setUsername ("");
                        Kyobee.getInstance ().setPassword ("");
                    }
                    // for select checkin or display mode
                    popUpCheckinDisplay ();

                } else{
                    CustomDialog.showAlertDialog (LoginActivity.this, "Error", login.getError ());
                }

            }

            @Override
            public void onFailure (Call<Login> call, Throwable t){


                CustomDialog.dismissProgressDialog ();
                Log.d (LOGTAG, "Log In - " + t.getMessage ());
            }
        });
    }

    public void validation (){
        username = edtUsername.getText ().toString ();
        password = edtPassword.getText ().toString ();
       // username = "jkim@kyobee.com";
       // password = "jaekim";
        if (username.equalsIgnoreCase ("")){
            CustomDialog.showAlertDialog (LoginActivity.this, "Kyobee", "All fields are mandatory.");
        } else if (password.equalsIgnoreCase ("")){
            CustomDialog.showAlertDialog (LoginActivity.this, "Kyobee", "All fields are mandatory.");
        } else{
            if (chkRemember.isChecked ())
                remember = true;
            else
                remember = false;
            callLogin ();
        }
    }

    public void popUpCheckinDisplay (){
        DisplayMetrics displayMetrics = new DisplayMetrics ();
        getWindowManager ().getDefaultDisplay ().getMetrics (displayMetrics);
        int width = 0;//displayMetrics.widthPixels;
        if (Utils.getScreenOrientation (activity) == 1){
            width = (displayMetrics.widthPixels * 80) / 100;//displayMetrics.heightPixels;
        } else{
            width = (displayMetrics.widthPixels * 95) / 100;
        }

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder (LoginActivity.this);
        LayoutInflater layout = (LayoutInflater) getSystemService (Context.LAYOUT_INFLATER_SERVICE);
        final View view = layout.inflate (R.layout.popup_guest_mode, (ViewGroup) findViewById (R.id.popUpGuestMode));
        alertDialogBuilder.setView (view);
        alertDialogBuilder.setCancelable (false);
        // create alert dialog
        alertDialog = alertDialogBuilder.create ();
        alertDialog.show ();

        alertDialog.getWindow ().setBackgroundDrawable (new ColorDrawable (Color.TRANSPARENT));

        Window window = alertDialog.getWindow ();
        window.setGravity (Gravity.CENTER);
        window.setLayout (width, WindowManager.LayoutParams.WRAP_CONTENT);

        LinearLayout linearPopup = (LinearLayout) view.findViewById (R.id.linearPopup);

        RelativeLayout.LayoutParams relativeParams = new RelativeLayout.LayoutParams ((width * 80) / 100, ViewGroup.LayoutParams.WRAP_CONTENT);
        relativeParams.addRule (RelativeLayout.CENTER_IN_PARENT);
        // linearPopup.setLayoutParams (relativeParams);

        //    LinearLayout.LayoutParams layoutParams=new LinearLayout.LayoutParams (500, WindowManager.LayoutParams.WRAP_CONTENT);
        //    layoutParams.gravity=Gravity.CENTER;
        //    linearPopup.setLayoutParams (layoutParams);

        final CustomButtonRegular btnCheckIn = (CustomButtonRegular) view.findViewById (R.id.btnCheckIn);
        final CustomButtonRegular btnDisplay = (CustomButtonRegular) view.findViewById (R.id.btnDisplay);

        btnCheckIn.setOnClickListener (new View.OnClickListener (){
            @Override
            public void onClick (View v){
                Kyobee.getInstance ().setLoginMode (GUEST_MODE);
                startActivity (new Intent (activity, GuestActivity.class));
                finish ();
            }
        });

        btnDisplay.setOnClickListener (new View.OnClickListener (){
            @Override
            public void onClick (View v){
                Kyobee.getInstance ().setLoginMode (DISPLAY_MODE);
                startActivity (new Intent (activity, DisplayMultiActivity.class));
                finish ();
            }
        });

        dismissPopUp (alertDialog);
    }

    @Override
    public void onConfigurationChanged (Configuration newConfig){
        super.onConfigurationChanged (newConfig);
        updateDialogSize ();
    }

    public void updateDialogSize (){
        DisplayMetrics displayMetrics = new DisplayMetrics ();
        getWindowManager ().getDefaultDisplay ().getMetrics (displayMetrics);
        int width = 0;//displayMetrics.widthPixels;
        if (Utils.getScreenOrientation (activity) == 1){
            width = displayMetrics.widthPixels;//displayMetrics.heightPixels;
        } else{
            width = displayMetrics.widthPixels;
        }
        if (alertDialog != null){
            Window window = alertDialog.getWindow ();
            window.setGravity (Gravity.CENTER);
            window.setLayout (width, WindowManager.LayoutParams.WRAP_CONTENT);
        }
    }

    private void dismissPopUp (final AlertDialog alert){
        alert.setOnKeyListener (new DialogInterface.OnKeyListener (){
            @Override
            public boolean onKey (DialogInterface dialogInterface, int keyCode, KeyEvent keyEvent){
                if (keyCode == KeyEvent.KEYCODE_BACK){
                    finish ();
                    alert.dismiss ();
                    return true;
                }
                return false;
            }
        });
    }

}
