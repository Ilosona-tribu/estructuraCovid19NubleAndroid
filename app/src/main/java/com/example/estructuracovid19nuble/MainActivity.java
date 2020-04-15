package com.example.estructuracovid19nuble;

import android.os.Bundle;

import com.example.estructuracovid19nuble.utils.MyApp;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

public class MainActivity extends AppCompatActivity {
    private MyApp myApp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.MyappTheme);
        super.onCreate(savedInstanceState);
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
    }

    public MyApp getMyApp(){
        if (myApp == null){
            myApp = MyApp.getInstance();
        }
        return myApp;
    }

}
