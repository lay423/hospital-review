package com.hospital_review.service;

import com.hospital_review.domain.User;
import com.hospital_review.domain.dto.UserDto;
import com.hospital_review.domain.dto.UserJoinRequest;
import com.hospital_review.exceptionManager.ErrorCode;
import com.hospital_review.exceptionManager.HospitalReviewAppException;
import com.hospital_review.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder encoder;

    public UserDto join(UserJoinRequest request) {
        // 비즈니스 로직 - 회원 가입
        // 회원 userName(id) 중복 Check
        // 중복이면 회원가입 x --> Exception(예외)발생
        // 있으면 에러처리
        userRepository.findByUserName(request.getUserName())
                .ifPresent(user ->{
                    throw new HospitalReviewAppException(ErrorCode.DUPLICATED_USER_NAME, String.format("UserName:%s", request.getUserName()));
                });

        // 회원가입 .save()
        User savedUser = userRepository.save(request.toEntity(encoder.encode(request.getPassword())));
        return UserDto.builder()
                .id(savedUser.getId())
                .userName(savedUser.getUserName())
                .email(savedUser.getEmailAddress())
                .build();
    }
}
