package com.fangjie.weibo.bean;

import android.graphics.Bitmap;

public class UserInfo
{
	private Long id;
	private String userId;
	private String userName;
	private String token;
	private String isDefault;
	private Bitmap  userIcon;
	private Boolean isV;
	
	/**
	 * 这里常量用于创建数据库的表的栏目
	 * @author 方杰
	 * @date 2013-7-29
	 */
	public static final String TB_NAME="UserInfo";
	
	public static final String ID="_id";
	public static final String USER_ID="userId";
	public static final String USER_NAME="userName";

	public static final String TOKEN="token";
	public static final String IS_DEFAULT="isDefault";
	public static final String USER_ICON="userIcon";
	
	public UserInfo(String userId, String userName, String token
										, String isDefault)
	{
		this.userId = userId;
		this.userName = userName;
		this.token = token;
		this.isDefault = isDefault;
	}	
	public UserInfo() {
		super();
		this.userName="";
	}

	public Long getId()
	{
		return id;
	}
	public void setId(Long id)
	{
		this.id = id;
	}
	public String getUserId()
	{
		return userId;
	}
	public void setUserId(String userId)
	{
		this.userId = userId;
	}
	public String getUserName()
	{
		return userName;
	}
	public void setUserName(String userName)
	{
		this.userName = userName;
	}
	public String getToken()
	{
		return token;
	}
	public void setToken(String token)
	{
		this.token = token;
	}
	public String getIsDefault()
	{
		return isDefault;
	}
	public void setIsDefault(String isDefault)
	{
		this.isDefault = isDefault;
	}
	public Bitmap getUserIcon()
	{
		return userIcon;
	}
	public void setUserIcon(Bitmap userIcon)
	{
		this.userIcon = userIcon;
	}	
	public Boolean getIsV() {
		return isV;
	}
	public void setIsV(Boolean isV) {
		this.isV = isV;
	}
}
