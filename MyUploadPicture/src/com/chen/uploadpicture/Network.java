package com.chen.uploadpicture;

import java.net.MalformedURLException;
import java.net.URL;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class Network extends Activity{

	private ImageView image;
	private Button get,upload;
	private String path=Environment.getExternalStorageDirectory() +"/download_test/";
	private String name="temp.jpg";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.network);
		
		
		image=(ImageView) findViewById(R.id.image);
		get=(Button) findViewById(R.id.get);
		upload=(Button) findViewById(R.id.upload);
		
		get.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				getTask task=new getTask();
				task.execute(GlobalUtil.IMAGE_URL);
			}
		});
		
		upload.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				uploadTask task=new uploadTask();
				task.execute(GlobalUtil.Service_URL);
			}
		});
	}
	
	
	class getTask extends AsyncTask<String, Void, Bitmap>{

		@Override
		protected Bitmap doInBackground(String... params) {
			String url=params[0];
			try {
				return GlobalUtil.getRemoteImage(new URL(url));
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
			return null;
		}
		
		@Override
		protected void onPostExecute(final Bitmap result) {
			super.onPostExecute(result);
			GlobalUtil.cancleDialog();
			if(result!=null){
				new Thread(){
					public void run() {
						GlobalUtil.SaveImage(result,path,name);
					};
				}.start();
				image.setImageBitmap(result);
				
				
			}
		}
		
		@Override
		protected void onPreExecute() {
			GlobalUtil.showDialog(Network.this,"正在加载");
			super.onPreExecute();
		}

		
	}

	class uploadTask extends AsyncTask<String, Void, String>{
		
		@Override
		protected String doInBackground(String... params) {
			String url=params[0];
			return GlobalUtil.Upload(url,path,name);
		}

		
		@Override
		protected void onPostExecute(String result) {
			GlobalUtil.cancleDialog();
			if(result!=null){
				Toast.makeText(Network.this, "图片上传成功", 0).show();
			}
			super.onPostExecute(result);
		}
		
		@Override
		protected void onPreExecute() {
			GlobalUtil.showDialog(Network.this, "正在上传");
			super.onPreExecute();
		}
		
	}
}
