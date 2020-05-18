package com.example.mye_wallet;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.util.List;

public class profil_activity extends AppCompatActivity {
    TextView txNama, txUsername, txNoHP, txId_user;
    Button btnEditProfil;
    ImageView imgQR;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profil_activity);

        txNama = findViewById(R.id.txNama);
        txUsername = findViewById(R.id.txUsername);
        txNoHP = findViewById(R.id.txNoHP);
        txId_user = findViewById(R.id.txId_user);
        btnEditProfil = findViewById(R.id.btnEditProfil);
        imgQR = findViewById(R.id.imgQR);

        btnEditProfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editProfil();
            }
        });

        getProfil();
        genQRID();
    }

    public void editProfil() {
        Log.i("EDITPROFIL", "editProfil: EDIT PROFIL");
        startActivity(new Intent(getApplicationContext(), edit_profil_activity.class)); //TAMPILKAN FORM EDIT
    }

    public void getProfil() {
        //MEMBUAT OBJEK SESSION MENGGUNAKAN [SHARED PREFERENCES] DARI KELAS SharedPrefUser.java
        SharedPrefUser sp = new SharedPrefUser(this);
        String username = sp.getSPUsername();
        String nama = sp.getSpNama();
        String noHP = sp.getSpNoHp();
        int id_user = sp.getSpIdUser();

        txNama.setText(nama);
        txUsername.setText(username);
        txNoHP.setText(noHP);
        txId_user.setText("ID User : "+id_user);
    }

    public void genQRID() {
        //MEMBUAT OBJEK SESSION MENGGUNAKAN [SHARED PREFERENCES] DARI KELAS SharedPrefUser.java
        SharedPrefUser sp = new SharedPrefUser(this);

        String QR = sp.getSPUsername();
        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        try {
            BitMatrix bitMatrix = multiFormatWriter.encode(QR, BarcodeFormat.QR_CODE, 200, 200);
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
            imgQR.setImageBitmap(bitmap);
        }
        catch (WriterException e) {
            e.printStackTrace();
        }
    }
}
