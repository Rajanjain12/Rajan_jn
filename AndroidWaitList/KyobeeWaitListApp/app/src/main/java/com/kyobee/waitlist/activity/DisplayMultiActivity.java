package com.kyobee.waitlist.activity;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
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
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.kyobee.waitlist.Kyobee;
import com.kyobee.waitlist.R;
import com.kyobee.waitlist.customcontrol.CustomButtonRegular;
import com.kyobee.waitlist.customcontrol.CustomDialog;
import com.kyobee.waitlist.customcontrol.CustomEditTextRegular;
import com.kyobee.waitlist.customcontrol.CustomTextViewRegular;
import com.kyobee.waitlist.customcontrol.CustomTextViewSemiBold;
import com.kyobee.waitlist.net.Connection;
import com.kyobee.waitlist.net.ConnectivityReceiver;
import com.kyobee.waitlist.pojo.APIService;
import com.kyobee.waitlist.pojo.ChannelMessage;
import com.kyobee.waitlist.pojo.Login;
import com.kyobee.waitlist.pojo.MultiColumnSession;
import com.kyobee.waitlist.pojo.PartyType;
import com.kyobee.waitlist.pojo.Response.ResponseGen;
import com.kyobee.waitlist.pojo.UpdGuest;
import com.kyobee.waitlist.pojo.UpdateGuest;
import com.kyobee.waitlist.utils.AppInfo;
import com.kyobee.waitlist.utils.GSONGetSet;
import com.kyobee.waitlist.utils.General;
import com.kyobee.waitlist.utils.RealTimePush;
import com.kyobee.waitlist.utils.Utils;
import com.piotrek.customspinner.CustomSpinner;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
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

import static org.apache.commons.lang3.CharEncoding.UTF_8;

public class DisplayMultiActivity extends AppCompatActivity implements RealTimePush.RealTimeListener, ConnectivityReceiver.ConnectivityReceiverListener, UILongPressGestureRecognizer.OnActionListener, UIGestureRecognizerDelegate.Callback{

    private static final int LOGOUT = 1;
    private static final int SETTINGS = 0;
    public static String LOGTAG = DisplayMultiActivity.class.getSimpleName ();
    CustomTextViewRegular txtVersion;
    CustomTextViewSemiBold txtNodata1, txtNodata2, txtNodata3, txtNodata4, txtColumn1, txtColumn2, txtColumn3, txtColumn4;
    CustomTextViewRegular txtParty1, txtParty2, txtParty3, txtParty4;
    CustomTextViewRegular txtGuestName1, txtGuestName2, txtGuestName3, txtGuestName4;
    Activity activity;
    String reqParam = "{\"filters\":null,\"sort\":null,\"sortOrder\":null,\"pageSize\":500,\"pageNo\":1}";
    ImageView imgLogo, imgRefresh;
    String imgPath = "";
    List<ResponseGen.Record> listRecords = new ArrayList<> ();
    List<ResponseGen.Record> listRecords30 = new ArrayList<> ();
    List<ResponseGen.Record> listRecords60 = new ArrayList<> ();
    List<ResponseGen.Record> listRecords90 = new ArrayList<> ();
    List<ResponseGen.Record> listRecords120 = new ArrayList<> ();
    RecyclerView recycle30, recycle60, recycle90, recycle120;
    DisplayRCLAdapter displayRCLAdapter;
    Login login;
    RealTimePush realTimePush;
    RelativeLayout reltiveLogout, relativeSettings;
    //Declare timer
    CountDownTimer cTimer = null;
    int rowLimit = 25;
    AlertDialog alertDialog;

    LinearLayout linearColumn1, linearColumn2, linearColumn3, linearColumn4;

    UIGestureRecognizerDelegate delegate;
    int operation = -1;
    private APIService mAPIService;

    @Override
    public void onCreate (Bundle savedInstanceState){
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_display_multi);
        activity = this;

        login = GSONGetSet.getLogin ();
        realTimePush = new RealTimePush (activity, login);

        imgLogo = (ImageView) findViewById (R.id.imgLogo);
        imgRefresh = (ImageView) findViewById (R.id.imgRefresh);

        linearColumn1 = (LinearLayout) findViewById (R.id.linearColumn1);
        linearColumn2 = (LinearLayout) findViewById (R.id.linearColumn2);
        linearColumn3 = (LinearLayout) findViewById (R.id.linearColumn3);
        linearColumn4 = (LinearLayout) findViewById (R.id.linearColumn4);

        recycle30 = (RecyclerView) findViewById (R.id.recycle30);
        recycle60 = (RecyclerView) findViewById (R.id.recycle60);
        recycle90 = (RecyclerView) findViewById (R.id.recycle90);
        recycle120 = (RecyclerView) findViewById (R.id.recycle120);

        reltiveLogout = (RelativeLayout) findViewById (R.id.reltiveLogout);
        relativeSettings = (RelativeLayout) findViewById (R.id.reltiveSettings);
        RecyclerView.LayoutManager layoutManager30 = new LinearLayoutManager (activity);
        RecyclerView.LayoutManager layoutManager60 = new LinearLayoutManager (activity);
        RecyclerView.LayoutManager layoutManager90 = new LinearLayoutManager (activity);
        RecyclerView.LayoutManager layoutManager120 = new LinearLayoutManager (activity);


        recycle30.setLayoutManager (layoutManager30);
        recycle30.setItemAnimator (new DefaultItemAnimator ());

        recycle60.setLayoutManager (layoutManager60);
        recycle60.setItemAnimator (new DefaultItemAnimator ());

        recycle90.setLayoutManager (layoutManager90);
        recycle90.setItemAnimator (new DefaultItemAnimator ());


        recycle120.setLayoutManager (layoutManager120);
        recycle120.setItemAnimator (new DefaultItemAnimator ());


        txtNodata4 = (CustomTextViewSemiBold) findViewById (R.id.txtNodata4);
        txtNodata3 = (CustomTextViewSemiBold) findViewById (R.id.txtNodata3);
        txtNodata2 = (CustomTextViewSemiBold) findViewById (R.id.txtNodata2);
        txtNodata1 = (CustomTextViewSemiBold) findViewById (R.id.txtNodata1);

        txtColumn1 = (CustomTextViewSemiBold) findViewById (R.id.txtColumn1);
        txtColumn2 = (CustomTextViewSemiBold) findViewById (R.id.txtColumn2);
        txtColumn3 = (CustomTextViewSemiBold) findViewById (R.id.txtColumn3);
        txtColumn4 = (CustomTextViewSemiBold) findViewById (R.id.txtColumn4);

        txtColumn1.setEllipsize (TextUtils.TruncateAt.END);
        txtColumn1.setMaxLines (1);

        txtColumn2.setEllipsize (TextUtils.TruncateAt.END);
        txtColumn2.setMaxLines (1);

        txtColumn3.setEllipsize (TextUtils.TruncateAt.END);
        txtColumn3.setMaxLines (1);

        txtColumn4.setEllipsize (TextUtils.TruncateAt.END);
        txtColumn4.setMaxLines (1);

        txtParty1 = (CustomTextViewRegular) findViewById (R.id.txtParty1);
        txtParty2 = (CustomTextViewRegular) findViewById (R.id.txtParty2);
        txtParty3 = (CustomTextViewRegular) findViewById (R.id.txtParty3);
        txtParty4 = (CustomTextViewRegular) findViewById (R.id.txtParty4);

        txtGuestName1 = (CustomTextViewRegular) findViewById (R.id.txtGuestName1);
        txtGuestName2 = (CustomTextViewRegular) findViewById (R.id.txtGuestName2);
        txtGuestName3 = (CustomTextViewRegular) findViewById (R.id.txtGuestName3);
        txtGuestName4 = (CustomTextViewRegular) findViewById (R.id.txtGuestName4);


        txtVersion = (CustomTextViewRegular) findViewById (R.id.txtVersion);
        String version = getString (R.string.copyright) + " " + AppInfo.getAppVersionName (this); //+ " " + Utils.getCurrentDateTime ();
        txtVersion.setText (version);

        imgPath = General.IMAGE + login.getLogofileName ();

        Glide.with (activity).load (imgPath).diskCacheStrategy (DiskCacheStrategy.ALL).into (imgLogo);
        showHideColumn ();

        delegate = new UIGestureRecognizerDelegate (this);
        UILongPressGestureRecognizer longPress = new UILongPressGestureRecognizer (this);
        longPress.setTag ("double-long-press");
        longPress.setNumberOfTouchesRequired (2);
        longPress.setNumberOfTapsRequired (0);
        //longPress.setNumberOfTouchesRequired (1);
        //longPress.setNumberOfTapsRequired (1);
        longPress.setMinimumPressDuration (2000);
        longPress.setActionListener (this);
        delegate.addGestureRecognizer (longPress);

        reltiveLogout.setOnTouchListener (new View.OnTouchListener (){
            @Override
            public boolean onTouch (View v, MotionEvent event){
                operation = LOGOUT;
                return delegate.onTouchEvent (v, event);
            }
        });

        relativeSettings.setOnTouchListener (new View.OnTouchListener (){
            @Override
            public boolean onTouch (View v, MotionEvent event){
                operation = SETTINGS;
                return delegate.onTouchEvent (v, event);
            }
        });

        if (Connection.checkConnectionStatus (activity) > 0){
            callCheckInUsers (login.getOrgId (), true);
        } else{
            Utils.noInternet (activity);
        }

        //   popUpSettings ();

    }

    // ui gesture recognizer event callback
    @Override
    public void onGestureRecognized (@NonNull final UIGestureRecognizer recognizer){

        if (operation == LOGOUT){
            Login logIn = GSONGetSet.getLogin ();
            realTimePush.disConnect (logIn);
            Kyobee.getInstance ().logout ();
            startActivity (new Intent (activity, LoginActivity.class));
            finish ();
        } else if (operation == SETTINGS){
            popUpCheckinDisplay ();
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
    public boolean shouldReceiveTouch (final UIGestureRecognizer recognizer){
        Log.d (LOGTAG, "shouldReceiveTouch");
        return true;
    }

    public void callCheckInUsers (String orgId, boolean progress){
        final MultiColumnSession multiColumnSession = Kyobee.getInstance ().getMultiColumnSession ();
        if (multiColumnSession != null){
            multiColumnSession.setPartyType (PartyType.COMMON);
            rowLimit = multiColumnSession.getTotalRows ();
        }

        listRecords.clear ();
        String totalPage = String.valueOf (rowLimit * 2);
        reqParam = "{\"filters\":null,\"sort\":null,\"sortOrder\":null,\"pageSize\":" + totalPage + ",\"pageNo\":1}";
        if (progress)
            CustomDialog.showProgressDialog (activity, "", getString (R.string.please_wait));
        try{
            String query = URLEncoder.encode (reqParam, UTF_8);
            mAPIService = General.getClient ().create (APIService.class);
            Call<ResponseGen> genCheckInUsers = mAPIService.genCheckInUsers (orgId, multiColumnSession.getPartyType (), query);
            genCheckInUsers.enqueue (new Callback<ResponseGen> (){
                @Override
                public void onResponse (Call<ResponseGen> call, Response<ResponseGen> response){
                    CustomDialog.dismissProgressDialog ();
                    Log.d (LOGTAG, response.toString ());
                    ResponseGen responseGen = response.body ();
                    if (responseGen.getStatus ().equalsIgnoreCase (General.SUCCESS)){
                        listRecords.clear ();
                        listRecords.addAll (responseGen.getServiceResult ().getRecords ());
                        callAdapter ();
                    } else{
                        CustomDialog.showAlertDialog (DisplayMultiActivity.this, "Error", responseGen.getErrorDescription ());
                    }
                }

                @Override
                public void onFailure (Call<ResponseGen> call, Throwable t){
                    CustomDialog.dismissProgressDialog ();
                    if (t instanceof UnknownHostException){
                        Utils.noInternet (activity);
                    }
                }
            });
        } catch (Exception e){

        }
    }

    @Override
    protected void onResume (){
        super.onResume ();
        Kyobee.getInstance ().setRealTimeInterface (this);
        Kyobee.getInstance ().setConnectivityListener (this);
    }

    @Override
    public void onNetworkConnectionChanged (boolean isConnected){
        if (isConnected){
            callCheckInUsers (login.getOrgId (), false);
            Glide.with (activity).load (imgPath).diskCacheStrategy (DiskCacheStrategy.ALL).into (imgLogo);
        }
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
    public void onReceivedResponse (String channel, String message){
        try{
            Gson gson = new GsonBuilder ().serializeNulls ().create ();
            Log.d (LOGTAG, "Message Json " + message);
            ChannelMessage channelMessage = gson.fromJson (message, ChannelMessage.class);
            if (channelMessage.getOP () == null){
                message = channelMessage.getM ();
            }
            JSONObject jsonObject = new JSONObject (message);
            String op = jsonObject.getString (General.OP);
            if (op.equalsIgnoreCase (General.ADD)){
                callCheckInUsers (login.getOrgId (), false);
            } else if (op.equalsIgnoreCase (General.DEL)){
                callCheckInUsers (login.getOrgId (), false);
            } else if (op.equalsIgnoreCase (General.UPDATE_GUEST_INFO)){
                UpdateGuest updateGuest = gson.fromJson (message, UpdateGuest.class);
                UpdGuest updGuest = updateGuest.getUpdguest ();
                for (int i = 0; i < listRecords.size (); i++){
                    ResponseGen.Record record = listRecords.get (i);
                    if (record.getGuestID () == updGuest.getGuestID ()){
                        record.setName (updGuest.getName ());
                        //record.setSms (updGuest.getSms ());
                        //record.setPrefType (updGuest.getPrefType ());
                        //record.setEmail(updGuest.getEmail ());
                        listRecords.set (i, record);
                        break;
                    }
                }
                updateAdapter ();
                Log.d (LOGTAG, General.UPDATE_GUEST_INFO + " - - " + updateGuest.getUpdguest ().getName ());
            } else if (op.equalsIgnoreCase (General.UPDATE)){
                listRecords.clear ();
                callCheckInUsers (login.getOrgId (), false);
            } else if (op.equalsIgnoreCase (General.NOT_PRESENT)){
                listRecords.clear ();
                //NotPresent notPresent= gson.fromJson (message, NotPresent.class);
                callCheckInUsers (login.getOrgId (), false);
            } else if (op.equalsIgnoreCase (General.IN_COMPLETE)){
                listRecords.clear ();
                callCheckInUsers (login.getOrgId (), false);
            } else if (op.equalsIgnoreCase (General.MARK_AS_SEATED)){
                UpdateGuest updateGuest = gson.fromJson (message, UpdateGuest.class);
                UpdGuest updGuest = updateGuest.getUpdguest ();
                for (int i = 0; i < listRecords.size (); i++){
                    ResponseGen.Record record = listRecords.get (i);
                    if (record.getGuestID () == updGuest.getGuestID ()){
                        listRecords.remove (i);
                        break;
                    }
                }
                updateAdapter ();
            }
        } catch (JsonSyntaxException e){
            Log.d (LOGTAG, "JsonSyntaxException" + e.getMessage ());

        } catch (JSONException e){
            Log.d (LOGTAG, "JSON Exception" + e.getMessage ());
        }

    }

    public void updateAdapter (){
        runOnUiThread (new Runnable (){
            @Override
            public void run (){
                callAdapter ();
            }
        });
    }

    public void showHideColumn (){
        MultiColumnSession multiColumnSession = Kyobee.getInstance ().getMultiColumnSession ();
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams (LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.weight = 1.0f;
        if (multiColumnSession.isParty ()){
            params.setMargins (0, 0, 0, 0);
        } else{
            params.setMargins (10, 0, 0, 0);
        }

        if (multiColumnSession == null){
            // multiColumnSession=new MultiColumnSession ();
            // Kyobee.getInstance ().setMultiColumnSession (multiColumnSession);
        } else if (multiColumnSession.getTotalColumn () == 1){
            linearColumn2.setVisibility (View.GONE);
            linearColumn3.setVisibility (View.GONE);
            linearColumn4.setVisibility (View.GONE);
            txtColumn1.setText (multiColumnSession.getColumnName1 ());
            txtGuestName1.setLayoutParams (params);

        } else if (multiColumnSession.getTotalColumn () == 2){
           /* linearColumn2.setVisibility (View.VISIBLE);
            linearColumn3.setVisibility (View.GONE);
            linearColumn4.setVisibility (View.GONE);
            txtColumn1.setText (multiColumnSession.getColumnName1 ());
            txtColumn2.setText (multiColumnSession.getColumnName2 ());

            txtGuestName1.setLayoutParams (params);
            txtGuestName2.setLayoutParams (params);*/

            linearColumn2.setVisibility (View.GONE);
            linearColumn3.setVisibility (View.GONE);
            linearColumn4.setVisibility (View.GONE);
            txtColumn1.setText (multiColumnSession.getColumnName1 ());
            txtGuestName1.setLayoutParams (params);


        } else if (multiColumnSession.getTotalColumn () == 3){
            /*linearColumn2.setVisibility (View.VISIBLE);
            linearColumn3.setVisibility (View.VISIBLE);
            linearColumn4.setVisibility (View.GONE);
            txtColumn1.setText (multiColumnSession.getColumnName1 ());
            txtColumn2.setText (multiColumnSession.getColumnName2 ());
            txtColumn3.setText (multiColumnSession.getColumnName3 ());

            txtGuestName1.setLayoutParams (params);
            txtGuestName2.setLayoutParams (params);
            txtGuestName3.setLayoutParams (params);*/

            linearColumn2.setVisibility (View.GONE);
            linearColumn3.setVisibility (View.GONE);
            linearColumn4.setVisibility (View.GONE);
            txtColumn1.setText (multiColumnSession.getColumnName1 ());
            txtGuestName1.setLayoutParams (params);


        } else if (multiColumnSession.getTotalColumn () == 4){

            linearColumn2.setVisibility (View.VISIBLE);
            linearColumn3.setVisibility (View.GONE);
            linearColumn4.setVisibility (View.GONE);
            txtColumn1.setText (multiColumnSession.getColumnName1 ());
            txtColumn2.setText (multiColumnSession.getColumnName2 ());

            txtGuestName1.setLayoutParams (params);
            txtGuestName2.setLayoutParams (params);

            /*linearColumn2.setVisibility (View.VISIBLE);
            linearColumn3.setVisibility (View.VISIBLE);
            linearColumn4.setVisibility (View.VISIBLE);
            txtColumn1.setText (multiColumnSession.getColumnName1 ());
            txtColumn2.setText (multiColumnSession.getColumnName2 ());
            txtColumn3.setText (multiColumnSession.getColumnName3 ());
            txtColumn4.setText (multiColumnSession.getColumnName4 ());

            txtGuestName1.setLayoutParams (params);
            txtGuestName2.setLayoutParams (params);
            txtGuestName3.setLayoutParams (params);
            txtGuestName4.setLayoutParams (params);*/
        }

        // For hide and show show party column in header
        if (multiColumnSession.isParty ()){
            txtParty1.setVisibility (View.VISIBLE);
            txtParty2.setVisibility (View.VISIBLE);
            txtParty3.setVisibility (View.VISIBLE);
            txtParty4.setVisibility (View.VISIBLE);
            params = new LinearLayout.LayoutParams (LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.weight = 2.0f;
            txtParty1.setLayoutParams (params);
            txtParty2.setLayoutParams (params);
            txtParty3.setLayoutParams (params);
            txtParty4.setLayoutParams (params);

        } else{
            txtParty1.setVisibility (View.GONE);
            txtParty2.setVisibility (View.GONE);
            txtParty3.setVisibility (View.GONE);
            txtParty4.setVisibility (View.GONE);
        }

        if (multiColumnSession != null){
            rowLimit = multiColumnSession.getTotalRows ();
        }
    }

    public void callAdapter (){
        showHideColumn ();
        MultiColumnSession multiColumnSession = Kyobee.getInstance ().getMultiColumnSession ();
        if (listRecords.size () > 0){
            listRecords30.clear ();
            listRecords60.clear ();
            // listRecords90.clear ();
            // listRecords120.clear ();

            // int rowLimit2 = rowLimit * 2;
            // int rowLimit3 = rowLimit * 3;

            for (int i = 0; i < listRecords.size (); i++){
                ResponseGen.Record record = listRecords.get (i);
                /*if (i < rowLimit){
                    listRecords30.add (record);
                } else if (i < rowLimit2){
                    listRecords60.add (record);
                } else{
                    listRecords90.add (record);
                }*/

                if (multiColumnSession == null){
                    listRecords30.add (record);
                } else{
                    if (multiColumnSession.getPartyType ().equalsIgnoreCase (PartyType.COMMON)){
                        listRecords30.add (record);
                    } else if (multiColumnSession.getPartyType ().equalsIgnoreCase (PartyType.SMALL)){
                        // 0 is for small and 1 is for large
                        if (record.getPartyType () == 0){
                            listRecords30.add (record);
                        }
                    } else if (multiColumnSession.getPartyType ().equalsIgnoreCase (PartyType.LARGE)){

                        // 0 is for small and 1 is for large
                        if (record.getPartyType () == 1){
                            listRecords30.add (record);
                        }

                    } else if (multiColumnSession.getPartyType ().equalsIgnoreCase (PartyType.BOTH)){
                        // 0 is for small and 1 is for large
                        if (record.getPartyType () == 0){
                            listRecords30.add (record);
                        } else if (record.getPartyType () == 1){
                            listRecords60.add (record);
                        }
                    }
                }
            }

            displayRCLAdapter = new DisplayRCLAdapter (activity, listRecords30);
            recycle30.setAdapter (displayRCLAdapter);
            /*if (listRecords30.size () > 0){
                txtNodata1.setVisibility (View.GONE);
            } else{
                txtNodata1.setVisibility (View.VISIBLE);
            }*/

            displayRCLAdapter = new DisplayRCLAdapter (activity, listRecords60);
            recycle60.setAdapter (displayRCLAdapter);

          /*  displayRCLAdapter = new DisplayRCLAdapter (activity, listRecords90);
            recycle90.setAdapter (displayRCLAdapter);

            displayRCLAdapter = new DisplayRCLAdapter (activity, listRecords120);
            recycle120.setAdapter (displayRCLAdapter);
*/
            displayRCLAdapter.notifyDataSetChanged ();
        }
    }

    @Override
    public void onSubscribeChannel (OrtcClient sender, String channel){
        Log.d (LOGTAG, "Subscribe Sender - " + sender + " Channel - " + channel);
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

    public void popUpCheckinDisplay (){
        DisplayMetrics displayMetrics = new DisplayMetrics ();
        getWindowManager ().getDefaultDisplay ().getMetrics (displayMetrics);
        int width = 0;//displayMetrics.widthPixels;
        if (Utils.getScreenOrientation (activity) == 1){
            width = (displayMetrics.widthPixels * 80) / 100;//displayMetrics.heightPixels;
        } else{
            width = (displayMetrics.widthPixels * 95) / 100;
        }

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder (activity);
        LayoutInflater layout = (LayoutInflater) getSystemService (Context.LAYOUT_INFLATER_SERVICE);
        final View view = layout.inflate (R.layout.popup_choose_operation, (ViewGroup) findViewById (R.id.popUpGuestMode));
        alertDialogBuilder.setView (view);
        alertDialogBuilder.setCancelable (true);
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

        final CustomButtonRegular btnBack = (CustomButtonRegular) view.findViewById (R.id.btnBack);
        final CustomButtonRegular btnRefresh = (CustomButtonRegular) view.findViewById (R.id.btnRefresh);
        final CustomButtonRegular btnSettings = (CustomButtonRegular) view.findViewById (R.id.btnSettings);

        btnBack.setOnClickListener (new View.OnClickListener (){
            @Override
            public void onClick (View v){
                alertDialog.dismiss ();
                startActivity (new Intent (activity, LoginActivity.class).putExtra (General.OP, Kyobee.getInstance ().getLoginMode ()));
                finish ();
            }
        });

        btnRefresh.setOnClickListener (new View.OnClickListener (){
            @Override
            public void onClick (View v){
                alertDialog.dismiss ();
                callCheckInUsers (login.getOrgId (), true);
            }
        });

        btnSettings.setOnClickListener (new View.OnClickListener (){
            @Override
            public void onClick (View v){
                alertDialog.dismiss ();
                popUpSettings ();
            }
        });

        dismissPopUp (alertDialog);
    }

    public void popUpSettings (){
        final MultiColumnSession multiColumnSession = Kyobee.getInstance ().getMultiColumnSession ();//new MultiColumnSession ();
        DisplayMetrics displayMetrics = new DisplayMetrics ();
        getWindowManager ().getDefaultDisplay ().getMetrics (displayMetrics);
        int width = 0;//displayMetrics.widthPixels;
        if (Utils.getScreenOrientation (activity) == 1){
            width = (displayMetrics.widthPixels * 80) / 100;//displayMetrics.heightPixels;
        } else{
            width = (displayMetrics.widthPixels * 95) / 100;
        }

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder (activity);
        LayoutInflater layout = (LayoutInflater) getSystemService (Context.LAYOUT_INFLATER_SERVICE);
        final View view = layout.inflate (R.layout.popup_settings, (ViewGroup) findViewById (R.id.pop_add_new_guest));
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

        ImageView imgCancel = (ImageView) view.findViewById (R.id.imgCancel);
        final CustomEditTextRegular edtColumnTitle1 = (CustomEditTextRegular) view.findViewById (R.id.edtColumn1);
        final CustomEditTextRegular edtColumnTitle2 = (CustomEditTextRegular) view.findViewById (R.id.edtColumn2);
        final CustomEditTextRegular edtColumnTitle3 = (CustomEditTextRegular) view.findViewById (R.id.edtColumn3);
        final CustomEditTextRegular edtColumnTitle4 = (CustomEditTextRegular) view.findViewById (R.id.edtColumn4);

        final CustomTextViewRegular txtNotPresent = (CustomTextViewRegular) view.findViewById (R.id.txtNotPresent);
        final CustomTextViewRegular txtIncomplete = (CustomTextViewRegular) view.findViewById (R.id.txtIncomplete);
        final CustomTextViewRegular txtShowPartyNumber = (CustomTextViewRegular) view.findViewById (R.id.txtShowPartyNumber);

        final CustomSpinner spnrColumn = (CustomSpinner) view.findViewById (R.id.spnrColumn);
        final String[] columns = getResources ().getStringArray (R.array.select_waitlist);
        spnrColumn.initializeStringValues (columns, getString (R.string.select_waitlist));
        spnrColumn.setSelection (multiColumnSession.getTotalColumn ());

        final CustomSpinner spnrRow = (CustomSpinner) view.findViewById (R.id.spnrRow);
        final String[] rows = getResources ().getStringArray (R.array.total_row_waitlist);
        spnrRow.initializeStringValues (rows, getString (R.string.total_row_waitlist));
        spnrRow.setSelection (multiColumnSession.getRowPosition ());

        CustomButtonRegular btnOk = (CustomButtonRegular) view.findViewById (R.id.btnOk);
        //        final SwitchCompat switchNotPresent = (SwitchCompat) view.findViewById (R.id.switchNotPresent);
        //        final SwitchCompat switchIncomplete = (SwitchCompat) view.findViewById (R.id.switchIncomplete);

        final CheckBox chkNotPresent = (CheckBox) view.findViewById (R.id.chkNotPresent);
        final CheckBox chkInComplete = (CheckBox) view.findViewById (R.id.chkInComplete);
        final CheckBox chkShowParty = (CheckBox) view.findViewById (R.id.chkShowParty);

        spnrColumn.setOnItemSelectedListener (new AdapterView.OnItemSelectedListener (){
            @Override
            public void onItemSelected (AdapterView<?> parent, View view, int position, long id){
                Log.d (LOGTAG, "" + position);
                spnrColumn.setBackgroundResource (R.drawable.correct_border);
                multiColumnSession.setTotalColumn (position);

                if (position == 1){
                    edtColumnTitle1.setVisibility (View.VISIBLE);
                    edtColumnTitle2.setVisibility (View.GONE);
                    edtColumnTitle3.setVisibility (View.GONE);
                    edtColumnTitle4.setVisibility (View.GONE);
                    spnrRow.setEnabled (true);
                    final String[] temp = getResources ().getStringArray (R.array.total_row_waitlist);
                    spnrRow.initializeStringValues (temp, getString (R.string.total_row_waitlist));
                    spnrRow.setSelection (1);
                    multiColumnSession.setRowPosition (1);
                    multiColumnSession.setPartyType (PartyType.COMMON);

                } else if (position == 2){

                    edtColumnTitle1.setVisibility (View.VISIBLE);
                    edtColumnTitle2.setVisibility (View.GONE);
                    edtColumnTitle3.setVisibility (View.GONE);
                    edtColumnTitle4.setVisibility (View.GONE);
                    final String[] temp = getResources ().getStringArray (R.array.total_row_waitlist_other);
                    spnrRow.initializeStringValues (temp, getString (R.string.total_row_waitlist));
                    spnrRow.setSelection (1);

                    //spnrRow.setEnabled (false);
                    multiColumnSession.setRowPosition (1);
                    multiColumnSession.setPartyType (PartyType.SMALL);

                } else if (position == 3){
                    edtColumnTitle1.setVisibility (View.VISIBLE);
                    edtColumnTitle2.setVisibility (View.GONE);
                    edtColumnTitle3.setVisibility (View.GONE);
                    edtColumnTitle4.setVisibility (View.GONE);
                    final String[] temp = getResources ().getStringArray (R.array.total_row_waitlist_other);
                    spnrRow.initializeStringValues (temp, getString (R.string.total_row_waitlist));
                    spnrRow.setSelection (1);
                    // spnrRow.setEnabled (false);
                    multiColumnSession.setRowPosition (1);
                    multiColumnSession.setPartyType (PartyType.LARGE);
                } else if (position == 4){
                    edtColumnTitle1.setVisibility (View.VISIBLE);
                    edtColumnTitle2.setVisibility (View.VISIBLE);
                    edtColumnTitle3.setVisibility (View.GONE);
                    edtColumnTitle4.setVisibility (View.GONE);
                    final String[] temp = getResources ().getStringArray (R.array.total_row_waitlist_other);
                    spnrRow.initializeStringValues (temp, getString (R.string.total_row_waitlist));
                    spnrRow.setSelection (1);
                    //spnrRow.setEnabled (false);
                    multiColumnSession.setRowPosition (1);
                    multiColumnSession.setPartyType (PartyType.BOTH);
                }
            }

            @Override
            public void onNothingSelected (AdapterView<?> parent){
                Log.d (LOGTAG, "Nothing ");
            }
        });

        spnrRow.setOnItemSelectedListener (new AdapterView.OnItemSelectedListener (){
            @Override
            public void onItemSelected (AdapterView<?> parent, View view, int position, long id){
                Log.d (LOGTAG, "" + position);
                spnrRow.setBackgroundResource (R.drawable.correct_border);
                if (position > 0){
                    int row = Integer.parseInt (spnrRow.getSelectedItem ().toString ());
                    multiColumnSession.setTotalRows (row);
                    multiColumnSession.setRowPosition (position);
                }
            }

            @Override
            public void onNothingSelected (AdapterView<?> parent){
                Log.d (LOGTAG, "Nohing ");
            }
        });

        //    switchIncomplete.setChecked (multiColumnSession.isInComplete ());
        //    switchNotPresent.setChecked (multiColumnSession.isNotPresent ());

        chkNotPresent.setChecked (multiColumnSession.isNotPresent ());
        chkInComplete.setChecked (multiColumnSession.isInComplete ());
        chkShowParty.setChecked (multiColumnSession.isParty ());


        /*if (Kyobee.getInstance ().getMultiColumnSession () == null){
            switchIncomplete.setChecked (true);
            switchNotPresent.setChecked (true);

        } else{
            switchIncomplete.setChecked (Kyobee.getInstance ().getMultiColumnSession ().isInComplete ());
            switchNotPresent.setChecked (Kyobee.getInstance ().getMultiColumnSession ().isNotPresent ());

        }*/


        txtIncomplete.setOnClickListener (new View.OnClickListener (){
            @Override
            public void onClick (View v){
                if (chkInComplete.isChecked ())
                    chkInComplete.setChecked (false);
                else
                    chkInComplete.setChecked (true);
            }
        });

        txtNotPresent.setOnClickListener (new View.OnClickListener (){
            @Override
            public void onClick (View v){
                if (chkNotPresent.isChecked ())
                    chkNotPresent.setChecked (false);
                else
                    chkNotPresent.setChecked (true);
            }
        });

        txtShowPartyNumber.setOnClickListener (new View.OnClickListener (){
            @Override
            public void onClick (View v){
                if (chkShowParty.isChecked ())
                    chkShowParty.setChecked (false);
                else
                    chkShowParty.setChecked (true);
            }
        });

        btnOk.setOnClickListener (new View.OnClickListener (){
            @Override
            public void onClick (View v){

                if (Connection.checkConnectionStatus (activity) > 0){

                    multiColumnSession.setColumnName1 (edtColumnTitle1.getText ().toString ().trim ());
                    multiColumnSession.setColumnName2 (edtColumnTitle2.getText ().toString ().trim ());
                    //multiColumnSession.setColumnName3 (edtColumnTitle3.getText ().toString ().trim ());
                    // multiColumnSession.setColumnName4 (edtColumnTitle4.getText ().toString ().trim ());

                    if (multiColumnSession.getTotalColumn () == 0){
                        spnrColumn.setBackgroundResource (R.drawable.wrong_border);

                    } else if (multiColumnSession.getRowPosition () == 0){
                        spnrRow.setBackgroundResource (R.drawable.wrong_border);

                    } else if (multiColumnSession.getColumnName1 ().equalsIgnoreCase ("")){
                        edtColumnTitle1.setBackgroundResource (R.drawable.wrong_border);

                    } else if (multiColumnSession.getColumnName2 ().equalsIgnoreCase ("") && multiColumnSession.getTotalColumn () > 1){
                        edtColumnTitle1.setBackgroundResource (R.drawable.correct_border);
                        edtColumnTitle2.setBackgroundResource (R.drawable.wrong_border);

                    } /*else if (multiColumnSession.getColumnName3 ().equalsIgnoreCase ("") && multiColumnSession.getTotalColumn () > 2){
                        edtColumnTitle2.setBackgroundResource (R.drawable.correct_border);
                        edtColumnTitle3.setBackgroundResource (R.drawable.wrong_border);
                    } else if (multiColumnSession.getColumnName4 ().equalsIgnoreCase ("") && multiColumnSession.getTotalColumn () > 3){
                        edtColumnTitle3.setBackgroundResource (R.drawable.correct_border);
                        edtColumnTitle4.setBackgroundResource (R.drawable.wrong_border);
                    }*/ else{
                        spnrColumn.setBackgroundResource (R.drawable.correct_border);
                        spnrRow.setBackgroundResource (R.drawable.correct_border);
                        edtColumnTitle1.setBackgroundResource (R.drawable.correct_border);
                        edtColumnTitle2.setBackgroundResource (R.drawable.correct_border);
                        // edtColumnTitle3.setBackgroundResource (R.drawable.correct_border);
                        // edtColumnTitle4.setBackgroundResource (R.drawable.correct_border);

                        alertDialog.dismiss ();
                        multiColumnSession.setNotPresent (chkNotPresent.isChecked ());
                        multiColumnSession.setInComplete (chkInComplete.isChecked ());
                        multiColumnSession.setParty (chkShowParty.isChecked ());
                        Kyobee.getInstance ().setMultiColumnSession (multiColumnSession);
                        callCheckInUsers (login.getOrgId (), true);
                        //callAdapter ();
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

        edtColumnTitle1.setText (multiColumnSession.getColumnName1 ());
        edtColumnTitle2.setText (multiColumnSession.getColumnName2 ());
        edtColumnTitle3.setText (multiColumnSession.getColumnName3 ());
        edtColumnTitle4.setText (multiColumnSession.getColumnName4 ());

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

    public class DisplayRCLAdapter extends RecyclerView.Adapter<DisplayRCLAdapter.DisplayViewHolder>{
        Activity activity;
        List<ResponseGen.Record> list = new ArrayList<> ();
        LayoutInflater inflater;

        boolean notPresent = true;
        boolean inComplete = true;
        boolean party = false;

        public DisplayRCLAdapter (Activity activity, List<ResponseGen.Record> list){
            this.activity = activity;
            this.list = list;
            inflater = (LayoutInflater) activity.getSystemService (Context.LAYOUT_INFLATER_SERVICE);

            MultiColumnSession multiColumnSession = Kyobee.getInstance ().getMultiColumnSession ();
            if (multiColumnSession != null){
                notPresent = Kyobee.getInstance ().getMultiColumnSession ().isNotPresent ();
                inComplete = Kyobee.getInstance ().getMultiColumnSession ().isInComplete ();
                party = Kyobee.getInstance ().getMultiColumnSession ().isParty ();
            }
        }

        @Override
        public DisplayViewHolder onCreateViewHolder (ViewGroup parent, int viewType){
            View view = inflater.inflate (R.layout.list_gen, parent, false);
            return new DisplayViewHolder (view);
        }

        @Override
        public int getItemCount (){
            return list.size ();
        }

        @Override
        public void onBindViewHolder (DisplayViewHolder holder, int position){
            ResponseGen.Record record = list.get (position);
            holder.txtGuestId.setText (String.valueOf (record.getRank ()));
            holder.txtGuestName.setText (record.getName ());

            if (party){
                holder.txtGuestId.setVisibility (View.VISIBLE);

            } else{
                holder.txtGuestId.setVisibility (View.GONE);
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams (RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                params.addRule (RelativeLayout.ALIGN_PARENT_LEFT);
                params.addRule (RelativeLayout.CENTER_VERTICAL);
                params.addRule (RelativeLayout.LEFT_OF, R.id.imgDown);
                params.setMargins (20, 0, 0, 0);
                holder.txtGuestName.setLayoutParams (params);
            }

            int callCount = 0;
            int inCompleteParty = 0;
            if (record.getCalloutCount () != null){
                callCount = Integer.parseInt (record.getCalloutCount ());
            }
            if (record.getIncompleteParty () != null){
                inCompleteParty = Integer.parseInt (record.getIncompleteParty ());
            }

            if (record.getCalloutCount () == null && record.getIncompleteParty () == null){
                holder.imgDown.setVisibility (View.GONE);
                holder.relativeRow.setBackgroundColor (ContextCompat.getColor (activity, R.color.colorTransparent));
            } else if (callCount >= 1 && inCompleteParty >= 1){

                if (notPresent){
                    holder.imgDown.setVisibility (View.VISIBLE);
                } else{
                    holder.imgDown.setVisibility (View.GONE);
                }

                if (inComplete){
                    holder.relativeRow.setBackgroundColor (ContextCompat.getColor (activity, R.color.colorYellow));
                } else{
                    holder.relativeRow.setBackgroundColor (ContextCompat.getColor (activity, R.color.colorTransparent));
                }


            } else if (callCount >= 1){
                if (notPresent){
                    holder.imgDown.setVisibility (View.VISIBLE);
                } else{
                    holder.imgDown.setVisibility (View.GONE);
                }
                holder.relativeRow.setBackgroundColor (ContextCompat.getColor (activity, R.color.colorTransparent));
                /*holder.imgDown.setVisibility (View.VISIBLE);
                holder.relativeRow.setBackgroundColor (ContextCompat.getColor (activity, R.color.colorTransparent));*/
            } else if (inCompleteParty >= 1){
                holder.imgDown.setVisibility (View.GONE);
                if (inComplete){
                    holder.relativeRow.setBackgroundColor (ContextCompat.getColor (activity, R.color.colorYellow));
                } else{
                    holder.relativeRow.setBackgroundColor (ContextCompat.getColor (activity, R.color.colorTransparent));
                }
                //holder.relativeRow.setBackgroundColor (ContextCompat.getColor (activity, R.color.colorYellow));
            }

        }

        public class DisplayViewHolder extends RecyclerView.ViewHolder{

            public CustomTextViewRegular txtGuestId, txtGuestName;//, txtStatus;
            RelativeLayout relativeRow;
            ImageView imgDown;

            public DisplayViewHolder (View view){
                super (view);
                this.txtGuestId = (CustomTextViewRegular) view.findViewById (R.id.txtGuestId);
                this.txtGuestName = (CustomTextViewRegular) view.findViewById (R.id.txtGuestName);
                this.imgDown = (ImageView) view.findViewById (R.id.imgDown);
                this.relativeRow = (RelativeLayout) view.findViewById (R.id.relativeRow);
            }
        }
    }

    /*@Override
    public void onBackPressed (){

    }*/
}
