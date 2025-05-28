package banlife;

import banlife.cleaner.DatabaseType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.support.TransactionTemplate;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@Slf4j
public abstract class DatabaseCleaner {

    public static void clear(ApplicationContext applicationContext) {
        var jdbcTemplate = applicationContext.getBean(JdbcTemplate.class);
        var transactionTemplate = applicationContext.getBean(TransactionTemplate.class);
        var dataSource = applicationContext.getBean(DataSource.class);

        var databaseProductName = resolveDatabaseProductName(dataSource);
        var databaseType = DatabaseType.fromProductName(databaseProductName);
        var cleaner = databaseType.getCleaner(jdbcTemplate);

        transactionTemplate.execute(status -> {
            cleaner.clean();
            return null;
        });
    }

    private static String resolveDatabaseProductName(DataSource dataSource) {
        try (Connection connection = dataSource.getConnection()) {
            var databaseName = connection.getMetaData().getDatabaseProductName();
            log.debug("데이터베이스 제품명={}", databaseName);
            return databaseName;
        } catch (SQLException e) {
            throw new IllegalStateException("데이터베이스 제품명을 가져오는 데 실패했습니다.", e);
        }
    }
}