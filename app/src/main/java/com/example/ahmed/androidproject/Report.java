package com.example.ahmed.androidproject;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import helper.Queries;

public class Report extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.activity_report, container, false);
        ArrayList<Integer> mytrips= new Queries(getActivity()).getCountAboutType();
        TextView reportupcoming =(TextView)view.findViewById(R.id.reportupcoming);
        TextView reportdone =(TextView)view.findViewById(R.id.reportdone);
        TextView reportcancel =(TextView)view.findViewById(R.id.reportcancel);
        for(int i=0;i<mytrips.size();i+=2) {
            switch (mytrips.get(i)){
                case 0:
                    reportupcoming.setText(mytrips.get(i+1)+"");
                    break;
                case 1:
                    reportdone.setText(mytrips.get(i+1)+"");
                    break;
                case 2:
                    reportcancel.setText(mytrips.get(i+1)+"");
                    break;
            }
        }
        return view;
    }
}
