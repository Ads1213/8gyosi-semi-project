package edu.kh.eightgyosi.common.interceptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import edu.kh.eightgyosi.member.model.dto.Member;
import edu.kh.eightgyosi.mypage.model.dto.FontDTO;
import edu.kh.eightgyosi.mypage.model.service.DiaryService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class QuotesFontInterceptor implements HandlerInterceptor {

    @Autowired
    private DiaryService service; 

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // preHandle은 보통 컨트롤러 실행 전 "권한 체크" 용도로 사용됩니다.
        // 여기서는 단순히 통과시키도록 true를 반환합니다.
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                           ModelAndView modelAndView) throws Exception {

        // 1. 뷰가 존재하고, 리다이렉트 요청이 아닐 때만 실행
        if (modelAndView != null && !modelAndView.getViewName().startsWith("redirect:")) {
            
            // 2. 세션에서 로그인 정보 가져오기
            HttpSession session = request.getSession(); 
            Member loginMember = (Member) session.getAttribute("loginMember");

            if (loginMember != null) {
                // 3. DB에서 유저의 글귀 및 폰트 설정 조회
                // service.selectFontConfig(int memberNo) 형태의 메서드가 있다고 가정합니다.
                FontDTO quotes = service.selectquotesFontList(loginMember.getMemberNo());
                
                // 4. 모델에 담기 (HTML에서 ${quotes}로 접근 가능)
                if (quotes != null) {
                    modelAndView.addObject("quotes", quotes);
                    log.info("마이페이지 인터셉터: 글귀 주입 완료 - {}", quotes.getQuotesContent());
                } else {
                    // 데이터가 없을 경우 기본값 생성해서 넘기기 (옵션)
                    FontDTO defaultFont = FontDTO.builder()
                            .quotesContent("오늘 하루도 화이팅!")
                            .fontSize(16)
                            .fontFamily("'Malgun Gothic', sans-serif")
                            .build();
                    modelAndView.addObject("quotes", defaultFont);
                }
            }
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws Exception {
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }
}