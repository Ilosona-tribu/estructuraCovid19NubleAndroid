package com.example.estructuracovid19nuble;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.amazonaws.mobile.client.AWSMobileClient;
import com.amazonaws.mobile.client.Callback;
import com.amazonaws.mobile.client.UserStateDetails;
import com.amazonaws.mobile.config.AWSConfiguration;
import com.amazonaws.mobileconnectors.pinpoint.PinpointConfiguration;
import com.amazonaws.mobileconnectors.pinpoint.PinpointManager;
import com.example.estructuracovid19nuble.utils.MyApp;
import com.example.estructuracovid19nuble.utils.Settings;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import java.util.HashMap;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = MainActivity.class.getSimpleName();
    private MyApp myApp;
    private static PinpointManager pinpointManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.MyappTheme);
        super.onCreate(savedInstanceState);
        Log.e("onCreateView", "MainActivity");
        setContentView(R.layout.activity_main);

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_test, R.id.navigation_advice, R.id.navigation_news,
                R.id.navigation_map, R.id.navigation_call)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        //NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

        myApp = MyApp.getInstance();

        // Initialize PinpointManager
        getPinpointManager(getApplicationContext());

//        //Query notification info if exist
//        String deeplink = Settings.getKeyOnce(getApplicationContext(), Settings.KEY_TOPIC);
//        Log.e(TAG, "Message: " + deeplink);
//        if (!deeplink.isEmpty()){
//            Log.e(TAG, "Message: do sth with " + deeplink);
//            int itemId = getTopicId(deeplink);
//            navView.setSelectedItemId(itemId);
//            getTopicId(deeplink);
//        }
    }

    private int getTopicId(String topic_in){
        String topic = topic_in.toLowerCase();
        if (topic.contains(Settings.TOPIC_NEWS)){
            return R.id.navigation_news;
        }
        if (topic.contains(Settings.TOPIC_ADVICE)){
            return R.id.navigation_advice;
        }
        if (topic.contains(Settings.TOPIC_MAPS)){
            return R.id.navigation_map;
        }
        if (topic.contains(Settings.TOPIC_CALL)){
            return R.id.navigation_call;
        }
        return R.id.navigation_test;
    }

    private void checkNotification() {
        String from = getIntent().getStringExtra(Settings.INTENT_SNS_NOTIFICATION_FROM);
        if (from != null){
            HashMap<String, String> dataMap = (HashMap<String, String>) getIntent().getSerializableExtra(Settings.INTENT_SNS_NOTIFICATION_DATA);
            Log.e("notification click", from + "");
            Log.e("notification click", dataMap.toString());
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public MyApp getMyApp(){
        if (myApp == null){
            myApp = MyApp.getInstance();
        }
        return myApp;
    }

    public static PinpointManager getPinpointManager(final Context applicationContext) {
        if (pinpointManager == null) {
            final AWSConfiguration awsConfig = new AWSConfiguration(applicationContext);
            AWSMobileClient.getInstance().initialize(applicationContext, awsConfig, new Callback<UserStateDetails>() {
                @Override
                public void onResult(UserStateDetails userStateDetails) {
                    Log.i("INIT", userStateDetails.getUserState() + "");
                }

                @Override
                public void onError(Exception e) {
                    Log.e("INIT", "Initialization error.", e);
                }
            });

            PinpointConfiguration pinpointConfig = new PinpointConfiguration(
                    applicationContext,
                    AWSMobileClient.getInstance(),
                    awsConfig).withPostNotificationsInForeground(true);

            pinpointManager = new PinpointManager(pinpointConfig);

            FirebaseInstanceId.getInstance().getInstanceId()
                    .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                        @Override
                        public void onComplete(@NonNull Task<InstanceIdResult> task) {
                            if (!task.isSuccessful()) {
                                Log.w(TAG, "getInstanceId failed", task.getException());
                                return;
                            }
                            final String token = task.getResult().getToken();
                            Log.d(TAG, "Registering push notifications token: " + token);
                            pinpointManager.getNotificationClient().registerDeviceToken(token);
                        }
                    });
        }
        return pinpointManager;
    }
}
