package com.talaga.myposresto.reports;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.ListView;
import android.widget.TextView;
import com.talaga.myposresto.R;

import com.talaga.myposresto.util.SqlAdapter;

public class Report07 extends AppCompatActivity {
    private ListView lv;
    SqlAdapter mBaseAdapter;
    int[] controls;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.report_page);

        setColumnHeader();

        lv=(ListView)findViewById(R.id.lv1);
        String sql="select customer_no,company,address " +
                "from ksr_customers " +
                "order by customer_no";
        controls= new int[]{R.id.txt1, R.id.txt2, R.id.txt3};
        mBaseAdapter=new SqlAdapter(getBaseContext(),R.layout.row_report,controls);
        mBaseAdapter.showColumnTitle=true;
        mBaseAdapter.colsWidth=new int[]{100,200,200};
        mBaseAdapter.OpenRecordset(sql);

        lv.setAdapter(mBaseAdapter);

        setColumnFooter();

    }
    void setColumnHeader(){

        TextView judul= (TextView) findViewById(R.id.judul);
        judul.setText("Daftar Customer");

        TextView txt1= (TextView) findViewById(R.id.txt1);
        txt1.setText("Kode");
        txt1.setWidth(100);

        TextView txt2= (TextView) findViewById(R.id.txt2);
        txt2.setText("Nama Pelanggan");
        txt2.setWidth(200);

        TextView txt3= (TextView) findViewById(R.id.txt3);
        txt3.setText("Alamat");
        txt3.setWidth(200);


    }
    void setColumnFooter(){


    }
}
