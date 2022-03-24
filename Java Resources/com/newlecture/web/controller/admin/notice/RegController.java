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
	//일정량 이상 넘어가면 디스크로 저장장소를 옮기자.그러나 location을 설정하지 않음.
	fileSizeThreshold = 1024*1024, //1mb이상은 디스크를 쓰자.
	maxFileSize=1024*1024*50, //사용자가 한 번에 보낼 수 있는 데이터 양을 제한. 하나의 파일사이즈의 최대 용량을 제한. 50mb로 설정.
	maxRequestSize=1024*1024*50*5 //전체 요청에 대한 사이즈. 전체 용량은 250mb를 초과할 수 없다.  
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
			
			Part filePart = p; //part정보에 있는 컨텐트 얻는 방법
			String fileName = filePart.getSubmittedFileName();
			InputStream fis = filePart.getInputStream();//file input stream도 말 그대로 파일을 받아주는 통로임
			builder.append(fileName);
			builder.append(",");
			
			String realPath = request.getServletContext().getRealPath("/member/upload"); //함수가 물리경로를 알려줌
			System.out.println(realPath);
			
			File path = new File(realPath);
			if(!path.exists())
				path.mkdirs();
			
			String filePath = realPath + File.separator + fileName;
			FileOutputStream fos = new FileOutputStream(filePath);
			
			//int b = fis.read(); //read의 반환타입은 정수형.
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
