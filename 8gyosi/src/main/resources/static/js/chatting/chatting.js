const addTarget = document.querySelector("#addTarget"); // 추가 버튼
const addTargetPopupLayer = document.querySelector("#addTargetPopupLayer"); // 팝업 레이어
const closeBtn = document.querySelector("#closeBtn"); // 닫기 버튼
const targetInput = document.querySelector("#targetInput"); // 사용자 검색
const resultArea = document.querySelector("#resultArea"); // 검색 결과
const chattingContent = document.querySelector(".chatting-content"); // 채팅방 영역
const prevMessage = document.querySelector(".prev-message"); // 채팅방 선택 전 메세지


let selectChattingNo; // 선택한 채팅방 번호
let selectTargetNo; // 현재 채팅 대상
let selectTargetName; // 대상의 이름
let selectTargetProfile; // 대상의 프로필


// 검색 팝업 레이어 열기
addTarget.addEventListener("click", e => {
	addTargetPopupLayer.classList.toggle("popup-layer-close");
	targetInput.focus();
});

// 검색 팝업 레이어  닫기
closeBtn.addEventListener("click", e => {
	addTargetPopupLayer.classList.toggle("popup-layer-close");
	resultArea.innerHTML = "";
});


// 사용자 검색(ajax)
targetInput.addEventListener("input", e => {

	const query = e.target.value.trim();

	// 입력된게 없을 때
	if(query.length == 0){
		resultArea.innerHTML = ""; // 이전 검색 결과 비우기
		return;
	}