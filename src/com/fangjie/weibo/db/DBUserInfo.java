package com.fangjie.weibo.db;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import com.fangjie.weibo.bean.UserInfo;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

public class DBUserInfo {

	
	private DBHelper dbhelper;
	
	public DBUserInfo(Context context)
	{
		dbhelper = new DBHelper(context);
	}	
	/**
	 *  添加用户信息
	 * @param UserInfo
	 */
	public void insertUserInfo(UserInfo user)
	{
		SQLiteDatabase db = dbhelper.getWritableDatabase();
		
		ByteArrayOutputStream baos=new ByteArrayOutputStream();
		user.getUserIcon().compress(Bitmap.CompressFormat.PNG, 100, baos);
		byte[] usericon=baos.toByteArray();
		ContentValues values = new ContentValues(5);
		
		values.put(UserInfo.USER_ID,user.getUserId());
		values.put(UserInfo.USER_NAME, user.getUserName());
		values.put(UserInfo.TOKEN,user.getToken());
		values.put(UserInfo.IS_DEFAULT,user.getIsDefault());
		values.put(UserInfo.USER_ICON,usericon);	
		
		db.insert(UserInfo.TB_NAME, null, values);//表名称
		db.close(); 
	}
	/**
	 * 根据用户userId获取用户对象
	 * @param String
	 * @return UserInfo
	 */
	public UserInfo getUserInfoByUserId(String uid)
	{
		
		SQLiteDatabase db = dbhelper.getReadableDatabase();

		Cursor cursor =db.query(UserInfo.TB_NAME, new String[]{UserInfo.ID,UserInfo.IS_DEFAULT,UserInfo.TOKEN,
				UserInfo.USER_ID,UserInfo.USER_NAME,UserInfo.USER_ICON},
				UserInfo.USER_ID +"=?",new String[]{uid}, null, null, null);
		UserInfo userInfo =null;
		
		if(null != cursor)
		{
			if(cursor.getCount() >0)
			{
				cursor.moveToFirst();
				userInfo = new UserInfo();
				Long id =cursor.getLong(cursor.getColumnIndex(UserInfo.ID));
				String uId = cursor.getString(cursor.getColumnIndex(UserInfo.USER_ID));
				String userName = cursor.getString(cursor.getColumnIndex(UserInfo.USER_NAME));
				String token = cursor.getString(cursor.getColumnIndex(UserInfo.TOKEN));
				String isDefault = cursor.getString(cursor.getColumnIndex(UserInfo.IS_DEFAULT));
				byte[] byteIcon = cursor.getBlob(cursor.getColumnIndex(UserInfo.USER_ICON));
				
				userInfo.setId(id);
				userInfo.setUserId(uId);
				userInfo.setIsDefault(isDefault);
				userInfo.setToken(token);
				userInfo.setToken(token);
				userInfo.setUserName(userName);
				if(null !=byteIcon)
				{
					Bitmap userIcon=BitmapFactory.decodeByteArray(byteIcon, 0, byteIcon.length);
					userInfo.setUserIcon(userIcon);
				}
			}
		}
		db.close();
		return userInfo;
	}
	
	/**
	 * 获取数据库所有授权用户信息
	 * @param 
	 * @return 	List<UserInfo>
	 */
	
	public List<UserInfo> getAllUserInfo()
	{
		List<UserInfo> users=new ArrayList<UserInfo>();
		
		SQLiteDatabase db = dbhelper.getReadableDatabase();
		Cursor cursor =db.query(UserInfo.TB_NAME, new String[]{UserInfo.ID,UserInfo.IS_DEFAULT,UserInfo.TOKEN,
				UserInfo.USER_ID,UserInfo.USER_NAME,UserInfo.USER_ICON},
				null,null, null, null, null);
		UserInfo userInfo =null;
		
		while(cursor.moveToNext())
		{
				userInfo = new UserInfo();
				Long id =cursor.getLong(cursor.getColumnIndex(UserInfo.ID));
				String uId = cursor.getString(cursor.getColumnIndex(UserInfo.USER_ID));
				String userName = cursor.getString(cursor.getColumnIndex(UserInfo.USER_NAME));
				String token = cursor.getString(cursor.getColumnIndex(UserInfo.TOKEN));
				String isDefault = cursor.getString(cursor.getColumnIndex(UserInfo.IS_DEFAULT));
				byte[] byteIcon = cursor.getBlob(cursor.getColumnIndex(UserInfo.USER_ICON));
				
				userInfo.setId(id);
				userInfo.setUserId(uId);
				userInfo.setIsDefault(isDefault);
				userInfo.setToken(token);
				userInfo.setUserName(userName);
				
				if(null !=byteIcon)
				{
					Bitmap userIcon=BitmapFactory.decodeByteArray(byteIcon, 0, byteIcon.length);
					Log.i("OUTPUT","dbuserinfo"+userIcon);
					userInfo.setUserIcon(userIcon);
				}
				users.add(userInfo);
			}
		db.close();
		return users;
	}
}
