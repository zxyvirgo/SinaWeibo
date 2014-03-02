package com.fangjie.weibo.ui;

import java.util.Date;
import java.util.List;

import com.fangjie.weibo.R;
import com.fangjie.weibo.bean.Weibo;
import com.fangjie.weibo.util.AsyncImageLoader;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.util.LruCache;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class WeiboAdapter extends BaseAdapter {
	
	private Context context;
	private List<Weibo> weibos;
	
	private final int maxMemory = (int) Runtime.getRuntime().maxMemory();//获取当前应用程序所分配的最大内存  
    private final int cacheSize = maxMemory / 5;//只分5分之一用来做图片缓存  
    private LruCache<String, Bitmap> mLruCache = new LruCache<String, Bitmap>(  
            cacheSize) {   
    	protected int sizeOf(String key, Bitmap bitmap) {//复写sizeof()方法  
            // replaced by getByteCount() in API 12  
            return bitmap.getRowBytes() * bitmap.getHeight() / 1024; //这里是按多少KB来算  
        }  
    }; 
	
	
	public WeiboAdapter(Context context,List<Weibo> weibos) {
        System.out.println(weibos.get(1).content);
		this.context=context;
		this.weibos=weibos;
	}

	public int getCount() {
		return weibos.size();
	}

	public Object getItem(int position) {
		return null;
	}
	
	public long getItemId(int position) {
		return 0;
	}
	
	public View getView(int position, View convertView, ViewGroup parent) {
		 //position代表位置  
        
        //通过View关联自定义Item布局，进行填充  
		
		 if(convertView == null)
         {
			 convertView = View.inflate(context, R.layout.wb_item, null);  
         }

        System.out.println(position);
        final Weibo weibo =weibos.get(position);

        //获取要显示的组件,注意findViewById的调用对象是上面填充了Item的布局的对象View  
        TextView tv_name = (TextView)convertView.findViewById(R.id.txt_wb_item_uname);  
        TextView tv_content = (TextView)convertView.findViewById(R.id.txt_wb_item_content);  
        TextView tv_time =(TextView)convertView.findViewById(R.id.txt_wb_item_time);  
        TextView tv_from =(TextView)convertView.findViewById(R.id.txt_wb_item_from);  
        TextView tv_comment =(TextView)convertView.findViewById(R.id.txt_wb_item_comment);  
        TextView tv_repost =(TextView)convertView.findViewById(R.id.txt_wb_item_redirect);  
        
        LinearLayout zlayout=(LinearLayout)convertView.findViewById(R.id.lyt_wb_item_sublayout);
        TextView tv_zcontent=(TextView)convertView.findViewById(R.id.txt_wb_item_subcontent); 

        final ImageView iv_userhead=(ImageView)convertView.findViewById(R.id.img_wb_item_head);
        ImageView iv_isv=(ImageView)convertView.findViewById(R.id.img_wb_item_V);
        ImageView iv_content_pic=(ImageView)convertView.findViewById(R.id.img_wb_item_content_pic);
        ImageView iv_zcontent_pic=(ImageView)convertView.findViewById(R.id.img_wb_item_content_subpic);
        
        
        //组件添加内容
        tv_content.setText(weibo.getContent());
        tv_name.setText(weibo.getUser().getName());
        tv_from.setText("来自:"+Html.fromHtml(weibo.getFrom()));
        tv_repost.setText(weibo.getReposts_count()+"");
        tv_comment.setText(weibo.getComments_count()+"");
        tv_time.setText(dealTime(weibo.getTime()));
        
        loadBitmap(weibo.getUser().getProfile_image_url(), iv_userhead,80,80);  
        
        if(!weibo.getBmiddle_pic().equals(""))
        {
            loadBitmap(weibo.getBmiddle_pic(), iv_content_pic,0,0);    
            iv_content_pic.setVisibility(View.VISIBLE);
        }
        else
        {
            iv_content_pic.setVisibility(View.GONE);        	
        }
        
        if(weibo.getUser().isIsv())
        	iv_isv.setVisibility(View.VISIBLE);
        else
        	iv_isv.setVisibility(View.GONE);
        
        if(weibo.getWeibo()!=null)
        {
        	zlayout.setVisibility(View.VISIBLE);
        	tv_zcontent.setText("@"+weibo.getWeibo().getUser().getName()+":"+weibo.getWeibo().getContent());
            if(!weibo.getWeibo().getBmiddle_pic().equals(""))
            {
                loadBitmap(weibo.getWeibo().getBmiddle_pic(), iv_zcontent_pic,0,0);    
                iv_zcontent_pic.setVisibility(View.VISIBLE);
            }
        }
        else
        	zlayout.setVisibility(View.GONE);

        return convertView;  
	}
	
	public void addItem(Weibo weibo)
	{
		weibos.add(weibo);
	}
	
	/**
	 * 
	 * @param urlStr 所需要加载的图片的url，以String形式传进来，可以把这个url作为缓存图片的key
	 * @param image ImageView 控件
	 */
	private void loadBitmap(String urlStr, ImageView image,int width,int height) {
		System.out.println(urlStr);
		AsyncImageLoader asyncLoader = new AsyncImageLoader(image, mLruCache,width,height);//什么一个异步图片加载对象
		Bitmap bitmap = asyncLoader.getBitmapFromMemoryCache(urlStr);//首先从内存缓存中获取图片
		if (bitmap != null) {
			image.setImageBitmap(bitmap);//如果缓存中存在这张图片则直接设置给ImageView
		} else {
			image.setImageResource(R.drawable.user_head);//否则先设置成默认的图片
			asyncLoader.execute(urlStr);//然后执行异步任务AsycnTask 去网上加载图片
		}
	}
	
	@SuppressWarnings("deprecation")
	public String dealTime(String time)
	{
		Date now=new Date();
		long lnow = now.getTime()/1000; 

		long ldate = Date.parse(time)/1000;
		Date date=new Date(ldate);
		
		if((lnow-ldate)<60)
			return (lnow-ldate)+"秒前";
		else if((lnow-ldate)<60*60)
			return ((lnow-ldate)/60)+"分钟前";
		else
			return date.getHours()+":"+date.getMinutes();
	}
}
