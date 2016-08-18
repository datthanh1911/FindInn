package model;

import android.graphics.AvoidXfermode;
import android.graphics.Bitmap;

/**
 * Created by SONY on 11/17/2015.
 */
public class ModelChiTietNhaTro {
    public String Loai;
    public String ID_TaiKhoan;
    public String ID_nhatro;
    public String Vung;
    public String KhuVuc;
    public double kinhdo;
    public String diachi;
    public double vido;
    public String giatien;
    public String sodienthoai;
    public Bitmap image1,image2,image3;
    public String mota;
    public ModelChiTietNhaTro( String ID_TaiKhoan,String Loai,String sodienthoai,String Vung , String KhuVuc,String diachi,double vido,double kinhdo,String giatien,Bitmap image1,Bitmap image2,Bitmap image3,String mota){
        this.ID_TaiKhoan = ID_TaiKhoan;
        this.Loai = Loai;
        this.Vung = Vung;
        this.KhuVuc = KhuVuc;
        this.diachi = diachi;
        this.kinhdo = kinhdo;
        this.vido = vido;
        this.sodienthoai = sodienthoai;
        this.giatien = giatien;
        this.image1 = image1;
        this.image2 = image2;
        this.image3 = image3;
        this.mota = mota;
    }
    public ModelChiTietNhaTro(){

    }

}
