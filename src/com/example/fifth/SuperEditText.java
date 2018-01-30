package com.example.fifth;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.view.View.OnClickListener;

public class SuperEditText extends EditText implements OnClickListener{
	private activity_2 activity;
	public SuperEditText(Context context, AttributeSet attrs) {
		super(context, attrs);
		activity = (activity_2)context;
		this.setBackground(null);
		this.setFocusable(false);
		this.setFocusableInTouchMode(false);
		this.setOnClickListener(this);
		this.addTextChangedListener(new TextWatcher(){
			@Override
			public void beforeTextChanged(CharSequence s, int start, int before, int count){
			}
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
			}
			@Override
			public void afterTextChanged(Editable s) {
				boolean isChanged = activity.isChanged();
				if(activity.viewType==3	&& !isChanged){
					activity.setViewType(null, 2);
				}
				if(activity.viewType==2 && isChanged){
					activity.setViewType(null, 3);
				}
			}
		});
	}
	@Override
	public void onClick(View v){
		activity_2 activity = SuperEditText.this.activity;
		int viewType = activity.viewType;
		activity.lastFocus = SuperEditText.this;
		if(viewType==0||viewType==1){
			//0 -> 2 ; 1 -> 3
			activity.setViewType(SuperEditText.this, viewType+2);
		}else{
			//仅仅改变当前焦点的属性和重新聚焦到点击位置
			View currentFocus = activity.getCurrentFocus();
			if(currentFocus != SuperEditText.this){
				SuperEditText.this.setFocusable(true);
				SuperEditText.this.setFocusableInTouchMode(true);
				SuperEditText.this.requestFocus();
				SuperEditText.this.clearFocus();
				currentFocus.setFocusable(false);
				currentFocus.setFocusableInTouchMode(false);
			}else{
				SuperEditText.this.clearFocus();
			}
		}
	}
	@Override
    public boolean onKeyPreIme(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
        	//2 -> 0 ; 3 -> 1
        	activity.setViewType(this, activity.viewType - 2);
        	return true;
        }
        return super.onKeyPreIme(keyCode, event);
    }
	
	
    
}
