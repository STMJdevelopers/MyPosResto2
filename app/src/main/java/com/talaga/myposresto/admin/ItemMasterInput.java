package com.talaga.myposresto.admin;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.talaga.myposresto.util.Recordset;
import com.talaga.myposresto.R;
import com.talaga.myposresto.util.RealPathUtil;
import com.talaga.myposresto.util.SqlAdapter;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Created by compaq on 03/25/2016.
 */
public class ItemMasterInput extends AppCompatActivity {

    private static final int GALLERY_REQUEST = 10 ;
    Recordset _item;
    String _mode,_barcode;
    EditText txtBarcode,txtItemName,txtUnit,txtPicture,txtItemNameLong;
    EditText txtPrice,txtCost;
    Button cmdDel,cmdSave,cmdClose;
    ImageView photo;
    Spinner txtCategory,txtSupplier;
    private SQLiteDatabase _db;
    private Cursor _cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_master);
        prepareControls();
        cmdDel.setVisibility(View.INVISIBLE);
        cmdDel.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                DeleteRecord();
            }
        });

        cmdSave.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                if(SaveRecord()) {
                    finish();
                }
            }
        });
        cmdClose.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                finish();
            }
        });
        _mode="add";
        Bundle bundle = getIntent().getExtras();
        if(bundle!=null) {
            if (bundle.getString("mode") != null) {
                _mode = bundle.getString("mode");
                _barcode = bundle.getString("barcode");
                cmdDel.setVisibility(View.VISIBLE);
                EditRecord();
            }
        }
        photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), GALLERY_REQUEST);
            }
        });

    }
    private void prepareControls(){
        cmdDel=  findViewById(R.id.cmdDelete);
        cmdSave= findViewById(R.id.cmdSave);
        cmdClose= findViewById(R.id.cmdClose);
        txtBarcode=  findViewById(R.id.txtBarcode);
        txtItemName =  findViewById(R.id.txtItemName);
        txtUnit =  findViewById(R.id.txtUnit);
        txtPicture = findViewById(R.id.txtIcon);
        txtSupplier =  findViewById(R.id.txtSupplier);
        loadSupplier();
        txtCategory =  findViewById(R.id.txtCategory);
        loadCategory();

        txtPrice =  findViewById(R.id.txtSales);
        txtCost =  findViewById(R.id.txtPurchase);
        photo= findViewById(R.id.photo);
        txtItemNameLong = findViewById(R.id.txtNamaBarangPanjang);

    }
    private void EditRecord(){
        _item= new Recordset(getBaseContext());
        _item.OpenRecordset("select * from ksr_item_master where barcode='" + _barcode + "'",
                "ksr_item_master","barcode");
        txtBarcode.setText(_item.getString("barcode"));
        txtBarcode.setEnabled(false);
        txtItemName.setText(_item.getString("item_name"));
        txtUnit.setText(_item.getString("unit"));
        txtPrice.setText(_item.getString("price"));
        txtCost.setText(_item.getString("cost"));
        txtPicture.setText(_item.getString("picture"));
        txtItemNameLong.setText(_item.getString("item_name_long"));

        txtSupplier.setSelection(((ArrayAdapter<String>)txtSupplier.getAdapter())
                .getPosition(_item.getString("supplier")));

        txtCategory.setSelection(((ArrayAdapter<String>)txtCategory.getAdapter())
                .getPosition(_item.getString("category")));


        Bitmap bitmap = null;
        try {
            String img=txtPicture.getText().toString();
            //bitmap = MediaStore.Images.Media.getBitmap(
            //        getContentResolver(), Uri.parse(img));
            FileInputStream streamIn = new FileInputStream(img);
            bitmap = BitmapFactory.decodeStream(streamIn);
            photo.setImageBitmap(bitmap);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    private Bitmap getBitmapFromUri(Uri uri) throws IOException {
        ParcelFileDescriptor parcelFileDescriptor =
                getContentResolver().openFileDescriptor(uri, "r");
        FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
        Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
        parcelFileDescriptor.close();
        return image;
    }
    private boolean valid(){
        boolean ret=false;
        if(txtPrice.getText().toString().isEmpty())txtPrice.setText("0");
        if(txtCost.getText().toString().isEmpty())txtCost.setText("0");

        if(txtBarcode.getText()!=null){
            if(txtBarcode.getText().toString()!="")ret=true;
        }
        if(txtCategory.getSelectedItem()!=null){
            if(txtCategory.getSelectedItem().toString()!="")ret=true;
        }
        if(txtSupplier.getSelectedItem()!=null){
            if(txtSupplier.getSelectedItem().toString()!="")ret=true;
        }
        return ret;
    }
    private boolean SaveRecord() {
        if (!valid()){
            Toast.makeText(getApplicationContext(),"Periksa inputan anda !",Toast.LENGTH_LONG).show();
            return false;
        }
        _item= new Recordset(getBaseContext());
        _item.OpenRecordset("select * from ksr_item_master where barcode='" + txtBarcode.getText().toString()+ "'",
                "ksr_item_master","barcode");
        if (_item.eof()) {
            _item.addNew();
            _item.put("barcode",txtBarcode.getText().toString());
        }
        _item.put("item_name",txtItemName.getText().toString());
        _item.put("unit",txtUnit.getText().toString());
        _item.put("price",txtPrice.getText().toString());
        _item.put("cost",txtCost.getText().toString());
        _item.put("picture",txtPicture.getText().toString());
        _item.put("item_name_long",txtItemNameLong.getText().toString());

        if(txtSupplier.getSelectedItem()!=null)_item.put("supplier",txtSupplier.getSelectedItem().toString());
        if(txtCategory.getSelectedItem()!=null)_item.put("category",txtCategory.getSelectedItem().toString());
        int id= _item.save();
        Toast.makeText(getApplicationContext(),
                "Data sudah disimpan, tekan refresh bila diperlukan.",
                Toast.LENGTH_SHORT).show();
        return id>-1;
    }
    private void DeleteRecord(){
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Delete")
                .setMessage("Are you sure you want to delete this item?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        EditText txtId = (EditText) findViewById(R.id.txtBarcode);
                        _item.delete(txtId.getText().toString());
                        finish();
                    }

                })
                .setNegativeButton("No", null)
                .show();
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Activity.RESULT_OK)
            switch (requestCode){
                case GALLERY_REQUEST:
                    String realPath;
                    // SDK < API11
                    if (Build.VERSION.SDK_INT < 11)
                        realPath = RealPathUtil.getRealPathFromURI_BelowAPI11(this, data.getData());

                        // SDK >= 11 && SDK < 19
                    else if (Build.VERSION.SDK_INT < 19)
                        realPath = RealPathUtil.getRealPathFromURI_API11to18(this, data.getData());

                        // SDK > 19 (Android 4.4)
                    else
                        realPath = RealPathUtil.getRealPathFromURI_API19(this, data.getData());


                    txtPicture.setText(realPath);
                    Uri uriFromPath = Uri.fromFile(new File(realPath));
                    Bitmap bitmap = null;
                    try {
                        bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(uriFromPath));
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    photo.setImageBitmap(bitmap);
                    break;
            }
    }
    void loadCategory(){
        String sql="select * from ksr_category";
        txtCategory.setAdapter(new SqlAdapter(getBaseContext(),sql).FillSpinner("category"));
    }
    void loadSupplier(){
        String sql="select * from ksr_suppliers";
        txtSupplier.setAdapter(new SqlAdapter(getBaseContext(),sql).FillSpinner("supplier_no"));
    }

}
