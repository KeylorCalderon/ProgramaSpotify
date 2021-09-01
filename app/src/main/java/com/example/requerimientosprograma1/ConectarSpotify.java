package com.example.requerimientosprograma1;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import android.content.SharedPreferences;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import com.spotify.android.appremote.api.ConnectionParams;

import com.spotify.sdk.android.auth.AuthorizationClient;
import com.spotify.sdk.android.auth.AuthorizationRequest;
import com.spotify.sdk.android.auth.AuthorizationResponse;

import android.content.Intent;
import android.util.Log;
import android.view.Window;

public class ConectarSpotify extends AppCompatActivity {

    private SharedPreferences.Editor editor;
    private SharedPreferences msharedPreferences;

    private RequestQueue queue;

    private static final String CLIENT_ID = "74205026b5424c308f3c1dc486a4be15";
    private static final String REDIRECT_URI = "https://requerimientosproyecto1.com/callback";
    private static final int REQUEST_CODE = 1337;
    private static final String SCOPES = "user-read-recently-played,user-library-modify,user-read-email,user-read-private";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_conectar_spotify);


        authenticateSpotify();

        msharedPreferences = this.getSharedPreferences("SPOTIFY", 0);
        queue = Volley.newRequestQueue(this);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        Log.d("Codigo: ", Integer.toString(requestCode));
        // Check if result comes from the correct activity
        if (requestCode == REQUEST_CODE) {
            AuthorizationResponse response = AuthorizationClient.getResponse(resultCode, intent);

            switch (response.getType()) {
                // Response was successful and contains auth token
                case TOKEN:
                    Log.d("Tipo: ","token");
                    editor = getSharedPreferences("SPOTIFY", 0).edit();
                    editor.putString("token", response.getAccessToken());
                    Log.d("STARTING", "GOT AUTH TOKEN");
                    editor.apply();
                    Log.d("AAA", response.getAccessToken());
                    waitForUserInfo();
                    break;

                // Auth flow returned an error
                case ERROR:
                    Log.d("Tipo: ","Error");
                    // Handle error response
                    break;

                // Most likely auth flow was cancelled
                default:
                    Log.d("Tipo: ","default");
                    // Handle other cases
            }
        }
    }

    private void authenticateSpotify() {
        AuthorizationRequest.Builder builder = new AuthorizationRequest.Builder(CLIENT_ID, AuthorizationResponse.Type.TOKEN, REDIRECT_URI);
        builder.setScopes(new String[]{SCOPES});
        AuthorizationRequest request = builder.build();
        AuthorizationClient.openLoginActivity(this, REQUEST_CODE, request);
    }

    private void waitForUserInfo() {
        UserService userService = new UserService(queue, msharedPreferences);
        userService.get(() -> {
            User user = userService.getUser();
            editor = getSharedPreferences("SPOTIFY", 0).edit();
            editor.putString("userid", user.id);
            Log.d("STARTING", "GOT USER INFORMATION");
            Log.d("Usuario: ", user.email);
            // We use commit instead of apply because we need the information stored immediately
            editor.commit();

            startMainActivity();
        });
    }

    private void startMainActivity() {
        Intent newIntent = new Intent(ConectarSpotify.this, MainActivity.class);
        startActivity(newIntent);
    }
}