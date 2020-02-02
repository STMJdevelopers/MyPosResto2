package com.talaga.myposresto;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.appcompat.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import com.talaga.myposresto.R;

import com.talaga.myposresto.util.Helper;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.text.DecimalFormat;
import java.util.ArrayList;

import static com.talaga.myposresto.util.Global.NAMA_DB;
import static com.talaga.myposresto.util.Global.VERSI_DB;

/**
 * Created by andri on 03/02/2017.
 */

public class ItemMasterSelectActivity extends AppCompatActivity {
    ListView lv1;
    private BaseAdapter mBaseAdapter = null;
    private int[] controls;
    private final int ITEM_SELECTED=2;
    FloatingActionButton fab;
    FloatingActionButton fabSearch;
    TextView txtTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list1);

        fab  = (FloatingActionButton) findViewById(R.id.fab);
        fab.setVisibility(View.GONE);
        fabSearch = (FloatingActionButton) findViewById(R.id.fabSearch);
        fabSearch.setVisibility(View.GONE);
        txtTitle = (TextView) findViewById(R.id.txtTitle);
        txtTitle.setText("Select Item Master");


        lv1 = (ListView) findViewById(R.id.list1);
/*
        lv1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Cursor cursor = (Cursor) mBaseAdapter.getItem(i);
                String sItemNo= String.valueOf(cursor.getString(cursor.getColumnIndex("barcode")));
                Intent intent=new Intent();
                intent.putExtra("ItemNo",sItemNo);
                intent.putExtra("Qty","1");
                setResult(ITEM_SELECTED,intent);
                finish();
            }
        });
*/

        loadData();
    }

    private void loadData() {
        controls = new int[]{R.id.nama, R.id.barcode, R.id.harga,R.id.qty,R.id.gambar};
        mBaseAdapter = new Adapter(getBaseContext(), R.layout.row_item_select);
        lv1.setAdapter(mBaseAdapter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private class Adapter extends BaseAdapter {
        private SQLiteDatabase _db;
        private LayoutInflater _layoutInflater;
        private String[] _fields;
        private Cursor _cursor;
        private Context _context;
        private ArrayList<TextView> _arTextView=null;
        private int _row_layout;
        private ArrayList<Object> _data=null;
        private int[] _controls;
        private String sql="select item_name,barcode,category,price,picture from ksr_item_master";
        private final int ITEM_SELECTED=2;

        public Adapter(Context aContext, int layout) {
            _context=aContext;
            _layoutInflater = LayoutInflater.from(_context);
            _row_layout=layout;
            Helper dbHelper = new Helper(_context, NAMA_DB, null, VERSI_DB);
            try {
                _db = dbHelper.getWritableDatabase();
            }
            catch (SQLiteException e) {
                _db = dbHelper.getReadableDatabase();
            }
            OpenRecordset(sql);
        }
        private boolean OpenRecordset(String sql){
            _cursor = _db.rawQuery(sql,null);
            _fields=_cursor.getColumnNames();
            if(_fields == null) return false;

            return true;
        }


        @Override
        public int getCount() {
            return _cursor.getCount();
        }

        @Override
        public Object getItem(int position) {
            _cursor.moveToPosition(position);
            return _cursor;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ViewHolder holder=new ViewHolder();

            if (convertView == null) {
                convertView = _layoutInflater.inflate(_row_layout, null);
                holder.nama_barang = (TextView) convertView.findViewById(R.id.nama);
                holder.barcode = (TextView) convertView.findViewById(R.id.barcode);
                holder.harga = (TextView) convertView.findViewById(R.id.harga);
                holder.gambar = (ImageView) convertView.findViewById(R.id.gambar);
                holder.qty = (TextView) convertView.findViewById(R.id.qty);
                holder.submit = (Button) convertView.findViewById(R.id.submit);
                holder.plus = (Button) convertView.findViewById(R.id.btnPlus);
                holder.minus = (Button) convertView.findViewById(R.id.btnMin);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }


            _cursor.moveToPosition(position);

            String value=_cursor.getString(_cursor.getColumnIndex("item_name"));
            holder.nama_barang.setText(value);

            value=_cursor.getString(_cursor.getColumnIndex("barcode"));
            holder.barcode.setText(value);

            value=_cursor.getString(_cursor.getColumnIndex("price"));
            if(value.isEmpty())value="0";
            DecimalFormat df = new DecimalFormat("###,###.##"); // or pattern "###,###.##$"
            holder.harga.setText(df.format(Double.valueOf(value)));

            String value_picture=_cursor.getString(_cursor.getColumnIndex("picture"));
            Bitmap bitmap = null;
            FileInputStream streamIn = null;
            try {
                streamIn = new FileInputStream(value_picture);
                bitmap = BitmapFactory.decodeStream(streamIn);
                holder.gambar.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            final ViewHolder finalHolder = holder;
            holder.submit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent();
                    intent.putExtra("ItemNo",finalHolder.barcode.getText().toString());
                    intent.putExtra("Qty",finalHolder.qty.getText().toString());
                    setResult(ITEM_SELECTED,intent);
                    finish();
                }
            });
            holder.minus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int qty_new=0;
                    qty_new= Integer.parseInt(finalHolder.qty.getText().toString());
                    qty_new--;
                    finalHolder.qty.setText(""+qty_new);
                }
            });
            holder.plus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int qty_new=0;
                    qty_new= Integer.parseInt(finalHolder.qty.getText().toString());
                    qty_new++;
                    finalHolder.qty.setText(""+qty_new);
                }
            });


            return convertView;
        }
        class ViewHolder {
            Button submit,plus,minus;
            TextView barcode,nama_barang,harga,qty;
            ImageView gambar;

        }

    }

}