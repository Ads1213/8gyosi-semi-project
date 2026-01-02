package edu.kh.eightgyosi.common.filter;

import java.io.IOException;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class LoginFilter implements Filter{
	
	@Override
	public void doFilter(ServletRequest request, 
						 ServletResponse response, 
						 FilterChain chain)
			throws IOException, ServletException {
	
		HttpServletRequest req = (HttpServletRequest)request;
		HttpServletResponse resp = (HttpServletResponse)response;
		
		// 현재 들어온 요청 URI 가져온다
		String path = req.getRequestURI();
		
		if(path.startsWith("/myPage/")) { // myPage 이하 경로 접근 차단
			
			chain.doFilter(request, response); // 필터를 통과한 후 return
			return;	
		}
		
		HttpSession session = req.getSession();
		
		if(session.getAttribute("loginMember") == null ) {
			
			resp.sendRedirect("/"); // 로그인 되어 있지 않을 시 처리 경로 필요
			
		} else {
			
			chain.doFilter(request, response);
		}
		
	}

}
