package com.voyd.safernote;

import java.util.Calendar;

import android.database.Cursor;

public class Event {
	public int year;
	public int month;
	public int date;
	public int dayofweek;
	public int level;
	public Event(Cursor cursor){
		year = cursor.getInt(cursor.getColumnIndex("year"));
		month = cursor.getInt(cursor.getColumnIndex("month"));
		date = cursor.getInt(cursor.getColumnIndex("date"));
		dayofweek = cursor.getInt(cursor.getColumnIndex("dayofweek"));
		level = cursor.getInt(cursor.getColumnIndex("level"));
	}
	public int compareTo(Calendar thatDay){
		int thisValue = this.year * 13 * 32 + this.month * 32 + this.date;
		int thatValue = thatDay.get(Calendar.YEAR) * 13 * 32 + (thatDay.get(Calendar.MONTH)+1) * 32 + thatDay.get(Calendar.DATE);
		if(thisValue > thatValue){return 1;}
		else if(thisValue == thatValue){return 0;}
		else return -1;
	}
}
