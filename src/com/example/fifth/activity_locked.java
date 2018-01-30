package com.example.fifth;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

public class activity_locked extends Activity implements OnClickListener{
	private String MD5password;
	private TextView input;
	private String inputString;
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.layout_locked);
		
		MD5password = Settings.getMD5Password(this);
		
		input = (TextView) findViewById(R.id.input_password);
		inputString = "";
		findViewById(R.id.submit_password).setOnClickListener(this);
		findViewById(R.id.pw_1).setOnClickListener(this);
		findViewById(R.id.pw_2).setOnClickListener(this);
		findViewById(R.id.pw_3).setOnClickListener(this);
		findViewById(R.id.pw_4).setOnClickListener(this);
		findViewById(R.id.pw_5).setOnClickListener(this);
		findViewById(R.id.pw_6).setOnClickListener(this);
		findViewById(R.id.pw_7).setOnClickListener(this);
		findViewById(R.id.pw_8).setOnClickListener(this);
		findViewById(R.id.pw_9).setOnClickListener(this);
		findViewById(R.id.pw_point).setOnClickListener(this);
		findViewById(R.id.pw_0).setOnClickListener(this);
		findViewById(R.id.pw_delete).setOnClickListener(this);
	}
	@Override
	public void onBackPressed() {
		
	}
	@Override
	public void onClick(View v){
		switch(v.getId()){
		case R.id.submit_password:
			String MD5inputString = MD5Util.MD5(inputString);
			if(MD5inputString.equals(MD5password)){
				//onRestart触发的
				if(!getIntent().getBooleanExtra("isOnAppStart",true)){
					finish();
				}else{//启动app时
					//将password储存到静态变量中
					SafeActivity.password = inputString;
					
					startActivity(new Intent(this,activity_1.class));
					finish();
				}
			}else{
				Toast.makeText(activity_locked.this, "Wrong password!", Toast.LENGTH_SHORT).show();
				inputString = "";
				addInput("");
			}
			break;
		case R.id.pw_1:
			addInput("1");
			break;
		case R.id.pw_2:
			addInput("2");
			break;
		case R.id.pw_3:
			addInput("3");
			break;
		case R.id.pw_4:
			addInput("4");
			break;
		case R.id.pw_5:
			addInput("5");
			break;
		case R.id.pw_6:
			addInput("6");
			break;
		case R.id.pw_7:
			addInput("7");
			break;
		case R.id.pw_8:
			addInput("8");
			break;
		case R.id.pw_9:
			addInput("9");
			break;
		case R.id.pw_point:
			addInput(".");
			break;
		case R.id.pw_0:
			addInput("0");
			break;
		case R.id.pw_delete:
			if(inputString.length()>0){
				inputString=inputString.substring(0, inputString.length() - 1);
				addInput("");
			}
			break;
		default:
		}
	}
	
	private void addInput(String c){
		if(inputString.length()+c.length()>16){
			Toast.makeText(activity_locked.this, "Max length is 16!", Toast.LENGTH_SHORT).show();
		}else{
			inputString += c;
			int n = inputString.length();
			String s="";
			for(int i=0;i<n;i++){
				s+="•";
			}
			input.setText(s);
		}
	}
}
