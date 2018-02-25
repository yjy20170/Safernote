package com.voyd.safernote;

import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class EventColumnAdapter  extends ArrayAdapter<EventColumn>{
	private int resourceId;
	private EventColumn eventColomn;
	private ViewHolder viewHolder;
	public EventColumnAdapter(Context context, int resourceId,List<EventColumn> objects){
		super(context, resourceId, objects);
		this.resourceId = resourceId;
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent){
		eventColomn = getItem(position);

		View view;
		if(convertView==null){
			view = LayoutInflater.from(getContext()).inflate(resourceId, null);
			viewHolder = new ViewHolder();
			viewHolder.year = (TextView)view.findViewById(R.id.event_year);
			viewHolder.month = (TextView)view.findViewById(R.id.event_month);
			viewHolder.eventBlocks[0] = view.findViewById(R.id.event_block_0);
			viewHolder.eventBlocks[1] = view.findViewById(R.id.event_block_1);
			viewHolder.eventBlocks[2] = view.findViewById(R.id.event_block_2);
			viewHolder.eventBlocks[3] = view.findViewById(R.id.event_block_3);
			viewHolder.eventBlocks[4] = view.findViewById(R.id.event_block_4);
			viewHolder.eventBlocks[5] = view.findViewById(R.id.event_block_5);
			viewHolder.eventBlocks[6] = view.findViewById(R.id.event_block_6);
			view.setTag(viewHolder);
		}else{
			view = convertView;
			viewHolder = (ViewHolder)view.getTag();
		}
		//设置颜色和年月
		for(int i=0;i<7;i++){
			setColor(i);
		}
		viewHolder.year.setText(eventColomn.year);
		viewHolder.month.setText(eventColomn.month);
		return view;
	}
	private void setColor(int i){
		int level = eventColomn.blocks[i];
		if(level == -1){
			viewHolder.eventBlocks[i].setBackgroundColor(Color.argb(0, 0, 0, 0));
		}else{
			int color = Color.parseColor(EventColumn.color[level]);
			viewHolder.eventBlocks[i].setBackgroundColor(color);
		}
	}
	class ViewHolder{
		TextView year;
		TextView month;
		View[] eventBlocks = {null, null, null, null, null, null, null};
	}
}
