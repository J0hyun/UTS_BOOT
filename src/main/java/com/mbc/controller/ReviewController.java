package com.mbc.controller;

import com.mbc.dto.ReviewFormDto;
import com.mbc.entity.Item;
import com.mbc.entity.Order;
import com.mbc.entity.OrderItem;
import com.mbc.entity.Review;
import com.mbc.repository.ItemRepository;
import com.mbc.repository.OrderItemRepository;
import com.mbc.repository.OrderRepository;
import com.mbc.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;
    private final ItemRepository itemRepository;
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;


    // 리뷰 등록 폼 페이지 (GET)
    @GetMapping("/review")
    public String createReviewForm(Model model) {
        // 상품 목록을 가져와서 선택할 수 있도록 모델에 추가 (상품 이름)
        List<Item> items = itemRepository.findAll();  // 또는 원하는 조건으로 상품을 조회
        model.addAttribute("items", items);

        // ReviewFormDto를 가져오되, 현재 로그인한 사용자 이름을 설정하여 전달
        ReviewFormDto reviewFormDto = reviewService.getReviewFormDto(); // 로그인한 사용자명 자동 설정
        model.addAttribute("reviewFormDto", reviewFormDto);

        return "review/reviewForm";  // reviewForm.html 템플릿을 반환
    }

    @GetMapping("/review/{itemId}")
    public String createReviewForm(@PathVariable Long itemId, Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String loggedInUserName = authentication.getName();

        // 사용자가 주문한 상품을 가져오기
        List<OrderItem> orderItems = orderItemRepository.findByOrder_MemberName(loggedInUserName); // OrderItemRepository에서 사용자가 주문한 상품들 가져오기

        // 해당 itemId가 주문 항목에 포함되어 있는지 확인
        OrderItem orderItem = orderItems.stream()
                .filter(item -> item.getItem().getId().equals(itemId)) // itemId가 일치하는 주문 항목 찾기
                .findFirst()
                .orElseThrow(() -> new RuntimeException("이 상품에 대한 주문 내역이 없습니다."));

        // 주문 항목에서 Item 객체를 가져오기
        Item item = orderItem.getItem(); // OrderItem에 포함된 Item을 가져옵니다.

        // ReviewFormDto에 상품명 넣기
        ReviewFormDto reviewFormDto = reviewService.getReviewFormDto(); // 로그인한 사용자명 자동 설정
        reviewFormDto.setItemName(item.getItemNm()); // 상품명 자동 입력

        model.addAttribute("reviewFormDto", reviewFormDto);
        model.addAttribute("item", item);

        return "review/reviewForm";
    }




    // 리뷰 등록 (POST)
    @PostMapping("/review")
    public String createReview(ReviewFormDto reviewFormDto) {
        reviewService.saveReview(reviewFormDto);  // 서비스로 데이터 저장
        return "redirect:/review";  // 등록 후 /review 페이지로 리다이렉트
    }

    // 모든 리뷰 조회 (GET)
    @GetMapping("/reviewDtl")
    public String viewAllReviews(Model model) {
        // 모든 리뷰를 조회
        List<Review> reviews = reviewService.getAllReviews();

        // 모델에 모든 리뷰를 전달
        model.addAttribute("reviews", reviews);

        // 리뷰 목록을 보여주는 화면으로 이동
        return "review/reviewList";  // reviewList.html로 이동 (모든 리뷰 목록을 보여줌)
    }

}
