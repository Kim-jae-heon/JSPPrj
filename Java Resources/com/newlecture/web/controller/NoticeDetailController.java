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
			/* �� ģ���� ��üȭ ����
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
		//servlet���� servlet���� ������ �� �ִ� ��� 2����.
		//redirect: servletȣ���ߴµ� �ٸ� �������� �������� ���. �α��� �������� �� servlet���� �α��� �������� �ٽ� ���������� ���. 
		
		//forward: �� ���������� �۾��ߴ� ����� �̾�޾Ƽ� �ٸ� ���������� ó���ϰԲ� �ϰ� ���� ��. dispatcher�� ���.
		request.getRequestDispatcher("/WEB-INF/view/notice/detail.jsp").forward(request, response);
	}

}
