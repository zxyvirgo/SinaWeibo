package com.fangjie.weibo.ui;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fangjie.weibo.R;
import com.fangjie.weibo.bean.Task;
import com.fangjie.weibo.bean.Weibo;
import com.fangjie.weibo.logic.MainService;
import com.fangjie.weibo.util.SharePreferencesUtil;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class HomeActivity extends Activity implements IWeiboAcitivity {
	private ListView weibolist;
	private List<Weibo> weibos;
	private WeiboAdapter adapter;
	
	private TextView tv_title;
	private Button btn_refresh;
	private Button btn_update;

	private LinearLayout progress;
	
	private Button loadMoreButton;
	
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.home);		
		init();
	}
	
	public void init() {
		weibolist=(ListView)findViewById(R.id.lv_weibos);
		tv_title=(TextView)findViewById(R.id.txt_wb_title);
		btn_refresh=(Button)findViewById(R.id.btn_refresh);
		btn_update=(Button)findViewById(R.id.btn_writer);
		progress=(LinearLayout)findViewById(R.id.layout_progress);
		
		//设置列表底部视图-加载更多
		View loadMoreView = getLayoutInflater().inflate(R.layout.load_more, null);  
		loadMoreButton= (Button) loadMoreView.findViewById(R.id.loadMoreButton);  
        weibolist.addFooterView(loadMoreView);   
		
        weibolist.setFadingEdgeLength(0);
        
		final String token=SharePreferencesUtil.getLoginUser(HomeActivity.this).getToken();
		tv_title.setText(SharePreferencesUtil.getLoginUser(HomeActivity.this).getUserName());
		
		Map<String,Object> params=new HashMap<String,Object>();
		params.put("token", token);
		Task task=new Task(Task.GET_WEIBOS, params);
		progress.setVisibility(View.VISIBLE);
		MainService.newTask(task);
		MainService.addActivty(HomeActivity.this);

		loadMoreButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Map<String,Object> params=new HashMap<String,Object>();
				params.put("token", token);
				params.put("max_id", weibos.get(weibos.size()-1).getWid());
				loadMoreButton.setText("正在加载，请稍候...");
				Task task=new Task(Task.LOADMORE, params);
				MainService.newTask(task);
				MainService.addActivty(HomeActivity.this);				
			}
		});
		
		btn_refresh.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Map<String,Object> params=new HashMap<String,Object>();
				params.put("token", token);
				progress.setVisibility(View.VISIBLE);
				Task task=new Task(Task.GET_WEIBOS, params);
				MainService.newTask(task);
				MainService.addActivty(HomeActivity.this);	
			}
		});

		btn_update.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Toast.makeText(HomeActivity.this, "亲，程序猿还没写好呢...", Toast.LENGTH_LONG).show();
			}
		});
	}

	@SuppressWarnings("unchecked")
	public void refresh(int taskID, Object... objects) {
		switch (taskID)
		{
			case Task.GET_WEIBOS:
				weibos=(List<Weibo>)objects[0];
				adapter=new WeiboAdapter(HomeActivity.this,weibos);
				weibolist.setAdapter(adapter);				
				break;
			case Task.LOADMORE:
				weibos=(List<Weibo>)objects[0];
				for(int i=1;i<weibos.size();i++)
					adapter.addItem(weibos.get(i));
				adapter.notifyDataSetChanged(); //数据集变化后,通知adapter 
				loadMoreButton.setText("加载更多");
		}
		progress.setVisibility(View.GONE);
		MainService.reMoveActivty(HomeActivity.this);
	}
}
