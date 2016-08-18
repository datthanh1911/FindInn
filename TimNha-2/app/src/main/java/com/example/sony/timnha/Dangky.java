package com.example.sony.timnha;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;

import database.ChiTietNhaTroDAO;
import database.taikhoanDAO;
import model.modelTaiKhoan;

public class Dangky extends AppCompatActivity{
    public EditText user,pass,nhaplaipass,email,sodienthoai;
    public Button bntDangKy;
    public RadioGroup groupRadio;
    public RadioButton RadioNam,RadioNu;
    taikhoanDAO taikhoans;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dangky);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Đăng Ký");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        taikhoans  = new taikhoanDAO();
        user = (EditText)findViewById(R.id.DKuser);
        pass = (EditText)findViewById(R.id.DKpass);
        nhaplaipass = (EditText)findViewById(R.id.DKNhapLaiPass);
        email = (EditText)findViewById(R.id.DKemail);
        sodienthoai = (EditText)findViewById(R.id.DKsdt);
        bntDangKy = (Button)findViewById(R.id.DKdangky);
        groupRadio = (RadioGroup)findViewById(R.id.DKGroup);
        RadioNam = (RadioButton)findViewById(R.id.DKGioiTinhNam);
        RadioNu = (RadioButton)findViewById(R.id.DKGioTinhNu);

        bntDangKy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    DangKy();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    public void DangKy() throws InterruptedException {
        final ProgressDialog dialog = ProgressDialog.show(this, "Sign Up", "Please wait...", true);
        if(user.length()<6 || pass.length()<6 || !(pass.getText().toString().equals(nhaplaipass.getText().toString()))|| sodienthoai.length()<9){
            dialog.dismiss();
            Toast.makeText(this,"Dang Ky Khong Thanh Cong",Toast.LENGTH_SHORT).show();
        }else{
            ParseQuery<ParseObject> query = ParseQuery.getQuery("TaiKhoan");
            query.whereEqualTo("ID_TaiKhoan", user.getText().toString());
            query.findInBackground(new FindCallback<ParseObject>() {
                public void done(List<ParseObject> users, ParseException e) {
                    if (e == null) {
                        if (users.size() == 0) {
                            modelTaiKhoan tk = new modelTaiKhoan();
                            tk.User = user.getText().toString();
                            tk.pass = pass.getText().toString();
                            tk.email = email.getText().toString();
                            tk.sdt = sodienthoai.getText().toString();
                            int selectedId = groupRadio.getCheckedRadioButtonId();
                            if(selectedId == RadioNam.getId()){
                                tk.gioitinh = true;
                                taikhoans.queryAdd(tk);
                            }else{
                                tk.gioitinh = false;
                                taikhoans.queryAdd(tk);
                            }
                            dialog.dismiss();
                            ToastNe("Đăng Ký Thành Công");
                            finish();

                        } else {
                            dialog.dismiss();
                            ToastNe("UserName Đã Tồn Tại");

                        }
                    } else {
                        dialog.dismiss();
                         ToastNe(e.getMessage());
                    }
                }
            });




        }

        }

    public void clear(){
        user.setText(null);
        pass.setText(null);
        email.setText(null);
        sodienthoai.setText(null);

    }
    public void ToastNe(String a ){
        Toast.makeText(this,a,Toast.LENGTH_SHORT).show();
    }

}
