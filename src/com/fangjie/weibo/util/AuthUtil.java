package com.fangjie.weibo.util;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;

import com.fangjie.weibo.ui.AuthActivity;
import com.weibo.sdk.android.Oauth2AccessToken;
import com.weibo.sdk.android.Weibo;
import com.weibo.sdk.android.WeiboAuthListener;
import com.weibo.sdk.android.WeiboDialogError;
import com.weibo.sdk.android.WeiboException;

public class AuthUtil 
{
    private Weibo mWeibo;
    private static final String CONSUMER_KEY = "798515055";// 替换为开发者的appkey，例如"1646212860";
    private static final String REDIRECT_URL = "http://fangjie.duapp.com";
    public static Oauth2AccessToken accessToken;
    private Context context;
    
    public AuthUtil(Context context)
    {
    	this.context=context;
    }
    //获取授权
    public void reqAccessToken()
    {
		accessToken=null;
    	mWeibo = Weibo.getInstance(CONSUMER_KEY, REDIRECT_URL);
	    mWeibo.authorize(context, new AuthDialogListener());
    }

    //获取accessToken
	public Oauth2AccessToken getAccessToken()
	{
		Oauth2AccessToken token=accessToken;
		accessToken=null;
		return token;
	}
	//授权回调
    class AuthDialogListener implements WeiboAuthListener {
    	
        @Override
        public void onComplete(Bundle values) {
            String token = values.getString("access_token");
            String expires_in = values.getString("expires_in");
            AuthUtil.accessToken = new Oauth2AccessToken(token, expires_in);
            
            //去除Cookie，防止拿原来授权的帐号自动授权
            CookieSyncManager.createInstance(context);  
            CookieSyncManager.getInstance().startSync();  
            CookieManager.getInstance().removeAllCookie();
            
            if (AuthUtil.accessToken.isSessionValid()) {
            //认证成功！！
            	Log.i("OUTPUT","accesstaken from oncomplete"+ AuthUtil.accessToken.getToken());
            	
            	Intent intent=new Intent(context,AuthActivity.class);
            	context.startActivity(intent);
            }
        }

        @Override
        public void onError(WeiboDialogError e) {

        }

        @Override
        public void onCancel() {

        }

        @Override
        public void onWeiboException(WeiboException e) {

        }
    }
}
