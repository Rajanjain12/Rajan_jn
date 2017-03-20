package com.kyobee.waitlist.pojo;

import com.kyobee.waitlist.utils.General;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface APIService{

    // for login api
    String USERNAME = "username";
    String PASSWORD = "password";

    // for check in users api
    String ORG_ID = "orgid";
    String PAGER_REQ_PARAM = "pagerReqParam";

    @GET(General.LOGIN)
    Call<Login> loginCredAuth (@Query(USERNAME) String username, @Query(PASSWORD) String password);

    @GET(General.CHECK_IN_USERS)
    @Headers("Content-Type: application/x-www-form-urlencoded")
    Call<CheckInUsers> checkInUsers (@Query(ORG_ID) String orgId, @Query(value = PAGER_REQ_PARAM, encoded = true) String pagerReqParam);

    @GET(General.WAIT_LIST_REST_ACTION)
    Call<Display> waiting (@Query(ORG_ID) String orgId);

    @POST(General.ADD_GUEST)
    Call<ResponseAddGuest> addGuest (@Body RequestAddGuest requestAddGuest);

}
