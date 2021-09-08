package com.example.requerimientosprograma1;


import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
//import android.support.v7.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.spotify.android.appremote.api.ConnectionParams;
import com.spotify.android.appremote.api.Connector;
import com.spotify.android.appremote.api.SpotifyAppRemote;

import com.spotify.protocol.client.Subscription;
import com.spotify.protocol.types.PlayerState;
import com.spotify.protocol.types.Track;
import com.spotify.android.appremote.api.ConnectApi;
import com.spotify.android.appremote.api.ContentApi;


public class MainActivity extends AppCompatActivity {

    private static final String CLIENT_ID = "74205026b5424c308f3c1dc486a4be15";
    private static final String REDIRECT_URI = "https://requerimientosproyecto1.com/callback";
    private SpotifyAppRemote mSpotifyAppRemote;
    //private SpotifyApi spotifyApi;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onStart() {
        super.onStart();
        ConnectionParams connectionParams =
                new ConnectionParams.Builder(CLIENT_ID)
                        .setRedirectUri(REDIRECT_URI)
                        .showAuthView(true)
                        .build();

        SpotifyAppRemote.connect(this, connectionParams,
                new Connector.ConnectionListener() {

                    public void onConnected(SpotifyAppRemote spotifyAppRemote) {
                        mSpotifyAppRemote = spotifyAppRemote;
                        Log.d("MainActivity", "Connected! Yay!");
                        connected();

                    }

                    public void onFailure(Throwable throwable) {
                        Log.e("MyActivity", throwable.getMessage(), throwable);
                    }
                });
    }

    @Override
    protected void onStop() {
        super.onStop();
        SpotifyAppRemote.disconnect(mSpotifyAppRemote);
    }

    private void connected() {
        // Play a playlist
        mSpotifyAppRemote.getPlayerApi().play("spotify:playlist:63bOYES9RBLM4yezA4HJQ5");

        // Subscribe to PlayerState
        mSpotifyAppRemote.getPlayerApi()
                .subscribeToPlayerState()
                .setEventCallback(playerState -> {
                    final Track track = playerState.track;
                    if (track != null) {
                        Log.d("MainActivity", track.name + " by " + track.artist.name);
                    }
                });
    }

    //BQADjSnnoAgYTs2r18_O_t5VtxAOrFt5L8clXxfn0aEgL_M04gTrHytDU6m8VlXswRtlAuy05D3-6O_fz9FijCROhmFW9heUSEg35D_qCt6ZLb6CyY5691BHHtmDPF30AbAcMQmzaevurL-IoFtMKGPOMQl0sLh8eKS8GbgxtkVsGfiniF9pTx8jSLru5J74kmWOcI_832UzsdmLh-8vHWhRtzNsPGDU

    public void reanudarPausar(View view) {
        /*Toast.makeText(
                getApplicationContext(),
                "HICISTE CLICK EN EL BOTÓN",
                Toast.LENGTH_LONG).show();*/
        Button boton = (Button)view;
        if(boton.getText().equals("Pause")){
            mSpotifyAppRemote.getPlayerApi().pause();
            boton.setText("Play");
        }else{
            mSpotifyAppRemote.getPlayerApi().resume();
            boton.setText("Pause");
        }
    }

    public void añadirCancion(View view) {

    }
}