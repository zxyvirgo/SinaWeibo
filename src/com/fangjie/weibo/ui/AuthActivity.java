package com.fangjie.weibo.ui;

import java.util.HashMap;
import java.util.Map;

import com.weibo.sdk.android.Oauth2AccessToken;
import com.fangjie.weibo.R;
import com.fangjie.weibo.bean.Task;
import com.fangjie.weibo.logic.MainService;
import com.fangjie.weibo.util.AuthUtil;




import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;


public class AuthActivity extends Activity implements IWeiboAcitivity{
    private Button btn_auth;
	private Dialog dialog;
	private Oauth2AccessToken access_token;
	private AuthUtil util;
	private ProgressDialog progressDialog;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.auth);

		util=new AuthUtil(AuthActivity.this);
		access_token=util.getAccessToken();

		/**
		 * 采用access_token判断是开始授权还是授权成功回调
		 * 1.开始授权就显示授权提醒的对话框
		 * 2.授权回调就做获取用户信息然后保存数据库的操作
		 * 
		 * 缺陷：这地方设计的不是
		 */
		//授权完成后重新跳转到该Activity
		if(access_token!=null){
			//获取授权码
			Log.i("OUTPUT","OAuth授权完成，已经获取到token："+access_token.getToken());

			progressDialog=new ProgressDialog(this);
			progressDialog.setMessage("正在获取用户信息，请稍候...");
			progressDialog.show();
			/**
			 * 新开任务：GET_USERINFO_BY_TOKEN
			 * 通过授权获得的token获取UserInfo，并保存到数据库中
			 */			
			Map<String,Object> params=new HashMap<String,Object>();
			params.put("token", access_token);
			Task task=new Task(Task.GET_USERINFO_BY_TOKEN,params); 
			MainService.newTask(task);
			MainService.addActivty(AuthActivity.this);	
			
		}
		//授权开始时加载该Activity
		else
		{
			View diaView=View.inflate(this, R.layout.dialog, null);
			dialog=new Dialog(AuthActivity.this,R.style.dialog);
			dialog.setContentView(diaView);
			dialog.show();
			
			btn_auth=(Button)diaView.findViewById(R.id.btn_auth);
			btn_auth.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					//请求授权
					util.reqAccessToken();
				}
			});
		}
    }
	@Override
	public void init() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void refresh(int taskID,Object... objects) {
		// TODO Auto-generated method stub
		if(((String)objects[0]).equals("成功"))
		{
			progressDialog.dismiss();
			
			MainService.reMoveActivty(AuthActivity.this);
			Intent intent=new Intent(AuthActivity.this,LoginActivity.class);
			startActivity(intent);
			finish();
		}
	}
}
