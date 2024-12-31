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

    @NotNull(message = "재고수량은 필수 입력 값입니다.")
    private Integer stockNumber;

    private ItemSellStatus itemSellStatus = ItemSellStatus.SELL;

    @NotNull(message = "상품상태는 필수 입력 값입니다.")
    private ItemStatus itemStatus;

    @NotNull(message = "카테고리는 필수 입력 값입니다.")
    private Long categoryId;

    @NotNull(message = "배송비 설정은 필수 입력 값입니다.")
    public String shipping; // 배송

    public Integer shippingPrice=0; //배송비

    @NotNull(message = "직거래 가능여부는 필수 입력 값입니다.")
    public String tradeAvailable; // "possible" 또는 "impossible"

    // 직거래 위치
    public String tradeLocation;  // 직거래 위치 정보

    private String userName; // 상품을 등록한 유저의 이름

    private List<ItemImgDto> itemImgDtoList = new ArrayList<>();

    private List<Long> itemImgIds = new ArrayList<>();

    private static ModelMapper modelMapper = new ModelMapper();

    public Item createItem(){
        return modelMapper.map(this, Item.class);
    }

    public static ItemFormDto of(Item item){
        ItemFormDto dto = modelMapper.map(item, ItemFormDto.class);
        // Item 엔티티에서 유저의 이름을 가져와 설정 (예: item.getCreatedBy().getName()으로 이름을 가져오기)
        dto.setUserName(item.getCreatedBy()); // item.getCreatedBy()가 Member 엔티티로 연결되어 있다고 가정
        return dto;
    }

}
