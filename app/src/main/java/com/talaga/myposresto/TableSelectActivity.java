package com.talaga.myposresto;

import android.content.Intent;
import android.database.Cursor;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import com.talaga.myposresto.R;

import com.talaga.myposresto.util.SqlAdapter;

public class TableSelectActivity extends AppCompatActivity {
    ListView lstTable;
    SqlAdapter mBaseAdapter;
    int[] controls;
    private final int TABLE_SELECTED=4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.table_select);
        lstTable=(ListView)findViewById(R.id.list1);
        controls= new int[]{R.id.nama_barang, R.id.barcode, R.id.harga};
        mBaseAdapter=new SqlAdapter(getBaseContext(), R.layout.row_item,
                controls, "select table_no,description,status from ksr_tables");
        lstTable.setAdapter(mBaseAdapter);
        lstTable.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Cursor cursor = (Cursor) mBaseAdapter.getItem(i);
                String sCustNo= String.valueOf(cursor.getString(cursor.getColumnIndex("table_no")));
                Intent intent=new Intent();
                intent.putExtra("TableNo",sCustNo);
                setResult(TABLE_SELECTED,intent);
                finish();
            }
        });
    }
}
