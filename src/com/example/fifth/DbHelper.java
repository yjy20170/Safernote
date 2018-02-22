package com.example.fifth;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DbHelper extends SQLiteOpenHelper {
	public static final String defaultPassword = "12345";
	public static final String CREATE_ITEMS = "create table items ("
			+ "id integer primary key autoincrement, "
			+ "title text, "
			+ "wordCount text, "
			+ "createTime text, "
			+ "editTime text, "
			+ "tagsString text, "
			+ "content text)";
	public static final String CREATE_SETTINGS = "create table settings ("
			+ "md5password text)";
	public static final String INSERT_SETTINGS = "insert into settings ("
			+ "md5password, safetyLevel) values("
			+ "'"+MD5Util.MD5(defaultPassword)+"')";
	public static final String CREATE_HISTORY = "";
	private String dbName;
	public DbHelper(Context context, String name, CursorFactory
			factory, int version) {
		super(context, name, factory, version);
		dbName = name;
	}
	@Override
	public void onCreate(SQLiteDatabase db) {
		if(dbName.equals(MyApplication.context.getString(R.string.database_name))){
			db.execSQL(CREATE_ITEMS);
			db.execSQL(CREATE_SETTINGS);
			db.execSQL(INSERT_SETTINGS);
			new alert("数据库创建完毕\n初始密码为"+defaultPassword);
		}
	}
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		switch(oldVersion){
		case 1://增加安全等级选项
			db.execSQL("alter table settings add column safetyLevel integer");
			db.execSQL("update settings set safetyLevel = 1");
		}
	}
}