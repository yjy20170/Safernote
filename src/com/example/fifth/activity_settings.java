package com.example.fifth;

import android.os.Bundle;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class activity_settings extends SafeActivity implements OnCheckedChangeListener{
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_settings);
		((RadioGroup)findViewById(R.id.set_safety_level)).setOnCheckedChangeListener(this);
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
			MyApplication.updateSafetyLevel(0);//开启时要求长密码，切入时无需密码
			break;
		case R.id.safety_level_1:
			MyApplication.updateSafetyLevel(1);//开启时要求长密码，切入时可使用短密码，错误1次后要求长密码
			break;
		case R.id.safety_level_2:
			MyApplication.updateSafetyLevel(2);//始终要求长密码
			break;
		}
	}
}
