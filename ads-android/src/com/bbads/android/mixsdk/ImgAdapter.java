// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
package com.bbads.android.mixsdk;
import java.util.ArrayList;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class ImgAdapter extends BaseAdapter {
    class ViewHolder {
        ImageView img;
        ImageView imgCancle;
        ImageView imgPoint;
    }
    public static ArrayList imgs;
    private Context context;
    public ImgAdapter() {}
    public ImgAdapter(Context context1, ArrayList arraylist) {
        context = context1;
        imgs = arraylist;
    }
    @Override
    public int getCount() {
        return 0x7fffffff;
    }
    @Override
    public Object getItem(int i) {
        return imgs.get(Math.abs(i % imgs.size()));
    }
    @Override
    public long getItemId(int i) {
        return Math.abs(i % imgs.size());
    }
    @Override
    public View getView(int i, View view, ViewGroup viewgroup) {
        new ImageView(context);
        return (ImageView) imgs.get(Math.abs(i % imgs.size()));
    }
}
