package com.coding404.jwt.security.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import com.coding404.jwt.security.config.JWTService;

public class JwtAuthorizationFilter extends BasicAuthenticationFilter{

	
	//생성자
	public JwtAuthorizationFilter(AuthenticationManager authenticationManager) {
		super(authenticationManager);
	}
	
	
	//필터기능
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		System.out.println("===============JwtAuthorizationFilter====실행=======");
		
		//헤더에 담긴 토큰이 유효성을 확인하고, 인증된 토큰이면 우리서비스로 연결, 만료or위조 인 경우 error메세지 반환
		String headers = request.getHeader("Authorization");
		
		//헤더가 없거나 bearer로 시작하지 않으면
		if(headers==null || headers.startsWith("Bearer ") == false) {
			response.setContentType("text/plain; charset=UTF8");
			response.sendError(403,"토큰 없음");
			
			return; //함수종료 
		}
		//if문을 지나면 토큰이 있다는 거임
		
		//토큰의 유효성 검사
		try {
			String token = headers.substring(7);//Bearer 자르는 과정
			boolean result =  JWTService.validateToken(token);//토큰 검증
			if(result) { // result == true 면 정상 토큰
				chain.doFilter(request, response); // 컨트롤러로 연결됨
			} else { //토큰 만료됨
				response.setContentType("text/plain; charset=UTF8");
				response.sendError(403,"토큰 만료");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			//토큰이 위조 
			response.setContentType("text/plain; charset=UTF8");
			response.sendError(403,"토큰 위조");
		}
		
		//super.doFilterInternal(request, response, chain);
	}
	
	//
	
}
