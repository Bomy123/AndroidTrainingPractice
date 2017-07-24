package com.zmb.androidtrainingpractice.layoutpractice;

import android.annotation.TargetApi;
import android.graphics.Color;
import android.os.Build;
import android.os.Process;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.style.BackgroundColorSpan;
import android.text.style.CharacterStyle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.zmb.androidtrainingpractice.R;

public class GActivity extends AppCompatActivity {

    TextView tv = null;
    AutoCompleteTextView autoCompleteTextView = null;
    private String text = "yfduyafoaufougfujhvbljhsblifvlvbfkhbvdkllbvjkdxbvlkhjsb;glsjkb\nfgvaufvaokfuyhavuk";
    String[] strArr = new String[]{"123qeswaffearf","12345677","123dfvbhjvbj","124656hhh","123hvjbshjvbjhb"};
    ProgressBar pb = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_g);
        tv = (TextView) findViewById(R.id.colortext);
        SpannableString sp = new SpannableString(text);
        BackgroundColorSpan backgroundColorSpan = new BackgroundColorSpan(Color.GREEN);
        sp.setSpan(new MCharactorStyle(Color.CYAN,Color.YELLOW),5,20, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tv.setText(sp);
        autoCompleteTextView = (AutoCompleteTextView) findViewById(R.id.atv);
        ArrayAdapter<String> arrayAdapter  =new ArrayAdapter<String>(this,android.R.layout.simple_dropdown_item_1line,strArr);
        autoCompleteTextView.setAdapter(arrayAdapter);
        pb = (ProgressBar) findViewById(R.id.mp);
        pb.setProgress(0);
//        pb.measure(0,0);
//        int[] pbPos = new int[2];
//        pb.getLocationOnScreen(pbPos);
//        LinearLayout.LayoutParams param = (LinearLayout.LayoutParams) pb.getLayoutParams();
//        param.topMargin = pbPos[1];
//        pb.setLayoutParams(param);
//        pb.setRotation(90);
        new Thread(new Runnable() {
            @Override
            public void run() {
                for(int i = 0 ; i<=100;i++)
                {
                    final  int p = i;
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    runOnUiThread(new Runnable() {
                        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
                        @Override
                        public void run() {
                            Log.d("GActivity", "run: setProgress:"+p);
//                            if(p < 50)
//                            {
                                pb.setProgress(p);
//                            }
//                            else if(p == 50)
//                            {
//                                pb.setSecondaryProgress(p);
//                                pb.setProgress(0);
//                            }
//                            else
//                            {
//                                pb.setSecondaryProgress(p);
//                            }
                        }
                    });
                }
            }
        }).start();
    }
    class MCharactorStyle extends CharacterStyle{
        private int tcolor = Color.BLACK;
        private int bcolor = Color.WHITE;
        public MCharactorStyle(int tcolor,int bcolor)
        {
            this.tcolor = tcolor;
            this.bcolor = bcolor;
        }

        @Override
        public void updateDrawState(TextPaint tp) {
            tp.bgColor = bcolor;
            tp.setColor(tcolor);
        }
    }
}
