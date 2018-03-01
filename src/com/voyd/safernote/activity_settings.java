package com.voyd.safernote;

import com.voyd.safernote.R;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RadioGroup;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.CheckBox;

public class activity_settings extends SafeActivity
		implements RadioGroup.OnCheckedChangeListener,OnClickListener,
			CompoundButton.OnCheckedChangeListener{
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_settings);
		
		((TextView)findViewById(R.id.titlebarText)).setText("设置");
		findViewById(R.id.save).setVisibility(View.INVISIBLE);
		findViewById(R.id.finish).setOnClickListener(this);

		initView();//根据保存的设置初始化radioGroup等

		((RadioGroup)findViewById(R.id.set_safety_level)).setOnCheckedChangeListener(this);
		((CheckBox)findViewById(R.id.check_wordCount)).setOnCheckedChangeListener(this);
		((CheckBox)findViewById(R.id.check_createAndEditTime)).setOnCheckedChangeListener(this);
		((CheckBox)findViewById(R.id.check_tags)).setOnCheckedChangeListener(this);
		((CheckBox)findViewById(R.id.check_summary)).setOnCheckedChangeListener(this);
		((RadioGroup)findViewById(R.id.set_summary_length)).setOnCheckedChangeListener(this);
	}
	void initView(){
		//安全等级
		switch (MyApplication.getSetting("safetyLevel")){
		case 0:
			((RadioGroup)findViewById(R.id.set_safety_level)).check(R.id.safety_level_0);
			break;
		case 1:
			((RadioGroup)findViewById(R.id.set_safety_level)).check(R.id.safety_level_1);
			break;
		case 2:
			((RadioGroup)findViewById(R.id.set_safety_level)).check(R.id.safety_level_2);
			break;
		}
		//视图
		((CheckBox)findViewById(R.id.check_wordCount)).setChecked(
				MyApplication.getSetting("isShowWordCount")==1?true:false);
		((CheckBox)findViewById(R.id.check_createAndEditTime)).setChecked(
				MyApplication.getSetting("isShowCreateAndEditTime")==1?true:false);
		((CheckBox)findViewById(R.id.check_tags)).setChecked(
				MyApplication.getSetting("isShowTags")==1?true:false);
		((CheckBox)findViewById(R.id.check_summary)).setChecked(
				MyApplication.getSetting("isShowSummary")==1?true:false);
		findViewById(R.id.set_summary_length).setVisibility(
				((CheckBox)findViewById(R.id.check_summary)).isChecked()
					?View.VISIBLE:View.GONE);
		((RadioGroup)findViewById(R.id.set_summary_length)).check(
				MyApplication.getSetting("summaryLength")==1?R.id.summary_long:R.id.summary_short);
	}
	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		switch(group.getId()){
		case R.id.set_safety_level:
			switch(checkedId){
			case R.id.safety_level_0:
				MyApplication.updateSetting("safetyLevel", 0);
				break;
			case R.id.safety_level_1:
				MyApplication.updateSetting("safetyLevel", 1);
				break;
			case R.id.safety_level_2:
				MyApplication.updateSetting("safetyLevel", 2);
				break;
			}
			break;
		case R.id.set_summary_length:
			switch(checkedId){
			case R.id.summary_short:
				MyApplication.updateSetting("summaryLength", 0);
				break;
			case R.id.summary_long:
				MyApplication.updateSetting("summaryLength", 1);
				break;
			}
			break;
		}
	}
	@Override
	public void onCheckedChanged(CompoundButton btn, boolean isChecked) {
		switch(btn.getId()){
		case R.id.check_wordCount:
			MyApplication.updateSetting("isShowWordCount", isChecked?1:0);
			break;
		case R.id.check_createAndEditTime:
			MyApplication.updateSetting("isShowCreateAndEditTime", isChecked?1:0);
			break;
		case R.id.check_tags:
			MyApplication.updateSetting("isShowTags", isChecked?1:0);
			break;
		case R.id.check_summary:
			MyApplication.updateSetting("isShowSummary", isChecked?1:0);
			findViewById(R.id.set_summary_length).setVisibility(isChecked?View.VISIBLE:View.GONE);
			break;
		}
	}
	@Override
	public void onClick(View v){
		switch (v.getId()){
		case R.id.finish:
			onBackPressed();
		}
	}
}
