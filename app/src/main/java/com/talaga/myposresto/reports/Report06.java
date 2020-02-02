package com.talaga.myposresto.reports;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import com.talaga.myposresto.R;

import com.talaga.myposresto.util.SqlAdapter;

public class Report06 extends AppCompatActivity {
    private ListView lv;
    SqlAdapter mBaseAdapter;
    int[] controls;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.report_page);

        setColumnHeader();

        lv=(ListView)findViewById(R.id.lv1);
        String sql="select i.barcode,i.item_name,i.unit,i.price, " +
                "i.cost, i.category, c.description as cat_name, " +
                "i.supplier, s.supplier_name " +
                "from ksr_item_master i " +
                "left join ksr_suppliers s " +
                "left join ksr_category c " +
                "order by i.barcode";
        controls= new int[]{R.id.txt1, R.id.txt2, R.id.txt3, R.id.txt4,
                R.id.txt5, R.id.txt6, R.id.txt7, R.id.txt8, R.id.txt9};
        mBaseAdapter=new SqlAdapter(getBaseContext(),R.layout.row_report,controls);
        mBaseAdapter.formatColumns("price","#,###");
        mBaseAdapter.formatColumns("cost","#,###");
        mBaseAdapter.showColumnTitle=true;
        mBaseAdapter.colsWidth=new int[]{100,200,50,50,50,
            50,50,100,
            50,100};
        mBaseAdapter.OpenRecordset(sql);

        lv.setAdapter(mBaseAdapter);

        setColumnFooter();

    }
    void setColumnHeader(){

        TextView judul= (TextView) findViewById(R.id.judul);
        judul.setText("Daftar Barang");

        TextView txt1= (TextView) findViewById(R.id.txt1);
        txt1.setText("Kode");
        txt1.setWidth(100);

        TextView txt2= (TextView) findViewById(R.id.txt2);
        txt2.setText("Nama Barang");
        txt2.setWidth(200);

        TextView txt3= (TextView) findViewById(R.id.txt3);
        txt3.setText("Unit");
        //txt3.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
        txt3.setWidth(50);

        TextView txt4= (TextView) findViewById(R.id.txt4);
        txt4.setText("Price");
        txt4.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
        txt4.setWidth(50);

        TextView txt5= (TextView) findViewById(R.id.txt5);
        txt5.setText("Cost");
        txt5.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
        txt5.setWidth(50);

        TextView txt6= (TextView) findViewById(R.id.txt6);
        txt6.setText("Cat");
        txt6.setWidth(50);

        TextView txt7= (TextView) findViewById(R.id.txt7);
        txt7.setText("Cat Name");
        txt7.setWidth(50);

        TextView txt8= (TextView) findViewById(R.id.txt8);
        txt8.setText("Supp");
        txt8.setWidth(50);

        TextView txt9= (TextView) findViewById(R.id.txt9);
        txt9.setText("Supp Name");
        txt9.setWidth(50);


    }
    void setColumnFooter(){


    }
}
