package com.mbc.controller;

import com.mbc.dto.ReviewFormDto;
import com.mbc.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;


    // 리뷰 등록 폼 페이지 (GET)
    @GetMapping("/review")
    public String createReviewForm(Model model) {
        model.addAttribute("reviewFormDto", new ReviewFormDto());
        return "review/reviewForm";  // reviewForm.html 템플릿을 반환
    }

    // 리뷰 등록 (POST)
    @PostMapping("/review")
    public String createReview(ReviewFormDto reviewFormDto) {
        reviewService.saveReview(reviewFormDto);  // 서비스로 데이터 저장
        return "redirect:/review";  // 등록 후 /review 페이지로 리다이렉트
    }
}
