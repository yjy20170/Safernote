package com.voyd.safernote;

import java.util.List;

import com.voyd.safernote.R;

import android.content.Context;
import android.text.TextUtils.TruncateAt;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
			
			viewHolder.itemCreateAndEditTimeLine = (LinearLayout)view.findViewById(R.id.itemCreateAndEditTimeLine);
			viewHolder.itemCreateTime = (TextView)view.findViewById(R.id.itemCreateTime);
			viewHolder.itemEditTime = (TextView)view.findViewById(R.id.itemEditTime);
			
			viewHolder.itemTagLine = (LinearLayout)view.findViewById(R.id.itemTagLine);
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
		
		//根据setting设置是否显示
		viewHolder.itemWordCount.setVisibility(
				MyApplication.getSetting("isShowWordCount")==1//TODO 减少读取数据库次数
					?View.VISIBLE:View.GONE);
		viewHolder.itemCreateAndEditTimeLine.setVisibility(
				MyApplication.getSetting("isShowCreateAndEditTime")==1
					?View.VISIBLE:View.GONE);
		viewHolder.itemTagLine.setVisibility(
				MyApplication.getSetting("isShowTags")==1
					?View.VISIBLE:View.GONE);
		viewHolder.itemSummary.setVisibility(
				MyApplication.getSetting("isShowSummary")==1
					?View.VISIBLE:View.GONE);
		viewHolder.itemTitle.setText(item.title);
		viewHolder.itemWordCount.setText("字数："+item.wordCount);
		viewHolder.itemCreateTime.setText("创建:"+item.createTime);
		viewHolder.itemEditTime.setText("修改:"+item.editTime);
		if(item.tagsString.equals("")){
			viewHolder.itemTags.setText("无");
		}else{
			viewHolder.itemTags.setText(item.tagsString);
		}
		if(MyApplication.getSetting("summaryLength")==0){
			viewHolder.itemSummary.setMaxLines(3);
			viewHolder.itemSummary.setEllipsize(TruncateAt.END);
			viewHolder.itemSummary.setText(item.content.replace("\n", "  "));
		}else{
			viewHolder.itemSummary.setMaxLines(Integer.MAX_VALUE);
			viewHolder.itemSummary.setEllipsize(null);
			viewHolder.itemSummary.setText(item.content);
		}
		
		return view;
	}
	class ViewHolder{
		public ImageView itemStick;
		public TextView itemTitle;
		public TextView itemWordCount;
		public LinearLayout itemCreateAndEditTimeLine;
		public TextView itemCreateTime;
		public TextView itemEditTime;
		public LinearLayout itemTagLine;
		public TextView itemTags;
		public TextView itemSummary;
	}
}
