package com.mbc.controller;

import com.mbc.dto.ItemDto;
import com.mbc.dto.ItemSearchDto;
import com.mbc.dto.MemberFormDto;
import com.mbc.entity.Item;
import com.mbc.entity.Member;
import com.mbc.service.ItemService;
import com.mbc.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
@RequestMapping("/member")
@Log4j2
public class MemberController {

    private final ItemService itemService;

    @GetMapping(value = "/mystore") // 내 상점 페이지
    public String myStore(ItemSearchDto itemSearchDto,
                          Model model, Principal principal) {
        // 판매내역
        String userEmail = principal.getName();
        Pageable pageable = PageRequest.of(0, 10000);
        Page<Item> items = itemService.getUserItems(userEmail, itemSearchDto, pageable);
        model.addAttribute("items", items);
        model.addAttribute("itemSearchDto", itemSearchDto);
        model.addAttribute("maxPage", 5);

        return "/member/mystore";
    }
}
