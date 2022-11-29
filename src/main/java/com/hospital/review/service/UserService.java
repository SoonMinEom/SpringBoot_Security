package com.hospital.review.service;

import com.hospital.review.domain.User;
import com.hospital.review.domain.dto.UserDto;
import com.hospital.review.domain.dto.UserJoinRequest;
import com.hospital.review.exception.ErrorCode;
import com.hospital.review.exception.HospitalReviewAppException;
import com.hospital.review.repository.UserRepository;
import com.hospital.review.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder encoder;

    @Value("${jwt.token.secret")
    private  String secretKey;

    private long expireTime = 1000 * 60 * 60; //1시간

    public UserDto join(UserJoinRequest request) {
//        // 검색한 유저 네임이 없을 때.
//        userRepository.findByUserName(request.getUserName())
//                .orElseThrow(()->new RuntimeException("해당 UserName이 존재하지 않습니다."));

        //중복체크 = 검색한 유저 네임이 있을 때.
        userRepository.findByUserName(request.getUserName())
                .ifPresent(user-> {
                    throw new HospitalReviewAppException(ErrorCode.DUPLICATED_USER_NAME,ErrorCode.DUPLICATED_USER_NAME.getMessage());
                });
        //회원가입
        User savedUser = userRepository.save(request.toEntity(encoder.encode(request.getPassword())));
        return UserDto.builder()
                .id(savedUser.getId())
                .userName(savedUser.getUserName())
                .email(savedUser.getEmailAddress())
                .build();
    }

    public String login(String userName, String password) {
        // 유저네임있는지 확인(없으면 NOT_FOUND에러)
        User user = userRepository.findByUserName(userName)
                .orElseThrow(()->new HospitalReviewAppException(ErrorCode.NOT_FOUND,String.format("%s는 존재하지 않는 아이디 입니다.", userName)));

        // 패스워드 일치하는지 확인
        if(!encoder.matches(password, user.getPassword())) {
            throw new HospitalReviewAppException(ErrorCode.INVALID_PASSWORD,"ID또는 PASSWORD가 잘못 되었습니다.");
        };

        // 두가지 확인 성공하면 토큰 발행
        return JwtUtil.createToken(userName,secretKey,expireTime);
    }
}
