package com.servlet;

import com.homepage.LocationHistory;


import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.time.LocalDateTime;

@WebServlet({ "/LocationHistoryServlet", "/location-history" })
public class LocationHistoryServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private List<LocationHistory> listLocationHistory = new ArrayList<>();
	
    private static final String jdbcUrl = "jdbc:mysql://220.85.169.165/seoul_wifi_info";
    private static final String username = "chan4760";
    private static final String password = "Tjtocks178@";
    
    
    static {
        try {
            Class.forName("org.mariadb.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public LocationHistoryServlet() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	
		System.out.println("LocationHistoryServlet.java : doGet() : STARTED");
		printLocationHistory();
		
		String latParam = request.getParameter("lat");
		String lngParam = request.getParameter("lng");
		String deleteIndexParam = request.getParameter("delete");

		if (latParam != null && lngParam != null) {
		   double lat = Double.parseDouble(latParam);
		   double lng = Double.parseDouble(lngParam);

		   addLocationHistory(lat, lng);
		   saveLocationToDB(lat, lng);
		}
		
		
		if( deleteIndexParam != null) {
			int deleteIndex = Integer.parseInt(deleteIndexParam);
			if (deleteIndex >= 0 && deleteIndex < listLocationHistory.size()) {
				listLocationHistory.remove(deleteIndex);
			}
		}

		
		response.setContentType("text/html;charset=UTF-8");
	    PrintWriter out = response.getWriter();

	    out.write("<html>");
	    out.write("<head>");
	    out.write("<meta charset=\"UTF-8\">");
	    out.write("<title>위치 히스토리 목록</title>");
	    out.write("<script src=\"http://code.jquery.com/jquery-latest.min.js\"></script>");
	    
        // Style
        out.write("<style>");
        out.write("#location_history_table {");
        out.write("    border-collapse: collapse;");
        out.write("    border-spacing: 0;");
        out.write("    width: 100%;");
        out.write("}");
        out.write("#location_history_table th, #location_history_table td {");
        out.write("    padding: 10px;");
        out.write("    border: 1px solid #ccc;");
        out.write("}");
        out.write("#location_history_table thead {");
        out.write("    background-color: #00FF00;");
        out.write("    border: 1px solid #000;");
        out.write("}");
        out.write("#location_history_table thead th {");
        out.write("    border-right: 1px solid #000;");
        out.write("}");
        out.write("#location_history_table tr:nth-child(even) {");
        out.write("    background-color: #f2f2f2;");
        out.write("}");
        out.write("button {");
        out.write("    font-size: 14px;");
        out.write("    padding: 5px 10px;");
        out.write("}");
        out.write("#lat, #lng, button {");
        out.write("    display: inline-block;");
        out.write("    margin-right: 10px;");
        out.write("}");
        out.write("</style>");
	    out.write("</head>");


	    out.write("<body>");
	    

	    out.write("<h1>와이파이 정보 구하기</h1>");


	    out.write("<table class=\"table table-stripped\" id=\"location_history_table\"> ");

	    
	    out.write("<thead>");
	    out.write("<tr>");
	    out.write("<th>ID</th>");
	    out.write("<th>X 좌표</th>");
	    out.write("<th>Y 좌표</th>");
	    out.write("<th>조회일자</th>");
	    out.write("<th>비고</th>");
	    out.write("</tr>");
	    out.write("</thead>");


	    out.write("<tbody>");
	    if (!listLocationHistory.isEmpty()) {
	        for (int i = 0; i < listLocationHistory.size(); i++) {
	            LocationHistory history = listLocationHistory.get(i);
	            out.write("<tr>");
	            out.write("<td>" + (i + 1) + "</td>");
	            out.write("<td>" + history.getX() + "</td>");
	            out.write("<td>" + history.getY() + "</td>");
	            out.write("<td>" + history.getFormattedTimestamp() + "</td>");
	            out.write("<td><button onclick='deleteEntry(" + i + ")'>삭제</button></td>");
	            out.write("</tr>");
	        }
	    }
	    out.write("</tbody>");

	    out.write("</table>");

	    // 삭제 버튼 클릭 시 - 해당 DATA 삭제
	    out.write("<script>");
	    out.write("function deleteEntry(index) {");
	    out.write("  if (confirm('정말로 삭제하시겠습니까?')) {");
	    out.write("      window.location.href = 'LocationHistoryServlet?delete=' + index;");
	    out.write("  }");
	    out.write("}");
	    
	    out.write("</script>");

	    System.out.println("LocationHistoryServlet.java : doGet() : ENDED");
		
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	    String latParam = request.getParameter("lat");
	    String lngParam = request.getParameter("lng");

	    if (latParam != null && lngParam != null ) {
	        double lat = Double.parseDouble(latParam);
	        double lng = Double.parseDouble(lngParam);
	        addLocationHistory(lat, lng);
	        
	        saveLocationToDB(lat, lng);
	    };
	}
	
	private void addLocationHistory(double lat, double lng) {
		LocalDateTime timestamp = LocalDateTime.now();
		LocationHistory eachLocationHistory = new LocationHistory(lat, lng, timestamp);
		listLocationHistory.add(eachLocationHistory);
		printLocationHistory();
	}
	

	private void printLocationHistory() {
	    try (Connection connection = DriverManager.getConnection(jdbcUrl, username, password)) {
	        String query = "SELECT * FROM location_history_table;";
	        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
	            try (ResultSet resultSet = preparedStatement.executeQuery()) {
	                System.out.println("Location History in DB:");
	                while (resultSet.next()) {
	                    int id = resultSet.getInt("ID");
	                    double x = resultSet.getDouble("X");
	                    double y = resultSet.getDouble("Y");
	                    LocalDateTime timestamp = resultSet.getTimestamp("조회일자").toLocalDateTime();

	                    System.out.println("ID: " + id + ", X: " + x + ", Y: " + y + ", Timestamp: " + timestamp);
	                }
	            }
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}
	

    private void saveLocationToDB(double lat, double lng) {
        try (Connection connection = DriverManager.getConnection(jdbcUrl, username, password)) {
            String query = "INSERT INTO location_history_table (X, Y) VALUES (?, ?);";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setDouble(1, lat);
                preparedStatement.setDouble(2, lng);
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
}
