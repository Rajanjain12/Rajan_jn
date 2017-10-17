package com.kyobee.waitlist.activity;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;

import com.github.reinaldoarrosi.maskededittext.MaskedEditText;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.kyobee.waitlist.Kyobee;
import com.kyobee.waitlist.R;
import com.kyobee.waitlist.customcontrol.CustomButtonBold;
import com.kyobee.waitlist.customcontrol.CustomButtonRegular;
import com.kyobee.waitlist.customcontrol.CustomDialog;
import com.kyobee.waitlist.customcontrol.CustomEditTextRegular;
import com.kyobee.waitlist.customcontrol.CustomTextViewBold;
import com.kyobee.waitlist.customcontrol.CustomTextViewKyobee;
import com.kyobee.waitlist.customcontrol.CustomTextViewRegular;
import com.kyobee.waitlist.customcontrol.HorizontalListView;
import com.kyobee.waitlist.net.Connection;
import com.kyobee.waitlist.net.ConnectivityReceiver;
import com.kyobee.waitlist.pojo.APIService;
import com.kyobee.waitlist.pojo.Add;
import com.kyobee.waitlist.pojo.ChannelMessage;
import com.kyobee.waitlist.pojo.Display;
import com.kyobee.waitlist.pojo.GuestPreference;
import com.kyobee.waitlist.pojo.Login;
import com.kyobee.waitlist.pojo.RequestAddGuest;
import com.kyobee.waitlist.pojo.ResponseAddGuest;
import com.kyobee.waitlist.pojo.Seatpref;
import com.kyobee.waitlist.pojo.Upd;
import com.kyobee.waitlist.utils.AppInfo;
import com.kyobee.waitlist.utils.GSONGetSet;
import com.kyobee.waitlist.utils.General;
import com.kyobee.waitlist.utils.RealTimePush;
import com.kyobee.waitlist.utils.Utils;
import com.kyobee.waitlist.utils.Validation;
import com.piotrek.customspinner.CustomSpinner;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import ibt.ortc.extensibility.OrtcClient;
import it.sephiroth.android.library.uigestures.UIGestureRecognizer;
import it.sephiroth.android.library.uigestures.UIGestureRecognizerDelegate;
import it.sephiroth.android.library.uigestures.UILongPressGestureRecognizer;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GuestActivity extends AppCompatActivity implements RealTimePush.RealTimeListener, ConnectivityReceiver.ConnectivityReceiverListener, UILongPressGestureRecognizer.OnActionListener, UIGestureRecognizerDelegate.Callback{

    private static final int LOGOUT = 1;
    private static final int SETTINGS = 0;
    public static String LOGTAG = GuestActivity.class.getSimpleName ();
    int operation = -1;

    CustomTextViewRegular txtCopyRight;
    CustomTextViewBold txtWelcome;
    CustomButtonBold btnCheckIn;
    CustomTextViewBold txtWaiting, txtParties, txtNumber, txtTime, txtServing, txtEstWaitTime;
    CustomTextViewKyobee txtName;
    RelativeLayout relativeBottom;
    View viewSeperator;
    Activity activity;

    ImageView imgLogo;
    String imgPath = "";
    RequestAddGuest requestAddGuest;

    Login login;
    String orgId;
    List<Seatpref> listSeatPref = new ArrayList<> ();
    RealTimePush realTimePush;
    AlertDialog alertDialog;
    View viewLogout, viewSettting;
    boolean validateEmailPhone = false;
    UIGestureRecognizerDelegate delegate;
    private APIService mAPIService;

    @Override
    public void onCreate (Bundle savedInstanceState){
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_guest);
        activity = this;
        login = GSONGetSet.getLogin ();
        if (login.getSmsRoute () == null || login.getSmsRoute ().equalsIgnoreCase ("")){
            validateEmailPhone = false;
        } else{
            validateEmailPhone = true;
        }

        realTimePush = new RealTimePush (activity, login);

        orgId = Kyobee.getInstance ().getOrgId ();
        imgLogo = (ImageView) findViewById (R.id.imgLogo);

        btnCheckIn = (CustomButtonBold) findViewById (R.id.btnCheckIn);


        relativeBottom = (RelativeLayout) findViewById (R.id.relativeBottom);
        viewSeperator = (View) findViewById (R.id.viewSeperator);

        txtName = (CustomTextViewKyobee) findViewById (R.id.txtName);
        txtEstWaitTime = (CustomTextViewBold) findViewById (R.id.txtEstWaitTime);
        txtServing = (CustomTextViewBold) findViewById (R.id.txtServing);
        txtWaiting = (CustomTextViewBold) findViewById (R.id.txtWaiting);
        txtParties = (CustomTextViewBold) findViewById (R.id.txtParties);
        txtNumber = (CustomTextViewBold) findViewById (R.id.txtNumber);
        txtTime = (CustomTextViewBold) findViewById (R.id.txtTime);
        txtWelcome = (CustomTextViewBold) findViewById (R.id.txtWelcome);
        txtCopyRight = (CustomTextViewRegular) findViewById (R.id.txtCopyRight);
        String version = getString (R.string.copyright) + " " + AppInfo.getAppVersionName (this);
        txtCopyRight.setText (version);

        viewLogout = (View) findViewById (R.id.viewLogout);
        viewSettting = (View) findViewById (R.id.viewSetting);
        imgPath = General.IMAGE + login.getLogofileName ();

        btnCheckIn.setOnClickListener (new View.OnClickListener (){
            @Override
            public void onClick (View v){
                if (Connection.checkConnectionStatus (activity) > 0){
                    popUpAddMe ();
                } else{
                    Utils.noInternet (activity);
                }
            }
        });


        delegate = new UIGestureRecognizerDelegate (this);
        UILongPressGestureRecognizer longPress = new UILongPressGestureRecognizer (this);
        longPress.setTag ("double-long-press");
        longPress.setNumberOfTouchesRequired (2);
        longPress.setNumberOfTapsRequired (0);
        longPress.setMinimumPressDuration (2000);
        longPress.setActionListener (this);
        delegate.addGestureRecognizer (longPress);


        viewLogout.setOnTouchListener (new View.OnTouchListener (){
            @Override
            public boolean onTouch (View v, MotionEvent event){
                operation = LOGOUT;
                return delegate.onTouchEvent (v, event);
            }
        });

        viewSettting.setOnTouchListener (new View.OnTouchListener (){
            @Override
            public boolean onTouch (View v, MotionEvent event){
                operation = SETTINGS;
                return delegate.onTouchEvent (v, event);
            }
        });

        UIElements ();

        if (Connection.checkConnectionStatus (activity) > 0){
            callWaiting (true);
        } else{
            Utils.noInternet (activity);
        }
    }

    @Override
    public void onNetworkConnectionChanged (boolean isConnected){
        if (isConnected){
            if (Connection.checkConnectionStatus (activity) > 0){
                callWaiting (false);
            } else{
                Utils.noInternet (activity);
            }
        }
    }

    // ui gesture recognizer event callback
    @Override
    public void onGestureRecognized (@NonNull final UIGestureRecognizer recognizer){
        //Log.d (getClass ().getSimpleName (), "onGestureRecognized(" + recognizer + "). state: " + recognizer.getState ());

        if (operation == LOGOUT){
            Login logIn = GSONGetSet.getLogin ();
            realTimePush.disConnect (logIn);
            Kyobee.getInstance ().logout ();
            startActivity (new Intent (activity, LoginActivity.class));
            finish ();
        } else if (operation == SETTINGS){
            startActivity (new Intent (activity, LoginActivity.class).putExtra (General.OP, Kyobee.getInstance ().getLoginMode ()));
            finish ();
        }
    }

    public void UIElements (){

        Login login = GSONGetSet.getLogin ();

        if (login.getClientBase ().equalsIgnoreCase (General.ADMIN)){
            txtWaiting.setTextColor (ContextCompat.getColor (activity, R.color.colorRed));
            txtParties.setTextColor (ContextCompat.getColor (activity, R.color.colorRed));
            txtServing.setTextColor (ContextCompat.getColor (activity, R.color.colorRed));
            txtNumber.setTextColor (ContextCompat.getColor (activity, R.color.colorRed));
            viewSeperator.setBackgroundColor (ContextCompat.getColor (activity, R.color.colorRed));
            txtEstWaitTime.setTextColor (ContextCompat.getColor (activity, R.color.colorRed));
            txtTime.setTextColor (ContextCompat.getColor (activity, R.color.colorRed));
            btnCheckIn.setBackgroundResource (R.drawable.round_button_red);
            relativeBottom.setBackgroundColor (ContextCompat.getColor (activity, R.color.colorRed));
            txtName.setText (activity.getString (R.string.kyobee_caps));
            //  txtCopyRight.setTextColor (ContextCompat.getColor (activity, R.color.colorRed));
        } else{
            txtWaiting.setTextColor (ContextCompat.getColor (activity, R.color.colorBlue));
            txtParties.setTextColor (ContextCompat.getColor (activity, R.color.colorBlue));
            txtServing.setTextColor (ContextCompat.getColor (activity, R.color.colorBlue));
            txtNumber.setTextColor (ContextCompat.getColor (activity, R.color.colorBlue));
            viewSeperator.setBackgroundColor (ContextCompat.getColor (activity, R.color.colorBlue));
            txtEstWaitTime.setTextColor (ContextCompat.getColor (activity, R.color.colorBlue));
            txtTime.setTextColor (ContextCompat.getColor (activity, R.color.colorBlue));
            btnCheckIn.setBackgroundResource (R.drawable.round_button_blue);
            relativeBottom.setBackgroundColor (ContextCompat.getColor (activity, R.color.colorBlue));
            txtName.setText (activity.getString (R.string.advantech));
            //txtCopyRight.setTextColor (ContextCompat.getColor (activity, R.color.colorBlue));
        }

    }

    @Override
    public boolean shouldBegin (final UIGestureRecognizer recognizer){
        Log.d (LOGTAG, "shouldBegin");
        return true;
    }

    @Override
    public boolean shouldRecognizeSimultaneouslyWithGestureRecognizer (final UIGestureRecognizer current, final UIGestureRecognizer recognizer){
        Log.d (LOGTAG, "shouldRecognizeSimultaneouslyWithGestureRecognizer");
        return true;
    }

    @Override
    protected void onDestroy (){
        super.onDestroy ();
        if (Kyobee.getInstance ().isLoggedIn ()){
            Login logIn = GSONGetSet.getLogin ();
            realTimePush.disConnect (logIn);
        }
    }

    @Override
    public boolean shouldReceiveTouch (final UIGestureRecognizer recognizer){
        Log.d (LOGTAG, "shouldReceiveTouch");
        return true;
    }

    @Override
    public boolean dispatchTouchEvent (MotionEvent ev){
        View view = getCurrentFocus();
        if (view != null && (ev.getAction() == MotionEvent.ACTION_UP || ev.getAction() == MotionEvent.ACTION_MOVE) && view instanceof EditText && !view.getClass().getName().startsWith("android.webkit.")) {
            int scrcoords[] = new int[2];
            view.getLocationOnScreen(scrcoords);
            float x = ev.getRawX() + view.getLeft() - scrcoords[0];
            float y = ev.getRawY() + view.getTop() - scrcoords[1];
            if (x < view.getLeft() || x > view.getRight() || y < view.getTop() || y > view.getBottom())
                ((InputMethodManager)this.getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow((this.getWindow().getDecorView().getApplicationWindowToken()), 0);
        }
        return super.dispatchTouchEvent(ev);
    }

    public void popUpAddMe (){
        requestAddGuest = new RequestAddGuest ();
        DisplayMetrics displayMetrics = new DisplayMetrics ();
        getWindowManager ().getDefaultDisplay ().getMetrics (displayMetrics);
        int width = 0;//displayMetrics.widthPixels;
        if (Utils.getScreenOrientation (activity) == 1){
            width = (displayMetrics.widthPixels * 80) / 100;//displayMetrics.heightPixels;
        } else{
            width = (displayMetrics.widthPixels * 95) / 100;
        }

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder (GuestActivity.this);
        LayoutInflater layout = (LayoutInflater) getSystemService (Context.LAYOUT_INFLATER_SERVICE);
        final View view = layout.inflate (R.layout.popup_add_new_guest, (ViewGroup) findViewById (R.id.pop_add_new_guest));
        alertDialogBuilder.setView (view);
        alertDialogBuilder.setCancelable (false);

        // create alert dialog
        alertDialog = alertDialogBuilder.create ();
        alertDialog.show ();

        alertDialog.getWindow ().setBackgroundDrawable (new ColorDrawable (Color.WHITE));

        Window window = alertDialog.getWindow ();
        window.setGravity (Gravity.CENTER);
        window.setLayout (width, WindowManager.LayoutParams.WRAP_CONTENT);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams ((width * 95) / 100, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.gravity = Gravity.CENTER;

        RelativeLayout relativeTop = (RelativeLayout) view.findViewById (R.id.relativeTop);

        ImageView imgCancel = (ImageView) view.findViewById (R.id.imgCancel);
        final CustomEditTextRegular edtName = (CustomEditTextRegular) view.findViewById (R.id.edtName);
        final RadioButton radioPhone = (RadioButton) view.findViewById (R.id.radioPhone);
        final RadioButton radioEmail = (RadioButton) view.findViewById (R.id.radioEmail);
        final CustomEditTextRegular edtEmail = (CustomEditTextRegular) view.findViewById (R.id.edtEmail);
        final MaskedEditText edtPhone = (MaskedEditText) view.findViewById (R.id.edtPhone);
        final CustomTextViewRegular txtValidPhone = (CustomTextViewRegular) view.findViewById (R.id.txtValidEmail);
        final CustomSpinner spnrPeople = (CustomSpinner) view.findViewById (R.id.spnrPeople);
        String[] types = getResources ().getStringArray (R.array.number_array);
        spnrPeople.initializeStringValues (types, getString (R.string.people_party));

        final CustomEditTextRegular edtAdults = (CustomEditTextRegular) view.findViewById (R.id.edtAdults);
        final CustomEditTextRegular edtChildrens = (CustomEditTextRegular) view.findViewById (R.id.edtChildrens);
        final CustomEditTextRegular edtInfants = (CustomEditTextRegular) view.findViewById (R.id.edtInfants);
        edtInfants.setEnabled (false);

        final HorizontalListView horizontalListView = (HorizontalListView) view.findViewById (R.id.horizontalList);
        GuestHorizontalListAdapter listAdapter = new GuestHorizontalListAdapter ();
        horizontalListView.setAdapter (listAdapter);

        final CheckBox chkReceive = (CheckBox) view.findViewById (R.id.chkReceive);
        CustomButtonRegular btnAddMe = (CustomButtonRegular) view.findViewById (R.id.btnAddMe);
        LinearLayout linearReceive = (LinearLayout) view.findViewById (R.id.linearReceive);
        final CustomTextViewRegular txtPoor = (CustomTextViewRegular) view.findViewById (R.id.txtPoor);
        txtPoor.setVisibility (View.GONE);
        Login logins = GSONGetSet.getLogin ();
        if (logins.getClientBase ().equalsIgnoreCase (General.ADMIN)){
            relativeTop.setBackgroundColor (ContextCompat.getColor (activity, R.color.colorRed));
            btnAddMe.setBackgroundColor (ContextCompat.getColor (activity, R.color.colorRed));
        } else{
            relativeTop.setBackgroundColor (ContextCompat.getColor (activity, R.color.colorBlue));
            btnAddMe.setBackgroundColor (ContextCompat.getColor (activity, R.color.colorBlue));
        }

        spnrPeople.setOnItemSelectedListener (new AdapterView.OnItemSelectedListener (){
            @Override
            public void onItemSelected (AdapterView<?> parent, View view, int position, long id){
                Log.d (LOGTAG, "" + position);
                spnrPeople.setBackgroundResource (R.drawable.correct_border);
                requestAddGuest.setNoOfPeople (String.valueOf (position));
            }

            @Override
            public void onNothingSelected (AdapterView<?> parent){
                Log.d (LOGTAG, "Nohing ");
            }
        });

        horizontalListView.setOnItemClickListener (new AdapterView.OnItemClickListener (){
            @Override
            public void onItemClick (AdapterView<?> parent, View view, int position, long id){
                Seatpref seatpref = login.getSeatpref ().get (position);
                CheckBox checkBox = (CheckBox) view.findViewById (R.id.chkSeating);
                if (checkBox.isChecked ()){
                    checkBox.setChecked (false);
                    for (Seatpref seatpref1 : listSeatPref){
                        if (seatpref1.getPrefValueId () == (seatpref.getPrefValueId ())){
                            listSeatPref.remove (seatpref1);
                            break;
                        }
                    }
                } else{
                    checkBox.setChecked (true);
                    listSeatPref.add (seatpref);
                }
            }
        });

        linearReceive.setOnClickListener (new View.OnClickListener (){
            @Override
            public void onClick (View v){
                if (chkReceive.isChecked ()){
                    requestAddGuest.setOptin ("false");
                    chkReceive.setChecked (false);
                } else{
                    requestAddGuest.setOptin ("true");
                    chkReceive.setChecked (true);
                }
            }
        });

        radioEmail.setOnClickListener (new View.OnClickListener (){
            @Override
            public void onClick (View v){
                radioPhone.setChecked (false);
                radioEmail.setChecked (true);
                edtPhone.setText ("");
                edtPhone.setVisibility (View.INVISIBLE);
                edtEmail.setVisibility (View.VISIBLE);
                txtValidPhone.setVisibility (View.GONE);
                edtEmail.requestFocus ();
            }
        });

        radioPhone.setOnClickListener (new View.OnClickListener (){
            @Override
            public void onClick (View v){
                radioPhone.setChecked (true);
                radioEmail.setChecked (false);
                edtEmail.setText ("");
                edtEmail.setVisibility (View.INVISIBLE);
                edtPhone.setVisibility (View.VISIBLE);
                txtValidPhone.setVisibility (View.VISIBLE);
                edtPhone.requestFocus ();
            }
        });

        edtName.addTextChangedListener (new TextWatcher (){
            @Override
            public void beforeTextChanged (CharSequence s, int start, int count, int after){

            }

            @Override
            public void onTextChanged (CharSequence s, int start, int before, int count){

            }

            @Override
            public void afterTextChanged (Editable s){

                String name = edtName.getText ().toString ().trim ();
                if (name.length () > 0){
                    edtName.setBackgroundResource (R.drawable.correct_border);
                }
            }
        });

        edtEmail.addTextChangedListener (new TextWatcher (){
            @Override
            public void beforeTextChanged (CharSequence s, int start, int count, int after){

            }

            @Override
            public void onTextChanged (CharSequence s, int start, int before, int count){

            }

            @Override
            public void afterTextChanged (Editable s){

                String email = edtEmail.getText ().toString ().trim ();
                if (email.length () > 0){
                    edtEmail.setBackgroundResource (R.drawable.correct_border);
                }
            }
        });

        edtPhone.addTextChangedListener (new TextWatcher (){
            @Override
            public void beforeTextChanged (CharSequence s, int start, int count, int after){

            }

            @Override
            public void onTextChanged (CharSequence s, int start, int before, int count){

            }

            @Override
            public void afterTextChanged (Editable s){
                String phone = edtPhone.getText ().toString ().trim ();
                if (phone.length () > 0){
                    edtPhone.setBackgroundResource (R.drawable.correct_border);
                }
            }
        });

        edtAdults.addTextChangedListener (new TextWatcher (){
            @Override
            public void beforeTextChanged (CharSequence s, int start, int count, int after){

            }

            @Override
            public void onTextChanged (CharSequence s, int start, int before, int count){

            }

            @Override
            public void afterTextChanged (Editable s){

                String adults = edtAdults.getText ().toString ().trim ();
                if (adults.length () > 0){
                    edtAdults.setBackgroundResource (R.drawable.correct_border);
                    edtInfants.setEnabled (true);
                    requestAddGuest.setNoOfAdults (adults);
                } else{
                    edtInfants.setEnabled (false);
                    requestAddGuest.setNoOfAdults ("0");
                }
            }
        });

        edtChildrens.addTextChangedListener (new TextWatcher (){
            @Override
            public void beforeTextChanged (CharSequence s, int start, int count, int after){

            }

            @Override
            public void onTextChanged (CharSequence s, int start, int before, int count){

            }

            @Override
            public void afterTextChanged (Editable s){

                String childrens = edtChildrens.getText ().toString ().trim ();
                if (childrens.length () > 0){
                    edtChildrens.setBackgroundResource (R.drawable.correct_border);
                    requestAddGuest.setNoOfChildren (childrens);
                }else{
                    requestAddGuest.setNoOfChildren ("0");
                }
            }
        });

        edtInfants.addTextChangedListener (new TextWatcher (){
            @Override
            public void beforeTextChanged (CharSequence s, int start, int count, int after){

            }

            @Override
            public void onTextChanged (CharSequence s, int start, int before, int count){

            }

            @Override
            public void afterTextChanged (Editable s){

                String infants = edtInfants.getText ().toString ().trim ();
                if (infants.length () > 0){
                    edtInfants.setBackgroundResource (R.drawable.correct_border);
                    requestAddGuest.setNoOfInfants (infants);
                }else{
                    requestAddGuest.setNoOfInfants ("0");
                }
            }
        });



        btnAddMe.setOnClickListener (new View.OnClickListener (){
            @Override
            public void onClick (View v){

                if (Connection.checkConnectionStatus (activity) > 0){
                    requestAddGuest.setStatus ("CHECKIN");
                    requestAddGuest.setOrganizationID (orgId);
                    requestAddGuest.setNote ("test");

                    if (chkReceive.isChecked ())
                        requestAddGuest.setOptin ("true");
                    else
                        requestAddGuest.setOptin ("false");

                    if (radioEmail.isChecked () && validateEmailPhone){
                        requestAddGuest.setPrefType ("email");
                        requestAddGuest.setEmail (edtEmail.getText ().toString ());
                        requestAddGuest.setSms ("");
                    } else if (radioPhone.isChecked () && validateEmailPhone){
                        String digits = edtPhone.getText ().toString ().replaceAll ("[^0-9.]", "");
                        requestAddGuest.setPrefType ("SMS");
                        requestAddGuest.setSms (digits);
                        requestAddGuest.setEmail ("");
                    }

                    requestAddGuest.setName (edtName.getText ().toString ());

                    if (listSeatPref.size () > 0){
                        for (Seatpref seat : listSeatPref){
                            requestAddGuest.getGuestPreferences ().add (new GuestPreference (seat.getPrefValueId (), seat.getPrefValue ()));
                        }
                    }

                    if (requestAddGuest.getName ().equalsIgnoreCase ("") && (requestAddGuest.getSms ().equalsIgnoreCase ("") && requestAddGuest.getEmail ().equalsIgnoreCase ("")) && requestAddGuest.getNoOfPeople ().equalsIgnoreCase ("0") && requestAddGuest.getNoOfAdults ().equalsIgnoreCase ("0")){
                        edtName.setBackgroundResource (R.drawable.wrong_border);
                        edtName.requestFocus ();
                        if (validateEmailPhone){
                            edtEmail.setBackgroundResource (R.drawable.wrong_border);
                            edtPhone.setBackgroundResource (R.drawable.wrong_border);
                        }
                        edtAdults.setBackgroundResource (R.drawable.wrong_border);
                        spnrPeople.setBackgroundResource (R.drawable.wrong_border);

                    } else if (requestAddGuest.getName ().equalsIgnoreCase ("")){
                        edtName.setBackgroundResource (R.drawable.wrong_border);
                        edtName.requestFocus ();

                    } else if (requestAddGuest.getSms ().equalsIgnoreCase ("") && requestAddGuest.getEmail ().equalsIgnoreCase ("") && validateEmailPhone){
                        edtEmail.setBackgroundResource (R.drawable.wrong_border);
                        edtPhone.setBackgroundResource (R.drawable.wrong_border);
                        if(radioPhone.isChecked ()){
                            edtPhone.requestFocus ();
                        }else{
                            edtEmail.requestFocus ();
                        }

                    } else if (requestAddGuest.getSms ().equalsIgnoreCase ("") && !Validation.emailValidator (requestAddGuest.getEmail ()) && validateEmailPhone){
                        edtEmail.setBackgroundResource (R.drawable.wrong_border);
                        edtEmail.requestFocus ();

                    } else if (!Validation.phoneValidator (edtPhone.getText ().toString ().trim ()) && requestAddGuest.getEmail ().toString ().equalsIgnoreCase ("") && validateEmailPhone){
                        edtPhone.setBackgroundResource (R.drawable.wrong_border);
                        edtPhone.requestFocus ();

                    }else if (edtAdults.getText ().toString ().equalsIgnoreCase ("")){
                        edtAdults.setBackgroundResource (R.drawable.wrong_border);
                        edtAdults.requestFocus ();
                    }
                    /*else if (requestAddGuest.getNoOfPeople ().equalsIgnoreCase ("0")){
                        spnrPeople.setBackgroundResource (R.drawable.wrong_border);

                    } else if (requestAddGuest.getGuestPreferences ().size () < 1){
                        CustomDialog.showAlertDialog (activity, activity.getString (R.string.kyobee), activity.getString (R.string.please_select));
                    }*/ else{
                        edtName.setBackgroundResource (R.drawable.correct_border);
                        edtEmail.setBackgroundResource (R.drawable.correct_border);
                        edtPhone.setBackgroundResource (R.drawable.correct_border);
                        spnrPeople.setBackgroundResource (R.drawable.correct_border);
                        edtAdults.setBackgroundResource (R.drawable.correct_border);

                        // Gson gson = new GsonBuilder ().serializeNulls ().create ();
                        // String json = gson.toJson (requestAddGuest);
                        // Log.d (LOGTAG, "JSON " + json);

                        txtPoor.setVisibility (View.GONE);

                        int noOfPeople=Integer.parseInt (requestAddGuest.getNoOfAdults ())+Integer.parseInt (requestAddGuest.getNoOfChildren ())+Integer.parseInt (requestAddGuest.getNoOfInfants ());
                        requestAddGuest.setNoOfPeople (String.valueOf (noOfPeople));
                        callAddGuest (txtPoor, alertDialog);
                        //alertDialog.dismiss ();
                    }
                } else{
                    Utils.noInternet (activity);
                }
            }
        });

        imgCancel.setOnClickListener (new View.OnClickListener (){
            @Override
            public void onClick (View v){
                alertDialog.dismiss ();
            }
        });


        dismissPopUp (alertDialog);
    }

    private void dismissPopUp (final AlertDialog alert){
        alert.setOnKeyListener (new DialogInterface.OnKeyListener (){
            @Override
            public boolean onKey (DialogInterface dialogInterface, int keyCode, KeyEvent keyEvent){
                if (keyCode == KeyEvent.KEYCODE_BACK){
                    alert.dismiss ();
                    return true;
                }
                return false;
            }
        });
    }

    public void popUpResponse (ResponseAddGuest responseAddGuest){

        DisplayMetrics displayMetrics = new DisplayMetrics ();
        getWindowManager ().getDefaultDisplay ().getMetrics (displayMetrics);
        int width = 0;//displayMetrics.widthPixels;
        if (Utils.getScreenOrientation (activity) == 1){
            width = displayMetrics.widthPixels;//displayMetrics.heightPixels;
        } else{
            width = displayMetrics.widthPixels;
        }

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder (activity);
        LayoutInflater layout = (LayoutInflater) getSystemService (Context.LAYOUT_INFLATER_SERVICE);
        final View view = layout.inflate (R.layout.popup_response_add_guest, (ViewGroup) findViewById (R.id.popup_response_add_guest));
        alertDialogBuilder.setView (view);
        alertDialogBuilder.setCancelable (false);

        // create alert dialog
        alertDialog = alertDialogBuilder.create ();
        alertDialog.show ();

        alertDialog.getWindow ().setBackgroundDrawable (new ColorDrawable (Color.WHITE));

        Window window = alertDialog.getWindow ();
        window.setGravity (Gravity.CENTER);
        window.setLayout ((width * 95) / 100, WindowManager.LayoutParams.WRAP_CONTENT);

        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams ((width * 95) / 100, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.gravity = Gravity.CENTER;

        LinearLayout linearPopup = (LinearLayout) view.findViewById (R.id.popup_response_add_guest);
        //linearPopup.setLayoutParams (layoutParams);

        CustomTextViewBold txtNumber = (CustomTextViewBold) view.findViewById (R.id.txtNumber);
        txtNumber.setText (String.valueOf ("#" + responseAddGuest.getServiceResultAdd ().getGuestRank ()));

        CustomButtonRegular btnOk = (CustomButtonRegular) view.findViewById (R.id.btnOk);

        Login login = GSONGetSet.getLogin ();
        if (login.getClientBase ().equalsIgnoreCase (General.ADMIN)){
            linearPopup.setBackgroundColor (ContextCompat.getColor (activity, R.color.colorRed));
            txtNumber.setTextColor (ContextCompat.getColor (activity, R.color.colorRed));
            btnOk.setBackgroundColor (ContextCompat.getColor (activity, R.color.colorRed));
        } else if (login.getClientBase ().equalsIgnoreCase (General.ADVANTECH)){
            linearPopup.setBackgroundColor (ContextCompat.getColor (activity, R.color.colorBlue));
            txtNumber.setTextColor (ContextCompat.getColor (activity, R.color.colorBlue));
            btnOk.setBackgroundColor (ContextCompat.getColor (activity, R.color.colorBlue));
        }


        btnOk.setOnClickListener (new View.OnClickListener (){
            @Override
            public void onClick (View v){
                alertDialog.dismiss ();
            }
        });

        dismissPopUp (alertDialog);

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

    public void callWaiting (boolean progress){
        if (progress)
            CustomDialog.showProgressDialog (activity, "", getString (R.string.please_wait));
        mAPIService = General.getClient ().create (APIService.class);
        Call<Display> waiting = mAPIService.waiting (orgId);
        waiting.enqueue (new Callback<Display> (){
            @Override
            public void onResponse (Call<Display> call, Response<Display> response){
                CustomDialog.dismissProgressDialog ();
                Log.d (LOGTAG, response.toString ());

                Display display = response.body ();
                if (display.getStatus ().equalsIgnoreCase (General.SUCCESS)){
                    Kyobee.getInstance ().setDisplay (display);
                    displayGuest (display);
                } else{
                    CustomDialog.showAlertDialog (activity, "Error", display.getErrorDescription ());
                }
            }

            @Override
            public void onFailure (Call<Display> call, Throwable t){
                CustomDialog.dismissProgressDialog ();
                if (t instanceof UnknownHostException){
                    Utils.noInternet (activity);
                }
            }
        });
        Log.d (LOGTAG, "Guest Url " + waiting.request ().url ());
    }

    public void updateGuestThread (final Display disp){
        runOnUiThread (new Runnable (){
            @Override
            public void run (){
                displayGuest (disp);
            }
        });
    }

    public void displayGuest (Display display){
        txtWaiting.setText ("( " + display.getServiceResultGuest ().getORGGUESTCOUNT () + " )");
        txtNumber.setText ("#" + display.getServiceResultGuest ().getGUESTRANKMIN ());
        String time = Utils.timeConversion (display.getServiceResultGuest ().getORGTOTALWAITTIME ());
        txtTime.setText (time);
        Utils.setImageWithCatch (activity, display.getServiceResultGuest ().getImageOrgPath (), imgLogo);
    }

    public void callAddGuest (final CustomTextViewRegular txtPoorConn, final AlertDialog alertDialog){
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
    }

    @Override
    protected void onResume (){
        super.onResume ();
        Kyobee.getInstance ().setRealTimeInterface (this);
        Kyobee.getInstance ().setConnectivityListener (this);
    }

    @Override
    public void onConfigurationChanged (Configuration newConfig){
        super.onConfigurationChanged (newConfig);
        updateDialogSize ();
    }

    @Override
    public void onReceivedResponse (String channel, String message){
        Log.d (LOGTAG, "Message Json " + message);
        try{
            Gson gson = new GsonBuilder ().serializeNulls ().create ();
            ChannelMessage channelMessage = gson.fromJson (message, ChannelMessage.class);
            // sometimes message coming with m and ch object so if op is null
            // we are get message from channel message m object.
            if (channelMessage.getOP () == null){
                message = channelMessage.getM ();
            }
            JSONObject jsonObject = new JSONObject (message);
            String op = jsonObject.getString (General.OP);
            if (op.equalsIgnoreCase (General.ADD)){
                Display disp = Kyobee.getInstance ().getDisplay ();
                Add add = gson.fromJson (message, Add.class);
                if (disp.getServiceResultGuest ().getORGGUESTCOUNT ().equalsIgnoreCase ("0") && disp.getServiceResultGuest ().getGUESTRANKMIN ().equalsIgnoreCase ("0")){
                    disp.getServiceResultGuest ().setGUESTRANKMIN (String.valueOf (add.getGuestRank ()));
                    disp.getServiceResultGuest ().setORGGUESTCOUNT (String.valueOf (add.getTotalPartiesWaiting ()));
                    disp.getServiceResultGuest ().setORGTOTALWAITTIME (String.valueOf (add.getORGTOTALWAITTIME ()));
                } else{
                    disp.getServiceResultGuest ().setORGGUESTCOUNT (String.valueOf (add.getTotalPartiesWaiting ()));
                    disp.getServiceResultGuest ().setORGTOTALWAITTIME (String.valueOf (add.getORGTOTALWAITTIME ()));
                }
                Kyobee.getInstance ().setDisplay (disp);
                updateGuestThread (disp);
            } else if (op.equalsIgnoreCase (General.DEL)){
                callWaiting (false);
            } else if (op.equalsIgnoreCase (General.UPDATE)){
                update (message);
            } else if (op.equalsIgnoreCase (General.MARK_AS_SEATED)){
                update (message);
            } else if (op.equalsIgnoreCase (General.UPDATE_GUEST_INFO)){
                callWaiting (false);
            } else if (op.equalsIgnoreCase (General.NOT_PRESENT)){
                callWaiting (false);
            } else if (op.equalsIgnoreCase (General.IN_COMPLETE)){
                callWaiting (false);
            }
        } catch (JsonSyntaxException e){
            Log.d (LOGTAG, "JsonSyntaxException" + e.getMessage ());

        } catch (JSONException e){
            Log.d (LOGTAG, "JSON Exception" + e.getMessage ());
        }
    }

    @Override
    public void onSubscribeChannel (OrtcClient sender, String channel){
        Log.d (LOGTAG, "Subscribe Sender - " + sender + " Channel - " + channel);
    }

    public void update (String message){
        Gson gson = new GsonBuilder ().serializeNulls ().create ();
        Display disp = Kyobee.getInstance ().getDisplay ();
        Upd upd = gson.fromJson (message, Upd.class);
        disp.getServiceResultGuest ().setGUESTRANKMIN (String.valueOf (upd.getnOWSERVINGGUESTID ()));
        disp.getServiceResultGuest ().setORGGUESTCOUNT (String.valueOf (upd.getoRGGUESTCOUNT ()));
        disp.getServiceResultGuest ().setORGTOTALWAITTIME (String.valueOf (upd.getoRGTOTALWAITTIME ()));
        Kyobee.getInstance ().setDisplay (disp);
        updateGuestThread (disp);
    }

    public class GuestHorizontalListAdapter extends BaseAdapter{

        public List<Seatpref> listSeating;
        LayoutInflater inflater;
        private String LOGTAG = GuestHorizontalListAdapter.class.getSimpleName ();

        public GuestHorizontalListAdapter (){
            listSeating = login.getSeatpref ();
            inflater = (LayoutInflater) getSystemService (Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount (){
            return listSeating.size ();
        }

        @Override
        public Object getItem (int position){
            return listSeating.get (position);
        }

        @Override
        public long getItemId (int position){
            return position;
        }

        @Override
        public View getView (int position, View convertView, ViewGroup parent){

            final CustomHolder holder;
            final Seatpref seatpref = listSeating.get (position);
            if (convertView == null){
                convertView = inflater.inflate (R.layout.list_preference, parent, false);
                holder = new CustomHolder ();
                holder.chkSeating = (CheckBox) convertView.findViewById (R.id.chkSeating);
                holder.txtSeating = (CustomTextViewRegular) convertView.findViewById (R.id.txtSeating);
                convertView.setTag (holder);
            } else{
                holder = (CustomHolder) convertView.getTag ();
            }

            holder.txtSeating.setText (seatpref.getPrefValue ());

            return convertView;
        }

        class CustomHolder{
            CheckBox chkSeating;
            CustomTextViewRegular txtSeating;
        }
    }

}
