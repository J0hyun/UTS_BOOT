package com.mbc.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "review_img")
@Getter
@Setter
public class ReviewImg extends BaseEntity {
    @Id
    @Column(name = "review_img_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String imgName;     // 이미지 파일 이름

    private String oriImgName;  // 원본 이미지 파일 이름

    private String imgUrl;      // 이미지 파일 URL

    private String repimgYn;  // 대표이미지

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "review_id")
    private Review review; // 해당 리뷰 (리뷰에 대한 외래 키)

    // 리뷰 이미지 정보 갱신 메서드
    public void updateReviewImg(String oriImgName, String imgName, String imgUrl) {
        this.oriImgName = oriImgName;
        this.imgName = imgName;
        this.imgUrl = imgUrl;
    }
}
