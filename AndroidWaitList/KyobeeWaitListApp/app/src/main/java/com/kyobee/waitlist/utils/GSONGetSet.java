package com.kyobee.waitlist.utils;

import com.google.gson.GsonBuilder;
import com.kyobee.waitlist.Kyobee;
import com.kyobee.waitlist.pojo.Login;

public class GSONGetSet{


    public static Login getLogin (){
        String lg=Kyobee.getInstance ().getLogin ();
        Login login;
        if(lg.equalsIgnoreCase ("")){
            login=new Login ();
        }else{
            login=new GsonBuilder ().serializeNulls ().create ().fromJson (lg,Login.class);
        }
        return login;
    }

    public static void setLogin (Login login){
        String lg=new GsonBuilder ().serializeNulls ().create ().toJson (login);
        Kyobee.getInstance ().setLogin (lg);
    }

   /* // get request restaurant  detail for search restaurant detail screen
    public static  Login getRequestRestaurantDetail (){
        String rd = Kyobee.getInstance ().getRequestRestaurantDetail ();
        RequestRestaurantDetail requestRestaurantDetail;
        if (rd.equalsIgnoreCase ("")){
            requestRestaurantDetail = new RequestRestaurantDetail ();
        } else{
            requestRestaurantDetail = new GsonBuilder ().serializeNulls ().create ().fromJson (rd, RequestRestaurantDetail.class);
        }
        return requestRestaurantDetail;
    }

    public static void setRequestRestaurantDetail (RequestRestaurantDetail requestRestaurantDetail){
        String rd = new GsonBuilder ().serializeNulls ().create ().toJson (requestRestaurantDetail);
        Ingreets.getInstance ().setRequestRestaurantDetail (rd);
    }
*/
}
