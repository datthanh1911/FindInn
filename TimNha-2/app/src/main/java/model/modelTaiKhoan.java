package model;

/**
 * Created by SONY on 11/17/2015.
 */
public class modelTaiKhoan {
    public String User;
    public String pass;
    public String email;
    public String sdt;
    public boolean gioitinh;

    public modelTaiKhoan(String User, String pass, String email, String sodienthoai,boolean gioitinh){
        this.User = User;
        this.pass = pass;
        this.email = email;
        this.sdt = sodienthoai;
        this.gioitinh = gioitinh;
    }
    public modelTaiKhoan(){

    }
}
