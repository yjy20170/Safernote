package com.example.fifth;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

public class SafeActivity extends Activity{
	public static String password;
	protected boolean isFromStack=false;
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		
		//���
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
	}
	
	@Override
	protected void onRestart(){//Stop
		super.onRestart();
		if(!isFromStack){
			isFromStack = true;
			Intent intent = new Intent(this,activity_locked.class);
			intent.putExtra("isOnAppStart", false);
			startActivityForResult(intent, 0);
		}else{
			//�����Ǵ�ջ�з��ز�����Restart�������ǰ�home���ȣ���
			//���activity��λ��ջ�����´�Restart����home���Ȳ���
			isFromStack = false;
		}
	}
	
	//���� startActivity(Intent) �� startActivityForResult(Intent, int) Ӧ��Ϊ startSafeActivity
	protected void startSafeActivity(Intent intent){
		isFromStack = true;
		startActivity(intent);
	}
	protected void startSafeActivityForResult(Intent intent,int requestCode){
		isFromStack = true;
		startActivityForResult(intent,requestCode);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent intent){
		if(requestCode==0){//�������activity_lockʱ��startActivityForResult(intent, 0)����
			isFromStack=true;
		}
	}
}
