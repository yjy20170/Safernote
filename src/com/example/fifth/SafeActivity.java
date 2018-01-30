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
		
		//外观
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
			//假如是从栈中返回产生的Restart（而不是按home键等），
			//则该activity必位于栈顶，下次Restart必由home键等产生
			isFromStack = false;
		}
	}
	
	//所有 startActivity(Intent) 和 startActivityForResult(Intent, int) 应改为 startSafeActivity
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
		if(requestCode==0){//由上面打开activity_lock时的startActivityForResult(intent, 0)决定
			isFromStack=true;
		}
	}
}
