package com.example.ahmed.androidproject;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import helper.MyAdapter;
import helper.Queries;
import helper.Trip;
import helper.TripStatus;

/**
 * Created by amany on 2/16/2017.
 */

public class DoneFrag extends Fragment {
    FloatingActionButton addBTN;int POSITION;
    ListView upcommingList;
    ArrayList<Trip> result=new ArrayList<>();
    Queries queries;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.upcommingdesign,container,false);
        addBTN = (FloatingActionButton)rootView.findViewById(R.id.addBtn);
        upcommingList = (ListView)rootView.findViewById(R.id.upcomming_list);
        queries = new Queries(getActivity());
        upcommingList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent myintent = new Intent(getContext(),DetailsOfTrip.class);
                myintent.putExtra("myid",result.get(position).id);
                startActivity(myintent);
            }
        });
        addBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myintent = new Intent(getContext(),SaveTrip.class);
                startActivity(myintent);
            }
        });
        return rootView;
    }
    @Override
    public void onResume() {
        super.onResume();
        result=queries.getTrips(TripStatus.Done);
        MyAdapter myAdapter = new MyAdapter(getActivity(),result);
        upcommingList.setAdapter(myAdapter);
    }
}
