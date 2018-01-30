package com.example.fifth;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class Settings {
	public static String getMD5Password(Context context){
		DbHelper dbHelper = new DbHelper(context, context.getString(R.string.database_name), null, 1);
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		Cursor cursor = db.rawQuery("select * from settings", null);		
		cursor.moveToFirst();
		return cursor.getString(cursor.getColumnIndex("md5password"));
	}
}