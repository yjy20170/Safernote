package com.voyd.safernote;

import com.voyd.safernote.R;

import android.app.Application;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class MyApplication extends Application{
	public static Context context;
	public static SQLiteDatabase db;
	public static final int dbVersion = 3;
	public static String password="";
	public static boolean isErrorPasswordInputed = false;
	@Override
	public void onCreate(){
		context = getApplicationContext();
		DbHelper dbHelper = new DbHelper(context, context.getString(R.string.database_name), null, dbVersion);
		db = dbHelper.getWritableDatabase();
	}
	public static String getMD5Password(){
		Cursor cursor = db.rawQuery("select * from settings", null);		
		cursor.moveToFirst();
		return cursor.getString(cursor.getColumnIndex("md5password"));
	}
	public static void updatePassword(String newPassword){
		//更新数据库MD5密码；重新加密所有Item数据；更新内存中密码；
		String newMD5Password = MD5Util.MD5(newPassword);
		ContentValues values = new ContentValues();
		values.put("md5password", newMD5Password);
		db.update("settings", values, null, null);
		
		int itemsCount = Item.getTableLength(db, "items");
		Item item = new Item();
		for(int i=0;i<itemsCount;i++){
			item.loadDbData(i);
			item.updateDbData(newPassword);
		}
		
		password = newPassword;
	}
	public static int getSafetyLevel(){
		Cursor cursor = db.rawQuery("select * from settings", null);		
		cursor.moveToFirst();
		return cursor.getInt(cursor.getColumnIndex("safetyLevel"));
	}
	public static void updateSafetyLevel(int k){
		db.execSQL("update settings set safetyLevel = "+k);
	}
}
