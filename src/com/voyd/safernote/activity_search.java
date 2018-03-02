package com.voyd.safernote;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.CompoundButton;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class activity_search extends SafeActivity implements OnClickListener, OnCheckedChangeListener{
	public boolean isInTitle = true;
	public boolean isInTags = true;
	public boolean isInContent = true;
	private ItemAdapter itemAdapter;
	private ArrayList<Item> list;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_search);
		
        ((TextView) findViewById(R.id.titlebarText)).setText("搜索");
        findViewById(R.id.finish).setOnClickListener(this);
        findViewById(R.id.save).setOnClickListener(this);
        ((CheckBox)findViewById(R.id.search_inTitle)).setOnCheckedChangeListener(this);
        ((CheckBox)findViewById(R.id.search_inTags)).setOnCheckedChangeListener(this);
        ((CheckBox)findViewById(R.id.search_inContent)).setOnCheckedChangeListener(this);
        ((CheckBox)findViewById(R.id.search_inTitle)).setChecked(isInTitle);
        ((CheckBox)findViewById(R.id.search_inTags)).setChecked(isInTags);
        ((CheckBox)findViewById(R.id.search_inContent)).setChecked(isInContent);
        findViewById(R.id.search_create_limitStartDate).setOnClickListener(this);
        findViewById(R.id.search_create_limitEndDate).setOnClickListener(this);
        ((TextView)findViewById(R.id.search_create_limitStartDate)).setText("2000-01-01");
        ((TextView)findViewById(R.id.search_create_limitEndDate)).setText("2099-12-31");

		list = new ArrayList<Item>();
		itemAdapter = new ItemAdapter(this, R.layout.layout_item, list, false, false);
		((ListView)findViewById(R.id.itemListView)).setAdapter(itemAdapter);
		
		((ListView)findViewById(R.id.itemListView)).setOnItemClickListener(new OnItemClickListener(){
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent(activity_search.this,activity_2.class);
				intent.putExtra("item",list.get(position));//TODO
				intent.putExtra("viewType", 0);
				
				startSafeActivity(intent);
			}
		});
	}
	@Override
    public void onClick(final View v) {
		switch(v.getId()){
		case R.id.finish:
			finish();
			break;
		case R.id.save:
			//关闭键盘
			InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
			if (inputMethodManager != null && getCurrentFocus() != null) {
				inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
						InputMethodManager.HIDE_NOT_ALWAYS);
			}
			doSearch();
			new alert("找到"+list.size()+"条结果");
			break;
		case R.id.search_create_limitStartDate:
		case R.id.search_create_limitEndDate:
			AlertDialog.Builder localBuilder = new AlertDialog.Builder(activity_search.this);
	        localBuilder.setTitle("选择日期");
	        //
	        final LinearLayout layout_alert= (LinearLayout) getLayoutInflater().inflate(R.layout.layout_date_picker_dialog, null);
	        localBuilder.setView(layout_alert);
	        localBuilder.setPositiveButton("确定", new DialogInterface.OnClickListener()
	        {
	            public void onClick(DialogInterface paramAnonymousDialogInterface, int paramAnonymousInt)
	            {
	                DatePicker datepicker1= (DatePicker) layout_alert.findViewById(R.id.datePicker_startTime);
	                int y=datepicker1.getYear();
	                int m=datepicker1.getMonth()+1;
	                int d=datepicker1.getDayOfMonth();

	                ((TextView)findViewById(v.getId())).setText(
	                		y+"-"+(m<10?"0":"")+m+"-"+(d<10?"0":"")+d);
	            }
	        }).setNegativeButton("取消", new DialogInterface.OnClickListener()
	        {
	            public void onClick(DialogInterface paramAnonymousDialogInterface, int paramAnonymousInt)
	            {

	            }
	        }).create().show();
	        break;
		}
    }
	@Override
	public void onCheckedChanged(CompoundButton btn, boolean isChecked){
		switch(btn.getId()){
		case R.id.search_inTitle:
			isInTitle = isChecked;
			break;
		case R.id.search_inTags:
			isInTags = isChecked;
			break;
		case R.id.search_inContent:
			isInContent = isChecked;
			break;
		}
	}
	private void doSearch(){
		list.clear();
		String[] searchWords = ((EditText)findViewById(R.id.search_inputText)).getText().toString().split(",");
		int itemsCount = MyApplication.getTableLength("items");
		Date createLimitStartDate;
		Date createLimitEndDate;
		try{
			createLimitStartDate = (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).parse(
					((TextView)findViewById(R.id.search_create_limitStartDate)).getText()
					+" 00:00:00");
			createLimitEndDate = (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).parse(
							((TextView)findViewById(R.id.search_create_limitEndDate)).getText()
							+" 23:59:59");
		}catch(ParseException e){
			new alert("createLimitTime格式解析出错");
			return;
		}
		for(int i=0;i<itemsCount;i++){
			Item item = new Item();
			item.loadDataByPosition(i, false);
			
			//判断创建时间
			Date itemCreateDate;
			try{
				itemCreateDate = Item.timeFormat.parse(item.createTime);
			}catch(ParseException e){
				new alert("createTime格式解析出错");
				return;
			}
			if(itemCreateDate.before(createLimitStartDate)
					|| itemCreateDate.after(createLimitEndDate)){
				continue;
			}
			boolean isMatch = true;
			for(String word:searchWords){
				if(word.equals("")) continue;
				if(!((isInTitle && item.title.indexOf(word)!=-1)
						|| (isInTags && item.tagsString.indexOf(word)!=-1)
						|| (isInContent && item.content.indexOf(word)!=-1))){
					isMatch = false;
					break;
				}
			}
			if(isMatch){
				list.add(item);
			}
		}
		itemAdapter.notifyDataSetChanged();
	}
	
	@Override
	public void onRestart(){
		if(isFromStack){
			doSearch();
		}
		super.onRestart();
	}
}
