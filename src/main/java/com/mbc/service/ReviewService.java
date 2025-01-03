package com.mbc.service;


import com.mbc.dto.ReviewFormDto;
import com.mbc.entity.Item;
import com.mbc.entity.Review;
import com.mbc.repository.ItemRepository;
import com.mbc.repository.OrderItemRepository;
import com.mbc.repository.OrderRepository;
import com.mbc.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final ItemRepository itemRepository;
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;

    // 리뷰 등록 메서드
    public Review saveReview(ReviewFormDto reviewFormDto) {

        // 현재 로그인한 사용자 정보 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String loggedInUserName = authentication.getName(); // 로그인한 사용자의 이름을 가져옵니다.

        // itemNm을 기준으로 여러 상품을 찾을 수 있도록 처리
        List<Item> items = itemRepository.findByItemNm(reviewFormDto.getItemName());

        if (items.isEmpty()) {
            // 상품이 없는 경우 예외 처리
            throw new RuntimeException("상품을 찾을 수 없습니다.");
        } else if (items.size() > 1) {
            // 상품 이름이 중복된 경우, 중복 상품을 사용자에게 전달
            // 예를 들어, 상품 목록을 반환하거나 정확한 선택을 요청할 수 있도록 처리
            StringBuilder itemNames = new StringBuilder();
            for (Item item : items) {
                itemNames.append(item.getItemNm()).append(" ");
            }
            throw new RuntimeException("상품 이름이 중복됩니다. 중복된 상품 목록: " + itemNames.toString() + ". 정확한 상품을 선택해주세요.");
        }

        Item item = items.get(0); // 첫 번째 상품 선택

        // 동일한 사용자가 해당 상품에 대해 이미 리뷰를 작성했는지 확인
        List<Review> existingReviews = reviewRepository.findByItemAndMemberName(item, loggedInUserName);

        if (!existingReviews.isEmpty()) {
            // 이미 리뷰가 존재하는 경우 해당 리뷰를 반환하거나 업데이트
            return existingReviews.get(0);
        }

        // ReviewFormDto에서 Review 엔티티 생성
        Review review = new Review();
        review.setMemberName(loggedInUserName);  // 자동으로 로그인한 사용자명 설정
        review.setReviewDetail(reviewFormDto.getReviewDetail()); // 후기 내용 설정
        review.setItem(item); // 상품 설정

        // 리뷰를 저장
        review = reviewRepository.save(review);

        return review;
    }

    // 리뷰 등록 폼을 표시할 때 자동으로 로그인한 사용자명 설정
    public ReviewFormDto getReviewFormDto() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String loggedInUserName = authentication.getName();  // 로그인한 사용자의 이름을 가져옵니다.

        ReviewFormDto reviewFormDto = new ReviewFormDto();
        reviewFormDto.setMemberName(loggedInUserName);  // 로그인한 사용자명 자동 설정
        return reviewFormDto;
    }



    // 모든 리뷰 조회
    public List<Review> getAllReviews() {
        return reviewRepository.findAll(); // 모든 리뷰를 반환
    }
}