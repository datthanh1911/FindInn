package database;

import android.app.Activity;

import com.parse.Parse;
import com.parse.ParseObject;

import model.modelLoai;

/**
 * Created by SONY on 11/17/2015.
 */
public class LoaiDAO {
    public  LoaiDAO(Activity activity){
        Parse.enableLocalDatastore(activity);
        Parse.initialize(activity, "baLEjE7EkM9lsBTsPkvkmGOaZNwmhjVGhSNQw2tt", "4UqCg8aIcyWiobEMvfArqFBc8AhvGFRbatcWTZme");
    }
    public void insert(modelLoai loai){
        ParseObject tableLoai = new ParseObject("Loai");
        tableLoai.put("ID_Loai", loai.ID_Loai);
        tableLoai.put("TenLoai", loai.TenLoai);
        tableLoai.saveInBackground();

    }
}
