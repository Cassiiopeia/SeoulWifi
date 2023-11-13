<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>북마크 그룹 관리</title>
    <script src="http://code.jquery.com/jquery-latest.min.js"></script>
    <style>
    	table thead th {
        	background-color: #00FF00; 
        	border: 1px solid #000; 
    	}

    	table th, table td {
        	padding: 10px;
        	border: 1px solid #ccc;
    	}
    	tr:nth-child(even) {
        	background-color: #f2f2f2;
    	}
		button{
        	font-size: 14px; /* 원하는 크기로 조절해봐 */
        	padding: 5px 10px; /* 위, 아래, 좌, 우 여백 설정 */
    	}
    </style>
</head>
<body>
    <h1>북마크 그룹 관리</h1>
	<a href="/WIFI-1107">홈</a> |
	<a href="/WIFI-1107/LocationHistoryServlet">위치 히스토리 목록</a> |
	<a href="/ApiDataToDBServlet">OpenAPI 와이파이 정보 가져오기</a> |
	<a href="/bookmark-group">북마크 보기</a> |
	<a href="/WIFI-1107/jsp/BookmarkGroupManagement.jsp">북마크 그룹 관리</a>>
    <div>
	<button onclick="window.location.href='/WIFI-1107/jsp/AddBookmarkGroup.jsp';">북마크 그룹 추가하기</button>
    </div>
    
    
    <table id="bookmark_groupdata_table">
        <!-- 테이블 헤더 -->
        <thead>
            <tr>
                <th>ID</th>
                <th>북마크 이름</th>
                <th>순서</th>
                <th>등록일자</th>
                <th>수정일자</th>
                <th>비고</th>
            </tr>
        </thead>

        <tbody>
            <!-- 서버로부터 받아온 데이터로 채워집니다 -->
        </tbody>
    </table>
    
    <script>
        function loadBookmarkGroup() {
            var groupId = $('#bookmarkGroup').val(); // 선택된 그룹 ID를 가져옵니다.
            $.ajax({
                url: '/WIFI-1107/BookmarkGroupServlet', // 데이터를 가져올 서버의 URL
                type: 'GET',
                data: { groupId: groupId }, // 요청과 함께 서버로 보낼 데이터
                success: function(data) {
                    // 테이블을 데이터로 채웁니다.
                    var tableBody = $('#bookmark_groupdata_table tbody');
                    tableBody.empty(); 
                    data.forEach(function(item) {
                        // 테이블에 새 행을 추가합니다.
            			var row = $('<tr>').append(
                			$('<td>').text(item.ID),
                			$('<td>').text(item.북마크이름),
                			$('<td>').text(item.순서),
                			$('<td>').text(item.등록일자),
                			$('<td>').text(item.수정일자),
                			$('<td>').append($('<button>').text('수정').click(function() {
                				window.location.href = '/WIFI-1107/jsp/EditBookmarkGroup.jsp?id=' +item.ID;
                			}
                			})),
                			$('<td>').append($('<button>').text('삭제').click(function() {
                    			// 삭제 버튼 클릭 시 이벤트 처리
                			}))
            			);
                        tableBody.append(row);
                    });
                },
                error: function() {
                    alert('북마크 그룹 데이터를 불러오는 데 실패했습니다.');
                }
            });
        }
        
        $(document).ready(function() {
            loadBookmarkGroup(); 
        });
    </script>
</body>
</html>
