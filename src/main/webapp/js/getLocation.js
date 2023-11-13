
var lat,lng;

function getLocation() {
	if (navigator.geolocation) {
		navigator.geolocation.getCurrentPosition(function(position) {
            lat = position.coords.latitude;
            lng = position.coords.longitude;
            document.getElementById("lat").value = lat;
            document.getElementById("lng").value = lng;
         	console.log("LAT, LNG Value Updated ");
         	addLocationHistory(lat,lng);
         }, function(error) {
     		alert("내 위치 정보를 가져오는데 실패했습니다.");
                });
            } else {
                alert("브라우저에서 내 위치 정보를 지원하지 않습니다.");
     }
}
           


function addLocationHistory(lat, lng) {
    // 서버에 데이터를 전송하는 AJAX 요청
    $.ajax({
        url: '/WIFI-1107/LocationHistoryServlet',  
        type: 'POST',
        data: {
            lat: lat,
            lng: lng
        },
        success: function(response) {
            alert("위치 히스토리에 성공적으로 저장되었습니다.");
            console.log('Update LocationHistoryServlet');
        },
        error: function(error) {
            console.log('Error adding LocationHistoryServlet', error);
        }
    });
}