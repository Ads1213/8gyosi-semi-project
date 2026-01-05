package edu.kh.eightgyosi.common.interceptor;

import java.util.List;

import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

import org.springframework.web.servlet.HandlerInterceptor;

import edu.kh.eightgyosi.board.model.dto.Board;
import edu.kh.eightgyosi.board.model.service.EditBoardService;
import edu.kh.eightgyosi.member.model.dto.Member;

/**
 * BoardTypeInterceptor
 * - Application Scope에 게시판 리스트 캐싱
 * - 관리자 전용 게시판 접근 차단
 */
public class BoardTypeInterceptor implements HandlerInterceptor {

    private final EditBoardService service;

    // 생성자 주입
    public BoardTypeInterceptor(EditBoardService service) {
        this.service = service;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {

        ServletContext application = request.getServletContext();

        // 1️⃣ 게시판 리스트 캐싱
        if(application.getAttribute("boardTypeList") == null) {
            List<Board> boardList = service.getCategoryList();
            application.setAttribute("boardTypeList", boardList);
        }

        // 2️⃣ 관리자 전용 접근 체크
        Object loginObj = request.getSession().getAttribute("loginMember");
        if(loginObj instanceof Member member) {
            String uri = request.getRequestURI(); // 예: /editBoard/6/insert

            @SuppressWarnings("unchecked")
            List<Board> boardList = (List<Board>) application.getAttribute("boardTypeList");

            try {
                String[] parts = uri.split("/");
                if(parts.length > 2){
                    int boardTypeNo = Integer.parseInt(parts[2]);

                    // 관리자 전용 게시판인지 확인
                    if(service.isAdminOnlyCategory(boardTypeNo) && member.getRole() != Member.Role.ADMIN){
                        // 일반 회원이면 접근 차단
                        response.sendRedirect(request.getContextPath() + "/editBoard/" + boardTypeNo);
                        return false;
                    }
                }
            } catch(NumberFormatException e){
                // URI 숫자 변환 실패 시 무시
            }
        }

        return true; // 권한 체크 통과 시 컨트롤러 실행
    }
}
