package com.example.jiao.myapplication.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.example.jiao.myapplication.BaseActivity;
import com.example.jiao.myapplication.R;

/**
 * @author jiao, <wenliang.jiao@extantfuture.com>
 * @date 2017年05月09日
 */
public class LoginActivity extends BaseActivity {

	@Bind(R.id.name)
	EditText name;
	@Bind(R.id.password)
	EditText password;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		ButterKnife.bind(this);
	}

	public void onClick(View view) {
		switch (view.getId()) {
			case R.id.submit:
				Intent intent = new Intent(getApplicationContext(), MainActivity.class);
				intent.putExtra("name", name.getText().toString().trim());
				intent.putExtra("password", password.getText().toString().trim());
				startActivity(intent);
				break;
		}
	}
}
