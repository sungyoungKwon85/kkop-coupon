package com.kkwonsy.kkopservice.repository;

import java.util.List;
import javax.persistence.EntityManager;

import org.springframework.stereotype.Repository;

import com.kkwonsy.kkopservice.domain.Coupon;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Repository
public class CouponRepository {

    private final EntityManager em;

    public List<Coupon> findAllWithUserId(Long userId) {
        return em.createQuery(
            "select c from Coupon c" +
                " join fetch UserCoupon uc" +
                " on c.id = uc.coupon.id" +
                " where uc.user.id = :userId"
            , Coupon.class)
            .setParameter("userId", userId)
            .getResultList();
    }

}
