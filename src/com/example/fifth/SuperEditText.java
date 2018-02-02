package com.example.fifth;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;

public class SuperEditText extends EditText
		implements OnClickListener,OnTouchListener{
	private activity_2 activity;
	public SuperEditText(Context context, AttributeSet attrs) {
		super(context, attrs);
		activity = (activity_2)context;
		this.setBackground(null);
		//this.setFocusable(false);
		//this.setFocusableInTouchMode(false);
		this.setCursorVisible(false);
		this.setOnClickListener(this);
		this.setOnTouchListener(this);
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
					new alert("right here");
					activity.setViewType(null, 2);
				}
				if(activity.viewType==2 && isChanged){
					activity.setViewType(null, 3);
				}
			}
		});
	}
	@Override
	public boolean onTouch(View v, MotionEvent event){
		if(activity.getCurrentFocus()!=this){
			boolean result = super.onTouchEvent(event);
			if(activity.getCurrentFocus()==this){
				onClick(null);
			}
			return result;
		}else{
			return super.onTouchEvent(event);
		}
	}
	@Override
	public void onClick(View v){
		activity_2 activity = SuperEditText.this.activity;
		int viewType = activity.viewType;
		if(viewType==0||viewType==1){
			//0 -> 2 ; 1 -> 3
			setCursorVisible(true);
			activity.setViewType(SuperEditText.this, viewType+2);
		}else{
			//仅仅改变当前焦点的属性和重新聚焦到点击位置
			if(activity.lastFocus != null
					&& activity.lastFocus.equals(SuperEditText.this)){
				activity.lastFocus.setCursorVisible(false);
			}
			setCursorVisible(true);
		}
		activity.lastFocus = SuperEditText.this;
		//return super.onTouchEvent(event);
	}
	@Override
    public boolean onKeyPreIme(int keyCode, KeyEvent event) {
		if(event.getAction()==KeyEvent.ACTION_DOWN
				&& keyCode == KeyEvent.KEYCODE_BACK){
        	//2 -> 0 ; 3 -> 1
        	leaveEdit();
        	if(activity.viewType==2||activity.viewType==3){
        		activity.setViewType(this, activity.viewType - 2);
        	}
        	clearFocus();
        	//防止自动聚焦重新将this设为焦点
        	activity.findViewById(R.id.blank_view).requestFocus();
        	//必须返回true，否则在关闭输入法的情况下点击事件将继续传播，被activity_2.onBackPressed()捕获
        	return true;
	    }
		return super.onKeyPreIme(keyCode, event);
    }
	
	public void leaveEdit(){
		setCursorVisible(false);
		InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
		if (inputMethodManager != null && activity.getCurrentFocus() != null) {
		inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(),
		InputMethodManager.HIDE_NOT_ALWAYS);
		}
	}
	public void enterEdit(){
		requestFocus();
		setCursorVisible(true);
		InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
		inputMethodManager.toggleSoftInput(0, InputMethodManager.SHOW_FORCED);
	}
}
