package com.example.fifth;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.ArrayAdapter;
import android.widget.TextView;


public class ItemAdapter extends ArrayAdapter<Item>{
	private int resourceId;
	public ItemAdapter(Context context, int itemViewResourceId, List<Item> items){
		super(context, itemViewResourceId, items);
		resourceId = itemViewResourceId;
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent){
		Item item = getItem(position);
		//重要
		item.getDbData(position);
		//
		View view;
		ViewHolder viewHolder;
		if(convertView==null){
			view = LayoutInflater.from(getContext()).inflate(resourceId, null);
			viewHolder = new ViewHolder();
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
		
		viewHolder.itemTitle.setText(item.title);
		viewHolder.itemWordCount.setText("字数："+item.wordCount);
		viewHolder.itemCreateTime.setText("创建:"+item.createTime);
		viewHolder.itemEditTime.setText("修改:"+item.editTime);
		viewHolder.itemSummary.setText(item.content);
		setLinesLimit(viewHolder.itemSummary);
		
		viewHolder.itemTags.setText(item.tagsString);
		
		return view;
	}
	public void setLinesLimit(final TextView itemSummary) {  
        //测试  
        ViewTreeObserver observer = itemSummary.getViewTreeObserver(); //textAbstract为TextView控件  
        observer.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {  
  
            @Override  
            public void onGlobalLayout() {
                ViewTreeObserver obs = itemSummary.getViewTreeObserver();  
                obs.removeOnGlobalLayoutListener(this);
                if(itemSummary.getLineCount() > 3){//TODO: 可设置
                    //int length=itemSummary.getText().length();  
                    int lineEndIndex = itemSummary.getLayout().getLineEnd(3 - 1); //设置第六行打省略号
                    String text = itemSummary.getText().toString().substring(0,lineEndIndex - 1) + " ..." ;  
                    itemSummary.setText(text);
                }  
            }  
        });  
          
    } 
	class ViewHolder{
		public TextView itemTitle;
		public TextView itemWordCount;
		public TextView itemCreateTime;
		public TextView itemEditTime;
		public TextView itemTags;
		public TextView itemSummary;
	}
}
