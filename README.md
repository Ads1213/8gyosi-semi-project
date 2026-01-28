# 🏫 8gyosi (팔교시)
> **중고등학생 커뮤니티**

## 1. 프로젝트 소개
- 방과 후 학생들이 학교 정보와 시험 노하우를 자유롭게 공유하고 의견을 교환할 수 있는 커뮤니티입니다.

## 2. 개발 기간
- 2025.12.08(월) ~ 2025.01.09(금)

## 3. 기술 스택
- **언어:** Java 21, HTML5, CSS3, JavaScript, SQL
- **프레임워크:** Spring Boot, Mybatis, Thymeleaf
- **데이터베이스:** Oracle DB

## 4. 내가 담당한 핵심 기능
### 🔐 회원 인증 및 관리
- **BCrypt**를 활용한 비밀번호 암호화 및 안전한 로그인 구현
- **Fetch API**와 **SMTP**를 연동한 이메일 인증 회원가입
- **MultipartFile**과 **DataTransfer**를 이용한 프로필 이미지 업로드/복구 로직

## 5. 핵심 트러블 슈팅
- **문제:** 파일 업로드 시 취소 버튼을 누르면 기존 파일 정보가 소실됨
- **해결:** `DataTransfer` 인터페이스를 활용하여 이전 파일 객체를 유지하도록 예외 처리 구현

https://github.com/Ads1213/8gyosi-semi-project/blob/main/git-image.jpg
