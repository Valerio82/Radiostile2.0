package com.radiostile.arcaik.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by arcaik on 11/12/2014.
 */
public class DbHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME="DB_CANZONI";

    public DbHelper(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
       String q="CREATE TABLE "+DatabaseStrings.TBL_NAME;
               q+="(_id INTEGER PRIMARY KEY AUTOINCREMENT,";
               q+=DatabaseStrings.NOME_ARTISTA+" TEXT,";
               q+=DatabaseStrings.NOME_CANZONE+" TEXT);";
        db.execSQL(q);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

}
