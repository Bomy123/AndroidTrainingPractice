package com.zmb.androidtrainingpractice.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.Image;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.zmb.androidtrainingpractice.R;
import com.zmb.androidtrainingpractice.gridviewpractice.GridviewPracticeActivity;

import java.net.HttpURLConnection;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by zhangmingbao on 17-7-25.
 */
public class MPhotoWallAdapterWithOkhttp<T> extends ArrayAdapter<String> implements AbsListView.OnScrollListener , LoadTask.ILoadTask{
    private int mFirstVisibleItemIndex = 0;
    private int mLastVisibleItemIndex = 0;
    private int mVisibleItemCount = 0;
    private boolean mIsFirstTimeEnter = false;
    private String[] urlArr = null;
    private T t;
    private LruCache<String,Bitmap> mMemeryCache = null;
    private Context mContext = null;
    private Set<LoadTask> taskCollection = null;
    public MPhotoWallAdapterWithOkhttp(Context context, int resource, String[] objects,T t) {
        super(context, resource, objects);
        this.urlArr = objects;
        this.t = t;
        this.mContext = context;
        long maxMemery =  Runtime.getRuntime().maxMemory();
        long memCache = maxMemery/16;
        mMemeryCache = new LruCache<String, Bitmap>((int) memCache){

            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getByteCount();
            }
        };
        taskCollection = new HashSet<LoadTask>();
    }

    private Bitmap getBitmapFromMemoryCache(String url){
        return mMemeryCache.get(url);
    }
    private Bitmap getBitmapToMemoryCache(String url,Bitmap bitmap){
        return mMemeryCache.put(url,bitmap);
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = null;
        String url = urlArr[position];
        //Bitmap bitmap = getBitmapFromMemoryCache(url);
        ImageView photoView = null;
        if(convertView == null){
            view = LayoutInflater.from(mContext).inflate(R.layout.layout_photowallitem,null);
        }
        else
        {
            view = convertView;
        }
        photoView = (ImageView) view.findViewById(R.id.photowallitem);
        view.setTag(url);
        Glide.with(mContext).load(url).placeholder(R.drawable.no_photo).into(photoView);
        
//        if(bitmap != null)
//        {
//            photoView.setImageBitmap(bitmap);
//        }
//        else
//        {
//            photoView.setImageResource(R.drawable.no_photo);
//        }
        return view;
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if(scrollState == SCROLL_STATE_IDLE){
            doDownLoadBitmaps();
        }
        else {
            cancelAllTasks();
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
mFirstVisibleItemIndex = firstVisibleItem;
        mVisibleItemCount = visibleItemCount;
        mLastVisibleItemIndex = mFirstVisibleItemIndex + mVisibleItemCount;
    }
    private void cancelAllTasks()
    {

//        for(LoadTask loadTask:taskCollection){
//            loadTask.cancel(false);
//            taskCollection.remove(loadTask);
//        }
    }
    private void doDownLoadBitmaps()
    {
        for(int i = mFirstVisibleItemIndex;i < mLastVisibleItemIndex;i++){
            String imgurl = urlArr[i];
            View view = ((GridView)t).findViewWithTag(imgurl);
            final ImageView imageView = (ImageView) view.findViewById(R.id.photowallitem);
//            LoadTask loadTask = new LoadTask(this);
//            taskCollection.add(loadTask);
//            loadTask.execute(urlArr[i]);
            Glide.with(mContext).load(imgurl).placeholder(R.drawable.no_photo).into(new SimpleTarget<GlideDrawable>() {
                @Override
                public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                    imageView.setImageDrawable(resource);
                }
            });
        }
    }

    @Override
    public Object doInBackground(Object... params) {
        String url = (String) params[0];

        return null;
    }

    @Override
    public void onPreExecute() {

    }

    @Override
    public void onPostExecute(Object o) {

    }

    @Override
    public void onProgressUpdate(Void... values) {

    }

    @Override
    public void onCancelled(Object o) {

    }

    @Override
    public void onCancelled() {

    }
}
