package com.rsloadimg.service;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class resload extends HttpServlet {

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		

	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		System.out.println("mmmmmmmm");
		String filePath = "D:\\Program Files\\tomcat7.0\\apache-tomcat-7.0.33\\webapps\\saveimg\\";
		System.out.println("有消息");
		// 定义上载文件的最大字节
		int MAX_SIZE = 102400 * 102400;
		// 声明文件读入类
		DataInputStream in = null;
		FileOutputStream fileOut = null;
		// 取得客户端上传的数据类型
		String contentType = request.getContentType();
		String str=request.getParameter("parame");
		//request.get
		System.out.println("-------------------------" + contentType);
		if (contentType.indexOf("binary/octet-stream") >= 0) {
			// 读入上传的数据
			in = new DataInputStream(request.getInputStream());
			int formDataLength = request.getContentLength();
			// 如果图片过大
			System.out.println(""+formDataLength);
			if (formDataLength > MAX_SIZE) {
				String errormsg = ("上传的文件字节数不可以超过" + MAX_SIZE);
				System.out.println(errormsg);
				return;
			}

			// 保存上传文件的数据
			byte dataBytes[] = new byte[formDataLength];
			int byteRead = 0;
			int totalBytesRead = 0;
			// 上传的数据保存在byte数组
			while (totalBytesRead < formDataLength) {
				byteRead = in.read(dataBytes, totalBytesRead, formDataLength);
				totalBytesRead += byteRead;
			}
			String fileName = filePath+ new SimpleDateFormat("yyyyMMdd_HHmmss")
			.format(new Date())+".png";
			//String fileName = filePath + "123a111" + ".png";
			// 检查上载文件的目录是否存在
			File fileDir = new File(filePath);
			if (!fileDir.exists()) {
				fileDir.mkdirs();

			}
			// 创建文件的写出类
			fileOut = new FileOutputStream(fileName);
			// 保存文件的数据
			fileOut.write(dataBytes);
			fileOut.close();
		}
		PrintWriter out = response.getWriter();
		out.print("yes");
	}
}
