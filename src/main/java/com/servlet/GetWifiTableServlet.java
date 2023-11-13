package com.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;

@WebServlet("/GetWifiTableServlet")
public class GetWifiTableServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public GetWifiTableServlet() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");

        response.setContentType("application/json");
        PrintWriter out = response.getWriter();

        String jdbcUrl = "jdbc:mysql://220.85.169.165/seoul_wifi_info";
        String username = "chan4760";
        String password = "Tjtocks178@";

        try {
            Class.forName("org.mariadb.jdbc.Driver");
            Connection connection = DriverManager.getConnection(jdbcUrl, username, password);
            System.out.println("Connected : GetWifiTableServlet.java");

            double lat = Double.parseDouble(request.getParameter("lat"));
            double lng = Double.parseDouble(request.getParameter("lng"));




            // 정상적인 DB의 쿼리
//            String query = "SELECT *, "
//                    + "(6371 * ACOS(COS(RADIANS(" + lat + ")) * COS(RADIANS(Y좌표)) * COS(RADIANS(X좌표) - RADIANS(" + lng + ")) + SIN(RADIANS(" + lat + ")) * SIN(RADIANS(Y좌표)))) AS distance "
//                    + "FROM wifi_table "
//                    + "WHERE X좌표 <> 0 AND Y좌표 <> 0 "
//                    + "HAVING distance < 25 "
//                    + "ORDER BY distance "
//                    + "LIMIT 100;";
            
            // DB에 LAT LNG값이 반대로 저장되는 경우 ( API 서버문제 )
            String query = "SELECT *, "
                    + "(6371 * ACOS(COS(RADIANS(" + lng + ")) * COS(RADIANS(Y좌표)) * COS(RADIANS(X좌표) - RADIANS(" + lat + ")) + SIN(RADIANS(" + lng + ")) * SIN(RADIANS(Y좌표)))) AS distance "
                    + "FROM wifi_table "
                    + "WHERE X좌표 <> 0 AND Y좌표 <> 0 "
                    + "HAVING distance < 25 "
                    + "ORDER BY distance "
                    + "LIMIT 100;";
             
            System.out.println("GetWifiTableServlet.java : query :" + query +" : Sented");

            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            JSONArray jsonArray = new JSONArray();
            while (resultSet.next()) {
                JSONObject jsonRow = new JSONObject();
                jsonRow.put("distance", resultSet.getDouble("distance"));
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

                jsonArray.put(jsonRow);
                System.out.println(jsonRow);
            }

            out.println(jsonArray.toString());


            System.out.println("GetWifiTableServlet.java : Task End");
            resultSet.close();
            statement.close();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace(System.out); // 로그에 출력
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR); // 오류 응답 코드 설정
            JSONObject error = new JSONObject();
            error.put("error", e.getMessage());
            out.println(error.toString()); // 오류 메시지를 JSON 형식으로 클라이언트에 전송
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

}
