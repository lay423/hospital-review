package com.hospital_review.service;

import com.hospital_review.domain.dto.UserDto;
import com.hospital_review.domain.dto.UserJoinRequest;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    public UserDto join(UserJoinRequest request) {
        return new UserDto("","","");
    }
}
