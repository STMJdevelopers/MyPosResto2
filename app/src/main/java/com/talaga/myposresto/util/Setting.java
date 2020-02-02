package com.talaga.myposresto.util;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import com.google.android.material.textfield.TextInputEditText;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import com.talaga.myposresto.R;


/**
 * Created by compaq on 03/20/2016.
 */
public class Setting extends AppCompatActivity {
    SharedPreferences mSetting = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting);
        mSetting = getSharedPreferences(getResources().getString(R.string.setting), Context.MODE_PRIVATE);

        Button btnCancel= (Button) findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        Button btnSave=(Button) findViewById(R.id.btnSave);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveSetting();
            }
        });

        final Switch blueOnOff= (Switch) findViewById(R.id.switchBlueTooth);
        blueOnOff.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // do something, the isChecked will be
                // true if the switch is in the On position
                if(isChecked){
                    selectBluetoothPrinter();
                }
            }
        });
        loadSetting();
    }
    void selectBluetoothPrinter(){
        Intent BTIntent = new Intent(getApplicationContext(), BTDeviceList.class);
        this.startActivityForResult(BTIntent, BTDeviceList.REQUEST_CONNECT_BT);

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==BTDeviceList.REQUEST_CONNECT_BT){

        }


    }
    void loadSetting(){
        TextInputEditText txt= (TextInputEditText) findViewById(R.id.header1t);
        txt.setText(mSetting.getString("header1","Header1"));
        txt= (TextInputEditText) findViewById(R.id.header2t);
        txt.setText(mSetting.getString("header2","Header2"));
        txt= (TextInputEditText) findViewById(R.id.header3t);
        txt.setText(mSetting.getString("header3","Header3"));
        txt= (TextInputEditText) findViewById(R.id.footer1t);
        txt.setText(mSetting.getString("footer1","Footer1"));
        txt= (TextInputEditText) findViewById(R.id.footer2t);
        txt.setText(mSetting.getString("footer2","Footer2"));
        txt= (TextInputEditText) findViewById(R.id.footer3t);
        txt.setText(mSetting.getString("footer3","Footer3"));
    }
    void saveSetting(){
        SharedPreferences.Editor editor = mSetting.edit();
        TextInputEditText txt= (TextInputEditText) findViewById(R.id.header1t);
        editor.putString("header1", txt.getText().toString());
        txt= (TextInputEditText) findViewById(R.id.header2t);
        editor.putString("header2", txt.getText().toString());
        txt= (TextInputEditText) findViewById(R.id.header3t);
        editor.putString("header3", txt.getText().toString());
        txt= (TextInputEditText) findViewById(R.id.footer1t);
        editor.putString("footer1", txt.getText().toString());
        txt= (TextInputEditText) findViewById(R.id.footer2t);
        editor.putString("footer2", txt.getText().toString());
        txt= (TextInputEditText) findViewById(R.id.footer3t);
        editor.putString("footer3", txt.getText().toString());
        editor.commit();
        finish();
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
    }


}
