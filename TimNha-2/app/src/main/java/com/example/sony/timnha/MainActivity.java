package com.example.sony.timnha;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.maps.GeoPoint;
import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import org.w3c.dom.Text;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import model.ModelChiTietNhaTro;

import static com.example.sony.timnha.R.id.nav_Add;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener , OnMapReadyCallback,LocationListener,GoogleMap.OnInfoWindowClickListener,GoogleMap.OnMarkerClickListener {
        boolean checkDangNhap;
        String username;
    double viDoHienTai;
    double kinhdoHienTai;
    TextView txtUser,email;
    String[] ARRvung = {"TP Hồ Chí Minh"};
    String[] ARRKhuVuc = {"Quận 1", "Quận 2","Quận 3","Quận 4","Quận 5","Quận 6","Quận 7","Quận 8","Quận 9","Quận 10","Quận 11","Quận 12","Quận Tân Bình", "Quận Phú Nhuận","Quận Gò Vap "};
    SharedPreferences pref ;
    SharedPreferences.Editor editor;
    GoogleMap Map;
    LocationManager locationManager;
    String provider;
    HashMap<String, String> markerMap  = new HashMap<String, String>();
    SupportMapFragment mapFragment;
    ArrayList<ModelChiTietNhaTro> Allnhatro = new ArrayList<ModelChiTietNhaTro>();
    ImageButton imgUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        txtUser = (TextView)findViewById(R.id.BARusername);
        email = (TextView)findViewById(R.id.BAREmail);
        Allnhatro = new ArrayList<ModelChiTietNhaTro>();
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapMain);
        Map = mapFragment.getMap();
        Map.setMyLocationEnabled(true);
        locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        imgUser = (ImageButton)findViewById(R.id.mainUser);
        //Cài đặt điều kiện lấy Location Provider
        Criteria criteria = new Criteria();
        provider = locationManager.getBestProvider(criteria, false);
        Location location = locationManager.getLastKnownLocation(provider);

        //Khởi tạo location fields
        if (location !=null){
            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
            Map.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            Map.animateCamera(CameraUpdateFactory.zoomTo(20));
        }
        else {

        }
        pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        editor = pref.edit();
        editor.putBoolean("isCheckStart",false);
        editor.commit();
        Allnhatro = new ArrayList<ModelChiTietNhaTro>();
        markerMap = new HashMap<String, String>();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        Parse.enableLocalDatastore(this);
        Parse.initialize(this, "baLEjE7EkM9lsBTsPkvkmGOaZNwmhjVGhSNQw2tt", "4UqCg8aIcyWiobEMvfArqFBc8AhvGFRbatcWTZme");
        getInn(this);
        imgUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Login();
            }
        });





    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;

        }

        return super.onOptionsItemSelected(item);
    }
    @SuppressWarnings("StatementWithEmptyBody")

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        /*if (id == nav_Login) {

        } else*/ if (id == R.id.nav_Home) {

        } else if (id == R.id.nav_Add) {
                AddNhaTro();
        } else if (id == R.id.nav_Search) {
            search(this);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    public void search(Activity activity){
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_search);

        final EditText txt1 = (EditText)dialog.findViewById(R.id.edtTim);
        Button bnt = (Button)dialog.findViewById(R.id.dialogTim);
        final Spinner vung = (Spinner)dialog.findViewById(R.id.dialogVung);
        final Spinner khuvuc = (Spinner)dialog.findViewById(R.id.dialogKhuVuc);
        ArrayAdapter<String> adtVung = new ArrayAdapter<String>(this, android.support.v7.appcompat.R.layout.support_simple_spinner_dropdown_item,ARRvung);
        vung.setAdapter(adtVung);
        ArrayAdapter<String> adtKhu = new ArrayAdapter<String>(this, android.support.v7.appcompat.R.layout.support_simple_spinner_dropdown_item,ARRKhuVuc);
        khuvuc.setAdapter(adtKhu);
        bnt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String diachis = "";
                if(txt1.length()>0){
                    diachis = txt1.getText().toString();
                }
                GeoPoint p = null;
                try {
                   p =getLocationFromAddress(diachis +", "+ khuvuc.getSelectedItem() +", " + vung.getSelectedItem()+", " + "Việt Nam");

                } catch (IOException e) {
                    e.printStackTrace();
                }
                if(p != null){
                    LatLng sydney = new LatLng(p.getLatitudeE6() / 1E6, p.getLongitudeE6() / 1E6);
                    Map.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 17));
                    dialog.dismiss();
                }
            }
        });
        dialog.show();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                 checkDangNhap = data.getBooleanExtra("CheckDangNhap",false);
                username = data.getStringExtra("ID_TaiKhoan");
                txtUser.setText(username);

                    if (checkDangNhap) {
                        Toast.makeText(this,"Dang Nhap Thanh Cong",Toast.LENGTH_SHORT).show();
                     }
            }
        }else if(requestCode == 2){
            checkDangNhap = data.getBooleanExtra("CheckDangNhap",true);
        }
    }
    public void AddNhaTro(){
        Bundle b = new Bundle();
        if(checkDangNhap){
            Intent i = new Intent(this,DangKyNhaTro.class);
            i.putExtra("username",username);
            startActivity(i);
        }else{
            Intent i = new Intent(this,Login.class);
            startActivityForResult(i, 1);
        }
    }
    public void Login(){
        if(checkDangNhap){
           Intent i = new Intent(this,ThongTinUser.class);
            i.putExtra("username",username);
            startActivityForResult(i, 2);
        }else{
            Intent i = new Intent(this, Login.class);
            startActivityForResult(i, 1);
        }
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {

        for(ModelChiTietNhaTro obj : Allnhatro){
            LatLng sydney = new LatLng(obj.vido , obj.kinhdo );
           Marker mar = googleMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_marker)).position(sydney).title(obj.ID_nhatro).snippet(obj.sodienthoai));
            markerMap.put(mar.getId(),obj.ID_nhatro);
        }
        Map.setOnInfoWindowClickListener(this);
        Map.setOnMarkerClickListener(this);


    }
    public void addMarker(){

    }
    public final GoogleMap.OnCameraChangeListener oncamera = new GoogleMap.OnCameraChangeListener() {
        @Override
        public void onCameraChange(CameraPosition cameraPosition) {

        }
    };
    public void getInn( final Activity activity){

        final ProgressDialog dialog = ProgressDialog.show(activity, "Uploading", "Please wait...", true);
        ParseQuery<ParseObject> query = ParseQuery.getQuery("ChiTietNhaTro");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null) {
                    for (ParseObject object : objects) {
                        final ModelChiTietNhaTro nhatro = new ModelChiTietNhaTro();
                        nhatro.diachi = object.getString("DiaChi");
                        nhatro.giatien = object.getString("GiaTien");
                        nhatro.Loai = object.getString("Loai");
                        nhatro.sodienthoai = object.getString("SoDienThoai");
                        nhatro.KhuVuc = object.getString("KhuVuc");
                        nhatro.Vung = object.getString("Vung");
                        nhatro.diachi = object.getString("DiaChi");
                        nhatro.mota = object.getString("MoTa");
                        nhatro.ID_nhatro = object.getObjectId();
                        ParseGeoPoint point = object.getParseGeoPoint("ToaDo");
                        nhatro.vido = point.getLatitude();
                        nhatro.kinhdo = point.getLongitude();
                        ParseFile fil1 = object.getParseFile("Image1");
                        fil1.getDataInBackground(new GetDataCallback() {
                            @Override
                            public void done(byte[] data, ParseException e) {
                                if (e == null) {
                                    nhatro.image1 = BitmapFactory.decodeByteArray(data, 0, data.length);
                                } else {

                                }
                            }
                        });
                        ParseFile fil2 = object.getParseFile("Image2");
                        fil2.getDataInBackground(new GetDataCallback() {
                            @Override
                            public void done(byte[] data, ParseException e) {
                                if (e == null) {
                                    nhatro.image2 = BitmapFactory.decodeByteArray(data, 0, data.length);
                                } else {

                                }
                            }

                        });
                        ParseFile fil3 = object.getParseFile("Image3");
                        fil3.getDataInBackground(new GetDataCallback() {
                            @Override
                            public void done(byte[] data, ParseException e) {
                                if (e == null) {
                                    nhatro.image3 = BitmapFactory.decodeByteArray(data, 0, data.length);
                                    Allnhatro.add(nhatro);
                                    mapFragment.getMapAsync((OnMapReadyCallback) activity);
                                    if (!pref.getBoolean("isCheckStart", false )) {
                                        search(activity);
                                        editor.putBoolean("isCheckStart", true);
                                        editor.commit();
                                    }

                                    dialog.dismiss();
                                } else {
                                    dialog.dismiss();
                                }
                            }
                        });

                    }


                } else {
                    dialog.dismiss();
                }
            }
        });

    }
    @Override
    public void onLocationChanged(Location location) {
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();

    }

    @Override
    public void onProviderDisabled(String provider) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onProviderEnabled(String provider) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // TODO Auto-generated method stub
    }


    @Override
    public void onInfoWindowClick(Marker marker) {
        ThongtinNhatro(marker);

    }
    public void ThongtinNhatro(Marker marker){
        int a = findID(markerMap.get(marker.getId()));
       ModelChiTietNhaTro obj = Allnhatro.get(a);
        String diachi = obj.diachi +", "  + obj.KhuVuc +", " + obj.Vung ;
        Intent i = new Intent(this,thongtinnhatro.class);
        i.putExtra("diachi",obj.diachi);
        i.putExtra("sodienthoai",obj.sodienthoai);
        i.putExtra(("image1"), covert(obj.image1));
        i.putExtra(("image2"), covert(obj.image2));
        i.putExtra(("image3"), covert(obj.image3));
        i.putExtra("loai",obj.Loai);
        i.putExtra("diachi",diachi);
        i.putExtra("gia",obj.giatien);
        i.putExtra("mota", obj.mota);
        startActivity(i);
    }
    public byte[] covert(Bitmap bmp){
        ByteArrayOutputStream byteArrayBitmapStream = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayBitmapStream);
        byte[] b = byteArrayBitmapStream.toByteArray();
        return b;
    }
    public int findID(String idnhatro){
        for(int i =0; i< Allnhatro.size(); i ++){
            if(idnhatro == Allnhatro.get(i).ID_nhatro){
                return i;
            }
        }
        return -1;
    }


    @Override
    public boolean onMarkerClick(Marker marker) {
        int i = findID(markerMap.get(marker.getId()));
        markerInfo(i);
        marker.showInfoWindow();
        return false;
    }
    public void markerInfo(final int i){
        Map.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

            // Use default InfoWindow frame
            @Override
            public View getInfoWindow(Marker arg0) {
                return null;
            }

            // Defines the contents of the InfoWindow
            @Override
            public View getInfoContents(Marker arg0) {
                // Getting view from the layout file info_window_layout
                View v = getLayoutInflater().inflate(R.layout.info_window_googlemap, null);
                ImageView img = (ImageView) v.findViewById(R.id.infImage);
                TextView txtDiachi = (TextView) v.findViewById(R.id.InfDiaChi);
                TextView txtGia = (TextView) v.findViewById(R.id.InfGia);
                int index = findID(arg0.getTitle());
                txtDiachi.setText("SDT: " + Allnhatro.get(i).sodienthoai);
                img.setImageBitmap(Allnhatro.get(i).image1);

                StringBuffer giamoi = new StringBuffer("Giá: " + Allnhatro.get(i).giatien);

                int count = Allnhatro.get(i).giatien.length() / 3;
                //Toast.makeText(acti,count%3 +" ", Toast.LENGTH_LONG).show();
                int ia = 1;
                if(Integer.parseInt(Allnhatro.get(i).giatien) >999999) {
                    if (count % 2 == 0) {
                        while (i < count) {
                            giamoi = giamoi.insert(giamoi.length() - (3 * ia) - (ia - 1), ".");
                            ia++;
                        }
                    } else {
                        while (i <= count) {
                            giamoi = giamoi.insert(giamoi.length() - (3 * ia) - (ia - 1), ".");
                            ia++;
                        }
                    }
                }else{
                    giamoi = giamoi.insert(giamoi.length() - 3 , ".");
                }


                txtGia.setText(giamoi + " VND");

                LatLng latLng = arg0.getPosition();
                return v;

            }
        });
    }
    public GeoPoint getLocationFromAddress(String strAddress) throws IOException {

        Geocoder coder = new Geocoder(this);
        List<Address> address;
        GeoPoint p1 = null;
        address = coder.getFromLocationName(strAddress,5);
        if (address==null) {

            return null;
        }if(address.size() >0){

            Address location = address.get(0);
            location.getLatitude();
            location.getLongitude();

            p1 = new GeoPoint((int) (location.getLatitude() * 1E6),
                    (int) (location.getLongitude() * 1E6));
        }else{
            Toast.makeText(this,"khong co dia chi nay",Toast.LENGTH_LONG).show();
        }

        return p1;

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Map.clear();
        Allnhatro = new ArrayList<ModelChiTietNhaTro>();
        markerMap  = new HashMap<String, String>();
        getInn(this);
    }
}
