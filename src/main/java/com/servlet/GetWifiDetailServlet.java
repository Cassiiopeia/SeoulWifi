package com.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.*;


@WebServlet("/GetWifiDetailServlet")
public class GetWifiDetailServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public GetWifiDetailServlet() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        
        String jdbcUrl = "jdbc:mysql://220.85.169.165/seoul_wifi_info";
        String username = "chan4760";
        String password = "Tjtocks178@";


        String 관리번호 = request.getParameter("관리번호");
        String distance = request.getParameter("distance");
        
        PrintWriter out = response.getWriter();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            Class.forName("org.mariadb.jdbc.Driver");
            connection = DriverManager.getConnection( jdbcUrl, username, password );
            String sql = "SELECT * FROM wifi_table WHERE 관리번호 = ?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, 관리번호);
            
            resultSet = preparedStatement.executeQuery();
            
            JSONObject jsonRow = new JSONObject();
            if (resultSet.next()) {
                jsonRow.put("distance", distance);
                jsonRow.put("관리번호", resultSet.getString("관리번호"));
                jsonRow.put("자치구", resultSet.getString("자치구"));
                jsonRow.put("와이파이명", resultSet.getString("와이파이명"));
                jsonRow.put("도로명주소", resultSet.getString("도로명주소"));
                jsonRow.put("상세주소", resultSet.getString("상세주소"));
                jsonRow.put("설치위치_층", resultSet.getString("설치위치_층"));
                jsonRow.put("설치유형", resultSet.getString("설치유형"));
                jsonRow.put("설치기관", resultSet.getString("설치기관"));
                jsonRow.put("서비스구분", resultSet.getString("서비스구분"));
                jsonRow.put("망종류", resultSet.getString("망종류"));
                jsonRow.put("설치년도", resultSet.getString("설치년도"));
                jsonRow.put("실내외구분", resultSet.getString("실내외구분"));
                jsonRow.put("wifi접속환경", resultSet.getString("wifi접속환경"));
                jsonRow.put("Y좌표", resultSet.getDouble("Y좌표"));
                jsonRow.put("X좌표", resultSet.getDouble("X좌표"));
                jsonRow.put("작업일자", resultSet.getString("작업일자"));
            }
            
            out.print(jsonRow.toString());
            System.out.println(jsonRow);
            resultSet.close();
            preparedStatement.close();
            connection.close();
            out.flush();
            out.close(); 
            
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        } 
    }
}
