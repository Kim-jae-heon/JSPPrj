package com.newlecture.web.controller.admin.notice;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.newlecture.web.entity.NoticeView;
import com.newlecture.web.service.NoticeService;

@WebServlet("/admin/board/notice/list")
public class ListController extends HttpServlet {
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String[] openIds = request.getParameterValues("open-id");//3,5,8
		String[] delIds =request.getParameterValues("del-id");
		String cmd = request.getParameter("cmd");
		String ids_ = request.getParameter("ids");
		String[] ids = ids_.trim().split(" "); 
		
		NoticeService service = new NoticeService();
		switch(cmd) {
		case "일괄공개" :
			for(String openId : openIds) 
				System.out.printf("open id : %s\n", openId);
			
			List<String> oids = Arrays.asList(openIds);
			//1,2,3,4,5,6,7,8,9,10 - 3,5,8
			//1,2,4,6,7,9,10
			List<String> cids = new ArrayList(Arrays.asList(ids));
			cids.removeAll(oids);
			System.out.println(Arrays.asList(ids));
			System.out.println(oids);
			System.out.println(cids);
			
			//Transaction처리
			service.pubNoticeAll(oids,cids);
			
			break;
			
		case "일괄삭제" :
			
			int[] Ids1 = new int[delIds.length];
			for(int i = 0;  i< delIds.length; i++)
				Ids1[i] = Integer.parseInt(delIds[i]);
			
			//int result = service.deleteNotice(ids);
			break;
		}
		
		response.sendRedirect("list");
		
	}
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//url은 있는데 요청 정보를 받을 수 있는 메서드가 없다면 405가 뜬다.
		//url도 있고 메서도드로 있는데 권한이 없다면 403이 뜬다.
		String field_ = request.getParameter("f");
		String query_ = request.getParameter("q");
		String page_ = request.getParameter("p");
		
		String field = "title";
		if(field_ != null && !field_.equals(""))
			field = field_;
		
		String query = "";
		if(query_ != null && !query_.equals(""))
			query = query_;
		
		int page = 1;
		if(page_ != null && !page_.equals(""))
			page = Integer.parseInt(page_);
		
		NoticeService service = new NoticeService();
		List<NoticeView> list = service.getNoticeList(field, query, page);
		int count = service.getNoticeCount(field, query);
		
		request.setAttribute("list", list);
		request.setAttribute("count", count);
		
		//forward방식의 이용 - dispatcher
		request.getRequestDispatcher("/WEB-INF/view/admin/board/notice/list.jsp").forward(request, response);
	}
}
