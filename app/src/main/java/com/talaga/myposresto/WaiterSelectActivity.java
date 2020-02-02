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

public class WaiterSelectActivity extends AppCompatActivity {
    ListView lstWaiter;
    SqlAdapter mBaseAdapter;
    int[] controls;
    private final int WAITER_SELECTED=5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.waiter_select);
        lstWaiter=(ListView)findViewById(R.id.list1);
        controls= new int[]{R.id.nama_barang, R.id.barcode, R.id.harga};
        mBaseAdapter=new SqlAdapter(getBaseContext(), R.layout.row_item,
                controls, "select waiter_no,description,status from ksr_waiters");
        lstWaiter.setAdapter(mBaseAdapter);
        lstWaiter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Cursor cursor = (Cursor) mBaseAdapter.getItem(i);
                String sCustNo= String.valueOf(cursor.getString(cursor.getColumnIndex("waiter_no")));
                Intent intent=new Intent();
                intent.putExtra("WaiterNo",sCustNo);
                setResult(WAITER_SELECTED,intent);
                finish();
            }
        });
    }
}
