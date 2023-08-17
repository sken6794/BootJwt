package com.coding404.jwt.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.coding404.jwt.command.UserVO;
import com.coding404.jwt.security.config.JWTService;

@RestController //rest 
public class APIController {
	/*
	//로그인하는 기능 가정해서 하나 만듦
	@PostMapping("/login")
	public ResponseEntity<String> login(@RequestBody UserVO vo) {
		
		//로그인 시도 -->
		//JWTService.createToken("회원아이디")
		String token = JWTService.createToken(vo.getUsername());
		
		return new ResponseEntity<>(token,HttpStatus.OK);
	}
	*/
	
	/*
	//사용자정보 확인
	//토큰을 헤더에 담아서 사용자 정보와 함께 요청
	@PostMapping("/api/v1/getInfo")
	public ResponseEntity<Object> getInfo(HttpServletRequest request){
		
		//헤더에 담긴 토큰
		String token =  request.getHeader("Authorization");
		
		//토큰의 무결성 검사
		try {
			boolean result =  JWTService.validateToken(token);
			System.out.println("토큰 무결성 결과 : "+result);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<Object>("토큰 위조",HttpStatus.UNAUTHORIZED);
		}
		
		
		return new ResponseEntity<>("getInfo() 데이터", HttpStatus.OK);
	}
	*/
	/////////////////////////JWT와 시큐리티 같이 쓰기//////////////////////
	
	//
	//@CrossOrigin()
	@GetMapping("/api/v1/hello")
	public String hello() {
		return "<h3>헬로</h3>";
	}
	
	//토큰 기반으로 사용자 요청 정보 반환 기능
	@PostMapping("/api/v1/getInfo")
	public ResponseEntity<Object> getInfo(){
		System.out.println("토큰이 있으면 호출됨(디비 연결 처리 알아서)");
		
		return new ResponseEntity<Object>("데이터 실리는 곳",HttpStatus.OK);
	}
	
	//회원가입이 비동기로 된다면?
	//
	@PostMapping("/join")
	public ResponseEntity<Object> join(){
		return new ResponseEntity<Object>("가입완료",HttpStatus.OK);
	}
}
