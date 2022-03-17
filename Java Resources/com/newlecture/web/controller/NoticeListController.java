package com.newlecture.web.controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.newlecture.web.entity.Notice;

@WebServlet("/notice/list")
public class NoticeListController extends HttpServlet {
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		List<Notice> list = new ArrayList<>();
		
		String url = "jdbc:oracle:thin:@211.204.34.28:1521/xepdb1";
		String sql = "SELECT * FROM NOTICE";
		
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			Connection con = DriverManager.getConnection(url, "NEWLEC", "tntorwndeo1!");
			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery(sql);
			
			while(rs.next()) {
				int id = rs.getInt("ID");
				String title = rs.getString("TITLE");
				String writerId = rs.getString("WRITER_ID");
				Date regDate = rs.getDate("REGDATE");
				String hit = rs.getString("HIT");
				
				Notice notice = new Notice (
							id,
							title,
							regDate,
							writerId,
							hit
						);
				
				list.add(notice);
			}
			
			rs.close();
			st.close();
			con.close(); 
		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		request.setAttribute("list", list);
		
		//forward방식의 이용 - dispatcher
		request.getRequestDispatcher("/WEB-INF/view/notice/list.jsp").forward(request, response);
	}
}
