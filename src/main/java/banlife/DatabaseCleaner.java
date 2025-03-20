package banlife;

import jakarta.persistence.EntityManager;
import org.springframework.context.ApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.List;

public abstract class DatabaseCleaner {
    public static void clear(ApplicationContext applicationContext) {
        var entityManager = applicationContext.getBean(EntityManager.class);
        var jdbcTemplate = applicationContext.getBean(JdbcTemplate.class);
        var transactionTemplate = applicationContext.getBean(TransactionTemplate.class);

        transactionTemplate.execute(status -> {
            entityManager.clear();
            deleteAll(jdbcTemplate, entityManager);
            return null;
        });
    }

    private static void deleteAll(JdbcTemplate jdbcTemplate, EntityManager entityManager) {
        entityManager.createNativeQuery("SET REFERENTIAL_INTEGRITY FALSE").executeUpdate();
        dropAllIndexes(jdbcTemplate);
        for (String tableName : findDatabaseTableNames(jdbcTemplate)) {
            entityManager.createNativeQuery("DELETE FROM %s".formatted(tableName)).executeUpdate();
        }
        entityManager.createNativeQuery("SET REFERENTIAL_INTEGRITY TRUE").executeUpdate();
    }

    private static void dropAllIndexes(JdbcTemplate jdbcTemplate) {
        List<String> indexNames = jdbcTemplate.queryForList(
            "SELECT INDEX_NAME FROM INFORMATION_SCHEMA.INDEXES",
            String.class
        );
        
        for (String indexName : indexNames) {
            if (!indexName.equals("PRIMARY_KEY")) {
                jdbcTemplate.execute("DROP INDEX IF EXISTS " + indexName);
            }
        }
    }

    private static List<String> findDatabaseTableNames(JdbcTemplate jdbcTemplate) {
        return jdbcTemplate
                .query("SHOW TABLES", (rs, rowNum) -> rs.getString(1))
                .stream().toList();
    }
}
