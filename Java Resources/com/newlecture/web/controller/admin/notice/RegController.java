package com.newlecture.web.controller.admin.notice;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import com.newlecture.web.entity.Notice;
import com.newlecture.web.service.NoticeService;

@MultipartConfig(
	//������ �̻� �Ѿ�� ��ũ�� ������Ҹ� �ű���.�׷��� location�� �������� ����.
	fileSizeThreshold = 1024*1024, //1mb�̻��� ��ũ�� ����.
	maxFileSize=1024*1024*50, //����ڰ� �� ���� ���� �� �ִ� ������ ���� ����. �ϳ��� ���ϻ������� �ִ� �뷮�� ����. 50mb�� ����.
	maxRequestSize=1024*1024*50*5 //��ü ��û�� ���� ������. ��ü �뷮�� 250mb�� �ʰ��� �� ����.  
)
@WebServlet("/admin/board/notice/reg")
public class RegController extends HttpServlet{
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		request.getRequestDispatcher("/WEB-INF/view/admin/board/notice/reg.jsp").forward(request, response);
	}
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String title = request.getParameter("title");
		String content = request.getParameter("content"); 
		String isOpen = request.getParameter("open");
		
		Collection<Part> parts = request.getParts();
		StringBuilder builder = new StringBuilder();
		for(Part p : parts) {
			if(!p.getName().equals("file")) continue;
			if(p.getSize() == 0) continue;
			
			Part filePart = p; //part������ �ִ� ����Ʈ ��� ���
			String fileName = filePart.getSubmittedFileName();
			InputStream fis = filePart.getInputStream();//file input stream�� �� �״�� ������ �޾��ִ� �����
			builder.append(fileName);
			builder.append(",");
			
			String realPath = request.getServletContext().getRealPath("/member/upload"); //�Լ��� ������θ� �˷���
			System.out.println(realPath);
			
			File path = new File(realPath);
			if(!path.exists())
				path.mkdirs();
			
			String filePath = realPath + File.separator + fileName;
			FileOutputStream fos = new FileOutputStream(filePath);
			
			//int b = fis.read(); //read�� ��ȯŸ���� ������.
			byte[] buf = new byte[1024];
			int size = 0;
			while((size = fis.read(buf)) != -1) {
				fos.write(buf, 0, size);
			}
			
			fos.close();
			fis.close();
		
		}
		
		builder.delete(builder.length()-1, builder.length());
		
		boolean pub = false;
		if(isOpen != null)
			pub = true;
		
		Notice notice = new Notice();
		notice.setTitle(title);
		notice.setContent(content);
		notice.setPub(pub);
		notice.setWriterId("newlec");
		notice.setFiles(builder.toString());
		
		NoticeService service = new NoticeService();
		int result = service.insertNotice(notice);
		
		response.sendRedirect("list");
	}
}
