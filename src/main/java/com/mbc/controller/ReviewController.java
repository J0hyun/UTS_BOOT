package com.mbc.controller;

import com.mbc.dto.ReviewFormDto;
import com.mbc.entity.*;
import com.mbc.repository.ItemRepository;
import com.mbc.repository.OrderItemRepository;
import com.mbc.repository.OrderRepository;
import com.mbc.service.ReviewImgService;
import com.mbc.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;
    private final ItemRepository itemRepository;
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final ReviewImgService reviewImgService;



    @GetMapping("/review/{itemId}")
    public String createReviewForm(@PathVariable Long itemId, Model model, RedirectAttributes redirectAttributes) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String loggedInUserName = authentication.getName();

        // 사용자가 주문한 상품을 가져오기
        List<OrderItem> orderItems = orderItemRepository.findByOrder_MemberName(loggedInUserName);

        // 해당 itemId가 주문 항목에 포함되어 있는지 확인
        OrderItem orderItem = orderItems.stream()
                .filter(item -> item.getItem().getId().equals(itemId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("이 상품에 대한 주문 내역이 없습니다."));

        Item item = orderItem.getItem();

        // 이미 리뷰가 있는지 확인
        List<Review> existingReviews = reviewService.getReviewsByItemAndMemberName(item.getId(), loggedInUserName);
        boolean hasAlreadyReviewed = !existingReviews.isEmpty(); // 리뷰가 존재하는지 여부

        if (hasAlreadyReviewed) {
            // 이미 리뷰가 있는 경우 알림 메시지 추가 후 'member/mystore'로 리다이렉트
            redirectAttributes.addFlashAttribute("errorMessage", "이미 리뷰를 작성하셨습니다.");
            return "redirect:/member/mystore";
        }

        // ReviewFormDto 생성
        ReviewFormDto reviewFormDto = reviewService.getReviewFormDto();
        reviewFormDto.setItemName(item.getItemNm()); // 상품명 자동 입력

        // 모델에 reviewFormDto, item, hasAlreadyReviewed 등을 전달
        model.addAttribute("reviewFormDto", reviewFormDto);
        model.addAttribute("item", item);
        model.addAttribute("hasAlreadyReviewed", hasAlreadyReviewed); // 리뷰 여부에 따라 true/false 전달

        return "review/reviewForm"; // 리뷰 작성 페이지로 이동
    }




    @PostMapping("/review")
    public String createReview(ReviewFormDto reviewFormDto, @RequestParam("reviewImgs") List<MultipartFile> reviewImgs,
                               RedirectAttributes redirectAttributes) throws Exception {
        // 리뷰 저장 (리뷰와 관련된 이미지 저장 후 리뷰 등록)
        Review review = reviewService.saveReview(reviewFormDto); // 리뷰 객체를 먼저 저장하고 받아옴

        // 리뷰 이미지 처리
        List<ReviewImg> reviewImgList = new ArrayList<>();
        reviewImgService.saveReviewImgs(reviewImgList, reviewImgs, review);  // Review 객체를 전달하여 이미지 저장

        // 리뷰 작성 후 알림 메시지를 redirectAttributes에 추가
        redirectAttributes.addFlashAttribute("message", "리뷰가 정상적으로 작성되었습니다.");

        return "redirect:/member/mystore";  // 등록 후 /member/mystore 페이지로 리다이렉트
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
