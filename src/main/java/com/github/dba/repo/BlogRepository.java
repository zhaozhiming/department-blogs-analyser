package com.github.dba.repo;

import com.github.dba.model.Blog;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

@Repository
public class BlogRepository {
    private static final String MAIN_PERSISTENCE_UNIT = "mainPersistenceUnit";

    public void createBlog(Blog blog) {
        EntityManager entityManager = getEntityManager(MAIN_PERSISTENCE_UNIT);
        entityManager.persist(blog);
        entityManagerClose(entityManager);
    }

    private EntityManager getEntityManager(String persistenceUnit) {
        EntityManagerFactory factory = Persistence.createEntityManagerFactory(persistenceUnit);
        EntityManager entityManager = factory.createEntityManager();
        entityManager.getTransaction().begin();
        return entityManager;
    }

    private void entityManagerClose(EntityManager entityManager) {
        entityManager.getTransaction().commit();
        entityManager.close();
    }

}
