package com.mbc.dto;


import com.mbc.entity.Review;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.List;

@Getter @Setter
@ToString
public class ReviewFormDto {

    private Long id;

    private String memberName; //등록자명

    private String reviewDetail; //후기 내용

    private String itemName; // 상품명 추가

    private int rating; // 별점 추가 (1~5)


}
