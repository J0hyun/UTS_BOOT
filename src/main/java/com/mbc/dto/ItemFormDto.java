package com.mbc.dto;

import com.mbc.constant.ItemSellStatus;
import com.mbc.constant.ItemStatus;
import com.mbc.entity.Item;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.List;

@Getter @Setter
@ToString
public class ItemFormDto {

    private Long id;

    @NotBlank(message = "상품명은 필수 입력 값입니다.")
    private String itemNm;

    @NotNull(message = "가격은 필수 입력 값입니다.")
    private Integer price;

    @NotBlank(message = "상품설명은 필수 입력 값입니다.")
    private String itemDetail;

    @NotNull(message = "재고는 필수 입력 값입니다.")
    private Integer stockNumber;

    private ItemSellStatus itemSellStatus;

    private ItemStatus itemStatus;

    @NotNull(message = "카테고리는 필수 입력 값입니다.")
    private Long categoryId;

    // 카테고리 계층을 추가하는 필드
    private String categoryHierarchy;

    public String shipping; // 배송

    public Integer shippingPrice=0; //배송비

    // 직거래 가능 여부
    public String tradeAvailable; // "possible" 또는 "impossible"

    // 직거래 위치
    public String tradeLocation;  // 직거래 위치 정보



    private List<ItemImgDto> itemImgDtoList = new ArrayList<>();

    private List<Long> itemImgIds = new ArrayList<>();

    private static ModelMapper modelMapper = new ModelMapper();

    public Item createItem(){
        return modelMapper.map(this, Item.class);
    }

    public static ItemFormDto of(Item item) {
        ItemFormDto itemFormDto = modelMapper.map(item, ItemFormDto.class);
        // 카테고리 계층을 설정
        itemFormDto.setCategoryHierarchy(item.getCategoryHierarchy());
        return itemFormDto;
    }


    // itemStatus를 한글로 반환하는 메서드
    public String getItemStatusDescription() {
        return (itemStatus != null) ? itemStatus.getDescription() : "상품 상태 정보 없음";
    }

}
