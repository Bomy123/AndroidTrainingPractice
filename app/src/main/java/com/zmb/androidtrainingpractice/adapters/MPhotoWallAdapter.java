package com.zmb.androidtrainingpractice.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Log;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;

import com.zmb.androidtrainingpractice.R;
import com.zmb.androidtrainingpractice.layoutpractice.FActivity;

import java.io.IOException;
import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by zhangmingbao on 17-7-25.
 */
public class MPhotoWallAdapter extends ArrayAdapter<String> implements GridView.OnScrollListener{

    private int mFirstVisibleItem = 0;
    private int mLastVisibleItem = 0;
    private int mVisibleItemCount = 0;
    private GridView mPhotoWall = null;
    private LruCache<String,Bitmap> mMemoryCache = null;
    private Set<BitmapLoadTask> mBitmapTaskCollection = null;
    private Context mContext = null;
    private String[] urlArr = null;
    private boolean isFirstEnter = true;
    public MPhotoWallAdapter(Context context, int resource, String[] objects , GridView wall) {
        super(context, resource, objects);
        mPhotoWall = wall;
        urlArr = objects;
        mContext = context;
        mBitmapTaskCollection = new HashSet<BitmapLoadTask>();
        int maxMemory = (int) Runtime.getRuntime().maxMemory();
        int mCacheMemory = maxMemory/24;
        Log.d("MPhotoWallAdapter", "MPhotoWallAdapter: mCacheMemory:"+mCacheMemory);
        mMemoryCache = new LruCache<String, Bitmap>(mCacheMemory){

            @Override
            protected void entryRemoved(boolean evicted, String key, Bitmap oldValue, Bitmap newValue) {
                super.entryRemoved(evicted, key, oldValue, newValue);
                Log.d("MPhotoWallAdapter", "entryRemoved: "+key);
            }

            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                return bitmap.getByteCount();
            }

        };
        wall.setOnScrollListener(this);
        try {
            mPhotoWall.setFastScrollEnabled(true);
            Field f = AbsListView.class.getDeclaredField("mFastScroll");
            f.setAccessible(true);
            Object obj = f.get(mPhotoWall);
            f = f.getType().getDeclaredField("mTrackDrawable");
            f.setAccessible(true);
            if(obj == null)
            {
                Log.d("MPhotoWallAdapter", "MPhotoWallAdapter:obj is null ");
            }
            Drawable d = (Drawable) f.get(obj);
            d = context.getResources().getDrawable(R.drawable.second);
            f.set(obj,d);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        String url = urlArr[position];
        View view;
        if(convertView == null)
            view = LayoutInflater.from(mContext).inflate(R.layout.layout_photowallitem,null);
        else
            view = convertView;
        ImageView wallImgView = (ImageView) view.findViewById(R.id.photowallitem);
        wallImgView.setTag(url);
        setWallImg(url,wallImgView);
        return view;
    }

    private void setWallImg(String url,ImageView view){
        Bitmap b = mMemoryCache.get(url);
        if(b != null)
            view.setImageBitmap(b);
        else {
            view.setImageResource(R.drawable.no_photo);
        }

    }

    public void cancelAllTask()
    {
        if(mBitmapTaskCollection == null)
            return;
        for(BitmapLoadTask b:mBitmapTaskCollection)
        {
            b.cancel(true);
        }
    }


    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if(scrollState == SCROLL_STATE_IDLE)
        {
            Log.d("MPhotoWallAdapter", "mFirstVisibleItem: " + mFirstVisibleItem + "   mVisibleItemCount:" + mVisibleItemCount);
            doLoadBitmaps();
        }
        else {
            cancelAllTask();
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

        mFirstVisibleItem = firstVisibleItem;
        mLastVisibleItem = firstVisibleItem + visibleItemCount;
        mVisibleItemCount = visibleItemCount;
        if(isFirstEnter && visibleItemCount > 0)
        {
            doLoadBitmaps();
            isFirstEnter =false;
        }
    }
    private Bitmap getBitmapFromMemoryCache(String url)
    {
        return mMemoryCache.get(url);
    }

    private void addBitmapToMemoryCache(String url,Bitmap bitmap)
    {
        mMemoryCache.put(url,bitmap);
    }
    protected void doLoadBitmaps(){
        for(int i = mFirstVisibleItem;i<mLastVisibleItem;i++)
        {
            String url = urlArr[i];
            Bitmap bitmap = getBitmapFromMemoryCache(url);
            if(bitmap != null){
                ImageView imageView = (ImageView) mPhotoWall.findViewWithTag(url);
                imageView.setImageBitmap(bitmap);
            }
            else
            {
                BitmapLoadTask bt = new BitmapLoadTask();
                mBitmapTaskCollection.add(bt);
                bt.execute(url);
            }
        }
    }

    class BitmapLoadTask extends AsyncTask<String,Void,Bitmap> {

        private String imgurl = null;
        @Override
        protected Bitmap doInBackground(String... params) {
            imgurl = params[0];
            Bitmap bitmap = downloadBitmap();
            if(bitmap != null)
                addBitmapToMemoryCache(imgurl,bitmap);
            return bitmap;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Bitmap o) {
            ImageView imageView = (ImageView) mPhotoWall.findViewWithTag(imgurl);
            if(imageView != null && o != null){
                imageView.setImageBitmap(o);
            }
            mBitmapTaskCollection.remove(this);
        }

        protected Bitmap downloadBitmap() {
            Bitmap bitmap = null;
            HttpURLConnection httpURLConnection = null;
            URL url = null;
            try {
                url = new URL(imgurl);

            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setConnectTimeout(5000);
            httpURLConnection.setReadTimeout(10000);
            bitmap = BitmapFactory.decodeStream(httpURLConnection.getInputStream());
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }finally {
                if(httpURLConnection != null)
                    httpURLConnection.disconnect();
            }
            return bitmap;
        }

    }

}
