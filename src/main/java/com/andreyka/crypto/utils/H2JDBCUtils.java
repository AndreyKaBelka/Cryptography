package com.andreyka.crypto.utils;

import lombok.experimental.UtilityClass;

import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Objects;
import java.util.Properties;

@UtilityClass
public class H2JDBCUtils {

    private static final String jdbcURL = "jdbc:h2:./data/database";
    private static final String jdbcUsername = "sa";
    private static final String jdbcPassword = "sa";
    private static final URL initDb = H2JDBCUtils.class.getResource("/initDb.sql");

    public Connection getConnection() {
        Connection connection = null;
        try {
            Properties info = getProperties();

            connection = DriverManager.getConnection(jdbcURL, info);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }

    private Properties getProperties() {
        Properties info = new Properties();
        info.setProperty("user", jdbcUsername);
        info.setProperty("password", jdbcPassword);
//        info.setProperty("CIPHER", "AES");
//        info.setProperty("FILE_LOCK", "SOCKET");
        String s = Objects.requireNonNull(initDb).getPath().replaceFirst("/", "");
        info.setProperty("INIT", "RUNSCRIPT FROM '%s'".formatted(s));


        return info;
    }
}
