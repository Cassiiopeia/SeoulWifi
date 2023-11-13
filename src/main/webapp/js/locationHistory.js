// locationHistory.js

// LocationHistoryServlet.java 에서 쓰임

function deleteEntry(index) {
    if (confirm('정말로 삭제하시겠습니까?')) {
        goDELETE(index);
    }
}

function goDELETE(index) {
    $.ajax({
        url: '/WIFI-1107/LocationHistoryServlet',
        type: 'DELETE',
        data: {
            delete: index
        },
        success: function (response) {
            alert("위치 히스토리가 성공적으로 삭제되었습니다.");
            console.log('Delete location history');
            location.reload();
        },
        error: function (error) {
            console.log('Error deleting location history:', error);
        }
    });
}
