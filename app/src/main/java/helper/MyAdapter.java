package helper;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ahmed.androidproject.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import Assist.Debuger;
import Assist.Information;

/**
 * Created by ahmed on 22/02/17.
 */

public class MyAdapter extends ArrayAdapter<Trip> {
    Context context;
    ArrayList<Trip> trips;
    public MyAdapter(Context context,ArrayList<Trip> trips){
        super(context, R.layout.rowdynamicimage,trips);
        this.context=context;
        this.trips=trips;
    }

    @Override
    public View getView (int position, View convertview, ViewGroup parent){
        ViewCach viewcach;
        ImageView imgstaticimage; TextView rownameoftrip,rowdatetimeoftrip;
        View rowview=convertview;
        if(rowview==null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowview = inflater.inflate(R.layout.rowdynamicimage, parent, false);
            viewcach=new ViewCach(rowview);
            rowview.setTag(viewcach);

        }else{
            viewcach=(ViewCach)rowview.getTag();
        }
        //imgstaticimage=viewcach.getmyimg();
        rownameoftrip =viewcach.getname();
        rowdatetimeoftrip=viewcach.getdate();
        //imgstaticimage.setImageResource(arr.get(position).img);
        rownameoftrip.setText(trips.get(position).name);
        rowdatetimeoftrip.setText(trips.get(position).startTime);
        //String originalString = "2010-07-14 09:00:02";
        Date date = null;
        try {
            date = Information.dateformate.parse(trips.get(position).startTime);
            rowdatetimeoftrip.setText(new SimpleDateFormat("dd MMM").format(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        //String newString = ; // 9:00

        return rowview;
    }

     class ViewCach {
        private View myview;
        private TextView rownameoftrip,rowdatetimeoftrip;

        public ViewCach(View myview){
            this.myview=myview;
        }
        public TextView getname(){
            if(rownameoftrip ==null)
                rownameoftrip=(TextView)myview.findViewById(R.id.rownameoftrip);
            return  rownameoftrip;
        }
         public TextView getdate(){
             if(rowdatetimeoftrip ==null)
                 rowdatetimeoftrip=(TextView)myview.findViewById(R.id.rowdatetimeoftrip);
             return  rowdatetimeoftrip;
         }
    }
}
