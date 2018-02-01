package com.example.fifth;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class activity_setPassword extends SafeActivity implements OnClickListener{
	private String inputString;
	private TextView tip;
	private PasswordInputer passwordInputer;
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_set_password);
		((Button)findViewById(R.id.finish)).setOnClickListener(this);
		((Button)findViewById(R.id.save)).setOnClickListener(this);
		tip = (TextView)findViewById(R.id.input_password_tip);
		tip.setText("������������");
		inputString = "null";
		passwordInputer = (PasswordInputer)findViewById(R.id.password_inputer);
	}
	@Override
	public void onClick(View v){
		switch(v.getId()){
		case R.id.finish:
			onBackPressed();
			break;
		case R.id.save:
			if(inputString == "null"){
				inputString = passwordInputer.getInput();
				tip.setText("���ٴ�����������");
				//����PasswordInputer
				passwordInputer.reset();
				break;
			}else{
				if(inputString.equals(passwordInputer.getInput())){
					new alert("���óɹ�");
					MyApplication.updatePassword(inputString);
					onBackPressed();
				}else{
					new alert("�������벻һ�£�����������");
					//����PasswordInputer������
					passwordInputer.reset();
					tip.setText("������������");
					inputString = "null";
				}
			}
		}
	}
}
