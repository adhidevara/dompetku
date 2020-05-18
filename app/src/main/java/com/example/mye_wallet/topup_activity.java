package com.example.mye_wallet;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import java.text.DecimalFormat;

public class topup_activity extends AppCompatActivity {
    EditText etNominal;
    Button btnBayar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.topup_activity);

        etNominal = findViewById(R.id.etNominal);
        btnBayar = findViewById(R.id.btnBayar);

        btnBayar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int inpSaldo = Integer.parseInt(etNominal.getText().toString());
                if (inpSaldo >= 10000 && inpSaldo <= 1000000) {
                    bayar();
                }
                else {
                    etNominal.setError("Nominal Tidak Sesuai Syarat");
                    Toast.makeText(topup_activity.this, "Nominal TOP-UP Tidak Sesuai Syarat", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void bayar() {
        SharedPrefUser sp = new SharedPrefUser(this);
        DatabaseUser db = DatabaseUser.getInstance(this);

        int saldoSP = sp.getSpSaldo();
        int saldoAkhir = Integer.parseInt(etNominal.getText().toString()) + saldoSP;

        User u = new User();
        u.setSaldo(saldoAkhir);
        u.setId_user(sp.getSpIdUser());
        db.updateSaldo(u);
        sp.saveSPInt("spSaldo", saldoAkhir);

        Transaksi t = new Transaksi();
        t.setId_user(sp.getSpIdUser());
        t.setNominal(Integer.parseInt(etNominal.getText().toString()));
        t.setTipe("DEBIT");
        t.setDeskripsi("Transaksi TOP-UP");
        db.createTransaction(t);

        Log.i("TOPUP", "bayar: SUKSES TOP-UP"+saldoSP);
        notif();
        startActivity(new Intent(getApplicationContext(), menu_utama.class));
        finish();
    }

    public void notif() {
        SharedPrefUser sp = new SharedPrefUser( this);
        DecimalFormat formatter = new DecimalFormat("#,###,###");

        String NOTIFICATION_CHANNEL_ID = "topup_notif";
        Context context = this.getApplicationContext();

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            String channelName = "Android Notif Channel";
            int importance = NotificationManager.IMPORTANCE_HIGH;

            NotificationChannel mChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, importance);
            notificationManager.createNotificationChannel(mChannel);
        }

        Intent mIntent = new Intent(this, menu_utama.class);
        Bundle bundle = new Bundle();
        bundle.putString("fromnotif", "notif");
        mIntent.putExtras(bundle);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, mIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this,NOTIFICATION_CHANNEL_ID);
        builder.setContentIntent(pendingIntent)
                .setSmallIcon(R.drawable.dompetku1)
                .setLargeIcon(BitmapFactory.decodeResource(this.getResources(), R.drawable.dompetku1))
                .setTicker("notif topup")
                .setAutoCancel(true)
                .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
                .setLights(Color.RED, 3000, 3000)
                .setDefaults(Notification.DEFAULT_SOUND)
                .setContentTitle("TOP-UP Sukses")
                .setContentText("Anda berhasil Top-Up senilai Rp."+formatter.format(Integer.parseInt(etNominal.getText().toString())))
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText("Saldo anda sekarang : Rp."+formatter.format(sp.getSpSaldo())+".\nTerima Kasih telah melakukan Top-Up Saldo di DOMPET.KU"))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(115, builder.build());
    }
}
