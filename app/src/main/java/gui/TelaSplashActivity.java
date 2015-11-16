package gui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;

import com.mpoo.ruralmaps.ruralmaps.R;

public class TelaSplashActivity extends Activity {

    private static int TEMPO_SPLASH = 4000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_splash);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(TelaSplashActivity.this, MapsActivity.class);
                startActivity(i);
                finish();
            }
        }, TEMPO_SPLASH);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_tela_splash, menu);
        return true;
    }
}