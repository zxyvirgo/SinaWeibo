package com.fangjie.weibo.util;

import com.fangjie.weibo.bean.UserInfo;

import android.content.Context;
import android.content.SharedPreferences;

public class SharePreferencesUtil {

	/**
	 * 保存当前登录用户信息
	 * @param context
	 * @param user
	 */
	public static void SaveLoginUser(Context context,UserInfo user)
	{
		SharedPreferences sp=context.getSharedPreferences("LOGIN_USER",Context.MODE_PRIVATE);
		SharedPreferences.Editor editor=sp.edit();
		
		editor.putString(UserInfo.TOKEN, user.getToken());
		editor.putString(UserInfo.IS_DEFAULT, user.getIsDefault());
		editor.putString(UserInfo.USER_ID, user.getUserId());
		editor.putString(UserInfo.USER_NAME, user.getUserName());
		
		editor.commit();
	}
	
	/**
	 * 获取当前登录用户信息
	 */
	public static UserInfo getLoginUser(Context context)
	{
		SharedPreferences sp=context.getSharedPreferences("LOGIN_USER",Context.MODE_PRIVATE);
		
		String userName=sp.getString(UserInfo.USER_NAME, "");
		String isDefault=sp.getString(UserInfo.IS_DEFAULT, "");
		String userId=sp.getString(UserInfo.USER_ID, "");
		String token=sp.getString(UserInfo.TOKEN, "");
		
		if("".equals(userName))
			return null;
		else
			return new UserInfo(userId,userName,token,isDefault);
	}
	
	/*
	 * 退出登录
	 */
	public static void removeLoginUser(Context context)
	{
		SharedPreferences sp=context.getSharedPreferences("LOGIN_USER",Context.MODE_PRIVATE);
		SharedPreferences.Editor editor=sp.edit();
		
		editor.remove(UserInfo.TOKEN);
		editor.remove(UserInfo.IS_DEFAULT);
		editor.remove(UserInfo.USER_ID);
		editor.remove(UserInfo.USER_NAME);	
		editor.commit();
	}
	
}
