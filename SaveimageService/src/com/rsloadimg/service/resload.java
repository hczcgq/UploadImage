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
		System.out.println("����Ϣ");
		// ���������ļ�������ֽ�
		int MAX_SIZE = 102400 * 102400;
		// �����ļ�������
		DataInputStream in = null;
		FileOutputStream fileOut = null;
		// ȡ�ÿͻ����ϴ�����������
		String contentType = request.getContentType();
		String str=request.getParameter("parame");
		//request.get
		System.out.println("-------------------------" + contentType);
		if (contentType.indexOf("binary/octet-stream") >= 0) {
			// �����ϴ�������
			in = new DataInputStream(request.getInputStream());
			int formDataLength = request.getContentLength();
			// ���ͼƬ����
			System.out.println(""+formDataLength);
			if (formDataLength > MAX_SIZE) {
				String errormsg = ("�ϴ����ļ��ֽ��������Գ���" + MAX_SIZE);
				System.out.println(errormsg);
				return;
			}

			// �����ϴ��ļ�������
			byte dataBytes[] = new byte[formDataLength];
			int byteRead = 0;
			int totalBytesRead = 0;
			// �ϴ������ݱ�����byte����
			while (totalBytesRead < formDataLength) {
				byteRead = in.read(dataBytes, totalBytesRead, formDataLength);
				totalBytesRead += byteRead;
			}
			String fileName = filePath+ new SimpleDateFormat("yyyyMMdd_HHmmss")
			.format(new Date())+".png";
			//String fileName = filePath + "123a111" + ".png";
			// ��������ļ���Ŀ¼�Ƿ����
			File fileDir = new File(filePath);
			if (!fileDir.exists()) {
				fileDir.mkdirs();

			}
			// �����ļ���д����
			fileOut = new FileOutputStream(fileName);
			// �����ļ�������
			fileOut.write(dataBytes);
			fileOut.close();
		}
		PrintWriter out = response.getWriter();
		out.print("yes");
	}
}
