package com.fangjie.weibo.logic;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

import com.fangjie.weibo.bean.*;
import com.fangjie.weibo.db.DBUserInfo;
import com.fangjie.weibo.ui.IWeiboAcitivity;
import com.fangjie.weibo.util.GetUserInfo;
import com.fangjie.weibo.util.SharePreferencesUtil;
import com.fangjie.weibo.util.WeiboUtil;
import com.weibo.sdk.android.Oauth2AccessToken;

public class MainService extends Service implements Runnable{

	private static Queue<Task> tasks=new LinkedList<Task>();
	private static ArrayList<Activity> appActivities=new ArrayList<Activity>();
	
	private boolean isRun;
	private Handler handler;
	
	@SuppressLint("HandlerLeak")
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub	
		/**
		 * 在MainService中开启第二线程来处理任务
		 * 主要是不断监听Tasks堆栈，从Tasks中处理task
		 */
		isRun=true;		
		Thread thread =new Thread(this);
		thread.start();
		super.onCreate();
		
		/**
		 * 主要是获取从MainService第二线程（doTask）中获取处理完任务的数据
		 * 并通知更新UI界面
		 */
		handler =new  Handler(){
			public void handleMessage(Message msg)
			{
				IWeiboAcitivity activity=null;
				switch(msg.what)
				{
					case Task.WEIBO_LOGIN:
						activity=(IWeiboAcitivity)getActivityByName("LoginActivity");
						activity.refresh(Task.WEIBO_LOGIN,msg.obj);
						break;
					case Task.GET_USERINFO_BY_TOKEN:
						activity=(IWeiboAcitivity)getActivityByName("AuthActivity");
						activity.refresh(Task.GET_USERINFO_BY_TOKEN,msg.obj);
						break;		
					case Task.GET_USERINFO_IN_LOGIN:
						activity=(IWeiboAcitivity)getActivityByName("LoginActivity");
						activity.refresh(Task.GET_USERINFO_IN_LOGIN,msg.obj);
						break;		
					case Task.GET_WEIBOS:
						activity=(IWeiboAcitivity)getActivityByName("HomeActivity");
						activity.refresh(Task.GET_WEIBOS,msg.obj);
						break;
					case Task.LOADMORE:
						activity=(IWeiboAcitivity)getActivityByName("HomeActivity");
						activity.refresh(Task.LOADMORE,msg.obj);
						break;
					case Task.UPDATE_WEIBO:
						activity=(IWeiboAcitivity)getActivityByName("UpdateWeibo");
						activity.refresh(Task.UPDATE_WEIBO,msg.obj);
						break;
					case Task.GUANZHU:
						activity=(IWeiboAcitivity)getActivityByName("MoreActivity");
						activity.refresh(Task.GUANZHU,msg.obj);
					default:
						break;
				}
			}
		};

	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		while(isRun)
		{
			if(!tasks.isEmpty())
			{
				doTask(tasks.poll());
			}
		}
	}
	
	/**
	 * UI层向MainService中发送任务task
	 * UI层调用，所以static
	 * @param task
	 */
	public static void newTask(Task task)
	{
		tasks.add(task);
	}
	
	/**
	 * UI层向MainService中发送任务的同时，同样要发送自己Activity，
	 * 以便在MainService中调用refresh();
	 * @param task
	 */
	public static void addActivty(Activity activity)
	{
		appActivities.add(activity);
	}
	/**
	 * UI层中有向MainService中传递Activity，在任务结束后（refresh），应去除该Activity，防止混淆！
	 * eg：在LoginActivity中的GET_USERINFO_IN_LOGIN任务会随Activity的每次重新加载而开始一次，如果不去除
	 * activity的话，会导致本次refresh的是上次的activity的实例。因为获取activity是通过getActivityByName，这两
	 * 次的activity实例的name是相同的。
	 * @param activity
	 */
	public static void reMoveActivty(Activity activity)
	{
		appActivities.remove(activity);
	}
	
	/**
	 * 通过name获取新开任务时传递过来的Activity实例
	 * @param name
	 * @return
	 */
	public Activity getActivityByName(String name)
	{
		if(!appActivities.isEmpty())
		{
			for(Activity activity:appActivities)
			{
				if(activity.getClass().getName().indexOf(name)>0)
				{
					return activity;
				}
			}
		}
		return null;
	}

	/**
	 * 处理Tasks堆栈中的task
	 * @param task
	 */
	public void doTask(Task task)
	{
		Message msg=handler.obtainMessage();
		msg.what=task.getTaskID();
		
		switch(task.getTaskID())
		{
			//登录操作
			case Task.WEIBO_LOGIN:
				UserInfo LoginUser=(UserInfo)(task.getParams().get("LoginUser"));
				Context context=getActivityByName("LoginActivity");
				SharePreferencesUtil.SaveLoginUser(context, LoginUser);
				msg.obj="成功";
				break;
				
			//通过access_token获取用户信息，并保存到数据库操作	
			case Task.GET_USERINFO_BY_TOKEN:
			{
				    Oauth2AccessToken access_token=(Oauth2AccessToken)task.getParams().get("token");
					Log.i("OUTPUT", "access_token:"+access_token.getToken());
					/**
					 * 步骤一：用授权码来获取用户Uid
					 */
					//请求获取uid
		        	String uid="";
		        	GetUserInfo.reqUID(access_token);
		        	//获取uid
		        	do{
		        		uid=GetUserInfo.getUID();
		        	}while(uid.equals(""));
		        	/**
		        	 * 步骤二：通过uid，token获取UserInfo
		        	 */
		        	//请求获取用户信息
		        	long _uid=Long.parseLong(uid);
		        	UserInfo user=new UserInfo();
		        	GetUserInfo.reqUserInfo(access_token, _uid);
		        	//获取UserInfo
		        	do{
		        		user=GetUserInfo.getUserInfo();
		        	}while(user.getUserName().equals(""));		        	
		        	user.setUserId(uid);		        
		        	Log.i("OUTPUT","获取用户信息成功   username:"+user.getUserName()+"userid+"+user.getUserId()+"usericon"+user.getUserIcon());
		        	
		        	/**
		        	 * 步骤三：把UserInfo的数据保存到数据库总
		        	 */
		        	//创建数据库
		        	DBUserInfo db=new DBUserInfo(getActivityByName("AuthActivity"));
		        	//如果该数据不存在数据库中
		        	if(db.getUserInfoByUserId(uid)==null)
		        	{
		        		//插入
		        		db.insertUserInfo(user);    
		        		Log.i("OUTPUT","保存数据库成功");
		        	}
		        	//如果该数据已经存在数据库中
		        	else
		        	{
		        		//修改
		        		Log.i("OUTPUT","数据修改成功");
		        	}       
		        	msg.obj="成功";
				}
				break;
				
			//登录界面获取用户信息显示操作
			case Task.GET_USERINFO_IN_LOGIN:
			{
	        	DBUserInfo db=new DBUserInfo(getActivityByName("LoginActivity"));
	        	List<UserInfo> users=db.getAllUserInfo();
	        	msg.obj=users;
	        	break;
			}
			//刷新微博
			case Task.GET_WEIBOS:
			{
				String token=(String)task.getParams().get("token");
				WeiboUtil weiboutil=new WeiboUtil();
				List<Weibo> weibos=weiboutil.getWeiboList(token,0);
				msg.obj=weibos;
				break;
			}
			//加载更多
			case Task.LOADMORE:
			{
				String token=(String)task.getParams().get("token");
				long max_id=(Long) task.getParams().get("max_id");
				WeiboUtil weiboutil=new WeiboUtil();
				List<Weibo> weibos=weiboutil.getWeiboList(token,max_id);
				msg.obj=weibos;
				break;
			}
			//发微博
			case Task.UPDATE_WEIBO:
			{
				String token=(String)task.getParams().get("token");
				String text=(String)task.getParams().get("text");
				WeiboUtil weiboutil=new WeiboUtil();
				if(weiboutil.update(token, text)==0)
				{
					msg.obj=0;	
				}
				else
				{
					msg.obj=1;
				}
				break;
			}
			//关注@方杰_Jay
			case Task.GUANZHU:
			{
				String token=(String)task.getParams().get("token");
				WeiboUtil weiboutil=new WeiboUtil();
				switch(weiboutil.guanzhuMe(token))
				{
					case 0:msg.obj=0;
						break;
					case 1:msg.obj=1;
						break;
					case 2:msg.obj=2;
						break;
					case 3:msg.obj=3;
						break;
				}
				break;				
			}
			default :
				break;
		}
		handler.sendMessage(msg);
	}

	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
}