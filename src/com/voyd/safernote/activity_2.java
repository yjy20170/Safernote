package com.voyd.safernote;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.voyd.safernote.R;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.FrameLayout;
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
	private Button setStick;
	public static SimpleDateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	//0：无键盘无按钮  1：无键盘有按钮  2：有键盘无按钮  3：有键盘有按钮
	public int viewType;
	Item item;
	int itemPosition;
	
	
	public SuperEditText lastFocus = null;
	
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
		setStick = (Button)findViewById(R.id.setStick);
		
		//绑定OnClickListener!!!
		finish.setOnClickListener(this);
		cancel.setOnClickListener(this);
		save.setOnClickListener(this);
		delete.setOnClickListener(this);
		setStick.setOnClickListener(this);
		
		itemPosition = getIntent().getIntExtra("position", 0);
		viewType = getIntent().getIntExtra("viewType", 0);
		item = new Item();
		if(viewType==0){
			//从数据库加载内容
			item.loadDbData(itemPosition);
			showItem();
			setViewType(null, 0);
		}else if(viewType==2){
			item.createNew(timeFormat.format(new Date()));
			showItem();
			lastFocus = titleView;
			setViewType(titleView, 2);
			titleView.enterEdit();
		}
		
		//随stick切换置顶按钮状态 
		if(item.stick==0){
			setStick.setText("置顶");
		}else{
			setStick.setText("取消置顶");
		}
	}
	
	@Override
	public void onClick(View v){
		switch(v.getId()){
		case R.id.finish:
			if(viewType==0||viewType==2){
				super.onBackPressed();
			}else{
				finishWithoutSave();
			}
			break;
		case R.id.cancel:
			setViewType(lastFocus, 0);
			lastFocus.leaveEdit();
			showItem();
			break;
		case R.id.save:
			save();
			/*
			//不改变键盘状态，只改变按钮显示
			setViewType(lastFocus, viewType - 1);
			*/
			setViewType(lastFocus, 0);
			lastFocus.leaveEdit();
			break;
		case R.id.delete:
			deleteWarning();
			break;
		case R.id.setStick:
			item.setStick(1-item.stick);//0-1
			if(item.stick==0){
				setStick.setText("置顶");
			}else{
				setStick.setText("取消置顶");
			}
			break;
		}
	}
	
	@SuppressLint("InflateParams")
	public void deleteWarning(){
		AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(activity_2.this);
		LinearLayout dialogView= (LinearLayout) getLayoutInflater().inflate(R.layout.layout_delete_dialog,null);
		
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
        		case R.id.dialogConfirm:
        			item.delete();
        			dialog.dismiss();
        			activity_2.super.onBackPressed();
        			break;
        		}
        	}
        };
        dialog.findViewById(R.id.dialogCancel).setOnClickListener(onClickListener);
        dialog.findViewById(R.id.dialogConfirm).setOnClickListener(onClickListener);
	}
	
	@SuppressLint("InflateParams")
	public void finishWithoutSave(){
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
			finish();
		}else if(viewType==1){
			finishWithoutSave();
		}else if(viewType==2){
			new alert("activity_2.onBackPressed() in viewType 2 ?");
		}else{
			new alert("activity_2.onBackPressed() in viewType 3 ?");
		}
	}
	
	public void showItem(){
		titleView.setText(item.title);
		//TODO: 改成一组按钮
		tagsView.setText(item.tagsString);
		contentView.setText(item.content);
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
		//new alert("setViewType: "+Integer.toString(viewType));
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
		if(viewType==0||viewType==1){
			//恢复滚动区域高度//R.id.mainArea LinearLayout.LayoutParams
			FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams)findViewById(R.id.layout_2).getLayoutParams();
			layoutParams.height = LayoutParams.WRAP_CONTENT;
			findViewById(R.id.layout_2).setLayoutParams(layoutParams);
		}else{
			//压缩滚动区域高度
			FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams)findViewById(R.id.layout_2).getLayoutParams();
			layoutParams.height = 755;//TODO: 自动化
			findViewById(R.id.layout_2).setLayoutParams(layoutParams);
		}
		this.viewType = viewType;
	}
	public void setLongClickViewType(View v, int viewType){
		setViewType(v, viewType);
		//压缩滚动区域高度
		FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams)findViewById(R.id.layout_2).getLayoutParams();
		layoutParams.height = 658;//TODO: 自动化
		findViewById(R.id.layout_2).setLayoutParams(layoutParams);
	}
}