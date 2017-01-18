package mx.com.taximex.taximex;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class splashActivity extends AppCompatActivity {
    private final int DURATION_SPLASH=3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        new Handler().postDelayed(new Runnable() {
            public void run() {
                Intent intent=new Intent(splashActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
            };

        },DURATION_SPLASH);
    }
}
