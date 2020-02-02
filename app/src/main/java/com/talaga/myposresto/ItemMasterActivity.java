package com.talaga.myposresto;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.talaga.myposresto.util.Recordset;
import com.talaga.myposresto.R;

import java.io.FileInputStream;
import java.io.IOException;

public class ItemMasterActivity extends AppCompatActivity {
    private String _mode="add";
    private String _barcode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_master);
        Bundle bundle = getIntent().getExtras();
        if(bundle!=null) {
            if (bundle.getString("mode") != null) {
                _mode = bundle.getString("mode");
                _barcode = bundle.getString("barcode");
                EditRecord();
            }
        }

    }
    private void EditRecord(){
        Recordset _item= new Recordset(getBaseContext());
        _item.OpenRecordset("select * from ksr_item_master where barcode='" + _barcode + "'",
                "ksr_item_master","barcode");
        TextView txtBarcode;
        txtBarcode = findViewById(R.id.barcode);
        txtBarcode.setText(_item.getString("barcode"));

        TextView txtItemName;
        txtItemName = findViewById(R.id.nama_barang);
        txtItemName.setText(_item.getString("item_name"));

        TextView txtPrice;
        txtPrice = findViewById(R.id.price);
        txtPrice.setText(_item.getString("price"));

        ImageView photo;
        photo = findViewById(R.id.gambar);

        TextView txtNamaPanjang;
        txtNamaPanjang = findViewById(R.id.nama_panjang);
        txtNamaPanjang.setText(_item.getString("item_name_long"));

        TextView txtCategory;
        txtCategory = findViewById(R.id.category);
        txtCategory.setText(_item.getString("category"));

        Bitmap bitmap = null;
        try {
            String img=_item.getString("picture");
            FileInputStream streamIn = new FileInputStream(img);
            bitmap = BitmapFactory.decodeStream(streamIn);
            photo.setImageBitmap(bitmap);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


}
