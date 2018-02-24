package com.voyd.safernote;

import java.lang.reflect.Field;
import java.util.ArrayList;

import com.voyd.safernote.R;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.ViewDragHelper;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;

public class activity_1 extends SafeActivity implements OnClickListener{
	private ListView listView;
	private ItemAdapter itemAdapter;
	private ArrayList<Item> list;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_1);
		
		listView = (ListView)findViewById(R.id.listView);
		int tableItemsLength = Item.getTableLength(MyApplication.db, "items");
		//初始化stick数组
		Item.loadSticks();
		//根据数据库中Item行数，初始化list
		list = new ArrayList<Item>();
		for(int i=0;i<tableItemsLength;i++){
			list.add(new Item());
		}
		
		itemAdapter = new ItemAdapter(this, R.layout.layout_item, list);
		listView.setAdapter(itemAdapter);
		
		listView.setOnItemClickListener(new OnItemClickListener(){
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent(activity_1.this,activity_2.class);
				intent.putExtra("position",position);
				intent.putExtra("viewType", 0);
				
				startSafeActivity(intent);
			}
		});
		((Button) findViewById(R.id.createItem)).setOnClickListener(this);
		((Button) findViewById(R.id.set_password)).setOnClickListener(this);
		((Button) findViewById(R.id.import_export_db)).setOnClickListener(this);
		((Button) findViewById(R.id.settings)).setOnClickListener(this);
		
		//改变左侧菜单响应范围
		setDrawerLeftEdgeSize((DrawerLayout)findViewById(R.id.drawer_layout),(float)0.15);
	}
	@Override
	public void onClick(View v){
		switch(v.getId()){
		case R.id.createItem:
			Intent intent = new Intent(this, activity_2.class);
			intent.putExtra("viewType", 2);
			intent.putExtra("position", -1);
			startSafeActivity(intent);
			/*/加密可能会出现在字符串结尾随机加上ascii码小于16的字符的bug;先滑动显示完每个item再使用
			for(Item item:list){
				String[] ss = {item.title,item.wordCount,item.createTime,item.editTime,item.tagsString,item.content};
				for(int k=0;k<6;k++){
					if(ss[k]!=null){
						while(ss[k].length()>0){
							if(Character.valueOf(ss[k].charAt(ss[k].length()-1)).hashCode()<16){
								ss[k]=ss[k].substring(0, ss[k].length()-1);
							}else{
								break;
							}
						}
					}
				}
				item.title=ss[0];
				item.wordCount=ss[1];
				item.createTime=ss[2];
				item.editTime=ss[3];
				item.tagsString=ss[4];
				item.content=ss[5];
				item.updateDbData();
			}
			new alert("over");
			/*/
			break;
		case R.id.set_password:
			startSafeActivity(new Intent(this, activity_setPassword.class));
			break;
		case R.id.import_export_db:
			startSafeActivity(new Intent(this, activity_importExportDb.class));
			break;
		case R.id.settings:
			startSafeActivity(new Intent(this, activity_settings.class));
			break;
		default:
		}
	}
	
	//
	@Override
	public void onRestart(){
		if(isFromStack){
			Item.loadSticks();
			listView = (ListView)findViewById(R.id.listView);
			int tableItemsLength = Item.getTableLength(MyApplication.db, "items");
			//根据数据库中Item行数，初始化list
			list.clear();
			for(int i=0;i<tableItemsLength;i++){
				list.add(new Item());
			}
			itemAdapter.notifyDataSetChanged();
		}
		super.onRestart();
	}
	private void setDrawerLeftEdgeSize(DrawerLayout drawerLayout, float displayWidthPercentage) {
		try {
			// find ViewDragHelper and set it accessible
			Field leftDraggerField = drawerLayout.getClass().getDeclaredField("mLeftDragger");
			leftDraggerField.setAccessible(true);
			ViewDragHelper leftDragger = (ViewDragHelper) leftDraggerField.get(drawerLayout);
			// find edge size and set is accessible
			Field edgeSizeField = leftDragger.getClass().getDeclaredField("mEdgeSize");
			edgeSizeField.setAccessible(true);
			int edgeSize = edgeSizeField.getInt(leftDragger);
			// set new edge size
			// Point displaySize = new Point();
			DisplayMetrics dm = new DisplayMetrics();
			getWindowManager().getDefaultDisplay().getMetrics(dm);
			edgeSizeField.setInt(leftDragger, Math.max(edgeSize, (int) (dm.widthPixels * displayWidthPercentage)));
		} catch (NoSuchFieldException e) {
			Log.e("NoSuchFieldException", e.getMessage().toString());
		} catch (IllegalArgumentException e) {
			Log.e("IllegalArgument", e.getMessage().toString());
		} catch (IllegalAccessException e) {
			Log.e("IllegalAccessException", e.getMessage().toString());
		}
	}
	
}