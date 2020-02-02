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

public class Report02 extends AppCompatActivity {

    private ListView lv;
    SqlAdapter mBaseAdapter;
    int[] controls;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.report_page);
        setColumnHeader();
        lv=(ListView)findViewById(R.id.lv1);
        String sql="select il.barcode,il.item_name,il.unit,sum(il.qty) as zqty, sum(il.amount) as zamt " +
                "from ksr_sales_detail il left join ksr_sales_header i " +
                "on il.invoice_no=i.invoice_no " +
                "group by il.barcode,il.item_name,il.unit";
        controls= new int[]{R.id.txt1, R.id.txt2, R.id.txt3,R.id.txt4,R.id.txt5};
        mBaseAdapter=new SqlAdapter(getBaseContext(),R.layout.row_report,controls);
        mBaseAdapter.formatColumns("zamt","#,###");
        mBaseAdapter.formatColumns("zqty","#,###");
        mBaseAdapter.showColumnTitle=true;
        mBaseAdapter.colsWidth=new int[]{100,200,50,50,100};
        mBaseAdapter.OpenRecordset(sql);

        lv.setAdapter(mBaseAdapter);

        setColumnFooter();

    }
    void setColumnHeader(){

        TextView judul= (TextView) findViewById(R.id.judul);
        judul.setText("SalesActivity by Item");

        TextView txt1= (TextView) findViewById(R.id.txt1);
        txt1.setText("Item No");
        txt1.setWidth(100);

        TextView txt2= (TextView) findViewById(R.id.txt2);
        txt2.setText("Item Name");
        txt2.setWidth(200);

        TextView txt3= (TextView) findViewById(R.id.txt3);
        txt3.setText("Unit");
        txt3.setWidth(50);

        TextView txt4= (TextView) findViewById(R.id.txt4);
        txt4.setText("Qty");
        txt4.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
        txt4.setWidth(50);

        TextView txt5= (TextView) findViewById(R.id.txt5);
        txt5.setText("Amount");
        txt5.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
        txt5.setWidth(100);

    }
    void setColumnFooter(){
        double sumQty=0,sumAmt=0;

        String sql="select sum(il.qty) as zqty, sum(il.amount) as zamt " +
                "from ksr_sales_detail il left join ksr_sales_header i " +
                "on il.invoice_no=i.invoice_no ";

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
        //_fields=_cursor.getColumnNames();
        //if(_fields == null) return false;
        _cursor.moveToPosition(0);

        if(_cursor.getString(0)!=null) sumQty= Double.parseDouble(_cursor.getString(0));
        if(_cursor.getString(1)!=null) sumAmt= Double.parseDouble(_cursor.getString(1));

        TextView txt1= (TextView) findViewById(R.id.txt1f);
        txt1.setText("TOTAL");
        txt1.setWidth(100);

        TextView txt2= (TextView) findViewById(R.id.txt2f);
        txt2.setText("");
        txt2.setWidth(200);

        TextView txt3= (TextView) findViewById(R.id.txt3f);
        txt3.setText("");
        txt3.setWidth(50);

        DecimalFormat df = new DecimalFormat("###,###"); // or pattern "###,###.##$"
        TextView txt4= (TextView) findViewById(R.id.txt4f);
        txt4.setText(String.valueOf(df.format(sumQty)));
        txt4.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
        txt4.setWidth(50);

        TextView txt5= (TextView) findViewById(R.id.txt5f);
        txt5.setText(String.valueOf(df.format(sumAmt)));
        txt5.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
        txt5.setWidth(100);

    }
}
