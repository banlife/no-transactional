package banlife.cleaner;

import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

public class H2Cleaner implements DatabaseCleanerStrategy {
    private final JdbcTemplate jdbcTemplate;

    public H2Cleaner(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void clean() {
        jdbcTemplate.execute("SET REFERENTIAL_INTEGRITY FALSE");
        for (var tableName : findTableNames()) {
            jdbcTemplate.execute("DELETE FROM " + tableName);
        }
        jdbcTemplate.execute("SET REFERENTIAL_INTEGRITY TRUE");
    }

    private List<String> findTableNames() {
        return jdbcTemplate.query("SHOW TABLES", (rs, rowNum) -> rs.getString(1));
    }
}