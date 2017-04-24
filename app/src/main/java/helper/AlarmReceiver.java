package helper;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.ahmed.androidproject.ReminderActivityDialog;

import Assist.Debuger;

/**
 * Created by ahmed on 26/02/17.
 */

public class AlarmReceiver extends BroadcastReceiver {

    //static boolean dialogLabelStart = false;

    @Override
    public void onReceive(Context context, Intent intent) {

        Intent viewreminder = new Intent(context,ReminderActivityDialog.class);
        viewreminder.putExtra("idtripinreminder",intent.getIntExtra("idtripinreminder",0));
        viewreminder.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(viewreminder);
    }
}