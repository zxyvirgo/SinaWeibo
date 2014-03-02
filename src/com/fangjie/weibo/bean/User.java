package com.fangjie.weibo.bean;

public class User {
	public long uid;
	public String name;
	public boolean isv;
	public String profile_image_url;
	public long getUid() {
		return uid;
	}
	public void setUid(long uid) {
		this.uid = uid;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public boolean isIsv() {
		return isv;
	}
	public void setIsv(boolean isv) {
		this.isv = isv;
	}
	public String getProfile_image_url() {
		return profile_image_url;
	}
	public void setProfile_image_url(String profile_image_url) {
		this.profile_image_url = profile_image_url;
	}
	
}
