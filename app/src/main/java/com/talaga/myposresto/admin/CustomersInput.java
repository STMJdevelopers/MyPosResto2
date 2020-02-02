package com.talaga.myposresto.admin;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.talaga.myposresto.util.Recordset;
import com.talaga.myposresto.R;

/**
 * Created by andri on 03/02/2017.
 */

public class CustomersInput extends AppCompatActivity {

    Recordset _item;
    String _mode,_kode;
    Button cmdDel,cmdSave,cmdClose;
    TextView txtCustomerNo,txtCompany,txtAddress,txtPhone,txtEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.customer);
        prepareControls();
        cmdDel.setVisibility(View.INVISIBLE);
        cmdDel.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                DeleteRecord();
            }
        });

        cmdSave.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                if(SaveRecord()) {
                    finish();
                }
            }
        });
        cmdClose.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                finish();
            }
        });
        _mode="add";
        Bundle bundle = getIntent().getExtras();
        if(bundle!=null) {
            if (bundle.getString("mode") != null) {
                _mode = bundle.getString("mode");
                _kode = bundle.getString("kode");
                cmdDel.setVisibility(View.VISIBLE);
                EditRecord();
            }
        }

    }
    private void prepareControls(){
         cmdDel= (Button) findViewById(R.id.cmdDelete);
         cmdSave=(Button) findViewById(R.id.cmdSave);
         cmdClose=(Button) findViewById(R.id.cmdClose);
         txtCustomerNo= (EditText) findViewById(R.id.txtCustomerNo);
         txtCompany = (EditText) findViewById(R.id.txtCompany);
         txtAddress = (EditText) findViewById(R.id.txtAddress);
         txtPhone = (EditText) findViewById(R.id.txtPhone);
         txtEmail = (EditText) findViewById(R.id.txtEmail);

    }
    private void EditRecord(){
        _item= new Recordset(getBaseContext());
        _item.OpenRecordset("select * from ksr_customers where customer_no='" + _kode + "'",
                "ksr_customers","customer_no");
        txtCustomerNo.setText(_item.getString("customer_no"));
        txtCustomerNo.setEnabled(false);
        txtCompany.setText(_item.getString("company"));
        txtAddress.setText(_item.getString("address"));
        txtPhone.setText(_item.getString("phone"));
        txtEmail.setText(_item.getString("email"));
    }

    private boolean SaveRecord() {

        _item= new Recordset(getBaseContext());
        _item.OpenRecordset("select * from ksr_customers where customer_no='" + txtCustomerNo.getText().toString()+ "'",
                "ksr_customers","customer_no");
        if (_item.eof())  {
            _item.addNew();
            _item.put("customer_no",txtCustomerNo.getText().toString());
        }
        _item.put("company",txtCompany.getText().toString());
        _item.put("address",txtAddress.getText().toString());
        _item.put("phone",txtPhone.getText().toString());
        _item.put("email",txtEmail.getText().toString());
        int id= _item.save();
        return id>-1;
    }
    private void DeleteRecord(){
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Delete")
                .setMessage("Are you sure you want to delete this item?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        EditText txtId = (EditText) findViewById(R.id.txtCustomerNo);
                        _item.delete(txtId.getText().toString());
                        finish();
                    }

                })
                .setNegativeButton("No", null)
                .show();
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}
