package com.example.sony.timnha;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import model.ModelChiTietNhaTro;

/**
 * Created by dat on 11/28/15.
 */
public class AdapterDSInn extends ArrayAdapter<ModelChiTietNhaTro> {
    private Activity context;
    private int textViewResourceId;
    private ArrayList<ModelChiTietNhaTro> nhatros;
    public AdapterDSInn(Activity context,int textViewResourceId,
                     ArrayList<ModelChiTietNhaTro> nhatro) {
        super(context, textViewResourceId, nhatro);
        this.context = context;
        this.textViewResourceId = textViewResourceId;
        this.nhatros = nhatro;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (v == null){
            LayoutInflater inflater = (LayoutInflater)
                    context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(textViewResourceId, null);
        }
        ModelChiTietNhaTro nhaTro = nhatros.get(position);
        if(nhaTro != null){
            ImageView img = (ImageView)v.findViewById(R.id.DSImage);
            img.setImageBitmap(nhaTro.image1);
            TextView diachi = (TextView)v.findViewById(R.id.DSDiaChi);
            diachi.setText(nhaTro.diachi);
            TextView gia = (TextView)v.findViewById(R.id.DSGia);

            StringBuffer giamoi = new StringBuffer(nhaTro.giatien);

            int count = nhaTro.giatien.length()/3;
            //Toast.makeText(acti,count%3 +" ", Toast.LENGTH_LONG).show();
            int ia = 1;
            if(Integer.parseInt(nhaTro.giatien) >999999) {
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


            gia.setText(giamoi + " VND");
        }

        return v;
    }
}
