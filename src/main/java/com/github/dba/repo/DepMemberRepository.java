package com.github.dba.repo;

import com.github.dba.model.DepMember;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

import static com.github.dba.repo.BlogRepository.MAIN_PERSISTENCE_UNIT;
import static com.github.dba.repo.BlogRepository.QUERY_PERSISTENCE_UNIT;

@Repository
public class DepMemberRepository {
    private static final Log log = LogFactory.getLog(DepMemberRepository.class);

    public void createDepMembers() {
        EntityManager entityManager = getEntityManager(MAIN_PERSISTENCE_UNIT);
        entityManager.createNativeQuery("DELETE FROM dep_members").executeUpdate();

        entityManager.persist(new DepMember("ZZM", "赵芝明"));
        entityManager.persist(new DepMember("WSL", "王苏龙"));
        entityManager.persist(new DepMember("FCH", "傅采慧"));
        entityManager.persist(new DepMember("SY", "宋裕"));
        entityManager.persist(new DepMember("GYY", "郭杨勇"));
        entityManager.persist(new DepMember("WZJ", "魏中佳"));
        entityManager.persist(new DepMember("LDP", "兰东平"));
        entityManager.persist(new DepMember("WJ", "刘杰"));

        entityManagerClose(entityManager);
    }

    public String findMemberFullNameByShort(String memberShort) {
        EntityManager entityManager = getEntityManager(QUERY_PERSISTENCE_UNIT);
        Query query = entityManager.createQuery(
                "select d from " + DepMember.class.getName() + " d where d.memberShort = ?")
                .setParameter(1, memberShort.toUpperCase());
        try {
            DepMember member = (DepMember) query.getSingleResult();
            return member.getName();
        } catch (Exception e) {
            log.debug(String.format("can't find member by short: %s", memberShort));
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
