package com.mbc.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "review")
@Getter
@Setter
@ToString
public class Review extends BaseEntity {

    @Id
    @Column(name = "review_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String memberName; //등록자명

    private String reviewDetail; //후기 내용


    // 기존의 member와 관계가 설정되어 있는지 확인
    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;  // 또는 Member 엔티티에서 email을 가져올 수 있도록


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item; // 해당 상품 (상품에 대한 외래 키)


    @OneToMany(mappedBy = "review", cascade = CascadeType.ALL)
    private List<ReviewImg> reviewImgs;




}
