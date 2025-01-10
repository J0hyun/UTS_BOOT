package com.mbc.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "payment")
public class Payment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="payment_id")
    private Long id;

    @Column(nullable = false)
    private String imp_uid;  // 아임포트 결재 uid

    @Column(nullable = false)
    private String merchant_uid;  // 주문번호

    @Column(nullable = false)
    private Long amount; // 결제 금액

    @Column(nullable = false)
    private String status; // 결제 상태 (e.g., paid, cancelled)

    @Column(nullable = false)
    private String paymentMethod; // 결제 방법 (CARD, BANK, VBANK 등)
}
