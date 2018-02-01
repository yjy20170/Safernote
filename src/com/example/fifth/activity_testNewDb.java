package com.example.fifth;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;


public class activity_testNewDb extends SafeActivity implements OnClickListener{
	private TextView tip;
	private PasswordInputer passwordInputer;
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_set_password);//只是借用布局，逻辑和activity_setPassword不同
		((Button)findViewById(R.id.finish)).setOnClickListener(this);
		((Button)findViewById(R.id.save)).setOnClickListener(this);
		tip = (TextView)findViewById(R.id.input_password_tip);
		tip.setText("验证导入数据的密码");
		passwordInputer = (PasswordInputer)findViewById(R.id.password_inputer);
	}
	@Override
	public void onClick(View v){
		switch(v.getId()){
		case R.id.save:
			String MD5inputString = MD5Util.MD5(passwordInputer.getInput());
			if(MD5inputString.equals(getIntent().getStringExtra("MD5Password"))){
				String passwordOfImportDb = passwordInputer.getInput();
				new alert("导入数据的密码为 "+passwordOfImportDb);
				//TODO 弹出对话框，询问是否导出原数据库
				
				//TODO 合并两个数据库？
			}else{
				new alert("密码错误！");
				passwordInputer.reset();
			}
			break;
		case R.id.finish:
			onBackPressed();
		}
	}
}