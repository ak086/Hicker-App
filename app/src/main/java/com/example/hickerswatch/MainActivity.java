package com.example.hickerswatch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    LocationManager locationManager;
    LocationListener locationListener;



    public void startListening(){
        if(ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
        }
    }


    public void updateLocationInfo(Location location){
        TextView latView=findViewById(R.id.textView1);
        TextView lonView=findViewById(R.id.textView2);
        TextView altView=findViewById(R.id.textView3);
        TextView accView=findViewById(R.id.textView4);
        TextView addressView=findViewById(R.id.textView5);

        latView.setText("Latitude :-" +Double.toString(location.getLatitude()));
        lonView.setText("Longitude :- "+Double.toString(location.getLongitude()));
        altView.setText("Altitude :- "+location.getAltitude());
        accView.setText("Accuracy :- "+location.getAccuracy());

        String address="Could not found";
        Geocoder geocoder=new Geocoder(this, Locale.getDefault());

        try {
            List<Address> addressList=geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);
            if(addressList!=null && addressList.size()>0){
                address="Address :- \n";
                if(addressList.get(0).getLocality()!=null){
                    address+=addressList.get(0).getLocality()+"\n";
                }
                if(addressList.get(0).getAdminArea()!=null){
                    address+=addressList.get(0).getAdminArea();
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        addressView.setText(address);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
           startListening();
        }
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        locationManager=(LocationManager)this.getSystemService(Context.LOCATION_SERVICE);
        locationListener=new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                //Log.i("Location",location.toString());
                updateLocationInfo(location);

            }
        };
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
        }
        else{
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,1,locationListener);
            Location lastknownLocation=locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if(lastknownLocation!=null ){
                updateLocationInfo(lastknownLocation);
            }
        }
    }

}