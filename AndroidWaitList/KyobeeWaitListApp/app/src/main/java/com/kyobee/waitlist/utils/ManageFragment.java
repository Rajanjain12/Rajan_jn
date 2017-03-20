package com.kyobee.waitlist.utils;

import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

public class ManageFragment{

    // add support fragment
    public static void add (FragmentActivity fragmentActivity, @IdRes int containerViewId, @NonNull Fragment fragment, @NonNull String fragmentTag){
        fragmentActivity.getSupportFragmentManager ().beginTransaction ().add (containerViewId, fragment, fragmentTag).disallowAddToBackStack ().commit ();
    }

    // replace support fragment
    public static void replace (FragmentActivity fragmentActivity, @IdRes int containerViewId, @NonNull Fragment fragment, @NonNull String fragmentTag, @Nullable String backStackStateName){
        fragmentActivity.getSupportFragmentManager ().beginTransaction ().addToBackStack (fragmentTag).replace (containerViewId, fragment, fragmentTag).commit ();
        //fragmentActivity.getSupportFragmentManager ().beginTransaction ().add (containerViewId, fragment, fragmentTag).addToBackStack (fragmentTag).commit ();
    }

    // replace support fragment
    public static void initial (FragmentActivity fragmentActivity, @IdRes int containerViewId, @NonNull Fragment fragment, @NonNull String fragmentTag, @Nullable String backStackStateName){
        fragmentActivity.getSupportFragmentManager ().beginTransaction ().replace (containerViewId, fragment, fragmentTag).commit ();
    }

    // replace no stack support fragment
    public static void replaceNoStack (FragmentActivity fragmentActivity, @IdRes int containerViewId, @NonNull Fragment fragment, @NonNull String fragmentTag, @Nullable String backStackStateName){
        fragmentActivity.getSupportFragmentManager ().beginTransaction ().replace (containerViewId, fragment, fragmentTag).commitAllowingStateLoss ();
        //fragmentActivity.getSupportFragmentManager ().beginTransaction ().add (containerViewId, fragment, fragmentTag).addToBackStack (fragmentTag).commit ();
    }

    // back support fragment
    public static void back (FragmentActivity activity){
        activity.getSupportFragmentManager ().popBackStack ();

    }

    // clear all fragments
    public static void clearFragment (FragmentActivity activity){
        FragmentManager fm = activity.getSupportFragmentManager ();
        for (int i = 0; i < fm.getBackStackEntryCount (); i++){
            try{
                fm.popBackStack (null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                fm.executePendingTransactions ();
            } catch (Exception e){
                e.printStackTrace ();
            }
        }
    }

}
