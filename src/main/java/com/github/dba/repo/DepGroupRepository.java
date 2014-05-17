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

    public void createDepGroups(String groups) {
        EntityManager entityManager = getEntityManager(MAIN_PERSISTENCE_UNIT);

        entityManager.createNativeQuery("DELETE FROM dep_groups").executeUpdate();

        String[] groupNames = groups.split(",");
        for (String groupName : groupNames) {
            String[] texts = groupName.split("-");
            entityManager.persist(new DepGroup(texts[0], texts[1]));
        }

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
