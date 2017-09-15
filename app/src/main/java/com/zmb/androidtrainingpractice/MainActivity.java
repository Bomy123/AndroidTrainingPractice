package com.zmb.androidtrainingpractice;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zmb.androidtrainingpractice.actionbarpractice.ActionBarActivity;
import com.zmb.androidtrainingpractice.baidumappractice.BaiduMapActivity;
import com.zmb.androidtrainingpractice.gridviewpractice.GridviewPracticeActivity;
import com.zmb.androidtrainingpractice.layoutpractice.FActivity;
import com.zmb.androidtrainingpractice.layoutpractice.GActivity;
import com.zmb.androidtrainingpractice.layoutpractice.LActivity;
import com.zmb.androidtrainingpractice.layoutpractice.RActivity;
import com.zmb.androidtrainingpractice.sensorpractice.AccelerometorSensorActivity;
import com.zmb.androidtrainingpractice.sensorpractice.CompressActivity;
import com.zmb.androidtrainingpractice.sensorpractice.LightSensorActivity;
import com.zmb.androidtrainingpractice.sensorpractice.OrientationSensorActivity;
import com.zmb.androidtrainingpractice.webviewpractice.WebViewActivity;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    List<DemoItemEntity> demoItemEntityList = null;
    ListView demoListView = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        init_demoItemEntityList();
    }
    private void init()
    {
        demoItemEntityList = new ArrayList<>();
        demoListView = (ListView) findViewById(R.id.demolistview);
        demoListView.setAdapter(adapter);
        demoListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                DemoItemEntity demoItem = demoItemEntityList.get(position);
                Intent intent = new Intent(MainActivity.this,demoItem.getIntentClass());
                startActivity(intent);
            }
        });

    }
    private void init_demoItemEntityList()
    {
        demoItemEntityList.add(new DemoItemEntity("ActionBarPractice", ActionBarActivity.class));
        demoItemEntityList.add(new DemoItemEntity("WebViewPractice", WebViewActivity.class));
        demoItemEntityList.add(new DemoItemEntity("LinearLayoutPractice", LActivity.class));
        demoItemEntityList.add(new DemoItemEntity("RelativeLayoutPractice", RActivity.class));
        demoItemEntityList.add(new DemoItemEntity("FrameLayoutPractice", FActivity.class));
        demoItemEntityList.add(new DemoItemEntity("GrideLayoutPractice", GActivity.class));
        demoItemEntityList.add(new DemoItemEntity("GridViewPractice", GridviewPracticeActivity.class));
        demoItemEntityList.add(new DemoItemEntity("LightSensorActivity", LightSensorActivity.class));
        demoItemEntityList.add(new DemoItemEntity("AccelerometorSensorActivity", AccelerometorSensorActivity.class));
        demoItemEntityList.add(new DemoItemEntity("OrientationSensorActivity", OrientationSensorActivity.class));
        demoItemEntityList.add(new DemoItemEntity("CompressActivity", CompressActivity.class));
        demoItemEntityList.add(new DemoItemEntity("BaiduMapActivity", BaiduMapActivity.class));
    }
    BaseAdapter adapter = new BaseAdapter() {
        @Override
        public int getCount() {
            return demoItemEntityList.size();
        }

        @Override
        public Object getItem(int position) {
            return demoItemEntityList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = LayoutInflater.from(MainActivity.this).inflate(R.layout.layout_demo_item,null);
            ViewHolder viewHolder = null;
            if(convertView ==null)
            {
                viewHolder = new ViewHolder();
                convertView = LayoutInflater.from(MainActivity.this).inflate(R.layout.layout_demo_item,null);
                viewHolder.item_name = (TextView) convertView.findViewById(R.id.demo_name);
                convertView.setTag(viewHolder);
            }
            viewHolder = (ViewHolder) convertView.getTag();
            viewHolder.item_name.setText(demoItemEntityList.get(position).getTitle());
            return convertView;
        }

        class ViewHolder
        {
            TextView item_name;
        }
    };
    class DemoItemEntity{
        public Class getIntentClass() {
            return aClass;
        }

        Class aClass;

        public String getTitle() {
            return title;
        }

        String title;
        public DemoItemEntity(String title,Class intend)
        {
            this.title = title;
            this.aClass = intend;
        }
    }
}
