package com.example.ahmed.androidproject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.Timer;
import java.util.TimerTask;

import Assist.Information;

public class Welcome extends AppCompatActivity {
    TimerTask timerTask;
    SharedPreferences prefs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        prefs = getSharedPreferences(Information.SharedPreferences, MODE_PRIVATE);

        //A task that can be scheduled for one-time or repeated execution by a Timer.
         timerTask = new TimerTask() {
            @Override
            public void run() {
                //The action to be performed by this timer task.
                if(!isNewUser() && prefs.getBoolean(Information.Rememberme,false))
                    startActivity(new Intent(Welcome.this, MainActivity.class));
                else
                    startActivity(new Intent(Welcome.this, SignIn.class));
                finish();
            }
        };
    }

    @Override
    protected void onResume() {
        super.onResume();
        //A facility for threads to schedule tasks for future execution in a background thread.
        // Tasks may be scheduled for one-time execution, or for repeated execution at regular intervals.
        Timer t = new Timer();
        //Schedules the specified task for repeated fixed-delay execution, beginning after the specified delay.
        // TimerTask task, long delay
        t.schedule(timerTask, 2000);
    }
    // return if user registered in SharedPreferences or new user
    private boolean isNewUser(){
        String name_pref = prefs.getString(Information.Username, "NULL");
        String pass_pref = prefs.getString(Information.Password, "NULL");
        if(name_pref.equals("NULL") && pass_pref.equals("NULL"))
            return true;
        return false;
    }
}
