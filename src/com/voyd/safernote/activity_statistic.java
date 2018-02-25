package com.voyd.safernote;

import java.util.ArrayList;
import java.util.Calendar;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;

public class activity_statistic extends SafeActivity{
	private HorizontalListView eventColumnListView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_statistic);

		//for test
		MyApplication.db.execSQL("delete from record");
		MyApplication.db.execSQL("insert into record values (2018,1,1,1,2)");
		MyApplication.db.execSQL("insert into record values (2018,1,4,4,3)");
		MyApplication.db.execSQL("insert into record values (2018,1,17,3,1)");
		MyApplication.db.execSQL("insert into record values (2018,1,25,4,0)");
		MyApplication.db.execSQL("insert into record values (2018,1,30,2,4)");
		MyApplication.db.execSQL("insert into record values (2018,2,2,5,1)");
		MyApplication.db.execSQL("insert into record values (2018,2,5,1,3)");
		MyApplication.db.execSQL("insert into record values (2018,2,10,6,1)");
		MyApplication.db.execSQL("insert into record values (2018,2,25,0,4)");
		
		//calendar
		/*
		Calendar now = Calendar.getInstance();
		new alert(""+now.get(Calendar.YEAR)+" "+(now.get(Calendar.MONTH)+1)
				+" "+now.get(Calendar.DATE)+" "+(now.get(Calendar.DAY_OF_WEEK)-1));
		now.add(Calendar.DATE, -1);
		new alert(""+now.get(Calendar.YEAR)+" "+(now.get(Calendar.MONTH)+1)
				+" "+now.get(Calendar.DATE)+" "+(now.get(Calendar.DAY_OF_WEEK)-1));
		*/
		
		eventColumnListView = (HorizontalListView)findViewById(R.id.eventColumnListView);
		ArrayList<EventColumn> eventColumnList = new ArrayList<EventColumn>();
		
		loadEventList(eventColumnList);
		
		EventColumnAdapter eventColomnAdapter = new EventColumnAdapter(this, R.layout.layout_event_column, eventColumnList);
		eventColumnListView.setAdapter(eventColomnAdapter);
		//定位到最右侧
		findViewById(R.id.test).setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v){
				activity_statistic.this.eventColumnListView.scrollTo((int)eventColumnListView.getX(), 0);
			}
		});
		
	}
	private void loadEventList(ArrayList<EventColumn> eventList){
		//*
		eventList.add(new EventColumn(0,1,2,0,0,1,3,"2001","Feb"));
		eventList.add(new EventColumn(1,3,4,0,2,1,0,"",""));
		eventList.add(new EventColumn(1,3,4,0,2,1,0,"",""));
		eventList.add(new EventColumn(0,1,2,0,0,1,3,"2017","Feb"));
		eventList.add(new EventColumn(1,3,4,0,2,1,0,"",""));
		eventList.add(new EventColumn(1,3,4,0,2,1,0,"",""));
		eventList.add(new EventColumn(0,1,2,0,0,1,3,"2017","Feb"));
		eventList.add(new EventColumn(1,3,4,0,2,1,0,"",""));
		eventList.add(new EventColumn(1,3,4,0,2,1,0,"",""));
		eventList.add(new EventColumn(0,1,2,0,0,1,3,"2017","Feb"));
		eventList.add(new EventColumn(1,3,4,0,2,1,0,"",""));
		eventList.add(new EventColumn(1,3,4,0,2,1,0,"",""));
		eventList.add(new EventColumn(0,1,2,0,0,1,3,"2017","Feb"));
		eventList.add(new EventColumn(1,3,4,0,2,1,0,"",""));
		eventList.add(new EventColumn(1,3,4,0,2,1,0,"",""));
		eventList.add(new EventColumn(0,1,2,0,0,1,3,"2017","Feb"));
		eventList.add(new EventColumn(1,3,4,0,2,1,0,"",""));
		eventList.add(new EventColumn(1,3,4,0,2,1,0,"",""));
		eventList.add(new EventColumn(0,1,2,0,0,1,3,"2017","Feb"));
		eventList.add(new EventColumn(1,3,4,0,2,1,0,"",""));
		eventList.add(new EventColumn(1,3,4,0,2,1,0,"",""));
		eventList.add(new EventColumn(0,1,2,0,0,1,3,"2017","Feb"));
		eventList.add(new EventColumn(1,3,4,0,2,1,0,"",""));
		eventList.add(new EventColumn(1,3,4,0,2,1,0,"",""));
		eventList.add(new EventColumn(0,1,2,0,0,1,3,"2017","Feb"));
		eventList.add(new EventColumn(1,3,4,0,2,1,0,"",""));
		eventList.add(new EventColumn(1,3,4,0,2,1,0,"",""));
		eventList.add(new EventColumn(0,1,2,0,0,1,3,"2049","Feb"));
		eventList.add(new EventColumn(-1,-1,-1,-1,-1,-1,-1,"",""));
		//*/
		//首先得到有记录的最早日期
		SQLiteDatabase db = MyApplication.db;
		Cursor cursor = db.rawQuery("select * from record", null);
		if(cursor.moveToFirst()){
			Event first = new Event(cursor);
		}
	}
}
