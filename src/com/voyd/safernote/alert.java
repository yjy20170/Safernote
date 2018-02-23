package com.voyd.safernote;

import android.widget.Toast;

public class alert {
	public alert(String string){
		Toast.makeText(MyApplication.context, string, Toast.LENGTH_SHORT).show();
	}
	public alert(String string,String flag){
		if(flag.equals("long")){
			Toast.makeText(MyApplication.context, string, Toast.LENGTH_LONG).show();
		}
	}
}
