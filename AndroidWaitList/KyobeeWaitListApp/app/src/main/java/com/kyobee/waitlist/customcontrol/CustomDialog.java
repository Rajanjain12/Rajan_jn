package com.kyobee.waitlist.customcontrol;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.kyobee.waitlist.Kyobee;
import com.kyobee.waitlist.R;
import com.kyobee.waitlist.activity.LoginActivity;

public class CustomDialog{

    public static ProgressDialog progressDialog;

    public static void showAlertDialog(Activity activity,String title,String message){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity);
        LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.custom_alert_dialog,(ViewGroup)activity.findViewById(R.id.custom_dialog));

        alertDialogBuilder.setView(view);
        // create alert dialog
        final AlertDialog alertDialog = alertDialogBuilder.create();
        // show it
        alertDialog.show();

        Button btnOk=(Button)view.findViewById(R.id.btnOk);
        TextView txtTitle=(TextView)view.findViewById(R.id.txtTitle);
        TextView txtMessage=(TextView)view.findViewById(R.id.txtMessage);
       txtTitle.setText(title);
        txtMessage.setText(message);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
    }

    public static void logoutDialog(final Activity activity, String title, String message){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity);
        LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.logout_dialog,(ViewGroup)activity.findViewById(R.id.custom_dialog));

        alertDialogBuilder.setView(view);
        // create alert dialog
        final AlertDialog alertDialog = alertDialogBuilder.create();
        // show it
        alertDialog.show();

        CustomButtonRegular btnNo=(CustomButtonRegular)view.findViewById(R.id.btnNo);
        CustomButtonRegular btnYes=(CustomButtonRegular)view.findViewById(R.id.btnYes);
        TextView txtTitle=(TextView)view.findViewById(R.id.txtTitle);
        TextView txtMessage=(TextView)view.findViewById(R.id.txtMessage);
        txtTitle.setText(title);
        txtMessage.setText(message);
        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                Kyobee.getInstance ().logout ();
                activity.startActivity (new Intent (activity, LoginActivity.class));
                activity.finish ();
            }
        });
    }


   /* public static void showAlertOkCancel(Activity activity,String title,String message){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity);
        LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.custom_alert_ok_cancel,(ViewGroup)activity.findViewById(R.id.custom_dialog));

        alertDialogBuilder.setView(view);
        // create alert dialog
        final AlertDialog alertDialog = alertDialogBuilder.create();
        // show it
        alertDialog.show();

        CustomButtonMedium btnAccept=(CustomButtonMedium)view.findViewById(R.id.btnAccept);
        CustomButtonMedium btnCancel=(CustomButtonMedium)view.findViewById(R.id.btnCancel);
        TextView txtTitle=(CustomTextViewMedium)view.findViewById(R.id.txtTitle);
        TextView txtMessage=(CustomTextViewLight)view.findViewById(R.id.txtMessage);
        txtTitle.setText(title);
        txtMessage.setText(message);
        btnAccept.setOnClickListener (new View.OnClickListener (){
            @Override
            public void onClick (View v){
                alertDialog.dismiss();
            }
        });
        btnCancel.setOnClickListener (new View.OnClickListener (){
            @Override
            public void onClick (View v){
                alertDialog.dismiss();
            }
        });
    }*/


    public static void showProgressDialog(Context ctx, String title, String msg) {

        dismissProgressDialog();
        progressDialog = new ProgressDialog(ctx);
        progressDialog.setMessage(msg);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
    }

    public static void dismissProgressDialog() {
        if (progressDialog != null) {
            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
            progressDialog = null;
        }
    }



}
