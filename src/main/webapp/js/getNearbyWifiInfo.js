    function getNearbyWifiInfo() {
        console.log("Function Started : getNearbyWifiInfo()");

        var lat = document.getElementById("lat").value;
        var lng = document.getElementById("lng").value;
        
        $.getJSON("GetWifiTableServlet?lat=" + lat + "&lng=" + lng, function(data) {
            console.log(data); 
            $.each(data, function(index, item) {
                let rowData = "<tr>";
                rowData += "<td>" + item.distance.toFixed(4) + " km</td>";
                rowData += "<td>" + item.관리번호 + "</td>";
                rowData += "<td>" + item.자치구 + "</td>";
            	rowData += "<td><a href='jsp/WifiDetailPage.jsp?관리번호=" + encodeURIComponent(item.관리번호) + "&distance=" + encodeURIComponent(item.distance.toFixed(4)) + "'>" + item.와이파이명 + "</a></td>";
                rowData += "<td>" + item.도로명주소 + "</td>";
                rowData += "<td>" + item.상세주소 + "</td>";
                rowData += "<td>" + item.설치위치_층 + "</td>";
                rowData += "<td>" + item.설치유형 + "</td>";
                rowData += "<td>" + item.설치기관 + "</td>";
                rowData += "<td>" + item.서비스구분 + "</td>";
                rowData += "<td>" + item.망종류 + "</td>";
                rowData += "<td>" + item.설치년도 + "</td>";
                rowData += "<td>" + item.실내외구분 + "</td>";
                rowData += "<td>" + item.wifi접속환경 + "</td>";
                rowData += "<td>" + item.Y좌표.toFixed(6) + "</td>";
                rowData += "<td>" + item.X좌표.toFixed(6) + "</td>";
                rowData += "<td>" + item.작업일자 + "</td>";
                rowData += "</tr>";

                $('#wifi_table_body').append(rowData);

                console.log(item.관리번호 + " added Complete ");
            });
        });

        console.log("Function Ended : getNearbyWifiInfo()");
    }
