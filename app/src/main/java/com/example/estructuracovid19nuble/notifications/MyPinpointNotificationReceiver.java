package com.example.estructuracovid19nuble.notifications;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;

public class MyPinpointNotificationReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
//        if (notificationClient != null) {
//            final String prefix = NotificationClientBase.CAMPAIGN_PUSH_KEY_PREFIX;
//            final Map<String, String> campaignAttributes = new HashMap<String, String>();
//            campaignAttributes.put(NotificationClientBase.CAMPAIGN_ID_ATTRIBUTE_KEY,
//                    intent.getStringExtra(prefix.concat(NotificationClientBase.CAMPAIGN_ID_ATTRIBUTE_KEY)));
//            campaignAttributes
//                    .put(NotificationClientBase.CAMPAIGN_TREATMENT_ID_ATTRIBUTE_KEY,
//                            intent.getStringExtra(prefix.concat(NotificationClientBase.CAMPAIGN_TREATMENT_ID_ATTRIBUTE_KEY)));
//            campaignAttributes
//                    .put(NotificationClientBase.CAMPAIGN_ACTIVITY_ID_ATTRIBUTE_KEY,
//                            intent.getStringExtra(prefix.concat(NotificationClientBase.CAMPAIGN_ACTIVITY_ID_ATTRIBUTE_KEY)));
//            notificationClient.handleNotificationOpen(campaignAttributes,
//                    intent.getExtras());
        final PackageManager pm = context.getPackageManager();
        final Intent launchIntent = pm.getLaunchIntentForPackage(intent.getPackage());
        launchIntent.putExtras(intent.getExtras());
        context.startActivity(launchIntent);
    }
}
