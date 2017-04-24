package com.example.ahmed.androidproject;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.zip.Inflater;

import Assist.Debuger;
import Assist.Information;

public class MainActivity extends AppCompatActivity {
    Toolbar toolbar;
    DrawerLayout drawlayoutmain;
    ActionBarDrawerToggle actionBarDrawerToggle;
    FragmentTransaction fragmentTransaction;
    NavigationView navigationView;
    SharedPreferences prefs;
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // init custom toolbar
        toolbar=(Toolbar)findViewById(R.id.toolbar);
        // add custom toolbar
        setSupportActionBar(toolbar);
        // init navagitionDrawer
        drawlayoutmain=(DrawerLayout)findViewById(R.id.drawlayoutmain);
        // init Hamburger icon
        // This drawable shows a Hamburger icon when drawer is closed and an arrow when drawer is open.
        // It animates between these two states as the drawer opens.
        actionBarDrawerToggle=new ActionBarDrawerToggle(this,drawlayoutmain,toolbar,0,0);
        // add Hamburger icon to navigationDrawer
        drawlayoutmain.addDrawerListener(actionBarDrawerToggle);
        //  *** Fragment *** //
        // get FragmentManager and start transaction
        fragmentTransaction=getSupportFragmentManager().beginTransaction();
        // create new object from fragment and put in specific layout
        fragmentTransaction.add(R.id.framelayout,new Home());
        fragmentTransaction.commit();
        // add title for action bar
        getSupportActionBar().setTitle("Home");
        /************/
        // get username and password from sharepreference
        prefs = getSharedPreferences(Information.SharedPreferences, MODE_PRIVATE);
        // Navigation Header add it and declare
        navigationView= (NavigationView) findViewById(R.id.navigation);
        View header=navigationView.inflateHeaderView(R.layout.navigationheader);
        TextView name_navigation_header = (TextView)header.findViewById(R.id.name_navigation_header);
        TextView email_navigation_header = (TextView)header.findViewById(R.id.email_navigation_header);
        name_navigation_header.setText(prefs.getString(Information.Username, ""));
        email_navigation_header.setText(prefs.getString(Information.Email, ""));
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId())
                {
                    case R.id.homeN:
                        fragmentTransaction=getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.framelayout,new Home());
                        fragmentTransaction.commit();
                        getSupportActionBar().setTitle("Home");
                        item.setCheckable(true);
                        break;
                    case R.id.signout:
                        SharedPreferences.Editor editor = getSharedPreferences(Information.SharedPreferences, MODE_PRIVATE).edit();
                        editor.putBoolean(Information.Rememberme,false);
                        editor.commit();
                        startActivity(new Intent(MainActivity.this, SignIn.class));
                        finish();
                        break;
                    case R.id.report:
                        fragmentTransaction=getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.framelayout,new Report());
                        fragmentTransaction.commit();
                        getSupportActionBar().setTitle("Reports");
                        item.setCheckable(true);
                        break;
                }
                drawlayoutmain.closeDrawers();
                return true;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        //show Hamburger icon and will synchronize the changed icon's state  ****Arrow to Menu , Menu to Arrow
        actionBarDrawerToggle.syncState();
    }
    @Override
    public void onBackPressed() {
        if(drawlayoutmain.isDrawerOpen(Gravity.LEFT)){
            drawlayoutmain.closeDrawers();
            return;
        }
        else
            finish();
    }
}
