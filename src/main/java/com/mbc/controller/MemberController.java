package com.mbc.controller;

import com.mbc.dto.ItemSearchDto;
import com.mbc.dto.OrderHistDto;
import com.mbc.entity.Item;
import com.mbc.entity.Member;
import com.mbc.service.ItemService;
import com.mbc.service.MemberService;
import com.mbc.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.time.Duration;
import java.time.LocalDateTime;

@Controller
@RequiredArgsConstructor
@Log4j2
public class MemberController {

    private final MemberService memberService;
    private final ItemService itemService;
    private final OrderService orderService;

    @GetMapping(value = "/member/mystore") // 내 상점 페이지
    public String Mystore(ItemSearchDto itemSearchDto,
                          Model model, Principal principal) {

        // 상점 정보 - 등록 날짜
        Member member = memberService.getMemberByUserName(principal.getName());
        LocalDateTime startdate = member.getRegTime();
        LocalDateTime enddate = LocalDateTime.now();
        Duration duration = Duration.between(startdate, enddate);
        Long openDay = duration.getSeconds()/60/60/24;
        model.addAttribute("openDay", openDay);

        // 상품 등록 총 갯수
        Long totalItem = itemService.getItemCount();
        model.addAttribute("totalItem", totalItem);

        // 판매내역
        String userEmail = principal.getName();
        Pageable pageable = PageRequest.of(0, 10000);
        Page<Item> items = itemService.getUserItems(userEmail, itemSearchDto, pageable);
        model.addAttribute("userName", userEmail);
        model.addAttribute("items", items);
        model.addAttribute("itemSearchDto", itemSearchDto);
        // 구매내역
        Page<OrderHistDto> orderHistDtoList = orderService.getOrderList(userEmail, pageable);
        model.addAttribute("orders", orderHistDtoList);

        return "member/mystore";
    }

    @GetMapping(value = "/store/{memberId}")
    public String store(@PathVariable Long memberId, ItemSearchDto itemSearchDto,
                        Model model, RedirectAttributes rttr) {
        Member member;
        String userEmail;
        Long openDay;

        try {
            member = memberService.getStoreMember(memberId);
            // 상점 이름 가져오기
            userEmail = member.getName();
            // 상점 정보 - 등록 날짜
            LocalDateTime startdate = member.getRegTime();
            LocalDateTime enddate = LocalDateTime.now();
            Duration duration = Duration.between(startdate, enddate);
            openDay = duration.getSeconds()/60/60/24;
            model.addAttribute("openDay", openDay);

            // 상품 등록 총 갯수
            Long totalItem = itemService.getItemCount();
            model.addAttribute("totalItem", totalItem);

        } catch (IllegalArgumentException e) {
            // 예외 발생 시 메시지 전달
            rttr.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/";
        }

        // 판매내역
        Pageable pageable = PageRequest.of(0, 10000);
        Page<Item> items = itemService.getUserItems(userEmail, itemSearchDto, pageable);
        model.addAttribute("userName", userEmail);
        model.addAttribute("items", items);
        model.addAttribute("itemSearchDto", itemSearchDto);

        // 구매내역
        Page<OrderHistDto> orderHistDtoList = orderService.getOrderList(userEmail, pageable);
        model.addAttribute("orders", orderHistDtoList);

        return "member/mystore";
    }
}
