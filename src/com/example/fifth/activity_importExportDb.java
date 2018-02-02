package com.example.fifth;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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
    		Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("*/*");//��������
            intent.addCategory(Intent.CATEGORY_OPENABLE);  
            //�������ļ�ѡ������������̨ʱ������������˽��ļ�ѡ������Ϊ����ȫ startActivityForResult
            //���鲻�ã��Ļ�
            startSafeActivityForResult(intent,1);
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
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data){
		//��ִ��SfaActivity��onActivityResult��������isFromStack
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == Activity.RESULT_OK) {
	        if (requestCode == 1) {
	            Uri uri = data.getData();
	            //����importDB()�����ļ��Ƿ���Ϲ淶��
	            //�����ܷ�õ�MD5Password(�˴�ͬMyApplication
	            String MD5Password="";
	            DbHelper dbHelper;
	            SQLiteDatabase db;
	            File toDb;
	            toDb = importDB(uri.getPath().toString(),"temp.db");//getString(R.string.database_name)
	            if(toDb != null){
		            dbHelper = new DbHelper(MyApplication.context, "temp.db", null, 1);
		            db = dbHelper.getWritableDatabase();
	            	try{
			            Cursor cursor = db.rawQuery("select * from settings", null);
			    		cursor.moveToFirst();
			    		MD5Password = cursor.getString(cursor.getColumnIndex("md5password"));
			    		db.close();
			    		dbHelper.close();
			    		new alert("��������");
	            	}catch(Exception e){
		            	new alert("�����쳣: "+e.toString(),"long");
		            	toDb.delete();
		            	return;//������ִ�к���Ĳ���
		            }
	            }else{
		    		return;
	            }
	            
	            //��������У��MD5ֵ����ͨ������ʾ�Ƿ񵼳�ԭ���ݣ�Ȼ�󸲸�ԭ���ݿ�
	            Intent intent = new Intent(this, activity_testNewDb.class);
	            intent.putExtra("MD5Password", MD5Password);
	            startSafeActivityForResult(intent, 1);//�ڷ��ظ�activityʱǿ�Ʋ�lock
	            
	        }
	    }
	}
    public static File importDB(String importFilePath, String dbName){
    	try{
	        File data  = Environment.getDataDirectory();
	        String  currentDBPath= "//data//" + MyApplication.context.getString(R.string.package_name)
	                + "//databases//" + dbName;
	        File backupDB = new File(importFilePath);
	        File currentDB  = new File(data, currentDBPath);
	        if(!currentDB.exists()){
	        	currentDB.createNewFile();
	        }
	        FileInputStream inputStream = new FileInputStream(backupDB);
	        FileOutputStream outputStream = new FileOutputStream(currentDB);
	        FileChannel src = inputStream.getChannel();
	        FileChannel dst = outputStream.getChannel();
	        dst.transferFrom(src, 0, src.size());
	        src.close();
	        dst.close();
	        inputStream.close();
	        outputStream.close();
	        return currentDB;
    	}catch(Exception e){
    		new alert("�����쳣: "+e.toString(),"long");
    		return null;
    	}
    }

    public static void exportDB() {
        try {
            File sd = Environment.getExternalStorageDirectory();
            File data = Environment.getDataDirectory();

            if (sd.canWrite()) {
                String  currentDBPath= "//data//" + MyApplication.context.getString(R.string.package_name)
                        + "//databases//" + MyApplication.context.getString(R.string.database_name);
                String backupDBPath  = "/" + MyApplication.context.getString(R.string.app_name)
                		+ "/" + MyApplication.context.getString(R.string.database_name);
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