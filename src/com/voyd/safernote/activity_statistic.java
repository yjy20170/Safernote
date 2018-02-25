package com.voyd.safernote;

import java.util.ArrayList;
import java.util.Calendar;

import com.meetme.android.horizontallistview.HorizontalListView;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;


public class activity_statistic extends SafeActivity{
	private HorizontalListView eventColumnListView;
	private ArrayList<EventColumn> eventColumnList = new ArrayList<EventColumn>();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_statistic);

		//for test
		MyApplication.db.execSQL("delete from events");
		MyApplication.db.execSQL("insert into events values (2017,4,7,5,1)");
		MyApplication.db.execSQL("insert into events values (2017,7,27,4,1)");
		MyApplication.db.execSQL("insert into events values (2017,10,15,0,4)");
		MyApplication.db.execSQL("insert into events values (2017,12,2,6,1)");
		MyApplication.db.execSQL("insert into events values (2017,12,30,6,4)");
		MyApplication.db.execSQL("insert into events values (2018,1,1,1,2)");
		MyApplication.db.execSQL("insert into events values (2018,1,4,4,3)");
		MyApplication.db.execSQL("insert into events values (2018,1,17,3,1)");
		MyApplication.db.execSQL("insert into events values (2018,1,25,4,4)");
		MyApplication.db.execSQL("insert into events values (2018,2,2,5,1)");
		MyApplication.db.execSQL("insert into events values (2018,2,5,1,3)");
		MyApplication.db.execSQL("insert into events values (2018,2,10,6,1)");
		MyApplication.db.execSQL("insert into events values (2018,2,25,0,4)");
		
		eventColumnListView = (HorizontalListView)findViewById(R.id.eventColumnListView);
		
		loadEventList();
		
		EventColumnAdapter eventColomnAdapter = new EventColumnAdapter(this, R.layout.layout_event_column, eventColumnList);
		eventColumnListView.setAdapter(eventColomnAdapter);
		//定位到最右侧
		/*
		findViewById(R.id.test).setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v){
				//eventColumnListView.scrollTo(1000);
				new Handler().postDelayed(new Runnable(){
					@Override
					public void run(){
						new alert("aaaa");
					}
				}, 150);
			}
		});*/
		
		findViewById(R.id.eventGraph).setVisibility(View.INVISIBLE);
		new Handler().postDelayed(new Runnable(){
			@Override
			public void run(){
				eventColumnListView.scrollTo(99999);
				new Handler().postDelayed(new Runnable(){
					@Override
					public void run(){
						findViewById(R.id.eventGraph).setVisibility(View.VISIBLE);
					}
				}, 250);
			}
		}, 150);
	}
	private void loadEventList(){
		//首先得到有记录的最早日期
		SQLiteDatabase db = MyApplication.db;
		Cursor cursor = db.rawQuery("select * from events", null);
		if(cursor.moveToFirst()){//打开app即会添加event，因此不会为空
			int[] lastYearAndMonth = {-1, -1};
			Event firstEvent = new Event(cursor);
			EventColumn col = new EventColumn();
			//calendar 注意month要+1 dayofweek要-1
			Calendar pointerDay = Calendar.getInstance();
			
			pointerDay.set(firstEvent.year, firstEvent.month-1, firstEvent.date);
			handleEventColumn(lastYearAndMonth, pointerDay, col, firstEvent.level, true);
			
			Event nextEvent;
			while(cursor.moveToNext()){
				nextEvent = new Event(cursor);
				while(nextEvent.compareTo(pointerDay)==1){//未到达下个event
					handleEventColumn(lastYearAndMonth, pointerDay, col, 0);
				}
				//到达
				handleEventColumn(lastYearAndMonth, pointerDay, col, nextEvent.level);
			}
			//所有历史event处理完，pointerDay指向最后event的下一天//似乎不需要
			Calendar tomorrow = Calendar.getInstance();
			tomorrow.add(Calendar.DATE, 1);
			while(!(pointerDay.get(Calendar.YEAR)==tomorrow.get(Calendar.YEAR)
					&& pointerDay.get(Calendar.MONTH)==tomorrow.get(Calendar.MONTH)
					&& pointerDay.get(Calendar.DATE)==tomorrow.get(Calendar.DATE))){//未到达明天
				//填充空白 即默认0
				handleEventColumn(lastYearAndMonth, pointerDay, col, 0);
			}
			//如果明天不是新的column, 需对最后一列的下半截透明化
			if(pointerDay.get(Calendar.DAY_OF_WEEK)-1 != 0){
				int dayOfWeekToday = pointerDay.get(Calendar.DAY_OF_WEEK)-1 - 1;
				for(int i = dayOfWeekToday+1; i<=6; i++){
					col.blocks[i] = -1;
				}
				eventColumnList.add(new EventColumn(col));
			}
			eventColumnList.add(new EventColumn(-1,-1,-1,-1,-1,-1,-1,"",""));
		}
	}
	public String toMonthString(int i){
		String[] month = {"Jan","Feb","Mar","Apr","May","June","July","Aug","Sept","Oct","Nov","Dec"};
		return month[i-1];
	}
	private void handleEventColumn(int[] lastYearAndMonth, Calendar pointerDay, EventColumn col, int level){
		//填充空白 即默认0
		col.blocks[pointerDay.get(Calendar.DAY_OF_WEEK)-1] = level;
		if(pointerDay.get(Calendar.DAY_OF_WEEK)-1 == 6){//开始新的一周
			eventColumnList.add(new EventColumn(col));
			col.reset();
		}
		if(pointerDay.get(Calendar.DAY_OF_WEEK)-1 == 0){
			Calendar spointerDay = pointerDay;//Calendar.getInstance();
			//new alert((spointerDay.get(Calendar.MONTH)+1)+"月 "+spointerDay.get(Calendar.DATE)+"号 level"+level);
			//新一列column，需判断是否显示/更新年，月
			if(pointerDay.get(Calendar.YEAR) != lastYearAndMonth[0]){
				col.year = Integer.toString(pointerDay.get(Calendar.YEAR));
				lastYearAndMonth[0] = pointerDay.get(Calendar.YEAR);
			}
			if(pointerDay.get(Calendar.MONTH)+1 != lastYearAndMonth[1]){
				col.month = toMonthString(pointerDay.get(Calendar.MONTH)+1);
				lastYearAndMonth[1] = pointerDay.get(Calendar.MONTH)+1;
			}
		}
		//pointer移至下一天
		pointerDay.add(Calendar.DATE, 1);
	}
	private void handleEventColumn(int[] lastYearAndMonth, Calendar pointerDay, EventColumn col, int level, boolean isFirst){
		if(!isFirst){
			handleEventColumn(lastYearAndMonth, pointerDay, col, level);
			return;
		}
		//第一列column，必会判断是否显示/更新年，月
		col.year = Integer.toString(pointerDay.get(Calendar.YEAR));
		lastYearAndMonth[0] = pointerDay.get(Calendar.YEAR);

		col.month = toMonthString(pointerDay.get(Calendar.MONTH)+1);
		lastYearAndMonth[1] = pointerDay.get(Calendar.MONTH)+1;
		
		//填充空白 即默认0
		col.blocks[pointerDay.get(Calendar.DAY_OF_WEEK)-1] = level;
		if(pointerDay.get(Calendar.DAY_OF_WEEK)-1 == 6){//开始新的一周
			eventColumnList.add(new EventColumn(col));
			col.reset();
		}
		//pointer移至下一天
		pointerDay.add(Calendar.DATE, 1);
	}
}
