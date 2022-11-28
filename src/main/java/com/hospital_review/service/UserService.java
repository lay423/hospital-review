package com.hospital_review.service;

import com.hospital_review.domain.User;
import com.hospital_review.domain.dto.UserDto;
import com.hospital_review.domain.dto.UserJoinRequest;
import com.hospital_review.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public UserDto join(UserJoinRequest request) {

        userRepository.findByUserName(request.getUserName())
                .ifPresent(user -> new RuntimeException("해당 UserName이 중복 됩니다."));
        User savedUser = userRepository.save(request.toEntity());

        return UserDto.builder()
                .id(savedUser.getId())
                .userName(savedUser.getUserName())
                .email(savedUser.getEmailAddress())
                .build();
    }
}
