package com.example.testwork;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    SharedPreferences sPref;
    public static boolean firstrun;
    public static int los;
    public static TextView attempts;
    int whatlast;
    public static int loses;
    public static int id;
    Fragment fragment = null;
    public static ImageView refresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sPref = getPreferences(MODE_PRIVATE);
        firstrun = sPref.getBoolean("firstrun", true);
        whatlast = sPref.getInt("last", 1);
        id = sPref.getInt("id", whatlast);
        loses = sPref.getInt("loses", 2147483647);
        Log.d("taag", Boolean.toString(firstrun));
        attempts = findViewById(R.id.attempts);
        if(savedInstanceState == null) {
            transaction(checkinternet());
        }
        refresh = findViewById(R.id.refresh);
        refresh.setVisibility(View.GONE);



        los = loses;
        if(savedInstanceState!=null){
        }

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer);
        ActionBarDrawerToggle togle = new ActionBarDrawerToggle(this,
                drawer, toolbar, R.string.app_name, R.string.app_name);
        drawer.addDrawerListener(togle);
        togle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.blue));
        togle.syncState();
        NavigationView nav = findViewById(R.id.nav_view);
        nav.setNavigationItemSelectedListener(this);
    }

    public boolean checkinternet(){
        boolean connected = false;
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            connected = true;
        }
        else
            connected = false;
        return  connected;
    }

    public void transaction(boolean inetconnected){
        Fragment fragment = null;
        if(firstrun) {
            if (inetconnected) {
                fragment = new Web();
                id = 1;
                whatlast = 1;
            }
            if (!inetconnected) {
                fragment = new Game();
                id = 2;
                whatlast = 2;
            }
        }if(!firstrun){
            if(whatlast == 1){
                fragment = new Web();
                id = 1;
            }else{
                fragment = new Game();
                id=2;
            }
        }
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.add(R.id.frame, fragment, "tag");
            ft.commit();
        }




    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        DrawerLayout drawer =  findViewById(R.id.drawer);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
        switch (item.getItemId()){
            case R.id.web:
                firstrun = true;
                changefragment(true);
                break;
            case R.id.game:
                firstrun = false;
                changefragment(false);
                break;
        }
        return true;
    }

    public void changefragment(boolean web){
        if(web){
            fragment = new Web();
            id = 1;
        }
        else{
            fragment = new Game();
            id = 2;
        }
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.frame, fragment, "tag");
        ft.commit();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        firstrun = false;
        loses = los;
        sPref = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor ed = sPref.edit();
        ed.putBoolean("firstrun", firstrun);
        ed.putInt("last", whatlast);
        ed.putInt("id", id);
        ed.putInt("loses", loses);
        ed.apply();
        refresh = null;
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("id", id);

        }


}