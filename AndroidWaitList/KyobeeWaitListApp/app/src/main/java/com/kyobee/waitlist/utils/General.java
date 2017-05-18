package com.kyobee.waitlist.utils;

// webapi list used in clee app

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class General{

    public static final String KYOBEE_WAIT_LIST = "kyobee_wait_list";

    // dev
    public static final String URL = "http://jbossdev-kyobee.rhcloud.com/";

    // qa
    //public static final String URL = "http://jbossqa-kyobee.rhcloud.com/";

    /*public static final String LOGIN=URL+"rest/loginCredAuth?";//username=jkim@kyobee.com&password=jaekim";
    public static final String WAIT_LIST_REST_ACTION=URL+"web/rest/waitlistRestAction/totalwaittimemetricks?orgid=";
    public static final String  ADD_GUEST=URL+"web/rest/waitlistRestAction/addGuest";
    public static final String CHECK_IN_USERS=URL+"/web/rest/waitlistRestAction/checkinusers?orgid=";*/

    public static final String LOGIN = "kyobee/rest/loginCredAuth";
    public static final String WAIT_LIST_REST_ACTION = "kyobee/web/rest/waitlistRestAction/totalwaittimemetricks";
    public static final String ADD_GUEST = "kyobee/web/rest/waitlistRestAction/addGuest";
    public static final String CHECK_IN_USERS = "kyobee/web/rest/waitlistRestAction/checkinusers";
    public static final String IMAGE=URL+"static/orglogos/";

    private static Retrofit retrofit = null;

    public static Retrofit getClient (){

        Gson gson = new GsonBuilder ()
                .setLenient()
                .create();

        //OkHttpClient okHttpClient = new OkHttpClient ().newBuilder ().connectTimeout (60, TimeUnit.SECONDS).readTimeout (60, TimeUnit.SECONDS).writeTimeout (60, TimeUnit.SECONDS).build ();
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        // set your desired log level
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient okHttpClient = new OkHttpClient ().newBuilder ().connectTimeout (10, TimeUnit.SECONDS).readTimeout (10,TimeUnit.SECONDS).writeTimeout (10,TimeUnit.SECONDS).addInterceptor (logging).build ();

        if (retrofit == null){
            //retrofit = new Retrofit.Builder ().baseUrl (URL).addCallAdapterFactory (RxJavaCallAdapterFactory.create ()).addConverterFactory (GsonConverterFactory.create ()).build ();
            retrofit = new Retrofit.Builder ().baseUrl (URL).client (okHttpClient).addCallAdapterFactory (RxJavaCallAdapterFactory.create ()).addConverterFactory (GsonConverterFactory.create (gson)).build ();

        }
        return retrofit;
    }

    public static final String OK = "0";
    public static final String SUCCESS="SUCCESS";


    // real time co
    public static final String APPLICATION_KEY = "j9MLMa";
    public static final String MY_TOKEN = "testToken";//"XQJYeUzTZCCP";

    public static final String DEV_ENV="DEV_";
    //public static final String DEV_ENV="QA_";
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

    public static final String ADMIN="admin";
    public static final String ADVANTECH="advantech";

}
