let isIdChecked = false;
let isNicknameChecked = false;

// 이용약관 동의 시 회원가입 입력 폼 표시
function showSignUpForm() {
    const agreeCheckbox = document.getElementById("agreeTerms");
    const signUpForm = document.querySelector(".signUpForm");
    const formData = document.querySelector(".formData");

    // 약관에 동의하면 회원가입 입력 폼을 표시하고 약관 부분을 숨깁니다.
    if (agreeCheckbox && agreeCheckbox.checked) {
        if (signUpForm) signUpForm.style.display = "block";
        if (formData) formData.style.display = "none";
    } else {
        alert("이용약관에 동의해야 다음으로 진행할 수 있습니다.");
    }
}
function checkDuplicateId() {
    const userIdField = document.querySelector("input[name='id']");
    const idCheckResult = document.getElementById('idCheckResult');

    if (userIdField) {
        const userId = userIdField.value.trim();
        if (!userId) {
            alert("아이디를 입력하세요.");
            return;
        }

        fetch(`/member/checkDuplicateId?id=${encodeURIComponent(userId)}`)
            .then(response => response.json())
            .then(data => {
                if (data.isDuplicate) {
                    alert("이미 사용 중인 아이디입니다.");
                    isIdChecked = false;
                } else {
                    alert("사용 가능한 아이디입니다.");
                    isIdChecked = true; // 중복 확인 완료 표시
                }
            })
            .catch(error => {
                console.error("Error:", error);
                alert("중복 확인 중 오류가 발생했습니다.");
            });
    }
}
    document.querySelector('form').addEventListener('submit', function() {
        var email = document.getElementById('email').value;
        var emailDomain = document.getElementById('emailDomain').value;

        // 이메일 앞부분과 도메인을 합쳐서 email 필드에 다시 설정
        document.getElementById('email').value = email + "@" + emailDomain;
    });

// 아이디 중복 확인
//function checkDuplicateId() {
//    const userIdField = document.querySelector("input[name='id']"); // 아이디 입력란
//    const idCheckResult = document.getElementById('idCheckResult'); // 결과를 표시할 요소
//
//    if (userIdField) {
//        const userId = userIdField.value.trim(); // trim()으로 공백 제거
//        if (!userId) {
//            alert("아이디를 입력하세요.");
//            return;
//        }
//
//        // 서버로 중복 확인 요청
//        fetch(`/member/checkDuplicateId?id=${userId}`)
//            .then(response => response.json())
//            .then(data => {
//                console.log(data.isDuplicate);
//                if (data.isDuplicate) {
//
//                    alert("이미 사용 중인 아이디입니다.");  // 중복 닉네임일 경우 알림창
//
//                } else {
//                    alert("사용 가능한 아이디입니다.");  // 사용 가능한 닉네임일 경우 알림창
//                }
//            })
//            .catch(error => console.error("Error:", error));
//    }
//}

// 닉네임 중복 확인
function checkDuplicateNickname() {
    const nicknameField = document.querySelector("input[name='nickname']");
    const nicknameCheckResult = document.getElementById('nicknameCheckResult');

    if (nicknameField) {
        const nickname = nicknameField.value.trim();
        if (!nickname) {
            alert("닉네임을 입력하세요.");
            return;
        }

        fetch(`/member/checkDuplicateNickname?nickname=${encodeURIComponent(nickname)}`)
            .then(response => response.json())
            .then(data => {
                if (data.isDuplicate) {
                    alert("이미 사용 중인 닉네임입니다.");
                    isNicknameChecked = false;
                } else {
                    alert("사용 가능한 닉네임입니다.");
                    isNicknameChecked = true; // 중복 확인 완료 표시
                }
            })
            .catch(error => {
                console.error("Error:", error);
                alert("중복 확인 중 오류가 발생했습니다.");
            });
    }
}

document.querySelector('form').addEventListener('submit', function(event) {
    if (!isIdChecked) {
        alert("아이디 중복 확인을 하지 않았습니다.");
        event.preventDefault(); // 폼 제출 중단
    } else if (!isNicknameChecked) {
        alert("닉네임 중복 확인을 하지 않았습니다.");
        event.preventDefault();
    } else if (isIdChecked && isNicknameChecked) {
        alert("아이디/닉네임 중복 확인이 완료되었습니다.");
    }
});