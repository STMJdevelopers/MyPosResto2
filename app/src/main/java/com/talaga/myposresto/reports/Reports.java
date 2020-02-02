package com.talaga.myposresto.reports;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import com.talaga.myposresto.R;

/**
 * Created by compaq on 03/20/2016.
 */
public class Reports extends AppCompatActivity {
    ListView lv1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.report);
        InitButtons();

        final String[] values = new String[] {
                "01. Daftar Penjualan per Nota",
                "02. Daftar Penjualan per Barang",
                "03. Daftar Penjualan per Customer",
                "04. Daftar Penjualan per Supplier",
                "05. Daftar Penjualan per Category",
                "06. Daftar Barang",
                "07. Daftar Pelanggan",
                "08. Daftar Supplier" };

        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, values);
        lv1= (ListView) findViewById(R.id.lv1);
        lv1.setAdapter(adapter1);
        lv1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //Toast.makeText(getBaseContext(),"Laporan: "+values[i],Toast.LENGTH_SHORT).show();
                print_laporan(i);
            }
        });
    }

    private void print_laporan(int value) {
        Intent intent = new Intent();
        value++;
        switch (value){
            case 1:
                startActivity(new Intent(getBaseContext(),Report01.class));
                break;
            case 2:
                startActivity(new Intent(getBaseContext(),Report02.class));
                break;
            case 3:
                startActivity(new Intent(getBaseContext(),Report03.class));
                break;
            case 4:
                startActivity(new Intent(getBaseContext(),Report04.class));
                break;
            case 5:
                startActivity(new Intent(getBaseContext(),Report05.class));
                break;
            case 6:
                startActivity(new Intent(getBaseContext(),Report06.class));
                break;
            case 7:
                startActivity(new Intent(getBaseContext(),Report07.class));
                break;
            case 8:
                startActivity(new Intent(getBaseContext(),Report08.class));
                break;
            case 9:
                startActivity(new Intent(getBaseContext(),Report09.class));
                break;
            default:
                Toast.makeText(getBaseContext(),"Unknown Report Index: "+value, Toast.LENGTH_LONG);
                break;
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
    private void InitButtons(){


    }

}
