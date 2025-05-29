package banlife.cleaner;

import org.springframework.jdbc.core.JdbcTemplate;

import java.util.Locale;

public enum DatabaseType {

    H2 {
        @Override
        public boolean matches(String productName) {
            return "H2".equalsIgnoreCase(productName);
        }

        @Override
        public DatabaseCleanerStrategy getCleaner(JdbcTemplate jdbcTemplate) {
            return new H2Cleaner(jdbcTemplate);
        }
    },

    MYSQL {
        @Override
        public boolean matches(String productName) {
            return "MySQL".equalsIgnoreCase(productName);
        }

        @Override
        public DatabaseCleanerStrategy getCleaner(JdbcTemplate jdbcTemplate) {
            return new MySqlCleaner(jdbcTemplate);
        }
    };

    public abstract boolean matches(String productName);

    public abstract DatabaseCleanerStrategy getCleaner(JdbcTemplate jdbcTemplate);

    public static DatabaseType fromProductName(String productName) {
        var normalized = productName.toLowerCase(Locale.ROOT);
        for (var type : values()) {
            if (type.matches(normalized)) {
                return type;
            }
        }
        throw new UnsupportedOperationException("지원하지 않는 데이터베이스입니다: " + productName);
    }
}