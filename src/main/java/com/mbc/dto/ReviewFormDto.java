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

    private List<ReviewImgDto> reviewImgDtoList = new ArrayList<>();

    private static ModelMapper modelMapper = new ModelMapper();

    public static ReviewFormDto of(Review review) {
        ReviewFormDto reviewFormDto = modelMapper.map(review, ReviewFormDto.class);
        reviewFormDto.setReviewDetail(review.getReviewDetail());
        return reviewFormDto;
    }
}
