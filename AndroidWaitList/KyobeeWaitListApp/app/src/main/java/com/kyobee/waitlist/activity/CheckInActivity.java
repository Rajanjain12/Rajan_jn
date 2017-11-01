package com.kyobee.waitlist.activity;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.kyobee.waitlist.Kyobee;
import com.kyobee.waitlist.R;
import com.kyobee.waitlist.customcontrol.CustomButtonRegular;
import com.kyobee.waitlist.customcontrol.CustomTextViewKyobee;
import com.kyobee.waitlist.customcontrol.CustomTextViewRegular;
import com.kyobee.waitlist.net.ConnectivityReceiver;
import com.kyobee.waitlist.pojo.APIService;
import com.kyobee.waitlist.pojo.Login;
import com.kyobee.waitlist.utils.AppInfo;
import com.kyobee.waitlist.utils.GSONGetSet;
import com.kyobee.waitlist.utils.General;
import com.kyobee.waitlist.utils.Utils;

public class CheckInActivity extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener{

    public static String LOGTAG = CheckInActivity.class.getSimpleName ();

    RelativeLayout relativeBottom;
    CustomTextViewRegular txtCopyRight;
    CustomTextViewKyobee txtName;

    CustomButtonRegular btnPrevious, btnNext;
    Activity activity;

    Login login;
    String orgId;
    AlertDialog alertDialog;
    private APIService mAPIService;

    @Override
    public void onCreate (Bundle savedInstanceState){
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_checkin);
        activity = this;
        relativeBottom = (RelativeLayout) findViewById (R.id.relativeBottom);
        txtName = (CustomTextViewKyobee) findViewById (R.id.txtName);
        txtCopyRight = (CustomTextViewRegular) findViewById (R.id.txtCopyRight);
        String version = getString (R.string.copyright) + " " + AppInfo.getAppVersionName (this);
        txtCopyRight.setText (version);


        UIElements ();


    }

    @Override
    public void onNetworkConnectionChanged (boolean isConnected){
        if (isConnected){
           /* if (Connection.checkConnectionStatus (activity) > 0){
                callWaiting (false);
            } else{
                Utils.noInternet (activity);
            }*/
        }
    }

    public void UIElements (){
        Login login = GSONGetSet.getLogin ();
        if (login.getClientBase ().equalsIgnoreCase (General.ADMIN)){
            btnPrevious.setBackgroundResource (R.drawable.round_button_red);
            btnNext.setBackgroundResource (R.drawable.round_button_red);
            relativeBottom.setBackgroundColor (ContextCompat.getColor (activity, R.color.colorRed));
            txtName.setText (activity.getString (R.string.kyobee_caps));
        } else{
            btnPrevious.setBackgroundResource (R.drawable.round_button_red);
            btnNext.setBackgroundResource (R.drawable.round_button_red);
            relativeBottom.setBackgroundColor (ContextCompat.getColor (activity, R.color.colorBlue));
            txtName.setText (activity.getString (R.string.advantech));
        }
    }

    @Override
    protected void onDestroy (){
        super.onDestroy ();
    }

    @Override
    public boolean dispatchTouchEvent (MotionEvent ev){
        View view = getCurrentFocus ();
        if (view != null && (ev.getAction () == MotionEvent.ACTION_UP || ev.getAction () == MotionEvent.ACTION_MOVE) && view instanceof EditText && !view.getClass ().getName ().startsWith ("android.webkit.")){
            int scrcoords[] = new int[2];
            view.getLocationOnScreen (scrcoords);
            float x = ev.getRawX () + view.getLeft () - scrcoords[0];
            float y = ev.getRawY () + view.getTop () - scrcoords[1];
            if (x < view.getLeft () || x > view.getRight () || y < view.getTop () || y > view.getBottom ())
                ((InputMethodManager) this.getSystemService (Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow ((this.getWindow ().getDecorView ().getApplicationWindowToken ()), 0);
        }
        return super.dispatchTouchEvent (ev);
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
            window.setLayout ((width * 95) / 100, WindowManager.LayoutParams.WRAP_CONTENT);
        }
    }

   /* public void callAddGuest (final CustomTextViewRegular txtPoorConn, final AlertDialog alertDialog){
        CustomDialog.showProgressDialog (activity, "", getString (R.string.please_wait));
        mAPIService = General.getClient ().create (APIService.class);
        Call<ResponseAddGuest> addGuest = mAPIService.addGuest (requestAddGuest);
        addGuest.enqueue (new Callback<ResponseAddGuest> (){
            @Override
            public void onResponse (Call<ResponseAddGuest> call, Response<ResponseAddGuest> response){
                CustomDialog.dismissProgressDialog ();
                ResponseAddGuest responseAddGuest = response.body ();
                //  Gson gson = new GsonBuilder ().serializeNulls ().create ();
                //  String json = gson.toJson (responseAddGuest);
                //  Log.d (LOGTAG, "JSON " + json);

                if (responseAddGuest.getStatus ().equalsIgnoreCase (General.SUCCESS)){
                    alertDialog.dismiss ();
                    popUpResponse (responseAddGuest);
                } else{
                    CustomDialog.showAlertDialog (activity, "Error", responseAddGuest.getErrorDescription ());
                }
            }

            @Override
            public void onFailure (Call<ResponseAddGuest> call, Throwable error){
                CustomDialog.dismissProgressDialog ();
                String message = "";
                if (error instanceof SocketTimeoutException){
                    message = "Poor internet connection detected, Please try again.";
                    txtPoorConn.setVisibility (View.VISIBLE);
                    txtPoorConn.setText (message);
                } else if (error instanceof UnknownHostException){
                    message = activity.getString (R.string.no_internet);
                    txtPoorConn.setVisibility (View.VISIBLE);
                    txtPoorConn.setText (message);
                }
            }
        });
    }*/

    @Override
    protected void onResume (){
        super.onResume ();
        Kyobee.getInstance ().setConnectivityListener (this);
    }

    @Override
    public void onConfigurationChanged (Configuration newConfig){
        super.onConfigurationChanged (newConfig);
        updateDialogSize ();
    }

}
