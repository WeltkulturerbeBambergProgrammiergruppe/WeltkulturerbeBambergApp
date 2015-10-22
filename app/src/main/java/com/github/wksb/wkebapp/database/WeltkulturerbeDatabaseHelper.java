package com.github.wksb.wkebapp.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * This class helps to manage the database "Weltkulturebe"
 *
 * @author Projekt-Seminar "Schnitzeljagd World-heritage" 2015/2016 des Clavius Gymnasiums Bamberg
 * @version 1.0
 * @since 2015-06-04
 */
public class WeltkulturerbeDatabaseHelper extends SQLiteOpenHelper{

    /** Name and version of the database **/
    private static final String DATABASE_WELTKULTURERBE = "weltkulturerbe.db";
    private static final int DATABASE_VERSION = 1;

    public WeltkulturerbeDatabaseHelper(Context context) {
        super(context, DATABASE_WELTKULTURERBE, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        WaypointsTable.addToDatabase(db);
        QuizzesTable.addToDatabase(db);
        RoutesTable.addToDatabase(db);
        InformationTable.addToDatabase(db);
        RouteSegmentsTable.addToDatabase(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Not used
    }
}
