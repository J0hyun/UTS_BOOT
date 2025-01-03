package com.mbc.service;

import com.mbc.dto.ItemFormDto;
import com.mbc.dto.ItemImgDto;
import com.mbc.dto.ItemSearchDto;
import com.mbc.dto.MainItemDto;
import com.mbc.entity.Category;
import com.mbc.entity.Item;
import com.mbc.entity.ItemImg;
import com.mbc.repository.CategoryRepository;
import com.mbc.repository.ItemImgRepository;
import com.mbc.repository.ItemRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;
    private final ItemImgService itemImgService;
    private final ItemImgRepository itemImgRepository;
    private final CategoryRepository categoryRepository;

    public Long saveItem(ItemFormDto itemFormDto, List<MultipartFile> itemImgFileList)
        throws Exception{

        System.out.println("itemFormDto: " + itemFormDto.toString());

        //상품 등록
        Item item = itemFormDto.createItem();
        itemRepository.save(item);

        //이미지 등록
        for(int i=0; i<itemImgFileList.size(); i++){

            ItemImg itemImg = new ItemImg();
            itemImg.setItem(item);
            if(i==0)
                itemImg.setRepimgYn("Y");
            else
                itemImg.setRepimgYn("N");
            itemImgService.saveItemImg(itemImg, itemImgFileList.get(i));
        }

        return item.getId();

    }

    @Transactional(readOnly = true)
    public ItemFormDto getItemDtl(Long itemId) {

        // 상품 이미지 목록을 가져오기
        List<ItemImg> itemImgList = itemImgRepository.findByItemIdOrderByIdAsc(itemId);
        List<ItemImgDto> itemImgDtoList = new ArrayList<>();
        for (ItemImg itemImg : itemImgList) {
            ItemImgDto itemImgDto = ItemImgDto.of(itemImg);
            itemImgDtoList.add(itemImgDto);
        }

        // 아이템 정보 가져오기
        Item item = itemRepository.findById(itemId)
                .orElseThrow(EntityNotFoundException::new);

        // Item을 ItemFormDto로 변환
        ItemFormDto itemFormDto = ItemFormDto.of(item);

        // 카테고리 경로 설정
        itemFormDto.setCategoryHierarchy(item.getCategoryHierarchy());

        // 이미지 리스트 추가
        itemFormDto.setItemImgDtoList(itemImgDtoList);

        // 카테고리 조부모 ID 설정
        Long grandparentCategoryId = item.getGrandparentCategoryId();
        itemFormDto.setGrandparentCategoryId(grandparentCategoryId);

        return itemFormDto;
    }

    public Long updateItem(ItemFormDto itemFormDto, List<MultipartFile> itemImgFileList)
        throws Exception{

        //상품 수정
        Item item = itemRepository.findById(itemFormDto.getId())
                .orElseThrow(EntityNotFoundException::new);
        item.updateItem(itemFormDto);

        // 카테고리 설정 (categoryId로 Category 조회)
        Long categoryId = itemFormDto.getCategoryId();
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid category ID: " + categoryId));
        item.setCategory(category);  // 카테고리 설정
        System.out.println("itemFormDto: " + itemFormDto.toString());

        List<Long> itemImgIds = itemFormDto.getItemImgIds();

        // 디버깅: 리스트 크기 확인
        System.out.println("itemImgFileList size: " + itemImgFileList.size());
        System.out.println("itemImgIds size: " + itemImgIds.size());

        //이미지 등록
        for(int i=0; i<itemImgFileList.size(); i++){
            itemImgService.updateItemImg(itemImgIds.get(i),
                    itemImgFileList.get(i));
        }

        return item.getId();
    }

    @Transactional(readOnly = true)
    public Page<Item> getAdminItemPage(ItemSearchDto itemSearchDto,
                                       Pageable pageable) {
        return itemRepository.getAdminItemPage(itemSearchDto, pageable);
    }

    @Transactional(readOnly = true)
    public Page<MainItemDto> getMainItemPage(ItemSearchDto itemSearchDto,
                                             Pageable pageable) {
        return itemRepository.getMainItemPage(itemSearchDto, pageable);
    }

    @Transactional(readOnly = true)
    public Page<Item> getUserItems(String userEmail, ItemSearchDto itemSearchDto, Pageable pageable) {
        return itemRepository.findItemsByUserEmail(userEmail, itemSearchDto, pageable);
    }

    public void deleteItem(Long itemId) {
        itemRepository.deleteById(itemId);  // DB에서 아이템 삭제
    }

    // 상품 등록 갯수
    public Long getItemCount() {
        return itemRepository.count();
    }
}
