function toggleSections(action) {
    // 기존에는 각 섹션을 숨기거나 보였는데, 이번엔 두 섹션 모두 항상 화면에 표시
    if (action === 'findId') {
        // 아이디 찾기 버튼 클릭 시, 아이디 관련 입력값을 서버로 보내기
        document.getElementById('findIdSection').style.display = 'block';
        document.getElementById('findPasswordSection').style.display = 'block';
    } else if (action === 'findPassword') {
        // 비밀번호 찾기 버튼 클릭 시, 비밀번호 관련 입력값을 서버로 보내기
        document.getElementById('findIdSection').style.display = 'block';
        document.getElementById('findPasswordSection').style.display = 'block';
    }
}

// 페이지가 로드될 때 두 섹션 모두 보여주기
document.addEventListener('DOMContentLoaded', function() {
    toggleSections();
});
