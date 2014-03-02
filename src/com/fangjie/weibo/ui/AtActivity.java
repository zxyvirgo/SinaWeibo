package com.fangjie.weibo.ui;

import com.fangjie.weibo.R;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

public class AtActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.at);
		Toast.makeText(AtActivity.this, "对不起，还没开发好！敬请期待！-by @方杰_Jay", Toast.LENGTH_LONG).show();
	}
}
