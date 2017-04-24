package com.example.ahmed.androidproject;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Date;

import Assist.Debuger;
import Assist.Information;
import helper.Queries;
import helper.Trip;
import helper.TripStatus;

/**
 * Created by amany on 2/17/2017.
 */

public class DetailsOfTrip extends AppCompatActivity implements View.OnClickListener{
    Toolbar toolbar;
    int id;
    TextView text_name, text_date, text_startPoint, text_destination, text_duration, text_speed, text_note, text_with,triptype,text_remindertime;
    ImageView map,editicon,deleteicon,reminderoff;
    ProgressBar load;
    RadioButton upcomming, done, cancelled;
    Trip trip;Queries queries;
    LinearLayout linearcontianimg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detailsoftrip);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Details Trip");
        // declare textview
        text_name = (TextView) findViewById(R.id.text_name);
        text_date = (TextView) findViewById(R.id.text_date);
        text_startPoint = (TextView) findViewById(R.id.text_startPoint);
        text_destination = (TextView) findViewById(R.id.text_destination);
        text_duration = (TextView) findViewById(R.id.text_duration);
        text_speed = (TextView) findViewById(R.id.text_speed);
        text_note = (TextView) findViewById(R.id.text_note);
        text_with = (TextView) findViewById(R.id.text_with);
        text_remindertime=(TextView) findViewById(R.id.text_remindertime);
        // declare radiobutton
        upcomming = (RadioButton) findViewById(R.id.upcomming);
        upcomming.setOnClickListener(this);
        done = (RadioButton) findViewById(R.id.done);
        done.setOnClickListener(this);
        cancelled = (RadioButton) findViewById(R.id.cancelled);
        cancelled.setOnClickListener(this);
        //declare progress bar
        load = (ProgressBar) findViewById(R.id.load);
        //declare imageview
        triptype = (TextView) findViewById(R.id.triptype);
        map = (ImageView) findViewById(R.id.map);
        editicon = (ImageView) findViewById(R.id.editicon);
        deleteicon = (ImageView) findViewById(R.id.deleteicon);
        reminderoff= (ImageView) findViewById(R.id.reminderoff);
        //declare linearlayout
        linearcontianimg=(LinearLayout)findViewById(R.id.linearcontianimg);
        map.setOnClickListener(this);
        editicon.setOnClickListener(this);
        deleteicon.setOnClickListener(this);
        reminderoff.setOnClickListener(this);
        id = getIntent().getIntExtra("myid", 0);
        if (id <= 0)
            finish();
    }

    @Override
    protected void onStart() {
        super.onStart();
        queries = new Queries(this);
        trip=queries.getTrip(id);
        setData();
    }

    private void setData() {
        new GetDuration().execute(Information.getUrl(trip.startPoint.replace(" ", "%20"), trip.destination.replace(" ", "%20")));
        text_name.setText(trip.name);
        text_date.setText(trip.startTime);
        text_startPoint.setText(trip.startPoint);
        text_destination.setText(trip.destination);
        text_note.setText(trip.note);
        text_with.setText(trip.with);
        text_remindertime.setText(trip.reminderTime);
        if (trip.oneDirection)
            triptype.setText("One Direction");
        else
            triptype.setText("Round Trip");
        switch (trip.myTripStatus) {
            case Upcomming:
                upcomming.setChecked(true);
                break;
            case Done:
                done.setChecked(true);
                break;
            case Cancelled:
                cancelled.setChecked(true);
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.upcomming:
                trip.myTripStatus= TripStatus.Upcomming;
                queries.updateTrip(trip,trip.id);
                break;
            case R.id.done:
                trip.myTripStatus= TripStatus.Done;
                queries.updateTrip(trip,trip.id);
                if(!trip.oneDirection){
                    ShowGobackDialgo();
                }
                break;
            case R.id.cancelled:
                trip.myTripStatus= TripStatus.Cancelled;
                queries.updateTrip(trip,trip.id);
                break;
            case R.id.map:
                startActivity(Information.getMap(text_startPoint.getText().toString(),text_destination.getText().toString()));
                break;
            case R.id.deleteicon:
                ShowDeleteDialgo();
                break;
            case R.id.editicon:
                Intent myintent = new Intent(DetailsOfTrip.this,SaveTrip.class);
                myintent.putExtra("myid",trip.id);
                startActivity(myintent);
                finish();
                break;
            case R.id.reminderoff:
                Information.cancelReminder(this,trip.id);
                Queries queries = new Queries(this);
                trip.reminderTime="";
                if(queries.updateTrip(trip,trip.id)==1);
                text_remindertime.setText("");
                break;
        }
    }
    private void ShowDeleteDialgo(){
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle("Are You Sure ...? ");
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        queries.deleteTrip(trip.id);
                        finish();
                    }
                });
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }
    private void ShowGobackDialgo(){
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle("Do you go back now .. ?");
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        String temp=trip.startPoint;
                        trip.startPoint=trip.destination;
                        trip.destination=temp;
                        trip.oneDirection=true;
                        trip.reminderTime="";
                        trip.name="return from "+trip.name;
                        trip.startTime= Information.dateformate.format(Calendar.getInstance().getTime());
                        if(queries.insertTrip(trip)>0)
                            Debuger.TOAST(DetailsOfTrip.this,"Your Trip Start Now");
                        finish();

                    }
                });
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }
    private String CalcAveraageSpeed(String[] duration,float distance){
        double time=0.0;
        for (int i=1;i<duration.length;i++) {
            switch (duration[i]){
                case "hours":
                    time=Double.parseDouble(duration[i-1]);
                break;
                case "mins":
                    time+= Double.parseDouble(duration[i-1])*0.0166667;
                break;
            }
        }
        return new DecimalFormat("#.00").format(distance/time);
    }

    class GetDuration extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            String result = "";
            try {
                result = dowload(params[0]);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return result;
        }

        protected void onPostExecute(String response) {
            String time = "";
            float distance=0;
            String result="";
            if (response != null) {
                try {
                    JSONObject object = new JSONObject(response);
                    JSONArray getrow = object.getJSONArray("rows");
                    JSONObject row = getrow.getJSONObject(0);
                    JSONArray getelement = row.getJSONArray("elements");
                    JSONObject element = getelement.getJSONObject(0);

                    if (element.getString("status").equals("OK")) {
                        time = element.getJSONObject("duration").getString("text");
                        distance=Float.parseFloat(element.getJSONObject("distance").getString("text").split(" ")[0].replace(",","."));
                        result=CalcAveraageSpeed(time.split(" "),distance)+" k/h";
                    } else {
                        time="Sorry Something Wrong happened";
                        result=0+" k/h";
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            text_duration.setText(time);
            text_speed.setText(result);
            load.setVisibility(View.GONE);
            linearcontianimg.setVisibility(View.VISIBLE);
        }

        public String dowload(String urlstring) throws IOException {
            String response = null;
            try {
                URL url = new URL(urlstring);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                InputStream in = new BufferedInputStream(conn.getInputStream());
                response = convertStreamToString(in);
            } catch (MalformedURLException e) {
                Debuger.LOGI("MalformedURLException: " + e.getMessage());
            } catch (ProtocolException e) {
                Debuger.LOGI("ProtocolException: " + e.getMessage());
            } catch (IOException e) {
                Debuger.LOGI("IOException: " + e.getMessage());
            } catch (Exception e) {
                Debuger.LOGI("Exception: " + e.getMessage());
            }

            return response;
        }

        private String convertStreamToString(InputStream is) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            StringBuilder sb = new StringBuilder();
            String line;
            try {
                while ((line = reader.readLine()) != null) {
                    sb.append(line).append('\n');
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return sb.toString();
        }
    }

}
