package com.talaga.myposresto;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.talaga.myposresto.util.SqlAdapter;
import com.talaga.myposresto.R;

/**
 * Created by compaq on 03/20/2016.
 */
public class PendingOrderActivity extends AppCompatActivity {
    ListView list1;
    Button btnRefresh;
    int[] ctr;
    SqlAdapter mBaseAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pending);
        list1 = (ListView) findViewById(R.id.list1);

        InitButtons();
        loadData();


    }
    @Override
    public void onDestroy() {
        super.onDestroy();
    }
    private void InitButtons(){
        btnRefresh= (Button) findViewById(R.id.btnRefresh);
        btnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadData();
            }
        });

    }
    private void loadData(){
        ctr = new int[]{R.id.txtNota, R.id.txtDate, R.id.txtCustomer, R.id.txtAmount};
        mBaseAdapter = new SqlAdapter(getBaseContext(), R.layout.row_nota,
                ctr, "select invoice_no,invoice_date,customer,amount from ksr_sales_header " +
                "where (paid=0 or paid is null)");
        mBaseAdapter.formatColumns("amount","###,###.##");
        list1.setAdapter(mBaseAdapter);
        list1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                TextView nota= (TextView) view.findViewById(R.id.txtNota);
                Intent myIntent4 = new Intent(getBaseContext(), SalesActivity.class);
                myIntent4.putExtra("nota",nota.getText().toString());
                myIntent4.putExtra("mode","edit");
                startActivity(myIntent4);
            }
        });

    }


}
