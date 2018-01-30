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
		//��Ҫ
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
		viewHolder.itemWordCount.setText("������"+item.wordCount);
		viewHolder.itemCreateTime.setText("����:"+item.createTime);
		viewHolder.itemEditTime.setText("�޸�:"+item.editTime);
		viewHolder.itemSummary.setText(item.content);
		setLinesLimit(viewHolder.itemSummary);
		
		viewHolder.itemTags.setText(item.tagsString);
		
		return view;
	}
	public void setLinesLimit(final TextView itemSummary) {  
        //����  
        ViewTreeObserver observer = itemSummary.getViewTreeObserver(); //textAbstractΪTextView�ؼ�  
        observer.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {  
  
            @Override  
            public void onGlobalLayout() {
                ViewTreeObserver obs = itemSummary.getViewTreeObserver();  
                obs.removeOnGlobalLayoutListener(this);
                if(itemSummary.getLineCount() > 3){//TODO: ������
                    //int length=itemSummary.getText().length();  
                    int lineEndIndex = itemSummary.getLayout().getLineEnd(3 - 1); //���õ����д�ʡ�Ժ�
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
