package com.kyobee.waitlist.utils;

import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.Surface;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.kyobee.waitlist.R;
import com.kyobee.waitlist.customcontrol.CustomDialog;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Utils{

    private static String LOGTAG = Utils.class.getSimpleName ();

    // if we want to catch image in memory than use this method
    public static void setImageWithCatch (Activity activity, String uri, ImageView imageView){
        Glide.with (activity).load (uri).diskCacheStrategy (DiskCacheStrategy.NONE).skipMemoryCache (true).into (imageView);
    }

    public static void noInternet (Activity activity){
        CustomDialog.showAlertDialog (activity, activity.getString (R.string.kyobee), activity.getString (R.string.no_internet));
    }

    public static void setGifWithCatch (Activity activity, String uri, ImageView imageView){
        Glide.with (activity).load (uri).asGif ().crossFade ().into (imageView);
    }

    public static void setDynamicImage (ImageView imageView){
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams (20, 20);
        layoutParams.gravity = Gravity.CENTER;
        imageView.setLayoutParams (layoutParams);
        // imageView.setImageResource (R.drawable.ic_close_2);
    }

    public static String ReadFromfile (String fileName, Context context){
        StringBuilder returnString = new StringBuilder ();
        InputStream fIn = null;
        InputStreamReader isr = null;
        BufferedReader input = null;
        try{
            fIn = context.getResources ().getAssets ().open (fileName, Context.MODE_PRIVATE);
            isr = new InputStreamReader (fIn);
            input = new BufferedReader (isr);
            String line = "";
            while ((line = input.readLine ()) != null){
                returnString.append (line);
            }
        } catch (Exception e){
            e.getMessage ();
        } finally{
            try{
                if (isr != null)
                    isr.close ();
                if (fIn != null)
                    fIn.close ();
                if (input != null)
                    input.close ();
            } catch (Exception e2){
                e2.getMessage ();
            }
        }
        return returnString.toString ();
    }

    public static String timeConversion (String total){

        int totalSeconds = Integer.parseInt (total);
        final int MINUTES_IN_AN_HOUR = 60;
        final int SECONDS_IN_A_MINUTE = 60;

        int seconds = totalSeconds % SECONDS_IN_A_MINUTE;
        int totalMinutes = totalSeconds / SECONDS_IN_A_MINUTE;
        int minutes = totalMinutes % MINUTES_IN_AN_HOUR;
        // int hours = totalMinutes / MINUTES_IN_AN_HOUR;

        return String.format ("%02d:%02d", minutes, seconds);
    }

    public static int getScreenOrientation (Context context){
        final int screenOrientation = ((WindowManager) context.getSystemService (Context.WINDOW_SERVICE)).getDefaultDisplay ().getOrientation ();
        switch (screenOrientation){
            case Surface.ROTATION_0:
                return 0;//"android portrait screen";
            case Surface.ROTATION_90:
                return 1;//"android landscape screen";
            case Surface.ROTATION_180:
                return 0;//"android reverse portrait screen";
            default:
                return 1;//"android reverse landscape screen";
        }
    }

    public static void checkConnection (Activity activity,Throwable error){

        String message = "";
        if (error instanceof SocketTimeoutException){
            message = "Poor internet connection detected, Please try again.";
            CustomDialog.showAlertDialog (activity,"",message);
        }else if(error instanceof UnknownHostException){
            noInternet (activity);
        }

        /*else if (error instanceof ServerError){
            message = "The server could not be found. Please try again after some time!!";
        } else if (error instanceof AuthFailureError){
            message = "Cannot connect to Internet...Please check your connection!";
        } else if (error instanceof ParseError){
            message = "Parsing error! Please try again after some time!!";
        } else if (error instanceof NoConnectionError){
            message = "Cannot connect to Internet...Please check your connection!";
        } else if (error instanceof TimeoutError){
            message = "Connection TimeOut! Please check your internet connection.";
        }*/

    }

    public static String getCurrentDateTime (){
        SimpleDateFormat sdf = new SimpleDateFormat ("MM/d/yyyy h:mm:a");
        String currentDate = sdf.format (new Date ());
        return currentDate;
    }


}
