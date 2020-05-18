package com.example.mye_wallet;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide(); //Hide Action Bar
        setContentView(R.layout.splash_screen); //KELAS INI MENAMPILKAN splash_screen.xml

        SharedPrefUser sp = new SharedPrefUser(this);

        //HANDLER UNTUK MENJALANKAN JEDA PADA LOADING AWAL APLIKASI
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (sp.getSPSudahLogin() == true) {
                    startActivity(new Intent(getApplicationContext(), menu_utama.class));
                    finish();
                }
                else {
                    startActivity(new Intent(getApplicationContext(), login_activity.class)); //KETIKA JEDA SELESAI MAKA AKAN MENJALANKAN KELAS login_activity.xml
                    finish();
                }
            }
        }, 2000L); //2000 L = SPLASH SCREEN DIJALANKAN SELAMA 2 DETIK
    }
}
