package com.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import org.json.*;

@WebServlet("/BookmarkGroupServlet")
public class BookmarkGroupServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public BookmarkGroupServlet() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");

        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM bookmark_group_table");
             ResultSet resultSet = preparedStatement.executeQuery()) {

            JSONArray jsonArray = new JSONArray();
            while (resultSet.next()) {
                JSONObject jsonRow = new JSONObject();
                jsonRow.put("ID", resultSet.getInt("ID"));
                jsonRow.put("북마크이름", resultSet.getString("북마크이름"));
                jsonRow.put("순서", resultSet.getInt("순서"));
                jsonRow.put("등록일자", resultSet.getTimestamp("등록일자").toString());

                Timestamp 수정일자 = resultSet.getTimestamp("수정일자");
                jsonRow.put("수정일자", (수정일자 != null) ? 수정일자.toString() : "");
                jsonArray.put(jsonRow);
            }
            response.getWriter().print(jsonArray.toString());
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().print("{\"error\":\"" + e.getMessage() + "\"}");
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");

        String bookmarkName = request.getParameter("bookmarkName");
        int bookmarkOrder = Integer.parseInt(request.getParameter("bookmarkOrder"));

        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO bookmark_group_table (북마크이름, 순서, 등록일자) VALUES (?, ?, NOW())",
                 Statement.RETURN_GENERATED_KEYS)) {
            
            preparedStatement.setString(1, bookmarkName);
            preparedStatement.setInt(2, bookmarkOrder);
            int affectedRows = preparedStatement.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Creating bookmark failed, no rows affected.");
            }

            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    JSONObject jsonRow = new JSONObject();
                    jsonRow.put("북마크이름", bookmarkName);
                    jsonRow.put("관리번호", generatedKeys.getInt(1));
                    response.getWriter().print(jsonRow.toString());
                } else {
                    throw new SQLException("Creating bookmark failed, no ID obtained.");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    private Connection getConnection() throws SQLException, ClassNotFoundException {
        String jdbcUrl = "jdbc:mysql://220.85.169.165/seoul_wifi_info";
        String username = "chan4760";
        String password = "Tjtocks178@";
        Class.forName("org.mariadb.jdbc.Driver");
        return DriverManager.getConnection(jdbcUrl, username, password);
    }
}
