package com.talaga.myposresto.model;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.talaga.myposresto.R;

import com.talaga.myposresto.util.Helper;
import com.talaga.myposresto.util.Recordset;

import java.text.DecimalFormat;
import java.util.Date;

/**
 * Created by andri on 03/02/2017.
 */

public class SalesPaymentCash extends AppCompatActivity{
    TextView txtAmountPaid,txtAmount,txtKembali,txtQty;
    Button btn10,btn20,btn50,btn100;
    private final int CASH_PAYMENT=6;
    int mTotalQty=0;
    float mTotalAmount=0;
    String mNota="";
    SharedPreferences mSetting = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.payment_cash);
        mSetting = getSharedPreferences(getResources().getString(R.string.setting), Context.MODE_WORLD_READABLE);

        String sNota="";
        txtAmountPaid=(TextView)findViewById(R.id.txtAmountPaid);
        txtAmount=(TextView)findViewById(R.id.txtAmount);
        txtKembali=(TextView)findViewById(R.id.txtKembali);
        txtQty=(TextView)findViewById(R.id.txtQty);

        Bundle bundle = getIntent().getExtras();
        if(bundle!=null) {
            if (bundle.getString("nota") != null) {
                mNota =bundle.getString("nota");
            }
            if (bundle.getString("amount") != null) {
                String sAmount=bundle.getString("amount");
                sAmount=sAmount.replaceAll(",","");
                mTotalAmount = Float.parseFloat(sAmount);
            }
            if (bundle.getString("qty") != null) {
                String sQty=bundle.getString("qty");
                sQty=sQty.replaceAll(",","");

                mTotalQty = (int) Float.parseFloat(sQty);
            }
        } else {
            mNota="$$$";
        }
        if(mNota.equals("$$$")){
            Recordset rst=new Recordset(getBaseContext());
            rst.execute("select sum(amount) as zAmt, count(1) as zCnt " +
                    "from ksr_sales_detail " +
                    "where invoice_no='"+mNota+"'");
            mTotalAmount = rst.getFloat("zAmt");
            mTotalQty = rst.getInt("zCnt");
        }

        DecimalFormat df = new DecimalFormat("###,###.##"); // or pattern "###,###.##$"
        txtAmount.setText(df.format(mTotalAmount));
        txtQty.setText("" + mTotalQty);

        Button cmdCancel=(Button)findViewById(R.id.cmdCancel);
        cmdCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        Button cmdSubmit=(Button)findViewById(R.id.cmdSubmit);
        cmdSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                savePayment();
            }
        });

        btn10=(Button)findViewById(R.id.btn10);
        btn10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtAmountPaid.setText("10000");
                calcTotal();
            }
        });
        btn20=(Button)findViewById(R.id.btn20);
        btn20.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtAmountPaid.setText("20000");
                calcTotal();
            }
        });
        btn50=(Button)findViewById(R.id.btn50);
        btn50.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtAmountPaid.setText("50000");
                calcTotal();
            }
        });
        btn100=(Button)findViewById(R.id.btn100);
        btn100.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtAmountPaid.setText("100000");
                calcTotal();
            }
        });

    }
    private void calcTotal(){
        float nKembali=0,nAmountPaid=0;
        nAmountPaid= Float.parseFloat(txtAmountPaid.getText().toString());
        nKembali=nAmountPaid-mTotalAmount;
        DecimalFormat df = new DecimalFormat("###,###.##"); // or pattern "###,###.##$"
        txtKembali.setText(df.format(nKembali));
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

    private void savePayment(){
        calcTotal();
        double nKembali= Double.parseDouble(txtKembali.getText().toString().replace(",",""));
        if (nKembali<0){
            Toast.makeText(getBaseContext(),"Masih ada sisa pembayaran !",Toast.LENGTH_LONG).show();
            return;
        }
        double mAmount= Double.parseDouble(txtAmountPaid.getText().toString());
        Date currentDate = new Date(System.currentTimeMillis());
        String tgl=currentDate.toString();
        Helper db=new Helper(getBaseContext());
        if(mNota.equals("$$$")){
            mNota=nextNumber();
            db.Execute("update ksr_sales_header set invoice_no='"+mNota+"',amount="+mAmount+",paid=1 where invoice_no='$$$'");
            db.Execute("update ksr_sales_detail set invoice_no='"+mNota+"' where invoice_no='$$$'");
        } else {
            db.Execute("update ksr_sales_header set paid=1,amount="+mAmount+" where invoice_no='"+mNota+"'");
        }
        db.Execute("insert into ksr_sales_payment(invoice_no,how_paid,date_paid,amount) " +
                "values('"+mNota+"','Cash','"+tgl+"','"+mAmount+"')");

        Intent intent=new Intent();
        intent.putExtra("status","OK");
        setResult(CASH_PAYMENT,intent);
        Toast.makeText(getBaseContext(),"Sukses, silahkan refresh.",Toast.LENGTH_LONG).show();
        finish();
    }
    private String getNewNota(){
        Recordset db=new Recordset(getBaseContext());
        db.execute("select count(1) as cnt from ksr_sales_header");
        int no = db.getInt("cnt");
        return String.valueOf(no);
    }
}
