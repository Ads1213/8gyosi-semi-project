// 쿠키에 저장된 이메일 input 창에 뿌려놓기

// 쿠키에서 매개변수로 전달받은 key가 일치하는 value 얻어오는 함수
const getCookie = (key) => {

  const cookies = document.cookie; // "K=V; K=V; ...."

  // console.log(cookies); // saveId=ads1213@kh.or.kr; testKey=testValue

  // cookies 문자열을 배열 형태로 변환
  // split : 문자열을 배열로 만들어 줌

  const cookieList = cookies.split("; ") // ["K=V", "K=V"...]
        .map( el => el.split("=")); // ["K", "V"]...

  // console.log(cookieList);

  // ['saveId', 'ads1213@gmail.co.kr'],
  // ['testKey', 'testValue']

  // 

}


// 이메일 작성 input 태그 요소
const loginEmail = document.querySelector("#loginForm input[name='memberEmail']")

if(loginEmail != null) { // 로그인창의 이메일 input 태그가 화면상에 존재할 때
                        // 즉, 로그인이 안 되어 있는 화면일 때

  // 쿠키 중 key 값이 "saveId"인 쿠키의 value 얻어오기
  const saveId = getCookie("saveId");



}