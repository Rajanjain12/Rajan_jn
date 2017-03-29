package com.kyobee.waitlist.utils;

// webapi list used in clee app

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class General{

    public static final String KYOBEE_WAIT_LIST = "kyobee_wait_list";

    public static final String URL = "http://jbossdev-kyobee.rhcloud.com/";
    /*public static final String LOGIN=URL+"rest/loginCredAuth?";//username=jkim@kyobee.com&password=jaekim";
    public static final String WAIT_LIST_REST_ACTION=URL+"web/rest/waitlistRestAction/totalwaittimemetricks?orgid=";
    public static final String  ADD_GUEST=URL+"web/rest/waitlistRestAction/addGuest";
    public static final String CHECK_IN_USERS=URL+"/web/rest/waitlistRestAction/checkinusers?orgid=";*/

    public static final String LOGIN = "kyobee/rest/loginCredAuth";
    public static final String WAIT_LIST_REST_ACTION = "kyobee/web/rest/waitlistRestAction/totalwaittimemetricks";
    public static final String ADD_GUEST = "kyobee/web/rest/waitlistRestAction/addGuest";
    public static final String CHECK_IN_USERS = "kyobee/web/rest/waitlistRestAction/checkinusers";
    public static final String IMAGE="http://jbossdev-kyobee.rhcloud.com/static/orglogos/";

    private static Retrofit retrofit = null;

    public static Retrofit getClient (){
        if (retrofit == null){
            retrofit = new Retrofit.Builder ().baseUrl (URL).addCallAdapterFactory (RxJavaCallAdapterFactory.create ()).addConverterFactory (GsonConverterFactory.create ()).build ();
        }
        return retrofit;
    }

    public static final String OK = "0";
    public static final String SUCCESS="SUCCESS";


    // real time co
    public static final String APPLICATION_KEY = "j9MLMa";
    public static final String MY_TOKEN = "testToken";//"XQJYeUzTZCCP";

    public static final String DEV_ENV="DEV_";
    public static final String CHANNEL_DEV="RSNT_GUEST_"+DEV_ENV;


    // channel name

    public static final String OP="OP";
    public static final String ADD="ADD";
    public static final String DEL="DEL";
    public static final String UPDATE_GUEST_INFO="UpdageGuestInfo";
    public static final String UPDATE="Upd";
    public static final String NOT_PRESENT="NOT_PRESENT";
    public static final String MARK_AS_SEATED="MARK_AS_SEATED";
    public static final String IN_COMPLETE="INCOMPLETE";

}
