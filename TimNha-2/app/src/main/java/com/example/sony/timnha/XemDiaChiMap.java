package com.example.sony.timnha;

import android.location.Address;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.location.Geocoder;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.CameraUpdate;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.google.android.maps.GeoPoint;


public class XemDiaChiMap extends AppCompatActivity implements OnMapReadyCallback {
    String diachiS;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xem_dia_chi_map);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Địa Chỉ Nhà  ");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        diachiS = getIntent().getStringExtra("diachi");
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }
    public GeoPoint getLocationFromAddress(String strAddress) throws IOException {

        Geocoder coder = new Geocoder(this);
        List<Address> address;
        GeoPoint p1 = null;
            address = coder.getFromLocationName(strAddress,5);
            if (address.size()==0) {
                return null;
            }else {
                Address location = address.get(0);
                location.getLatitude();
                location.getLongitude();

                p1 = new GeoPoint((int) (location.getLatitude() * 1E6),
                        (int) (location.getLongitude() * 1E6));
            }

            return p1;

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        GeoPoint diachi = null;
        try {
            diachi = getLocationFromAddress(diachiS);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(diachi != null) {
            LatLng sydney = new LatLng(diachi.getLatitudeE6() / 1E6, diachi.getLongitudeE6() / 1E6);
            googleMap.addMarker(new MarkerOptions().position(sydney).title("Vi Tri Cua Ban"));
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 17));
        }else{
            Toast.makeText(this,"Địa Chỉ Không Đúng",Toast.LENGTH_LONG).show();
        }
    }
}
