package com.example.fifth;

import java.util.ArrayList;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;

public class activity_1 extends SafeActivity implements OnClickListener{
	private Button start2Btn;
	private ListView listView;
	private ItemAdapter itemAdapter;
	private ArrayList<Item> list;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_1);
		DbHelper dbHelper = new DbHelper(this, getString(R.string.database_name), null, 1);
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		/*/db test
		Cursor cursor = db.rawQuery("select * from items", null);
		if(cursor.moveToFirst()){
			do{
				String title = cursor.getString(cursor.getColumnIndex("id"));
				new alert(this, title);
			}while(cursor.moveToNext());
		}/*/
		
		listView = (ListView)findViewById(R.id.listView);
		int tableItemsLength = Item.getTableLength(db, "items");
		//根据数据库中Item行数，初始化list
		list = new ArrayList<Item>();
		for(int i=0;i<tableItemsLength;i++){
			list.add(new Item(this));
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
		start2Btn = (Button) findViewById(R.id.start2);
		start2Btn.setOnClickListener(this);
	}
	@Override
	public void onClick(View v){
		switch(v.getId()){
		case R.id.start2:
			Intent intent = new Intent(this, activity_2.class);
			intent.putExtra("viewType", 2);
			intent.putExtra("position", -1);
			startSafeActivity(intent);
			break;
		default:
		}
	}
	
	//
	@Override
	public void onRestart(){
		if(isFromStack){
			DbHelper dbHelper = new DbHelper(this, getString(R.string.database_name), null, 1);
			SQLiteDatabase db = dbHelper.getWritableDatabase();
			listView = (ListView)findViewById(R.id.listView);
			int tableItemsLength = Item.getTableLength(db, "items");
			new alert(this, "onRestart, items count: "+tableItemsLength);
			//根据数据库中Item行数，初始化list
			//list = new ArrayList<Item>();
			list.clear();
			for(int i=0;i<tableItemsLength;i++){
				list.add(new Item(this));
			}
			//listView.requestFocus();
			//itemAdapter.notifyAll();
			//itemAdapter.notifyDataSetInvalidated();
			itemAdapter.notifyDataSetChanged();
			/*
			itemAdapter = new ItemAdapter(this, R.layout.layout_item, list);
			listView.setAdapter(itemAdapter);
			*/
			//itemAdapter.notifyDataSetChanged();
			/*
	        Intent intent = new Intent(activity_1.this, activity_1.class); 
	        //关闭动画效果
	        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
	        startSafeActivity(intent);
			finish();
			*/
		}
		super.onRestart();
		//重绘
		//getWindow().getDecorView().invalidate();
		//onCreate(null);
		//
		//
		//itemAdapter.notifyDataSetChanged
	}//
	
}