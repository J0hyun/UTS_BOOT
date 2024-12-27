package com.mbc.controller;

import com.mbc.dto.ItemFormDto;
import com.mbc.dto.ItemSearchDto;
import com.mbc.entity.Category;
import com.mbc.entity.Item;
import com.mbc.service.CategoryService;
import com.mbc.service.ItemService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Controller
@Log4j2
public class ItemController {

    private final ItemService itemService;
    private final CategoryService categoryService;

    public ItemController(ItemService itemService, CategoryService categoryService) {
        this.itemService = itemService;
        this.categoryService = categoryService;
    }

    @GetMapping(value = "/member/item/new")
    public String itemForm(Model model) {
        // 최상위 카테고리 목록을 가져와서 모델에 추가
        List<Category> parentCategories = categoryService.getParentCategories();
        model.addAttribute("itemFormDto", new ItemFormDto());
        model.addAttribute("parentCategories", parentCategories);

        return "/item/itemForm";
    }

    @GetMapping(value = "/member/item/subCategories")
    @ResponseBody
    public List<Category> getSubCategories(@RequestParam Long parentId) {
        // 선택한 부모 카테고리의 하위 카테고리 목록을 반환
        return categoryService.getSubCategories(parentId);
    }

    @PostMapping(value = "/member/item/new")
    public String itemNew(@Valid ItemFormDto itemFormDto, BindingResult bindingResult,
                          Model model, @RequestParam("itemImgFile")List<MultipartFile> itemImgFileList) {
        if (bindingResult.hasErrors()) {
            return "/item/itemForm";
        }

        if(itemImgFileList.get(0).isEmpty() && itemFormDto.getId() == null){
            model.addAttribute("errorMessage", "첫번쨰 상품 이미지는 필수 입력 값 입니다.");
            return "/item/itemForm";
        }

        if (itemImgFileList.size() > 12) {
            model.addAttribute("errorMessage", "상품 이미지는 최대 12개까지만 등록 가능합니다.");
            return "/item/itemForm";
        }

        try{
            itemService.saveItem(itemFormDto, itemImgFileList);
        } catch (Exception e) {
            model.addAttribute("errorMessage", "상품 등록 중 에러가 발생하였습니다.");
            return "/item/itemForm";
        }

        return "redirect:/";
    }

    @GetMapping(value = "/member/item/{itemId}")
    public String itemDtl(@PathVariable("itemId") Long itemId, Model model) {

        try{
            ItemFormDto itemFormDto = itemService.getItemDtl(itemId);
            model.addAttribute("itemFormDto", itemFormDto);
        } catch (EntityNotFoundException e) {
            model.addAttribute("errorMessage", "존재하지 않는 상품입니다.");
            model.addAttribute("itemFormDto", new ItemFormDto());
            return "/item/itemForm";
        }

        return "/item/itemForm";
    }

    @PostMapping(value = "/member/item/{itemId}")
    public String itemUpdate(@Valid ItemFormDto itemFormDto,
                             BindingResult bindingResult, @RequestParam("itemImgFile")
                             List<MultipartFile> itemImgFileList, Model model) {

        if(bindingResult.hasErrors()) {
            return "/item/itemForm";
        }

        if(itemImgFileList.get(0).isEmpty()){
            model.addAttribute("errorMessage", "첫번째 상품 이미지는 필수 입력 값입니다.");
            return "/item/itemForm";
        }

        try {
            itemService.updateItem(itemFormDto, itemImgFileList);
        } catch (Exception e) {
            model.addAttribute("errorMessage", "상품 수정 중 에러가 발생하였습니다.");
            return "/item/itemForm";
        }

        return "redirect:/";
    }

    @GetMapping(value = {"/member/items", "/member/items/{page}"})
    public String itemManage(ItemSearchDto itemSearchDto,
  @PathVariable("page") Optional<Integer> page, Model model){
        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0,5);
        Page<Item> items = itemService.getAdminItemPage(itemSearchDto, pageable);
        model.addAttribute("items", items);
        model.addAttribute("itemSearchDto", itemSearchDto);
        model.addAttribute("maxPage", 5);
        return "item/itemMng";
    }

    @GetMapping(value = "/item/{itemId}")
    public String itemDtl(Model model, @PathVariable("itemId") Long itemId) {
        ItemFormDto itemFormDto = itemService.getItemDtl(itemId);
        model.addAttribute("item", itemFormDto);
        return "item/itemDtl";
    }

 }
