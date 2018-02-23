package com.voyd.safernote;

import com.voyd.safernote.R;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class activity_settings extends SafeActivity implements OnCheckedChangeListener,OnClickListener{
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_settings);
		((RadioGroup)findViewById(R.id.set_safety_level)).setOnCheckedChangeListener(this);
		findViewById(R.id.finish).setOnClickListener(this);
		initView();
	}
	void initView(){
		//安全等级
		switch (MyApplication.getSafetyLevel()){
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
		
	}
	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		switch (group.getCheckedRadioButtonId()){
		case R.id.safety_level_0:
			MyApplication.updateSafetyLevel(0);
			break;
		case R.id.safety_level_1:
			MyApplication.updateSafetyLevel(1);
			break;
		case R.id.safety_level_2:
			MyApplication.updateSafetyLevel(2);
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
