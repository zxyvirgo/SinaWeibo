package com.fangjie.weibo.ui;

import com.fangjie.weibo.R;
import com.fangjie.weibo.util.SharePreferencesUtil;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

@SuppressWarnings("deprecation")
public class MainActivity extends TabActivity{
	private TabHost tabHost;
	
	private static final String HOME_TAB="home"; 
	private static final String AT_TAB="at"; 
	private static final String MSG_TAB="msg"; 
	private static final String MORE_TAB="more"; 

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

	    tabHost = this.getTabHost();
	    TabSpec homeSpec=tabHost.newTabSpec(HOME_TAB).setIndicator(HOME_TAB).setContent(new Intent(this,HomeActivity.class));
	    TabSpec atSpec=tabHost.newTabSpec(AT_TAB).setIndicator(AT_TAB).setContent(new Intent(this,AtActivity.class));
	    TabSpec msgSpec=tabHost.newTabSpec(MSG_TAB).setIndicator(MSG_TAB).setContent(new Intent(this,MsgActivity.class));
	    TabSpec moreSpec=tabHost.newTabSpec(MORE_TAB).setIndicator(MORE_TAB).setContent(new Intent(this,MoreActivity.class));
	    
	    tabHost.addTab(homeSpec);
	    tabHost.addTab(atSpec);
	    tabHost.addTab(msgSpec);
	    tabHost.addTab(moreSpec);
	    
	    tabHost.setCurrentTabByTag(HOME_TAB);

	    RadioGroup radioGroup =  (RadioGroup) this.findViewById(R.id.main_radio);
	    final RadioButton rb=(RadioButton)this.findViewById(R.id.rb_home);
	    rb.setBackgroundResource(R.drawable.tabhost_press);
	    
	    radioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener()
		{
			public void onCheckedChanged(RadioGroup group, int checkedId)
			{
				rb.setBackgroundResource(R.drawable.tabhost_bg_selector);
				switch (checkedId)
				{
					case R.id.rb_home:
						tabHost.setCurrentTabByTag(HOME_TAB);
						break;
						
					case R.id.rb_at:
						tabHost.setCurrentTabByTag(AT_TAB);
						break;
						
					case R.id.rb_mess:
						tabHost.setCurrentTabByTag(MSG_TAB);
						break;
						
					case R.id.rb_more:
						tabHost.setCurrentTabByTag(MORE_TAB);
						break;
						
					default:
						break;
				}
			}
		});
	    
	}

	
	public boolean onCreateOptionsMenu(Menu menu) {
	     /*
         * add()方法的四个参数，依次是：
         * 1、组别，如果不分组的话就写Menu.NONE,
         * 2、Id，这个很重要，Android根据这个Id来确定不同的菜单
         * 3、顺序，那个菜单现在在前面由这个参数的大小决定
         * 4、文本，菜单的显示文本
         */
        menu.add(Menu.NONE, Menu.FIRST +1, 5, "退出微博");
        return true;
	}

	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId())
		{
	        case Menu.FIRST + 1:
	            SharePreferencesUtil.removeLoginUser(MainActivity.this);
	        	Intent intent=new Intent(MainActivity.this,LoginActivity.class);
	        	startActivity(intent);
	        	finish();
	            break;
		}
		return false;
	}
}
