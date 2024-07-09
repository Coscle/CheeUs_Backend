package com.cheeus.member.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cheeus.member.domain.Member;
import com.cheeus.member.response.SignInResponse;
import com.cheeus.member.response.SignUpResponse;
import com.cheeus.member.service.MemberService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {
	
	private final MemberService service;
	
	@GetMapping
	public String test() {
		return "Hello Member!";
	}
	
	@GetMapping("/exist")
	public ResponseEntity<?> existByEmail(@RequestParam("email") String email) {
		System.out.println("email 중복 요청 : " + email);
		
		HttpStatus status = service.existByEmail(email);
		return new ResponseEntity<>(status);
	}
	
//	@PostMapping("/signIn")
//	public ResponseEntity<SignUpResponse> signIn(@RequestBody Member member) {
//		System.out.println("회원 가입 요청 : " + member);
//		
//		return ResponseEntity.ok(service.signUp(member));
//	}
	
	@PostMapping("/signIn")
	public ResponseEntity<SignInResponse> signIn(@RequestBody Member member) {
		System.out.println("로그인 요청 : " + member);
		
		return ResponseEntity.ok(service.signIn(member));
	}
	
	
	@PostMapping("/signUp")
	public ResponseEntity<SignUpResponse> signUp(@RequestBody Member member) {
		System.out.println("회원 가입 요청 : " + member);
		
		return ResponseEntity.ok(service.signUp(member));
	}
	
}
