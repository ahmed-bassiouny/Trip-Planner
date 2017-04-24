package com.example.ahmed.androidproject;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;


import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

import Assist.Debuger;
import Assist.Information;
import helper.AlarmReceiver;
import helper.Queries;
import helper.Trip;
import helper.TripStatus;

/**
 * Created by amany on 2/16/2017.
 */

public class SaveTrip extends AppCompatActivity implements View.OnClickListener {

    private Spinner typeSpinner;
    Toolbar toolbar;
    int PLACE_AUTOCOMPLETE_REQUEST_CODE_FROM = 1;
    int PLACE_AUTOCOMPLETE_REQUEST_CODE_TO = 2;
    FloatingActionButton save,cancel;
    Calendar date;
    Trip TRIP = new Trip();
    EditText edit_trip_name, txt_datetime, txt_startpoint, txt_destination, with, edit_note, txt_remindertime;
    //ImageView img__datetime, img_startpoint, img_destination, img__remindertime;
    Queries queries = new Queries(this);
    int getfromintent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.savedesign);
        toolbar = (Toolbar) findViewById(R.id.include);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Add ,Edit Trip");
        typeSpinner = (Spinner) findViewById(R.id.spinner_type);

        edit_trip_name = (EditText) findViewById(R.id.edit_trip_name);
        txt_datetime = (EditText) findViewById(R.id.txt_datetime);
        txt_startpoint = (EditText) findViewById(R.id.txt_startpoint);
        txt_destination = (EditText) findViewById(R.id.txt_destination);
        with = (EditText) findViewById(R.id.with);
        edit_note = (EditText) findViewById(R.id.edit_note);
        txt_remindertime = (EditText) findViewById(R.id.txt_remindertime);


        save=(FloatingActionButton)findViewById(R.id.save);
        cancel=(FloatingActionButton)findViewById(R.id.btn_cancle);

        // Create adapter for spinner. The list options are from the String array it will use
        // the spinner will use the default layout
        ArrayAdapter genderSpinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.type_array, android.R.layout.simple_spinner_item);

        // Specify dropdown layout style - simple list view with 1 item per line
        genderSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

        // Apply the adapter to the spinner
        typeSpinner.setAdapter(genderSpinnerAdapter);

        txt_datetime.setOnClickListener(this);
        txt_remindertime.setOnClickListener(this);
        txt_startpoint.setOnClickListener(this);
        txt_destination.setOnClickListener(this);
        save.setOnClickListener(this);
        cancel.setOnClickListener(this);
        getfromintent=getIntent().getIntExtra("myid",0);
        if(getfromintent>0)
            setData(getfromintent);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.save:
                getDataFromUser();
                finish();
                break;
            case R.id.btn_cancle:
                onBackPressed();
                break;
            case R.id.txt_datetime:
                viewDialogeDate(txt_datetime);
                break;
            case R.id.txt_remindertime:
                viewDialogeDate(txt_remindertime);
                break;
            case R.id.txt_startpoint:
                getlocation(PLACE_AUTOCOMPLETE_REQUEST_CODE_FROM);
                break;
            case R.id.txt_destination:
                getlocation(PLACE_AUTOCOMPLETE_REQUEST_CODE_TO);
                break;
        }

    }

    public void viewDialogeDate(final EditText dynamicedittext) {
         date = Calendar.getInstance();
        //The initial year,month,day of the dialog.
        //first paramanter in DatePickerDialog callBack - How the parent is notified that the date is set
        DatePickerDialog dpd = DatePickerDialog.newInstance(
                null,
                date.get(Calendar.YEAR),
                date.get(Calendar.MONTH),
                date.get(Calendar.DAY_OF_MONTH)
        );
        dpd.setOnDateSetListener(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
                date.set(year,monthOfYear,dayOfMonth);
                TimePickerDialog tpd = TimePickerDialog.newInstance(
                        null,
                        date.get(Calendar.HOUR_OF_DAY),
                        date.get(Calendar.MINUTE),
                        false
                );
                tpd.setOnTimeSetListener(new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute) {
                        date.set(Calendar.HOUR_OF_DAY,hourOfDay);
                        date.set(Calendar.MINUTE,minute);
                        dynamicedittext.setText(Information.dateformate.format(date.getTime()));
                    }
                });
                tpd.show(getFragmentManager(), "Timepickerdialog");
            }
        });

        dpd.show(getFragmentManager(), "Datepickerdialog");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == SaveTrip.this.RESULT_OK) {
            Place place = PlaceAutocomplete.getPlace(SaveTrip.this, data);

            if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE_FROM) {
                txt_startpoint.setText(place.getAddress());
                //firstplace=place.getAddress().toString().replace(" ","_");
            } else if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE_TO) {
                txt_destination.setText(place.getAddress());
                //secondplace=place.getAddress().toString().replace(" ","_");
                //new GetDuration().execute(Information.getUrl(firstplace,secondplace));
            }
        } else {
            Debuger.TOAST(SaveTrip.this, "Sorry ... Something Wrong Happened");
        }
    }

    private void getlocation(int PLACE_AUTOCOMPLETE_REQUEST_CODE) {
        try {
            //Creates a new builder that creates an intent to launch the autocomplete activity.
            Intent intent =
                    new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY)
                            .build(SaveTrip.this);
            startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
        } catch (GooglePlayServicesRepairableException e) {

        } catch (GooglePlayServicesNotAvailableException e) {

        }
    }
    private void getDataFromUser(){
        if(!validate(new EditText []{edit_trip_name, txt_datetime, txt_startpoint, txt_destination }))
            Debuger.TOAST(this,"your data invalid please check it ");
        else{
            TRIP.name=edit_trip_name.getText().toString();
            TRIP.startTime=txt_datetime.getText().toString();
            TRIP.startPoint=txt_startpoint.getText().toString();
            TRIP.destination=txt_destination.getText().toString();
            TRIP.note=edit_note.getText().toString();
            TRIP.with=with.getText().toString();
            TRIP.reminderTime=txt_remindertime.getText().toString();
            if(!typeSpinner.getSelectedItem().toString().equals("onedirection"))
                TRIP.oneDirection=false;
            try {
                Date date=Information.dateformate.parse(TRIP.startTime);
                if(date.after(new Date()))
                    TRIP.myTripStatus=TripStatus.Upcomming;
                else
                    TRIP.myTripStatus=TripStatus.Done;
            } catch (ParseException e) {
                TRIP.myTripStatus=TripStatus.Cancelled;
            }

            if(getfromintent==0)
                executeInsert();
            else
                executeUpdate();
        }
    }
    private boolean validate(EditText[] fields){
        for(int i=0; i<fields.length; i++){
            EditText currentField=fields[i];
            if(currentField.getText().toString().isEmpty()){
                return false;
            }
        }
        return true;
    }
    private void setData(int id){
     Trip trip=queries.getTrip(id);
        edit_trip_name.setText(trip.name);
        txt_datetime.setText(trip.startTime);
        txt_startpoint.setText(trip.startPoint);
        txt_destination.setText(trip.destination);
        edit_note.setText(trip.note);
        with.setText(trip.with);
        txt_remindertime.setText(trip.reminderTime);
        if(trip.oneDirection)
            typeSpinner.setSelection(0);
        else
            typeSpinner.setSelection(1);
    }
    private void executeInsert(){
        int id=(int)queries.insertTrip(TRIP);
        if(id>0){
            Debuger.TOAST(this,"Your Trip Added ");
            if(!txt_remindertime.getText().toString().isEmpty())
                Information.SetReminder(this,id,txt_remindertime.getText().toString());
        }else{
            Debuger.TOAST(this,"Can't Add your Trip ");
        }
    }
    private void executeUpdate(){
        if(queries.updateTrip(TRIP,getfromintent)==1){
            Debuger.TOAST(this,"Your Trip Updated ");
            if(!txt_remindertime.getText().toString().isEmpty())
                Information.SetReminder(this,getfromintent,txt_remindertime.getText().toString());
        }else{
            Debuger.TOAST(this,"Can't Update your Trip ");
        }
    }

}

