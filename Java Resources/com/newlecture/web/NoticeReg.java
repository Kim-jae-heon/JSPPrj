package com.newlecture.web;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/notice-reg")
public class NoticeReg extends HttpServlet {
	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		resp.setCharacterEncoding("UTF-8"); //사용자에게 보내는 코딩방식
		resp.setContentType("text/html; charset=UTF-8"); //사용자가 어떻게 해석할 것인가에 대한 정보. 
		//브라우저가 가지고 있는 코드테이블 중 한글을 해석할 수 있는 데이터를 사용하는 것. 반응으로 생기는 문제를 해결.
		//req.setCharacterEncoding("UTF-8"); //전달하다가 발생하는 문제를 해결하는 방법. 서버가 받아들이는 방법을 설정.
		
		PrintWriter out = resp.getWriter();
		
		String title = req.getParameter("title");
		String content = req.getParameter("content");
		
		out.println(title);
		out.println(content);
		
		}
	}
