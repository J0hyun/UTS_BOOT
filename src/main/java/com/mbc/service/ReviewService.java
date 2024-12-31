package com.mbc.service;


import com.mbc.dto.ReviewFormDto;
import com.mbc.dto.ReviewImgDto;
import com.mbc.entity.Review;
import com.mbc.entity.ReviewImg;
import com.mbc.repository.ReviewImgRepository;
import com.mbc.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final ReviewImgRepository reviewImgRepository;

    // 리뷰 등록 메서드
    public Review saveReview(ReviewFormDto reviewFormDto) {
        // ReviewFormDto에서 Review 엔티티 생성
        Review review = new Review();
        review.setMemberName(reviewFormDto.getMemberName());  // 등록자
        review.setReviewDetail(reviewFormDto.getReviewDetail()); // 후기

        // 리뷰를 저장
        review = reviewRepository.save(review);

        // 리뷰에 이미지 추가 (리스트로 저장)
        if (reviewFormDto.getReviewImgDtoList() != null) {
            for (ReviewImgDto reviewImgDto : reviewFormDto.getReviewImgDtoList()) {
                ReviewImg reviewImg = new ReviewImg();
                reviewImg.setImgUrl(reviewImgDto.getImgUrl()); // 이미지 URL 설정
                reviewImg.setReview(review); // 해당 리뷰와 연결

                reviewImgRepository.save(reviewImg); // 이미지 저장
            }
        }
        return review;
    }
}
