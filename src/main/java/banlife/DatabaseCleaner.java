package banlife;

import jakarta.persistence.EntityManager;
import org.springframework.context.ApplicationContext;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.List;

public abstract class DatabaseCleaner {
    public static void clear(ApplicationContext applicationContext) {
        var entityManager = applicationContext.getBean(EntityManager.class);
        var repositories = applicationContext.getBeansOfType(JpaRepository.class).values().stream().toList();
        var transactionTemplate = applicationContext.getBean(TransactionTemplate.class);

        transactionTemplate.execute(status -> {
            entityManager.clear();
            deleteAll(entityManager, repositories);
            return null;
        });
    }

    private static void deleteAll(EntityManager entityManager, List<JpaRepository> repositories) {
        entityManager.createNativeQuery("SET REFERENTIAL_INTEGRITY FALSE").executeUpdate();
        for (JpaRepository<?, ?> repository : repositories) {
            repository.deleteAllInBatch();
        }
        entityManager.createNativeQuery("SET REFERENTIAL_INTEGRITY TRUE").executeUpdate();
    }
}
