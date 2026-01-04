package com.wiz.usermanagement.aspect;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.hibernate.Session;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
public class SoftDeleteAspect {

    private final EntityManager entityManager;

    @Before("execution(* com.wiz.usermanagement.service.impl.UserServiceImpl.getAllUsers(..)) || " +
            "execution(* com.wiz.usermanagement.service.impl.UserServiceImpl.getUserById(..)) || " +
            "execution(* com.wiz.usermanagement.service.impl.UserServiceImpl.updateUser(..)) || " +
            "execution(* com.wiz.usermanagement.service.impl.UserServiceImpl.deleteUser(..))")
    public void enableDeletedFilter() {
        Session session = entityManager.unwrap(Session.class);
        session.enableFilter("deletedUserFilter").setParameter("isDeleted", false);
    }
}
