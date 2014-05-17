package com.github.dba.repo;

import com.github.dba.model.DepGroup;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

@Repository
public class DepGroupRepository {
    private static final Log log = LogFactory.getLog(DepGroupRepository.class);
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

    public String findGroupFullNameByShort(String groupShort) {
        EntityManager entityManager = getEntityManager(QUERY_PERSISTENCE_UNIT);
        Query query = entityManager.createQuery(
                "select d from " + DepGroup.class.getName() + " d where d.groupShort = ?")
                .setParameter(1, groupShort.toUpperCase());
        try {
            DepGroup group = (DepGroup) query.getSingleResult();
            return group.getName();
        } catch (Exception e) {
            log.debug(String.format("can't find group by short: %s", groupShort));
            return "unknown";
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
