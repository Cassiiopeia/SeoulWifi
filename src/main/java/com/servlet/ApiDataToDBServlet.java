package com.servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;



import com.lib.controller.ApiDataToDB;

@WebServlet("/ApiDataToDBServlet")
public class ApiDataToDBServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       

    public ApiDataToDBServlet() {
        super();

    }


	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int totalItemCount = ApiDataToDB.runApiDataToDB(); 
        String resultMessage = Integer.toString(totalItemCount) + "개의 WIFI 정보를 정상적으로 저장하였습니다.";
        
        request.getSession().setAttribute("resultMessage", resultMessage);
        response.getWriter().append(resultMessage);
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		doGet(request, response);
	}

}
