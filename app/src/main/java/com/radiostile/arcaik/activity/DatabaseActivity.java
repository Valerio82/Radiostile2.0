package com.radiostile.arcaik.activity;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Typeface;
import android.os.Bundle;
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

public class DatabaseActivity extends Activity {
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
        getActionBar().setDisplayHomeAsUpEnabled(true);
        Cursor cursor=dbManager.query();
        cursorAdapter=new CursorAdapter(this,cursor,0) {
            @Override
            public View newView(Context context, Cursor cursor, ViewGroup parent) {
                return getLayoutInflater().inflate(R.layout.row,null);
            }

            @Override
            public void bindView(View view, final Context context, Cursor cursor) {
                Typeface font=Typeface.createFromAsset(getAssets(),"font/Lenka.ttf");
                TextView nomeArtista=(TextView)view.findViewById(R.id.textViewNomeArtistaTitoloCanzone);
                nomeArtista.setTypeface(font);
                String stringNomeArtista=cursor.getString(cursor.getColumnIndex(DatabaseStrings.NOME_ARTISTA));
                String stringNomeCanzone=cursor.getString(cursor.getColumnIndex(DatabaseStrings.NOME_CANZONE));
                nomeArtista.setText(stringNomeArtista+"\n"+stringNomeCanzone);
                notifyDataSetChanged();
                deleteButton=(ImageButton)view.findViewById(R.id.imageButton);
                deleteButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int position=listView.getPositionForView(view);
                        long id=cursorAdapter.getItemId(position);
                        if(dbManager.delete(id))
                        cursorAdapter.changeCursor(dbManager.query());
                        Toast.makeText(context, "La canzone è stata cancellata ", Toast.LENGTH_SHORT).show();
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
