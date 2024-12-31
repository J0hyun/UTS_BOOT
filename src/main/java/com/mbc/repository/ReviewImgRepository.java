package com.mbc.repository;

import com.mbc.entity.ReviewImg;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewImgRepository extends JpaRepository<ReviewImg, Long> {
    List<ReviewImg> findByReviewIdOrderByIdAsc(Long id);
    ReviewImg findByReviewIdAndRepimgYn(Long reviewId, String repimgYn);
}
