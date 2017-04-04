package com.kyobee.waitlist.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.kyobee.waitlist.Kyobee;
import com.kyobee.waitlist.R;
import com.kyobee.waitlist.customcontrol.CustomDialog;
import com.kyobee.waitlist.customcontrol.CustomTextViewRegular;
import com.kyobee.waitlist.net.ConnectivityReceiver;
import com.kyobee.waitlist.pojo.APIService;
import com.kyobee.waitlist.pojo.ChannelMessage;
import com.kyobee.waitlist.pojo.CheckInUsers;
import com.kyobee.waitlist.pojo.Login;
import com.kyobee.waitlist.pojo.Record;
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

public class DisplayActivity extends AppCompatActivity implements RealTimePush.RealTimeListener,ConnectivityReceiver.ConnectivityReceiverListener{

    public static String LOGTAG = DisplayActivity.class.getSimpleName ();
    CustomTextViewRegular txtCopyRight;
    AppCompatActivity activity;
    String reqParam = "{\"filters\":null,\"sort\":null,\"sortOrder\":null,\"pageSize\":500,\"pageNo\":1}";
    ImageView imgLogo, imgRefresh;
    String imgPath = "";
    List<Record> listRecords = new ArrayList<> ();
    RecyclerView recycleDisplay;
    DisplayRCLAdapter displayRCLAdapter;
    Login login;
    RealTimePush realTimePush;
    private APIService mAPIService;

    @Override
    public void onCreate (Bundle savedInstanceState){
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_display);
        activity = this;

        login = GSONGetSet.getLogin ();
        realTimePush = new RealTimePush (activity, login);

        imgLogo = (ImageView) findViewById (R.id.imgLogo);
        imgRefresh = (ImageView) findViewById (R.id.imgRefresh);

        recycleDisplay = (RecyclerView) findViewById (R.id.recycleDisplay);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager (activity);
        recycleDisplay.setLayoutManager (layoutManager);
        recycleDisplay.setItemAnimator (new DefaultItemAnimator ());

        txtCopyRight = (CustomTextViewRegular) findViewById (R.id.txtCopyRight);
        String version = getString (R.string.copyright) + " " + AppInfo.getAppVersionName (this);
        txtCopyRight.setText (version);

        imgPath = General.IMAGE + login.getLogofileName ();

        Glide.with (activity).load (imgPath).diskCacheStrategy (DiskCacheStrategy.ALL).into (imgLogo);
        callCheckInUsers (login.getOrgId (), true);

        imgRefresh.setOnClickListener (new View.OnClickListener (){
            @Override
            public void onClick (View v){
                callCheckInUsers (login.getOrgId (), true);
            }
        });

        txtCopyRight.setOnClickListener (new View.OnClickListener (){
            @Override
            public void onClick (View v){
                CustomDialog.logoutDialog (activity, activity.getString (R.string.kyobee), activity.getString (R.string.logout));
            }
        });

    }

    public void callCheckInUsers (String orgId, boolean progress){
        listRecords.clear ();
        if (progress)
            CustomDialog.showProgressDialog (activity, "", getString (R.string.please_wait));
        try{
            String query = URLEncoder.encode (reqParam, UTF_8);
            mAPIService = General.getClient ().create (APIService.class);
            Call<CheckInUsers> checkInUsersCall = mAPIService.checkInUsers (orgId, query);
            checkInUsersCall.enqueue (new Callback<CheckInUsers> (){
                @Override
                public void onResponse (Call<CheckInUsers> call, Response<CheckInUsers> response){
                    CustomDialog.dismissProgressDialog ();
                    Log.d (LOGTAG, response.toString ());

                    CheckInUsers checkInUsers = response.body ();
                    if (checkInUsers.getStatus ().equalsIgnoreCase (General.SUCCESS)){
                        listRecords.addAll (checkInUsers.getServiceResult ().getRecords ());
                        callAdapter ();
                    } else{
                        CustomDialog.showAlertDialog (activity, "Error", checkInUsers.getErrorDescription ());
                    }
                }

                @Override
                public void onFailure (Call<CheckInUsers> call, Throwable t){
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
        if(isConnected)
            callCheckInUsers (login.getOrgId (),false);
    }

    @Override
    public void onReceivedResponse (String channel, String message){
        try{
            Gson gson = new GsonBuilder ().serializeNulls ().create ();
            Log.d (LOGTAG, "Message Json " + message);
            ChannelMessage channelMessage = gson.fromJson (message, ChannelMessage.class);
            if (channelMessage.getOP () == null){
                 message=channelMessage.getM ();
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
                    Record record = listRecords.get (i);
                    if (record.getGuestID () == updGuest.getGuestID ()){
                        record.setName (updGuest.getName ());
                        //record.setSms (updGuest.getSms ());
                        //record.setPrefType (updGuest.getPrefType ());
                        //record.setEmail(updGuest.getEmail ());
                        listRecords.set (i, record);
                        break;
                    }
                }
                updateAdapter();
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
                    Record record = listRecords.get (i);
                    if (record.getGuestID () == updGuest.getGuestID ()){
                        listRecords.remove (i);
                        break;
                    }
                }
                updateAdapter();
            }
        } catch (JsonSyntaxException e){
            Log.d (LOGTAG, "JsonSyntaxException" + e.getMessage ());

        } catch (JSONException e){
            Log.d (LOGTAG, "JSON Exception" + e.getMessage ());
        }

    }

    public void updateAdapter(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                callAdapter ();
            }
        });
    }

    public void callAdapter (){
        if (listRecords.size () > 0){
            displayRCLAdapter = new DisplayRCLAdapter (activity, listRecords);
            recycleDisplay.setAdapter (displayRCLAdapter);
            displayRCLAdapter.notifyDataSetChanged ();
        }
    }

    @Override
    public void onSubscribeChannel (OrtcClient sender, String channel){
        Log.d (LOGTAG, "Subscribe Sender - " + sender + " Channel - " + channel);
    }

    public class DisplayRCLAdapter extends RecyclerView.Adapter<DisplayRCLAdapter.DisplayViewHolder>{
        Activity activity;
        List<Record> list=new ArrayList<> ();
        LayoutInflater inflater;

        public DisplayRCLAdapter (Activity activity, List<Record> list){
            this.activity = activity;
            this.list = list;
            inflater = (LayoutInflater) activity.getSystemService (Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public DisplayViewHolder onCreateViewHolder (ViewGroup parent, int viewType){
            View view = inflater.inflate (R.layout.list_display, parent, false);
            return new DisplayViewHolder (view);
        }

        @Override
        public int getItemCount (){
            return list.size ();
        }

        @Override
        public void onBindViewHolder (DisplayViewHolder holder, int position){
            Record record = list.get (position);
            holder.txtNo.setText (String.valueOf (record.getRank ()));
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
                holder.txtStatus.setText (record.getStatus ());
            } else if (callCount >= 1 && inCompleteParty >= 1){
                holder.txtStatus.setText ("Incomplete/Not Present");
                holder.txtStatus.setTextSize (20);
            } else if (callCount >= 1)
                holder.txtStatus.setText ("Not Present");
            else if (inCompleteParty >= 1){
                holder.txtStatus.setText ("Incomplete");
            }

        }

        public class DisplayViewHolder extends RecyclerView.ViewHolder{

            public CustomTextViewRegular txtNo, txtGuestName, txtStatus;

            public DisplayViewHolder (View view){
                super (view);
                this.txtNo = (CustomTextViewRegular) view.findViewById (R.id.txtNo);
                this.txtGuestName = (CustomTextViewRegular) view.findViewById (R.id.txtGuestName);
                this.txtStatus = (CustomTextViewRegular) view.findViewById (R.id.txtStatus);

            }
        }
    }


}
