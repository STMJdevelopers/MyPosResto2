package com.talaga.myposresto.util;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.talaga.myposresto.R;
import com.talaga.myposresto.SalesActivity;


import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static com.talaga.myposresto.util.Global.NAMA_DB;
import static com.talaga.myposresto.util.Global.VERSI_DB;

/**
 * Created by andri on 03/02/2017.
 */

public class SqlAdapter extends BaseAdapter {
    public boolean showColumnTitle=false;
    public int[] colsWidth=null;
    private SQLiteDatabase _db;
    private LayoutInflater _layoutInflater;
    private String[] _fields;
    private Cursor _cursor;
    private Context _context;
    private ArrayList<TextView> _arTextView=null;
    private int _row_layout;
    private ArrayList<Object> _data=null;
    private int[] _controls;
    private String _sql;
    private ArrayList<FormatColumn> _format_columns=null;
    private boolean _show_image=true,_show_button_min_plus;

    public void show_image(boolean v){
        _show_image=v;
    }

    public void show_button_min_plus(boolean v){
        _show_button_min_plus=v;
    }

    SalesActivity _parent;
    public void setParent(SalesActivity parent){
        _parent=parent;
    }

    public SqlAdapter(Context aContext, int layout, int[] controls, String sql) {
        _context=aContext;
        _layoutInflater = LayoutInflater.from(_context);
        _row_layout=layout;
        _controls=controls;
        OpenConnection();
        OpenRecordset(sql);
    }

    public SqlAdapter(Context aContext, int layout, int[] controls) {
        _context=aContext;
        _layoutInflater = LayoutInflater.from(_context);
        _row_layout=layout;
        _controls=controls;
        OpenConnection();
    }


    public void OpenConnection(){
        Helper dbHelper = new Helper(this._context, NAMA_DB, null, VERSI_DB);
        try {
            _db = dbHelper.getWritableDatabase();
        }
        catch (SQLiteException e) {
            _db = dbHelper.getReadableDatabase();
        }
    }
    public ArrayAdapter<String> FillSpinner(String field){
        _cursor = _db.rawQuery(this._sql,null);

        String data[] = new String[_cursor.getCount()];
        int i = 0;
        if (_cursor.getCount() > 0)
        {
            _cursor.moveToFirst();
            do {
                data[i] = _cursor.getString(_cursor.getColumnIndex(field));
                i++;
            } while (_cursor.moveToNext());
            _cursor.close();
        }

        // inisialiasi Array Adapter dengan memasukkan string array di atas
        final ArrayAdapter<String> adapter = new ArrayAdapter<>(this._context,
                android.R.layout.simple_spinner_item, data);

        return adapter;

    }
    public  SqlAdapter(Context baseContext, String sql) {
        this._context=baseContext;
        this._sql=sql;
        OpenConnection();
    }
    public boolean OpenRecordset(String sql){
        _cursor = _db.rawQuery(sql,null);
        _fields=_cursor.getColumnNames();

        if(_fields == null) return false;

        return true;
    }
    @Override
    public int getCount() {
        return _cursor.getCount();
    }

    @Override
    public Object getItem(int position) {
        _cursor.moveToPosition(position);
        return _cursor;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder=null;
        if(this._format_columns==null){
            holder=new ViewHolder();
        } else {
            holder=new ViewHolder(this._format_columns);
        }
        if (convertView == null) {
            convertView = _layoutInflater.inflate(_row_layout, null);
            for(int i=0;i<_controls.length;i++){
                TextView id=convertView.findViewById(_controls[i]);
                if(colsWidth!=null){
                    int nWidth=colsWidth[i];
                    if (nWidth>1){
                        id.setWidth(nWidth);
                    }
                }
                holder.add(id);
            }
            if(_show_image){
                holder.img = convertView.findViewById(R.id.gambar);
            }
            if(_show_button_min_plus){
                holder.btnMin=convertView.findViewById(R.id.btnMin);
                holder.btnPlus=convertView.findViewById(R.id.btnPlus);
                holder.btnDel = convertView.findViewById(R.id.btnDel);
            }
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        _cursor.moveToPosition(position);

        String fieldName="",value="";

        for(int i=0;i<_fields.length;i++){
            fieldName=_fields[i];
            holder.setText(fieldName);
        }
        if(_show_button_min_plus){
            final ViewHolder finalHolder = holder;
            holder.btnMin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    updateMin(finalHolder);
                }
            });
            holder.btnPlus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    updatePlus(finalHolder);
                }
            });
            holder.btnDel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    delItem(finalHolder);
                }
            });

        }

        return convertView;
    }
    void delItem(ViewHolder holder){
        TextView id=holder.get(5);
        int nId= Integer.parseInt(id.getText().toString());
        Recordset rItem=new Recordset(_context);
        String s="delete from ksr_sales_detail where id="+nId;
        rItem.execute(s);
        Libs.messageBox(_context,"Baris sudah dihapus silahkan di refresh");
        _parent.calcTotal();
        _parent.loadItemSale();
    }
    void updatePlus(ViewHolder holder) {
        TextView qty=holder.get(3);
        TextView price=holder.get(2);
        TextView amount=holder.get(4);
        TextView id=holder.get(5);
        int nQty = Integer.parseInt(qty.getText().toString());
        int nId= Integer.parseInt(id.getText().toString());
        String sPrice=price.getText().toString();
        double nPrice = Libs.c_(price.getText().toString());
        nQty++;
        qty.setText( String.valueOf(nQty));
        double nAmount=nQty*nPrice;
        amount.setText(String.valueOf(nAmount));
        updateItem(nId, nQty,nPrice,nAmount);


    }
    void updateMin(ViewHolder holder){
        TextView qty=holder.get(3);
        TextView price=holder.get(2);
        TextView amount=holder.get(4);
        TextView id=holder.get(5);
        int nQty = Integer.parseInt(qty.getText().toString());
        int nId= Integer.parseInt(id.getText().toString());
        double nPrice = Libs.c_(price.getText().toString());
        if(nQty>1) nQty--;
        qty.setText( String.valueOf(nQty));
        double nAmount=nQty*nPrice;
        amount.setText(String.valueOf(nAmount));
        updateItem(nId, nQty,nPrice,nAmount);
    }
    public void updateItem(int id,int qty,double price,double amount){
        if(id==0)return;
        if(qty==0)return;
        Recordset rItem=new Recordset(_context);
        String s="update ksr_sales_detail set qty="+qty+",price="+price+",amount="+amount+" where id="+id;
        rItem.execute(s);
        Libs.messageBox(_context,"Baris sudah diupdate, silahkan direfresh.");
        _parent.calcTotal();
        _parent.loadItemSale();

    }

    public void setSql(String sql) {
        this._sql = sql;
    }

    public String getSql() {
        return _sql;
    }

    public void formatColumns(String sField, String sFormat) {
        if(this._format_columns==null){
            this._format_columns=new ArrayList<>();
        }
        this._format_columns.add(new FormatColumn(sField,sFormat));
    }

    class ViewHolder {
        private ArrayList<TextView> txt=new ArrayList<>();
        private ArrayList<FormatColumn> _format_columns=null;
        ImageView img=null;
        Button btnMin,btnPlus,btnDel;

        public ViewHolder(ArrayList<FormatColumn> format_columns) {
            this._format_columns=format_columns;
        }

        public ViewHolder() {

        }

        private void add(TextView id){
            txt.add( id);
        }
        private TextView get(int id){
            return txt.get(id);
        }

        private void setText(String fieldName){
            int idx=_cursor.getColumnIndex(fieldName);
            String value=_cursor.getString(idx);
            boolean alignRight=false;

            if(this._format_columns!=null){
                for(int j=0;j<this._format_columns.size();j++){

                    FormatColumn fc=this._format_columns.get(j);

                    if ( fc._field.equals(fieldName)){
                        if(fc._format.equals("date")){
                            SimpleDateFormat dateFormat = new SimpleDateFormat(Global.FORMAT_DATE);
                            value= dateFormat.format(new Date(value)).toString();
                        } else {
                            //DecimalFormat df = new DecimalFormat(fc._format); // or pattern "###,###.##$"
                            if(value==null)value="0";
                            if(value.isEmpty())value="0";
                            //value=df.format(Double.valueOf(value));
                            value=Libs.formatRupiah(value);
                            alignRight=true;

                        }
                    }
                }
            }
            if(fieldName.equals("picture")){
                if(value!=""){
                    FileInputStream streamIn = null;
                    try {
                        streamIn = new FileInputStream(value);
                        Bitmap bitmap = BitmapFactory.decodeStream(streamIn);
                        img.setImageBitmap(bitmap);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }

                }

            } else {
                txt.get(idx).setText(value);
                if(alignRight)txt.get(idx).setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
            }
            if(img==null){
                //img.setImageResource(R.drawable.nopic);
            }
        }
    }
    private class FormatColumn {
        private String _field="", _format="";
        public FormatColumn(String sField, String sFormat) {
            this._field=sField;
            this._format=sFormat;
        }
    }
}