package database;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.example.sony.timnha.Dangky;
import com.example.sony.timnha.Login;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import com.parse.SaveCallback;


import java.util.List;
import java.util.Objects;

import model.modelTaiKhoan;

/**
 * Created by SONY on 11/17/2015.
 */
public class taikhoanDAO extends Activity {
    public int checkSignUp;
    public modelTaiKhoan thongtin;
    public taikhoanDAO(){


    }

    public void chuyenact(){

    }
    public void queryAdd(modelTaiKhoan tk){
        ParseObject a = new ParseObject("TaiKhoan");
        a.put("ID_TaiKhoan", tk.User);
        a.put("Pass", tk.pass);
        a.put("Email", tk.email);
        a.put("SoDienThoai", tk.sdt);
        a.put("GioiTinh", tk.gioitinh);
        a.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {

                } else {

                }

            }
        });
    }
    public void getUser(String user, final Activity activity){
        final ProgressDialog dialog = ProgressDialog.show(activity, "Uploading", "Please wait...", true);

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
                    //Toast.makeText(activity, thongtin.email, Toast.LENGTH_LONG).show();
                    dialog.dismiss();

                } else {
                    Toast.makeText(activity, e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }




}
