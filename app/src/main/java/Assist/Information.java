package Assist;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.TaskStackBuilder;

import com.example.ahmed.androidproject.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import helper.AlarmReceiver;
import helper.Trip;

import static android.content.Context.NOTIFICATION_SERVICE;
import static android.content.Intent.ACTION_VIEW;

/**
 * Created by ahmed on 13/02/17.
 */

public class Information {
    public static int ID=100;
    public static String SharedPreferences ="MySharedPreferences";
    public static String Username ="username";
    public static String Password ="Password";
    public static String Email ="Email";
    public static String Rememberme="Rememberme";
    public static String MatrixAPI="AIzaSyAaDtvubZFbY4m1Lw-hgCL1bVczmT7tcAg";
    public static SimpleDateFormat dateformate = new SimpleDateFormat("dd-MM-yyyy hh:mm a");
    public static String[] GetLatLng(String LatLng){
        // LatLan =  Lat/Lan (15.2,15.9)
        // it's method remove some string
        return LatLng.replace("lat/lng: (","").replace(")","").split(",");
    }
    public static String getUrl(String from , String to){
        return "https://maps.googleapis.com/maps/api/distancematrix/json?units=metric&origins="+from+"&destinations="+to+"&key="+MatrixAPI;
    }
    public static void SetReminder(Context context, int id,String time){
        Calendar cal=Calendar.getInstance();
        try {
            Intent intent = new Intent(context, AlarmReceiver.class);
            intent.putExtra("idtripinreminder",(ID+id));
            PendingIntent sender = PendingIntent.getBroadcast(context, (ID+id), intent, PendingIntent.FLAG_UPDATE_CURRENT);
            AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            cal.setTime(Information.dateformate.parse(time));
            am.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), sender);
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }
    public static void cancelReminder(Context context,int id)
    {
        Intent intent = new Intent(context, AlarmReceiver.class);
        PendingIntent sender = PendingIntent.getBroadcast(context,(ID+id), intent, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(sender);
        Debuger.TOAST(context,"Reminder Cancelled");
    }

    public static void shownNotifiction(Context context,String startAdrress,String destinationAddress,int id,String tripName)
    {
        TaskStackBuilder stackBuilder= TaskStackBuilder.create(context);
        stackBuilder.addParentStack(context.getClass());
        stackBuilder.addNextIntent(getMap(startAdrress,destinationAddress));
        PendingIntent pendingIntent= stackBuilder.getPendingIntent(0,PendingIntent.FLAG_UPDATE_CURRENT);
        Notification notification= new Notification.Builder(context).setContentText("LET'S START TRIP").setContentTitle(tripName)
                .setSmallIcon(R.drawable.ic_launcher)
                .setDefaults(Notification.DEFAULT_VIBRATE)
                .setDefaults(Notification.PRIORITY_HIGH)
                .setContentIntent(pendingIntent)
                .build();

        NotificationManager notificationManager= (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(id,notification);

    }
    public static Intent getMap(String startPoint,String destination){
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("http://maps.google.com/maps?saddr=" + startPoint+ "&daddr="+destination));
        intent.setPackage("com.google.android.apps.maps");
        return intent;
    }
}
