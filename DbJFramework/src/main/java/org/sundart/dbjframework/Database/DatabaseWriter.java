package org.sundart.dbjframework.Database;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class DatabaseWriter {
    private final JdbcTemplate jdbcTemplate;

    public DatabaseWriter(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void writeSql(String sql){
        jdbcTemplate.execute(sql);
    }
}
