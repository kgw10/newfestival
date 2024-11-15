let isIdChecked = false;
let isNicknameChecked = false;

function showSignUpForm() {
    const agreeCheckbox = document.getElementById("agreeTerms");
    const signUpForm = document.querySelector(".signUpForm");
    const formData = document.querySelector(".formData");

    // 약관에 동의하면 회원가입 입력 폼을 표시하고 약관 부분을 숨깁니다.
    if (agreeCheckbox && agreeCheckbox.checked) {
        if (signUpForm) signUpForm.style.display = "block";
        if (formData) formData.style.display = "none"; // 약관 부분 숨기기
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
                    isIdChecked = true;
                }
            })
            .catch(error => {
                console.error("Error:", error);
                alert("중복 확인 중 오류가 발생했습니다.");
            });
    }
}

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
                    isNicknameChecked = true;
                }
            })
            .catch(error => {
                console.error("Error:", error);
                alert("중복 확인 중 오류가 발생했습니다.");
            });
    }
}

//document.querySelector('form').addEventListener('submit', function(event) {
//    if (!isIdChecked) {
//        alert("아이디 중복 확인을 하지 않았습니다.");
//        event.preventDefault();
//    } else if (!isNicknameChecked) {
//        alert("닉네임 중복 확인을 하지 않았습니다.");
//        event.preventDefault();
//    }
//});

// signUpFailMsg가 있는 경우 alert 표시
//if (typeof signUpFailMsg !== 'undefined' && signUpFailMsg) {
//    alert(signUpFailMsg);
//}

function validatePasswords() {
    var password = document.querySelector('input[name="password"]').value;
    var confirmPassword = document.getElementById('confirmPassword').value;
    if (password !== confirmPassword) {
        alert("비밀번호가 일치하지 않습니다.");
        return false;
    }
    return true;
}