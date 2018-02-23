package com.voyd.safernote;

import java.util.ArrayList;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

public class Item {
	public int id;
	public boolean isNew = false;
	public SQLiteDatabase db = MyApplication.db;
	public String title;
	public String createTime;
	public String editTime;
	public ArrayList<String> tags = new ArrayList<String>();
	public String tagsString;
	public String content;
	public String wordCount;
	
	public Item(){
	}
	//供测试
	public Item(String title){
		this.title = title;
		this.wordCount = "999";
	}
	
	public void createNew(String createTime){
		isNew = true;
		title = "";
		wordCount = "0";
		this.createTime = createTime;
		tagsString = "";
		content = "";
	}
	//用于修改密码后的更新
	public void updateDbData(String password){
		//new alert(context, "updateDbData...");
		ContentValues values = new ContentValues();
		values.put("title", AES.encrypt(password, title));
		values.put("wordCount",AES.encrypt(password, wordCount));
		values.put("createTime", AES.encrypt(password, createTime));
		values.put("editTime", AES.encrypt(password, editTime));
		values.put("tagsString",AES.encrypt(password, tagsString));
		values.put("content", AES.encrypt(password, content));
		db.update("items", values, "id = ?", new String[]{Integer.toString(id)});
	}
	public void updateDbData(){
		if(isNew){
			//建一条空项目
			String INSERT_ITEM = "insert into items ("
					+ "title, wordCount, createTime, editTime, tagsString, content) values("
					+ "'', 0, '"+AES.encrypt(MyApplication.password, createTime)+"','','','')";
			db.execSQL(INSERT_ITEM);
			int offset = getTableLength(db, "items") - 1;
			Cursor cursor = db.rawQuery("select * from items limit 1 offset "+offset, null);		
			cursor.moveToFirst();
			id = Integer.parseInt(cursor.getString(cursor.getColumnIndex("id")));
			isNew = false;
		}
		//new alert(context, "updateDbData...");
		updateDbData(MyApplication.password);
	}
	
	//根据id，从数据库加载数据
	public void getDbData(int position){
		//wrong: this.id = getTableLength(db, "items") - position;
		int offset = getTableLength(db, "items") - position - 1;
		Cursor cursor = db.rawQuery("select * from items limit 1 offset "+offset, null);		
		cursor.moveToFirst();
		id = Integer.parseInt(cursor.getString(cursor.getColumnIndex("id")));
		
		title = AES.decrypt(MyApplication.password, cursor.getString(cursor.getColumnIndex("title")));
		wordCount = AES.decrypt(MyApplication.password, cursor.getString(cursor.getColumnIndex("wordCount")));
		createTime = AES.decrypt(MyApplication.password, cursor.getString(cursor.getColumnIndex("createTime")));
		editTime = AES.decrypt(MyApplication.password, cursor.getString(cursor.getColumnIndex("editTime")));
		tagsString = AES.decrypt(MyApplication.password, cursor.getString(cursor.getColumnIndex("tagsString")));
		for(String tag: tagsString.split(",")){
			tags.add(tag);
		}
		content = AES.decrypt(MyApplication.password, cursor.getString(cursor.getColumnIndex("content")));
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
