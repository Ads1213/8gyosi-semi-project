package edu.kh.eightgyosi.common.config;

import java.util.Arrays;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import edu.kh.eightgyosi.common.filter.LoginFilter;

// 로그인된 회원만 접근할 수 있는 경로 지정하는 Filter Config
@Configuration
public class FilterConfig {

	@Bean
	public FilterRegistrationBean<LoginFilter> loginFilter(){
		
		FilterRegistrationBean<LoginFilter> filter = new FilterRegistrationBean<>();
		
		filter.setFilter(new LoginFilter());
		
		String[] filteringURL = {"/myPage/*"};
		
		filter.setUrlPatterns(Arrays.asList(filteringURL));
		
		filter.setName("loginFilteR");
		
		filter.setOrder(1);
		
		return filter;
	};
		
}
