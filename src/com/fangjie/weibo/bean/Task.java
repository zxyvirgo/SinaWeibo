package com.fangjie.weibo.bean;

import java.util.Map;

/**
 * Task实体类
 * 
 * @author 方杰
 * @date 2013-7-28
 */
public class Task {

	//任务ID
	private int taskID;
	//任务参数
	private Map<String ,Object> Params;
	
	//登陆的任务ID
	public static final int WEIBO_LOGIN =1;
	//通过授权码获取用户信息，并保存到数据库中
	public static final int GET_USERINFO_BY_TOKEN=2;
	//登录界面获取用户信息
	public static final int GET_USERINFO_IN_LOGIN=3;
	//获取首页微博
	public static final int GET_WEIBOS = 4;
	//加载更多
	public static final int LOADMORE = 5;
	//发微博
	public static final int UPDATE_WEIBO = 6;
	//关注@方杰_Jay
	public static final int GUANZHU = 0;
	
	//构造方法
	public Task(int taskID,Map<String ,Object> Params)
	{
		this.Params=Params;
		this.taskID=taskID;
	}

	public int getTaskID() {
		return taskID;
	}

	public void setTaskID(int taskID) {
		this.taskID = taskID;
	}

	public Map<String, Object> getParams() {
		return Params;
	}

	public void setParams(Map<String, Object> params) {
		Params = params;
	}
	
	
}
