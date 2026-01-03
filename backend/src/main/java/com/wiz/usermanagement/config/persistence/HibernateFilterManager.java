package com.wiz.usermanagement.config.persistence;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class HibernateFilterManager {

    private final EntityManager entityManager;

    public void enableNotDeletedFilter() {
        Session session = entityManager.unwrap(Session.class);
        session.enableFilter("deletedFilter")
                .setParameter("isDeleted", false);
    }

    public void enableDeletedFilter() {
        Session session = entityManager.unwrap(Session.class);
        session.enableFilter("deletedFilter")
                .setParameter("isDeleted", true);
    }

    public void disableDeletedFilter() {
        Session session = entityManager.unwrap(Session.class);
        session.disableFilter("deletedFilter");
    }
}
