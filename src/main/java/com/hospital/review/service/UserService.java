package com.hospital.review.service;

import com.hospital.review.domain.User;
import com.hospital.review.domain.dto.UserDto;
import com.hospital.review.domain.dto.UserJoinRequest;
import com.hospital.review.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public UserDto join(UserJoinRequest request) {
//        // 검색한 유저 네임이 없을 때.
//        userRepository.findByUserName(request.getUserName())
//                .orElseThrow(()->new RuntimeException("해당 UserName이 존재하지 않습니다."));

        //중복체크 = 검색한 유저 네임이 있을 때.
        userRepository.findByUserName(request.getUserName())
                .ifPresent(user->new RuntimeException("이미 존재하는 UserName 입니다."));
        //회원가입
        User savedUser = userRepository.save(request.toEntity());
        return UserDto.builder()
                .id(savedUser.getId())
                .userName(savedUser.getUserName())
                .email(savedUser.getEmailAddress())
                .build();
    }
}
