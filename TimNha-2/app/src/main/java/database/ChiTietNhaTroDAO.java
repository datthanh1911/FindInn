package database;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.widget.Toast;

import com.example.sony.timnha.DangKyNhaTro;
import com.google.android.maps.GeoPoint;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;

import model.ModelChiTietNhaTro;

/**
 * Created by SONY on 11/17/2015.
 */
public class ChiTietNhaTroDAO {

    public ChiTietNhaTroDAO(){

    }
    public void insert(ModelChiTietNhaTro nhatro, final Activity activity){
        final ProgressDialog dialog = ProgressDialog.show(activity, "Uploading", "Please wait...", true);
        ParseObject tablenhatro = new ParseObject("ChiTietNhaTro");
        ParseGeoPoint point = new ParseGeoPoint();
        point.setLatitude(nhatro.vido);
        point.setLongitude(nhatro.kinhdo);
        tablenhatro.put("Loai", nhatro.Loai);
        tablenhatro.put("ID_TaiKhoan", nhatro.ID_TaiKhoan);
        tablenhatro.put("Vung",nhatro.Vung);
        tablenhatro.put("KhuVuc", nhatro.KhuVuc);
        tablenhatro.put("DiaChi",nhatro.diachi);
        tablenhatro.put("SoDienThoai",nhatro.sodienthoai);
        tablenhatro.put("ToaDo",point);
        tablenhatro.put("GiaTien", nhatro.giatien);
        ByteArrayOutputStream stream1 = new ByteArrayOutputStream();
        nhatro.image1.compress(Bitmap.CompressFormat.JPEG, 100, stream1);
        byte[] data1 = stream1.toByteArray();
        ParseFile file1 = new ParseFile("image1.JPEG", data1);

        ByteArrayOutputStream stream2 = new ByteArrayOutputStream();
        nhatro.image2.compress(Bitmap.CompressFormat.JPEG, 100, stream2);
        byte[] data2 = stream2.toByteArray();
        ParseFile file2 = new ParseFile("image2.JPEG", data2);


        ByteArrayOutputStream stream3 = new ByteArrayOutputStream();
        nhatro.image3.compress(Bitmap.CompressFormat.JPEG, 100, stream3);
        byte[] data3 = stream3.toByteArray();
        ParseFile file3 = new ParseFile("image3.JPEG", data3);

        tablenhatro.put("Image1",file1);
        tablenhatro.put("Image2",file2);
        tablenhatro.put("Image3",file3);
        tablenhatro.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                dialog.dismiss();
                Toast.makeText(activity, "Đăng Nhà Trọ Thành Công!", Toast.LENGTH_LONG).show();
            }
        });
    }
}
