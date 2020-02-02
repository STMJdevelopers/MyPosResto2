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

public class CategoryInput  extends AppCompatActivity {

    Recordset _item;
    String _mode,_category;
    Button cmdDel,cmdSave,cmdClose;
    EditText category,description,picture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.category);
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
        Button cmdClose=(Button) findViewById(R.id.cmdClose);
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
                _category = bundle.getString("category");
                cmdDel.setVisibility(View.VISIBLE);
                EditRecord();
            }
        }
    }
    private void EditRecord(){
        _item= new Recordset(getBaseContext());
        _item.OpenRecordset("select * from ksr_category where category='" + _category + "'",
                "ksr_category","category");
        category.setText(_item.getString("category"));
        category.setEnabled(false);
        description.setText(_item.getString("description"));
        picture.setText(_item.getString("picture"));
    }

    private void prepareControls(){

        cmdDel= (Button) findViewById(R.id.cmdDelete);
        cmdSave=(Button) findViewById(R.id.cmdSave);
        category= (EditText) findViewById(R.id.txtCategory);
        description = (EditText) findViewById(R.id.txtDescription);
        picture = (EditText) findViewById(R.id.txtPicture);


    }
    private boolean SaveRecord() {
        _item= new Recordset(getBaseContext());
        _item.OpenRecordset("select * from ksr_category where category='" + category.getText().toString()+ "'",
                "ksr_category","category");
        if (_item.eof()) {
            _item.addNew();
            _item.put("category",category.getText().toString());
        }
        _item.put("description",description.getText().toString());
        _item.put("picture",picture.getText().toString());
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
                        EditText category = (EditText) findViewById(R.id.txtCategory);
                        _item.delete(category.getText().toString());
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
