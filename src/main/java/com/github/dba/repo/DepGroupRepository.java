package com.github.dba.repo;

import com.github.dba.model.DepGroup;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

@Repository
public class DepGroupRepository {
    private static final String MAIN_PERSISTENCE_UNIT = "mainPersistenceUnit";
    private static final String QUERY_PERSISTENCE_UNIT = "queryPersistenceUnit";

    public void createDepGroups() {
        EntityManager entityManager = getEntityManager(MAIN_PERSISTENCE_UNIT);

        entityManager.createNativeQuery("DELETE FROM dep_groups").executeUpdate();
        entityManager.persist(new DepGroup("访问安全组"));
        entityManager.persist(new DepGroup("流程管理组"));
        entityManager.persist(new DepGroup("文件管理组"));
        entityManager.persist(new DepGroup("公共支持组"));

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
