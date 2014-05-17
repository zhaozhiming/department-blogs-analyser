package com.github.dba.repo;

import com.github.dba.model.Author;
import com.github.dba.model.Blog;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

@Repository
public class BlogRepository {
    private static final Log log = LogFactory.getLog(BlogRepository.class);
    public static final String MAIN_PERSISTENCE_UNIT = "mainPersistenceUnit";
    public static final String QUERY_PERSISTENCE_UNIT = "queryPersistenceUnit";

    public void createBlog(Blog blog) {
        EntityManager entityManager = getEntityManager(MAIN_PERSISTENCE_UNIT);
        entityManager.persist(blog);
        entityManagerClose(entityManager);
    }

    public Blog queryBlogBy(String website, String blogId) {
        EntityManager entityManager = getEntityManager(QUERY_PERSISTENCE_UNIT);
        Query query = entityManager.createQuery(
                "select b from " + Blog.class.getName() + " b where b.blogId = ? and b.website = ? ")
                .setParameter(1, blogId).setParameter(2, website);
        try {
            return (Blog)query.getSingleResult();
        } catch (Exception e) {
            log.debug(String.format("blog with %s and %s is not not found", website, blogId));
            return null;
        } finally {
            entityManagerClose(entityManager);
        }
    }

    public void updateBlog(Long id, String title, int view, int comment, Author author) {
        EntityManager entityManager = getEntityManager(MAIN_PERSISTENCE_UNIT);

        log.debug(String.format("blog id:%d", id));
        Blog blog = entityManager.find(Blog.class, id);
        log.debug(String.format("blog:%s", blog));

        blog.setTitle(title);
        blog.setView(view);
        blog.setComment(comment);
        blog.setAuthor(author);

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
