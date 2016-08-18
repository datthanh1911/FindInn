package com.example.sony.timnha;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.ArrayList;

/**
 * Created by dat on 12/3/15.
 */
public class AdapterImage extends PagerAdapter {
    private ArrayList<Bitmap> imageBM;
    private Context ctx;
    private LayoutInflater inf;
    public AdapterImage(Context ctx,ArrayList<Bitmap>imageBM){
        this.imageBM = imageBM;
        this.ctx = ctx;
    }
    @Override
    public int getCount() {
        return imageBM.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return (view==(LinearLayout)object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        inf  = (LayoutInflater)ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inf.inflate(R.layout.custom_img_ttnt,container,false);
        ImageView img = (ImageView)v.findViewById(R.id.CTTTNTimage);
        img.setImageBitmap(imageBM.get(position));
        container.addView(v);
        return v;

    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout) object);

    }
}
