package com.example.sony.timnha;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.text.ParseException;
import java.util.List;

import model.modelTaiKhoan;

import static android.widget.Toast.LENGTH_SHORT;

public class Login extends AppCompatActivity {
    public TextView bntDKTK;
    public EditText username,pass;
    public ImageButton bntDN;
     ProgressDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Đăng Nhập");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        bntDKTK = (TextView)findViewById(R.id.LogbntDKTK);
        bntDN = (ImageButton)findViewById(R.id.LogBntDn);
        username = (EditText)findViewById(R.id.LogUser);
        pass = (EditText)findViewById(R.id.LogPass);
        bntDKTK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent();
            }
        });
        bntDN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DN(username.getText().toString(),pass.getText().toString());
            }
        });


    }
    public void intent(){
        Intent i = new Intent(this,Dangky.class);
        startActivity(i);
    }
    public void DN(final String user,String pass){
       dialog = ProgressDialog.show(this, "Login", "Please wait...", true);
        ParseQuery<ParseObject> query = ParseQuery.getQuery("TaiKhoan");
        query.whereEqualTo("ID_TaiKhoan", user);
        query.whereEqualTo("Pass", pass);
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> users, com.parse.ParseException e) {
                if (e == null) {
                    if (users.size() == 0) {
                        username.setError("Sai Username Hoặc Password");
                        dialog.dismiss();
                    } else {
                        chuyendulieu(user);
                        dialog.dismiss();
                    }
                } else {
                    ToastNe(e.getMessage());
                    dialog.dismiss();
                }
            }
        });


    }
    public void chuyendulieu(String id_taikhoan){
        Intent i = new Intent(this, MainActivity.class);
        i.putExtra("CheckDangNhap", true);
        i.putExtra("ID_TaiKhoan", id_taikhoan);
        setResult(RESULT_OK, i);
        dialog.dismiss();
        finish();
    }
    public void ToastNe(String a ){
        Toast.makeText(this,a,Toast.LENGTH_SHORT).show();
    }
}
