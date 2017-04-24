package Assist;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by ahmed on 13/02/17.
 */

public class Debuger {

    public static void  TOAST(Context c , String msg){
        Toast.makeText(c,msg+"", Toast.LENGTH_LONG).show();
    }
    public static void LOGI(String msg){
        Log.i("TAG", msg);
    }
}
