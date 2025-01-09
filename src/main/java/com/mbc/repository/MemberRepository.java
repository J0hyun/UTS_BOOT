package com.mbc.repository;

import com.mbc.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Member findByEmail(String email);

    Member findByname(String name);

    boolean existsByEmail(String email);  // 추가

}
