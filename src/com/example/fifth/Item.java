package com.example.fifth;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

public class Item {
	public int id;
	public boolean isNew = false;
	public Context context;
	public SQLiteDatabase db;
	public String title;
	public String createTime;
	public String editTime;
	public ArrayList<String> tags = new ArrayList<String>();
	public String tagsString;
	public String content;
	public int wordCount;
	
	public Item(Context context){
		this.context = context;
		DbHelper dbHelper = new DbHelper(context, context.getString(R.string.database_name), null, 1);
		db = dbHelper.getWritableDatabase();
	}
	//供测试
	public Item(String title){
		this.title = title;
		this.wordCount = 999;
	}
	
	public void createNew(String createTime){
		isNew = true;
		title = "";
		wordCount = 0;
		this.createTime = createTime;
		tagsString = "";
		content = "";
	}
	
	public void updateDbData(){
		if(isNew){
			//建一条空项目
			String INSERT_ITEM = "insert into items ("
					+ "title, wordCount, createTime, editTime, tagsString, content) values("
					+ "'', 0, '"+createTime+"','','','')";
			db.execSQL(INSERT_ITEM);
			int offset = getTableLength(db, "items") - 1;
			Cursor cursor = db.rawQuery("select * from items limit 1 offset "+offset, null);		
			cursor.moveToFirst();
			id = Integer.parseInt(cursor.getString(cursor.getColumnIndex("id")));
			isNew = false;
		}
		//new alert(context, "updateDbData...");
		ContentValues values = new ContentValues();
		values.put("title", title);
		values.put("wordCount",wordCount);
		values.put("editTime", editTime);
		values.put("tagsString",tagsString);
		values.put("content", content);
		db.update("items", values, "id = ?", new String[]{Integer.toString(id)});
	}
	
	//TODO: 直接加载整个items列表
	//根据id，从数据库加载数据
	public void getDbData(int position){
		//wrong: this.id = getTableLength(db, "items") - position;
		int offset = getTableLength(db, "items") - position - 1;
		Cursor cursor = db.rawQuery("select * from items limit 1 offset "+offset, null);		
		cursor.moveToFirst();
		id = Integer.parseInt(cursor.getString(cursor.getColumnIndex("id")));
		
		title = cursor.getString(cursor.getColumnIndex("title"));
		wordCount = Integer.parseInt(cursor.getString(cursor.getColumnIndex("wordCount")));
		createTime = cursor.getString(cursor.getColumnIndex("createTime"));
		editTime = cursor.getString(cursor.getColumnIndex("editTime"));
		tagsString = cursor.getString(cursor.getColumnIndex("tagsString"));
		for(String tag: tagsString.split(",")){
			tags.add(tag);
		}
		content = cursor.getString(cursor.getColumnIndex("content"));
	}
	
	public void delete(){
		if(isNew){//未写入数据库
			return;
		}
		db.delete("items", "id = ?", new String[] { Integer.toString(id) });
	}
	
	public static int getTableLength(SQLiteDatabase db, String tableName){
		//得到表中行数
		SQLiteStatement statement = db.compileStatement("select count(*) from "+tableName);
		long count = statement.simpleQueryForLong();
		return (int)count;
	}
}
