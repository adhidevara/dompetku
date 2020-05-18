package com.example.mye_wallet;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.text.DecimalFormat;
import java.util.List;

public class menu_utama extends AppCompatActivity {
    //DEKLARASI VARIABEL
    String nama, username, password, noHp;
    int id_user, saldoSP;
    TextView saldo, txNama;
    Button btnTopUp,
           btnTransfer,
           btnProfil,
           btnHistory,
           btnLogOut;

    //CUSTOM REQUEST CODE UNTUK SCAN QR
    public final int CUSTOMIZED_REQUEST_CODE = 0x0000ffff;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); //KELAS INI MENJALANKAN activity_main.xml

        //MEMBUAT OBJEK SESSION MENGGUNAKAN [SHARED PREFERENCES] DARI KELAS SharedPrefUser.java
        SharedPrefUser sp = new SharedPrefUser(this);

        //MENGAMBIL DATA DARI SESSION
        id_user = sp.getSpIdUser();
        nama = sp.getSpNama();
        username = sp.getSPUsername();
        noHp = sp.getSpNoHp();
        password = sp.getSPPassword();
        saldoSP = sp.getSpSaldo();

        //DEKLARASI VARIABEL DARI LAYOUT
        saldo = findViewById(R.id.saldo);
        txNama = findViewById(R.id.txNama);
        btnTopUp = findViewById(R.id.btnBayar);
        btnTransfer = findViewById(R.id.btnTransfer);
        btnProfil = findViewById(R.id.btnProfil);
        btnHistory = findViewById(R.id.btnHistory);
        btnLogOut = findViewById(R.id.btnLogOut);

        //AKSI KETIKA BUTTON TOP UP DI TEKAN
        btnTopUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                topUp_page();
            }
        });

        //AKSI KETIKA BUTTON TRANSFER DI TEKAN
        btnTransfer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                transfer_page();
            }
        });

        //AKSI KETIKA BUTTON PROFIL DI TEKAN
        btnProfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                profil_page();
            }
        });

        //AKSI KETIKA BUTTON HISTORY TRANSAKSI DI TEKAN
        btnHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                history_page();
            }
        });

        //AKSI KETIKA BUTTON LOGOUT DI TEKAN
        btnLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logout();
            }
        });

        checkSession();
        getSaldo();
    }

    public void checkSession() {
        //MEMBUAT OBJEK SESSION MENGGUNAKAN [SHARED PREFERENCES] DARI KELAS SharedPrefUser.java
        SharedPrefUser sp = new SharedPrefUser(this);

        if (sp.getSPSudahLogin() == true){
            Log.i("LOGIN", "checkSession: LOGIN SUKSES");
        }
        else{
            startActivity(new Intent(getApplicationContext(), login_activity.class));
            finish();
        }
    }

    public void getSaldo() {
        DecimalFormat formatter = new DecimalFormat("#,###,###");
        String saldoF = formatter.format(saldoSP);

        txNama.setText("Selamat Datang, "+nama);
        saldo.setText(saldoF);
    }

    public void topUp_page(){
        startActivity(new Intent(getApplicationContext(), topup_activity.class));
    }

    public void transfer_page(){
        new IntentIntegrator(this).setCaptureActivity(transfer_activity.class).initiateScan();
    }

    public void profil_page(){
        startActivity(new Intent(getApplicationContext(), profil_activity.class));
    }

    public void history_page(){
        startActivity(new Intent(getApplicationContext(), history_activity.class));
    }

    public void logout(){
        //MEMBUAT OBJEK SESSION MENGGUNAKAN [SHARED PREFERENCES] DARI KELAS SharedPrefUser.java
        SharedPrefUser sharedPrefUser = new SharedPrefUser(this);

        sharedPrefUser.saveSPBoolean("spSudahLogin", false);
        sharedPrefUser.saveSPInt("spIdUser", -1);
        sharedPrefUser.saveSPString("spNama", "");
        sharedPrefUser.saveSPString("spUsername", "");
        sharedPrefUser.saveSPString("spNoHP", "");
        sharedPrefUser.saveSPString("spPassword", "");
        sharedPrefUser.saveSPInt("spSaldo", -1);

        startActivity(new Intent(getApplicationContext(), login_activity.class));
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode != CUSTOMIZED_REQUEST_CODE && requestCode != IntentIntegrator.REQUEST_CODE) {
            // This is important, otherwise the result will not be passed to the fragment
            super.onActivityResult(requestCode, resultCode, data);
            return;
        }
        switch (requestCode) {
            case CUSTOMIZED_REQUEST_CODE: {
                Toast.makeText(this, "REQUEST_CODE = " + requestCode, Toast.LENGTH_LONG).show();
                break;
            }
            default:
                break;
        }

        IntentResult result = IntentIntegrator.parseActivityResult(resultCode, data);

        if(result.getContents() == null) {
            Log.d("MainActivity", "Cancelled scan");
            Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
        } else {
            Log.d("MainActivity", "Scanned");

            DatabaseUser db = DatabaseUser.getInstance(this);
            List<User> userList = db.cekAkun(result.getContents());

            if (userList.size() != 0) {
                SharedPrefUser sp = new SharedPrefUser(this);
                sp.saveSPString("spUsernameTujuan", result.getContents());
                startActivity(new Intent(getApplicationContext(), transfer_nominal_activity.class));
            }
            else{
                Toast.makeText(this, "QR CODE: " + result.getContents()+" Tidak Dikenal", Toast.LENGTH_LONG).show();
            }
        }
    }
}