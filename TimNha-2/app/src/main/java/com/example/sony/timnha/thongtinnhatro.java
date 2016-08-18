package com.example.sony.timnha;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Display;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import model.ModelChiTietNhaTro;

public class thongtinnhatro extends AppCompatActivity {
    Button goi,sms,xemmap;
    TextView diachi,gia,mota;
    ViewPager viewp;
    AdapterImage adtImage;

    Bitmap hinh1,hinh2,hinh3;
    ImageView imgV1;
    static final int MIN_DISTANCE = 150;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thongtinnhatro);
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
        goi = (Button)findViewById(R.id.TTNTGoi);
        sms = (Button)findViewById(R.id.TTNTSms);
        diachi = (TextView)findViewById(R.id.TTNTDiaChi);
        gia = (TextView)findViewById(R.id.TTNTGIA);
        mota = (TextView)findViewById(R.id.TTNTMota);
        xemmap = (Button)findViewById(R.id.TTNTButtonXemDiaChi);
        viewp = (ViewPager)findViewById(R.id.view_pager);


        Intent i  = getIntent();
        final String sodienthoaiS = i.getStringExtra("sodienthoai");
        String diachiS = i.getStringExtra("diachi");
        String giaS = i.getStringExtra("gia");
        String motaS = i.getStringExtra("mota");
        byte[] img1 = i.getByteArrayExtra("image1");
        byte[] img2 = i.getByteArrayExtra("image2");
        byte[] img3 = i.getByteArrayExtra("image3");
        StringBuffer giamoi = new StringBuffer(giaS);

        int count = giaS.length()/3;
        //Toast.makeText(acti,count%3 +" ", Toast.LENGTH_LONG).show();
        int ia = 1;
        if(Integer.parseInt(giaS) >999999) {
            if (count % 2 == 0) {
                while (ia < count) {
                    giamoi = giamoi.insert(giamoi.length() - (3 * ia) - (ia - 1), ".");
                    ia++;
                }
            } else {
                while (ia <= count) {
                    giamoi = giamoi.insert(giamoi.length() - (3 * ia) - (ia - 1), ".");
                    ia++;
                }
            }
        }else{
            giamoi = giamoi.insert(giamoi.length() - 3 , ".");
        }
        diachi.setText(diachiS);
        gia.setText(giamoi + " đ");
        mota.setText(motaS);
        Display display = getWindowManager().getDefaultDisplay();
        int width = display.getWidth(); // ((display.getWidth()*20)/100)
         hinh1 =  BitmapFactory.decodeByteArray(img1, 0, img1.length);
        hinh2 =  BitmapFactory.decodeByteArray(img2, 0, img2.length);
         hinh3 =  BitmapFactory.decodeByteArray(img3, 0, img3.length);
        ArrayList<Bitmap> arrImage = new ArrayList<Bitmap>();
        arrImage.add(hinh1);
        arrImage.add(hinh2);
        arrImage.add(hinh3);
        AdapterImage adt = new AdapterImage(this,arrImage);
        viewp.setAdapter(adt);
        goi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                call(sodienthoaiS);
            }
        });
        sms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.fromParts("sms", sodienthoaiS, null)));
            }
        });
        xemmap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               xemnhatro();
            }
        });

    }
    public void xemnhatro(){
        Intent i = new Intent(this,XemDiaChiMap.class);
        i.putExtra("diachi",diachi.getText().toString() + "Việt Nam");
        startActivity(i);
    }
    public void call(String number){
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + number));
        startActivity(intent);
    }
}
