package com.fangjie.weibo.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fangjie.weibo.R;
import com.fangjie.weibo.bean.Task;
import com.fangjie.weibo.bean.UserInfo;
import com.fangjie.weibo.logic.MainService;
import com.fangjie.weibo.util.GetUserInfo;
import com.fangjie.weibo.util.SharePreferencesUtil;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;

/**
 * 登录界面
 * 
 * @author 方杰
 *
 */
public class LoginActivity extends Activity implements IWeiboAcitivity{
   
    private Button addcountBtn,loginbtn;
    private Spinner name;
    private ImageView icon;
    @SuppressWarnings("rawtypes")
	private ArrayAdapter adapter;			
    private List<UserInfo> users;
	
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        
        //当前如果有登录用户就直接登录
        if(null!=SharePreferencesUtil.getLoginUser(this))
        {
        	Intent intent=new Intent(LoginActivity.this,MainActivity.class);
        	startActivity(intent);
        	finish();
        }
        
		//初始化登录界面
		init();       
		
		/*
		 * 开启任务：GET_USERINFO_IN_LOGIN
		 * 从数据库中获取授权用户信息
		 */
		Task task_getuser=new Task(Task.GET_USERINFO_IN_LOGIN,null);
		MainService.newTask(task_getuser);
		MainService.addActivty(LoginActivity.this);

    }
	@Override
	public void init() {
		
        name=(Spinner)findViewById(R.id.spinner_user_list);
		icon=(ImageView)findViewById(R.id.image_head);
        addcountBtn = (Button) findViewById(R.id.btn_add_account);           
        loginbtn = (Button) findViewById(R.id.btn_login); 
        
        //增加用户，进行授权（按钮监听）
        addcountBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(LoginActivity.this, AuthActivity.class);
                startActivity(intent);
                finish();
            }
        });
        
        //登录按钮监听
        loginbtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				
				UserInfo LoginUser=users.get(name.getSelectedItemPosition());
				//Log.i("OUTPUT","当前登录的用户是："+LoginUser.getUserName());
				/**
				 * 新开任务：WEIBO_LOGIN
				 * 登录Spinner中显示的用户
				 */
				Map<String,Object> params=new HashMap<String,Object>();
				params.put("LoginUser", LoginUser);
				Task task =new Task(Task.WEIBO_LOGIN,params);
				MainService.newTask(task);
				MainService.addActivty(LoginActivity.this);
			}
		});
        
        //Spinner选择用户监听，选择不同的用户显示相应的用户头像
        name.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				icon.setImageBitmap(GetUserInfo.scaleImg(users.get(position).getUserIcon() , 200, 200));
			}
			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub
				
			}
		});
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void refresh(int taskID,Object... objects) {
		// TODO Auto-generated method stub
		switch(taskID)
		{
			//获取到当前所有授权用户信息后
			case Task.GET_USERINFO_IN_LOGIN:
				users=(List<UserInfo>)objects[0];
				//当前有授权的用户
				if(users.size()>0)
				{
					List<String> list=new ArrayList<String>();
					for( UserInfo user:users)
					{
						Log.i("OUTPUT","当前已经授权的帐号："+user.getUserName());
						list.add(user.getUserName());
						Log.i("OUTPUT","list："+list.size());
					}	
					adapter=new ArrayAdapter(this,android.R.layout.simple_spinner_item,list);
					adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);	
					adapter.notifyDataSetChanged();
					name.setAdapter(adapter);	
					icon.setImageBitmap(GetUserInfo.scaleImg(users.get(0).getUserIcon() , 200, 200));
				}
				//当前没有授权用户时，自动跳转到授权界面
				else
				{
	                Intent intent=new Intent(LoginActivity.this, AuthActivity.class);
	                startActivity(intent);
	                finish();
				}
				MainService.reMoveActivty(LoginActivity.this);
				break;
				
			//登录任务完成时
			case Task.WEIBO_LOGIN:	
				MainService.reMoveActivty(LoginActivity.this);
				Intent intent =new Intent(LoginActivity.this,MainActivity.class);
				startActivity(intent);
				finish();
				break;
		}
	}
}
