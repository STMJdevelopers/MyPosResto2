package com.talaga.myposresto.admin;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.talaga.myposresto.util.Recordset;
import com.talaga.myposresto.R;

/**
 * Created by andri on 03/02/2017.
 */

public class TablesInput extends AppCompatActivity {

    Recordset _item;
    String _mode,_kode;
    Button cmdDel,cmdSave,cmdClose;
    EditText txtTableNo,txtDescription,txtStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.table);
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
        txtTableNo= (EditText) findViewById(R.id.txtTableNo);
        txtDescription = (EditText) findViewById(R.id.txtDescription);
        txtStatus = (EditText) findViewById(R.id.txtStatus);

    }
    private void EditRecord(){
        _item= new Recordset(getBaseContext());
        _item.OpenRecordset("select * from ksr_tables where table_no='" + _kode + "'",
                "ksr_tables","table_no");
        txtTableNo.setText(_item.getString("table_no"));
        txtTableNo.setEnabled(false);
        txtDescription.setText(_item.getString("description"));
        txtStatus.setText(_item.getString("status"));
    }

    private boolean SaveRecord() {

        _item= new Recordset(getBaseContext());
        _item.OpenRecordset("select * from ksr_tables where table_no='" + txtTableNo.getText().toString()+ "'",
                "ksr_tables","table_no");
        if (_item.eof()) {
            _item.addNew();
            _item.put("table_no",txtTableNo.getText().toString());
        }
        _item.put("description",txtDescription.getText().toString());
        _item.put("status",txtStatus.getText().toString());
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
                        EditText txtId = (EditText) findViewById(R.id.txtTableNo);
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
