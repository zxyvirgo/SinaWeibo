package com.fangjie.weibo.util;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v4.util.LruCache;
import android.widget.ImageView;

public class AsyncImageLoader extends AsyncTask<String, Void, Bitmap> {

	private ImageView image;  
    private LruCache<String, Bitmap> lruCache;  
    private int width;
    private int height;
    /** 
     * 构造方法，需要把ImageView控件和LruCache 对象传进来 
     * @param image 加载图片到此 {@code}ImageView 
     * @param lruCache 缓存图片的对象 
     */  
    public AsyncImageLoader(ImageView image, LruCache<String, Bitmap> lruCache,int width,int height) {  
        super();  
        this.image = image;  
        this.lruCache = lruCache;  
        this.width=width;
        this.height=width;
    }  
  
    @Override  
    protected Bitmap doInBackground(String... params) {  
        Bitmap bitmap = null;  
        bitmap = GetUserInfo.getBitmap(params[0]); 
        if(width!=0&height!=0)
        	bitmap=GetUserInfo.scaleImg(bitmap, width, height);
        addBitmapToMemoryCache(params[0], bitmap);  
        return bitmap;  
    }  
  
    @Override  
    protected void onPostExecute(Bitmap bitmap) {  
        image.setImageBitmap(bitmap);  
    }  
        //调用LruCache的put 方法将图片加入内存缓存中，要给这个图片一个key 方便下次从缓存中取出来  
    private void addBitmapToMemoryCache(String key, Bitmap bitmap) {  
        if (getBitmapFromMemoryCache(key) == null) {  
            lruCache.put(key, bitmap);  
        }  
    }  
        //调用Lrucache的get 方法从内存缓存中去图片  
    public Bitmap getBitmapFromMemoryCache(String key) {  
        return lruCache.get(key);  
    }  
}
