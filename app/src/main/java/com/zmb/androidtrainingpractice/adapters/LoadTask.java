package com.zmb.androidtrainingpractice.adapters;

import android.os.AsyncTask;

/**
 * Created by zhangmingbao on 17-7-25.
 */
public class LoadTask extends AsyncTask<Object,Void,Object> {
    ILoadTask loadTaskImpl = null;
    public LoadTask(ILoadTask loadTaskImpl) {
        super();
        this.loadTaskImpl = loadTaskImpl;
    }

    @Override
    protected Object doInBackground(Object... params) {

        return loadTaskImpl.doInBackground(params);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        loadTaskImpl.onPreExecute();
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
        loadTaskImpl.onPostExecute(o);
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
        loadTaskImpl.onProgressUpdate(values);
    }

    @Override
    protected void onCancelled(Object o) {
        super.onCancelled(o);
        loadTaskImpl.onCancelled();
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
        loadTaskImpl.onCancelled();
    }

    interface ILoadTask{
        Object doInBackground(Object... params);
        void onPreExecute();
        void onPostExecute(Object o);
        void onProgressUpdate(Void... values);
        void onCancelled(Object o);
        void onCancelled();
    }
}
