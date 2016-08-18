package com.example.sony.timnha;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.maps.GeoPoint;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.GetDataCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import database.taikhoanDAO;
import model.ModelChiTietNhaTro;
import model.modelTaiKhoan;

public class ThongTinUser extends AppCompatActivity {
    TextView email,sodienthoai,gioitinh;
    ListView lvAllNhaTro;
    taikhoanDAO taikhoan;
    modelTaiKhoan thongtin;

    Button bntLogout;
    String ID;
    ArrayList<ModelChiTietNhaTro> Allnhatro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thong_tin_user);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Thông Tin Cá ");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backAVT();
            }
        });

        taikhoan = new taikhoanDAO();
        ID = getIntent().getStringExtra("username");
        modelTaiKhoan User = getUser(ID,this);

        email = (TextView)findViewById(R.id.TTEmail);
        sodienthoai = (TextView)findViewById(R.id.TTSdt);
        gioitinh = (TextView)findViewById(R.id.TTGioiTinh);
        lvAllNhaTro = (ListView)findViewById(R.id.TTList);
        bntLogout = (Button)findViewById(R.id.TTBtnLogout);
        bntLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickLogOut();
            }
        });
        getInn(ID, this);
        lvAllNhaTro.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                clickLv(position);

            }
        });




    }
    public void clickLv(int pos){
        ModelChiTietNhaTro obj = Allnhatro.get(pos);
        Intent i = new Intent(this,thong_tin_nha_tro_user.class);
        i.putExtra("diachi", obj.diachi);
        i.putExtra("sodienthoai",obj.sodienthoai);
        i.putExtra(("image1"), covert(obj.image1));
        i.putExtra(("image2"), covert(obj.image2));
        i.putExtra(("image3"), covert(obj.image3));
        i.putExtra("loai",obj.Loai);
        i.putExtra("gia",obj.giatien);
        i.putExtra("mota",obj.mota);
        i.putExtra("vung",obj.Vung);
        i.putExtra("khuvuc",obj.KhuVuc);
        i.putExtra("loai", obj.Loai);
        i.putExtra("idNhaTro", obj.ID_nhatro);
        startActivity(i);
    }
    public void clickLogOut(){
        Intent i = new Intent(this,MainActivity.class);
        i.putExtra("CheckDangNhap", false);
        setResult(RESULT_OK, i);
        finish();
    }
    public modelTaiKhoan getUser(String user, final Activity activity){

        thongtin = new modelTaiKhoan();
        ParseQuery<ParseObject> query = ParseQuery.getQuery("TaiKhoan");
        query.whereEqualTo("ID_TaiKhoan", user);
        query.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject User, ParseException e) {
                if (e == null) {
                    thongtin = new modelTaiKhoan();
                    thongtin.User = User.getString("ID_TaiKhoan");
                    thongtin.email = User.getString("Email");
                    thongtin.gioitinh = User.getBoolean("GioiTinh");
                    thongtin.sdt = User.getString("SoDienThoai");
                    thongtin.pass = User.getString("Pass");
                    email.setText(thongtin.email);
                    sodienthoai.setText(thongtin.sdt);
                    if (thongtin.gioitinh) {
                        gioitinh.setText("Nam");
                    } else {
                        gioitinh.setText("Nữ");
                    }


                } else {

                    Toast.makeText(activity, e.getMessage(), Toast.LENGTH_LONG).show();

                }
            }
        });
        return thongtin;
    }
    public void backAVT(){
        Intent i = new Intent(this,MainActivity.class);
        setResult(RESULT_CANCELED, i);
        finish();
    }

    public void getInn(String user, final Activity activity){
        Allnhatro = new ArrayList<ModelChiTietNhaTro>();
        final ProgressDialog dialog = ProgressDialog.show(activity, "Loading", "Please wait...", true);
        ParseQuery<ParseObject> query = ParseQuery.getQuery("ChiTietNhaTro");
        query.whereEqualTo("ID_TaiKhoan", user);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (objects.size() != 0) {
                    if (e == null) {
                        for (ParseObject object : objects) {
                            final ModelChiTietNhaTro nhatro = new ModelChiTietNhaTro();
                            nhatro.ID_nhatro = object.getObjectId();
                            nhatro.diachi = object.getString("DiaChi");
                            nhatro.giatien = object.getString("GiaTien");
                            nhatro.Loai = object.getString("Loai");
                            nhatro.sodienthoai = object.getString("SoDienThoai");
                            nhatro.KhuVuc = object.getString("KhuVuc");
                            nhatro.Vung = object.getString("Vung");
                            nhatro.diachi = object.getString("DiaChi");
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
                                        AdapterDSInn  adt = new AdapterDSInn(activity, R.layout.layout_ds_inn, Allnhatro);
                                        lvAllNhaTro.setAdapter(adt);
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
                } else {
                    dialog.dismiss();
                    Toast.makeText(activity, "Chua Co Nha Tro Nao", Toast.LENGTH_LONG).show();
                }
            }


        });
    }
    public byte[] covert(Bitmap bmp){
        ByteArrayOutputStream byteArrayBitmapStream = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayBitmapStream);
        byte[] b = byteArrayBitmapStream.toByteArray();
        return b;
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        getInn(ID,this);
    }
}
