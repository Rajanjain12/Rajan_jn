package com.kyobee.waitlist.utils;

import android.app.Activity;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.util.Log;

import java.io.File;

public class AppInfo{

    private static String LOGTAG = AppInfo.class.getSimpleName ();

    // app version name which we can give any name
    public static String getAppVersionName (Activity activity){
        String versionName = "1.0";
        try{
            PackageInfo pinfo = activity.getPackageManager ().getPackageInfo (activity.getPackageName (), 0);
            //int versionNumber = pinfo.versionCode;
            versionName = pinfo.versionName;
        } catch (Exception e){
            Log.e (LOGTAG, "App VersionName Exception " + e.getMessage ());
        }
        return versionName;
    }

    // return app version which we need to increase when we upload apk on playstore
    public static int getAppVersionCode (Activity activity){
        int versionNumber = 1;
        try{
            PackageInfo pinfo = activity.getPackageManager ().getPackageInfo (activity.getPackageName (), 0);
            versionNumber = pinfo.versionCode;
        } catch (Exception e){
            Log.e (LOGTAG, "App VersionCode Exception " + e.getMessage ());
        }
        return versionNumber;
    }

    // return app version which we need to increase when we upload apk on playstore
    public static long getAppInstallTime (Activity activity){
        int versionNumber = 1;
        try{
            ApplicationInfo appInfo = activity.getPackageManager ().getApplicationInfo (activity.getPackageName (), 0);//getPackageInfo(activity.getPackageName(), 0);
            String appFile = appInfo.sourceDir;
            long installed = new File (appFile).lastModified ();
            Log.e (LOGTAG, "AppInstall-Time " + installed);
        } catch (Exception e){
            Log.e (LOGTAG, "AppInstall-Time Exception " + e.getMessage ());
        }
        return versionNumber;

    }

    // return app version which we need to increase when we upload apk on playstore
    public static long getAppCurrentTime (Activity activity){
        return System.currentTimeMillis ();
    }

}
