package com.example.jiao.myapplication.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.example.jiao.myapplication.R;

/**
 * Created by jiao on 16/8/1.
 */
public class MainActivity extends AppCompatActivity {

	@Bind(R.id.result)
	TextView textView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		ButterKnife.bind(this);
		String name = getIntent().getStringExtra("name");
		String password = getIntent().getStringExtra("password");
		if ("android".equals(name) && "123456".equals(password)) {
			textView.setText("登录成功");
		} else {
			textView.setText("登录失败");
		}
	}

}