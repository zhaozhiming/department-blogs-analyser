package com.github.dba.repo;

import com.github.dba.model.Blog;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

@Repository
public class BlogRepository {
    private static final String MAIN_PERSISTENCE_UNIT = "mainPersistenceUnit";
    private static final String QUERY_PERSISTENCE_UNIT = "queryPersistenceUnit";

    public void createBlog(Blog blog) {
        EntityManager entityManager = getEntityManager(MAIN_PERSISTENCE_UNIT);
        entityManager.persist(blog);
        entityManagerClose(entityManager);
    }

    public boolean isBlogExist(String website, String blogId) {
        EntityManager entityManager = getEntityManager(QUERY_PERSISTENCE_UNIT);
        Query query = entityManager.createQuery(
                "select b from " + Blog.class.getName() + " b where b.blogId = ? and b.website = ? ")
                .setParameter(1, blogId).setParameter(2, website);
        try {
            query.getSingleResult();
            return true;
        } catch (Exception e) {
            return false;
        } finally {
            entityManagerClose(entityManager);
        }
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
