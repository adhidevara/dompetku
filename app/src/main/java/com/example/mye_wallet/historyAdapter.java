package com.example.mye_wallet;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.List;

public class historyAdapter extends BaseAdapter {
    private List<Transaksi> transaksis;
    Context context;
    LayoutInflater inflater;

    public historyAdapter(List<Transaksi> transaksis, Context context) {
        this.transaksis = transaksis;
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return transaksis.size();
    }

    @Override
    public Object getItem(int i) {
        return transaksis.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.listview_history, null);
            holder = new ViewHolder();
            holder.txNominalHis = convertView.findViewById(R.id.txNominalHis);
            holder.txDeskripsiHis = convertView.findViewById(R.id.txDeskripsiHis);
            holder.txTipeHis = convertView.findViewById(R.id.txTipeHis);
            holder.txTanggalHis = convertView.findViewById(R.id.txTanggalHis);
            holder.txIdTransHis = convertView.findViewById(R.id.txIdTransHis);
            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }

        int bckgColor = 0;
        DecimalFormat formatter = new DecimalFormat("#,###,###");
        String nominal = formatter.format(transaksis.get(position).getNominal());
        switch (transaksis.get(position).getTipe()) {
            case "DEBIT" :
                holder.txNominalHis.setText("+ "+"Rp."+nominal);
                bckgColor = convertView.getResources().getColor(R.color.item_tx_debit);
                break;
            case "KREDIT" :
                holder.txNominalHis.setText("- "+"Rp."+nominal);
                bckgColor = convertView.getResources().getColor(R.color.item_tx_kredit);
                break;
        }
        convertView.setBackgroundColor(bckgColor);


        holder.txDeskripsiHis.setText(transaksis.get(position).getDeskripsi());
        holder.txTipeHis.setText(transaksis.get(position).getTipe());
        holder.txTanggalHis.setText(transaksis.get(position).getTgl_transaksi());
        holder.txIdTransHis.setText("ID Transaksi : "+String.valueOf(transaksis.get(position).getId_transaksi()));

        return convertView;
    }

    private class ViewHolder {
        TextView txNominalHis, txDeskripsiHis, txTipeHis, txTanggalHis, txIdTransHis;
    }
}
