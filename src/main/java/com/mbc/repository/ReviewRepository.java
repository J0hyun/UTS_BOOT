package com.mbc.repository;

import com.mbc.entity.Item;
import com.mbc.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    public List<Review> findByItemAndMemberName(Item item, String memberName);
}
