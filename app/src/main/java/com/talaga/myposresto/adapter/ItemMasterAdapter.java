package com.talaga.myposresto.adapter;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.talaga.myposresto.ItemMasterActivity;
import com.talaga.myposresto.SalesActivity;
import com.talaga.myposresto.model.ItemMaster;
import com.talaga.myposresto.model.ItemMasterField;
import com.talaga.myposresto.util.Helper;
import com.talaga.myposresto.util.Libs;
import com.talaga.myposresto.R;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;

import static com.talaga.myposresto.util.Global.NAMA_DB;
import static com.talaga.myposresto.util.Global.VERSI_DB;

public class ItemMasterAdapter extends RecyclerView.Adapter<ItemMasterViewHolder>{
    private SQLiteDatabase _db;
    private RecyclerView _view;
    private Context _context;
    private List<ItemMasterField> _data;
    private int _list_view;
    private String sql="select * from ksr_item_master";
    private Cursor _cursor;
    private String[] _fields;
    SalesActivity parentClass;

    public boolean OpenRecordset(){
        _cursor = _db.rawQuery(sql,null);
        _fields=_cursor.getColumnNames();
        if(_fields == null) return false;
        return true;
    }
    public ItemMasterAdapter(Context baseContext, SalesActivity parent) {
        _context=baseContext;
        parentClass=parent;
        Helper dbHelper = new Helper(_context, NAMA_DB, null, VERSI_DB);
        try {
            _db = dbHelper.getWritableDatabase();
        }
        catch (SQLiteException e) {
            _db = dbHelper.getReadableDatabase();
        }
        OpenRecordset();
    }


    @NonNull
    @Override
    public ItemMasterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if(this._list_view==1){
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_item2, parent, false);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_item2, parent, false);
        }
        return new ItemMasterViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ItemMasterViewHolder holder, int position) {
        if (holder.barcode==null)return ;
        _cursor.moveToPosition(position);
        ItemMasterField productObject=new ItemMasterField();
        productObject.getRecord(_cursor);
        holder.barcode.setText(productObject.getBarcode());
        holder.nama_barang.setText(productObject.getName());
        String sPrice=String.valueOf(productObject.getPrice());
        holder.harga.setText(Libs.formatRupiah(sPrice));
        String sFile;
        if(productObject.get_picture()==null){
            sFile="noimage.png";
        } else {
            sFile=productObject.get_picture();
        }
        FileInputStream streamIn = null;
        try {
            streamIn = new FileInputStream(sFile);
            Bitmap bitmap = BitmapFactory.decodeStream(streamIn);
            holder.gambar.setImageBitmap(bitmap);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        final String sKode = holder.barcode.getText().toString();
        holder.cvItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(_context,"Barcode terpilih:"  + sKode,Toast.LENGTH_LONG).show();
            viewItem(sKode);
            }
        });
        holder.btnBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addItem(sKode,1);
            }
        });
    }
    private void viewItem(String sKode){
        Intent intent=new Intent(_context, ItemMasterActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("mode","view");
        intent.putExtra("barcode",sKode);
        _context.startActivity(intent);

    }

    @Override
    public int getItemCount() {
        return _cursor.getCount();
    }
    private void addItem(String kode,int qty){
        parentClass.addItem(kode, String.valueOf(qty));
    }
}
