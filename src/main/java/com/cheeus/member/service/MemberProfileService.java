package com.cheeus.member.service;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Random;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.cheeus.firebase.ImageUploadService;
import com.cheeus.member.domain.MemberPopularity;
import com.cheeus.member.domain.MemberProfile;
import com.cheeus.member.domain.MemberScrap;
import com.cheeus.member.exception.MemberException;
import com.cheeus.member.repository.MemberProfileDao;
import com.cheeus.member.request.LocationRequest;
import com.cheeus.member.response.MyInsertedPostResponse;

import lombok.RequiredArgsConstructor;

/////// 기능 ///////
//1. 닉네임 중복 확인 로직
//2. 회원 정보 로드 로직
//3. 회원 정보 수정 로직 (파이어베이스 + MySQL)
//*4. 회원탈퇴 로직 -- 아직 프론트와 연결하지 않음
//5. 위치 및 매칭 동의 업데이트 로직
//6. 좋아요 로직

@RequiredArgsConstructor
@Service
public class MemberProfileService {
	
	private final MemberProfileDao profileDao;
	private final ImageUploadService imageUploadService;
	
	@Value("${spring.email.secret}")
	private String emailSecret;
	
	
	// 닉네임 중복 확인
	public HttpStatus existNickname (String nickname) {
		
		Integer existNickname = profileDao.existNickname(nickname);
		
		if (existNickname > 0) {
			throw new MemberException("이미 존재하는 닉네임 입니다.", HttpStatus.BAD_REQUEST);
		}
		
		return HttpStatus.OK;
	}
	
	
	// 회원 정보 불러오기
	public MemberProfile findByEmail (String email) {
		
		MemberProfile findMember = profileDao.findByEmail(email);
		
		return findMember;
	}
	
	
	// 이메일 디코딩
	public String decrypt(String encryptedData, String iv) throws Exception {
		
		String base64EncryptedData = encryptedData.replace('-', '+').replace('_', '/');
		SecretKeySpec secretKey = new SecretKeySpec(emailSecret.getBytes(StandardCharsets.UTF_8), "AES");
        IvParameterSpec ivParameterSpec = new IvParameterSpec(Base64.getDecoder().decode(iv));

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, secretKey, ivParameterSpec);

        byte[] decodedValue = Base64.getDecoder().decode(base64EncryptedData);
        byte[] decryptedValue = cipher.doFinal(decodedValue);

        return new String(decryptedValue, StandardCharsets.UTF_8);
    }
	
	
	// 회원 수정
	@Transactional
	public MemberProfile updateMember (
			MemberProfile memberProfile,
			List<MultipartFile> photos,
			List<String> imageName) throws IOException {
		
		// 파이어베이스에 사진저장
		for(MultipartFile photo : photos) {
			if (!photo.getOriginalFilename().equals("emptyfile.txt")) {
				File tmp = imageUploadService.convertToFile( photo , "test" );
				String completeMsg = imageUploadService.uploadFile(
						tmp, 
						"profile/" + imageName.get(photos.indexOf(photo)),
						photo.getContentType() );
				
				System.out.println(completeMsg);
			}
		};
		
		// 정보 수정
		profileDao.updateMember(memberProfile);
		
		return memberProfile;
	}
	
	
	// 회원 탈퇴
	public HttpStatus deleteMember (String email) {
		
		MemberProfile findMember = findByEmail(email);
		
		if (findMember == null) {
			throw new MemberException("존재하지 않은 아이디입니다.", HttpStatus.BAD_REQUEST);
		}
		
		profileDao.deleteMember(email);
		
		return HttpStatus.OK;
	}
	
	
	//위치 및 매칭 동의
	public void allowLocationMatching(String email, String type, String latitude, String longitude) {
		if (type.equals("location")) profileDao.allowLocation(new LocationRequest(email, latitude, longitude));
		else profileDao.allowMatching(email);
	}
	
	
	
	// 좋아요 목록 불러오기
	public ArrayList<MemberPopularity> loadPopularity(String email) {
		
		ArrayList<MemberPopularity> popularities = profileDao.findPopularity(email);
		
		return popularities;
	}
	
	// 좋아요 개수 불러오기
	public Integer findPopularity (String email) {
		
		return profileDao.countPopularity(email);
	}
	
	
	// 내가 좋아요 눌렀는가
	public boolean isClickedPopularity (MemberPopularity popularity) {
		
		//liker는 현재 로그인 유저, 나.
		if (popularity.getLiker() == null) {
			throw new MemberException("로그인 해주세요.", HttpStatus.BAD_REQUEST);
		} else {
			
			Integer check = profileDao.existPopularity(popularity);
			
			if (check == 0) {
				// 좋아요 안눌름
				return false;
			}
			
			// 눌름
			return true;
		}
	}
	
	// 좋아요 추가/삭제
	public void addPopularity (MemberPopularity popularity) {
		
		if (!isClickedPopularity(popularity)) {
			// 좋아요가 없으면 좋아요 추가
			profileDao.addPopularity(popularity);
			
		} else {
			// 있으면 좋아요 삭제
			profileDao.deletePopularity(popularity);
		}
	}
	
	
	// 스크랩 목록 불러오기
	public ArrayList<MemberScrap> findMyScrap(String email) {
			
		return profileDao.findMyScrap(email);
	}
	
	// 내가 쓴글
	public ArrayList<MyInsertedPostResponse> findMyPost(String email) {
		
		return profileDao.findMyPost(email);
	}
	
	// 내가 스크랩 하였는가
	public boolean isScrapped (MemberScrap memberScrap) {
		
		if (memberScrap.getMemberEmail() == null) {
			
			throw new MemberException("로그인 해주세요.", HttpStatus.BAD_REQUEST);
		} else {
			Integer check = profileDao.isScrapped(memberScrap);
			
			if (check == 0) {
				return false;
			} else {
				return true;
			}
		}
		
	}

	private final ArrayList<String> zzim= new ArrayList<>() {
		private static final long serialVersionUID = 9999;
	{
		add("갈비찜");
		add("찜");
		add("김치찜");
		add("아구찜");
		add("살찜");
	}};
	// 스크랩 추가 및 삭제
	public ResponseEntity<?> addScrap (MemberScrap memberScrap) {
		
		try {
			if (isScrapped(memberScrap)) {
				profileDao.deleteScrap(memberScrap);
				
				return ResponseEntity.ok("삭제");
			} else {
				profileDao.addScrap(memberScrap);
				
				Random random = new Random();
				
				return ResponseEntity.ok(zzim.get(random.nextInt(5)));
			}
		} catch (Exception e) {
			
			throw e;
		}
	}
	

}
