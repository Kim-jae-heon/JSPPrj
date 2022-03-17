package com.newlecture.web.controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.newlecture.web.entity.Notice;

@WebServlet("/notice/detail")
public class NoticeDetailController extends HttpServlet{
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		int id = Integer.parseInt(request.getParameter("id"));

		String url = "jdbc:oracle:thin:@211.204.34.28:1521/xepdb1";
		String sql = "SELECT * FROM NOTICE WHERE ID=?";

		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			Connection con = DriverManager.getConnection(url, "NEWLEC", "tntorwndeo1!");
			PreparedStatement st = con.prepareStatement(sql);
			st.setInt(1,id);
			
			ResultSet rs = st.executeQuery();
			
			rs.next();
			
			String title = rs.getString("TITLE"); 
			Date regDate = rs.getDate("REGDATE");
			String writerId = rs.getString("WRITER_ID");
			String hit = rs.getString("HIT");
			String files = rs.getString("FILES"); 
			String content = rs.getString("CONTENT");
			
			Notice notice = new Notice(
						id,
						title,
						regDate,
						writerId,
						hit,
						files,
						content
					);
			request.setAttribute("n", notice);
			/* 이 친구들 객체화 예정
			request.setAttribute("title", title);
			request.setAttribute("regDate", regDate);
			request.setAttribute("writerId", writerId);
			request.setAttribute("hit", hit);
			request.setAttribute("files", files);
			request.setAttribute("content", content);
			*/
			
			rs.close();
			st.close();
			con.close();  
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//servlet에서 servlet으로 전이할 수 있는 방법 2가지.
		//redirect: servlet호출했는데 다른 페이지로 가버리는 방법. 로그인 실패했을 때 servlet에서 로그인 페이지로 다시 보내버리는 방법. 
		
		//forward: 현 페이지에서 작업했던 내용들 이어받아서 다른 페이지에서 처리하게끔 하고 싶을 때. dispatcher를 사용.
		request.getRequestDispatcher("/WEB-INF/view/notice/detail.jsp").forward(request, response);
	}

}
