package com.newlecture.web;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/calc")
public class Calc extends HttpServlet {
	@Override
	protected void service(HttpServletRequest req, 
			HttpServletResponse resp) 
					throws ServletException, IOException {
		PrintWriter out = resp.getWriter();
		
		String x = req.getParameter("x");
		String y = req.getParameter("y");
		String op = req.getParameter("operator");
		
		int ax = Integer.parseInt(x);
		int why = Integer.parseInt(y);
		
		int result = 0;
		if(op.equals("µ¡¼À"))
			result = ax + why;
		else
			result = ax - why;
		
		out.println(result);
	}
}
