package com.andreyka.crypto.database;

import com.andreyka.crypto.utils.H2JDBCUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;

import java.sql.Connection;
import java.sql.SQLException;

public abstract class Repository<T> {
    private final static QueryRunner run = new QueryRunner();

    protected abstract ResultSetHandler<T> getHandler();

    protected T query(final String sql, final Object... values) {
        T res = null;
        try (Connection connection = H2JDBCUtils.getConnection()) {
            res = run.query(connection, sql, getHandler(), values);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return res;
    }

    protected void execute(final String sql, final Object... params) {
        try (Connection connection = H2JDBCUtils.getConnection()) {
            run.execute(connection, sql, params);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
