// --------------------------------------------
// 1. calender 관련 ------------------------------
// 오늘 날짜 생성
let today = new Date();
let year = today.getFullYear();
let month = today.getMonth()+1;

// tbody 변수 저장
let tbody = document.querySelector(".calender-table-body");

// 1-1. 캘린더 및 연월 생성 부분 -------------------------
callCalender();
callYearMonth();

// 1-2. 캘린더 왼쪽 오른쪽 클릭 부분 -------------------------
const rightBtn = document.querySelector("#calender-right-btn");
const leftBtn = document.querySelector("#calender-left-btn");

rightBtn.addEventListener("click", () => {
	today.setMonth(today.getMonth() + 1);
	year = today.getFullYear();
	month = today.getMonth() + 1;
	tbody.innerHTML = "";
	callCalender();
	callYearMonth();
});

leftBtn.addEventListener("click", () => {
	today.setMonth(today.getMonth() - 1);
	year = today.getFullYear();
	month = today.getMonth() + 1;
	tbody.innerHTML = "";
	callCalender();
	callYearMonth();
});

// function. 캘린더 상단 연월 출력 함수
function callYearMonth(){
	document.querySelector("#calender-year").innerText = String(year) + "년";
	document.querySelector("#calender-month").innerText = String(month) + "월";
};

// function. 캘린더 호출 함수
function callCalender(){

	// 현재 월 기준 첫째 날짜 및 마지막 날짜 구하기
	let firstDate = new Date(year, month - 1, 1).getDate(); // 1월이면 1
	let lastDate = new Date(year, month, 0).getDate(); // 1월이면 31
	 
	// 현재 월 기준 첫날, 마지막 날 요일 구하기
	let firstDay = new Date(year, month - 1, 1).getDay(); // 일(0) ~ 토(6)
	let lastDay = new Date(year, month, 0).getDay();  // 일(0) ~ 토(6)
	
	// js 에서 일요일은 0이므로 7로 바꿔주기
	if(firstDay == 0) firstDay = 7;
	if(lastDay == 0) lastDay = 7;

	// console.log(firstDate);
	// console.log(lastDate);
	// console.log(firstDay);
	// console.log(lastDay);
	
	// 시작 날짜 변수 저장
	let calDate = 1;

	// calender 표 생성
	// for문으로 날짜가 있는 요일만 클래스 구분 후 날짜 채우기
	for (let i = 1; i <= 6; i++){ // tr 생성 (총 6행)
		const tr = document.createElement("tr"); 
		tbody.append(tr);
		for(let j = 1; j <= 7; j++){ // tr 별로 td 생성
			const td = document.createElement("td");
			td.classList.add("tdEmpty");
			tr.append(td);
		}
	}

	// tr 배열 생성
	const trList = Array.from(tbody.children);
	// console.log(trRow);
	
	for (i = 0; i < trList.length; i++){ // tr은 무조건 6행까지만 생성되었으므로 총 6줄만 돌게 됨
		
		// td 배열 생성
		let tdList = Array.from(trList[i].children);
		
		if(i == 0){ // tr 이 첫행인 경우에 시작날짜 이용해 빈 날짜는 비워주기(목요일 시작인 경우 월화수 비우기)
			for(let j = firstDay; j <= 7; j++){
				tdList[j-1].classList.add("tdNormal");
				tdList[j-1].classList.remove("tdEmpty");
				tdList[j-1].innerText = calDate;
				calDate++;
			}
		}
		
		if(i > 0){ // tr 첫번째 줄을 제외한 나머지 줄 채워주기
			for(let j = 1; j <= 7; j++){
				tdList[j-1].classList.add("tdNormal");
				tdList[j-1].classList.remove("tdEmpty");
				tdList[j-1].innerText = calDate;
				calDate++;
				if(calDate > lastDate) break; // 만약 해당 월 마지막 날짜를 초과 시 break
			}
		}
		
		if(calDate > lastDate) break;
	}

	calenderSchedule();

};

function calenderSchedule(){
	// tbody 내 전체 td list 로 저장(총 42개)
	const allTdList = Array.from(tbody.querySelectorAll("td")); 
	
	for(let i = 0; i < allTdList.length; i++){
		
		// 모든 td 돌면서 공백인 td 는 지나치기
		if(allTdList[i].innerText == 0) continue;
		
		let tdDay = Number(allTdList[i].innerText);
		// console.log("tdday :" , tdDay);
		let compareDate = new Date(year, month, tdDay);
		
		console.log(calenderList);
		
		// 서버에서 넘어온 calenderList 돌기
		for(const schedule of calenderList){
			let startDate = new Date(schedule.startYear, schedule.startMonth - 1, schedule.startDay);
			// console.log(startDate);
			let endDate = new Date(schedule.endYear, schedule.endMonth - 1, schedule.endDay);
			
			// console.log(startDate);
			
			if(startDate.getFullYear() == year && startDate.getMonth() + 1 == month && startDate.getDate() == tdDay){
				
				console.log(startDate.getDate());
				const div = document.createElement("div")
				allTdList[i].append(div);
				div.classList.add("calender-td-content");
				div.innerText = schedule.calenderContent;
			}	
		}
		
	}
	
}

// --------------------------------------------
// 2. diary 관련 ------------------------------

// 오늘 날짜 출력
document.querySelector("#diary-today").innerText = year + '년 ' + month + '월 ' + today.getDate() + '일';





