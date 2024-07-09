package com.cheeus.member.repository;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.cheeus.member.domain.Member;

@Mapper
@Repository
public interface MemberDao {

	Integer existByEmail(String email);
	
	Integer createMember(Member member);
	
}
