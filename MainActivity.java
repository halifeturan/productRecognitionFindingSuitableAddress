package com.example.sondeneme;

import android.Manifest;

import java.net.Authenticator;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.IOException;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {
    ImageButton button_capture, button_database;
    Button button_map;
   // TextView textview_data;
    String urunBulma;
    Bitmap bitmap;
    String marketAdres;
    List<Address> addresses = null;
    public static String asd=null;
    private static final int REQUEST_CAMERA_CODE = 100;
    FusedLocationProviderClient fusedLocationProviderClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button_capture = findViewById(R.id.button_capture);
        button_database = findViewById(R.id.button_database);
        button_map = findViewById(R.id.button_map);
        //textview_data = findViewById(R.id.text_data);





        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{
                    Manifest.permission.CAMERA
            }, REQUEST_CAMERA_CODE);

        }


        button_capture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //String gelenYazi= veritabani.VeriGetir(stringBuilder.toString(),Integer.parseInt(urunFiyati.toString()));

                CropImage.activity().setGuidelines(CropImageView.Guidelines.ON).start(MainActivity.this);


            }
        });
        button_database.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), UrunEkle.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result;
            result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), resultUri);
                    getTextFromImage(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void getTextFromImage(Bitmap bitmap) {

        TextRecognizer recognizer = new TextRecognizer.Builder(this).build();
        if (!recognizer.isOperational()) {
            Toast.makeText(MainActivity.this, "Hata", Toast.LENGTH_SHORT);
        } else {
            Frame frame = new Frame.Builder().setBitmap(bitmap).build();
            SparseArray<TextBlock> textBlockSparseArray = recognizer.detect(frame);
            StringBuilder stringBuilder = new StringBuilder();
            for (int i = 0; i < textBlockSparseArray.size(); i++) {
                TextBlock textBlock = textBlockSparseArray.valueAt(i);
                stringBuilder.append(textBlock.getValue());
                stringBuilder.append("\n");

            }

            VeriTabani veriTabani = new VeriTabani(MainActivity.this);

            String urunAdi = stringBuilder.toString().substring(0, stringBuilder.indexOf("-"));
            String urunFiyati = stringBuilder.toString().substring(urunAdi.length() + 1, stringBuilder.indexOf("TL"));
            String marketAdi = stringBuilder.toString().substring(urunAdi.length() + urunFiyati.length() + 8, stringBuilder.indexOf("-M"));


            switch (marketAdi) {
                case "1":
                    marketAdi="A101";
                   // textview_data.setText(urunAdi + " " + urunFiyati.toString() + " " + marketAdi + " " + marketAdres);
                    break;
                case "2":
                    marketAdi="Bim";
                   // textview_data.setText(urunAdi + " " + urunFiyati.toString() + " " + marketAdi + " " + marketAdres);
                    break;
                case "3":
                    marketAdi="Şok";
                   // textview_data.setText(urunAdi + " " + urunFiyati.toString() + " " + marketAdi + " " + marketAdres);
                    break;
                case "4":
                    marketAdi="Migros";
                   // textview_data.setText(urunAdi + " " + urunFiyati.toString() + " " + marketAdi + " " + marketAdres);
                    break;
                case "5":
                    marketAdi="Akyurt";
                 //   textview_data.setText(urunAdi + " " + urunFiyati.toString() + " " + marketAdi + " " + marketAdres);
                    break;
                case "6":
                    marketAdi="Altunbilekler";
                   // textview_data.setText(urunAdi + " " + urunFiyati.toString() + " " + marketAdi + " " + marketAdres);
                    break;
                case "7":
                    marketAdi="Bildirici";
                   // textview_data.setText(urunAdi + " " + urunFiyati.toString() + " " + marketAdi + " " + marketAdres);
                    break;
                default: //textview_data.setText(urunAdi + " " + urunFiyati.toString() + " " + "Market Verisi Bulunamadı." + " " + marketAdres);
                break;

            }

            if (ActivityCompat.checkSelfPermission(MainActivity.this,Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED)
            {
                fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(MainActivity.this);
                marketAdres=getLocation();
                // textview_data.setText(marketAdres);
            }
            else
            {
                ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},44);
            }
            String karsilastirmaYazisi = veriTabani.VeriKarsilastir(urunAdi, Integer.parseInt(urunFiyati));
            if (karsilastirmaYazisi.equals("yesil"))
            {

                button_map.setVisibility(View.VISIBLE);
                button_map.setText("Okutulan ürün civardaki marketlerin arasından en uygun fiyatlısı.");
                //textview_data.setBackgroundColor(Color.GREEN);
                //textview_data.setTextColor(Color.WHITE);
            }
            else if (karsilastirmaYazisi.equals("kirmizi"))//ürün fiyatından daha ucuza ürün bulundu
            {

                //textview_data.setBackgroundColor(Color.RED);
                //textview_data.setTextColor(Color.BLACK);
                button_map.setVisibility(View.VISIBLE);
                button_map.setText("Okutulan ürün daha uygun fiyata başka bir markette bulundu, adresi görmek için tıklayın.");
                GoogleSignInOptions gso=new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
                GoogleApiClient mGoogleApiClient=new GoogleApiClient.Builder(this).enableAutoManage(MainActivity.this,this).addApi(Auth.GOOGLE_SIGN_IN_API,gso).build();
                button_map.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        VeriTabani veriTabani = new VeriTabani(MainActivity.this);
                        Uri gmmIntentUri=Uri.parse("geo:39.923130,32.851070?q="+veriTabani.ucuzAdres(urunAdi,Integer.parseInt(urunFiyati))+"");
                        Intent mapIntent = new Intent(Intent.ACTION_VIEW,gmmIntentUri);
                        mapIntent.setPackage("com.google.android.apps.maps");
                        startActivity(mapIntent);
                    }
                });

            }
            else
            {
              // textview_data.setBackgroundColor(Color.MAGENTA);
              // textview_data.setTextColor(Color.BLACK);
            }


            //market adı ve adresi gelecek buraya
            veriTabani.VeriEkle(urunAdi, Integer.parseInt(urunFiyati), marketAdi, marketAdres);

            // veriTabani.VeriEkle("urunAdi", 123123, marketAdi, marketAdres);

        }
    }

    @SuppressLint("MissingPermission")
    private String getLocation() {
        fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                Location location =task.getResult();
                if(location!=null)
                {
                    try {
                        Geocoder geocoder=new Geocoder(MainActivity.this, Locale.getDefault());
                        addresses=geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);
                        asd = addresses.get(0).getAddressLine(0);
                       // textview_data.setText(asd);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

                   return asd;
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}