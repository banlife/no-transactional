package banlife.cleaner;

import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

public class MySqlCleaner implements DatabaseCleanerStrategy {
    private final JdbcTemplate jdbcTemplate;

    public MySqlCleaner(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void clean() {
        jdbcTemplate.execute("SET FOREIGN_KEY_CHECKS=0");
        for (var tableName : findTableNames()) {
            jdbcTemplate.execute("DELETE FROM " + tableName);
        }
        jdbcTemplate.execute("SET FOREIGN_KEY_CHECKS=1");
    }

    private List<String> findTableNames() {
        return jdbcTemplate.queryForList("SHOW TABLES", String.class);
    }
}