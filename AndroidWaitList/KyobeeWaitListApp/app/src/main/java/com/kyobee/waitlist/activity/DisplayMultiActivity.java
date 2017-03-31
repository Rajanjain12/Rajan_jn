package com.kyobee.waitlist.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.kyobee.waitlist.Kyobee;
import com.kyobee.waitlist.R;
import com.kyobee.waitlist.customcontrol.CustomDialog;
import com.kyobee.waitlist.customcontrol.CustomTextViewRegular;
import com.kyobee.waitlist.customcontrol.CustomTextViewSemiBold;
import com.kyobee.waitlist.net.ConnectivityReceiver;
import com.kyobee.waitlist.pojo.APIService;
import com.kyobee.waitlist.pojo.ChannelMessage;
import com.kyobee.waitlist.pojo.Login;
import com.kyobee.waitlist.pojo.Response.ResponseGen;
import com.kyobee.waitlist.pojo.UpdGuest;
import com.kyobee.waitlist.pojo.UpdateGuest;
import com.kyobee.waitlist.utils.AppInfo;
import com.kyobee.waitlist.utils.GSONGetSet;
import com.kyobee.waitlist.utils.General;
import com.kyobee.waitlist.utils.RealTimePush;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import ibt.ortc.extensibility.OrtcClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static org.apache.commons.lang3.CharEncoding.UTF_8;

public class DisplayMultiActivity extends AppCompatActivity implements RealTimePush.RealTimeListener, ConnectivityReceiver.ConnectivityReceiverListener{

    public static String LOGTAG = DisplayMultiActivity.class.getSimpleName ();
    CustomTextViewRegular txtVersion;
    CustomTextViewSemiBold txtNodata1, txtNodata2, txtNodata3;
    AppCompatActivity activity;
    String reqParam = "{\"filters\":null,\"sort\":null,\"sortOrder\":null,\"pageSize\":500,\"pageNo\":1}";
    ImageView imgLogo, imgRefresh;
    String imgPath = "";
    List<ResponseGen.Record> listRecords = new ArrayList<> ();
    List<ResponseGen.Record> listRecords30 = new ArrayList<> ();
    List<ResponseGen.Record> listRecords60 = new ArrayList<> ();
    List<ResponseGen.Record> listRecords90 = new ArrayList<> ();

    RecyclerView recycle30, recycle60, recycle90;
    DisplayRCLAdapter displayRCLAdapter;
    Login login;
    RealTimePush realTimePush;
    /*** override recycleview scroll ***/
    int viewIsScrolling = 1;
    RelativeLayout reltiveLogout;
    //Declare timer
    CountDownTimer cTimer = null;
    int rowLimit = 25;
    private APIService mAPIService;

    //start timer function
    public void startTimer (){
        cTimer = new CountDownTimer (3000, 1000){
            public void onTick (long millisUntilFinished){
            }

            public void onFinish (){
                cancelTimer ();
                Kyobee.getInstance ().logout ();
                startActivity (new Intent (activity, LoginActivity.class));
                finish ();
                //CustomDialog.logoutDialog (activity, activity.getString (R.string.kyobee), activity.getString (R.string.logout));

            }
        };
        cTimer.start ();
    }

    //cancel timer
    public void cancelTimer (){
        if (cTimer != null)
            cTimer.cancel ();
    }

    @Override
    public void onCreate (Bundle savedInstanceState){
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_display_multi);
        activity = this;

        login = GSONGetSet.getLogin ();
        realTimePush = new RealTimePush (activity, login);

        imgLogo = (ImageView) findViewById (R.id.imgLogo);
        imgRefresh = (ImageView) findViewById (R.id.imgRefresh);

        recycle30 = (RecyclerView) findViewById (R.id.recycle30);
        recycle60 = (RecyclerView) findViewById (R.id.recycle60);
        recycle90 = (RecyclerView) findViewById (R.id.recycle90);

        reltiveLogout = (RelativeLayout) findViewById (R.id.reltiveLogout);

        RecyclerView.LayoutManager layoutManager30 = new LinearLayoutManager (activity);
        RecyclerView.LayoutManager layoutManager60 = new LinearLayoutManager (activity);
        RecyclerView.LayoutManager layoutManager90 = new LinearLayoutManager (activity);
        recycle30.setLayoutManager (layoutManager30);
        recycle30.setItemAnimator (new DefaultItemAnimator ());

        recycle60.setLayoutManager (layoutManager60);
        recycle60.setItemAnimator (new DefaultItemAnimator ());

        recycle90.setLayoutManager (layoutManager90);
        recycle90.setItemAnimator (new DefaultItemAnimator ());


        txtNodata3 = (CustomTextViewSemiBold) findViewById (R.id.txtNodata3);
        txtNodata2 = (CustomTextViewSemiBold) findViewById (R.id.txtNodata2);
        txtNodata1 = (CustomTextViewSemiBold) findViewById (R.id.txtNodata1);

        txtVersion = (CustomTextViewRegular) findViewById (R.id.txtVersion);
        String version = getString (R.string.copyright) + " " + AppInfo.getAppVersionName (this); //+ " " + Utils.getCurrentDateTime ();
        txtVersion.setText (version);

        imgPath = General.IMAGE + login.getLogofileName ();

        Glide.with (activity).load (imgPath).diskCacheStrategy (DiskCacheStrategy.ALL).into (imgLogo);
        callCheckInUsers (login.getOrgId (), true);

        imgRefresh.setOnClickListener (new View.OnClickListener (){
            @Override
            public void onClick (View v){
                callCheckInUsers (login.getOrgId (), true);
            }
        });

        txtVersion.setOnClickListener (new View.OnClickListener (){
            @Override
            public void onClick (View v){
                //  CustomDialog.logoutDialog (activity, activity.getString (R.string.kyobee), activity.getString (R.string.logout));
            }
        });


        reltiveLogout.setOnTouchListener (new View.OnTouchListener (){
            @Override
            public boolean onTouch (View v, MotionEvent event){
                int pointerIndex = ((event.getAction () & MotionEvent.ACTION_POINTER_ID_MASK) >> MotionEvent.ACTION_POINTER_ID_SHIFT);
                int action = event.getAction () & MotionEvent.ACTION_MASK;
                int pointerId = event.getPointerId (pointerIndex);
                Log.i ("", "Pointer ID = " + pointerId);
                switch (action){
                    case MotionEvent.ACTION_POINTER_UP:{
                        startTimer ();
                        break;
                    }
                }
                return true;
            }
        });

    }

    public void callCheckInUsers (String orgId, boolean progress){
        listRecords.clear ();
        String totalPage = String.valueOf (rowLimit * 3);
        reqParam = "{\"filters\":null,\"sort\":null,\"sortOrder\":null,\"pageSize\":" + totalPage + ",\"pageNo\":1}";
        if (progress)
            CustomDialog.showProgressDialog (activity, "", getString (R.string.please_wait));
        try{
            String query = URLEncoder.encode (reqParam, UTF_8);
            mAPIService = General.getClient ().create (APIService.class);
            Call<ResponseGen> genCheckInUsers = mAPIService.genCheckInUsers (orgId, query);
            genCheckInUsers.enqueue (new Callback<ResponseGen> (){
                @Override
                public void onResponse (Call<ResponseGen> call, Response<ResponseGen> response){
                    CustomDialog.dismissProgressDialog ();
                    Log.d (LOGTAG, response.toString ());

                    ResponseGen responseGen = response.body ();
                    if (responseGen.getStatus ().equalsIgnoreCase (General.SUCCESS)){
                        listRecords.addAll (responseGen.getServiceResult ().getRecords ());
                        callAdapter ();
                    } else{
                        CustomDialog.showAlertDialog (DisplayMultiActivity.this, "Error", responseGen.getErrorDescription ());
                    }
                }

                @Override
                public void onFailure (Call<ResponseGen> call, Throwable t){
                    CustomDialog.dismissProgressDialog ();
                    Log.d (LOGTAG, "Error " + t.toString ());
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
        if (isConnected)
            callCheckInUsers (login.getOrgId (), false);
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

            } else if (op.equalsIgnoreCase (General.NOT_PRESENT)){
                //NotPresent notPresent= gson.fromJson (message, NotPresent.class);
                callCheckInUsers (login.getOrgId (), false);
            } else if (op.equalsIgnoreCase (General.IN_COMPLETE)){
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

    public void callAdapter (){
        if (listRecords.size () > 0){

            listRecords30.clear ();
            listRecords60.clear ();
            listRecords90.clear ();

            int rowLimit2 = rowLimit * 2;
            int rowLimit3 = rowLimit * 3;

            for (int i = 0; i < listRecords.size (); i++){
                ResponseGen.Record record = listRecords.get (i);
                if (i < rowLimit){
                    listRecords30.add (record);
                } else if (i < rowLimit2){
                    listRecords60.add (record);
                } else{
                    listRecords90.add (record);
                }
            }

            displayRCLAdapter = new DisplayRCLAdapter (activity, listRecords30);
            recycle30.setAdapter (displayRCLAdapter);
            if (listRecords30.size () > 0){
                txtNodata1.setVisibility (View.GONE);
            } else{
                txtNodata1.setVisibility (View.VISIBLE);
            }

            displayRCLAdapter = new DisplayRCLAdapter (activity, listRecords60);
            recycle60.setAdapter (displayRCLAdapter);
            if (listRecords60.size () > 0){
                txtNodata2.setVisibility (View.GONE);
            } else{
                txtNodata2.setVisibility (View.VISIBLE);
            }

            displayRCLAdapter = new DisplayRCLAdapter (activity, listRecords90);
            recycle90.setAdapter (displayRCLAdapter);
            if (listRecords90.size () > 0){
                txtNodata3.setVisibility (View.GONE);
            } else{
                txtNodata3.setVisibility (View.VISIBLE);
            }

            displayRCLAdapter.notifyDataSetChanged ();
        }
    }

    @Override
    public void onSubscribeChannel (OrtcClient sender, String channel){
        Log.d (LOGTAG, "Subscribe Sender - " + sender + " Channel - " + channel);
    }

    public class DisplayRCLAdapter extends RecyclerView.Adapter<DisplayRCLAdapter.DisplayViewHolder>{
        Activity activity;
        List<ResponseGen.Record> list = new ArrayList<> ();
        LayoutInflater inflater;

        public DisplayRCLAdapter (Activity activity, List<ResponseGen.Record> list){
            this.activity = activity;
            this.list = list;
            inflater = (LayoutInflater) activity.getSystemService (Context.LAYOUT_INFLATER_SERVICE);
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
                holder.imgDown.setVisibility (View.VISIBLE);
                holder.relativeRow.setBackgroundColor (ContextCompat.getColor (activity, R.color.colorYellow));
            } else if (callCount >= 1){
                holder.imgDown.setVisibility (View.VISIBLE);
                holder.relativeRow.setBackgroundColor (ContextCompat.getColor (activity, R.color.colorTransparent));
            } else if (inCompleteParty >= 1){
                holder.imgDown.setVisibility (View.GONE);
                holder.relativeRow.setBackgroundColor (ContextCompat.getColor (activity, R.color.colorYellow));
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


}
