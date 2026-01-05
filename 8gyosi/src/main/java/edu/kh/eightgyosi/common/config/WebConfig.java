package edu.kh.eightgyosi.common.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import edu.kh.eightgyosi.board.model.service.EditBoardService;
import edu.kh.eightgyosi.common.interceptor.BoardTypeInterceptor;

import lombok.RequiredArgsConstructor;

/**
 * WebConfig
 * - Interceptor 등록
 * - editBoard 관련 요청을 BoardTypeInterceptor로 가로채 권한 체크
 */
@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final EditBoardService editBoardService;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new BoardTypeInterceptor(editBoardService))
                .addPathPatterns("/editBoard/**") // editBoard 요청 인터셉트
                .excludePathPatterns("/css/**", "/js/**", "/images/**"); // 정적 리소스 제외
    }
}
