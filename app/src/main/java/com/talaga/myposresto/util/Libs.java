package com.talaga.myposresto.util;

import android.content.Context;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Locale;

public class Libs {
    public static String formatRupiah(String sVal){
        DecimalFormat df = new DecimalFormat(Global.FORMAT_NUMBER);
        String sRetval= String.valueOf(sVal);
        if (sRetval==null)sRetval="0";
        Double nRetval= Double.parseDouble(sRetval);
        sRetval=df.format(nRetval);
        return  sRetval;

        //Locale localeID = new Locale("in", "ID");
        //NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);
        //sRetval = formatRupiah.format(nRetval);
    }
    public static double c_(String sVal){
        //Locale localeID = new Locale("in", "ID");
        //DecimalFormatSymbols symbols = new DecimalFormatSymbols(localeID);

        String sRet=sVal;
        if(sRet==null)sRet="0";
        if(sRet=="")sRet="0";
        sRet  = sRet.replace("Rp.","");
//        if('.' == symbols.getDecimalSeparator()) {
//            sRet = sRet.replace(",", "");
//        } else {
            sRet = sRet.replace(",", "");
//        }
        return Double.parseDouble(sRet);
    }

    public static void messageBox(Context context, String text) {
        Toast.makeText(context,text, Toast.LENGTH_LONG).show();
    }
}
