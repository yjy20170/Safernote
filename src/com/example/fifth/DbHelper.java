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
			+ "wordCount integer, "
			+ "createTime text, "
			+ "editTime text, "
			+ "tagsString text, "
			+ "content text)";
	public static final String INSERT_ITEM1 = "insert into items ("
			+ "title, wordCount, createTime, editTime, tagsString, content) values("
			+ "'welcome', 10, '2018-01-27 21:57','2018-01-27 21:57','test','1\n2\n3\n4\n5\n6\n7\n8\n9\n10"
			+ "\n11\n12\n13\n14\n15\n16\n17\n18\n19\n20\n"
			+ "21\n22\n23\n24\n25\n26\n27\n28\n29\n30')";
	public static final String CREATE_SETTINGS = "create table settings ("
			+ "md5password text)";
	public static final String INSERT_SETTINGS = "insert into settings ("
			+ "md5password) values("
			+ "'"+MD5Util.MD5(defaultPassword)+"')";
	private Context mContext;
	public DbHelper(Context context, String name, CursorFactory
			factory, int version) {
		super(context, name, factory, version);
		mContext = context;
	}
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_ITEMS);
		db.execSQL(INSERT_ITEM1);
		db.execSQL(CREATE_SETTINGS);
		db.execSQL(INSERT_SETTINGS);
		new alert(mContext, "database created");
		new alert(mContext, "default password is "+defaultPassword);
	}
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	}
}