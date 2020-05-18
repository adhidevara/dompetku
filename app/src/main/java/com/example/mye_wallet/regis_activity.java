package com.example.mye_wallet;

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

public class regis_activity extends AppCompatActivity {
    EditText etNama, etUsernameReg, etNoHp, etPassReg, etRePassReg;
    Button btnDaftar;
    TextView txAdaAkun;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.regis_activity);

        etNama = findViewById(R.id.etNama);
        etUsernameReg = findViewById(R.id.etUsernameReg);
        etNoHp = findViewById(R.id.etNoHp);
        etPassReg = findViewById(R.id.etPassReg);
        etRePassReg = findViewById(R.id.etRePassReg);
        txAdaAkun = findViewById(R.id.txAdaAkun);
        btnDaftar = findViewById(R.id.btnDaftar);

        txAdaAkun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });

        btnDaftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                daftar();
            }
        });
    }

    public void login() {
        new IntentIntegrator(this).setCaptureActivity(login_activity.class).initiateScan();
    }

    public void daftar() {
        DatabaseUser db = DatabaseUser.getInstance(this);

        List<User> userList = db.cekAkun(etUsernameReg.getText().toString());

        if (etPassReg.getText().toString().equals(etRePassReg.getText().toString())) {
            if (userList.size() == 0){
                int saldo = 0;

                User u = new User();
                u.setNama(etNama.getText().toString());
                u.setUsername(etUsernameReg.getText().toString());
                u.setNoHp(etNoHp.getText().toString());
                u.setPassword(etPassReg.getText().toString());
                u.setSaldo(saldo);

                db.createUser(u);
                Log.i("DB", "daftar: "+"Sukses Daftar");
                Toast.makeText(this, "BERHASIL DAFTAR AKUN", Toast.LENGTH_LONG).show();
                login();
            }
            else {
                etUsernameReg.setError("USERNAME TELAH TERDAFTAR");
                Toast.makeText(this, "USERNAME TELAH TERDAFTAR", Toast.LENGTH_SHORT).show();
            }
        }
        else{
            etPassReg.setError("PASSWORD ANDA HARUS SAMA");
            etRePassReg.setError("PASSWORD ANDA HARUS SAMA");
            Toast.makeText(this, "PASSWORD ANDA HARUS SAMA", Toast.LENGTH_SHORT).show();
        }
    }
}
