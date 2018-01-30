package com.example.fifth;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

public class activity_2 extends SafeActivity implements OnClickListener{
	private Button finish;
	private SuperEditText titleView;
	private Button cancel;
	private Button save;
	private SuperEditText tagsView;
	private Button delete;
	private SuperEditText contentView;
	public static SimpleDateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	//0：无键盘无按钮  1：无键盘有按钮  2：有键盘无按钮  3：有键盘有按钮
	public int viewType;	
	Item item;
	int itemPosition;
	
	public SuperEditText lastFocus;
	
	private void setTwoBackground(View v, int id){
		GradientDrawable pressed = (GradientDrawable)getResources().getDrawable(R.drawable.activity_2_titlebar_btn_style);
		Drawable normal = getResources().getDrawable(id);
		StateListDrawable stateListDrawable = new StateListDrawable();
        stateListDrawable.addState(new int[]{android.R.attr.state_pressed}, pressed);
        stateListDrawable.addState(new int[]{}, normal);
        v.setBackground(stateListDrawable);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_2);

		//绑定View
		finish = (Button)findViewById(R.id.finish);
		titleView = (SuperEditText)findViewById(R.id.title);
		cancel = (Button)findViewById(R.id.cancel);
		save = (Button)findViewById(R.id.save);
		tagsView = (SuperEditText)findViewById(R.id.tags);
		delete = (Button)findViewById(R.id.delete);
		contentView = (SuperEditText)findViewById(R.id.content);

		//设置按钮样式
		setTwoBackground(finish, R.drawable.back);
		setTwoBackground(cancel, R.drawable.cancel);
		setTwoBackground(save, R.drawable.save);
		
		//绑定OnClickListener!!!
		finish.setOnClickListener(this);
		cancel.setOnClickListener(this);
		save.setOnClickListener(this);
		delete.setOnClickListener(this);
		
		itemPosition = getIntent().getIntExtra("position", 0);
		viewType = getIntent().getIntExtra("viewType", 0);
		item = new Item(this);
		if(viewType==0){
			//从数据库加载内容
			item.getDbData(itemPosition);
			showItem();
			setViewType(null, 0);
		}else if(viewType==2){
			item.createNew(timeFormat.format(new Date()));
			showItem();
			lastFocus = titleView;
			setViewType(titleView, 2);
		}
	}
	
	@Override
	public void onClick(View v){
		switch(v.getId()){
		case R.id.finish:
			if(activity_2.this.viewType==0||activity_2.this.viewType==2){
				activity_2.super.onBackPressed();
			}else{
				activity_2.this.finishWithoutSave();
			}
			break;
		case R.id.cancel:
			activity_2.this.showItem();
			activity_2.this.setViewType(lastFocus, 0);
			break;
		case R.id.save:
			activity_2.this.save();
			activity_2.this.setViewType(lastFocus, 0);
			break;
		case R.id.delete:
			item.delete();
			activity_2.super.onBackPressed();
			break;
		}
	}
	
	@Override
 	protected void onRestart(){
		super.onRestart();
		if(viewType==2||viewType==3){
			//打开键盘
			InputMethodManager inputManager = 
					(InputMethodManager)this.getSystemService(Context.INPUT_METHOD_SERVICE);    
			inputManager.toggleSoftInput(0, InputMethodManager.SHOW_FORCED);
		}
	}
	
	//根据当前所处状态：read 返回到activity_1 ; editing 关闭软键盘，进入edited状态 ; edited 弹出dialog选择是否保存
	@Override
	public void onBackPressed() {
		if(viewType==0){
			super.onBackPressed();
		}else if(viewType==1){
			finishWithoutSave();
		}else if(viewType==2){
			new alert(this, "activity_2 handle onBackPressed in viewType 2 ?");
		}else{
			new alert(this, "activity_2 handle onBackPressed in viewType 3 ?");
		}
	}
	
	public void showItem(){
		titleView.setText(item.title);
		//TODO: 改成一组按钮
		tagsView.setText(item.tagsString);
		contentView.setText(item.content);
	}
	
	@SuppressLint("InflateParams") public void finishWithoutSave(){
		AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(activity_2.this);
		LinearLayout dialogView= (LinearLayout) getLayoutInflater().inflate(R.layout.layout_askifsave_dialog,null);
		
		dialogBuilder.setView(dialogView);
		dialogBuilder.setCancelable(true);
		final AlertDialog dialog = dialogBuilder.show();
		OnClickListener onClickListener = new OnClickListener(){
        	@Override
        	public void onClick(View v){
        		switch(v.getId()){
        		case R.id.dialogCancel:
        			dialog.dismiss();
        			break;
        		case R.id.dialogNosave:
        			dialog.dismiss();
        			activity_2.super.onBackPressed();
        			break;
        		case R.id.dialogSave:
        			activity_2.this.save();
        			activity_2.super.onBackPressed();
        			break;
        		}
        	}
        };
        dialog.findViewById(R.id.dialogCancel).setOnClickListener(onClickListener);
        dialog.findViewById(R.id.dialogNosave).setOnClickListener(onClickListener);
        dialog.findViewById(R.id.dialogSave).setOnClickListener(onClickListener);
	}
	
	public void save(){
		//将View中的字符串写入item，及相关数据
		item.title = titleView.getText().toString();
		item.wordCount = Integer.toString(contentView.getText().length());
		item.editTime = timeFormat.format(new Date());
		item.tagsString = tagsView.getText().toString();
		item.content = contentView.getText().toString();
		//将item上传到数据库
		item.updateDbData();
	}
	
	public boolean isChanged(){
		return !(titleView.getText().toString().equals(item.title)
				&& tagsView.getText().toString().equals(item.tagsString)
				&& contentView.getText().toString().equals(item.content));
	}
	
	public void setViewType(View v, int viewType){	//v 被点击的元素，只有editing和edited用到
		//new alert(this, "setViewType: "+Integer.toString(viewType));
		//改变SuperEditText内容后，根据与数据库中内容的对比来触发
		if((viewType==2||viewType==3) && v == null){
			save.setVisibility(viewType==3?View.VISIBLE:View.GONE);
			cancel.setVisibility(viewType==3?View.VISIBLE:View.GONE);
			this.viewType = viewType;
			return;
		}
		//按钮
		if(viewType==0||viewType==2){
			save.setVisibility(View.GONE);
			cancel.setVisibility(View.GONE);
		}else{
			save.setVisibility(View.VISIBLE);
			cancel.setVisibility(View.VISIBLE);
		}
		//输入法
		final InputMethodManager inputMethodManager = (InputMethodManager) 
				this.getSystemService(Context.INPUT_METHOD_SERVICE);
		if(viewType==0||viewType==1){
			
        	//强制关闭输入法
			if (inputMethodManager != null && this.getCurrentFocus() != null) {
				inputMethodManager.hideSoftInputFromWindow(
						this.getCurrentFocus().getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);
			}
			
			//隐藏光标
			if(v != null){
				v.setFocusable(false);
				v.setFocusableInTouchMode(false);
			}
			//恢复滚动区域高度
			//findViewById(R.id.mainArea).setLayoutParams(initMainAreaLayoutPrams);
			LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams)findViewById(R.id.mainArea).getLayoutParams();
			layoutParams.height = LayoutParams.WRAP_CONTENT;
			findViewById(R.id.mainArea).setLayoutParams(layoutParams);
		}else{
			//压缩滚动区域高度
			LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams)findViewById(R.id.mainArea).getLayoutParams();
			layoutParams.height = 650;//TODO: 自动化
			findViewById(R.id.mainArea).setLayoutParams(layoutParams);
			//显示cursor，弹出软键盘
			v.setFocusable(true);
			v.setFocusableInTouchMode(true);
			v.requestFocus();
			v.clearFocus();
			inputMethodManager.toggleSoftInput(0, InputMethodManager.SHOW_FORCED);
		}
		this.viewType = viewType;
	}
}