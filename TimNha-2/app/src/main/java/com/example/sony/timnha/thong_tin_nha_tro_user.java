package com.example.sony.timnha;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import java.text.ParseException;
import java.util.ArrayList;

public class thong_tin_nha_tro_user extends AppCompatActivity {
    Button luu,xoa;
    Spinner vung,khuvuc,loai;
    EditText diachi,sodienthoai,gia,mota;

    ViewPager viewpager;

    String[] ARRvung = {"TP Hồ Chí Minh"};
    ArrayList<String> ARRKhuVuc = new ArrayList<String>();
    ArrayList<String> ARRloai = new ArrayList<String>();
    ArrayList<Bitmap> bmt = new ArrayList<Bitmap>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thong_tin_nha_tro_user);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Chi Tiết Nhà Trọ ");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        for (int i = 0; i<12;i++){
            String a = "Quận " + (i+1);
            ARRKhuVuc.add(a);
        }
        ARRKhuVuc.add("Quận Tân Bình");
        ARRKhuVuc.add("Quận Phú Nhuận");
        ARRKhuVuc.add("Quận Gò Vap");
        ARRloai.add("Nguyên Căn");
        ARRloai.add("Phòng");
        loai = (Spinner)findViewById(R.id.NTULoai);
        vung = (Spinner)findViewById(R.id.NTUVung);
        mota = (EditText)findViewById(R.id.NTUChiTiet);
        khuvuc = (Spinner)findViewById(R.id.NTUkhuvuc);
        diachi = (EditText)findViewById(R.id.NTUDiaChi);
        sodienthoai = (EditText)findViewById(R.id.NTUSdt);
        gia = (EditText)findViewById(R.id.NTUgiatien);
        luu = (Button)findViewById(R.id.NTUbntLUU);
        xoa = (Button)findViewById(R.id.NTUbntxoa);
        viewpager  = (ViewPager)findViewById(R.id.view_pager_user);
        ArrayAdapter<String> adtVung = new ArrayAdapter<String>(this, android.support.v7.appcompat.R.layout.support_simple_spinner_dropdown_item,ARRvung);
        vung.setAdapter(adtVung);
        ArrayAdapter<String> adtKhu = new ArrayAdapter<String>(this, android.support.v7.appcompat.R.layout.support_simple_spinner_dropdown_item,ARRKhuVuc);
        khuvuc.setAdapter(adtKhu);
        ArrayAdapter<String> adtLoai = new ArrayAdapter<String>(this, android.support.v7.appcompat.R.layout.support_simple_spinner_dropdown_item,ARRloai);
        loai.setAdapter(adtLoai);
        Intent i = getIntent();
        String sodienthoaiS = i.getStringExtra("sodienthoai");
        String diachiS = i.getStringExtra("diachi");
        String giaS = i.getStringExtra("gia");
        String motaS = i.getStringExtra("mota");
        String khuvucs = i.getStringExtra("khuvuc");
        String loais =  i.getStringExtra("loai");
         final String ids = i.getStringExtra("idNhaTro");
        byte[] img1 = i.getByteArrayExtra("image1");
        byte[] img2 = i.getByteArrayExtra("image2");
        byte[] img3 = i.getByteArrayExtra("image3");
        Bitmap hinh1 =  BitmapFactory.decodeByteArray(img1, 0, img1.length);
        Bitmap hinh2 =  BitmapFactory.decodeByteArray(img2, 0, img2.length);
        Bitmap hinh3 =  BitmapFactory.decodeByteArray(img3, 0, img3.length);
        bmt.add(hinh1);
        bmt.add(hinh2);
        bmt.add(hinh3);
        loai.setSelection(findIndex(loais,ARRloai));
        khuvuc.setSelection(findIndex(khuvucs, ARRKhuVuc));
        AdapterImage adt = new AdapterImage(this,bmt);
        viewpager.setAdapter(adt);
        sodienthoai.setText(sodienthoaiS);
        gia.setText(giaS);
        diachi.setText(diachiS);
        mota.setText(motaS);
        luu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                update(ids);
            }
        });
        xoa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delete(ids);
            }
        });
    }
    public int findIndex(String item,ArrayList<String> a){
        for (int i = 0; i< a.size();i++){
            if(item.equalsIgnoreCase(a.get(i))){
                return i;
            }
        }
        return -1;
    }
    public void update(String id_nhatro){
        final ProgressDialog dialog = ProgressDialog.show(this, "Uploading", "Please wait...", true);
        ParseQuery<ParseObject> query = ParseQuery.getQuery("ChiTietNhaTro");
        query.getInBackground(id_nhatro, new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject object, com.parse.ParseException e) {
                object.put("Vung",vung.getSelectedItem().toString());
                object.put("Loai",loai.getSelectedItem().toString());
                object.put("KhuVuc",khuvuc.getSelectedItem().toString());
                object.put("DiaChi",diachi.getText().toString());
                object.put("GiaTien",gia.getText().toString());
                object.put("SoDienThoai",sodienthoai.getText().toString());
                object.put("MoTa",mota.getText().toString());
                object.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(com.parse.ParseException e) {
                        dialog.dismiss();
                        finish();
                    }
                });
            }
        });
    }
    public void delete(final String id_nhatro){
        ParseQuery<ParseObject> query = ParseQuery.getQuery("ChiTietNhaTro");
        query.getInBackground(id_nhatro, new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject object, com.parse.ParseException e) {
                try {
                    object.delete();
                } catch (com.parse.ParseException e1) {
                    e1.printStackTrace();
                }
                object.saveInBackground();
                finish();
            }
        });
    }

}
