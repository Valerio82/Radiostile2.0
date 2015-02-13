package com.radiostile.arcaik.activity;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.radiostile.arcaik.database.DatabaseStrings;
import com.radiostile.arcaik.database.DbManager;
import com.radiostile.arcaik.radiostile.R;

public class DatabaseActivity extends ActionBarActivity {
    private DbManager dbManager;
    private ListView listView;
    private CursorAdapter cursorAdapter;
    private ImageButton deleteButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_database);
        listView=(ListView)findViewById(R.id.listView);
        dbManager=new DbManager(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        final Cursor cursor=dbManager.query();
        cursorAdapter=new CursorAdapter(this,cursor,0) {
            @Override
            public View newView(Context context, Cursor cursor, ViewGroup parent) {
                View v=getLayoutInflater().inflate(R.layout.row,null);
                return v;
            }

            @Override
            public void bindView(View view, final Context context, Cursor cursor) {
                TextView nomeArtista=(TextView)view.findViewById(R.id.textViewNomeArtista);
                TextView nomeCanzone=(TextView)view.findViewById(R.id.textViewNomeCanzone);
                nomeArtista.setText(cursor.getString(cursor.getColumnIndex(DatabaseStrings.NOME_ARTISTA)));
                nomeCanzone.setText(cursor.getString(cursor.getColumnIndex(DatabaseStrings.NOME_CANZONE)));
                deleteButton=(ImageButton)view.findViewById(R.id.imageButton);
                deleteButton.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View view, MotionEvent event) {
                        int position=listView.getPositionForView(view);
                        long id=cursorAdapter.getItemId(position);
                        dbManager.delete(id);
                         //   cursorAdapter.changeCursor(dbManager.query());
                        Toast.makeText(context, "La canzone è stata cancellata ", Toast.LENGTH_SHORT).show();
                        return false;
                    }
                });
            }

            @Override
            public long getItemId(int position){
                Cursor crs=cursorAdapter.getCursor();
                crs.moveToPosition(position);
                return crs.getLong(crs.getColumnIndex(DatabaseStrings.FIELD_ID));
            }
        };
        listView.setAdapter(cursorAdapter);
    }






}
