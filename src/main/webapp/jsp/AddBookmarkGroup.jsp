<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>북마크 그룹 추가하기</title>
    <script src="http://code.jquery.com/jquery-latest.min.js"></script>
    <style>
        body {
            font-family: Arial, sans-serif;
        }
        form {
            margin-bottom: 20px;
        }
        label, input[type=text], input[type=number], input[type=button] {
            margin: 5px;
            padding: 5px;
            font-size: 14px;
        }
        input[type=button] {
            background-color: #4CAF50; /* Green */
            border: none;
            color: white;
            text-align: center;
            text-decoration: none;
            display: inline-block;
            cursor: pointer;
        }
        input[type=text], input[type=number] {
            border: 1px solid #ccc;
        }
    </style>
</head>
<body>
    <h1>북마크 그룹 추가하기</h1>
	<a href="/WIFI-1107">홈</a> |
	<a href="/WIFI-1107/LocationHistoryServlet">위치 히스토리 목록</a> |
	<a href="/ApiDataToDBServlet">OpenAPI 와이파이 정보 가져오기</a> |
	<a href="/bookmark-group">북마크 보기</a> |
	<a href="/WIFI-1107/jsp/BookmarkGroupManagement.jsp">북마크 그룹 관리</a>>

    <!-- 북마크 추가 폼 -->
    <form id="addBookmarkForm">
        <label for="bookmarkName">북마크 이름:</label>
        <input type="text" id="bookmarkName" name="bookmarkName">
        <label for="bookmarkOrder">순서:</label>
        <input type="number" id="bookmarkOrder" name="bookmarkOrder">
        <input type="button" value="추가" onclick="addBookmarkGroup()">
    </form>
    
    <script>
        function addBookmarkGroup() {
            var bookmarkName = $('#bookmarkName').val();
            var bookmarkOrder = $('#bookmarkOrder').val();
            
            // 입력 검증
            if (!bookmarkName) {
                alert('북마크 이름을 입력해주세요.');
                return;
            }

            if (!bookmarkOrder) {
                alert('순서를 입력해주세요.');
                return;
            }

            // 서버에 데이터 전송
            $.ajax({
                url: '/WIFI-1107/BookmarkGroupServlet',
                type: 'POST',
                data: {
                    bookmarkName: bookmarkName,
                    bookmarkOrder: bookmarkOrder
                },
                success: function(response) {
                    alert('북마크 그룹이 성공적으로 추가되었습니다.');
                    // 성공 후 페이지를 새로 고침하거나 다른 페이지로 리디렉션 할 수 있습니다.
                    // 예: window.location.href = '/WIFI-1107/jsp/BookmarkGroupManagement.jsp';
                },
                error: function() {
                    alert('북마크 그룹 추가에 실패했습니다.');
                }
            });
        }
    </script>
</body>
</html>
