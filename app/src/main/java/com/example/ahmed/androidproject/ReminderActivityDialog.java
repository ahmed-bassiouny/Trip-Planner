package com.example.ahmed.androidproject;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import Assist.Information;
import helper.Queries;
import helper.Trip;
import helper.TripStatus;

public class ReminderActivityDialog extends AppCompatActivity implements View.OnClickListener {
    TextView remindertripnote;
    Button remindercancel,reminderlater,reminderstart;
    Queries queries;Trip trip;
    MediaPlayer mp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder_dialog);
        remindertripnote=(TextView)findViewById(R.id.remindertripnote);
        remindercancel=(Button)findViewById(R.id.remindercancel);
        reminderlater=(Button)findViewById(R.id.reminderlater);
        reminderstart=(Button)findViewById(R.id.reminderstart);
        int id =getIntent().getIntExtra("idtripinreminder",0);
        if(id< Information.ID)
            finish();

        id=id-Information.ID;
        trip = new Trip();
        queries=new Queries(this);
        trip=queries.getTrip(id);
        remindercancel.setOnClickListener(this);
        reminderlater.setOnClickListener(this);
        reminderstart.setOnClickListener(this);
        setTitle(trip.name);
        remindertripnote.setText(trip.note);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mp = MediaPlayer.create(ReminderActivityDialog.this, R.raw.alart2);
        mp.start();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.remindercancel:
                trip.myTripStatus= TripStatus.Cancelled;
                queries.updateTrip(trip,trip.id);
                break;
            case R.id.reminderlater:
                Information.shownNotifiction(this,trip.startPoint,trip.destination,trip.id,trip.name);
                break;
            case R.id.reminderstart:
                startActivity(Information.getMap(trip.startPoint,trip.destination));
                break;
        }
        finish();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mp.stop();
    }
}
