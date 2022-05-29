package com.example.sondeneme;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class UrunEkle extends AppCompatActivity {
public int sayac=1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_urun_ekle);

        final EditText urun_adi=(EditText) findViewById(R.id.urun_adi);
        final EditText urun_fiyati=(EditText) findViewById(R.id.urun_fiyati);
        final EditText market_adi=(EditText) findViewById(R.id.market_adi);
        final EditText market_adresi=(EditText) findViewById(R.id.market_adresi);
       // final EditText urun_adi=(EditText) findViewById(R.id.urun_adi);
        //final EditText urun_fiyati=(EditText) findViewById(R.id.urun_fiyati);

        Button button=(Button)findViewById(R.id.ekle);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                VeriTabani veriTabani=new VeriTabani(UrunEkle.this);
                veriTabani.VeriEkle(urun_adi.getText().toString(),Integer.parseInt(urun_fiyati.getText().toString()),market_adi.getText().toString(),market_adresi.getText().toString());
            }
        });
        Button listele=(Button)findViewById(R.id.listele);
        ListView listView=(ListView) findViewById(R.id.ListView);
        // List<String> karsilastir=new ArrayList<>();

        //karsilastir.add("Seker");//fotoğraftan gelen text buraya yazılacak, ürün adı çay ise veri tabanındaki bütün çaylar gelecek
        listele.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                VeriTabani veritabani =new VeriTabani(UrunEkle.this);
                    sayac++;
                    List<Object> veriler = veritabani.VeriListele();//1. parametre ürün ismi olacak, 2. parametre ürün fiyatı olacak

                ArrayAdapter<Object> adapter =new ArrayAdapter<Object>(UrunEkle.this, android.R.layout.simple_list_item_1,android.R.id.text1,veriler);
                    if(sayac%2==0)
                    {
                        listView.setVisibility(View.VISIBLE);
                        listView.setAdapter(adapter);
                    }
                    else
                    {
                        listView.setVisibility(View.INVISIBLE);
                    }

                }


        });
    }
}