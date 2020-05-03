package com.example.estructuracovid19nuble;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.amazonaws.mobileconnectors.pinpoint.targeting.notification.NotificationClient;
import com.amazonaws.mobileconnectors.pinpoint.targeting.notification.NotificationDetails;
import com.example.estructuracovid19nuble.utils.Settings;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.HashMap;
import java.util.Map;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

//Galaxy S7 id: 936d4109-d26c-4b88-9789-60faf826e475
// token: eu-b8oUFTy-X5bnNmiBve7:APA91bFErh706Yhk5xr7Z4YqBG_R06zhtU0VG7bJsdiPtAlZI36kazXnbhtovXdh0FWUYD5DAUEigVUNXVK2iJQTSe_RSYxhCW83qyJF59nWng8i36YUpzBf-1_H_tPmdRlu-kmrM6yp
public class PushListenerService extends FirebaseMessagingService {
    public static final String TAG = PushListenerService.class.getSimpleName();

    // Intent action used in local broadcast
    public static final String ACTION_PUSH_NOTIFICATION = "push-notification";

    protected static final String PINPOINT_PUSH_KEY_PREFIX = "pinpoint.";
    private static final String CAMPAIGN_URL_PUSH_KEY = PINPOINT_PUSH_KEY_PREFIX + "url";
    private static final String CAMPAIGN_DEEP_LINK_PUSH_KEY = PINPOINT_PUSH_KEY_PREFIX + "deeplink";
    private static final String CAMPAIGN_OPEN_APP_PUSH_KEY = PINPOINT_PUSH_KEY_PREFIX + "openApp";

    @Override
    public void onNewToken(String token) {
        super.onNewToken(token);

        Log.d(TAG, "Registering push notifications token: " + token);
        MainActivity.getPinpointManager(getApplicationContext()).getNotificationClient().registerDeviceToken(token);
    }

    private void retrieveNSaveMessage(final NotificationDetails notificationDetails, String timeId) {

        final Bundle data = notificationDetails.getBundle();
        final String imageUrl = data.getString(Settings.CAMPAIGN_IMAGE_PUSH_KEY);
        final String imageIconUrl = data.getString(Settings.CAMPAIGN_IMAGE_ICON_PUSH_KEY);
        final String imageSmallIconUrl = data.getString(Settings.CAMPAIGN_IMAGE_SMALL_ICON_PUSH_KEY);
        final String title = data.getString(Settings.NOTIFICATION_TITLE_PUSH_KEY);
        final String message = data.getString(Settings.NOTIFICATION_BODY_PUSH_KEY);

        Settings.putKey(getApplicationContext(), (Settings.CAMPAIGN_IMAGE_PUSH_KEY), imageUrl, timeId);
        Settings.putKey(getApplicationContext(), Settings.CAMPAIGN_IMAGE_ICON_PUSH_KEY, imageIconUrl, timeId);
        Settings.putKey(getApplicationContext(), Settings.CAMPAIGN_IMAGE_SMALL_ICON_PUSH_KEY, imageSmallIconUrl, timeId);
        Settings.putKey(getApplicationContext(), Settings.NOTIFICATION_TITLE_PUSH_KEY, title, timeId);
        Settings.putKey(getApplicationContext(), Settings.NOTIFICATION_BODY_PUSH_KEY, message, timeId);
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Map<String, String> remoteData = remoteMessage.getData();
        Log.d(TAG, "Message: " + remoteData);

        String deepLink = remoteData.get(CAMPAIGN_DEEP_LINK_PUSH_KEY);
        final String timeId = System.currentTimeMillis() + "";
        if (deepLink != null && (deepLink.contains("a_news") || deepLink.contains("an_advice"))) {
            //1. TODO adding time_id argument
            Log.d(TAG, deepLink);
            deepLink += "?timeid=" + timeId;
            remoteData.remove(CAMPAIGN_DEEP_LINK_PUSH_KEY);
            remoteData.put(CAMPAIGN_DEEP_LINK_PUSH_KEY, deepLink);
            Log.d(TAG, deepLink);
            Log.d(TAG, "Message: " + remoteData);
        }

        final NotificationClient notificationClient = MainActivity.getPinpointManager(getApplicationContext()).getNotificationClient();

        final NotificationDetails notificationDetails = NotificationDetails.builder()
                .from(remoteMessage.getFrom())
                .mapData(remoteData)
                .intentAction(NotificationClient.FCM_INTENT_ACTION)
                .build();
        if (deepLink != null) {
            //2. TODO Save push message info into pref file, later load in deeplinked fragment
            retrieveNSaveMessage(notificationDetails, timeId);
        }

        NotificationClient.CampaignPushResult pushResult = notificationClient.handleCampaignPush(notificationDetails);

        if (NotificationClient.CampaignPushResult.APP_IN_FOREGROUND.equals(pushResult)) {
            Toast.makeText(getApplicationContext(), "in-app:pushed: " + deepLink, Toast.LENGTH_SHORT).show();

            //TODO: perform in-app action correpond to deeplink.

            //TODO: (maybe) skip saving deeplink as pref key?
        }

        if (!NotificationClient.CampaignPushResult.NOT_HANDLED.equals(pushResult)) {
            /**
             The push message was due to a Pinpoint campaign.
             If the app was in the background, a local notification was added
             in the notification center. If the app was in the foreground, an
             event was recorded indicating the app was in the foreground,
             for the demo, we will broadcast the notification to let the main
             activity display it in a dialog.
             */
            if (NotificationClient.CampaignPushResult.APP_IN_FOREGROUND.equals(pushResult)) {
                /* Create a message that will display the raw data of the campaign push in a dialog. */
//                final HashMap<String, String> dataMap = new HashMap<>(remoteMessage.getData());
                final HashMap<String, String> dataMap = new HashMap<>(remoteData);
                broadcast(remoteMessage.getFrom(), dataMap);
            }
            return;
        }
    }

    private void broadcast(final String from, final HashMap<String, String> dataMap) {
        Intent intent = new Intent(ACTION_PUSH_NOTIFICATION);
        intent.putExtra(Settings.INTENT_SNS_NOTIFICATION_FROM, from);
        intent.putExtra(Settings.INTENT_SNS_NOTIFICATION_DATA, dataMap);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    /**
     * Helper method to extract push message from bundle.
     *
     * @param data bundle
     * @return message string from push notification
     */
    public static String getMessage(Bundle data) {
        return ((HashMap) data.get(Settings.INTENT_SNS_NOTIFICATION_DATA)).toString();
    }
}