package com.ahsailabs.almuwahhidplayer.services;

import com.ahsailabs.alcore.utils.NotificationUtil;
import com.ahsailabs.almuwahhidplayer.R;
import com.ahsailabs.almuwahhidplayer.pages.home.MainActivity;
import com.ahsailabs.alutils.PrefsData;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by ahsai on 8/22/2017.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    final public static String smartFirebaseMessagingServiceTAG = "SmartFirebaseMessagingService";

    @Override
    public void onNewToken(String refreshedToken) {
        PrefsData.setPushyToken(refreshedToken);
        PrefsData.setPushyTokenSent(false);
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        String notifTitle = null;
        String notifBody=null;
        String clickAction=null;
        RemoteMessage.Notification notification = remoteMessage.getNotification();
        if(notification != null) {
            notifTitle = remoteMessage.getNotification().getTitle();
            notifBody = remoteMessage.getNotification().getBody();
        }

        NotificationUtil.onMessageReceived(getBaseContext(),remoteMessage.getData(), notifTitle, notifBody
                ,MainActivity.class, MainActivity.class, null, R.string.app_name,R.mipmap.ic_launcher, null, PrefsData.isAccountLoggedIn());
    }
}
