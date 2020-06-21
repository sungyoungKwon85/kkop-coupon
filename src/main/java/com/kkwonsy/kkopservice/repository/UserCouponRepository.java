package com.kkwonsy.kkopservice.repository;

import java.time.LocalDate;
import java.util.List;
import javax.persistence.EntityManager;

import org.springframework.stereotype.Repository;

import com.kkwonsy.kkopservice.domain.Coupon;
import com.kkwonsy.kkopservice.model.response.v1.UserCouponExpiry;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Repository
public class UserCouponRepository {

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

    public List<UserCouponExpiry> getUserCouponExpiryWithin3Days() {
        return em.createQuery(
            "select new com.kkwonsy.kkopservice.model.response.v1.UserCouponExpiry(" +
                "u.name, u.email, c.code, c.expiryDate)" +
                " from Coupon c" +
                " join fetch UserCoupon uc" +
                " on c.id = uc.coupon.id" +
                " join fetch User u" +
                " on uc.user.id = u.id" +
                " where c.expiryDate between :startDate and :endDate"
            , UserCouponExpiry.class)
            .setParameter("startDate", LocalDate.now())
            .setParameter("endDate", LocalDate.now().plusDays(3))
            .getResultList();
    }

}
