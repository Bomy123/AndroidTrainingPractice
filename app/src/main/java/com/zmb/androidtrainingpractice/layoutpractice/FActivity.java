package com.zmb.androidtrainingpractice.layoutpractice;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.TextView;

import com.zmb.androidtrainingpractice.R;

public class FActivity extends AppCompatActivity {
TextView gotv = null;
    String text = "testgoOterActivity https://www.baidu.com go go go";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_f);
        gotv = (TextView) findViewById(R.id.goactivity);
        SpannableString sp =new SpannableString(text);
        sp.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                Intent intent = new Intent(FActivity.this,RActivity.class);
                startActivity(intent);
            }
        },3,8, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        SpannableString sp1 = new SpannableString(sp);
        sp1.setSpan(new ClickableSpan() {

            @Override
            public void onClick(View widget) {
                Intent intent = new Intent(FActivity.this,TActivity.class);
                startActivity(intent);
            }
        },9,13,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        gotv.setText(sp1);
        gotv.setMovementMethod(LinkMovementMethod.getInstance());
    }
}
