package com.talaga.myposresto.adapter;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.talaga.myposresto.R;

public class ItemMasterViewHolder extends RecyclerView.ViewHolder {
    public ImageView gambar;
    public TextView barcode, nama_barang, harga;
    public View view;
    public CardView cvItem;
    public Button btnBuy;
    public ItemMasterViewHolder(@NonNull View itemView) {
        super(itemView);
        barcode = itemView.findViewById(R.id.barcode);
        nama_barang = itemView.findViewById(R.id.nama_barang);
        harga =  itemView.findViewById(R.id.harga);
        gambar = itemView.findViewById(R.id.gambar);
        cvItem = itemView.findViewById(R.id.cvItem);
        btnBuy = itemView.findViewById(R.id.btnBuy);

    }
}
