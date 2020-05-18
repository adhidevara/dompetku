package com.example.mye_wallet;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

public class DatabaseUser extends SQLiteOpenHelper {
    public static String DB_NAME = "dbuser"; //NAMA DATABASE
    public static int DB_VERSION = 2;
    private static DatabaseUser instance = null;

    //CREATE TABEL "USER"
    private String CREATE_USER_TABLE = "CREATE TABLE user(" +
            "_id_user INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "nama TEXT NOT NULL, " +
            "username TEXT NOT NULL, " +
            "noHp TEXT NOT NULL, " +
            "saldo INTEGER NOT NULL, " +
            "password TEXT NOT NULL);";

    //CREATE TABEL "TRANSAKSI"
    private String CREATE_TRANSAKSI_TABLE = "CREATE TABLE `transaksi` (" +
            "`_id_transaksi` INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "`_id_user` INTEGER NOT NULL, " +
            "`nominal` INTEGER NOT NULL, " +
            "`tgl_transaksi` DATETIME DEFAULT CURRENT_TIMESTAMP, " +
            "`tipe` TEXT NOT NULL, " +
            "`deskripsi` TEXT NOT NULL, " +
            "FOREIGN KEY(`_id_user`) REFERENCES `pegawai`(`_id_user`)" +
            ");";

    public static DatabaseUser getInstance(Context context) {
        if (instance == null) {
            instance = new DatabaseUser(context.getApplicationContext());
        }
        return instance;
    }

    private DatabaseUser(Context context) {
        super(context, DB_NAME,
                null, DB_VERSION);
    }

    public DatabaseUser(Context context, String name,
                           SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public DatabaseUser(Context context, String name,
                           SQLiteDatabase.CursorFactory factory, int version,
                           DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        Log.d("DB", "Membuat database (USER & TRANSAKSI.");
        sqLiteDatabase.execSQL(CREATE_USER_TABLE);
        sqLiteDatabase.execSQL(CREATE_TRANSAKSI_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i0, int i1) {
        Log.d("DB", "Mengupgrade database " + i0 + " ke " + i1);

        if (i1 == 2 && i0 == 1) {
            sqLiteDatabase.execSQL(CREATE_TRANSAKSI_TABLE);
        }
    }

    //UNTUK MENAMBAHKAN DATA "USER" KE DATABASE
    public void createUser(User user) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("nama", user.getNama());
        cv.put("username", user.getUsername());
        cv.put("noHp", user.getNoHp());
        cv.put("password", user.getPassword());
        cv.put("saldo", user.getSaldo());
        db.insert("user", null, cv);

        db.close();
    }

    //UNTUK MENAMBAHKAN DATA "TRANSAKSI" DI DATABASE
    public void createTransaction(Transaksi transaksi) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("_id_user", transaksi.getId_user());
        cv.put("nominal", transaksi.getNominal());
        cv.put("tipe", transaksi.getTipe());
        cv.put("deskripsi", transaksi.getDeskripsi());
        db.insert("transaksi", null, cv);

        db.close();
    }

    //UNTUK CEK DI DATABASE APAKAH ADA AKUN SESUAI "USERNAME" DAN "PASSWORD" YANG DI INPUTKAN (LOGIN)
    public List<User> selectAkun(String username, String password) {
        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT * FROM user WHERE username='"+username+"' AND password='"+password+"';";
        List<User> user = new ArrayList<>();
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                User user1 = new User();
                user1.setId_user(cursor.getInt(0));
                user1.setNama(cursor.getString(1));
                user1.setUsername(cursor.getString(2));
                user1.setNoHp(cursor.getString(3));
                user1.setSaldo(cursor.getInt(4));
                user1.setPassword(cursor.getString(5));
                user.add(user1);
            }
            while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return user;
    }

    //UNTUK CEK DI DATABASE APAKAH ADA AKUN SESUAI "USERNAME" YANG DI INPUTKAN (REGISTRASI)
    public List<User> cekAkun(String username) {
        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT * FROM user WHERE username='"+username+"';";
        List<User> user = new ArrayList<>();
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                User user1 = new User();
                user1.setId_user(cursor.getInt(0));
                user1.setNama(cursor.getString(1));
                user1.setUsername(cursor.getString(2));
                user1.setNoHp(cursor.getString(3));
                user1.setSaldo(cursor.getInt(4));
                user1.setPassword(cursor.getString(5));
                user.add(user1);
            }
            while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return user;
    }

    public List<Transaksi> selectTrans() {
        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT * FROM transaksi";
        List<Transaksi> t = new ArrayList<>();
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                Transaksi transaksi = new Transaksi();
                transaksi.setId_transaksi(cursor.getInt(0));
                transaksi.setId_user(cursor.getInt(1));
                transaksi.setNominal(cursor.getInt(2));
                transaksi.setTgl_transaksi(cursor.getString(3));
                transaksi.setTipe(cursor.getString(4));
                transaksi.setDeskripsi(cursor.getString(5));
                t.add(transaksi);
            }
            while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return t;
    }

    public List<Transaksi> selectTransWhere(int idUser) {
        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT * FROM transaksi WHERE _id_user ="+idUser+" ORDER BY _id_transaksi DESC";
        List<Transaksi> t = new ArrayList<>();
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                Transaksi transaksi = new Transaksi();
                transaksi.setId_transaksi(cursor.getInt(0));
                transaksi.setId_user(cursor.getInt(1));
                transaksi.setNominal(cursor.getInt(2));
                transaksi.setTgl_transaksi(cursor.getString(3));
                transaksi.setTipe(cursor.getString(4));
                transaksi.setDeskripsi(cursor.getString(5));
                t.add(transaksi);
            }
            while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return t;
    }

    public void updateUser(User user){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("nama", user.getNama());
        cv.put("noHp", user.getNoHp());
        db.update("user", cv, "_id_user="+user.getId_user(), null);
        db.close();
    }

    public void updateSaldo(User user){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("saldo", user.getSaldo());
        db.update("user", cv, "_id_user="+user.getId_user(), null);
        db.close();
    }
}
