package com.example.fifth;

import android.widget.Toast;

public class alert {
	public alert(String string){
		Toast.makeText(MyApplication.context, string, Toast.LENGTH_SHORT).show();
	}
}
