package com.mbc.dto;

import com.mbc.entity.Item;
import com.mbc.entity.Order;
import com.mbc.entity.OrderItem;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class OrderItemDto {

    private String itemNm;

    private int count;

    private int orderPrice;

    // 결재 정보 DTO
    private String impUid;

    private String merchantUid;

    private String imgUrl;

    private Item item;  // Item 객체를 추가합니다.

    public OrderItemDto(OrderItem orderItem, String imgUrl) {
        this.item = orderItem.getItem(); // OrderItem에서 item을 가져와 저장
        this.itemNm = orderItem.getItem().getItemNm();
        this.count = orderItem.getCount();
        this.orderPrice = orderItem.getOrderPrice();
        this.impUid = orderItem.getImpUid();
        this.merchantUid = orderItem.getMerchantUid();
        this.imgUrl = imgUrl;
    }
}
