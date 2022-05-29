package com.example.sondeneme;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class VeriTabani extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "db_urunler";
    private static final int DATABASE_VERSION = 1;
    private static final String URUNLER_TABLE = "t_urunler";

    public static final String ROW_ID = "id";
    public static final String ROW_NAME = "urun_adi";
    public static final String ROW_PRICE = "urun_fiyati";
    public static final String ROW_SHOP_NAME="market_adi";
    public static final String ROW_SHOP_INFO="market_adresi";


    public VeriTabani(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(" CREATE TABLE " + URUNLER_TABLE + "(" + ROW_ID + " INTEGER PRIMARY KEY," + ROW_NAME + " TEXT NOT NULL," + ROW_PRICE + " INTEGER NOT NULL,"+ROW_SHOP_NAME+" TEXT NOT NULL,"+ROW_SHOP_INFO+" TEXT NOT NULL)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL(" DROP TABLE IF EXISTS " + URUNLER_TABLE);
    }

    public void VeriEkle(String urun_adi, int urun_fiyati,String market_adi, String market_adresi) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(ROW_NAME, urun_adi.trim());
        cv.put(ROW_PRICE, urun_fiyati);
        cv.put(ROW_SHOP_NAME, market_adi);
        cv.put(ROW_SHOP_INFO, market_adresi);
        db.insert(URUNLER_TABLE, null, cv);
        db.close();
    }

    public String VeriGetir(String urun_adi, int urun_fiyati) {

        List<Object> veriler = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String[] sutunlar = {ROW_ID, ROW_NAME, ROW_PRICE,ROW_SHOP_NAME};
        Cursor cursor = db.rawQuery("select * from t_urunler where urun_adi='" + urun_adi + "' and urun_fiyati <'" + urun_fiyati + "' ", null);

        while (cursor.moveToNext()) {
            if (urun_adi == cursor.getString(1) && urun_fiyati < cursor.getInt(2)) {
                return "Yeşil";
            } else {
                return "Kırmızı";
                // veriler.add("Urun Adi: " + cursor.getString(1));
                //veriler.add("Urun Fiyati: " + cursor.getInt(2));
            }
        }
        return "Boş";
    }

    public String VeriKarsilastir(String urunAdi, int urunFiyati) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from t_urunler ", null);
        String yesilkirmizi = "";
        String urunadi = "";
        int urunfiyati = 0;

        while (cursor.moveToNext()) {
            urunadi = cursor.getString(1);
            urunfiyati = cursor.getInt(2);
        }
        if (urunAdi.equals(urunadi) && urunFiyati <= urunfiyati) {
            yesilkirmizi = "yesil";
        }
        if (urunAdi.equals(urunadi) && urunFiyati >= urunfiyati) {
            yesilkirmizi = "kirmizi";
        }
        if (!urunAdi.equals(urunadi)) {
            yesilkirmizi = "hata";
        }
        return yesilkirmizi;


    }
    public String ucuzAdres(String urunAdi, int urunFiyati) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from t_urunler ", null);
        String urunAdresi = "";
        String urunadi = "";
        int urunfiyati = 0;

        while (cursor.moveToNext()) {
            urunadi = cursor.getString(1);
            urunfiyati = cursor.getInt(2);
            urunAdresi =  cursor.getString(4);
        }
        if (urunAdi.equals(urunadi) && urunFiyati <= urunfiyati) {
            urunAdresi=urunAdresi;
        }
       else
        {

        }
         return urunAdresi;

    }

    public List<Object> VeriListele() {

        List<Object> veriler = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String[] sutunlar = {ROW_ID, ROW_NAME, ROW_PRICE,ROW_SHOP_NAME,ROW_SHOP_INFO};
        Cursor cursor = db.rawQuery("select * from t_urunler ", null);

        while (cursor.moveToNext()) {

            veriler.add("Urun Adi: " + cursor.getString(1));
            veriler.add("Urun Fiyati: " + cursor.getInt(2));
            veriler.add("Market Adi: " + cursor.getString(3));
            veriler.add("Marketin Adresi: " + cursor.getString(4));
        }
        return veriler;
    }
}
