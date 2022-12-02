package com.hospital_review.service;

import com.hospital_review.domain.User;
import com.hospital_review.domain.dto.UserDto;
import com.hospital_review.domain.dto.UserJoinRequest;
import com.hospital_review.exceptionManager.ErrorCode;
import com.hospital_review.exceptionManager.HospitalReviewAppException;
import com.hospital_review.repository.UserRepository;
import com.hospital_review.utils.JwtTokenUtil;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder encoder;

    @Value("${jwt.token.secret")
    private String secretKey;

    private final long expiredTimeMs = 1000 * 60 * 60;

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

    public String login(String userName, String password) {

        //userName 있는지 여부 확인
        //없으면 NOT_FOUND 에러 발생
        User user = userRepository.findByUserName(userName)
                .orElseThrow(() -> new HospitalReviewAppException(ErrorCode.NOT_FOUND, String.format("%s는 가입된 적이 없습니다.", userName)));

        //password 일치 하는지 여부 확인
        if(!encoder.matches(password, user.getPassword()))
            throw new HospitalReviewAppException(ErrorCode.INVALID_PASSWORD, String.format("id또는 password가 잘못 됐습니다."));

        return JwtTokenUtil.createToken(userName, secretKey, expiredTimeMs);
    }
}
