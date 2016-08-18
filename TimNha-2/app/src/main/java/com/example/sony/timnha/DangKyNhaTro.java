package com.example.sony.timnha;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.maps.GeoPoint;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import database.ChiTietNhaTroDAO;
import model.ModelChiTietNhaTro;

public class DangKyNhaTro extends AppCompatActivity {
    EditText giatien,sodienthoai,diachi,chitiet;
    Spinner khuvuc,vung,loai;
    Button xembando,bntDangKy,bntHuy;
    ImageButton addImage;
    String[] ARRvung = {"TP Hồ Chí Minh"};
    String[] ARRKhuVuc = {"Quận 1", "Quận 2","Quận 3","Quận 4","Quận 5","Quận 6","Quận 7","Quận 8","Quận 9","Quận 10","Quận 11","Quận 12","Quận Tân Bình", "Quận Phú Nhuận","Quận Gò Vap "};
    String[] ARRloai = {"Nguyên Căn","Phòng"};
    private static int RESULT_LOAD_IMG = 1;
    private static int RESULT_LOAD_IMG2 = 2;
    private static int RESULT_LOAD_IMG3 = 3;
    List<Address> address;
    String imgDecodableString;
    ImageView imgView1,imgView2,imgView3;
    int coutPickimg = 0;
    ChiTietNhaTroDAO nhatro;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dang_ky_nha_tro);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);



        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Đăng Ký Nhà Trọ");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        nhatro = new ChiTietNhaTroDAO();
        loai = (Spinner)findViewById(R.id.NTLoai);
        giatien = (EditText)findViewById(R.id.NTgiatien);
        sodienthoai = (EditText)findViewById(R.id.NTSdt);
        chitiet = (EditText)findViewById(R.id.NTChiTiet);
        diachi = (EditText)findViewById(R.id.NTDiaChi);
        khuvuc = (Spinner)findViewById(R.id.NTkhuvuc);
        vung = (Spinner)findViewById(R.id.NTVung);
        xembando = (Button)findViewById(R.id.NTbntXemBanDo);
        bntDangKy = (Button)findViewById(R.id.NTbntDK);
        bntHuy = (Button)findViewById(R.id.NTbntHuy);
        addImage = (ImageButton)findViewById(R.id.NTAddImage);
        imgView1 =  (ImageView) findViewById(R.id.NTImage1);
        imgView2 =  (ImageView) findViewById(R.id.NTImage2);
        imgView3 =  (ImageView) findViewById(R.id.NTImage3);
        chitiet =(EditText)findViewById(R.id.NTChiTiet);
        ArrayAdapter<String> adtVung = new ArrayAdapter<String>(this, android.support.v7.appcompat.R.layout.support_simple_spinner_dropdown_item,ARRvung);
        vung.setAdapter(adtVung);
        ArrayAdapter<String> adtKhu = new ArrayAdapter<String>(this, android.support.v7.appcompat.R.layout.support_simple_spinner_dropdown_item,ARRKhuVuc);
        khuvuc.setAdapter(adtKhu);
        ArrayAdapter<String> adtLoai = new ArrayAdapter<String>(this, android.support.v7.appcompat.R.layout.support_simple_spinner_dropdown_item,ARRloai);
        loai.setAdapter(adtLoai);
        String IDtk = getIntent().getStringExtra("username");
        xembando.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                xembandoClick();
            }
        });
        addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addImageClick();
            }
        });
        bntDangKy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    dangkyClick();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });
        bntHuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clear();
            }
        });

    }
    public void dangkyClick() throws IOException {
        boolean checkGiaTien,checkSoDienThoai,checkDiaChi,CheckImage;
        long GiaTiendouble;
        String sodienthoaiS = null ,VungS = null ,KhuVucS = null,DiaChiS = null,MoTaS = null,LoaiS= null,ChiTietS = null,GiaTienS = null;
        Bitmap image1 = null ,image2 = null,image3 = null;
        double kinhdo = 0.0D ,vido = 0.0D;
        VungS = vung.getSelectedItem().toString();
        KhuVucS = khuvuc.getSelectedItem().toString();
        LoaiS = loai.getSelectedItem().toString();
       try{
           GiaTiendouble = Long.parseLong(giatien.getText().toString()) ;
           if(GiaTiendouble >=100000){
               checkGiaTien = true;
           }else {
               checkGiaTien = false;
               giatien.setError("Số Tiền Phải Lớn Hơn Hoac Bang 100.000 ");
           }

       }catch (Exception e){
           checkGiaTien = false;
           giatien.setError("Gia Tien Khong Hop Le");

       }

        if(sodienthoai.length()>9){
            sodienthoaiS = sodienthoai.getText().toString();
            checkSoDienThoai = true;
        }else{
            checkSoDienThoai = false;
            sodienthoai.setError("So Dien Thoai Khong Dung");
        }

        if(diachi.length()>2){
            DiaChiS = diachi.getText().toString();
            String address = diachi.getText().toString() +"," + khuvuc.getSelectedItem().toString()+ "," +vung.getSelectedItem().toString()+","+"Việt Nam";
            vido = getLocationFromAddress(address).getLatitudeE6()/1E6;
            kinhdo = getLocationFromAddress(address).getLongitudeE6()/1E6;

            checkDiaChi = true;
        }else{
            checkDiaChi = false;
        }
        if((imgView1.getDrawable()!=null)&&(imgView2.getDrawable()!=null)&&(imgView3.getDrawable()!=null) ){
            image1 = ((BitmapDrawable)imgView1.getDrawable()).getBitmap();
            image2 = ((BitmapDrawable)imgView2.getDrawable()).getBitmap();
            image3 = ((BitmapDrawable)imgView3.getDrawable()).getBitmap();
            CheckImage = true;
        }else{
            CheckImage = false;
            Toast.makeText(this,"Phai Dang Du 3 Anh",Toast.LENGTH_LONG).show();
        }

        if(chitiet.length()>0){
            ChiTietS = chitiet.getText().toString();
        }else{
            ChiTietS = "";
        }
        String ID = getIntent().getStringExtra("username");
        if(checkGiaTien&&checkSoDienThoai&&checkDiaChi&&CheckImage&&(address.size()>0)){
            GiaTienS = giatien.getText().toString();
            ModelChiTietNhaTro chitiet = new ModelChiTietNhaTro(ID,LoaiS,sodienthoaiS,VungS,KhuVucS,DiaChiS,vido,kinhdo,GiaTienS,image1,image2,image3,ChiTietS);
            nhatro.insert(chitiet, this);
            clear();

        }else{
            Toast.makeText(this,"Không Thành Công ",Toast.LENGTH_LONG).show();
        }
    }
    public void xembandoClick(){
        if(diachi.length()>0){
             String diachiS = diachi.getText().toString() +"," + khuvuc.getSelectedItem().toString()+ "," +vung.getSelectedItem().toString()+","+"Việt Nam";
        Intent i = new Intent(this,XemDiaChiMap.class);
            i.putExtra("diachi",diachiS);
        startActivity(i);
        }else{
            Toast.makeText(this,"Chưa Nhập Địa Chỉ",Toast.LENGTH_LONG).show();
        }
    }
    public void addImageClick(){
        if(coutPickimg <=2) {
            Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(galleryIntent, RESULT_LOAD_IMG);
        }else{
            Toast.makeText(this,"Ban Chi Duoc Chon 3 Hinh",Toast.LENGTH_LONG).show();
        }

    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (requestCode == RESULT_LOAD_IMG && resultCode == RESULT_OK
                    && null != data) {
                Uri selectedImage = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};


                Cursor cursor = getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);

                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                imgDecodableString = cursor.getString(columnIndex);
                cursor.close();
                if (coutPickimg == 0) {
                    imgView1.setImageBitmap(BitmapFactory
                            .decodeFile(imgDecodableString));
                } else if (coutPickimg == 1) {
                    imgView2.setImageBitmap(BitmapFactory
                            .decodeFile(imgDecodableString));
                } else if (coutPickimg == 2) {
                    imgView3.setImageBitmap(BitmapFactory
                            .decodeFile(imgDecodableString));
                }
                coutPickimg++;

            } else {
                Toast.makeText(this, "Chưa Chọn Image",
                        Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Ảnh Không Đúng  ", Toast.LENGTH_LONG)
                    .show();
        }
    }


    public GeoPoint getLocationFromAddress(String strAddress) throws IOException {

        Geocoder coder = new Geocoder(this);

        GeoPoint p1 = null;
        address = coder.getFromLocationName(strAddress,5);
        if (address.size()==0) {
            Toast.makeText(this,"Địa Chỉ Không Đúng",Toast.LENGTH_LONG).show();
            diachi.setText("");
            return p1 = new GeoPoint(0,0);

        }else {

            Address location = address.get(0);
            location.getLatitude();
            location.getLongitude();

            p1 = new GeoPoint((int) (location.getLatitude() * 1E6),
                    (int) (location.getLongitude() * 1E6));

            return p1;
        }

    }
        public void clear(){
            imgView1.setImageBitmap(null);
            imgView2.setImageBitmap(null);
            imgView3.setImageBitmap(null);
            coutPickimg=0;
            giatien.setText("");
            sodienthoai.setText("");
            diachi.setText("");
            chitiet.setText("");

        }
    }

