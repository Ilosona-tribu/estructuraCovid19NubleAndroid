package com.example.estructuracovid19nuble.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class Settings {
    //Deeplinks
    //Home:         ilosona://topic/home
    //Advices:      ilosona://topic/advices
    //News:         ilosona://topic/news
    //Call:         ilosona://topic/call
    //Maps:         ilosona://topic/maps
    //A news:       ilosona://topic/a_news
    //An advice:    ilosona://topic/an_advice

    // Notification keys
    public static final String KEY_FILE = "com.example.estructuracovid19nuble.utils.key_file";
    public static final String KEY_TOPIC = "key_topic";
    public static final String TOPIC_TEST = "test";
    public static final String TOPIC_ADVICE = "advice";
    public static final String TOPIC_NEWS = "news";
    public static final String TOPIC_MAPS = "maps";
    public static final String TOPIC_CALL = "call";

    // Notification
    public static final String PINPOINT_PUSH_KEY_PREFIX = "pinpoint.";
    public static final String GCM_NOTIFICATION_PUSH_KEY_PREFIX = PINPOINT_PUSH_KEY_PREFIX + "notification.";
    public static final String NOTIFICATION_SILENT_PUSH_KEY = GCM_NOTIFICATION_PUSH_KEY_PREFIX + "silentPush";
    public static final String NOTIFICATION_TITLE_PUSH_KEY = GCM_NOTIFICATION_PUSH_KEY_PREFIX + "title";
    public static final String NOTIFICATION_BODY_PUSH_KEY = GCM_NOTIFICATION_PUSH_KEY_PREFIX + "body";
    public static final String NOTIFICATION_COLOR_PUSH_KEY = GCM_NOTIFICATION_PUSH_KEY_PREFIX + "color";
    public static final String NOTIFICATION_ICON_PUSH_KEY = GCM_NOTIFICATION_PUSH_KEY_PREFIX + "icon";
    public static final String CAMPAIGN_IMAGE_PUSH_KEY = GCM_NOTIFICATION_PUSH_KEY_PREFIX + "imageUrl";
    public static final String CAMPAIGN_IMAGE_ICON_PUSH_KEY = GCM_NOTIFICATION_PUSH_KEY_PREFIX + "imageIconUrl";
    public static final String CAMPAIGN_IMAGE_SMALL_ICON_PUSH_KEY = GCM_NOTIFICATION_PUSH_KEY_PREFIX + "imageSmallIconUrl";


    public static final String INTENT_SNS_NOTIFICATION_FROM = "from";
    public static final String INTENT_SNS_NOTIFICATION_DATA = "data";

    public static void putKey(Context context, String key, String value) {
        SharedPreferences sharedPref = context.getSharedPreferences(KEY_FILE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static String getKey(Context context, String key) {
        SharedPreferences sharedPref = context.getSharedPreferences(KEY_FILE, Context.MODE_PRIVATE);
        return sharedPref.getString(key, "");
    }

    public static String getKeyOnce(Context context, String key) {
        String value = getKey(context, key);
        putKey(context, key, "");
        return value;
    }

    public static void putKey(Context context, String key, String value, String timeId) {
        if (value != null) {
            putKey(context, key + "_" + timeId, value);
        }
    }

    public static String getKeyOnce(Context context, String key, String timeId) {
        return getKeyOnce(context, key + "_" + timeId);
    }
}
