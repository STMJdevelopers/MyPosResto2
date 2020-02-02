package com.talaga.myposresto.reports;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import com.talaga.myposresto.R;

import com.talaga.myposresto.util.Helper;
import com.talaga.myposresto.util.SqlAdapter;

import java.text.DecimalFormat;

import static com.talaga.myposresto.util.Global.NAMA_DB;
import static com.talaga.myposresto.util.Global.VERSI_DB;

public class Report04 extends AppCompatActivity {
    private ListView lv;
    SqlAdapter mBaseAdapter;
    int[] controls;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.report_page);

        setColumnHeader();

        lv=(ListView)findViewById(R.id.lv1);
        String sql="select stk.supplier,s.supplier_name, sum(i.amount) as zamt " +
                "from ksr_sales_header i " +
                "left join ksr_sales_detail il on il.invoice_no=i.invoice_no  " +
                "left join ksr_item_master stk on stk.barcode=il.barcode " +
                "left join ksr_suppliers s on s.supplier_no=stk.supplier " +
                "group by stk.supplier,s.supplier_name";
        controls= new int[]{R.id.txt1, R.id.txt2, R.id.txt3};
        mBaseAdapter=new SqlAdapter(getBaseContext(),R.layout.row_report,controls);
        mBaseAdapter.formatColumns("zamt","#,###");
        mBaseAdapter.showColumnTitle=true;
        mBaseAdapter.colsWidth=new int[]{100,200,100,50};
        mBaseAdapter.OpenRecordset(sql);

        lv.setAdapter(mBaseAdapter);

        setColumnFooter();

    }
    void setColumnHeader(){

        TextView judul= (TextView) findViewById(R.id.judul);
        judul.setText("SalesActivity by Supplier");

        TextView txt1= (TextView) findViewById(R.id.txt1);
        txt1.setText("Supp No");
        txt1.setWidth(100);

        TextView txt2= (TextView) findViewById(R.id.txt2);
        txt2.setText("Supplier Name");
        txt2.setWidth(200);

        TextView txt3= (TextView) findViewById(R.id.txt3);
        txt3.setText("Amount");
        txt3.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
        txt3.setWidth(100);

    }
    void setColumnFooter(){
        double sumQty=0,sumAmt=0;

        String sql="select  sum(i.amount) as zamt " +
                "from ksr_sales_header i ";

        SQLiteDatabase _db;
        Cursor _cursor;
        Helper dbHelper = new Helper(this, NAMA_DB, null, VERSI_DB);
        try {
            _db = dbHelper.getWritableDatabase();
        }
        catch (SQLiteException e) {
            _db = dbHelper.getReadableDatabase();
        }
        _cursor = _db.rawQuery(sql,null);
        _cursor.moveToPosition(0);
        DecimalFormat df = new DecimalFormat("###,###");

        if(_cursor.getString(0)!=null) sumAmt= Double.parseDouble(_cursor.getString(0));

        TextView txt1= (TextView) findViewById(R.id.txt1f);
        txt1.setText("TOTAL");
        txt1.setWidth(100);

        TextView txt2= (TextView) findViewById(R.id.txt2f);
        txt2.setText("");
        txt2.setWidth(200);

        TextView txt3= (TextView) findViewById(R.id.txt3f);
        txt3.setText(df.format(sumAmt));
        txt3.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
        txt3.setWidth(100);

    }
}
