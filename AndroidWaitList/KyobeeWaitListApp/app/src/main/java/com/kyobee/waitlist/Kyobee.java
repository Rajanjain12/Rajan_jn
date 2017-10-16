package com.kyobee.waitlist;

import android.app.Application;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.kyobee.waitlist.net.ConnectivityReceiver;
import com.kyobee.waitlist.pojo.Display;
import com.kyobee.waitlist.pojo.MultiColumnSession;
import com.kyobee.waitlist.utils.General;
import com.kyobee.waitlist.utils.PreferenceHelper;
import com.kyobee.waitlist.utils.RealTimePush;

public class Kyobee extends Application{

    public static final String MULTI_COLUMN="multiColumn";
    public static final String DISPLAY="display";
    public static final String LOGIN = "login";
    public static final String LOGIN_ADMIN = "loginAdmin";
    public static final String ORG_ID="orgId";
    public static final String LOGIN_MODE="mode";
    public static final String USERNAME="username";
    public static final String PASSWORD="password";

    private static Kyobee kInstance;


    public static synchronized Kyobee getInstance (){
        return kInstance;
    }

    @Override
    public void onCreate (){
        super.onCreate ();
        kInstance = this;
    }


    public boolean isLoggedIn (){
        String orgId = PreferenceHelper.getString (getApplicationContext (), General.KYOBEE_WAIT_LIST, ORG_ID);
        if (orgId == null || orgId.equalsIgnoreCase (""))
            return false;
        else
            return true;
    }

    // user selected guest mode or display mode
    public boolean isModeSelected (){
        int orgId = PreferenceHelper.getInt (getApplicationContext (), General.KYOBEE_WAIT_LIST, LOGIN_MODE);
        if (orgId <0)
            return false;
        else
            return true;
    }

    // user selected guest mode or display mode
    public boolean isRemember(){
        String uname = PreferenceHelper.getString (getApplicationContext (), General.KYOBEE_WAIT_LIST, USERNAME);
        if (uname == null || uname.equalsIgnoreCase (""))
            return false;
        else
            return true;
    }


    public String getUsername (){
        return PreferenceHelper.getString (getApplicationContext (), General.KYOBEE_WAIT_LIST, USERNAME);
    }

    public void setUsername (String username){
        PreferenceHelper.putString (getApplicationContext (), General.KYOBEE_WAIT_LIST, USERNAME, username);
    }

    public String getPassword (){
        return PreferenceHelper.getString (getApplicationContext (), General.KYOBEE_WAIT_LIST, PASSWORD);
    }

    public void setPassword (String password){
        PreferenceHelper.putString (getApplicationContext (), General.KYOBEE_WAIT_LIST, PASSWORD, password);
    }

    // org id for checking login
    public String getOrgId (){
        return PreferenceHelper.getString (getApplicationContext (), General.KYOBEE_WAIT_LIST, ORG_ID);
    }

    public void setOrgId (String orgId){
        PreferenceHelper.putString (getApplicationContext (), General.KYOBEE_WAIT_LIST, ORG_ID, orgId);
    }

    // for check login mode
    public int getLoginMode (){
        return PreferenceHelper.getInt (getApplicationContext (), General.KYOBEE_WAIT_LIST, LOGIN_MODE);
    }

    public void setLoginMode (int loginMode){
        PreferenceHelper.putInt (getApplicationContext (), General.KYOBEE_WAIT_LIST, LOGIN_MODE, loginMode);
    }

    // login class convert
    public String getLogin (){
        return PreferenceHelper.getString (getApplicationContext (), General.KYOBEE_WAIT_LIST, LOGIN);
    }

    public void setLogin (String login){
        PreferenceHelper.putString (getApplicationContext (), General.KYOBEE_WAIT_LIST, LOGIN, login);
    }

    // login class convert
    public String getLoginAdmin (){
        return PreferenceHelper.getString (getApplicationContext (), General.KYOBEE_WAIT_LIST, LOGIN_ADMIN);
    }

    public void setLoginAdmin (String login){
        PreferenceHelper.putString (getApplicationContext (), General.KYOBEE_WAIT_LIST, LOGIN_ADMIN, login);
    }


    public void logout(){
        PreferenceHelper.clear (getApplicationContext (), General.KYOBEE_WAIT_LIST);
    }

    public void setRealTimeInterface (RealTimePush.RealTimeListener realTime){
        RealTimePush.realTimeListener = realTime;
    }


    public MultiColumnSession getMultiColumnSession (){
        String disp=PreferenceHelper.getString (getApplicationContext (),General.KYOBEE_WAIT_LIST,MULTI_COLUMN);
        Gson gson=new GsonBuilder ().serializeNulls ().create ();
        MultiColumnSession multi=gson.fromJson (disp,MultiColumnSession.class);
        return multi;
    }

    public void setMultiColumnSession (MultiColumnSession multiColumnSession){
        Gson gson=new GsonBuilder ().serializeNulls ().create ();
        String multi=gson.toJson (multiColumnSession);
        PreferenceHelper.putString (getApplicationContext (), General.KYOBEE_WAIT_LIST, MULTI_COLUMN, multi);
    }

    public Display getDisplay (){
        String disp=PreferenceHelper.getString (getApplicationContext (),General.KYOBEE_WAIT_LIST,DISPLAY);
        Gson gson=new GsonBuilder ().serializeNulls ().create ();
        Display display=gson.fromJson (disp,Display.class);
        return display;
    }

    public void setDisplay (Display display){
        Gson gson=new GsonBuilder ().serializeNulls ().create ();
        String disp=gson.toJson (display);
        PreferenceHelper.putString (getApplicationContext (), General.KYOBEE_WAIT_LIST, DISPLAY, disp);
    }
    // for checking internet connection
    public void setConnectivityListener(ConnectivityReceiver.ConnectivityReceiverListener listener) {
        ConnectivityReceiver.connectivityReceiverListener = listener;
    }
}
