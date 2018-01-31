package com.example.fifth;

import android.content.ContentValues;
import android.database.Cursor;

public class Settings {
	public static String password="";
	public static String getMD5Password(){
		Cursor cursor = MyApplication.db.rawQuery("select * from settings", null);		
		cursor.moveToFirst();
		return cursor.getString(cursor.getColumnIndex("md5password"));
	}
	public static void updatePassword(String newPassword){
		//TODO �������ݿ�MD5���룻���¼�������Item���ݣ������ڴ������룻
		String newMD5Password = MD5Util.MD5(newPassword);
		ContentValues values = new ContentValues();
		values.put("md5password", newMD5Password);
		MyApplication.db.update("settings", values, null, null);
		
		int itemsCount = Item.getTableLength(MyApplication.db, "items");
		Item item = new Item();
		for(int i=0;i<itemsCount;i++){
			item.getDbData(i);
			item.updateDbData(newPassword);
		}
		
		Settings.password = newPassword;
	}
}