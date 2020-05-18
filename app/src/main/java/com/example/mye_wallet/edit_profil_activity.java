package com.example.mye_wallet;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class edit_profil_activity extends AppCompatActivity {
    TextView txIdUser, txUsername;
    EditText etNama, etNoHp, etPasswordEdit;
    Button btnEditSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_profil_activity);

        txIdUser = findViewById(R.id.txIdUser);
        txUsername = findViewById(R.id.txUsername);
        etNama = findViewById(R.id.etNama);
        etNoHp = findViewById(R.id.etNoHp);
        etPasswordEdit = findViewById(R.id.etPasswordEdit);
        btnEditSubmit = findViewById(R.id.btnEditSubmit);

        btnEditSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editProfil1();
            }
        });
        getData();
    }

    public void editProfil1() {
        SharedPrefUser sp = new SharedPrefUser(this);

        if (etPasswordEdit.getText().toString().equals(sp.getSPPassword())) {
            DatabaseUser db = DatabaseUser.getInstance(this);
            User u = new User();
            u.setNama(etNama.getText().toString());
            u.setNoHp(etNoHp.getText().toString());
            u.setId_user(sp.getSpIdUser());
            db.updateUser(u);

            sp.saveSPString("spNama", etNama.getText().toString());
            sp.saveSPString("spNoHP", etNoHp.getText().toString());

            startActivity(new Intent(getApplicationContext(), menu_utama.class));
            finish();
        }
        else {
            Toast.makeText(this, "PASSWORD SALAH", Toast.LENGTH_SHORT).show();
        }
    }

    public void getData() {
        SharedPrefUser sp = new SharedPrefUser(this);
        int id_user = sp.getSpIdUser();
        String username = sp.getSPUsername();
        String nama = sp.getSpNama();
        String noHP = sp.getSpNoHp();

        txIdUser.setText("ID User : "+id_user);
        txUsername.setText(username);
        etNama.setText(nama);
        etNoHp.setText(noHP);
    }
}
