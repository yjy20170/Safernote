package com.example.fifth;

import android.content.Context;
import android.widget.Toast;

public class alert {
	public alert(Context context, String string){
		Toast.makeText(context, string, Toast.LENGTH_SHORT).show();
	}
}
