package com.example.mye_wallet;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefUser {
    public static final String SP_USER_APP = "spUserApp";

    public static final String SP_ID_USER = "spIdUser";
    public static final String SP_NAMA = "spNama";
    public static final String SP_USERNAME = "spUsername";
    public static final String SP_NO_HP = "spNoHP";
    public static final String SP_PASSWORD = "spPassword";
    public static final String SP_SALDO = "spSaldo";
    public static final String SP_USERNAME_TUJUAN = "spUsernameTujuan";

    public static final String SP_SUDAH_LOGIN = "spSudahLogin";

    SharedPreferences sp;
    SharedPreferences.Editor spEditor;

    public SharedPrefUser(Context context){
        sp = context.getSharedPreferences(SP_USER_APP, Context.MODE_PRIVATE);
        spEditor = sp.edit();
    }

    public void saveSPString(String keySP, String value){
        spEditor.putString(keySP, value);
        spEditor.commit();
    }

    public void saveSPInt(String keySP, int value){
        spEditor.putInt(keySP, value);
        spEditor.commit();
    }

    public void saveSPBoolean(String keySP, boolean value){
        spEditor.putBoolean(keySP, value);
        spEditor.commit();
    }

    public int getSpIdUser(){
        return sp.getInt(SP_ID_USER, -1);
    }

    public String getSpNama(){
        return sp.getString(SP_NAMA, "");
    }

    public String getSPUsername(){
        return sp.getString(SP_USERNAME, "");
    }

    public String getSpNoHp(){
        return sp.getString(SP_NO_HP, "");
    }

    public String getSPPassword(){
        return sp.getString(SP_PASSWORD, "");
    }

    public int getSpSaldo(){
        return sp.getInt(SP_SALDO, -1);
    }

    public String getSpUsernameTujuan(){
        return sp.getString(SP_USERNAME_TUJUAN, "");
    }

    public Boolean getSPSudahLogin(){
        return sp.getBoolean(SP_SUDAH_LOGIN, false);
    }
}
