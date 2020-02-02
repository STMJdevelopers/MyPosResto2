package com.talaga.myposresto;

import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.talaga.myposresto.adapter.ItemMasterAdapter;
import com.talaga.myposresto.model.SalesPaymentCard;
import com.talaga.myposresto.model.SalesPaymentCash;
import com.talaga.myposresto.util.BTDeviceList;
import com.talaga.myposresto.util.Global;
import com.talaga.myposresto.util.Helper;
import com.talaga.myposresto.util.Libs;
import com.talaga.myposresto.util.Recordset;
import com.talaga.myposresto.util.SqlAdapter;

import java.io.IOException;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.util.Date;

public class SalesActivity extends AppCompatActivity {

    ListView lstBarang;
    Button cmdCustomer,cmdCash,cmdCard,cmdPrint,cmdSave;
    TextView txtAmount,txtQty,txtNota,txtCst,txtTanggal;
    float mTotalAmount,mTotalQty;
    String mNota;
    BaseAdapter mItemAdapter;
    private final int ITEM_SELECTED=2,CUSTOMER_SELECTED=3,TABLE_SELECTED=4,WAITER_SELECTED=5;
    private final int CASH_PAYMENT=6,CARD_PAYMENT=7;
    private final String STATUS="status", OK="OK";
    private boolean mSaved=false;
    SharedPreferences mSetting = null;
    String _mode;

    private static BluetoothSocket btsocket;
    private static OutputStream btoutputstream;
    byte FONT_TYPE;
    RecyclerView lstItem;
    private GridLayoutManager gridLayoutManager;
    ImageView btnMinNota,btnRefresh;
    LinearLayout divItem;


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        // Checks the orientation of the screen
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            Toast.makeText(this, "landscape", Toast.LENGTH_SHORT).show();
            gridLayoutManager = new GridLayoutManager(getBaseContext(),4);
            lstItem.setLayoutManager(gridLayoutManager);
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
            Toast.makeText(this, "portrait", Toast.LENGTH_SHORT).show();
            gridLayoutManager = new GridLayoutManager(getBaseContext(),2);
            lstItem.setLayoutManager(gridLayoutManager);
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales);
        loadGoogleAds();
        //-- init variables
        mNota="$$$";
        mTotalQty=0;
        mTotalAmount=0;
        mSetting = getSharedPreferences(getResources().getString(R.string.setting), Context.MODE_PRIVATE);

        //-- init controls
        lstItem=findViewById(R.id.lstItem);
        lstItem.setHasFixedSize(true);
        gridLayoutManager = new GridLayoutManager(getBaseContext(),2);
        lstItem.setLayoutManager(gridLayoutManager);

        lstBarang=findViewById(R.id.lstBarang);
        lstBarang.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                return false;
            }
        });
        cmdCash = findViewById(R.id.cmdCash);
        cmdCash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txtAmount =  findViewById(R.id.txtAmount);
                txtQty =  findViewById(R.id.txtQty);
                txtNota = findViewById(R.id.txtNotaNo);

                Intent i = new Intent(getBaseContext(), SalesPaymentCash.class);
                i.putExtra("nota",txtNota.getText().toString());
                i.putExtra("amount",txtAmount.getText().toString());
                i.putExtra("qty",txtQty.getText().toString());

                startActivityForResult(i,CASH_PAYMENT);
            }
        });
        cmdCard = findViewById(R.id.cmdCard);
        cmdCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getBaseContext(), SalesPaymentCard.class);
                i.putExtra("nota",txtNota.getText().toString());
                i.putExtra("amount",txtAmount.getText().toString());
                i.putExtra("qty",txtQty.getText().toString());
                startActivityForResult(i,CARD_PAYMENT);
            }
        });
        cmdCustomer = findViewById(R.id.cmdCustomer);
        cmdCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getBaseContext(), CustomerSelectActivity.class);
                startActivityForResult(i,CUSTOMER_SELECTED);

            }
        });
        cmdSave=findViewById(R.id.cmdSave);
        cmdSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(saveOrder()){
                    Toast.makeText(getBaseContext(),"Order berhasil disimpan.",Toast.LENGTH_LONG).show();
                    finish();
                } else {
                    Toast.makeText(getBaseContext(),"Gagal simpan !",Toast.LENGTH_LONG).show();
                }
            }
        });
        cmdPrint=findViewById(R.id.cmdPrint);
        cmdPrint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(printOrder()){
                    Toast.makeText(getBaseContext(),"Order berhasil dicetak.",Toast.LENGTH_LONG).show();
                    finish();
                } else {
                    Toast.makeText(getBaseContext(),"Gagal cetak !",Toast.LENGTH_LONG).show();
                }
            }
        });
        txtAmount = findViewById(R.id.txtAmount);
        txtQty = findViewById(R.id.txtQty);
        txtNota = findViewById(R.id.txtNotaNo);
        txtCst=findViewById(R.id.lblCustomer);

        _mode="add";
        Bundle bundle = getIntent().getExtras();
        if(bundle!=null) {
            if (bundle.getString("mode") != null) {
                _mode = bundle.getString("mode");
                mNota = bundle.getString("nota");
            }
        }
        txtNota.setText(mNota);
        if(_mode.equals("edit")){
            editRecord();
        }
        divItem = findViewById(R.id.divItem);
        btnRefresh = findViewById(R.id.btnRefresh);
        btnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calcTotal();
                loadItemSale();
            }
        });

        //load item sales
        loadItemSale();
        calcTotal();
        loadItems();

    }
    void loadGoogleAds(){
        AdView mAdView =  findViewById(R.id.adView);
        AdRequest.Builder adBuilder = new AdRequest.Builder();
        adBuilder.addTestDevice("0E4C535F8F0639511F507B6977C9BB2B");
        AdRequest adRequest=adBuilder.build();
        mAdView.loadAd(adRequest);
    }

    private void loadItems(){
        ItemMasterAdapter mBaseAdapter=new ItemMasterAdapter(getBaseContext(),this);
        lstItem.setAdapter(mBaseAdapter);
    }
    private boolean printOrder(){

        try {
            btsocket = BTDeviceList.getSocket();
            if(btsocket != null){
                print_bt();
            }

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        /*
        if(btsocket == null){
            Intent BTIntent = new Intent(getApplicationContext(), BTDeviceList.class);
            this.startActivityForResult(BTIntent, BTDeviceList.REQUEST_CONNECT_BT);
        } else {

            OutputStream opstream = null;
            try {
                opstream = btsocket.getOutputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }
            btoutputstream = opstream;
            print_bt();

        }
        */
        return true;
    }
    private void print_bt(){
        try {
            /*
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            */

            String header1=mSetting.getString("header1","Header1");
            String header2=mSetting.getString("header2","Header2");
            String header3=mSetting.getString("header3","Header3");

            String footer1=mSetting.getString("footer1","Footer1");
            String footer2=mSetting.getString("footer2","Footer2");
            String footer3=mSetting.getString("footer3","Footer3");

            btoutputstream = btsocket.getOutputStream();

            byte[] printformat = { 0x1B, 0x21, FONT_TYPE };

            Recordset rSales=new Recordset(getBaseContext());
            rSales.OpenRecordset("select * from ksr_sales_header where invoice_no='"+mNota+"'",
                    "ksr_sales_header","invoice_no");
            String customer="CASH",invoice_date="",amount="",comments="";
            String item="";
            if(!rSales.eof()) {
                customer = rSales.getString("customer").toString();
                invoice_date = rSales.getString("invoice_date").toString();
                amount = rSales.getString("amount").toString();
                comments = rSales.getString("comments").toString();
            }
            Recordset rSalesItem=new Recordset(getBaseContext());
            rSalesItem.OpenRecordset("select * from ksr_sales_detail where invoice_no='"+mNota+"'",
                    "ksr_sales_detail","invoice_no");

            btoutputstream.write(printformat);
            btoutputstream.write((header1).getBytes());
            btoutputstream.write((header2).getBytes());
            btoutputstream.write((header3).getBytes());
            btoutputstream.write(("Nota#" + mNota).getBytes());
            btoutputstream.write(("Customer# "+customer).getBytes());
            btoutputstream.write(("Date "+invoice_date).getBytes());
            btoutputstream.write(("Amount "+amount).getBytes());

            while (!rSalesItem.eof()){
                item =rSalesItem.getString("qty").toString() + " " +
                        rSalesItem.getString("item_name").toString() + " Rp. " +
                        rSalesItem.getString("amount").toString();
                btoutputstream.write((item).getBytes());
            }

            btoutputstream.write((footer1).getBytes());
            btoutputstream.write((footer2).getBytes());
            btoutputstream.write((footer3).getBytes());
            btoutputstream.write(0x0D);
            btoutputstream.write(0x0D);
            btoutputstream.write(0x0D);
            btoutputstream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void editRecord(){
        Recordset rSales=new Recordset(getBaseContext());
        rSales.OpenRecordset("select * from ksr_sales_header where invoice_no='"+mNota+"'",
                "ksr_sales_header","invoice_no");
        if(!rSales.eof()) {
            txtCst.setText(rSales.getString("customer"));
            //txtTanggal.setText(rSales.getString("invoice_date"));
        }
    }
    private boolean saveOrder(){
        Helper db=new Helper(getBaseContext());
        Date currentDate = new Date(System.currentTimeMillis());
        String tgl=currentDate.toString();
        String cst=txtCst.getText().toString();

        if(mNota.equals("$$$")){

            mNota=nextNumber();

            db.Execute("update ksr_sales_detail set invoice_no='"+mNota+"' " +
                    "where invoice_no='$$$'");
        }
        addHeader(mNota, tgl,cst,mTotalAmount);

        calcTotal();
        return true;
    }
    private String nextNumber(){
        String sRet="";
        int nNo= Integer.parseInt(mSetting.getString("no_nota", "1"));
        sRet = "P"+ nNo;
        SharedPreferences.Editor editor = mSetting.edit();
        editor.putString("no_nota", String.valueOf(nNo+1));
        editor.commit();
        return sRet;
    }
    public void calcTotal(){
        Recordset rst=new Recordset(getBaseContext());
        rst.execute("select sum(amount) as zAmt, count(1) as zCnt " +
                "from ksr_sales_detail " +
                "where invoice_no='"+mNota+"'");
        mTotalAmount = rst.getFloat("zAmt");
        mTotalQty = rst.getInt("zCnt");
        //DecimalFormat df = new DecimalFormat("###,###.##"); // or pattern "###,###.##$"
        txtAmount.setText(Libs.formatRupiah(String.valueOf(mTotalAmount)));
        txtQty.setText("" + mTotalQty);
        String sCstNo=txtCst.getText().toString();
        Helper db=new Helper(getBaseContext());
        db.Execute("update ksr_sales_header set amount="+mTotalAmount +
                ",customer='"+sCstNo+"' where invoice_no='"+mNota+"'");

   }
    public void loadItemSale(){
        int[] controls = new int[]{R.id.nama_barang, R.id.barcode, R.id.harga,R.id.qty,R.id.amount,R.id.id};
        String sql="select k.item_name,k.barcode,k.price,k.qty,k.amount,k.id,m.picture " +
                        "from ksr_sales_detail k " +
                        "left join ksr_item_master m on m.barcode=k.barcode " +
                        "where invoice_no='" + mNota + "'";
        SqlAdapter sqlAdapter=new SqlAdapter(getBaseContext(),
                R.layout.row_sales_item,
                controls, sql);
        sqlAdapter.formatColumns("price", Global.FORMAT_NUMBER);
        sqlAdapter.formatColumns("amount",Global.FORMAT_NUMBER);
        sqlAdapter.show_button_min_plus(true);
        sqlAdapter.setParent(this);
        lstBarang.setAdapter(sqlAdapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if(data==null) return;
        // check if the request code is same as what is passed  here it is 2
        if(requestCode==CUSTOMER_SELECTED){
            String sCustNo="";
            if(data.hasExtra("CustNo")) sCustNo=data.getStringExtra("CustNo");
            TextView txtCust= findViewById(R.id.lblCustomer);
            txtCust.setText(sCustNo);
        }
        if(requestCode==ITEM_SELECTED)
        {
            String sItemNo="",sQty="1";
            if(data.hasExtra("ItemNo")) sItemNo=data.getStringExtra("ItemNo");
            if(data.hasExtra("Qty")) sQty=data.getStringExtra("Qty");
            addItem(sItemNo,sQty);
        }
        if(requestCode==CASH_PAYMENT){
            String status="";
            if(data!=null){
                if(data.hasExtra(STATUS)) status = data.getStringExtra(STATUS);
                    if(status.equals(OK)){
                        printOrder();
                        finish();
                    }
                }

        }
        if(requestCode==CARD_PAYMENT){
            String status="";
            if(data!=null) {
                if (data.hasExtra(STATUS)) status = data.getStringExtra(STATUS);
                if (status.equals(OK)) {
                    finish();
                }
            }
        }

    }
    public void addItem(String sItemNo,String sQty){
        if(sQty=="" || sQty=="0")sQty="1";
        Recordset rItem=new Recordset(getBaseContext());
        rItem.OpenRecordset("select * from ksr_item_master where barcode='"+sItemNo+"'",
                "ksr_item_master","barcode");
        if(rItem.eof()){
            Log.d("addItem","Item not found !! "+sItemNo);
            return ;
        }
        String sNama=rItem.getString("item_name");
        Toast.makeText(getBaseContext(),"Sukses item terpilih: "  + sNama,Toast.LENGTH_LONG).show();
        Recordset rSales=new Recordset(getBaseContext());
        rSales.OpenRecordset("select * from ksr_sales_detail where invoice_no='"+mNota+"'",
                "ksr_sales_detail","id");
        String sPrice=rItem.getString("price");
        Float nAmount= Float.valueOf(sPrice)*Float.valueOf(sQty);
        rSales.addNew();
        rSales.put("invoice_no",mNota);
        rSales.put("barcode", rItem.getString("barcode"));
        rSales.put("item_name",sNama);
        rSales.put("price",sPrice);
        rSales.put("cost",rItem.getString("cost"));
        rSales.put("qty",sQty);
        rSales.put("amount", String.valueOf(nAmount));
        rSales.put("unit",rItem.getString("unit"));
        rSales.put("discount","0");
        rSales.save();
        loadItemSale();
        calcTotal();
    }
    private void addHeader(String sNota,String sDate,String sCst,double nAmt){
        Recordset rSales=new Recordset(getBaseContext());
        rSales.OpenRecordset("select * from ksr_sales_header where invoice_no='"+sNota+"'",
                "ksr_sales_header","invoice_no");
        if(rSales.eof()) {
            rSales.addNew();
            rSales.put("invoice_no", sNota);
        }
        rSales.put("invoice_date",sDate);
        rSales.put("customer",sCst);
        rSales.put("amount", String.valueOf(nAmt));
        rSales.save();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            if(btsocket!= null){
                btoutputstream.close();
                btsocket.close();
                btsocket = null;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
