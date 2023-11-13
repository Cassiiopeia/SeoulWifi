package com.lib.controller;

import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import com.google.gson.Gson;
import com.dto.WifiData;
import com.dto.WifiDataResponse;
import org.json.JSONException;
import org.json.JSONObject;


public class ApiDataToDB {
    public static int runApiDataToDB() {
    	int return_totalItemCount = -1;
        try {
            Class.forName("org.mariadb.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        
        // MariaDB 연결 정보
        String jdbcUrl = "jdbc:mysql://220.85.169.165/seoul_wifi_info";
        String username = "chan4760";
        String password = "Tjtocks178@";

        try (Connection connection = DriverManager.getConnection(jdbcUrl, username, password)) {
            String apiKey = "485750685763686138357277624b70";
            int itemsPerPage = 1000; // 페이지 Limit // 1000개  
            String baseUrl = "http://openapi.seoul.go.kr:8088";
            String serviceName = "TbPublicWifiInfo";
            
            // 데이터베이스 초기화
            String deleteAllSql = "DELETE FROM wifi_table"; 
            try (PreparedStatement deleteAllStatement = connection.prepareStatement(deleteAllSql)) {
                deleteAllStatement.executeUpdate();
                System.out.println("wifi_table : DELETE ALL : Completed");
            }

            

            // 데이터 총 개수 조회
            String totalItemCountUrl = baseUrl + "/" + apiKey + "/json/" + serviceName + "/1/1";
            int totalItemCount = getTotalItemCount(totalItemCountUrl);
            return_totalItemCount = totalItemCount;

            // 반복해야하는 횟수
            int totalPages = (totalItemCount + itemsPerPage - 1) / itemsPerPage;
            
            int startItem = 1;
            int endItem = itemsPerPage;

            System.out.println("runAPIDataToDB() : totalItemCount : "+ return_totalItemCount);
            
            // 1000개씩 DB에 저장
            for (int page = 1; page <= totalPages; page++) {
                // 데이터 요청 URL 생성
                String dataRequestUrl = baseUrl + "/" + apiKey + "/json/" + serviceName + "/" + startItem + "/" + endItem;
                System.out.printf("New dataRequestURL Updated : %d ~ %d\n", startItem, endItem);

                // 데이터 가져오기
                WifiDataResponse dataResponse = fetchDataFromApi(dataRequestUrl);

                // 데이터베이스에 저장
                saveDataToDatabase(connection, dataResponse);
                System.out.printf(" DB Save Completed : %d ~ %d\n", startItem, endItem);
                
                startItem = endItem + 1;
                endItem = Math.min(startItem + itemsPerPage - 1, totalItemCount);
        
        
            }

            System.out.println("DB Update COMPLETED");
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
        
                
        return return_totalItemCount;                
    }

    private static int getTotalItemCount(String totalItemCountUrl) throws IOException {
        int totalItemCount = -1;
        try {
            URL totalPageUrl = new URL(totalItemCountUrl);
            HttpURLConnection totalPageConn = (HttpURLConnection) totalPageUrl.openConnection();
            totalPageConn.setRequestMethod("GET");

            if (totalPageConn.getResponseCode() == 200) {
                BufferedReader br = new BufferedReader(new InputStreamReader(totalPageConn.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;

                while ((line = br.readLine()) != null) {
                    response.append(line);
                }
                br.close();

                // JSONresponse 파싱 -> list_total_count
                JSONObject jsonResponse = new JSONObject(response.toString());
                JSONObject tbPublicWifiInfo = jsonResponse.getJSONObject("TbPublicWifiInfo");
                totalItemCount = tbPublicWifiInfo.getInt("list_total_count");
                
                System.out.println("totalItemCount Updated :" + totalItemCount );
            } else {
                System.out.println("totalPageConn.getResponseCode() is not 200");
            }

            totalPageConn.disconnect();
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        return totalItemCount;
    }

    private static WifiDataResponse fetchDataFromApi(String dataRequestUrl) throws IOException {
        Gson gson = new Gson();
        try {
            URL url = new URL(dataRequestUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            if (conn.getResponseCode() == 200) {
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;

                while ((line = br.readLine()) != null) {
                    response.append(line);
                }
                br.close();

                // JSON 데이터를 Java 객체로 변환
                WifiDataResponse dataResponse = gson.fromJson(response.toString(), WifiDataResponse.class);
                return dataResponse;
            } else {
                System.out.println("conn.getResponseCode() is not 200");
            }

            conn.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static void saveDataToDatabase(Connection connection, WifiDataResponse dataResponse) {
        if (dataResponse == null) {
            return;
        }

        //DB 저장
        for (WifiData data : dataResponse.getWifiDataList()) {
            String insertSql = "INSERT INTO wifi_table (관리번호, 자치구, 와이파이명, 도로명주소, 상세주소, 설치위치_층, 설치유형, 설치기관, 서비스구분, 망종류, 설치년도, 실내외구분, wifi접속환경, Y좌표, X좌표, 작업일자) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

            try (PreparedStatement statement = connection.prepareStatement(insertSql)) {
                statement.setString(1, data.getX_SWIFI_MGR_NO());
                statement.setString(2, data.getX_SWIFI_WRDOFC());
                statement.setString(3, data.getX_SWIFI_MAIN_NM());
                statement.setString(4, data.getX_SWIFI_ADRES1());
                statement.setString(5, data.getX_SWIFI_ADRES2());
                statement.setString(6, data.getX_SWIFI_INSTL_FLOOR());
                statement.setString(7, data.getX_SWIFI_INSTL_TY());
                statement.setString(8, data.getX_SWIFI_INSTL_MBY());
                statement.setString(9, data.getX_SWIFI_SVC_SE());
                statement.setString(10, data.getX_SWIFI_CMCWR());
                statement.setString(11, data.getX_SWIFI_CNSTC_YEAR());
                statement.setString(12, data.getX_SWIFI_INOUT_DOOR());
                statement.setString(13, data.getX_SWIFI_REMARS3());
                statement.setDouble(14, data.getLAT());
                statement.setDouble(15, data.getLNT());
                statement.setString(16, data.getWORK_DTTM());
                statement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            } 
        }
    }
}
