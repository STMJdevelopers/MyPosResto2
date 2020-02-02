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

public class CustomerSelectActivity extends AppCompatActivity {
    ListView lstCustomer;
    SqlAdapter mBaseAdapter;
    int[] controls;
    private final int CUSTOMER_SELECTED=3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.customer_select);
        lstCustomer=(ListView)findViewById(R.id.list1);
        controls= new int[]{R.id.company, R.id.address, R.id.customer_no};
        mBaseAdapter=new SqlAdapter(getBaseContext(), R.layout.row_customer,
                controls, "select company,address,customer_no from ksr_customers");
        lstCustomer.setAdapter(mBaseAdapter);
        lstCustomer.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Cursor cursor = (Cursor) mBaseAdapter.getItem(i);
                String sCustNo= String.valueOf(cursor.getString(cursor.getColumnIndex("customer_no")));
                Intent intent=new Intent();
                intent.putExtra("CustNo",sCustNo);
                setResult(CUSTOMER_SELECTED,intent);
                finish();
            }
        });
    }
}
