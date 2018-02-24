package com.voyd.safernote;

import java.util.List;

import com.voyd.safernote.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;


public class ItemAdapter extends ArrayAdapter<Item>{
	private int resourceId;
	private Item item;
	public ItemAdapter(Context context, int itemViewResourceId, List<Item> items){
		super(context, itemViewResourceId, items);
		resourceId = itemViewResourceId;
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent){
		item = getItem(position);
		//从数据库加载
		item.loadDbData(position);
		View view;
		ViewHolder viewHolder;
		if(convertView==null){
			view = LayoutInflater.from(getContext()).inflate(resourceId, null);
			viewHolder = new ViewHolder();
			viewHolder.itemStick = (ImageView)view.findViewById(R.id.itemStick);
			viewHolder.itemTitle = (TextView)view.findViewById(R.id.itemTitle);
			viewHolder.itemWordCount = (TextView)view.findViewById(R.id.itemWordCount);
			viewHolder.itemCreateTime = (TextView)view.findViewById(R.id.itemCreateTime);
			viewHolder.itemEditTime = (TextView)view.findViewById(R.id.itemEditTime);
			viewHolder.itemTags = (TextView)view.findViewById(R.id.itemTags);
			viewHolder.itemSummary = (TextView)view.findViewById(R.id.itemSummary);
			view.setTag(viewHolder);
		}else{
			view = convertView;
			viewHolder = (ViewHolder)view.getTag();
		}
		
		if(item.stick > 0){
			viewHolder.itemStick.setVisibility(View.VISIBLE);
		}else{
			viewHolder.itemStick.setVisibility(View.GONE);
		}
		
		viewHolder.itemTitle.setText(item.title);
		viewHolder.itemWordCount.setText("字数："+item.wordCount);
		viewHolder.itemCreateTime.setText("创建:"+item.createTime);
		viewHolder.itemEditTime.setText("修改:"+item.editTime);
		if(item.tagsString.equals("")){
			viewHolder.itemTags.setText("无");
		}else{
			viewHolder.itemTags.setText(item.tagsString);
		}
		viewHolder.itemSummary.setMaxLines(3);
		viewHolder.itemSummary.setText(item.content.replace("\n", "  "));//TODO: 可设置全文，缩略，仅标题
		
		return view;
	}
	class ViewHolder{
		public ImageView itemStick;
		public TextView itemTitle;
		public TextView itemWordCount;
		public TextView itemCreateTime;
		public TextView itemEditTime;
		public TextView itemTags;
		public TextView itemSummary;
	}
}
