// signup ( dasol )

// -----------------------------------------------------
//               **회원가입 유효성 검사** 
// -----------------------------------------------------

// 필수 입력 항목의 유효성 검사 여부를 체크하기 위한 객체
// - true  == 해당 항목은 유효한 형식으로 작성됨
// - false == 해당 항목은 유효하지 않은 형식으로 작성됨
const checkObj = {
    "memberEmail": false,
    "memberPw": false,
    "memberPwConfirm": false,
    "memberNickname": false,
    "memberTel": false,
    "authKey": false,
    "memberSchool" : false
};

// -----------------------------------------------------
//               **회원 프로필 이미지** 
// -----------------------------------------------------
// -------------------------------------------------------
// 이미지 업로드 구간

/*  [input type="file" 사용 시 유의 사항]

1. 파일 선택 후 취소를 누르면 
    선택한 파일이 사라진다  (value == '')

2. value로 대입할 수 있는 값은  '' (빈칸)만 가능하다

3. 선택된 파일 정보를 저장하는 속성은
    value가 아니라 files이다
*/

// 요소 참조
const profileForm = document.getElementById("profile");  // 프로필 form
const profileImg = document.getElementById("profileImg");  // 미리보기 이미지 img
const imageInput = document.getElementById("imageInput");  // 이미지 파일 선택 input
const deleteImage = document.getElementById("deleteImage");  // 이미지 삭제 버튼
const MAX_SIZE = 1024 * 1024 * 5;  // 최대 파일 크기 설정 (5MB)

// 절대경로로 기본 이미지 URL 설정
// -> http://localhost/images/user.png
const defaultImageUrl = `${window.location.origin}/images/user.png`;

let statusCheck = -1; // -1 : 초기 상태, 0 : 이미지 삭제, 1 : 새 이미지 선택
let previousImage = profileImg.src; // 이전 이미지 URL 기록 (초기 상태의 이미지 URL 저장)
let previousFile = null;  // 이전에 선택된 파일 객체를 저장

// 이미지 선택 시 미리보기 및 파일 크기 검사
imageInput.addEventListener("change", () => {
    // change 이벤트 : 기존에 있던 값과 달라지면 change 이벤트 일어남
    // console.log(imageInput.files); // FileList (input 태그는 FileList 로 저장)
    const file = imageInput.files[0]; // 선택한 File 객체 가져오기
    if (file) { // 파일이 선택된 경우
        if (file.size <= MAX_SIZE) { // 파일 크기가 허용범위 이내인 경우
            const newImageUrl = URL.createObjectURL(file); // 임시 URL 생성
            // blob:http://localhost/05631bf1-ab54-4219-819f-64600ba28301
            // 미리보기 이미지 url 용도
            profileImg.src = newImageUrl; // 미리보기 이미지 설정(img 태그의 src에 선택한 파일 임시 경로 대입)
            statusCheck = 1; // 새 이미지 선택 상태 기록
            previousImage = newImageUrl; // 현재 선택된 이미지를 이전 이미지로 저장(다음에 바뀔일에 대비)
            previousFile = file; // 현재 선택된 파일 객체를 이전 파일로 저장(다음에 바뀔일에 대비)
        } else { // 파일 크기가 허용 범위를 초과한 경우
            alert("5MB 이하의 이미지를 선택해주세요!");
            imageInput.value = ""; // 1. 파일 선택 초기화
            // (alert 창은 띄웠지만 이미 선택된 큰 사이즈 파일을 비우는건 따로 해야함)
            // == imageInput.files = null;

            profileImg.src = previousImage; // 2. 이전 미리보기 이미지로 복원

            // 3. 파일 입력 복구  : 이전 파일이 존재하면 다시 할당
            if (previousFile) {
                const dataTransfer = new DataTransfer();
                // DataTransfer : 자바스크립트로 파일을 조작할 때 사용되는 인터페이스
                // DataTransfer.items.add() : 파일 추가
                // DataTransfer.items.remove() : 파일 제거
                // DataTransfer.files : FileList 객체를 반환
                // -> <input type="file"> 요소에 파일을 동적으로 설정 가능
                // --> input 태그의 files 속성은 FileList만 저장 가능하기 때문에
                // DataTransfer를 이용하여 현재 File 객체를 FileList 변환하여 할당
                dataTransfer.items.add(previousFile);
                // 이전 파일을 추가해두기 : DataTransfer에 File 객체를 추가
                imageInput.files = dataTransfer.files;
                // 이전 파일로 input 요소의 files 속성을 복구 : DataTransfer에 저장된
                // 파일의 리스트를 FileList 객체로 반환
            }
        }
    } else { // 파일 선택이 취소된 경우
        profileImg.src = previousImage; // 이전 미리보기 이미지로 복원
        // 파일 입력 복구 : 이전 파일이 존재하면 다시 할당
        if (previousFile) {
            const dataTransfer = new DataTransfer();
            dataTransfer.items.add(previousFile);
            imageInput.files = dataTransfer.files; // 이전 파일로 input 태그의 files 속성 복구
        }
    }
});

// 이미지 삭제 버튼 클릭 시
deleteImage.addEventListener("click", () => {
    // 기본 이미지 상태가 아니면 삭제 처리
    if (profileImg.src !== defaultImageUrl) {
        imageInput.value = ""; // 파일 선택 초기화
        profileImg.src = defaultImageUrl; // 기본 이미지로 설정
        statusCheck = 0; // 삭제 상태 기록
        previousFile = null; // 이전 파일 초기화 기록
    } else {
        // 기본 이미지 상태에서 삭제 버튼 클릭 시 상태를 변경하지 않음
        statusCheck = -1; // 변경 사항 없음 상태 유지
    }
});

// 폼 제출 시 유효성 검사
profileForm.addEventListener("submit", e => {
    if (statusCheck === -1) { // 변경 사항이 없는 경우 제출 막기
        e.preventDefault();
        alert("이미지 변경 후 제출하세요"); 
    }
});

// -----------------------------------------------------
//               **인증번호 받기 버튼** 
// -----------------------------------------------------
const sendAuthKeyBtn = document.querySelector("#sendAuthKeyBtn");

// 인증번호 입력 input
const authKey = document.querySelector("#authKey");

// 인증번호 입력 후 확인 버튼
const checkAuthKeyBtn = document.querySelector("#checkAuthKeyBtn");

// 인증번호 관련 메시지 출력 span
const authKeyMessage = document.querySelector("#authKeyMessage");

let authTimer; // 타이머 역할을 할 setInterval을 저장할 변수

const initMin = 4; // 타이머 초기값 (분)
const initSec = 59; // 타이머 초기값 (초)
const initTime = "05:00";

// 실제 줄어드는 시간을 저장할 변수
let min = initMin;
let sec = initSec;


// -----------------------------------------------------
//                  **이메일 유효성 검사** 
// -----------------------------------------------------

// 1) 이메일 유효성 검사에 사용될 요소 얻어오기
const memberEmail = document.querySelector("#memberEmail");
const emailMessage = document.querySelector("#emailMessage");

console.log("memberEmail 요소:", memberEmail); // 여기서 null이 나오면 안 돼!
console.log("emailMessage 요소:", emailMessage);

// 2) 이메일이 입력(input) 될 때 마다 유효성 검사 수행
memberEmail.addEventListener("input", e => {
    console.log("입력 이벤트 발생!");

    // 이메일 인증 후 이메일이 변경된 경우
    checkObj.authKey = false;
    document.querySelector("#authKeyMessage").innerText = "";
    clearInterval(authTimer);

    // 작성된 이메일 값 얻어오기
    const inputEmail = e.target.value;

    //console.log(inputEmail);

    // 3) 입력된 이메일이 없을 경우
    if (inputEmail.trim().length === 0) {
        emailMessage.innerText = "메일을 받을 수 있는 이메일을 입력해주세요.";

        // 메시지에 색상을 추가하는 클래스 모두 제거
        emailMessage.classList.remove('confirm', 'error');

        // 이메일 유효성 검사 여부를 false 변경
        checkObj.memberEmail = false;

        // 잘못 입력한 띄어쓰기가 있을 경우 없앰
        memberEmail.value = "";

        return;
    }

    // 4) 입력된 이메일이 있을 경우 정규식 검사
    //    (알맞은 형태로 작성했는지 검사)
    const regExp = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
    // 영어, 숫자, 특수문자(. _ % + -)를 허용
    // 이메일 형식으로 작성
    // 도메인 부분은 최소 2개의 문자로 끝나야함 (.com, .net, .org, .kr 등) 

    // 입력 받은 이메일이 정규식과 일치하지 않는 경우
    // (알맞은 이메일 형태가 아닌 경우)
    if (!regExp.test(inputEmail)) {
        emailMessage.innerText = "알맞은 이메일 형식으로 작성해주세요.";
        emailMessage.classList.add('error'); // 글자를 빨간색으로 변경
        emailMessage.classList.remove('confirm'); // 초록색 제거
        checkObj.memberEmail = false; // 유효하지 않은 이메일임을 기록
        return;
    }

    // 5) 유효한 이메일 형식인 경우 중복 검사 수행
    // 비동기(ajax)
    fetch("/member/checkEmail?memberEmail=" + inputEmail)
        .then(resp => resp.text())
        .then(count => {
            // count : 1이면 중복, 0이면 중복 아님
            if (count == 1) { // 중복 0
                emailMessage.innerText = "이미 사용중인 이메일입니다.";
                emailMessage.classList.add("error");
                emailMessage.classList.remove("confirm");
                checkObj.memberEmail = false; // 중복은 유효하지 않은 상태
                return;
            }

            // 중복 X 경우
            emailMessage.innerText = "사용 가능한 이메일입니다.";
            emailMessage.classList.add("confirm");
            emailMessage.classList.remove("error");
            checkObj.memberEmail = true; // 유효한 이메일

        }).catch(error => {
            // fetch API 이용한 비동기 요청 수행 중 발생한 예외 처리
            console.log(error); // 발생한 예외 출력
        });

});

// -----------------------------------------------------
//                  **이메일 인증** 
// -----------------------------------------------------

// 인증번호 받기 버튼 클릭 시 
sendAuthKeyBtn.addEventListener("click", () => {

    // 새로운 인증번호 발급을 원하는것이기 때문에 
    // 새로 발급받은 인증번호 확인전까진 checkObj.authKey는 false
    checkObj.authKey = false;
    // 인증번호 발급 관련 메세지 비우기
    authKeyMessage.innerText = "";

    // 중복되지 않은 유효한 이메일을 입력한 경우가 아니면
    if (!checkObj.memberEmail) {
        alert("유효한 이메일 작성 후 클릭해 주세요");
        return;
    }

    // 클릭 시 타이머 숫자 초기화
    min = initMin;
    sec = initSec;

    // 이전 동작중인 인터벌 클리어(없애기)
    clearInterval(authTimer);

    // *************************************
    // 비동기로 서버에서 메일보내기 (ajax 요청)
    fetch("/email/signup", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: memberEmail.value
    })
        .then(resp => resp.text())
        .then(result => {
            if (result == 1) {
                console.log("인증 번호 발송 성공");
            } else {
                console.log("인증 번호 발송 실패!!!");
            }
        });

    // *************************************

    // 메일은 비동기로 서버에서 보내라고 하고
    // 화면에서는 타이머 시작하기
    authKeyMessage.innerText = initTime; // 05:00 세팅
    authKeyMessage.classList.remove("confirm", "error"); // 검정 글씨

    alert("인증번호가 발송되었습니다.");

    // setInterval(함수, 지연시간(ms))
    // - 지연시간(ms)만큼 시간이 지날 때 마다 함수 수행

    // clearInterval(Interval이 저장된 변수)
    // - 매개변수로 전달받은 interval을 멈춤

    // 인증 시간 출력(1초 마다 동작)
    authTimer = setInterval(() => {

        authKeyMessage.innerText = `${addZero(min)}:${addZero(sec)}`;

        // 0분 0초인 경우 ("00:00" 출력 후)
        if (min == 0 && sec == 0) {
            checkObj.authKey = false; // 인증 못함
            clearInterval(authTimer); // interval 멈춤
            authKeyMessage.classList.add('error');
            authKeyMessage.classList.remove('confirm');
            return;
        }

        // 0초인 경우(0초를 출력한 후)
        if (sec == 0) {
            sec = 60;
            min--;
        }

        sec--; // 1초 감소

    }, 1000); // 1초 지연시간

});


// 전달 받은 숫자가 10 미만인 경우(한자리) 앞에 0 붙여서 반환
function addZero(number) {
    if (number < 10) return "0" + number;
    else return number;
}


// -------------------------------------------------------------

// 인증하기 버튼 클릭 시
// 입력된 인증번호를 비동기로 서버에 전달
// -> 입력된 인증번호와 발급된 인증번호가 같은지 비교
//   같으면 1, 아니면 0반환
// 단, 타이머가 00:00초가 아닐 경우에만 수행

checkAuthKeyBtn.addEventListener("click", () => {

    if (min === 0 && sec === 0) { // 타이머가 00:00인 경우
        alert("인증번호 입력 제한시간을 초과하였습니다.");
        return;
    }

    if (authKey.value.length < 6) { // 인증번호가 제대로 입력 안된 경우(길이가 6미만인 경우)
        alert("인증번호를 정확히 입력해 주세요.");
        return;
    }

    // 문제 없는 경우(제한시간, 인증번호 길이 유효 시)
    // 입력받은 이메일, 인증번호로 객체 생성
    const obj = {
        "email": memberEmail.value,
        "authKey": authKey.value
    };

    // 인증번호 확인용 비동기 요청(ajax) 보내기
    // "/email/checkAuthKey", POST 요청
    fetch("/email/checkAuthKey", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(obj)
    })
        .then(resp => resp.text())
        .then(result => {
            // 1 or 0

            if (result == 0) {
                alert("인증번호가 일치하지 않습니다!");
                checkObj.authKey = false;
                return;
            }

            // 일치할 때
            clearInterval(authTimer); // 타이머 멈춤

            authKeyMessage.innerText = "인증 되었습니다";
            authKeyMessage.classList.remove("error");
            authKeyMessage.classList.add("confirm");

            checkObj.authKey = true; // 인증번호 검사 여부 true 변경

        })

});


// -----------------------------------------------------
//          **비밀번호 / 비밀번호 확인 유효성 검사** 
// -----------------------------------------------------

// 1) 비밀번호 관련 요소 얻어오기
const memberPw = document.querySelector("#memberPw");
const memberPwConfirm = document.querySelector("#memberPwConfirm");
const pwMessage = document.querySelector("#pwMessage");

// 5) 비밀번호, 비밀번호확인이 같은지 검사하는 함수
const checkPw = () => {

    // 같을 경우
    if (memberPw.value === memberPwConfirm.value) {
        pwMessage.innerText = "비밀번호가 일치합니다";
        pwMessage.classList.add("confirm");
        pwMessage.classList.remove("error");
        checkObj.memberPwConfirm = true; // 비밀번호 확인 true
        return;
    }

    pwMessage.innerText = "비밀번호가 일치하지 않습니다";
    pwMessage.classList.add("error");
    pwMessage.classList.remove("confirm");
    checkObj.memberPwConfirm = false; // 비밀번호 확인 false

};



// 2) 비밀번호 유효성 검사
memberPw.addEventListener("input", e => {

    // 입력 받은 비밀번호 값
    const inputPw = e.target.value;

    // 3) 입력되지 않은 경우
    if (inputPw.trim().length === 0) {
        pwMessage.innerText = "영어,숫자,특수문자(!,@,#,$,%,^,&,*) 8 ~ 16자 사이로 입력해주세요.";
        pwMessage.classList.remove("confirm", "error");
        checkObj.memberPw = false; // 비밀번호가 유효하지 않다고 표시
        memberPw.value = ""; // 처음에 띄어쓰기 입력 못하게 하기
        return;
    }

    // 4) 입력 받은 비밀번호 정규식 검사
    const regExp = /^[a-zA-Z0-9!@#$%^&*]{8,16}$/;

    if (!regExp.test(inputPw)) { // 유효하지 않으면
        pwMessage.innerText = "비밀번호가 유효하지 않습니다";
        pwMessage.classList.add("error");
        pwMessage.classList.remove("confirm");
        checkObj.memberPw = false;
        return;
    }

    // 유효한 경우
    pwMessage.innerText = "유효한 비밀번호 형식입니다";
    pwMessage.classList.add("confrim");
    pwMessage.classList.remove("error");
    checkObj.memberPw = true;

    // 비밀번호 입력 시 확인란의 값과 비교하는 코드 추가

    // 비밀번호 확인에 값이 작성되어 있을 때
    if (memberPwConfirm.value.length > 0) {
        checkPw();
    }
});

// 6) 비밀번호 확인 유효성 검사
// 단, 비밀번호(memberPw)가 유효할 때만 검사 수행
memberPwConfirm.addEventListener("input", () => {

    if (checkObj.memberPw) { // memberPw가 유효한 경우
        checkPw(); // 비교하는 함수 수행
        return;
    }

    // memberPw가 유효하지 않은 경우
    // memberPwConfirm 검사 X
    checkObj.memberPwConfirm = false;
});

// -----------------------------------------------------
//                **닉네임 유효성 검사** 
// -----------------------------------------------------
const memberNickname = document.querySelector("#memberNickname");
const nickMessage = document.querySelector("#nickMessage");

memberNickname.addEventListener("input", (e) => {

    const inputNickname = e.target.value;

    // 1) 입력 안한 경우
    if (inputNickname.trim().length === 0) {
        nickMessage.innerText = "한글,영어,숫자로만 2~10글자";
        nickMessage.classList.remove("confirm", "error");
        checkObj.memberNickname = false;
        memberNickname.value = "";
        return;
    }

    // 2) 정규식 검사
    const regExp = /^[가-힣\w\d]{2,10}$/;

    if (!regExp.test(inputNickname)) { // 유효 X
        nickMessage.innerText = "유효하지 않은 닉네임 형식입니다.";
        nickMessage.classList.add("error");
        nickMessage.classList.remove("confirm");
        checkObj.memberNickname = false;
        return;
    }

    // 3) 중복 검사 ajax요청 (유효한 경우)
    fetch("/member/checkNickname?memberNickname=" + inputNickname)
        .then(resp => resp.text())
        .then(count => {
            if (count == 1) { // 중복 O
                nickMessage.innerText = "이미 사용중인 닉네임입니다.";
                nickMessage.classList.add("error");
                nickMessage.classList.remove("confirm");
                checkObj.memberNickname = false;
                return;
            }

            // 중복 아닌 경우
            nickMessage.innerText = "사용 가능한 닉네임입니다.";
            nickMessage.classList.add("confirm");
            nickMessage.classList.remove("error");
            checkObj.memberNickname = true;

        });
});

// -----------------------------------------------------
//                **전화번호 유효성 검사** 
// -----------------------------------------------------

/* 전화번호(휴대폰번호) 유효성 검사 */
const memberTel = document.querySelector("#memberTel");
const telMessage = document.querySelector("#telMessage");

memberTel.addEventListener("input", e => {

    const inputTel = e.target.value;

    if (inputTel.trim().length === 0) {
        telMessage.innerText = "전화번호를 입력해주세요.(- 제외)";
        telMessage.classList.remove("confirm", "error");
        checkObj.memberTel = false;
        memberTel.value = "";
        return;
    }

    const regExp = /^01[0-9]{1}[0-9]{3,4}[0-9]{4}$/;

    if (!regExp.test(inputTel)) {
        telMessage.innerText = "유효하지 않은 전화번호 형식입니다.";
        telMessage.classList.add("error");
        telMessage.classList.remove("confirm");
        checkObj.memberTel = false;
        return;
    }

    telMessage.innerText = "유효한 전화번호 형식입니다.";
    telMessage.classList.add("confirm");
    telMessage.classList.remove("error");
    checkObj.memberTel = true;

});

// -----------------------------------------------------
//                **학교 유효성 검사** 
// -----------------------------------------------------

/* 학교명 유효성 검사 */
const memberSchool = document.querySelector("#memberSchool");
const schoolMessage = document.querySelector("#schoolMessage");

memberSchool.addEventListener("input", e => {

    const inputSchool = e.target.value;

    if (inputSchool.trim().length === 0) {
        schoolMessage.innerText = "학교명을 입력해 주세요(한글, 영어, 숫자, ·, 띄어쓰기)";
        schoolMessage.classList.remove("confirm", "error");
        checkObj.memberSchool = false;
        memberSchool.value = "";
        return;
    }

    const regExp = /^[가-힣a-zA-Z0-9·\s-]{2,40}$/;

    if (!regExp.test(inputSchool)) {
        schoolMessage.innerText = "학교명 형식이 올바르지 않습니다. (2~40자, 특수문자 제한)";
        schoolMessage.classList.add("error");
        schoolMessage.classList.remove("confirm");
        checkObj.memberSchool = false;
        return;
    }

    schoolMessage.innerText = "유효한 학교명입니다.";
    schoolMessage.classList.add("confirm");
    schoolMessage.classList.remove("error");
    checkObj.memberSchool = true;

});

// -----------------------------------------------------
//                      ** 주소 ** 
// -----------------------------------------------------

// 다음 주소 API
function execDaumPostcode() {
    new daum.Postcode({
        oncomplete: function (data) {
            // 팝업에서 검색결과 항목을 클릭했을때 실행할 코드를 작성하는 부분.

            // 각 주소의 노출 규칙에 따라 주소를 조합한다.
            // 내려오는 변수가 값이 없는 경우엔 공백('')값을 가지므로, 이를 참고하여 분기 한다.
            var addr = ''; // 주소 변수

            //사용자가 선택한 주소 타입에 따라 해당 주소 값을 가져온다.
            if (data.userSelectedType === 'R') { // 사용자가 도로명 주소를 선택했을 경우
                addr = data.roadAddress;
            } else { // 사용자가 지번 주소를 선택했을 경우(J)
                addr = data.jibunAddress;
            }

            // 우편번호와 주소 정보를 해당 필드에 넣는다.
            document.getElementById('postcode').value = data.zonecode;
            document.getElementById("address").value = addr;
            // 커서를 상세주소 필드로 이동한다.
            document.getElementById("detailAddress").focus();
        }
    }).open();
}

// 주소 검색 버튼 클릭 시
document.querySelector("#searchAddress")
.addEventListener("click", execDaumPostcode);


// -----------------------------------------------------
//  ** 회원 가입 버튼 클릭 시 전체 유효성 검사 여부 확인 ** 
// -----------------------------------------------------

const signUpForm = document.querySelector("#signUpForm");

// 회원 가입 폼 제출 시
signUpForm.addEventListener("submit", e => {

    // checkObj의 저장된 값(value) 중
    // 하나라도 false가 있으면 제출 X

    // for ~ in (객체 전용 향상된 for 문)
    for (let key in checkObj) { // checkObj 요소의 key 값을 순서대로 꺼내옴

        if (!checkObj[key]) { // 현재 접근중인 checkObj[key]의 value 값이 false 인 경우 (유효하지 않음)
            // checkObj["memberEmail"]
            let str; // 출력할 메시지를 저장할 변수

            switch (key) {
                case "memberEmail":
                    str = "이메일이 유효하지 않습니다"; break;

                case "authKey":
                    str = "이메일이 인증되지 않았습니다"; break;

                case "memberPw":
                    str = "비밀번호가 유효하지 않습니다"; break;

                case "memberPwConfirm":
                    str = "비밀번호가 일치하지 않습니다"; break;

                case "memberNickname":
                    str = "닉네임이 유효하지 않습니다"; break;

                case "memberTel":
                    str = "전화번호가 유효하지 않습니다"; break;

                case "memberSchool":
                    str = "학교명이 유효하지 않습니다"; break;
            }

            alert(str);

            document.getElementById(key).focus(); // 초점 이동

            e.preventDefault(); // form 태그 기본 이벤트(제출) 막기
            return;
        }
    }
});