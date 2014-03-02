package com.fangjie.weibo.ui;


import android.os.Bundle;

import com.fangjie.weibo.R;
import com.fangjie.weibo.logic.MainService;

import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.ImageView;

public class WelcomeActivity extends Activity {

	private ImageView weibo_logo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
        		WindowManager.LayoutParams.FLAG_FULLSCREEN);
        
        setContentView(R.layout.welcome);
        
        //开启服务
		Intent intent=new Intent(WelcomeActivity.this,MainService.class);
		startService(intent);
		
        weibo_logo=(ImageView)findViewById(R.id.weibo_logo);
        
        //为logo图片设置alpha动画效果，并设置监听器监听动画结束时跳转
        AlphaAnimation animation=new AlphaAnimation(0.1f,1.0f);
        animation.setDuration(3000);
        animation.setAnimationListener(new AnimationListener(){
			@Override
			public void onAnimationEnd(Animation arg0) {
				// TODO Auto-generated method stub
				Intent intent =new Intent(WelcomeActivity.this,LoginActivity.class);
				startActivity(intent);
				finish();
			}

			@Override
			public void onAnimationRepeat(Animation arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onAnimationStart(Animation arg0) {
				// TODO Auto-generated method stub
				
			}
        	
        });
        weibo_logo.setAnimation(animation);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
}
