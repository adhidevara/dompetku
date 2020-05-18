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
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import java.text.DecimalFormat;
import java.util.List;

public class transfer_nominal_activity extends AppCompatActivity {
    EditText etNominalTrans;
    Button btnNominalTrans;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.transfer_nominal_activity);

        etNominalTrans = findViewById(R.id.etNominalTrans);
        btnNominalTrans = findViewById(R.id.btnNominalTrans);

        btnNominalTrans.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                transfer();
            }
        });
    }

    public void transfer() {
        SharedPrefUser sp = new SharedPrefUser(this);
        DatabaseUser db = DatabaseUser.getInstance(this);
        String username = sp.getSpUsernameTujuan();

        List<User> userList = db.cekAkun(username);

        int saldoAkhir = sp.getSpSaldo() - Integer.parseInt(etNominalTrans.getText().toString());
        int saldoTujuanAkhir = userList.get(0).getSaldo() + Integer.parseInt(etNominalTrans.getText().toString());

        if (Integer.parseInt(etNominalTrans.getText().toString()) <= sp.getSpSaldo()) {
            Transaksi t1 = new Transaksi();
            t1.setId_user(sp.getSpIdUser());
            t1.setNominal(Integer.parseInt(etNominalTrans.getText().toString()));
            t1.setTipe("KREDIT");
            t1.setDeskripsi("Transaksi (SEND) Transfer Ke ID User : "+userList.get(0).getId_user()+" - "+userList.get(0).getNama());
            db.createTransaction(t1);

            Transaksi t2 = new Transaksi();
            t2.setId_user(userList.get(0).getId_user());
            t2.setNominal(Integer.parseInt(etNominalTrans.getText().toString()));
            t2.setTipe("DEBIT");
            t2.setDeskripsi("Transaksi (RECEIVE) Transfer Dari ID User : "+sp.getSpIdUser()+" - "+sp.getSpNama());
            db.createTransaction(t2);

            User u1 = new User();
            u1.setSaldo(saldoAkhir);
            u1.setId_user(sp.getSpIdUser());
            db.updateSaldo(u1);
            sp.saveSPInt("spSaldo", saldoAkhir);

            User u2 = new User();
            u2.setSaldo(saldoTujuanAkhir);
            u2.setId_user(userList.get(0).getId_user());
            db.updateSaldo(u2);

            startActivity(new Intent(getApplicationContext(), menu_utama.class));
            Toast.makeText(this, "TRANSFER KE "+userList.get(0).getNama(), Toast.LENGTH_SHORT).show();
            notif();
            finish();
        }
        else {
            etNominalTrans.setError("Saldo Anda Tidak Cukup");
        }
    }

    public void notif() {
        SharedPrefUser sp = new SharedPrefUser(this);
        DatabaseUser db = DatabaseUser.getInstance(this);
        List<User> userList = db.cekAkun(sp.getSpUsernameTujuan());
        DecimalFormat formatter = new DecimalFormat("#,###,###");

        String NOTIFICATION_CHANNEL_ID = "transfer_notif";
        Context context = this.getApplicationContext();

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            String channelName = "Android Notif Channel Transfer";
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
                .setTicker("notif transfer")
                .setAutoCancel(true)
                .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
                .setLights(Color.RED, 3000, 3000)
                .setDefaults(Notification.DEFAULT_SOUND)
                .setContentTitle("Transfer Sukses")
                .setContentText("Anda berhasil Transfer senilai Rp."+formatter.format(Integer.parseInt(etNominalTrans.getText().toString())))
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText("Saldo anda sekarang : Rp."+formatter.format(sp.getSpSaldo())+
                                ".\nAnda berhasil transfer kepada "+userList.get(0).getNama()+
                                " Senilai Rp."+formatter.format(Integer.parseInt(etNominalTrans.getText().toString()))+
                                ".\nTerima Kasih telah melakukan Transfer Saldo di DOMPET.KU"))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(115, builder.build());
        sp.saveSPString("spUsernameTujuan", "");
    }
}
