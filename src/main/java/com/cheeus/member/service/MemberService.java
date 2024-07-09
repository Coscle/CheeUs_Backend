package com.cheeus.member.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.cheeus.member.domain.Member;
import com.cheeus.member.exception.MemberException;
import com.cheeus.member.repository.MemberDao;
import com.cheeus.member.response.SignInResponse;
import com.cheeus.member.response.SignUpResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberService {
	
	private final MemberDao dao;

	// 가입 시 이미 존재하는 회원인지 확인
	public HttpStatus existByEmail(String email) {
		
		Integer existMember = dao.existByEmail(email);
		if (existMember > 0) {
			throw new MemberException("이미 존재하는 이메일입니다.", HttpStatus.BAD_REQUEST);
		}
		return HttpStatus.OK;
	}
	
	public SignUpResponse signUp(Member member) {
		//이메일 중복 확인
		//existByEmail(member.getEmail());
		
		Integer existMember = dao.existByEmail(member.getEmail());
		if (existMember == 0) {
			
			dao.createMember(member);
			return new SignUpResponse(member.getEmail());
			
		} else {
			
			return new SignUpResponse(null);
			
		}
		
	}
	
	public SignInResponse signIn(Member member) {
//		authenticate(req.getId(), req.getPwd());

//		final UserDetails userDetails = userDetailsService.loadUserByUsername(req.getId());
//		final String token = jwtTokenUtil.generateToken(userDetails);

//		System.out.println("인증 성공 토큰 출력 : " + token);
		System.out.println("이메일 출력 : " + member.getEmail());

		// 회원정보 존재 확인
		Integer existMember = dao.existByEmail(member.getEmail());
		if (existMember == 0) {
			return new SignInResponse(null, null);
		}
		
		return new SignInResponse(null, member.getEmail());
	}
	
}
