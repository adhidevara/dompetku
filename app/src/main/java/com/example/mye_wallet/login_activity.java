package com.example.mye_wallet;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.zxing.integration.android.IntentIntegrator;

import java.util.List;

public class login_activity extends AppCompatActivity {

    //DEKLARASI VARIABEL
    EditText etUsername, etPassword;
    Button btnLogin;
    TextView txDaftar;

    SharedPrefUser sharedPrefUser; //VARIABEL SESSION MENGGUNAKAN [SHARED PREFERENCES] DARI KELAS "SharedPrefUser"

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity); //KELAS INI MENAMPILKAN LAYOUT login_activity.xml

        //DEKLARASI VARIABEL DARI LAYOUT
        etUsername = findViewById(R.id.etNama);
        etPassword = findViewById(R.id.etPassword);
        txDaftar = findViewById(R.id.txNoHp);
        btnLogin = findViewById(R.id.btnLogin);

        //AKSI KETIKA TULISAN "BELUM PUNYA AKUN?" DITEKAN
        txDaftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //MEMANGGIL METHOD regis()
                regis();
            }
        });

        //AKSI KETIKA TOMBOL "LOGIN" DITEKAN
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //MEMANGGIL METHOD login()
                login();
            }
        });
    }

    //METHOD UNTUK MASUK KE TAMPILAN REGIS (regis_activity.xml & regis_activity.java)
    public void regis() {
        new IntentIntegrator(this).setCaptureActivity(regis_activity.class).initiateScan();
    }

    //METHOD UNTUK MEMPROSES DATA LOGIN (USERNAME & PASSWORD)
    public void login() {

        //MEMBUAT OBJEK DATABASE DARI KELAS DatabaseUser.java
        DatabaseUser db = DatabaseUser.getInstance(this);

        //MEMBUAT LIST userList YANG MEMANGGIL METHOD selectAkun PADA KELAS DatabaseUser.java
        List<User> userList = db.selectAkun(etUsername.getText().toString(), etPassword.getText().toString());

        //MENGGUNAKAN PERULANGAN UNTUK MENGAMBIL DATA PADA LIST DIATAS (Line-66)
        for (User u : userList) {
            Log.i("DB", "ACCOUNT LOGED IN: "+
                String.format("ID User : %d, Nama : %s, Username : %s, NoHP : %s, Saldo : %d",
                    u.getId_user(),
                    u.getNama(),
                    u.getUsername(),
                    u.getNoHp(),
                    u.getSaldo())
            );
        }
        Log.i("JML", "login: "+userList.size()); //MENAMPILKAN LOG JUMLAH DATA YANG ADA PADA LIST (Line-66)

        //PENGECEKAN APAKAH LIST BERISI LEBIH DARI 1 / KURANG DARI 1 DATA
        if (userList.size() > 1 || userList.size() < 1) { //JIKA LIST BERISI LEBIH DARI 1 ATAU KURANG DARI 1 DATA
            etUsername.setError("USERNAME / PASSWORD SALAH");
            etPassword.setError("USERNAME / PASSWORD SALAH");
            Toast.makeText(this, "USERNAME / PASSWORD SALAH", Toast.LENGTH_SHORT).show(); //TAMPILKAN TOAST "USERNAME / PASSWORD SALAH"
        }
        else { //JIKA LIST BERISI 1 DATA
            sharedPrefUser = new SharedPrefUser(this); //MEMBUAT OBJEK UNTUK SESSION MENGGUNAKAN [SHARED PREFERENCES] DARI KELAS "SharedPrefUser.java"
            sharedPrefUser.saveSPBoolean("spSudahLogin", true);
            sharedPrefUser.saveSPInt("spIdUser", userList.get(0).getId_user());
            sharedPrefUser.saveSPString("spNama", userList.get(0).getNama());
            sharedPrefUser.saveSPString("spUsername", etUsername.getText().toString());
            sharedPrefUser.saveSPString("spNoHP", userList.get(0).getNoHp());
            sharedPrefUser.saveSPString("spPassword", etPassword.getText().toString());
            sharedPrefUser.saveSPInt("spSaldo", userList.get(0).getSaldo());

            startActivity(new Intent(getApplicationContext(), menu_utama.class)); //TAMPILKAN MENU UTAMA
            finish();
        }
    }
}
