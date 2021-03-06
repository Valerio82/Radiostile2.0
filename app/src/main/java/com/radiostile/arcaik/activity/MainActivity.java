package com.radiostile.arcaik.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.radiostile.arcaik.database.DbManager;
import com.radiostile.arcaik.radiostile.R;
import com.radiostile.arcaik.service.MediaPlayerService;
import com.radiostile.arcaik.utility.IcyStreamMeta;
import com.radiostile.arcaik.utility.MetadataString;
import com.radiostile.arcaik.utility.Singleton;
import java.io.IOException;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;


public class MainActivity extends Activity {
    private ImageButton buttonPlayPause;
    private TextView textViewSongTitle=null;
    Button buttonTag;
    private boolean statusBottone = false;
    private boolean metadata=false;
    private Timer timer;
    private String datiCanzone=null;
    private static final String RADIOSTILE_URL="http://178.32.138.88:8046/stream";
    private Intent intentStartMediaplayerService;
    private DbManager dbManager;
    private String confrontoDatiCanzone="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        buttonPlayPause=(ImageButton) findViewById(R.id.imageButtonPlayPause);
        textViewSongTitle=(TextView)findViewById(R.id.textViewSongTitle);
        Typeface font=Typeface.createFromAsset(getAssets(),"font/Lenka.ttf");
        textViewSongTitle.setTypeface(font);
        intentStartMediaplayerService = new Intent(this, MediaPlayerService.class);
        buttonTag=(Button)findViewById(R.id.buttonTag);
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        dbManager=new DbManager(this);


    }
    public void onResume(){
        super.onResume();
        statusBottone = Singleton.getInstance().getBoolean();
        if (!statusBottone) {
            buttonPlayPause.setImageResource(R.drawable.play);
        } else {
            buttonPlayPause.setImageResource(R.drawable.pause);
            metadata=true;
            getMeta();
        }
        final IntentFilter broadcastIntentFilter=new IntentFilter(MediaPlayerService.NOTIFICATION);
        registerReceiver(receiver, broadcastIntentFilter);
    }
    public void onPause(){
        super.onPause();
        unregisterReceiver(receiver);
        Singleton.getInstance().setBoolean(statusBottone);
        if(metadata) {
            timer.cancel();
            timer.purge();
        }
    }
    public void startService(View v){
        if (!statusBottone){
            metadata=true;
            getMeta();
            startService(intentStartMediaplayerService);
            statusBottone = true;
            buttonPlayPause.setImageResource(R.drawable.pause);
        }else{
            statusBottone = false;
            metadata=false;
            datiCanzone=null;
            confrontoDatiCanzone="";
            textViewSongTitle.setText(null);
            timer.purge();
            timer.cancel();
            stopService(intentStartMediaplayerService);
            buttonPlayPause.setImageResource(R.drawable.play);        }
    }
    private void getMeta()
    {
        timer = new Timer();
        timer.schedule(new TimerTask()
        {
            public void run()
            {
                try
                {
                    IcyStreamMeta icy   = new IcyStreamMeta(new URL(RADIOSTILE_URL));
                    datiCanzone = icy.getStreamTitle();
                    runOnUiThread(new Runnable() {
                        public void run() {
                                if(!datiCanzone.contentEquals(confrontoDatiCanzone)) {
                                    textViewSongTitle.setText(datiCanzone);
                                    textViewSongTitle.refreshDrawableState();
                                    Log.i("TEXTVIEW", "TextView aggiornata");

                                }confrontoDatiCanzone=datiCanzone;
                        }
                    });
                } catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        },0,7000);
    }
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        boolean serverOffline = false;
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();
            serverOffline = bundle.getBoolean(MediaPlayerService.STATO_SERVER);
            if (serverOffline) {
                statusBottone = false;
                buttonPlayPause.setImageResource(R.drawable.play);
                Toast.makeText(getApplicationContext(), "SERVER OFFLINE", Toast.LENGTH_LONG).show();
            } else {
                statusBottone = true;
                buttonPlayPause.setImageResource(R.drawable.pause);
            }
        }
    };
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_myTag:
               sendMetadata();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    public void salvaTag(View salva){
        MetadataString metadatastring;
        if(datiCanzone!=null){
            metadatastring=new MetadataString();
            metadatastring.setString(datiCanzone);
            String nomeArtista = metadatastring.getNomeArtista();
            String nomeCanzone = metadatastring.getNomeCanzone();
            dbManager.save(nomeArtista, nomeCanzone);
        }else
            Toast.makeText(this,"Canzone non presente",Toast.LENGTH_LONG).show();
    }
    public void sendMetadata(){
        Intent intentActivity=new Intent(this,DatabaseActivity.class);
        startActivity(intentActivity);
    }
}
