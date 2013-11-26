package com.chen.uploadpicture;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

import com.chen.uploadpicture.Network.uploadTask;

import android.R.color;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class Nativep extends Activity{

	private Bitmap bitmap;
	private ImageView image;
	private Button choise;
	private String path=Environment.getExternalStorageDirectory() +"/head/";
	private String name="temp.jpg";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.nativep);
		
		image=(ImageView) findViewById(R.id.image);
		choise=(Button) findViewById(R.id.choise);
		
		choise.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				CharSequence[] arrayOfCharSequence = { "相册", "拍照" };
				new AlertDialog.Builder(Nativep.this)
						.setTitle("选择图片")
						.setItems(arrayOfCharSequence,
								new DialogInterface.OnClickListener() {
									public void onClick(
											DialogInterface paramAnonymousDialogInterface,
											int paramAnonymousInt) {
										if (paramAnonymousInt == 1) {
											Intent localIntent1 = new Intent(
													"android.media.action.IMAGE_CAPTURE");
											Nativep.this
													.startActivityForResult(
															localIntent1, 1);
											return;
										}
										Intent localIntent2 = Nativep
												.getImageClipIntent();
										Nativep.this.startActivityForResult(
												localIntent2, 0);
									}
								}).create().show();
			}
		});
	}
	
	
	/**
	 * 拍照获取图片
	 * @return
	 */
	public static Intent getImageClipIntent() {
		Intent localIntent = new Intent("android.intent.action.GET_CONTENT",
				null);
		localIntent.setType("image/*");
		localIntent.putExtra("crop", "true");
		localIntent.putExtra("aspectX", 1);
		localIntent.putExtra("aspectY", 1);
		localIntent.putExtra("outputX", 60);
		localIntent.putExtra("outputY", 60);
		localIntent.putExtra("return-data", true);
		return localIntent;
	}
	
	
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		ContentResolver resolver = getContentResolver();
		/**
		 * 因为两种方式都用到了startActivityForResult方法， 这个方法执行完后都会执行onActivityResult方法，
		 * 所以为了区别到底选择了那个方式获取图片要进行判断，
		 * 这里的requestCode跟startActivityForResult里面第二个参数对应
		 */
		System.out.println(requestCode+"  "+RESULT_CANCELED+"  "+data);
		if (requestCode == 0 && data!=null) {
			bitmap = ((Bitmap) data.getParcelableExtra("data"));
//			String str3 = this.UniqueId + ".jpg";
			ByteArrayOutputStream localByteArrayOutputStream2 = new ByteArrayOutputStream();
			bitmap.compress(Bitmap.CompressFormat.JPEG, 100,
					localByteArrayOutputStream2);
//			String str4 = bytesToHexString(localByteArrayOutputStream2
//					.toByteArray());
			new Thread() {
				public void run() {
					GlobalUtil.SaveImage(bitmap,path, name);
				}
			}.start();
			uploadTask task = new uploadTask();
			task.execute(GlobalUtil.Service_URL);
		} else if (requestCode == 1 &&data!=null) {
			bitmap = ((Bitmap) data.getExtras().get("data"));
//			String str1 = this.UniqueId + ".jpg";
			ByteArrayOutputStream localByteArrayOutputStream1 = new ByteArrayOutputStream();
			bitmap.compress(Bitmap.CompressFormat.JPEG, 100,
					localByteArrayOutputStream1);
//			String str2 = bytesToHexString(localByteArrayOutputStream1
//					.toByteArray());
			new Thread() {
				public void run() {
					GlobalUtil.SaveImage(bitmap,path, name);
				}
			}.start();
			
			uploadTask task = new uploadTask();
			task.execute(GlobalUtil.Service_URL);
		}
	}
	
	/**
	 * 将字节转换成字符串
	 * @param paramArrayOfByte
	 * @return
	 */
	public static String bytesToHexString(byte[] paramArrayOfByte) {
		StringBuilder localStringBuilder = new StringBuilder("");
		if ((paramArrayOfByte == null) || (paramArrayOfByte.length <= 0))
			return null;
		for (int i = 0;; i++) {
			if (i >= paramArrayOfByte.length)
				return localStringBuilder.toString();
			String str = Integer.toHexString(0xFF & paramArrayOfByte[i]);
			if (str.length() < 2)
				localStringBuilder.append(0);
			localStringBuilder.append(str);
		}
	}
	
	
	class uploadTask extends AsyncTask<String, Void, String>{

		@Override
		protected String doInBackground(String... params) {
			String url=params[0];
			return GlobalUtil.Upload(url, path, name);
		}
		@Override
		protected void onPostExecute(String result) {
			GlobalUtil.cancleDialog();
			if(result!=null){
				image.setImageBitmap(bitmap);
				Toast.makeText(Nativep.this, "图片上传成功", 0).show();
			}
			super.onPostExecute(result);
		}
		
		@Override
		protected void onPreExecute() {
			GlobalUtil.showDialog(Nativep.this, "正在上传");
			super.onPreExecute();
		}
		
	}
}
