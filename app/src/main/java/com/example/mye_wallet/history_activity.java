package com.example.mye_wallet;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class history_activity extends AppCompatActivity {
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.history_list_activity);

        listView = findViewById(R.id.listView);

        listView.setAdapter(new historyAdapter(getData(), this));

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Transaksi clicked = (Transaksi) listView.getItemAtPosition(position);
                String pesan = clicked.getDeskripsi();
                Toast.makeText(history_activity.this, pesan, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public List<Transaksi> getData() {
        DatabaseUser db = DatabaseUser.getInstance(this);
        SharedPrefUser sp = new SharedPrefUser(this);

        List<Transaksi> tList = db.selectTransWhere(sp.getSpIdUser());

        return tList;
    }
}
