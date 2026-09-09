package com.thermcampos.config;

import com.thermcampos.config.PropertiesLoadConfig;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

public class DbConfig {

    private PropertiesLoadConfig props;

    public DbConfig(PropertiesLoadConfig props) {
        this.props = props;
    }

    public HikariDataSource create() {
        var config = new HikariConfig();
        config.setJdbcUrl(props.get("order.db.url", "jdbc:postgresql://localhost:5432/order"));
        config.setUsername(props.get("order.db.user", "order"));
        config.setPassword(props.get("order.db.password", "order"));
        config.setMaximumPoolSize(props.getInt("order.db.max.pool.size", 10));
        config.setPoolName(props.get("order.db.pool.name", "linjava-pool"));
        return new HikariDataSource(config);
    }
}

