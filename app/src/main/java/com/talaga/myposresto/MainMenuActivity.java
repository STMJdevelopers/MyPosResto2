package com.talaga.myposresto;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

//import com.google.android.gms.ads.AdRequest;
//import com.google.android.gms.ads.AdView;
//import com.google.android.gms.ads.MobileAds;
import com.talaga.myposresto.admin.CategoryList;
import com.talaga.myposresto.admin.CustomersList;
import com.talaga.myposresto.admin.ItemMasterInput;
import com.talaga.myposresto.admin.ItemMasterList;
import com.talaga.myposresto.admin.SuppliersList;
import com.talaga.myposresto.admin.TablesList;
import com.talaga.myposresto.admin.WaitersList;
import com.talaga.myposresto.R;
import com.talaga.myposresto.reports.Reports;
import com.talaga.myposresto.util.Setting;
import com.talaga.myposresto.util.SqlAdapterView;


/**
 * Created by compaq on 02/29/2016.
 */
public class MainMenuActivity extends AppCompatActivity {
    Button btnExit,btnHelp,btnNewOrder,btnItemMaster,btnCategory;
    Button btnCustomer,btnSupplier,btnPending;
    Button btnReport,btnSetting,btnWaiter,btnTable;
    RecyclerView listItems;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_utama);

        //MobileAds.initialize(getApplicationContext(), "ca-app-pub-1869511402643723~8811604696");
        //AdView mAdView = (AdView) findViewById(R.id.adView);
        //AdRequest adRequest = new AdRequest.Builder().build();
        //mAdView.loadAd(adRequest);

        btnExit=(Button) findViewById(R.id.btnExit);
        btnExit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                cmdExit();
            }
        });

        btnHelp=(Button) findViewById(R.id.btnHelp);
        btnHelp.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent myIntent1 = new Intent(getBaseContext(), HelpActivity.class );
                startActivity(myIntent1);
            }
        });
        btnItemMaster=(Button) findViewById(R.id.btnItemMaster);
        btnItemMaster.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent myIntent3 = new Intent(getBaseContext(), ItemMasterList.class );
                startActivity(myIntent3);
            }
        });

        btnNewOrder=(Button) findViewById(R.id.btnNewOrder);
        btnNewOrder.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent myIntent2 = new Intent(getBaseContext(), SalesActivity.class );
                startActivity(myIntent2);
            }
        });


        btnCategory=(Button) findViewById(R.id.btnCategory);
        btnCategory.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent myIntent4 = new Intent(getBaseContext(), CategoryList.class );
                startActivity(myIntent4);
            }
        });
        btnCustomer=(Button) findViewById(R.id.btnCustomer);
        btnCustomer.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent myIntent5 = new Intent(getBaseContext(), CustomersList.class);
                startActivity(myIntent5);
            }
        });

        btnPending=(Button) findViewById(R.id.btnPendingOrder);
        btnPending.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent myIntent6 = new Intent(getBaseContext(), PendingOrderActivity.class );
                startActivity(myIntent6);
            }
        });
        btnReport=(Button) findViewById(R.id.btnReports);
        btnReport.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent myIntent7 = new Intent(getBaseContext(), Reports.class );
                startActivity(myIntent7);
            }
        });
        btnSetting=(Button) findViewById(R.id.btnSetting);
        btnSetting.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent myIntent8 = new Intent(getBaseContext(), Setting.class );
                startActivity(myIntent8);
            }
        });
        btnSupplier=(Button) findViewById(R.id.btnSupplier);
        btnSupplier.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent myIntent9 = new Intent(getBaseContext(), SuppliersList.class );
                startActivity(myIntent9);
            }
        });
        btnWaiter=(Button) findViewById(R.id.btnWaiters);
        btnWaiter.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent myIntent10 = new Intent(getBaseContext(), WaitersList.class );
                startActivity(myIntent10);
            }
        });
        btnTable=(Button) findViewById(R.id.btnTable);
        btnTable.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent myIntent11 = new Intent(getBaseContext(), TablesList.class );
                startActivity(myIntent11);
            }
        });
    }
    private void loadData() {
        int[] controls = new int[]{R.id.nama_barang, R.id.barcode, R.id.harga};
        SqlAdapterView mBaseAdapter = new SqlAdapterView(getBaseContext(), R.layout.row_item,
                controls, "select item_name,barcode,price,picture from ksr_item_master");
        mBaseAdapter.formatColumns("price","###,###.##");

        listItems.setAdapter(mBaseAdapter);
        listItems.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView kode = (TextView) v.findViewById(R.id.barcode);
                Intent myIntent4 = new Intent(getBaseContext(), ItemMasterInput.class);
                myIntent4.putExtra("barcode",kode.getText().toString());
                myIntent4.putExtra("mode","edit");
                startActivity(myIntent4);

            }
        });

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void cmdExit(){
        finish();
    }
}
