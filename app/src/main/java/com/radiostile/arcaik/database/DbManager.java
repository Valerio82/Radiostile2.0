package com.radiostile.arcaik.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

/**
 * Created by arcaik on 11/12/2014.
 */
public class DbManager {
    private DbHelper dbHelper;

    public DbManager(Context context) {
        dbHelper = new DbHelper(context);
    }

    public void save(String artista, String canzone){
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(DatabaseStrings.NOME_ARTISTA, artista);
        cv.put(DatabaseStrings.NOME_CANZONE, canzone);
        database.insert(DatabaseStrings.TBL_NAME,null,cv);
    }

    public boolean delete(long id) {
        SQLiteDatabase db=dbHelper.getWritableDatabase();
        try
        {
            if (db.delete(DatabaseStrings.TBL_NAME, DatabaseStrings.FIELD_ID+"=?", new String[]{Long.toString(id)})>0)
                return true;
            return false;
        }
        catch (SQLiteException sqle)
        {
            return false;
        }

    }
    public Cursor query(){
        Cursor cursor=null;
        try
        {
        SQLiteDatabase database=dbHelper.getReadableDatabase();
            cursor=database.query(DatabaseStrings.TBL_NAME,null,null,null,null,null,null,null);
        }
        catch (SQLiteException sql)
        {
            return null;
        }
        return cursor;
    }

}

