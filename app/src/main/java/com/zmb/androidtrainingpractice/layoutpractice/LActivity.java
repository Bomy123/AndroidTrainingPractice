package com.zmb.androidtrainingpractice.layoutpractice;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ImageSpan;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.zmb.androidtrainingpractice.R;

public class LActivity extends AppCompatActivity {
    ImageView imageView = null;
    MEditText mEditText = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_l);
        mEditText = (MEditText) findViewById(R.id.mtext);
        imageView = (ImageView) findViewById(R.id.image);
        final String path = this.getIntent().getStringExtra("1");
        Log.d("LActivity", path);;
        String imgtag = "<h1>this is a imgtextview</h1>"+
                "<p>the picture is :</p>"+
                "<img src = '"+ path +"'/>";
        imageView.setImageURI(Uri.parse(path));
        GradientDrawable gd = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM,new int[]{Color.GREEN, Color.WHITE,Color.YELLOW});
        getWindow().setBackgroundDrawable(gd);
        TextView imgtv = (TextView) findViewById(R.id.imgtextview);
//        CharSequence cs = Html.fromHtml(imgtag, new Html.ImageGetter() {
//            @Override
//            public Drawable getDrawable(String source) {
//                Log.d("LActivity", "source: "+source);
//                Bitmap b = BitmapFactory.decodeFile(source);
//                Drawable dr = new BitmapDrawable(b);
//                dr.setBounds(0,0,dr.getIntrinsicWidth(),dr.getIntrinsicHeight());
//                return dr;
//            }
//        },null);
//        imgtv.setText(cs);
        Bitmap bt = BitmapFactory.decodeFile(path);
        Bitmap btsmall = Bitmap.createBitmap(bt,0,bt.getHeight()/4,bt.getWidth(),bt.getHeight()/3);
        Drawable dr = new BitmapDrawable(btsmall);
        dr.setBounds(0,0,dr.getIntrinsicWidth(),dr.getIntrinsicHeight());
        ImageSpan is = new ImageSpan(dr,ImageSpan.ALIGN_BASELINE);
        SpannableString ss = new SpannableString("132422424244242444242424icon");
        ss.setSpan(is,0,4, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        imgtv.setText(ss);
    }
}
