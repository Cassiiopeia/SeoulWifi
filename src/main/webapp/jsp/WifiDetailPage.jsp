<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>와이파이 상세 정보</title>
    <script src="http://code.jquery.com/jquery-latest.min.js"></script>
    <style>
    	#wifi_detail_table thead th {
        	background-color: #00FF00; 
        	border: 1px solid #000; 
    	}

    	#wifi_detail_table th, #wifi_detail_table td {
        	padding: 10px;
        	border: 1px solid #ccc;
    	}
    	#wifi_detail_table tr:nth-child(even) {
        	background-color: #f2f2f2;
    	}
		button, select {
        	font-size: 14px; /* 원하는 크기로 조절해봐 */
        	padding: 5px 10px; /* 위, 아래, 좌, 우 여백 설정 */
    	}
    	.dropdown {
        	margin-bottom: 20px;
    	}
    	.dropdown-content {
        	display: none;
        	position: absolute;
        	background-color: #f9f9f9;
        	min-width: 160px;
        	box-shadow: 0px 8px 16px 0px rgba(0,0,0,0.2);
        	padding: 12px 16px;
        	z-index: 1;
    	}
    	.dropdown:hover .dropdown-content {
        	display: block;
    	}
    </style>
	<script src = "js/addBookmark.js"></script>
</head>
<body>
    <h1>와이파이 상세 정보</h1>
	<a href="/WIFI-1107">홈</a> |
	<a href="/WIFI-1107/LocationHistoryServlet">위치 히스토리 목록</a> |
	<a href="/ApiDataToDBServlet">OpenAPI 와이파이 정보 가져오기</a> |
	<a href="/bookmark-group">북마크 보기</a> |
	<a href="/WIFI-1107/jsp/BookmarkGroupManagement.jsp">북마크 그룹 관리</a>>

    
    
    <div style="display: flex; justify-content: flex-start;">
    	<div>
        <label for="bookmarkGroup">북마크 그룹 이름 선택:</label>
        <select id="bookmarkGroup">
            <!-- 옵션들은 서버에서 동적으로 로딩됩니다. -->
        </select>
    	</div>
    <button onclick="addBookmark()">북마크 추가하기</button>
    </div>

    <div id="wifiDetailContainer">
        <!-- 여기에 서버로부터 받은 와이파이 상세 정보를 표시합니다. -->
    </div>

    <script type="text/javascript">
        $(document).ready(function() {
            var 관리번호 = new URLSearchParams(window.location.search).get('관리번호');
            var distance = new URLSearchParams(window.location.search).get('distance');

            $.ajax({
                url: "/WIFI-1107/GetWifiDetailServlet",          
                type: "GET", 
                data: { 관리번호: 관리번호 , distance:distance}, 
                success: function(data) {
                    console.log("Success response:", data);
                    var tableHtml = '<table class="table table-striped" id="wifi_detail_table">';
                    tableHtml += '<thead>';
                    tableHtml += '<tr><th>거리(km)</th><td>' + data.distance + '</td></tr>';
                    tableHtml += '<tr><th>관리번호</th><td>' + data.관리번호 + '</td></tr>';
                    tableHtml += '<tr><th>자치구</th><td>' + data.자치구 + '</td></tr>';
                    tableHtml += '<tr><th>와이파이명</th><td>' + data.와이파이명 + '</td></tr>';
                    tableHtml += '<tr><th>도로명주소</th><td>' + data.도로명주소 + '</td></tr>';
                    tableHtml += '<tr><th>상세주소</th><td>' + data.상세주소 + '</td></tr>';
                    tableHtml += '<tr><th>설치위치(층)</th><td>' + data.설치위치_층 + '</td></tr>';
                    tableHtml += '<tr><th>설치유형</th><td>' + data.설치유형 + '</td></tr>';
                    tableHtml += '<tr><th>설치기관</th><td>' + data.설치기관 + '</td></tr>';
                    tableHtml += '<tr><th>서비스구분</th><td>' + data.서비스구분 + '</td></tr>';
                    tableHtml += '<tr><th>망종류</th><td>' + data.망종류 + '</td></tr>';
                    tableHtml += '<tr><th>설치년도</th><td>' + data.설치년도 + '</td></tr>';
                    tableHtml += '<tr><th>실내외구분</th><td>' + data.실내외구분 + '</td></tr>';
                    tableHtml += '<tr><th>WIFI접속환경</th><td>' + data.wifi접속환경 + '</td></tr>';
                    tableHtml += '<tr><th>X좌표</th><td>' + data.X좌표 + '</td></tr>';
                    tableHtml += '<tr><th>Y좌표</th><td>' + data.Y좌표 + '</td></tr>';
                    tableHtml += '<tr><th>작업일자</th><td>' + data.작업일자 + '</td></tr>';
                    tableHtml += '</table>';

                    // 생성된 테이블을 페이지에 추가
                    $('#wifiDetailContainer').html(tableHtml);
                },
                error: function(jqXHR, textStatus, errorThrown) {
                    console.error("Error getting wifi details: ", textStatus, errorThrown);
                }
            });
        });
    </script>
</body>
</html>
