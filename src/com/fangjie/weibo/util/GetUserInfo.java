package com.fangjie.weibo.util;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import org.json.JSONException;
import org.json.JSONObject;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.util.Log;

import com.fangjie.weibo.bean.UserInfo;
import com.weibo.sdk.android.Oauth2AccessToken;
import com.weibo.sdk.android.WeiboException;
import com.weibo.sdk.android.api.AccountAPI;
import com.weibo.sdk.android.api.UsersAPI;
import com.weibo.sdk.android.net.RequestListener;
/**
 * GetUserInfo类
 * 通过API获取用户信息
 * @author 方杰
 * @date 2013-7-29
 */
public class GetUserInfo {

	public static String UID="";
	private static UserInfo user;
	
	/**
	 * 通过accessToken请求用户信息UID
	 * @param accessToken
	 */
	public static void reqUID(Oauth2AccessToken accessToken)
	{
		AccountAPI account=new AccountAPI(accessToken);
		account.getUid(new RequestListener(){
			@Override
			public void onComplete(String result) {
				// TODO Auto-generated method stub
				try {
					JSONObject object =new JSONObject(result);
					UID=object.getString("uid");
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			@Override
			public void onError(WeiboException arg0) {
				// TODO Auto-generated method stub
			}
			@Override
			public void onIOException(IOException arg0) {
				// TODO Auto-generated method stub
				
			}
		});
	}
	
	/**
	 * 请求UID之后，获取UID
	 * @return string
	 */
	public static String getUID()
	{
		String id=UID;
		UID="";
		return id;
	}
	
	/**
	 * 通过accessToken，uid请求获取用户信息UserInfo(name,icon......)
	 * @param accessToken
	 * @param uid
	 */
	public static void reqUserInfo(final Oauth2AccessToken accessToken,long uid)
	{
		user=new UserInfo();
		UsersAPI userapi=new UsersAPI(accessToken);
		userapi.show(uid, new RequestListener() {
			@Override
			public void onIOException(IOException arg0) {
				// TODO Auto-generated method stub

			}
			@Override
			public void onError(WeiboException arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onComplete(String arg0) {
				// TODO Auto-generated method stub
				JSONObject object;
				try {
					object = new JSONObject(arg0);
					Bitmap bm=GetUserInfo.getBitmap(object.getString("profile_image_url"));
					GetUserInfo.user.setUserIcon(bm);
					GetUserInfo.user.setToken(accessToken.getToken());
					GetUserInfo.user.setIsDefault("0");
					GetUserInfo.user.setUserName(object.getString("screen_name"));
					
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
	}
	public static UserInfo getUserInfo()
	{
		Log.i("OUTPUT",user.getUserName());
		return user;
	}
	
	public static Bitmap getBitmap(String biturl)
	{
		Bitmap bitmap=null;
		
		try {
			URL url=new URL(biturl);
			URLConnection conn=url.openConnection();
			InputStream in =conn.getInputStream();
			bitmap=BitmapFactory.decodeStream(new BufferedInputStream(in));

		} catch (Exception e) {
			e.printStackTrace();
		}
		return bitmap;
	}
	/**
	 * 设置显示头像图片大小
	 * @param bm  图片源
	 * @param newWidth  图片宽度
	 * @param newHeight  图片高度
	 * @return
	 */
	public static  Bitmap scaleImg(Bitmap bm, int newWidth, int newHeight) {
	    // 图片源
	    // Bitmap bm = BitmapFactory.decodeStream(getResources()
	    // .openRawResource(id));
	    // 获得图片的宽高
	    int width = bm.getWidth();
	    int height = bm.getHeight();
	    // 设置想要的大小
	    int newWidth1 = newWidth;
	    int newHeight1 = newHeight;
	    // 计算缩放比例
	    float scaleWidth = ((float) newWidth1) / width;
	    float scaleHeight = ((float) newHeight1) / height;
	    // 取得想要缩放的matrix参数
	    Matrix matrix = new Matrix();
	    matrix.postScale(scaleWidth, scaleHeight);
	    // 得到新的图片
	    Bitmap newbm = Bitmap.createBitmap(bm, 0, 0, width, height, matrix,
	      true);
	    return newbm;
	   }
}
