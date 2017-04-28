package com.kyobee.waitlist.utils;

import android.app.Activity;
import android.util.Log;

import com.kyobee.waitlist.pojo.Login;

import org.apache.commons.lang3.StringEscapeUtils;

import ibt.ortc.api.Ortc;
import ibt.ortc.extensibility.OnConnected;
import ibt.ortc.extensibility.OnDisconnected;
import ibt.ortc.extensibility.OnMessage;
import ibt.ortc.extensibility.OnSubscribed;
import ibt.ortc.extensibility.OnUnsubscribed;
import ibt.ortc.extensibility.OrtcClient;
import ibt.ortc.extensibility.OrtcFactory;

public class RealTimePush{

    public static final String LOGTAG = RealTimePush.class.getSimpleName ();

    public static RealTimeListener realTimeListener;

    OrtcFactory factory;
    OrtcClient client;
    Ortc ortc;

    Activity activity;

    public RealTimePush (Activity activity, Login login){
        this.activity = activity;
        ortc = new Ortc ();
        realTime (login);
    }

    public void realTime (final Login login){
        try{
            factory = ortc.loadOrtcFactory ("IbtRealtimeSJ");
            client = factory.createClient ();

            client.setClusterUrl ("http://ortc-developers.realtime.co/server/2.1/");
            client.setConnectionMetadata ("AndroidApp");

            if (client != null){
                try{


                    if (client.getIsConnected ()){
                        Log.d (LOGTAG, "Channel Connected Already");
                    } else{
                        Log.d (LOGTAG, "Channel New Connect");
                        client.connect (General.APPLICATION_KEY, General.MY_TOKEN);


                        client.onConnected = new OnConnected (){
                            @Override
                            public void run (OrtcClient sender){

                                Log.d (LOGTAG, "Connected - ");

                                activity.runOnUiThread (new Runnable (){
                                    @Override
                                    public void run (){
                                        String channel = General.CHANNEL_DEV + login.getOrgId ();
                                        client.subscribe (channel, true, new OnMessage (){
                                            public void run (OrtcClient sender, final String channel, final String message){
                                                //  Log.d (LOGTAG, "Receive Channel Before " + channel + " Message - " + message);
                                                String message1 = StringEscapeUtils.unescapeJson (message);
                                                //  Log.d (LOGTAG, "Receive Channel After- " + channel + " Message - " + message1);
                                                realTimeListener.onReceivedResponse (channel, message1);        // Code here will run in UI thread
                                            }
                                        });
                                    }
                                });
                            }
                        };

                        client.onSubscribed = new OnSubscribed (){
                            @Override
                            public void run (OrtcClient sender, String channel){
                                // if (realTimeListener != null)
                                Log.d (LOGTAG, "Subscribed Channel : " + channel);
                                realTimeListener.onSubscribeChannel (sender, channel);
                            }
                        };

                        client.onDisconnected = new OnDisconnected (){
                            @Override
                            public void run (OrtcClient sender){
                                Log.d (LOGTAG, "Disconnect Client");
                            }
                        };

                        client.onUnsubscribed = new OnUnsubscribed (){
                            @Override
                            public void run (OrtcClient sender, String channel){
                                Log.d (LOGTAG, "UnSubscribed Channel : " + channel);
                                client.disconnect ();
                            }
                        };
                    }
                } catch (Exception e){

                }
            }

        } catch (InstantiationException e){

        } catch (IllegalAccessException e){

        } catch (ClassNotFoundException e){

        }
    }

    public void disConnect (Login login){
        String channel = General.CHANNEL_DEV + login.getOrgId ();
        //client.disconnect ();
        client.unsubscribe (channel);
    }

    public interface RealTimeListener{
        void onReceivedResponse (String channel, String message);

        void onSubscribeChannel (OrtcClient sender, String channel);
    }
}
