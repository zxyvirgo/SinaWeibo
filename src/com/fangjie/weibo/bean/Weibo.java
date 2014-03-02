package com.fangjie.weibo.bean;

public class Weibo {

	public long wid;
	public String content;
	public String from;
	public String time;
	public String bmiddle_pic;
	public int reposts_count;
	public int comments_count;
	public Weibo weibo;
	public User user;
	
	public long getWid() {
		return wid;
	}
	public void setWid(long wid) {
		this.wid = wid;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getFrom() {
		return from;
	}
	public void setFrom(String from) {
		this.from = from;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getBmiddle_pic() {
		return bmiddle_pic;
	}
	public void setBmiddle_pic(String bmiddle_pic) {
		this.bmiddle_pic = bmiddle_pic;
	}
	public int getReposts_count() {
		return reposts_count;
	}
	public void setReposts_count(int reposts_count) {
		this.reposts_count = reposts_count;
	}
	public int getComments_count() {
		return comments_count;
	}
	public void setComments_count(int comments_count) {
		this.comments_count = comments_count;
	}
	public Weibo getWeibo() {
		return weibo;
	}
	public void setWeibo(Weibo weibo) {
		this.weibo = weibo;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	
}
