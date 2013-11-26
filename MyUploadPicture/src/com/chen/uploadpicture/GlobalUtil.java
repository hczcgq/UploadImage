package com.chen.uploadpicture;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.FileEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class GlobalUtil {
	public static ProgressDialog dialog;
	public final static String IMAGE_URL="http://t1.baidu.com/it/u=1811746958,1644650383&fm=116&gp=0.jpg";
	public final static String Service_URL="http://192.168.1.186:8080/SaveimageService/servlet/resload";
	
	public static Bitmap getRemoteImage(URL url) {
		URLConnection conn;
		Bitmap bm;
		try {
			conn = url.openConnection();
			conn.setDoInput(true);
			conn.connect();
			InputStream is = conn.getInputStream();
			BufferedInputStream bis = new BufferedInputStream(is);
			
			bm = BitmapFactory.decodeStream(bis);
			bis.close();
			is.close();
			return bm;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	public static void showDialog(Context context,String title){
		if((dialog==null)||!(dialog.isShowing())){
			dialog=ProgressDialog.show(context, title, "请稍后...",true,true);
		}
	}
	
	public static void cancleDialog(){
		if(dialog!=null||dialog.isShowing()){
			dialog.dismiss();
		}
	}


	/**
	 * 保存图片
	 * @param result
	 * @param path
	 * @param name
	 */
	public static void SaveImage(Bitmap result, String path, String name) {
		File dirFile = new File(path);
		System.out.println(dirFile+"    "+dirFile.exists());
		if (!dirFile.exists()) {
			dirFile.mkdir();
		}
		File myCaptureFile = new File(path + name);
		BufferedOutputStream bos;
		try {
			FileOutputStream out=new FileOutputStream(myCaptureFile);
			bos = new BufferedOutputStream(out);
			result.compress(Bitmap.CompressFormat.JPEG, 80, bos);
			bos.flush();
			bos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static String Upload(String url,String path,String name) {
		try {
			HttpClient client = new DefaultHttpClient();
			HttpPost post = new HttpPost(url);
			File file = new File(path+name);
			FileEntity entity = new FileEntity(file, "binary/octet-stream");
			HttpResponse response;
			post.setEntity(entity);
			entity.setContentEncoding("binary/octet-stream");
			response = client.execute(post);
			System.out.println(response.getStatusLine().getStatusCode());
			if (response.getStatusLine().getStatusCode() == 200) {
				return EntityUtils.toString(response.getEntity());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
