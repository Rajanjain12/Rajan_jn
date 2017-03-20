package com.kyobee.waitlist.utils;

import android.content.Context;
import android.os.Build;
import android.support.v4.app.FragmentActivity;
import android.view.View;

import com.kyobee.waitlist.R;
import com.kyobee.waitlist.customcontrol.CustomTextViewRegular;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validation{

    public static int correct = R.color.colorLightBrown;
    public static int wrong = R.color.colorRed;

    public static boolean passwordValidator (String s){
        Pattern mPattern = Pattern.compile ("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[$@$!%*?&])[A-Za-z\\d$@$!%*?&]{8,10}");

        // Pattern mPattern = Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[$@$!%*?&])[A-Za-z\\d$@$!%*?&]{8,10}");
        Matcher matcher = mPattern.matcher (s.toString ());
        if (matcher.find ()){
            return true;
        }
        return false;
    }

    public static String correctPhone (String phone){
        String updatedPhone = phone.replaceAll ("[^0-9]+", "");
        String newPhone = updatedPhone;
        if (updatedPhone.length () > 10){
            newPhone = updatedPhone.substring (updatedPhone.length () - 10);
        }
        return newPhone;
    }

    public static boolean emailValidator (String email){
        Pattern pattern;
        Matcher matcher;
        final String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        pattern = Pattern.compile (EMAIL_PATTERN);
        matcher = pattern.matcher (email);
        return matcher.matches ();
    }

    public static boolean phoneValidator (String phone){
        if (phone.length () != 14)
            return false;
        /*String updatedPhone = "";
        if (phone.length() > 10) {
            updatedPhone = phone.substring(phone.length() - 10);
        } else {
            return false;
        }

        if (updatedPhone.length() != 10) {
            return false;
        }*/

        return true;
    }

    public static void errorView (View view, int id){
        view.setBackgroundColor (id);
    }

    public static void errorCheck (FragmentActivity activity, CustomTextViewRegular txtError, String message, View view, boolean visible, int id){
        int color = getColorWrapper (activity, id);
        view.setBackgroundColor (color);
        if (visible){
            txtError.setVisibility (View.VISIBLE);
            txtError.setText (message);
            txtError.setTextColor (color);
        } else{
            txtError.setVisibility (View.INVISIBLE);
        }
    }

    public static void errorCheckCount (FragmentActivity activity, CustomTextViewRegular txtError, String message, View view, boolean visible, int id, int idtext){
        int color = getColorWrapper (activity, id);
        view.setBackgroundColor (color);
        if (visible){
            txtError.setVisibility (View.VISIBLE);
            txtError.setText (message);
            txtError.setTextColor (idtext);
        } else{
            txtError.setVisibility (View.INVISIBLE);
        }
    }

    public static int getColorWrapper (Context context, int id){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            return context.getColor (id);
        } else{
            //noinspection deprecation
            return context.getResources ().getColor (id);
        }
    }

}
