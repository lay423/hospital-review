package com.hospital_review.controller;

import com.hospital_review.domain.dto.ReviewCreateRequest;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/reviews")
public class ReviewController {

    @PostMapping
    public String write(@RequestBody ReviewCreateRequest dto) {
        return "리뷰 등록에 성공 했습니다.";
    }
}
