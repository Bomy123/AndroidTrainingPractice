package com.zmb.androidtrainingpractice.layoutpractice;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.zmb.androidtrainingpractice.MainActivity;
import com.zmb.androidtrainingpractice.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class RActivity extends AppCompatActivity {
    private final String TAG = "RActivity";
    Button b1 = null,b5;
    RelativeLayout.LayoutParams params = null;
    RelativeLayout relativeLayout = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        relativeLayout = (RelativeLayout) LayoutInflater.from(this).inflate(R.layout.activity_r,null);
        relativeLayout.addView(createButton());
        setContentView(relativeLayout);
        b1 = (Button) findViewById(R.id.b1);
        b1.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
            @Override
            public void onClick(View v) {
                int[] location = new int[2];
                b1.getLocationOnScreen(location);
                b1.measure(0,0);
                int height = b1.getMeasuredHeight();
                int width = b1.getMeasuredWidth();
                Log.d(TAG, "x:"+location[0]+"  y:"+location[1]+"  height:"+height+"   width:"+width);
//                b1.startAnimation(new AlphaAnimation(0.0f,1.0f));
//                params.removeRule(RelativeLayout.RIGHT_OF);
//                params.removeRule(RelativeLayout.BELOW);
//                params.addRule(RelativeLayout.RIGHT_OF,R.id.b2);
//                params.addRule(RelativeLayout.BELOW,R.id.b2);
                File file = new File("/data/data/"+getPackageName()+"/view.png");
                if(file.exists())
                {
                    file.delete();
                }
                getScreenCap();
                Intent intent = new Intent();
                intent.setAction("android.intent.action.LActivity");
                intent.putExtra("1","/data/data/"+getPackageName()+"/view.png");

                startActivity(intent);
            }
        });
    }
    private Button createButton()
    {
        b5 = new Button(this);
        b5.setText("Buttom5");
        params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.RIGHT_OF,R.id.b1);
        params.addRule(RelativeLayout.BELOW,R.id.b1);
        b5.setLayoutParams(params);
return b5;
    }
    @TargetApi(Build.VERSION_CODES.N)
    private void getScreenCap()
    {
//        View view = getWindow().getDecorView();
//        view.setDrawingCacheEnabled(true);
//        view.buildDrawingCache();
        relativeLayout.setDrawingCacheEnabled(true);
//        relativeLayout.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
//        Log.d(TAG, "l:0"+"  y:0"+"  height:"+relativeLayout.getMeasuredHeight()+"   width:"+relativeLayout.getMeasuredWidth());
//        relativeLayout.layout(0,0,relativeLayout.getMeasuredWidth(),relativeLayout.getMeasuredHeight());
//        relativeLayout.buildDrawingCache();
        Bitmap bitmap = relativeLayout.getDrawingCache();
        try {
            FileOutputStream op = new FileOutputStream("/data/data/"+getPackageName()+"/view.png");
            bitmap.compress(Bitmap.CompressFormat.PNG,100,op);
            op.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
