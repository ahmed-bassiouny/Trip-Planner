package helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import Assist.Debuger;

/**
 * Created by ahmed on 14/02/17.
 */

public class Queries {

    SqlHelper sqlHelper;
    SQLiteDatabase db;
    private String selectTrip="select * from "+SqlHelper.TABLE_NAME;
    private String asec=" order by startTime";
    private String desc =" order by startTime desc";
    public Queries(Context context)
    {
        this.sqlHelper=new SqlHelper(context);
    }

    public ArrayList<Trip> getTrips(TripStatus tripStatus){
        ArrayList<Trip> mytrips = new ArrayList<Trip>();
        db = sqlHelper.getReadableDatabase();
        Cursor cursor =db.rawQuery(selectTrip+" where tripStatus = "+tripStatus.ordinal()+asec,null);
        while(cursor.moveToNext()){
            mytrips.add(fillDate(cursor));
        }
        cursor.close();
        db.close();
        return mytrips;
    }
    public Trip getTrip(int id){
        Trip mytrip = new Trip();
        db = sqlHelper.getReadableDatabase();
        Cursor cursor =db.rawQuery(selectTrip+" where id = "+id,null);
        cursor.moveToNext();
        mytrip=fillDate(cursor);
        cursor.close();
        db.close();
        return mytrip;
    }
    public long insertTrip(Trip trip  ){
        long result =-1 ;
        db = sqlHelper.getWritableDatabase();
        result= db.insert(SqlHelper.TABLE_NAME,null,createdata(trip));
        db.close();
        return  result; // return id of new row
    }
    public long updateTrip(Trip trip ,int id ){
        long result =-1 ;
        db = sqlHelper.getWritableDatabase();
        result= db.update(SqlHelper.TABLE_NAME,createdata(trip)," id = "+id,null);
        db.close();
        return  result;  // return 1 id update done
    }

    private Trip fillDate(Cursor cursor){
        // it's get Cursor and get data from it and return Trip object with data
        Trip trip = new Trip();
        trip.id   = cursor.getInt(0);
        trip.name = cursor.getString(1);
        trip.startTime=cursor.getString(2);
        trip.startPoint=cursor.getString(3);
        trip.destination=cursor.getString(4);
        trip.note=cursor.getString(5);
        trip.myTripStatus=TripStatus.values()[cursor.getInt(6)];
        if(cursor.getInt(7)==0)
            trip.oneDirection=false;
        trip.with=cursor.getString(8);
        trip.reminderTime=cursor.getString(9);
        return trip;
    }

    public boolean deleteTrip(int id){
        // get id trip and remove row
        // if row removed return true ifnot return false
        db = sqlHelper.getWritableDatabase();
        boolean result=db.delete(SqlHelper.TABLE_NAME," id = "+id,null)==1;
        db.close();
        return result;

    }
    private ContentValues createdata(Trip trip){
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", trip.name);
        contentValues.put("startTime", trip.startTime);
        contentValues.put("startPoint", trip.startPoint);
        contentValues.put("destination",trip.destination);
        contentValues.put("note", trip.note);
        contentValues.put("tripStatus",trip.myTripStatus.ordinal());
        contentValues.put("oneDirection",trip.oneDirection);
        contentValues.put("with",trip.with);
        contentValues.put("reminderTime",trip.reminderTime);
        return contentValues;
    }
    public ArrayList<Integer> getCountAboutType(){
        db = sqlHelper.getReadableDatabase();
        ArrayList<Integer> counts=new ArrayList<>();
        Cursor cursor =db.rawQuery("SELECT tripStatus, count(*) FROM trip GROUP BY tripStatus ",null);
        while(cursor.moveToNext()){
            counts.add(cursor.getInt(0));
            counts.add(cursor.getInt(1));
        }
        return counts;
    }
}
