package com.example.fifth;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class activity_importExportDb extends SafeActivity implements OnClickListener{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_import_export_db);
        ((Button) findViewById(R.id.finish)).setOnClickListener(this);
        ((Button) findViewById(R.id.export_db)).setOnClickListener(this);
        ((Button) findViewById(R.id.import_db)).setOnClickListener(this);
        ((Button) findViewById(R.id.export_db)).setText(
        		"�����ݵ����� /" + getString(R.string.app_name)
        		+ "/" + getString(R.string.database_name));
    }
    @Override
    public void onClick(View v){
    	switch(v.getId()){
    	case R.id.finish:
    		onBackPressed();
    		break;
    	case R.id.import_db:
    		getImportFilePath();
    		break;
    	case R.id.export_db:
    		//�������ڣ��������ļ���
    		File direct = new File(Environment.getExternalStorageDirectory() + "/"+getString(R.string.app_name));
            if(!direct.exists())
            {
            	if(direct.mkdir()) 
                {
            		//directory is created
                }
            }
            exportDB();
    		break;
    	}
    }
    private void getImportFilePath(){
		Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");//��������
        intent.addCategory(Intent.CATEGORY_OPENABLE);  
        startActivityForResult(intent,1);
	}
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data){
		if (resultCode == Activity.RESULT_OK) {
	        if (requestCode == 1) {
	            Uri uri = data.getData();
	            //TODO �����ļ��Ƿ���Ϲ淶������importDB()
	            //�迼�ǵ���������뵱ǰ���벻һ�µ����
	            new alert("�ļ�·����"+uri.getPath().toString());
	        }  
	    }
	}
    private void importDB(String importFilePath) {
        try {
            File sd = Environment.getExternalStorageDirectory();
            File data  = Environment.getDataDirectory();

            if (sd.canRead()) {
                String  currentDBPath= "//data//" + getString(R.string.package_name)
                        + "//databases//" + getString(R.string.database_name);
                File backupDB = new File(sd, importFilePath);
                File currentDB  = new File(data, currentDBPath);

                FileInputStream inputStream = new FileInputStream(currentDB);
                FileOutputStream outputStream = new FileOutputStream(backupDB);
                FileChannel src = inputStream.getChannel();
                FileChannel dst = outputStream.getChannel();
                dst.transferFrom(src, 0, src.size());
                src.close();
                dst.close();
                inputStream.close();
                outputStream.close();
                new alert(backupDB.toString());

            }
        } catch (Exception e) {

            new alert(e.toString());

        }
    }

    private void exportDB() {
        try {
            File sd = Environment.getExternalStorageDirectory();
            File data = Environment.getDataDirectory();

            if (sd.canWrite()) {
                String  currentDBPath= "//data//" + getString(R.string.package_name)
                        + "//databases//" + getString(R.string.database_name);
                String backupDBPath  = "/" + getString(R.string.app_name)
                		+ "/" + getString(R.string.database_name);
                File currentDB = new File(data, currentDBPath);
                File backupDB = new File(sd, backupDBPath);
                FileInputStream inputStream = new FileInputStream(currentDB);
                FileOutputStream outputStream = new FileOutputStream(backupDB);
                FileChannel src = inputStream.getChannel();
                FileChannel dst = outputStream.getChannel();
                dst.transferFrom(src, 0, src.size());
                src.close();
                dst.close();
                inputStream.close();
                outputStream.close();
                new alert("�����ɹ�");
            }
        } catch (Exception e) {

            new alert(e.toString());

        }
    }

}