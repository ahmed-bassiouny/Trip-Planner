package helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by ahmed on 14/02/17.
 */

public class SqlHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME ="Trips";
    public static final String TABLE_NAME ="trip";
    public static final int VERSION =1;
    Context context;
    public SqlHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
        this.context=context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable=
                "create table " +TABLE_NAME +
                        "(id integer primary key ," +
                        " name text ," +
                        " startTime datetime default null ," +
                        " startPoint text ," +
                        " destination text ," +
                        " note text ," +
                        " tripStatus integer ," +
                        " oneDirection integer ," +
                        " with text ," +
                        " reminderTime datetime default null )";

        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

}
