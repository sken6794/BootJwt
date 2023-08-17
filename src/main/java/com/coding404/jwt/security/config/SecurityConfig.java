package com.coding404.jwt.security.config;

import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.coding404.jwt.security.filter.CustomLoginFilter;
import com.coding404.jwt.security.filter.JwtAuthorizationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	// 비밀번호 암호화객체
	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

		// 기본로그인 방식, 세션, 베이직인증, csrf토큰 전부 사용하지 않는다.
		http.csrf().disable();
		// form 기반 로그인을 사용하지 않는다.
		http.formLogin().disable();
		// Authorization : 아이디 형식으로 넘어오는 basic인증을 사용하지 않는다.
		http.httpBasic().disable();
		// 세션인증 기반을 사용하지 않고, JWT 사용해서 인증 할 것.
		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		// 모든 요청에 대해 전부 허용
		http.authorizeHttpRequests(auth -> auth.anyRequest().permitAll());

		// 1. 크로스 오리진 필터 생성 cors 개념 모르겠으면 다시 보기
		http.cors(Customizer.withDefaults());

		// 2. 필터체이닝 연습
		// http.addFilter(new FilterOne());

		// http.addFilterBefore(new FilterOne(), 시큐리티의 필터타입)
		// http.addFilterBefore(new FilterOne(),
		// UsernamePasswordAuthenticationFilter.class);
		// http.addFilterBefore(new FilterTwo(), FilterOne.class);
		// http.addFilterAfter(new FilterTwo(), FilterOne.class);

		// 3. 로그인 시도에 AuthenticationManager가 필요합니다
		// AuthenticationManager authenticationManager =
		// http.getSharedObject(AuthenticationManager.class);
		// ++ UserDetailsService 객체, PasswordEncoder가 반드시 필요하다.
		AuthenticationManager authenticationManager = authenticationManager(
				http.getSharedObject(AuthenticationConfiguration.class));

		System.out.println(authenticationManager);

		// 4. 로그인 필터를 등록
		http.addFilter(new CustomLoginFilter(authenticationManager));

		//5. jwt검증 필터를 등록 ( 기능 확인했고 요청 분기별로 하는거 할 때 주석 처리 했음 )
		// 둘 다 가능한 방법
		//http.addFilter(new JwtAuthorizationFilter(authenticationManager));
		//http.addFilterBefore(new JwtAuthorizationFilter(authenticationManager), BasicAuthenticationFilter.class);
		
		//6. 요청별로 필터 실행 
		// login 요청에만 CustomLoginFilter가 실행됨
		http.requestMatchers().antMatchers("/login")
							  .and()
							  .addFilter(new CustomLoginFilter(authenticationManager));
		
		//api로 시작하는 요청에만 jwt필터가 실행됨 
		http.requestMatchers()
			.antMatchers("/api/v1/**")
			.antMatchers("/api/v2/**")
			.and()
			.addFilter(new JwtAuthorizationFilter(authenticationManager));
			
						
						
							
		
		
		return http.build();
	}

	// 로그인 시도에 필요한 AuthenticationManager객체
	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
			throws Exception {
		return authenticationConfiguration.getAuthenticationManager();
	}

	// 크로스오리진 필터
	@Bean
	CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration configuration = new CorsConfiguration();
		// 어느 요청에 대해서 허용을 할 것인가??
		configuration.setAllowedOrigins(Arrays.asList("*"));
		// 어느 메소드를 열어 줄 것인가??
		configuration.setAllowedMethods(Arrays.asList("*"));
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		// 어느 요청에 대해서 적용할 것인가??
		source.registerCorsConfiguration("/**", configuration);
		return source;
	}

}
